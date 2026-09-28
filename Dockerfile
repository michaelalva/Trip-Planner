# Use an official OpenJDK image as base
FROM eclipse-temurin:17-jdk-jammy

# Install Node.js (needed to build the React frontend)
RUN apt-get update && apt-get install -y nodejs npm

# Set working directory
WORKDIR /app

# Copy project files
COPY . .

# Build the application 
RUN mvn clean package

# Expose the port Java Spark app runs on 
EXPOSE 4567

# Command to run app
CMD ["./bin/run.sh"]