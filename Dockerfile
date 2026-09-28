# Use official OpenJDK image as base
FROM eclipse-temurin:17-jdk-jammy

# Install Maven, Node.js, and npm
RUN apt-get update && apt-get install -y maven nodejs npm

# Set working directory
WORKDIR /app

# Copy project files
COPY . .

# Move into server directory containing pom.xml
WORKDIR /app/server

# Build the shaded jar and skip tests
RUN mvn clean package -Dmaven.test.skip=true

# Expose server port
EXPOSE 4567

# Run the compiled jar directly using the target directory prefix setup from pom.xml
WORKDIR /app
CMD ["java", "-jar", "target/server-local.jar", "4567"]