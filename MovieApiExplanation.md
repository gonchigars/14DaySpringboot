## **Spring Boot Movie API Tutorial (Without `adult` Field)**

### **Objective**

To build a Spring Boot backend API that returns movie data in a structured JSON format, with fields such as `backdropPath`, `genreIds`, `id`, `originalLanguage`, `originalTitle`, and `overview`. The `adult` field is excluded from the response.

### **Step 1: Set Up the Spring Boot Project**

1. **Go to Spring Initializr**: Visit [Spring Initializr](https://start.spring.io/) to generate a new Spring Boot project.
2. **Configure the Project**:

   - **Project**: Maven Project
   - **Language**: Java
   - **Spring Boot Version**: 2.7.x or 3.0.x (latest stable version)
   - **Group**: `com.example`
   - **Artifact**: `movie-api`
   - **Name**: `movie-api`
   - **Package Name**: `com.example.movieapi`
   - **Packaging**: Jar
   - **Java Version**: 17 (or your installed version)

3. **Add Dependencies**:

   - **Spring Web**: For building RESTful APIs.
   - **Spring Data JPA**: For database interaction using JPA.
   - **H2 Database**: An in-memory database for development and testing.

4. **Generate and Download**: Click "Generate" to download the project as a zip file. Unzip it to your desired location.

### **Step 2: Create the Movie Entity**

The `Movie` entity represents the structure of your movie data. It maps to a database table where each movie is stored as a row.

1. **Create the Entity Class**:

   - In your project, navigate to `src/main/java/com/example/movieapi/`.
   - Create a package named `model` inside the `movieapi` package.
   - Create a new Java class named `Movie` inside the `model` package.

2. **Define the Movie Entity**:

   ```java
   package com.example.movieapi.model;

   import javax.persistence.*;
   import java.util.List;

   @Entity
   public class Movie {
       @Id
       @GeneratedValue(strategy = GenerationType.IDENTITY)
       private Long id;  // Primary key of type Long

       private String backdropPath;
       private String originalLanguage;
       private String originalTitle;
       private String overview;

       @ElementCollection
       private List<Integer> genreIds;

       // Getters and Setters

       public Long getId() {
           return id;
       }

       public void setId(Long id) {
           this.id = id;
       }

       public String getBackdropPath() {
           return backdropPath;
       }

       public void setBackdropPath(String backdropPath) {
           this.backdropPath = backdropPath;
       }

       public String getOriginalLanguage() {
           return originalLanguage;
       }

       public void setOriginalLanguage(String originalLanguage) {
           this.originalLanguage = originalLanguage;
       }

       public String getOriginalTitle() {
           return originalTitle;
       }

       public void setOriginalTitle(String originalTitle) {
           this.originalTitle = originalTitle;
       }

       public String getOverview() {
           return overview;
       }

       public void setOverview(String overview) {
           this.overview = overview;
       }

       public List<Integer> getGenreIds() {
           return genreIds;
       }

       public void setGenreIds(List<Integer> genreIds) {
           this.genreIds = genreIds;
       }
   }
   ```

   **Explanation**:

   - **`@Entity`**: Marks this class as a JPA entity, meaning it will map to a table in the database.
   - **`@Id` and `@GeneratedValue(strategy = GenerationType.IDENTITY)`**: The `id` field is the primary key, and its value will be automatically generated.
   - **Fields**: Each field represents a column in the database table. The `genreIds` field is a list of integers stored as a separate collection within the entity.

### **Step 3: Create the Movie Repository**

The repository layer handles database operations like saving, deleting, and retrieving data.

1. **Create the Repository Interface**:

   - In your project, navigate to `src/main/java/com/example/movieapi/`.
   - Create a package named `repository` inside the `movieapi` package.
   - Create a new Java interface named `MovieRepository` inside the `repository` package.

2. **Define the Repository Interface**:

   ```java
   package com.example.movieapi.repository;

   import com.example.movieapi.model.Movie;
   import org.springframework.data.jpa.repository.JpaRepository;

   public interface MovieRepository extends JpaRepository<Movie, Long> {
       // JpaRepository provides CRUD operations and more
   }
   ```

   **Explanation**:

   - **`MovieRepository extends JpaRepository<Movie, Long>`**: This interface extends `JpaRepository`, which provides built-in methods for CRUD operations. The `Movie` class is the entity type, and `Long` is the type of the primary key.

### **Step 4: Create the Movie Service**

The service layer contains the business logic. It interacts with the repository to perform operations and may include additional logic.

1. **Create the Service Class**:

   - In your project, navigate to `src/main/java/com/example/movieapi/`.
   - Create a package named `service` inside the `movieapi` package.
   - Create a new Java class named `MovieService` inside the `service` package.

2. **Define the Service Class**:

   ```java
   package com.example.movieapi.service;

   import com.example.movieapi.model.Movie;
   import com.example.movieapi.repository.MovieRepository;
   import org.springframework.beans.factory.annotation.Autowired;
   import org.springframework.stereotype.Service;

   import java.util.List;

   @Service
   public class MovieService {
       @Autowired
       private MovieRepository movieRepository;

       public List<Movie> getAllMovies() {
           return movieRepository.findAll();
       }
   }
   ```

   **Explanation**:

   - **`@Service`**: Marks this class as a service component in the Spring context.
   - **`@Autowired`**: Injects the `MovieRepository` into the service to allow interaction with the database.
   - **`getAllMovies()`**: Retrieves all movie records from the database.

### **Step 5: Create the Movie Controller**

The controller handles HTTP requests and maps them to appropriate service methods. It is responsible for exposing the API endpoints.

1. **Create the Controller Class**:

   - In your project, navigate to `src/main/java/com/example/movieapi/`.
   - Create a package named `controller` inside the `movieapi` package.
   - Create a new Java class named `MovieController` inside the `controller` package.

2. **Define the Controller Class**:

   ```java
   package com.example.movieapi.controller;

   import com.example.movieapi.model.Movie;
   import com.example.movieapi.service.MovieService;
   import org.springframework.beans.factory.annotation.Autowired;
   import org.springframework.http.ResponseEntity;
   import org.springframework.web.bind.annotation.GetMapping;
   import org.springframework.web.bind.annotation.RequestMapping;
   import org.springframework.web.bind.annotation.RestController;

   import java.util.HashMap;
   import java.util.List;
   import java.util.Map;

   @RestController
   @RequestMapping("/api/movies")
   public class MovieController {

       @Autowired
       private MovieService movieService;

       @GetMapping("/popular")
       public ResponseEntity<Map<String, Object>> getPopularMovies() {
           List<Movie> movies = movieService.getAllMovies();
           Map<String, Object> response = new HashMap<>();
           response.put("results", movies);
           return ResponseEntity.ok(response);
       }
   }
   ```

   **Explanation**:

   - **`@RestController`**: Marks this class as a REST controller, meaning it handles HTTP requests and returns data as JSON or XML.
   - **`@RequestMapping("/api/movies")`**: Specifies the base URL for all endpoints in this controller.
   - **`@GetMapping("/popular")`**: Maps HTTP GET requests to the `getPopularMovies()` method.
   - **`ResponseEntity<Map<String, Object>>`**: Wraps the response in an HTTP response entity, with a status code and a body containing the movie data.

### **Step 6: Populate the Database**

Add initial data to the database by creating a `data.sql` file. This file will be automatically executed when the application starts, populating the database with predefined data.

1. **Create the Data File**:

   - Navigate to `src/main/resources/`.
   - Create a new file named `data.sql`.

2. **Insert Initial Data**:

   ```sql
   INSERT INTO movie (backdrop_path, original_language, original_title, overview) VALUES
   ('/9l1eZiJHmhr5jIlthMdJN5WYoff.jpg', 'en', 'Deadpool & Wolverine', 'A listless Wade Wilson toils away in civilian life with his days as the morally flexible mercenary, Deadpool, behind him. But when his homeworld faces an existential threat, Wade must reluctantly suit-up again with an even more reluctant Wolverine.'),
   ('/2RVcJbWFmICRDsVxRI8F5xRmRsK.jpg', 'en', 'A Quiet Place: Day One', 'As New York City is invaded by alien creatures who hunt by sound, a woman named Sam fights to survive with her cat
   ```

.');

````

**Explanation**:
- This SQL script inserts two rows into the `movie` table, each representing a movie.

### **Step 7: Run the Application**

1. **Run the Application**:
- Open your terminal or IDE and navigate to the root directory of your project.
- Use the following command to run the application:

  ```bash
  mvn spring-boot:run
  ```

2. **Access the API**:
- Open your web browser or use a tool like Postman.
- Navigate to `http://localhost:8080/api/movies/popular`.
- You should receive a JSON response similar to the following:

  ```json
  {
    "results": [
      {
        "backdropPath": "/9l1eZiJHmhr5jIlthMdJN5WYoff.jpg",
        "genreIds": [28, 35, 878],
        "id": 533535,
        "originalLanguage": "en",
        "originalTitle": "Deadpool & Wolverine",
        "overview": "A listless Wade Wilson toils away in civilian life with his days as the morally flexible mercenary, Deadpool, behind him. But when his homeworld faces an existential threat, Wade must reluctantly suit-up again with an even more reluctant Wolverine."
      },
      {
        "backdropPath": "/2RVcJbWFmICRDsVxRI8F5xRmRsK.jpg",
        "genreIds": [27, 878, 53],
        "id": 762441,
        "originalLanguage": "en",
        "originalTitle": "A Quiet Place: Day One",
        "overview": "As New York City is invaded by alien creatures who hunt by sound, a woman named Sam fights to survive with her cat."
      }
    ]
  }
  ```

### **Step 8: Serving Images**

Images can be served from different locations depending on your requirements.

#### **Option 1: Storing Images Locally**

1. **Store Images in Project Directory**:
- Place the image files in `src/main/resources/static/images/`.
- Update the `backdropPath` in the `Movie` entity to reflect the local path:

  ```sql
  INSERT INTO movie (backdrop_path, original_language, original_title, overview) VALUES
  ('/images/9l1eZiJHmhr5jIlthMdJN5WYoff.jpg', 'en', 'Deadpool & Wolverine', 'A listless Wade Wilson toils away in civilian life...'),
  ('/images/2RVcJbWFmICRDsVxRI8F5xRmRsK.jpg', 'en', 'A Quiet Place: Day One', 'As New York City is invaded by alien creatures...');
  ```

- Access the images via URL: `http://localhost:8080/images/9l1eZiJHmhr5jIlthMdJN5WYoff.jpg`.


### **Conclusion**

You have successfully created a Spring Boot API that returns movie data without the `adult` field. The API is structured to return a list of movies with fields like `backdropPath`, `genreIds`, `id`, `originalLanguage`, `originalTitle`, and `overview`. You also have the flexibility to serve images from local storage, AWS S3, or Google Drive based on your application's needs.

This tutorial provides a solid foundation for building and extending a backend API using Spring Boot. You can further enhance this application by adding more endpoints, integrating it with a real database, implementing authentication, or deploying it to a cloud platform.
````
