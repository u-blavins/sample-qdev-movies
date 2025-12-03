package com.amazonaws.samples.qdevmovies.movies;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Ahoy matey! These be the tests for our MovieService treasure hunting capabilities!
 * We'll make sure our search methods work ship-shape and Bristol fashion!
 */
public class MovieServiceTest {

    private MovieService movieService;

    @BeforeEach
    public void setUp() {
        movieService = new MovieService();
    }

    @Test
    @DisplayName("Should load all movie treasures from JSON")
    public void testGetAllMovies() {
        List<Movie> movies = movieService.getAllMovies();
        assertNotNull(movies, "Movie treasure chest should not be null!");
        assertEquals(12, movies.size(), "Should have 12 movie treasures in our collection!");
    }

    @Test
    @DisplayName("Should find movie treasure by valid ID")
    public void testGetMovieByValidId() {
        Optional<Movie> movie = movieService.getMovieById(1L);
        assertTrue(movie.isPresent(), "Should find treasure with ID 1!");
        assertEquals("The Prison Escape", movie.get().getMovieName());
    }

    @Test
    @DisplayName("Should return empty for invalid ID")
    public void testGetMovieByInvalidId() {
        Optional<Movie> movie = movieService.getMovieById(999L);
        assertFalse(movie.isPresent(), "Should not find treasure with invalid ID!");
    }

    @Test
    @DisplayName("Should return empty for null ID")
    public void testGetMovieByNullId() {
        Optional<Movie> movie = movieService.getMovieById(null);
        assertFalse(movie.isPresent(), "Should not find treasure with null ID!");
    }

    @Test
    @DisplayName("Should search treasures by name (case-insensitive)")
    public void testSearchMovieTreasuresByName() {
        List<Movie> results = movieService.searchMovieTreasures("prison", null, null);
        assertEquals(1, results.size(), "Should find 1 treasure with 'prison' in the name!");
        assertEquals("The Prison Escape", results.get(0).getMovieName());
        
        // Test case-insensitive search
        List<Movie> resultsUpperCase = movieService.searchMovieTreasures("PRISON", null, null);
        assertEquals(1, resultsUpperCase.size(), "Case-insensitive search should work!");
    }

    @Test
    @DisplayName("Should search treasures by genre")
    public void testSearchMovieTreasuresByGenre() {
        List<Movie> results = movieService.searchMovieTreasures(null, null, "Drama");
        assertTrue(results.size() > 0, "Should find treasures with Drama genre!");
        
        // Verify all results contain the genre
        for (Movie movie : results) {
            assertTrue(movie.getGenre().toLowerCase().contains("drama"), 
                      "All results should contain 'drama' in genre!");
        }
    }

    @Test
    @DisplayName("Should search treasures by specific ID")
    public void testSearchMovieTreasuresById() {
        List<Movie> results = movieService.searchMovieTreasures(null, 1L, null);
        assertEquals(1, results.size(), "Should find exactly 1 treasure with ID 1!");
        assertEquals("The Prison Escape", results.get(0).getMovieName());
    }

    @Test
    @DisplayName("Should search treasures by ID with additional criteria")
    public void testSearchMovieTreasuresByIdWithCriteria() {
        // Search by ID with matching genre
        List<Movie> results = movieService.searchMovieTreasures(null, 1L, "Drama");
        assertEquals(1, results.size(), "Should find treasure with ID 1 and Drama genre!");
        
        // Search by ID with non-matching genre
        List<Movie> noResults = movieService.searchMovieTreasures(null, 1L, "Comedy");
        assertEquals(0, noResults.size(), "Should not find treasure with ID 1 and Comedy genre!");
    }

    @Test
    @DisplayName("Should search treasures by multiple criteria")
    public void testSearchMovieTreasuresByMultipleCriteria() {
        List<Movie> results = movieService.searchMovieTreasures("family", null, "Crime");
        assertTrue(results.size() > 0, "Should find treasures matching both name and genre!");
        
        for (Movie movie : results) {
            assertTrue(movie.getMovieName().toLowerCase().contains("family") && 
                      movie.getGenre().toLowerCase().contains("crime"),
                      "All results should match both criteria!");
        }
    }

    @Test
    @DisplayName("Should return empty list when no treasures match criteria")
    public void testSearchMovieTreasuresNoMatches() {
        List<Movie> results = movieService.searchMovieTreasures("nonexistent", null, null);
        assertEquals(0, results.size(), "Should return empty list when no treasures match!");
    }

    @Test
    @DisplayName("Should handle null and empty search parameters")
    public void testSearchMovieTreasuresWithNullParameters() {
        List<Movie> results = movieService.searchMovieTreasures(null, null, null);
        assertEquals(12, results.size(), "Should return all treasures when all parameters are null!");
        
        List<Movie> emptyResults = movieService.searchMovieTreasures("", null, "");
        assertEquals(12, emptyResults.size(), "Should return all treasures when parameters are empty!");
    }

    @Test
    @DisplayName("Should handle whitespace-only search parameters")
    public void testSearchMovieTreasuresWithWhitespace() {
        List<Movie> results = movieService.searchMovieTreasures("   ", null, "   ");
        assertEquals(12, results.size(), "Should return all treasures when parameters are whitespace!");
    }

    @Test
    @DisplayName("Should return all unique genres")
    public void testGetAllGenres() {
        List<String> genres = movieService.getAllGenres();
        assertNotNull(genres, "Genres list should not be null!");
        assertTrue(genres.size() > 0, "Should have at least one genre!");
        assertTrue(genres.contains("Drama"), "Should contain Drama genre!");
        assertTrue(genres.contains("Action/Crime"), "Should contain Action/Crime genre!");
        
        // Check that genres are sorted
        for (int i = 1; i < genres.size(); i++) {
            assertTrue(genres.get(i-1).compareTo(genres.get(i)) <= 0, 
                      "Genres should be sorted alphabetically!");
        }
    }

    @Test
    @DisplayName("Should search treasures with partial name matches")
    public void testSearchMovieTreasuresPartialNameMatch() {
        List<Movie> results = movieService.searchMovieTreasures("the", null, null);
        assertTrue(results.size() > 1, "Should find multiple treasures with 'the' in the name!");
        
        for (Movie movie : results) {
            assertTrue(movie.getMovieName().toLowerCase().contains("the"),
                      "All results should contain 'the' in the name!");
        }
    }

    @Test
    @DisplayName("Should search treasures with partial genre matches")
    public void testSearchMovieTreasuresPartialGenreMatch() {
        List<Movie> results = movieService.searchMovieTreasures(null, null, "sci");
        assertTrue(results.size() > 0, "Should find treasures with 'sci' in the genre!");
        
        for (Movie movie : results) {
            assertTrue(movie.getGenre().toLowerCase().contains("sci"),
                      "All results should contain 'sci' in the genre!");
        }
    }
}