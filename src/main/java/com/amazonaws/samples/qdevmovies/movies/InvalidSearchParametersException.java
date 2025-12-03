package com.amazonaws.samples.qdevmovies.movies;

/**
 * Arrr! This exception be thrown when a landlubber provides invalid search parameters!
 * Custom exception for invalid search criteria in our treasure hunting operations.
 */
public class InvalidSearchParametersException extends RuntimeException {
    
    public InvalidSearchParametersException(String message) {
        super(message);
    }
    
    public InvalidSearchParametersException(String message, Throwable cause) {
        super(message, cause);
    }
}