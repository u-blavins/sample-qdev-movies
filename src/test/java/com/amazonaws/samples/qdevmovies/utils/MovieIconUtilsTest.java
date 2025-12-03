package com.amazonaws.samples.qdevmovies.utils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import static org.junit.jupiter.api.Assertions.*;

public class MovieIconUtilsTest {

    @Test
    public void testGetMovieIconForKnownMovies() {
        assertEquals("🔒", MovieIconUtils.getMovieIcon("the prison escape"));
        assertEquals("👔", MovieIconUtils.getMovieIcon("the family boss"));
        assertEquals("🦇", MovieIconUtils.getMovieIcon("the masked hero"));
        assertEquals("🌆", MovieIconUtils.getMovieIcon("urban stories"));
        assertEquals("🏃", MovieIconUtils.getMovieIcon("life journey"));
        assertEquals("💭", MovieIconUtils.getMovieIcon("dream heist"));
        assertEquals("🕶️", MovieIconUtils.getMovieIcon("the virtual world"));
        assertEquals("🤵", MovieIconUtils.getMovieIcon("the wise guys"));
        assertEquals("💍", MovieIconUtils.getMovieIcon("the quest for the ring"));
        assertEquals("🚀", MovieIconUtils.getMovieIcon("space wars: the beginning"));
        assertEquals("🏭", MovieIconUtils.getMovieIcon("the factory owner"));
        assertEquals("👊", MovieIconUtils.getMovieIcon("underground club"));
    }

    @Test
    public void testGetMovieIconCaseInsensitive() {
        // Test uppercase
        assertEquals("🔒", MovieIconUtils.getMovieIcon("THE PRISON ESCAPE"));
        assertEquals("👔", MovieIconUtils.getMovieIcon("THE FAMILY BOSS"));
        assertEquals("🦇", MovieIconUtils.getMovieIcon("THE MASKED HERO"));
        
        // Test mixed case
        assertEquals("🌆", MovieIconUtils.getMovieIcon("Urban Stories"));
        assertEquals("🏃", MovieIconUtils.getMovieIcon("Life Journey"));
        assertEquals("💭", MovieIconUtils.getMovieIcon("Dream Heist"));
        
        // Test with different capitalization patterns
        assertEquals("🕶️", MovieIconUtils.getMovieIcon("The Virtual World"));
        assertEquals("🤵", MovieIconUtils.getMovieIcon("The Wise Guys"));
        assertEquals("💍", MovieIconUtils.getMovieIcon("The Quest For The Ring"));
    }

    @Test
    public void testGetMovieIconForUnknownMovies() {
        assertEquals("🎬", MovieIconUtils.getMovieIcon("unknown movie"));
        assertEquals("🎬", MovieIconUtils.getMovieIcon("random title"));
        assertEquals("🎬", MovieIconUtils.getMovieIcon("some other movie"));
        assertEquals("🎬", MovieIconUtils.getMovieIcon("completely different"));
    }

    @Test
    public void testGetMovieIconWithNullInput() {
        // This will likely cause a NullPointerException, but let's test the current behavior
        assertThrows(NullPointerException.class, () -> {
            MovieIconUtils.getMovieIcon(null);
        });
    }

    @Test
    public void testGetMovieIconWithEmptyString() {
        assertEquals("🎬", MovieIconUtils.getMovieIcon(""));
    }

    @Test
    public void testGetMovieIconWithWhitespace() {
        assertEquals("🎬", MovieIconUtils.getMovieIcon("   "));
        assertEquals("🎬", MovieIconUtils.getMovieIcon("\t"));
        assertEquals("🎬", MovieIconUtils.getMovieIcon("\n"));
    }

    @Test
    public void testGetMovieIconWithSpecialCharacters() {
        assertEquals("🎬", MovieIconUtils.getMovieIcon("movie@home"));
        assertEquals("🎬", MovieIconUtils.getMovieIcon("movie#1"));
        assertEquals("🎬", MovieIconUtils.getMovieIcon("movie$"));
        assertEquals("🎬", MovieIconUtils.getMovieIcon("movie%"));
        assertEquals("🎬", MovieIconUtils.getMovieIcon("movie&tv"));
    }

    @Test
    public void testGetMovieIconWithNumbers() {
        assertEquals("🎬", MovieIconUtils.getMovieIcon("movie 2"));
        assertEquals("🎬", MovieIconUtils.getMovieIcon("2023 movie"));
        assertEquals("🎬", MovieIconUtils.getMovieIcon("123456"));
    }

    @Test
    public void testGetMovieIconWithUnicodeCharacters() {
        assertEquals("🎬", MovieIconUtils.getMovieIcon("película"));
        assertEquals("🎬", MovieIconUtils.getMovieIcon("映画"));
        assertEquals("🎬", MovieIconUtils.getMovieIcon("фильм"));
    }

    @Test
    public void testGetMovieIconWithPartialMatches() {
        // These should not match because they're not exact matches
        assertEquals("🎬", MovieIconUtils.getMovieIcon("prison escape"));
        assertEquals("🎬", MovieIconUtils.getMovieIcon("the prison"));
        assertEquals("🎬", MovieIconUtils.getMovieIcon("escape"));
        assertEquals("🎬", MovieIconUtils.getMovieIcon("family boss"));
        assertEquals("🎬", MovieIconUtils.getMovieIcon("the family"));
    }

    @Test
    public void testGetMovieIconWithExtraSpaces() {
        // Test with leading/trailing spaces - these should not match
        assertEquals("🎬", MovieIconUtils.getMovieIcon(" the prison escape "));
        assertEquals("🎬", MovieIconUtils.getMovieIcon("  the family boss  "));
        assertEquals("🎬", MovieIconUtils.getMovieIcon("\tthe masked hero\t"));
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "the prison escape", "the family boss", "the masked hero", "urban stories",
        "life journey", "dream heist", "the virtual world", "the wise guys",
        "the quest for the ring", "space wars: the beginning", "the factory owner", "underground club"
    })
    public void testAllKnownMoviesReturnSpecificIcons(String movieName) {
        String icon = MovieIconUtils.getMovieIcon(movieName);
        assertNotNull(icon);
        assertNotEquals("🎬", icon); // Should not return the default icon
        assertTrue(icon.length() > 0);
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"unknown", "random", "test movie", "   ", "123", "!@#$%"})
    public void testUnknownMoviesReturnDefaultIcon(String movieName) {
        if (movieName == null) {
            assertThrows(NullPointerException.class, () -> {
                MovieIconUtils.getMovieIcon(movieName);
            });
        } else {
            assertEquals("🎬", MovieIconUtils.getMovieIcon(movieName));
        }
    }

    @Test
    public void testGetMovieIconConsistency() {
        // Test that multiple calls return the same result
        String movieName = "the prison escape";
        String firstCall = MovieIconUtils.getMovieIcon(movieName);
        String secondCall = MovieIconUtils.getMovieIcon(movieName);
        String thirdCall = MovieIconUtils.getMovieIcon(movieName);
        
        assertEquals(firstCall, secondCall);
        assertEquals(secondCall, thirdCall);
        assertEquals("🔒", firstCall);
    }

    @Test
    public void testAllIconsAreValidEmojis() {
        // Test that all returned icons are valid emoji characters
        String[] knownMovies = {
            "the prison escape", "the family boss", "the masked hero", "urban stories",
            "life journey", "dream heist", "the virtual world", "the wise guys",
            "the quest for the ring", "space wars: the beginning", "the factory owner", "underground club"
        };
        
        for (String movie : knownMovies) {
            String icon = MovieIconUtils.getMovieIcon(movie);
            assertNotNull(icon);
            assertTrue(icon.length() > 0);
            // Basic check that it's likely an emoji (Unicode characters > ASCII range)
            assertTrue(icon.codePointAt(0) > 127);
        }
        
        // Test default icon
        String defaultIcon = MovieIconUtils.getMovieIcon("unknown");
        assertEquals("🎬", defaultIcon);
        assertTrue(defaultIcon.codePointAt(0) > 127);
    }
}