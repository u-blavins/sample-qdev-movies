package com.amazonaws.samples.qdevmovies.movies;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

public class ReviewTest {

    private Review review;

    @BeforeEach
    public void setUp() {
        review = new Review("John Doe", "😊", 4.5, "Great movie! Highly recommended.");
    }

    @Test
    public void testReviewConstructorAndGetters() {
        assertEquals("John Doe", review.getUserName());
        assertEquals("😊", review.getAvatarEmoji());
        assertEquals(4.5, review.getRating(), 0.01);
        assertEquals("Great movie! Highly recommended.", review.getComment());
    }

    @Test
    public void testReviewWithMinimalValues() {
        Review minimalReview = new Review("", "", 0.0, "");
        
        assertEquals("", minimalReview.getUserName());
        assertEquals("", minimalReview.getAvatarEmoji());
        assertEquals(0.0, minimalReview.getRating(), 0.01);
        assertEquals("", minimalReview.getComment());
    }

    @Test
    public void testReviewWithMaxValues() {
        Review maxReview = new Review("Very Long User Name", "🎭", 10.0, 
                                     "This is a very long comment about the movie that goes on and on...");
        
        assertEquals("Very Long User Name", maxReview.getUserName());
        assertEquals("🎭", maxReview.getAvatarEmoji());
        assertEquals(10.0, maxReview.getRating(), 0.01);
        assertEquals("This is a very long comment about the movie that goes on and on...", maxReview.getComment());
    }

    @Test
    public void testReviewWithNullValues() {
        Review nullReview = new Review(null, null, 5.0, null);
        
        assertNull(nullReview.getUserName());
        assertNull(nullReview.getAvatarEmoji());
        assertEquals(5.0, nullReview.getRating(), 0.01);
        assertNull(nullReview.getComment());
    }

    @Test
    public void testReviewWithNegativeRating() {
        Review negativeReview = new Review("User", "😞", -1.0, "Bad movie");
        
        assertEquals("User", negativeReview.getUserName());
        assertEquals("😞", negativeReview.getAvatarEmoji());
        assertEquals(-1.0, negativeReview.getRating(), 0.01);
        assertEquals("Bad movie", negativeReview.getComment());
    }

    @Test
    public void testReviewWithVariousRatings() {
        // Test boundary values for ratings
        Review zeroRating = new Review("User1", "😐", 0.0, "No opinion");
        assertEquals(0.0, zeroRating.getRating(), 0.01);
        
        Review lowRating = new Review("User2", "😞", 1.0, "Poor");
        assertEquals(1.0, lowRating.getRating(), 0.01);
        
        Review midRating = new Review("User3", "😊", 5.0, "Average");
        assertEquals(5.0, midRating.getRating(), 0.01);
        
        Review highRating = new Review("User4", "😍", 10.0, "Excellent");
        assertEquals(10.0, highRating.getRating(), 0.01);
    }

    @Test
    public void testReviewWithSpecialCharacters() {
        Review specialReview = new Review("José María", "🎬", 8.5, 
                                         "¡Excelente película! Très bien. 素晴らしい映画です。");
        
        assertEquals("José María", specialReview.getUserName());
        assertEquals("🎬", specialReview.getAvatarEmoji());
        assertEquals(8.5, specialReview.getRating(), 0.01);
        assertEquals("¡Excelente película! Très bien. 素晴らしい映画です。", specialReview.getComment());
    }

    @Test
    public void testReviewImmutability() {
        // Test that Review objects are immutable by verifying getters return consistent values
        String originalUserName = review.getUserName();
        String originalAvatarEmoji = review.getAvatarEmoji();
        double originalRating = review.getRating();
        String originalComment = review.getComment();
        
        // Multiple calls should return the same values
        assertEquals(originalUserName, review.getUserName());
        assertEquals(originalAvatarEmoji, review.getAvatarEmoji());
        assertEquals(originalRating, review.getRating(), 0.01);
        assertEquals(originalComment, review.getComment());
    }

    @Test
    public void testReviewWithEmptyStrings() {
        Review emptyReview = new Review("", "", 3.0, "");
        
        assertTrue(emptyReview.getUserName().isEmpty());
        assertTrue(emptyReview.getAvatarEmoji().isEmpty());
        assertEquals(3.0, emptyReview.getRating(), 0.01);
        assertTrue(emptyReview.getComment().isEmpty());
    }

    @Test
    public void testReviewWithWhitespaceStrings() {
        Review whitespaceReview = new Review("   ", " ", 7.5, "  Good movie  ");
        
        assertEquals("   ", whitespaceReview.getUserName());
        assertEquals(" ", whitespaceReview.getAvatarEmoji());
        assertEquals(7.5, whitespaceReview.getRating(), 0.01);
        assertEquals("  Good movie  ", whitespaceReview.getComment());
    }
}