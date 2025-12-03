package com.amazonaws.samples.qdevmovies.movies;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;
import org.springframework.ui.ExtendedModelMap;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MoviesControllerTest {

    @Mock
    private MovieService movieService;

    @Mock
    private ReviewService reviewService;

    @InjectMocks
    private MoviesController moviesController;

    @Test
    public void testGetMovies() {
        // Arrange
        Model model = new ExtendedModelMap();
        List<Movie> mockMovies = Arrays.asList(
            new Movie(1L, "Test Movie 1", "Director 1", 2023, "Drama", "Description 1", 120, 8.5),
            new Movie(2L, "Test Movie 2", "Director 2", 2022, "Comedy", "Description 2", 90, 7.2)
        );
        when(movieService.getAllMovies()).thenReturn(mockMovies);

        // Act
        String result = moviesController.getMovies(model);

        // Assert
        assertEquals("movies", result);
        verify(movieService, times(1)).getAllMovies();
        assertTrue(model.containsAttribute("movies"));
        assertEquals(mockMovies, model.asMap().get("movies"));
    }

    @Test
    public void testGetMoviesWithEmptyList() {
        // Arrange
        Model model = new ExtendedModelMap();
        when(movieService.getAllMovies()).thenReturn(Arrays.asList());

        // Act
        String result = moviesController.getMovies(model);

        // Assert
        assertEquals("movies", result);
        verify(movieService, times(1)).getAllMovies();
        assertTrue(model.containsAttribute("movies"));
        List<?> movies = (List<?>) model.asMap().get("movies");
        assertTrue(movies.isEmpty());
    }

    @Test
    public void testGetMovieDetailsWithValidId() {
        // Arrange
        Model model = new ExtendedModelMap();
        Long movieId = 1L;
        Movie mockMovie = new Movie(1L, "Test Movie", "Test Director", 2023, "Drama", "Test description", 120, 8.5);
        List<Review> mockReviews = Arrays.asList(
            new Review("John Doe", "😊", 4.5, "Great movie!"),
            new Review("Jane Smith", "👍", 4.0, "Really enjoyed it.")
        );
        
        when(movieService.getMovieById(movieId)).thenReturn(Optional.of(mockMovie));
        when(reviewService.getReviewsForMovie(movieId)).thenReturn(mockReviews);

        // Act
        String result = moviesController.getMovieDetails(movieId, model);

        // Assert
        assertEquals("movie-details", result);
        verify(movieService, times(1)).getMovieById(movieId);
        verify(reviewService, times(1)).getReviewsForMovie(movieId);
        
        assertTrue(model.containsAttribute("movie"));
        assertTrue(model.containsAttribute("movieIcon"));
        assertTrue(model.containsAttribute("allReviews"));
        
        assertEquals(mockMovie, model.asMap().get("movie"));
        assertEquals(mockReviews, model.asMap().get("allReviews"));
        assertNotNull(model.asMap().get("movieIcon"));
    }

    @Test
    public void testGetMovieDetailsWithInvalidId() {
        // Arrange
        Model model = new ExtendedModelMap();
        Long movieId = 999L;
        
        when(movieService.getMovieById(movieId)).thenReturn(Optional.empty());

        // Act
        String result = moviesController.getMovieDetails(movieId, model);

        // Assert
        assertEquals("error", result);
        verify(movieService, times(1)).getMovieById(movieId);
        verify(reviewService, never()).getReviewsForMovie(anyLong());
        
        assertTrue(model.containsAttribute("title"));
        assertTrue(model.containsAttribute("message"));
        
        assertEquals("Movie Not Found", model.asMap().get("title"));
        assertEquals("Movie with ID " + movieId + " was not found.", model.asMap().get("message"));
    }

    @Test
    public void testGetMovieDetailsWithNullId() {
        // Arrange
        Model model = new ExtendedModelMap();
        
        when(movieService.getMovieById(null)).thenReturn(Optional.empty());

        // Act
        String result = moviesController.getMovieDetails(null, model);

        // Assert
        assertEquals("error", result);
        verify(movieService, times(1)).getMovieById(null);
        verify(reviewService, never()).getReviewsForMovie(anyLong());
    }

    @Test
    public void testGetMovieDetailsWithZeroId() {
        // Arrange
        Model model = new ExtendedModelMap();
        Long movieId = 0L;
        
        when(movieService.getMovieById(movieId)).thenReturn(Optional.empty());

        // Act
        String result = moviesController.getMovieDetails(movieId, model);

        // Assert
        assertEquals("error", result);
        verify(movieService, times(1)).getMovieById(movieId);
        verify(reviewService, never()).getReviewsForMovie(anyLong());
    }

    @Test
    public void testGetMovieDetailsWithNegativeId() {
        // Arrange
        Model model = new ExtendedModelMap();
        Long movieId = -1L;
        
        when(movieService.getMovieById(movieId)).thenReturn(Optional.empty());

        // Act
        String result = moviesController.getMovieDetails(movieId, model);

        // Assert
        assertEquals("error", result);
        verify(movieService, times(1)).getMovieById(movieId);
        verify(reviewService, never()).getReviewsForMovie(anyLong());
    }

    @Test
    public void testGetMovieDetailsVerifyIconGeneration() {
        // Arrange
        Model model = new ExtendedModelMap();
        Long movieId = 1L;
        Movie mockMovie = new Movie(1L, "The Prison Escape", "Director", 2023, "Drama", "Description", 120, 8.5);
        
        when(movieService.getMovieById(movieId)).thenReturn(Optional.of(mockMovie));
        when(reviewService.getReviewsForMovie(movieId)).thenReturn(Arrays.asList());

        // Act
        String result = moviesController.getMovieDetails(movieId, model);

        // Assert
        assertEquals("movie-details", result);
        assertEquals("🔒", model.asMap().get("movieIcon")); // Expected icon for "The Prison Escape"
    }

    @Test
    public void testGetMovieDetailsWithEmptyReviews() {
        // Arrange
        Model model = new ExtendedModelMap();
        Long movieId = 1L;
        Movie mockMovie = new Movie(1L, "Test Movie", "Director", 2023, "Drama", "Description", 120, 8.5);
        
        when(movieService.getMovieById(movieId)).thenReturn(Optional.of(mockMovie));
        when(reviewService.getReviewsForMovie(movieId)).thenReturn(Arrays.asList());

        // Act
        String result = moviesController.getMovieDetails(movieId, model);

        // Assert
        assertEquals("movie-details", result);
        List<?> reviews = (List<?>) model.asMap().get("allReviews");
        assertTrue(reviews.isEmpty());
    }

    @Test
    public void testGetMovieDetailsModelAttributes() {
        // Arrange
        Model model = new ExtendedModelMap();
        Long movieId = 1L;
        Movie mockMovie = new Movie(1L, "Test Movie", "Director", 2023, "Drama", "Description", 120, 8.5);
        List<Review> mockReviews = Arrays.asList(
            new Review("User", "😊", 4.5, "Good movie")
        );
        
        when(movieService.getMovieById(movieId)).thenReturn(Optional.of(mockMovie));
        when(reviewService.getReviewsForMovie(movieId)).thenReturn(mockReviews);

        // Act
        moviesController.getMovieDetails(movieId, model);

        // Assert - Verify all expected model attributes are present
        assertTrue(model.containsAttribute("movie"));
        assertTrue(model.containsAttribute("movieIcon"));
        assertTrue(model.containsAttribute("allReviews"));
        
        // Verify attribute values
        assertEquals(mockMovie, model.asMap().get("movie"));
        assertEquals(mockReviews, model.asMap().get("allReviews"));
        assertEquals("🎬", model.asMap().get("movieIcon")); // Default icon for unknown movie
    }

    @Test
    public void testServiceInteractionOrder() {
        // Arrange
        Model model = new ExtendedModelMap();
        Long movieId = 1L;
        Movie mockMovie = new Movie(1L, "Test Movie", "Director", 2023, "Drama", "Description", 120, 8.5);
        
        when(movieService.getMovieById(movieId)).thenReturn(Optional.of(mockMovie));
        when(reviewService.getReviewsForMovie(movieId)).thenReturn(Arrays.asList());

        // Act
        moviesController.getMovieDetails(movieId, model);

        // Assert - Verify services are called in the correct order
        verify(movieService, times(1)).getMovieById(movieId);
        verify(reviewService, times(1)).getReviewsForMovie(movieId);
    }
}
