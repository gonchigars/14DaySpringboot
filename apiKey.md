To implement API key authentication in your Spring Boot application, you can follow these steps:

### Step 1: Add API Key Property
First, you need to store your API key securely, typically in the `application.properties` or `application.yml` file.

```properties
# application.properties
api.key=your-secure-api-key
```

### Step 2: Create an API Key Filter
You need to create a filter that intercepts incoming HTTP requests and checks for the presence of a valid API key.

```java
package com.example.demo.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class ApiKeyFilter implements javax.servlet.Filter {

    @Value("${api.key}")
    private String apiKey;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Initialization logic if needed
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String requestApiKey = httpRequest.getHeader("API-Key");

        if (apiKey.equals(requestApiKey)) {
            chain.doFilter(request, response); // Valid API key, proceed with the request
        } else {
            HttpServletResponse httpResponse = (HttpServletResponse) response;
            httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // Unauthorized
            httpResponse.getWriter().write("Invalid API Key");
            return;
        }
    }

    @Override
    public void destroy() {
        // Cleanup logic if needed
    }
}
```

### Step 3: Register the Filter
Next, you need to register the filter in your security configuration.

```java
package com.example.demo.config;

import com.example.demo.security.ApiKeyFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final ApiKeyFilter apiKeyFilter;

    @Autowired
    public SecurityConfig(ApiKeyFilter apiKeyFilter) {
        this.apiKeyFilter = apiKeyFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf().disable()  // Disable CSRF protection
            .authorizeRequests()
            .antMatchers("/books/public/view").permitAll()  // Allow access to /books/public/view without authentication
            .antMatchers("/h2-console/**").permitAll()  // Allow access to H2 console without authentication
            .anyRequest().authenticated()  // Secure all other endpoints
            .and()
            .httpBasic();  // Enable Basic Authentication

        http.addFilterBefore(apiKeyFilter, UsernamePasswordAuthenticationFilter.class); // Register the API key filter

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
        manager.createUser(User.withUsername("user")
            .password("{noop}password")  // {noop} is a password encoder that does nothing, just for testing
            .roles("USER")
            .build());
        return manager;
    }
}
```

### Step 4: Test the API Key
Now, every request to your secured endpoints must include the `API-Key` header with the correct API key. For example:

```http
GET /books HTTP/1.1
Host: example.com
API-Key: your-secure-api-key
```

If the API key is valid, the request will be processed; otherwise, it will be rejected with a `401 Unauthorized` status.

### Optional: Exclude Public Endpoints
If you want to exclude certain public endpoints from API key verification, you can modify the filter to skip verification for those endpoints.

### Step 5: Secure the API Key
Ensure that the API key is stored securely and not hard-coded. You can use environment variables or a secret management service for better security.

This setup allows you to protect your API with an API key while still using Spring Security for other aspects of authentication.
