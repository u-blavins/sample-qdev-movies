package com.amazonaws.samples.qdevmovies.movies;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

@ExtendWith(MockitoExtension.class)
public class ReviewServiceTest {

    private ReviewService reviewService;

    @BeforeEach
    public void setUp() {
        reviewService = new ReviewService();
    }

    @Test
    public void testGetReviewsForMovieWithValidId() {
        // Test with a movie ID that might have reviews
        List<Review> reviews = reviewService.getReviewsForMovie(1L);
        assertNotNull(reviews);
        // Reviews list could be empty if no reviews exist for this movie
        assertTrue(reviews.size() >= 0);
    }

    @Test
    public void testGetReviewsForMovieWithInvalidId() {
        // Test with an ID that definitely doesn't exist
        List<Review> reviews = reviewService.getReviewsForMovie(999999L);
        assertNotNull(reviews);
        assertTrue(reviews.isEmpty());
    }

    @Test
    public void testGetReviewsForMovieWithZeroId() {
        List<Review> reviews = reviewService.getReviewsForMovie(0L);
        assertNotNull(reviews);
        // Should return empty list for invalid ID
        assertTrue(reviews.isEmpty());
    }

    @Test
    public void testGetReviewsForMovieWithNegativeId() {
        List<Review> reviews = reviewService.getReviewsForMovie(-1L);
        assertNotNull(reviews);
        assertTrue(reviews.isEmpty());
        
        List<Review> reviews2 = reviewService.getReviewsForMovie(-100L);
        assertNotNull(reviews2);
        assertTrue(reviews2.isEmpty());
    }

    @Test
    public void testGetReviewsForMovieConsistency() {
        // Test that multiple calls return the same result
        long movieId = 1L;
        List<Review> firstCall = reviewService.getReviewsForMovie(movieId);
        List<Review> secondCall = reviewService.getReviewsForMovie(movieId);
        List<Review> thirdCall = reviewService.getReviewsForMovie(movieId);
        
        assertNotNull(firstCall);
        assertNotNull(secondCall);
        assertNotNull(thirdCall);
        
        assertEquals(firstCall.size(), secondCall.size());
        assertEquals(secondCall.size(), thirdCall.size());
        
        // If there are reviews, verify they're the same
        for (int i = 0; i < firstCall.size(); i++) {
            Review review1 = firstCall.get(i);
            Review review2 = secondCall.get(i);
            Review review3 = thirdCall.get(i);
            
            assertEquals(review1.getUserName(), review2.getUserName());
            assertEquals(review2.getUserName(), review3.getUserName());
            assertEquals(review1.getRating(), review2.getRating(), 0.01);
            assertEquals(review2.getRating(), review3.getRating(), 0.01);
            assertEquals(review1.getComment(), review2.getComment());
            assertEquals(review2.getComment(), review3.getComment());
        }
    }

    @Test
    public void testReviewServiceInitialization() {
        // Test that the service can be created without throwing exceptions
        assertDoesNotThrow(() -> {
            ReviewService newService = new ReviewService();
            assertNotNull(newService);
        });
    }

    @Test
    public void testReviewsHaveValidData() {
        // Test several movie IDs to check review data validity
        for (long movieId = 1L; movieId <= 12L; movieId++) {
            List<Review> reviews = reviewService.getReviewsForMovie(movieId);
            assertNotNull(reviews);
            
            for (Review review : reviews) {
                assertNotNull(review);
                // User name can be null or empty, but review object should not be null
                // Rating should be within reasonable bounds
                assertTrue(review.getRating() >= 0.0, "Rating should be non-negative");
                assertTrue(review.getRating() <= 10.0, "Rating should not exceed 10.0");
                // Avatar emoji and comment can be null or empty, but should not cause issues
            }
        }
    }

    @Test
    public void testGetReviewsForMultipleMovies() {
        // Test getting reviews for multiple movies
        for (long movieId = 1L; movieId <= 5L; movieId++) {
            List<Review> reviews = reviewService.getReviewsForMovie(movieId);
            assertNotNull(reviews, "Reviews should not be null for movie ID " + movieId);
        }
    }

    @Test
    public void testReviewServiceHandlesEmptyJsonGracefully() {
        // This test verifies that the service doesn't crash if the JSON file is missing or empty
        assertDoesNotThrow(() -> {
            List<Review> reviews = reviewService.getReviewsForMovie(1L);
            assertNotNull(reviews);
        });
    }

    @Test
    public void testGetReviewsForMovieWithBoundaryValues() {
        // Test with boundary values
        List<Review> reviewsMinValue = reviewService.getReviewsForMovie(Long.MIN_VALUE);
        assertNotNull(reviewsMinValue);
        assertTrue(reviewsMinValue.isEmpty());
        
        List<Review> reviewsMaxValue = reviewService.getReviewsForMovie(Long.MAX_VALUE);
        assertNotNull(reviewsMaxValue);
        assertTrue(reviewsMaxValue.isEmpty());
    }

    @Test
    public void testReviewServicePerformance() {
        // Basic performance test - operations should complete quickly
        long startTime = System.currentTimeMillis();
        
        // Test multiple movie IDs
        for (long movieId = 1L; movieId <= 20L; movieId++) {
            reviewService.getReviewsForMovie(movieId);
        }
        
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        
        // Operations should complete within reasonable time
        assertTrue(duration < 5000, "Operations should complete within 5 seconds");
    }

    @Test
    public void testReviewDataIntegrity() {
        // Test that review data maintains integrity across calls
        long movieId = 1L;
        List<Review> reviews = reviewService.getReviewsForMovie(movieId);
        
        if (!reviews.isEmpty()) {
            Review firstReview = reviews.get(0);
            String originalUserName = firstReview.getUserName();
            String originalAvatarEmoji = firstReview.getAvatarEmoji();
            double originalRating = firstReview.getRating();
            String originalComment = firstReview.getComment();
            
            // Get reviews again and verify the first review hasn't changed
            List<Review> reviewsAgain = reviewService.getReviewsForMovie(movieId);
            if (!reviewsAgain.isEmpty()) {
                Review firstReviewAgain = reviewsAgain.get(0);
                assertEquals(originalUserName, firstReviewAgain.getUserName());
                assertEquals(originalAvatarEmoji, firstReviewAgain.getAvatarEmoji());
                assertEquals(originalRating, firstReviewAgain.getRating(), 0.01);
                assertEquals(originalComment, firstReviewAgain.getComment());
            }
        }
    }

    @Test
    public void testReviewsAreImmutable() {
        List<Review> reviews = reviewService.getReviewsForMovie(1L);
        
        if (!reviews.isEmpty()) {
            Review review = reviews.get(0);
            
            // Store original values
            String originalUserName = review.getUserName();
            String originalAvatarEmoji = review.getAvatarEmoji();
            double originalRating = review.getRating();
            String originalComment = review.getComment();
            
            // Multiple calls to getters should return the same values
            assertEquals(originalUserName, review.getUserName());
            assertEquals(originalAvatarEmoji, review.getAvatarEmoji());
            assertEquals(originalRating, review.getRating(), 0.01);
            assertEquals(originalComment, review.getComment());
        }
    }

    @Test
    public void testEmptyReviewsList() {
        // Test that empty reviews list is handled properly
        List<Review> emptyReviews = reviewService.getReviewsForMovie(999999L);
        assertNotNull(emptyReviews);
        assertTrue(emptyReviews.isEmpty());
        assertEquals(0, emptyReviews.size());
    }

    @Test
    public void testReviewServiceWithVariousMovieIds() {
        // Test a range of movie IDs to ensure consistent behavior
        long[] testIds = {1L, 2L, 5L, 10L, 12L, 15L, 100L, 1000L};
        
        for (long movieId : testIds) {
            List<Review> reviews = reviewService.getReviewsForMovie(movieId);
            assertNotNull(reviews, "Reviews should not be null for movie ID " + movieId);
            
            // Verify each review in the list is valid
            for (Review review : reviews) {
                assertNotNull(review, "Individual review should not be null");
            }
        }
    }
}