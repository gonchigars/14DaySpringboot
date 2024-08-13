## Docker for Developers: A Quick Start Guide

Docker is a platform that allows developers to package applications into containers—standardized units of software that include everything needed to run: code, runtime, system tools, and libraries. Containers are lightweight, portable, and ensure that your application runs consistently across different environments.

### 1. **Installing Docker**

Before you begin, you need to have Docker installed on your machine. You can download Docker from [Docker's official website](https://www.docker.com/get-started) and follow the installation instructions for your operating system.

### 2. **Pulling an Image**

Docker images are blueprints for containers. You can pull pre-built images from Docker Hub, a large repository of Docker images.

- To pull an image, use the `docker pull` command followed by the image name:

  ```bash
  docker pull nginx
  ```

  This command pulls the latest `nginx` image from Docker Hub.

### 3. **Running a Container**

Once you have pulled an image, you can create and start a container from it.

- To run a container, use the `docker run` command:

  ```bash
  docker run -d -p 8080:80 nginx
  ```

  - `-d`: Runs the container in detached mode (in the background).
  - `-p 8080:80`: Maps port 8080 on your local machine to port 80 inside the container. This allows you to access the web server running inside the container via `http://localhost:8080`.

### 4. **Changing Ports**

If the default ports are already in use or you want to expose a different port, you can easily change the port mapping.

- For example, to run the same `nginx` container on port 8081:

  ```bash
  docker run -d -p 8081:80 nginx
  ```

  Now, the `nginx` web server will be accessible via `http://localhost:8081`.

### 5. **Viewing Running Containers**

You can list all running containers using:

```bash
docker ps
```

This command shows details like container ID, image name, command, creation time, and port mappings.

### 6. **Stopping and Removing Containers**

To stop a running container, use the `docker stop` command followed by the container ID or name:

```bash
docker stop <container_id>
```

To remove a container, first, stop it (if it’s running), then use:

```bash
docker rm <container_id>
```

### 7. **Pulling, Tagging, and Running Custom Images**

If you want to pull an image, modify it, and run it with a custom setup:

- Pull an image and run it:

  ```bash
  docker run -it ubuntu
  ```

  This command runs an interactive terminal inside an Ubuntu container.

- After making changes inside the container (like installing software), you can commit these changes to create a new image:

  ```bash
  docker commit <container_id> my_custom_image
  ```

- Tagging the image with a specific version:

  ```bash
  docker tag my_custom_image my_custom_image:v1
  ```

- Running your custom image:

  ```bash
  docker run -d -p 8080:80 my_custom_image:v1
  ```

### 8. **Conclusion**

Docker simplifies the process of developing, testing, and deploying applications by isolating them in containers. By pulling images, changing ports, and running containers, you can ensure your applications behave consistently across different environments.

Experiment with Docker commands and gradually explore more advanced features like Docker Compose and multi-stage builds to enhance your development workflow. Happy Dockering!

### Step 1: Update the Dockerfile

1. mvn clean package -DskipTests --> Run this to create jar file.

1. Open your `Dockerfile`.
1. Modify the line that copies the JAR file to match the actual file name and location:

   ```Dockerfile
   # Use a base image with JDK
   FROM openjdk:17-jdk-slim

   # Set the working directory inside the container
   WORKDIR /app

   # Copy the JAR file into the container
   COPY target/demo-1.0-SNAPSHOT.jar /app/demo-1.0-SNAPSHOT.jar

   # Expose the application port
   EXPOSE 8080

   # Run the Spring Boot application
   CMD ["java", "-jar", "demo-1.0-SNAPSHOT.jar"]
   ```

### Step 2: Build the Docker Image

1. Navigate to the root of your project where the `Dockerfile` is located.
2. Run the following command to build the Docker image:

   ```bash
   docker build -t springboot-app .
   ```

   This will build a Docker image named `springboot-app` using the instructions in the `Dockerfile`.

### Step 3: Run the Docker Container

1. After successfully building the image, you can run it using:

   ```bash
   docker run -p 8080:8080 springboot-app
   ```

   This command maps port 8080 of your local machine to port 8080 of the container, allowing you to access the application via `http://localhost:8080`.

### Step 4: Verify the Application

1. Once the container is running, you can access your Spring Boot application's endpoints using your browser or tools like Postman.
