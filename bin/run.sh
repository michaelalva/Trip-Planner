#!/bin/bash

umask 077;

# add preconfigured module on CS-dept systems if present
source /etc/profile.d/modules.sh
module purge
module load courses/cs314
if [[ $? != 0 ]]; then
  echo "The '$0' script appears to have been invoked on a non-CS-dept machine."
  echo "You may be using an unsupported configuration, which is OK, but you may"
  echo "need to refer to the detail in LOCAL-SETUP.md and the guide wiki."
  echo
fi

export SERVER_PORT=$((0+${CS314_SERVER_BASE:-41300}+${CS314_TEAM:-15}))
export CLIENT_PORT=$((0+${CS314_CLIENT_BASE:-41300}+${CS314_TEAM:-15}))
echo Using client/server ports: $CLIENT_PORT / $SERVER_PORT

if [[ -z "$REVISION" ]]; then
  REVISION=local
fi

check_error() {
  if [ "$1" -ne 0 ]; then
    echo "Failed at step: ${FUNCNAME[1]}"
    exit "$1"
  fi
}

realpath() {
    [[ $1 = /* ]] && echo "$1" || echo "$PWD/${1#./}";
}

function get_repo_root_dir {
  dir="$(realpath $1)";
  while [[ ! -d "$dir/.git" ]];
  do
    dir=`echo $dir | sed 's~\(.*\)/.*~\1~'`;
  done;

  export REPO_ROOT=$dir;
}

function get_nfs_mounted {
  OS=$(uname);
  if [[ "$OS" == "Darwin" ]]; then
    return;
  fi

  file_system="$(stat -f -L -c %T ${REPO_ROOT})";
  if [[ "$file_system" == "nfs" ]]; then
    export NFS_MOUNTED=1;
  fi
}

function get_default_prefix {
  if [[ -n "$NFS_MOUNTED" ]]; then
    export BUILD_DIRECTORY_PREFIX="/tmp/cs314-$(whoami)"
  else
    export BUILD_DIRECTORY_PREFIX="${REPO_ROOT}"
  fi
}

get_repo_root_dir $0;
get_nfs_mounted;
get_default_prefix;

function copy_client_bundle_to_build_dir {
  echo $REPO_ROOT
  echo $BUILD_DIRECTORY_PREFIX
  mkdir -p ${BUILD_DIRECTORY_PREFIX}/client
  mkdir -p ${BUILD_DIRECTORY_PREFIX}/bin
  cp -r ${REPO_ROOT}/client/dist ${BUILD_DIRECTORY_PREFIX}/client
}

function install_server_dependencies {
  if [[ -n "${NFS_MOUNTED}" ]]; then
    pushd ${REPO_ROOT}/server > /dev/null
    function _install_server_dependences {
      if [[ ! -e .m2/repository ]]; then
        mkdir -p ${BUILD_DIRECTORY_PREFIX}/server/.m2/repository
        mvn --global-settings .m2/settings.xml \
          -Dmaven.repo.local=${BUILD_DIRECTORY_PREFIX}/server/.m2/repository \
          -Drevision=${REVISION} \
          -Dbuild.directory.prefix=${BUILD_DIRECTORY_PREFIX} \
          install 2>&1 | grep '^Download'
        ln -sf ${BUILD_DIRECTORY_PREFIX}/server/.m2/repository .m2/repository
      fi
    }
    _install_server_dependences
    check_error $?
    popd > /dev/null
  else
    if [[ ! -d "${REPO_ROOT}/server/.m2/repository" ]]; then
      mvn -f ${REPO_ROOT}/server \
        --global-settings ${REPO_ROOT}/server/.m2/settings.xml \
        -Drevision=${REVISION} \
        -Dbuild.directory.prefix=${BUILD_DIRECTORY_PREFIX} \
        install 2>&1 | grep '^Download'
      check_error $?
    fi
  fi
}

function build_server {
  mvn -f ${REPO_ROOT}/server --global-settings ${REPO_ROOT}/server/.m2/settings.xml -Drevision=${REVISION} -Dbuild.directory.prefix=${BUILD_DIRECTORY_PREFIX} package $@
  check_error $?
}

function run_server {
  delayopen &
  java -Dorg.slf4j.simpleLogger.log.com.tco=info -jar $BUILD_DIRECTORY_PREFIX/target/server-local.jar ${SERVER_PORT}
  check_error $?
}

function arch_dependent_echo {
  case "$ARCH" in 
    "x86_64")
      echo "$1" ;;
    "arm64" | "aarch64")
      echo "$2" ;;
    *)
      >&2 echo "Unknown architecture $(uname -m). Please alert a TA about this issue." 
      return 2 ;;
  esac
}

function download_newman {

  CURL_COMMAND="$(command -v curl 2> /dev/null)"
  WGET_COMMAND="$(command -v wget 2> /dev/null)"
  if [ -n "$CURL_COMMAND" ]; then
    curl -o "$1" --progress-bar "$(arch_dependent_echo "$NEWMAN_BINARIES_LOCATION/$NEWMAN_X86_64" "$NEWMAN_BINARIES_LOCATION/$NEWMAN_ARM64")"
  elif [ -n "$WGET_COMMAND" ]; then
    wget -qO "$1" --show-progress "$(arch_dependent_echo "$NEWMAN_BINARIES_LOCATION/$NEWMAN_X86_64" "$NEWMAN_BINARIES_LOCATION/$NEWMAN_ARM64")"
  else
    echo "Either the curl or wget command is required to download newman for running Postman tests!"
    return 1
  fi

}

function verify_newman {
  echo "$(arch_dependent_echo "$NEWMAN_X86_64_SHA256SUM" "$NEWMAN_ARM64_SHA256SUM")  $1" | sha256sum -c --quiet - 
}

function local_newman_healthy {
  [ -f "$LOCAL_NEWMAN" ] && verify_newman "$LOCAL_NEWMAN"  > /dev/null 2>&1
}

function set_newman {
  ARCH="$(uname -m)"

  NEWMAN_BINARIES_LOCATION="https://cs.colostate.edu/~cs314/content/binaries/newman"
  NEWMAN_X86_64="newmancomp-x86_64"
  NEWMAN_X86_64_SHA256SUM="180890c9eb62362c0b0fa6af7da1060c7185394fb35496b73482ab7e6bfb3792"
  NEWMAN_ARM64="newmancomp-arm64"
  NEWMAN_ARM64_SHA256SUM="f72ed2c63236afcdd06c1484eed07d03279682bde4085f26dea6c5c62b88ec46"
  LOCAL_NEWMAN="$BUILD_DIRECTORY_PREFIX/bin/newman"
  GLOBAL_NEWMAN="$(command -v newman 2> /dev/null)"
  if ! local_newman_healthy && [ -z "$GLOBAL_NEWMAN" ]; then
    echo "Download of newman needed..."
    if ! download_newman "$LOCAL_NEWMAN"; then
      rm -f "$LOCAL_NEWMAN"
      >&2 echo "NEWMAN DOWNLOAD FAILED!"
      >&2 echo "Exiting..."
      exit 2
    fi
    verify_newman "$LOCAL_NEWMAN"
    chmod +x "$LOCAL_NEWMAN"
  fi

  NEWMAN_BIN="${GLOBAL_NEWMAN:-$LOCAL_NEWMAN}"
}

function postman_tests {
  set_newman
  #Count Postman Collections
  count=`ls -1 ${REPO_ROOT}/Postman/*.json 2>/dev/null | wc -l`

  if [[ $count == 0 ]]; then
    echo =============================
    echo Postman Collections not found
    echo =============================
  else
    BASE_URL="http://localhost:"${SERVER_PORT}

    echo Starting server-local.jar on ${BASE_URL}
    echo "Port ${SERVER_PORT} status before starting sever (1 normal): " `nc -z localhost ${SERVER_PORT} ; echo $?`

    java -Dorg.slf4j.simpleLogger.log.com.tco=error -jar ${BUILD_DIRECTORY_PREFIX}/target/server-local.jar ${SERVER_PORT} &
    bg_pid=$!

    #Sleep to give time for the server to run
    sleep 5 
    echo "Port ${SERVER_PORT} status after starting sever (0 normal): " `nc -z localhost ${SERVER_PORT} ; echo $?`

    for filename in ${REPO_ROOT}/Postman/*.json; do
      echo
      echo ===============================================
      echo Running Collection: $filename
      echo ===============================================
      "$NEWMAN_BIN" run $filename --env-var "BASE_URL=${BASE_URL}"
      
      if [[ $? == 1 ]]; then
        echo
        echo Error occured while running Postman tests from $filename
        kill -9 ${bg_pid}
        exit 1
      fi
    done

    echo Successful
    echo
    kill -9 ${bg_pid}
    echo "Port ${SERVER_PORT} status after kill (1 normal): " `nc -z localhost ${SERVER_PORT} ; echo $?`
    sleep 2
  fi
}

function safeopen {
    kernel=$(uname -s)
    if [ $kernel == "Darwin" ]; then
        open $1
    else
        xdg-open $1
    fi
}

function delayopen {
  sleep 2
  if ! safeopen http://localhost:"$SERVER_PORT"; then
    echo "Error: failed to automatically open a web browser."
    echo "Are you on an unsupported dev environment?"
    echo "Click here to launch the site: $site"
  fi
}

# parse args
PARAMS=""
while (( "$#" )); do
  case "$1" in
    -h|--help)
      usage;
      exit 0;
      ;;
    -*|--*=) # unsupported flags
      echo "unrecognized option -- $(echo $1 | sed 's~^-*~~')" >&2
      usage;
      exit 1
      ;;
    *) # preserve positional arguments
      PARAMS="$PARAMS $1"
      shift
      ;;
  esac
done

eval set -- "$PARAMS";

if [[ -z "$CS314_ENV" ]]; then
  CS314_ENV=dev;
fi;

# Remove target to avoid huge Maven shade warnings
rm -rf ${BUILD_DIRECTORY_PREFIX}/target

if [[ -n "${NFS_MOUNTED}" ]]; then
  mkdir -p ${BUILD_DIRECTORY_PREFIX}/target
  if [[ ! -L "${REPO_ROOT}/target" && -d "${REPO_ROOT}/target" ]]; then
    echo "Cleaning NFS mounted target"
    rm -rf ${REPO_ROOT}/target
  fi
  ln -sf ${BUILD_DIRECTORY_PREFIX}/target ${REPO_ROOT}/target
fi

# install dependencies
install_server_dependencies

if [[ -n "$NFS_MOUNTED" ]]; then
  copy_client_bundle_to_build_dir
fi

build_server
postman_tests
run_server