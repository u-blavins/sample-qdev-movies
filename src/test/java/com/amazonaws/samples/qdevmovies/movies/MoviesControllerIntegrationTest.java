package com.amazonaws.samples.qdevmovies.movies;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MoviesController.class)
public class MoviesControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MovieService movieService;

    @MockBean
    private ReviewService reviewService;

    @Test
    public void testGetMoviesEndpoint() throws Exception {
        // Arrange
        List<Movie> mockMovies = Arrays.asList(
            new Movie(1L, "Test Movie 1", "Director 1", 2023, "Drama", "Description 1", 120, 8.5),
            new Movie(2L, "Test Movie 2", "Director 2", 2022, "Comedy", "Description 2", 90, 7.2)
        );
        when(movieService.getAllMovies()).thenReturn(mockMovies);

        // Act & Assert
        mockMvc.perform(get("/movies"))
                .andExpect(status().isOk())
                .andExpect(view().name("movies"))
                .andExpect(model().attributeExists("movies"))
                .andExpect(model().attribute("movies", mockMovies));

        verify(movieService, times(1)).getAllMovies();
    }

    @Test
    public void testGetMoviesEndpointWithEmptyList() throws Exception {
        // Arrange
        when(movieService.getAllMovies()).thenReturn(Arrays.asList());

        // Act & Assert
        mockMvc.perform(get("/movies"))
                .andExpect(status().isOk())
                .andExpect(view().name("movies"))
                .andExpect(model().attributeExists("movies"));

        verify(movieService, times(1)).getAllMovies();
    }

    @Test
    public void testGetMovieDetailsEndpointWithValidId() throws Exception {
        // Arrange
        Long movieId = 1L;
        Movie mockMovie = new Movie(1L, "Test Movie", "Test Director", 2023, "Drama", "Test description", 120, 8.5);
        List<Review> mockReviews = Arrays.asList(
            new Review("John Doe", "😊", 4.5, "Great movie!"),
            new Review("Jane Smith", "👍", 4.0, "Really enjoyed it.")
        );
        
        when(movieService.getMovieById(movieId)).thenReturn(Optional.of(mockMovie));
        when(reviewService.getReviewsForMovie(movieId)).thenReturn(mockReviews);

        // Act & Assert
        mockMvc.perform(get("/movies/{id}/details", movieId))
                .andExpect(status().isOk())
                .andExpect(view().name("movie-details"))
                .andExpect(model().attributeExists("movie"))
                .andExpect(model().attributeExists("movieIcon"))
                .andExpect(model().attributeExists("allReviews"))
                .andExpect(model().attribute("movie", mockMovie))
                .andExpect(model().attribute("allReviews", mockReviews));

        verify(movieService, times(1)).getMovieById(movieId);
        verify(reviewService, times(1)).getReviewsForMovie(movieId);
    }

    @Test
    public void testGetMovieDetailsEndpointWithInvalidId() throws Exception {
        // Arrange
        Long movieId = 999L;
        when(movieService.getMovieById(movieId)).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(get("/movies/{id}/details", movieId))
                .andExpect(status().isOk())
                .andExpect(view().name("error"))
                .andExpect(model().attributeExists("title"))
                .andExpect(model().attributeExists("message"))
                .andExpect(model().attribute("title", "Movie Not Found"))
                .andExpect(model().attribute("message", "Movie with ID " + movieId + " was not found."));

        verify(movieService, times(1)).getMovieById(movieId);
        verify(reviewService, never()).getReviewsForMovie(anyLong());
    }

    @Test
    public void testGetMovieDetailsEndpointWithZeroId() throws Exception {
        // Arrange
        Long movieId = 0L;
        when(movieService.getMovieById(movieId)).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(get("/movies/{id}/details", movieId))
                .andExpect(status().isOk())
                .andExpect(view().name("error"));

        verify(movieService, times(1)).getMovieById(movieId);
    }

    @Test
    public void testGetMovieDetailsEndpointWithNegativeId() throws Exception {
        // Arrange
        Long movieId = -1L;
        when(movieService.getMovieById(movieId)).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(get("/movies/{id}/details", movieId))
                .andExpect(status().isOk())
                .andExpect(view().name("error"));

        verify(movieService, times(1)).getMovieById(movieId);
    }

    @Test
    public void testGetMovieDetailsEndpointWithSpecificMovieIcon() throws Exception {
        // Arrange
        Long movieId = 1L;
        Movie mockMovie = new Movie(1L, "The Prison Escape", "Director", 2023, "Drama", "Description", 120, 8.5);
        
        when(movieService.getMovieById(movieId)).thenReturn(Optional.of(mockMovie));
        when(reviewService.getReviewsForMovie(movieId)).thenReturn(Arrays.asList());

        // Act & Assert
        mockMvc.perform(get("/movies/{id}/details", movieId))
                .andExpect(status().isOk())
                .andExpect(view().name("movie-details"))
                .andExpect(model().attribute("movieIcon", "🔒")); // Expected icon for "The Prison Escape"
    }

    @Test
    public void testGetMovieDetailsEndpointWithEmptyReviews() throws Exception {
        // Arrange
        Long movieId = 1L;
        Movie mockMovie = new Movie(1L, "Test Movie", "Director", 2023, "Drama", "Description", 120, 8.5);
        
        when(movieService.getMovieById(movieId)).thenReturn(Optional.of(mockMovie));
        when(reviewService.getReviewsForMovie(movieId)).thenReturn(Arrays.asList());

        // Act & Assert
        mockMvc.perform(get("/movies/{id}/details", movieId))
                .andExpect(status().isOk())
                .andExpect(view().name("movie-details"))
                .andExpect(model().attributeExists("allReviews"));

        verify(reviewService, times(1)).getReviewsForMovie(movieId);
    }

    @Test
    public void testInvalidEndpoint() throws Exception {
        // Test that invalid endpoints return 404
        mockMvc.perform(get("/invalid-endpoint"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testGetMovieDetailsWithStringId() throws Exception {
        // Test with non-numeric ID - should result in 400 Bad Request
        mockMvc.perform(get("/movies/abc/details"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testGetMovieDetailsWithVeryLargeId() throws Exception {
        // Arrange
        Long movieId = Long.MAX_VALUE;
        when(movieService.getMovieById(movieId)).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(get("/movies/{id}/details", movieId))
                .andExpect(status().isOk())
                .andExpect(view().name("error"));

        verify(movieService, times(1)).getMovieById(movieId);
    }

    @Test
    public void testMoviesEndpointResponseTime() throws Exception {
        // Arrange
        when(movieService.getAllMovies()).thenReturn(Arrays.asList());

        // Act & Assert - Basic performance test
        long startTime = System.currentTimeMillis();
        
        mockMvc.perform(get("/movies"))
                .andExpect(status().isOk());
        
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        
        // Response should be reasonably fast (adjust threshold as needed)
        assert(duration < 1000); // Less than 1 second
    }
}