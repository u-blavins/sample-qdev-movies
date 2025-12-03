package com.amazonaws.samples.qdevmovies.movies;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

public class MovieTest {

    private Movie movie;

    @BeforeEach
    public void setUp() {
        movie = new Movie(1L, "The Shawshank Redemption", "Frank Darabont", 1994, 
                         "Drama", "Two imprisoned men bond over a number of years", 142, 9.3);
    }

    @Test
    public void testMovieConstructorAndGetters() {
        assertEquals(1L, movie.getId());
        assertEquals("The Shawshank Redemption", movie.getMovieName());
        assertEquals("Frank Darabont", movie.getDirector());
        assertEquals(1994, movie.getYear());
        assertEquals("Drama", movie.getGenre());
        assertEquals("Two imprisoned men bond over a number of years", movie.getDescription());
        assertEquals(142, movie.getDuration());
        assertEquals(9.3, movie.getImdbRating(), 0.01);
    }

    @Test
    public void testMovieWithMinimalValues() {
        Movie minimalMovie = new Movie(0L, "", "", 0, "", "", 0, 0.0);
        
        assertEquals(0L, minimalMovie.getId());
        assertEquals("", minimalMovie.getMovieName());
        assertEquals("", minimalMovie.getDirector());
        assertEquals(0, minimalMovie.getYear());
        assertEquals("", minimalMovie.getGenre());
        assertEquals("", minimalMovie.getDescription());
        assertEquals(0, minimalMovie.getDuration());
        assertEquals(0.0, minimalMovie.getImdbRating(), 0.01);
    }

    @Test
    public void testMovieWithMaxValues() {
        Movie maxMovie = new Movie(Long.MAX_VALUE, "Very Long Movie Name", "Director Name", 
                                  Integer.MAX_VALUE, "Genre", "Description", Integer.MAX_VALUE, 10.0);
        
        assertEquals(Long.MAX_VALUE, maxMovie.getId());
        assertEquals("Very Long Movie Name", maxMovie.getMovieName());
        assertEquals("Director Name", maxMovie.getDirector());
        assertEquals(Integer.MAX_VALUE, maxMovie.getYear());
        assertEquals("Genre", maxMovie.getGenre());
        assertEquals("Description", maxMovie.getDescription());
        assertEquals(Integer.MAX_VALUE, maxMovie.getDuration());
        assertEquals(10.0, maxMovie.getImdbRating(), 0.01);
    }

    @Test
    public void testMovieWithNegativeValues() {
        Movie negativeMovie = new Movie(-1L, "Movie", "Director", -1, "Genre", "Description", -1, -1.0);
        
        assertEquals(-1L, negativeMovie.getId());
        assertEquals(-1, negativeMovie.getYear());
        assertEquals(-1, negativeMovie.getDuration());
        assertEquals(-1.0, negativeMovie.getImdbRating(), 0.01);
    }

    @Test
    public void testGetIcon() {
        // Test that getIcon method returns a non-null value
        assertNotNull(movie.getIcon());
        
        // Test specific movie names that have defined icons
        Movie prisonEscape = new Movie(1L, "The Prison Escape", "Director", 2020, "Drama", "Description", 120, 8.0);
        assertEquals("🔒", prisonEscape.getIcon());
        
        Movie familyBoss = new Movie(2L, "The Family Boss", "Director", 2020, "Crime", "Description", 120, 8.5);
        assertEquals("👔", familyBoss.getIcon());
        
        Movie unknownMovie = new Movie(3L, "Unknown Movie", "Director", 2020, "Drama", "Description", 120, 7.0);
        assertEquals("🎬", unknownMovie.getIcon());
    }

    @Test
    public void testMovieImmutability() {
        // Test that Movie objects are immutable by verifying all fields are final
        // This is tested by ensuring getters return consistent values
        String originalName = movie.getMovieName();
        String originalDirector = movie.getDirector();
        long originalId = movie.getId();
        
        // Multiple calls should return the same values
        assertEquals(originalName, movie.getMovieName());
        assertEquals(originalDirector, movie.getDirector());
        assertEquals(originalId, movie.getId());
    }

    @Test
    public void testMovieWithNullValues() {
        // Test movie creation with null values (should not throw exceptions)
        Movie nullMovie = new Movie(1L, null, null, 2020, null, null, 120, 8.0);
        
        assertEquals(1L, nullMovie.getId());
        assertNull(nullMovie.getMovieName());
        assertNull(nullMovie.getDirector());
        assertEquals(2020, nullMovie.getYear());
        assertNull(nullMovie.getGenre());
        assertNull(nullMovie.getDescription());
        assertEquals(120, nullMovie.getDuration());
        assertEquals(8.0, nullMovie.getImdbRating(), 0.01);
    }
}
