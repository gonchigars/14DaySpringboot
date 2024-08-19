### Overview

The `FileController` class is a Spring Boot REST controller that handles file upload and download operations. It provides two main functionalities:

1. Uploading a file to the server.
2. Downloading a file from the server.

### Package and Imports

```java
package com.example.demo.controller;
```

- The `package` declaration indicates that this class is part of the `com.example.demo.controller` package.

```java
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.net.MalformedURLException;
```

- **Imports**: These bring in external classes and libraries that the code will use.
  - `Logger` and `LoggerFactory`: Used for logging messages.
  - `Resource` and `UrlResource`: Used to handle file resources.
  - `HttpHeaders`, `HttpStatus`, `MediaType`, `ResponseEntity`: Used for handling HTTP responses.
  - `MultipartFile`: Represents the file uploaded by the client.
  - `Path`, `Paths`, `Files`, `StandardCopyOption`: Used for file handling.
  - `AccessDeniedException`, `MalformedURLException`, `IOException`: Handle specific exceptions.

### Class Declaration

```java
@RestController
@RequestMapping("/files")
public class FileController {
```

- **`@RestController`**: This annotation indicates that this class will handle HTTP requests and will return responses, typically in JSON format.
- **`@RequestMapping("/files")`**: This sets the base URL for all the endpoints in this controller. All endpoints will start with `/files`.

### Logger Initialization

```java
private static final Logger logger = LoggerFactory.getLogger(FileController.class);
```

- **Logger**: This is used to log messages for debugging or informational purposes. The `LoggerFactory` is used to create a logger instance.

### File Storage Location

```java
private final Path fileStorageLocation = Paths.get("uploads").toAbsolutePath().normalize();
```

- **`fileStorageLocation`**: This is a `Path` object that represents the directory where uploaded files will be stored. The directory is named `uploads`, and it is converted to an absolute path and normalized to remove any redundant elements (like `.` or `..`).

### File Upload Method

```java
@PostMapping("/upload")
public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
```

- **`@PostMapping("/upload")`**: This maps HTTP POST requests to this method. The client will call this endpoint to upload a file.
- **`@RequestParam("file") MultipartFile file`**: This annotation tells Spring to extract the file from the request's form-data and bind it to the `file` parameter.

### Handling the File Upload

```java
try {
    // Normalize file name
    String fileName = file.getOriginalFilename();
    if (fileName == null || fileName.contains("..")) {
        logger.error("Invalid file name: " + fileName);
        return ResponseEntity.badRequest().body("Invalid file name.");
    }
```

- **Normalize File Name**: The file's original name is retrieved using `getOriginalFilename()`. The code checks if the filename is null or contains invalid characters (e.g., `..`), which could be a security risk by allowing path traversal. If the filename is invalid, an error is logged, and a `400 Bad Request` response is returned.

```java
    // Copy file to the target location (Replacing existing file with the same name)
    Path targetLocation = this.fileStorageLocation.resolve(fileName);
    Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
```

- **Copying the File**: The `resolve` method appends the file name to the `fileStorageLocation` path. The `Files.copy` method copies the file's contents to the target location, replacing any existing file with the same name.

```java
    logger.info("File uploaded successfully: " + fileName);
    return ResponseEntity.ok("File uploaded successfully: " + fileName);
```

- **Success Response**: If the file is successfully saved, a success message is logged, and a `200 OK` response is returned with the message "File uploaded successfully".

### Exception Handling

```java
} catch (AccessDeniedException ex) {
    logger.error("Access denied: " + ex.getMessage());
    return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied: " + ex.getMessage());
} catch (IOException ex) {
    logger.error("Could not upload the file: " + ex.getMessage());
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Could not upload the file.");
} catch (Exception ex) {
    logger.error("Unexpected error: " + ex.getMessage());
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error occurred.");
}
```

- **Exception Handling**: Different exceptions are caught and handled:
  - **AccessDeniedException**: If the server lacks the necessary permissions to save the file, a `403 Forbidden` response is returned.
  - **IOException**: If there's an input/output error while saving the file, a `500 Internal Server Error` response is returned.
  - **Generic Exception**: Any other unexpected error is caught and logged, with a `500 Internal Server Error` response.

### File Download Method

```java
@GetMapping("/download/{fileName:.+}")
public ResponseEntity<Resource> downloadFile(@PathVariable String fileName) {
```

- **`@GetMapping("/download/{fileName:.+}")`**: This maps HTTP GET requests to this method. The `fileName:.+` pattern allows for file extensions in the path variable.
- **`@PathVariable String fileName`**: This annotation tells Spring to extract the `fileName` from the URL and pass it as a parameter.

### Handling the File Download

```java
try {
    Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
    Resource resource = new UrlResource(filePath.toUri());

    if (resource.exists()) {
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(Files.probeContentType(filePath)))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    } else {
        return ResponseEntity.notFound().build();
    }
```

- **Resolving File Path**: The file path is constructed by resolving the `fileName` against the `fileStorageLocation` and normalizing it.
- **Loading the Resource**: The `UrlResource` is created from the file path's URI, which represents the file as a resource.
- **File Existence Check**: If the file exists, the method returns the file wrapped in a `ResponseEntity`. The response includes the content type (determined by the file type) and a header specifying that the file should be downloaded as an attachment with its original name.
- **File Not Found**: If the file does not exist, a `404 Not Found` response is returned.

### Exception Handling in Download Method

```java
} catch (MalformedURLException ex) {
    logger.error("File not found: " + ex.getMessage());
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
} catch (IOException ex) {
    logger.error("Could not determine file type: " + ex.getMessage());
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
}
```

- **MalformedURLException**: This is caught if the file path's URI is invalid. A `500 Internal Server Error` response is returned.
- **IOException**: This is caught if there is an issue determining the file's content type. Again, a `500 Internal Server Error` response is returned.

### Summary

- **FileController**: This class provides endpoints for file upload and download.
- **Upload**: Files are uploaded via a POST request to `/files/upload`, where they are saved to the `uploads` directory.
- **Download**: Files can be downloaded via a GET request to `/files/download/{fileName}`, where the file is served as a downloadable resource.
- **Error Handling**: The controller includes robust error handling for various potential issues, ensuring appropriate HTTP responses are returned.
