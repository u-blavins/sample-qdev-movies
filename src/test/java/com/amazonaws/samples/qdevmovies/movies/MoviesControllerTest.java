package com.amazonaws.samples.qdevmovies.movies;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.ui.Model;
import org.springframework.ui.ExtendedModelMap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Ahoy matey! These be the tests for our MoviesController treasure hunting endpoints!
 * We'll make sure our controller handles all requests ship-shape!
 */
public class MoviesControllerTest {

    private MoviesController moviesController;
    private Model model;
    private MockMovieService mockMovieService;
    private ReviewService mockReviewService;

    @BeforeEach
    public void setUp() {
        moviesController = new MoviesController();
        model = new ExtendedModelMap();
        
        // Create mock services
        mockMovieService = new MockMovieService();
        mockReviewService = new MockReviewService();
        
        // Inject mocks using reflection
        try {
            java.lang.reflect.Field movieServiceField = MoviesController.class.getDeclaredField("movieService");
            movieServiceField.setAccessible(true);
            movieServiceField.set(moviesController, mockMovieService);
            
            java.lang.reflect.Field reviewServiceField = MoviesController.class.getDeclaredField("reviewService");
            reviewServiceField.setAccessible(true);
            reviewServiceField.set(moviesController, mockReviewService);
        } catch (Exception e) {
            throw new RuntimeException("Failed to inject mock services", e);
        }
    }

    @Test
    @DisplayName("Should return movies view with all treasures")
    public void testGetMovies() {
        String result = moviesController.getMovies(model);
        assertNotNull(result);
        assertEquals("movies", result);
        assertTrue(model.containsAttribute("movies"));
        assertTrue(model.containsAttribute("genres"));
    }

    @Test
    @DisplayName("Should return movie details view for valid treasure ID")
    public void testGetMovieDetails() {
        String result = moviesController.getMovieDetails(1L, model);
        assertNotNull(result);
        assertEquals("movie-details", result);
        assertTrue(model.containsAttribute("movie"));
    }

    @Test
    @DisplayName("Should return error view for invalid treasure ID")
    public void testGetMovieDetailsNotFound() {
        String result = moviesController.getMovieDetails(999L, model);
        assertNotNull(result);
        assertEquals("error", result);
        assertTrue(model.containsAttribute("title"));
        assertTrue(model.containsAttribute("message"));
    }

    @Test
    @DisplayName("Should search treasures by name successfully")
    public void testSearchMovieTreasuresByName() {
        String result = moviesController.searchMovieTreasures("Test", null, null, model);
        assertEquals("movies", result);
        assertTrue(model.containsAttribute("movies"));
        assertTrue(model.containsAttribute("searchPerformed"));
        assertTrue(model.containsAttribute("searchName"));
        assertEquals("Test", model.getAttribute("searchName"));
    }

    @Test
    @DisplayName("Should search treasures by ID successfully")
    public void testSearchMovieTreasuresById() {
        String result = moviesController.searchMovieTreasures(null, 1L, null, model);
        assertEquals("movies", result);
        assertTrue(model.containsAttribute("movies"));
        assertTrue(model.containsAttribute("searchPerformed"));
        assertTrue(model.containsAttribute("searchId"));
        assertEquals(1L, model.getAttribute("searchId"));
    }

    @Test
    @DisplayName("Should search treasures by genre successfully")
    public void testSearchMovieTreasuresByGenre() {
        String result = moviesController.searchMovieTreasures(null, null, "Drama", model);
        assertEquals("movies", result);
        assertTrue(model.containsAttribute("movies"));
        assertTrue(model.containsAttribute("searchPerformed"));
        assertTrue(model.containsAttribute("searchGenre"));
        assertEquals("Drama", model.getAttribute("searchGenre"));
    }

    @Test
    @DisplayName("Should handle search with multiple criteria")
    public void testSearchMovieTreasuresMultipleCriteria() {
        String result = moviesController.searchMovieTreasures("Test", 1L, "Drama", model);
        assertEquals("movies", result);
        assertTrue(model.containsAttribute("searchName"));
        assertTrue(model.containsAttribute("searchId"));
        assertTrue(model.containsAttribute("searchGenre"));
        assertEquals("Test", model.getAttribute("searchName"));
        assertEquals(1L, model.getAttribute("searchId"));
        assertEquals("Drama", model.getAttribute("searchGenre"));
    }

    @Test
    @DisplayName("Should return error for invalid treasure ID in search")
    public void testSearchMovieTreasuresInvalidId() {
        String result = moviesController.searchMovieTreasures(null, -1L, null, model);
        assertEquals("error", result);
        assertTrue(model.containsAttribute("title"));
        assertTrue(model.containsAttribute("message"));
        String message = (String) model.getAttribute("message");
        assertTrue(message.contains("invalid"), "Error message should mention invalid ID");
    }

    @Test
    @DisplayName("Should handle empty search results")
    public void testSearchMovieTreasuresNoResults() {
        // Mock service will return empty list for "nonexistent" search
        mockMovieService.setReturnEmptyResults(true);
        String result = moviesController.searchMovieTreasures("nonexistent", null, null, model);
        assertEquals("movies", result);
        assertTrue(model.containsAttribute("noResultsMessage"));
        String message = (String) model.getAttribute("noResultsMessage");
        assertTrue(message.contains("No movie treasures found"), "Should have pirate-themed no results message");
    }

    @Test
    @DisplayName("Should handle search with results found")
    public void testSearchMovieTreasuresWithResults() {
        String result = moviesController.searchMovieTreasures("Test", null, null, model);
        assertEquals("movies", result);
        assertTrue(model.containsAttribute("resultsMessage"));
        String message = (String) model.getAttribute("resultsMessage");
        assertTrue(message.contains("treasure"), "Should have pirate-themed results message");
    }

    @Test
    @DisplayName("Should handle search exception gracefully")
    public void testSearchMovieTreasuresException() {
        // Mock service will throw exception
        mockMovieService.setThrowException(true);
        String result = moviesController.searchMovieTreasures("test", null, null, model);
        assertEquals("error", result);
        assertTrue(model.containsAttribute("title"));
        assertTrue(model.containsAttribute("message"));
        String message = (String) model.getAttribute("message");
        assertTrue(message.contains("treasure hunt"), "Error message should have pirate theme");
    }

    @Test
    @DisplayName("Should integrate with movie service correctly")
    public void testMovieServiceIntegration() {
        List<Movie> movies = mockMovieService.getAllMovies();
        assertEquals(1, movies.size());
        assertEquals("Test Movie", movies.get(0).getMovieName());
        
        List<String> genres = mockMovieService.getAllGenres();
        assertTrue(genres.contains("Drama"));
    }

    // Mock MovieService for testing
    private static class MockMovieService extends MovieService {
        private boolean returnEmptyResults = false;
        private boolean throwException = false;
        
        public void setReturnEmptyResults(boolean returnEmptyResults) {
            this.returnEmptyResults = returnEmptyResults;
        }
        
        public void setThrowException(boolean throwException) {
            this.throwException = throwException;
        }
        
        @Override
        public List<Movie> getAllMovies() {
            return Arrays.asList(new Movie(1L, "Test Movie", "Test Director", 2023, "Drama", "Test description", 120, 4.5));
        }
        
        @Override
        public Optional<Movie> getMovieById(Long id) {
            if (id == 1L) {
                return Optional.of(new Movie(1L, "Test Movie", "Test Director", 2023, "Drama", "Test description", 120, 4.5));
            }
            return Optional.empty();
        }
        
        @Override
        public List<Movie> searchMovieTreasures(String name, Long id, String genre) {
            if (throwException) {
                throw new RuntimeException("Test exception for treasure hunt");
            }
            
            if (returnEmptyResults) {
                return new ArrayList<>();
            }
            
            return Arrays.asList(new Movie(1L, "Test Movie", "Test Director", 2023, "Drama", "Test description", 120, 4.5));
        }
        
        @Override
        public List<String> getAllGenres() {
            return Arrays.asList("Drama", "Action", "Comedy");
        }
    }
    
    // Mock ReviewService for testing
    private static class MockReviewService extends ReviewService {
        @Override
        public List<Review> getReviewsForMovie(long movieId) {
            return new ArrayList<>();
        }
    }
}
