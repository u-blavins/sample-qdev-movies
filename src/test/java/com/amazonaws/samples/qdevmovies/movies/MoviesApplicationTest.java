package com.amazonaws.samples.qdevmovies.movies;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(properties = {
    "logging.level.com.amazonaws.samples.qdevmovies=INFO"
})
public class MoviesApplicationTest {

    @Test
    public void contextLoads() {
        // This test verifies that the Spring Boot application context loads successfully
        // If the context fails to load, this test will fail
    }
}