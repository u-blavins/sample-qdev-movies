package com.amazonaws.samples.qdevmovies.movies;

/**
 * Shiver me timbers! This exception be thrown when something goes wrong in our movie service!
 * Custom exception for movie service operations that encounter unexpected errors.
 */
public class MovieServiceException extends RuntimeException {
    
    public MovieServiceException(String message) {
        super(message);
    }
    
    public MovieServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}