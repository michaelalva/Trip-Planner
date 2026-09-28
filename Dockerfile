# Use official OpenJDK image as base
FROM eclipse-temurin:17-jdk-jammy

# Install Maven, Node.js, and npm
RUN apt-get update && apt-get install -y maven nodejs npm

# Set working directory
WORKDIR /app

# Copy project files
COPY . .

# Build application
RUN mvn clean package

# Expose server port
EXPOSE 4567

# Run application script
CMD ["./bin/run.sh"]