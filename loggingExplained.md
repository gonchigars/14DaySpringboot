### Step 1: **Setting Up the Logger**
Spring Boot uses the **SLF4J** (Simple Logging Facade for Java) along with **Logback** as the default logging framework. SLF4J acts as a facade for various logging frameworks, allowing you to use a unified API for logging, while Logback handles the actual logging operations.

#### 1.1 Dependency for Logging (Already Included)
Spring Boot comes with SLF4J and Logback as default dependencies, so you don’t need to add them manually. However, in your `pom.xml`, you have included a test-specific logging dependency:

```xml
<dependency>
    <groupId>uk.org.lidalia</groupId>
    <artifactId>slf4j-test</artifactId>
    <version>1.2.0</version>
    <scope>test</scope>
</dependency>
```
- **SLF4J-Test**: This is used for testing purposes to verify the logging behavior during unit tests.

### Step 2: **Configure Logging Levels**
Logging levels are configured in the `application.properties` file, allowing you to control the verbosity of the logs for different parts of the application.

#### 2.1 Logging Level Configuration
In the `application.properties` file, you’ve specified the logging levels for different packages and classes:

```properties
logging.level.com.example.demo.service.BookService=INFO
logging.level.com.example.demo.scheduletask.BookCleanupScheduler=INFO
logging.level.org.springframework.security=DEBUG
```

- **INFO Level for BookService**: This logs all messages at the `INFO` level and above (i.e., INFO, WARN, ERROR, FATAL) for the `BookService` class.
- **INFO Level for BookCleanupScheduler**: Similarly, logs all messages at the `INFO` level and above for the `BookCleanupScheduler` class.
- **DEBUG Level for Spring Security**: Logs detailed information at the `DEBUG` level for Spring Security, which can help in troubleshooting security-related issues.

### Step 3: **Using the Logger in Code**
You use the SLF4J `Logger` to log messages at different levels in your application. Here’s how it’s done:

#### 3.1 Example in `FileController`
In your `FileController.java` class, you create and use a logger as follows:

```java
package com.example.demo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/files")
public class FileController {

    private static final Logger logger = LoggerFactory.getLogger(FileController.class);

    // Example usage of logger
    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            // Normal operation log
            logger.info("File upload requested: " + file.getOriginalFilename());
            
            // Simulate file processing...
            
            // Success log
            logger.info("File uploaded successfully: " + file.getOriginalFilename());
            return ResponseEntity.ok("File uploaded successfully: " + file.getOriginalFilename());
        } catch (Exception e) {
            // Error log
            logger.error("File upload failed: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("File upload failed.");
        }
    }
}
```

- **Logger Initialization**: The logger is initialized using `LoggerFactory.getLogger(FileController.class)`.
- **INFO Level Logging**: `logger.info(...)` logs general information, like when a file upload is requested and when it’s successful.
- **ERROR Level Logging**: `logger.error(...)` logs error messages, such as when file upload fails.

### Step 4: **Log Output Configuration**
Spring Boot automatically configures Logback as the logging framework. By default, logs are output to the console. You can also configure logging to output to a file or other destinations by adding a `logback-spring.xml` file in your `src/main/resources` directory, but it’s not necessary unless you want custom configurations.

### Step 5: **Running the Application**
When you run your Spring Boot application, the logs will appear in the console. The level of detail depends on the logging levels configured:

- **INFO** logs will include general information about operations.
- **DEBUG** logs will include detailed information, especially useful for debugging Spring Security issues.
- **ERROR** logs will highlight issues that need immediate attention.

### Summary of Logging Implementation
1. **SLF4J** is used as the logging API, with **Logback** as the underlying logging framework.
2. **Logging levels** are configured in the `application.properties` file to control the verbosity of the logs.
3. **Log messages** are added to your application using SLF4J’s `Logger` within your code.
4. **Logs** are output to the console by default, with the option to customize the logging output via Logback configuration.

This logging setup helps you monitor the application's behavior, troubleshoot issues, and ensure that critical information is recorded during operation.
