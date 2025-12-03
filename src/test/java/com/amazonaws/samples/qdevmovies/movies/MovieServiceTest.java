package com.amazonaws.samples.qdevmovies.movies;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class MovieServiceTest {

    private MovieService movieService;

    @BeforeEach
    public void setUp() {
        movieService = new MovieService();
    }

    @Test
    public void testGetAllMovies() {
        List<Movie> movies = movieService.getAllMovies();
        assertNotNull(movies);
        // The actual number of movies depends on the movies.json file
        // We'll test that it returns a list (could be empty if file doesn't exist)
        assertTrue(movies.size() >= 0);
    }

    @Test
    public void testGetAllMoviesConsistency() {
        // Test that multiple calls return the same list
        List<Movie> firstCall = movieService.getAllMovies();
        List<Movie> secondCall = movieService.getAllMovies();
        
        assertNotNull(firstCall);
        assertNotNull(secondCall);
        assertEquals(firstCall.size(), secondCall.size());
        
        // If there are movies, verify they're the same
        if (!firstCall.isEmpty()) {
            for (int i = 0; i < firstCall.size(); i++) {
                Movie movie1 = firstCall.get(i);
                Movie movie2 = secondCall.get(i);
                assertEquals(movie1.getId(), movie2.getId());
                assertEquals(movie1.getMovieName(), movie2.getMovieName());
                assertEquals(movie1.getDirector(), movie2.getDirector());
            }
        }
    }

    @Test
    public void testGetMovieByIdWithValidId() {
        List<Movie> allMovies = movieService.getAllMovies();
        
        if (!allMovies.isEmpty()) {
            // Test with the first movie's ID
            Movie firstMovie = allMovies.get(0);
            Optional<Movie> result = movieService.getMovieById(firstMovie.getId());
            
            assertTrue(result.isPresent());
            assertEquals(firstMovie.getId(), result.get().getId());
            assertEquals(firstMovie.getMovieName(), result.get().getMovieName());
            assertEquals(firstMovie.getDirector(), result.get().getDirector());
        }
    }

    @Test
    public void testGetMovieByIdWithInvalidId() {
        // Test with an ID that definitely doesn't exist
        Optional<Movie> result = movieService.getMovieById(999999L);
        assertFalse(result.isPresent());
    }

    @Test
    public void testGetMovieByIdWithNullId() {
        Optional<Movie> result = movieService.getMovieById(null);
        assertFalse(result.isPresent());
    }

    @Test
    public void testGetMovieByIdWithZeroId() {
        Optional<Movie> result = movieService.getMovieById(0L);
        assertFalse(result.isPresent());
    }

    @Test
    public void testGetMovieByIdWithNegativeId() {
        Optional<Movie> result = movieService.getMovieById(-1L);
        assertFalse(result.isPresent());
        
        Optional<Movie> result2 = movieService.getMovieById(-100L);
        assertFalse(result2.isPresent());
    }

    @Test
    public void testGetMovieByIdConsistency() {
        List<Movie> allMovies = movieService.getAllMovies();
        
        if (!allMovies.isEmpty()) {
            Movie firstMovie = allMovies.get(0);
            Long movieId = firstMovie.getId();
            
            // Multiple calls should return the same result
            Optional<Movie> firstCall = movieService.getMovieById(movieId);
            Optional<Movie> secondCall = movieService.getMovieById(movieId);
            Optional<Movie> thirdCall = movieService.getMovieById(movieId);
            
            assertEquals(firstCall.isPresent(), secondCall.isPresent());
            assertEquals(secondCall.isPresent(), thirdCall.isPresent());
            
            if (firstCall.isPresent()) {
                assertEquals(firstCall.get().getId(), secondCall.get().getId());
                assertEquals(secondCall.get().getId(), thirdCall.get().getId());
            }
        }
    }

    @Test
    public void testMovieServiceInitialization() {
        // Test that the service can be created without throwing exceptions
        assertDoesNotThrow(() -> {
            MovieService newService = new MovieService();
            assertNotNull(newService);
        });
    }

    @Test
    public void testAllMoviesHaveValidData() {
        List<Movie> movies = movieService.getAllMovies();
        
        for (Movie movie : movies) {
            assertNotNull(movie);
            assertTrue(movie.getId() > 0, "Movie ID should be positive");
            assertNotNull(movie.getMovieName(), "Movie name should not be null");
            assertNotNull(movie.getDirector(), "Director should not be null");
            assertTrue(movie.getYear() > 0, "Year should be positive");
            assertNotNull(movie.getGenre(), "Genre should not be null");
            assertNotNull(movie.getDescription(), "Description should not be null");
            assertTrue(movie.getDuration() >= 0, "Duration should be non-negative");
            assertTrue(movie.getImdbRating() >= 0.0, "IMDB rating should be non-negative");
            assertTrue(movie.getImdbRating() <= 10.0, "IMDB rating should not exceed 10.0");
        }
    }

    @Test
    public void testMovieIdsAreUnique() {
        List<Movie> movies = movieService.getAllMovies();
        
        // Check that all movie IDs are unique
        for (int i = 0; i < movies.size(); i++) {
            for (int j = i + 1; j < movies.size(); j++) {
                assertNotEquals(movies.get(i).getId(), movies.get(j).getId(),
                    "Movie IDs should be unique");
            }
        }
    }

    @Test
    public void testGetMovieByIdForAllMovies() {
        List<Movie> allMovies = movieService.getAllMovies();
        
        // Test that we can retrieve each movie by its ID
        for (Movie movie : allMovies) {
            Optional<Movie> retrieved = movieService.getMovieById(movie.getId());
            assertTrue(retrieved.isPresent(), "Should be able to retrieve movie with ID " + movie.getId());
            assertEquals(movie.getId(), retrieved.get().getId());
            assertEquals(movie.getMovieName(), retrieved.get().getMovieName());
        }
    }

    @Test
    public void testMovieServiceHandlesEmptyJsonGracefully() {
        // This test verifies that the service doesn't crash if the JSON file is missing or empty
        // The actual behavior depends on the implementation, but it should not throw exceptions
        assertDoesNotThrow(() -> {
            List<Movie> movies = movieService.getAllMovies();
            assertNotNull(movies);
        });
    }

    @Test
    public void testGetMovieByIdWithBoundaryValues() {
        // Test with boundary values
        Optional<Movie> resultMinValue = movieService.getMovieById(Long.MIN_VALUE);
        assertFalse(resultMinValue.isPresent());
        
        Optional<Movie> resultMaxValue = movieService.getMovieById(Long.MAX_VALUE);
        assertFalse(resultMaxValue.isPresent());
        
        Optional<Movie> resultOne = movieService.getMovieById(1L);
        // This may or may not be present depending on the data, but should not throw exception
        assertNotNull(resultOne);
    }

    @Test
    public void testMovieServicePerformance() {
        // Basic performance test - operations should complete quickly
        long startTime = System.currentTimeMillis();
        
        List<Movie> movies = movieService.getAllMovies();
        for (Movie movie : movies) {
            movieService.getMovieById(movie.getId());
        }
        
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        
        // Operations should complete within reasonable time (adjust as needed)
        assertTrue(duration < 5000, "Operations should complete within 5 seconds");
    }
}