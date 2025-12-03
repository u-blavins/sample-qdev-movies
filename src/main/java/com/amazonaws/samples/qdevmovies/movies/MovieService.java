package com.amazonaws.samples.qdevmovies.movies;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;

@Service
public class MovieService {
    private static final Logger logger = LogManager.getLogger(MovieService.class);
    private final List<Movie> movies;
    private final Map<Long, Movie> movieMap;

    public MovieService() {
        this.movies = loadMoviesFromJson();
        this.movieMap = new HashMap<>();
        for (Movie movie : movies) {
            movieMap.put(movie.getId(), movie);
        }
    }

    private List<Movie> loadMoviesFromJson() {
        List<Movie> movieList = new ArrayList<>();
        try {
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("movies.json");
            if (inputStream != null) {
                Scanner scanner = new Scanner(inputStream, StandardCharsets.UTF_8.name());
                String jsonContent = scanner.useDelimiter("\\A").next();
                scanner.close();
                
                JSONArray moviesArray = new JSONArray(jsonContent);
                for (int i = 0; i < moviesArray.length(); i++) {
                    JSONObject movieObj = moviesArray.getJSONObject(i);
                    movieList.add(new Movie(
                        movieObj.getLong("id"),
                        movieObj.getString("movieName"),
                        movieObj.getString("director"),
                        movieObj.getInt("year"),
                        movieObj.getString("genre"),
                        movieObj.getString("description"),
                        movieObj.getInt("duration"),
                        movieObj.getDouble("imdbRating")
                    ));
                }
            }
        } catch (Exception e) {
            logger.error("Failed to load movies from JSON: {}", e.getMessage());
        }
        return movieList;
    }

    public List<Movie> getAllMovies() {
        return movies;
    }

    public Optional<Movie> getMovieById(Long id) {
        if (id == null || id <= 0) {
            return Optional.empty();
        }
        return Optional.ofNullable(movieMap.get(id));
    }

    /**
     * Ahoy matey! Search for movie treasures using various criteria.
     * This method charts a course through our movie collection to find the perfect treasures!
     * 
     * @param name Movie name to search for (case-insensitive partial match)
     * @param id Specific movie ID to find
     * @param genre Genre to filter by (case-insensitive partial match)
     * @return List of movies matching the search criteria
     */
    public List<Movie> searchMovieTreasures(String name, Long id, String genre) {
        logger.info("Ahoy! Starting treasure hunt with criteria - name: '{}', id: {}, genre: '{}'", 
                   name, id, genre);
        
        List<Movie> treasureChest = new ArrayList<>();
        
        // If searching by ID, that takes priority - a specific treasure map!
        if (id != null && id > 0) {
            Optional<Movie> specificTreasure = getMovieById(id);
            if (specificTreasure.isPresent()) {
                // Check if the found treasure also matches other criteria
                Movie movie = specificTreasure.get();
                if (matchesSearchCriteria(movie, name, genre)) {
                    treasureChest.add(movie);
                    logger.info("Arrr! Found the exact treasure ye be lookin' for with ID: {}", id);
                } else {
                    logger.info("Found treasure with ID {}, but it doesn't match other search criteria, matey!", id);
                }
            } else {
                logger.warn("Shiver me timbers! No treasure found with ID: {}", id);
            }
            return treasureChest;
        }
        
        // Search through all our movie treasures
        for (Movie movie : movies) {
            if (matchesSearchCriteria(movie, name, genre)) {
                treasureChest.add(movie);
            }
        }
        
        logger.info("Treasure hunt complete! Found {} movie treasures matching yer criteria", treasureChest.size());
        return treasureChest;
    }
    
    /**
     * Helper method to check if a movie matches the search criteria
     * Like checking if a treasure matches what we're hunting for!
     */
    private boolean matchesSearchCriteria(Movie movie, String name, String genre) {
        // Check name criteria (case-insensitive partial match)
        if (name != null && !name.trim().isEmpty()) {
            String movieName = movie.getMovieName().toLowerCase();
            String searchName = name.trim().toLowerCase();
            if (!movieName.contains(searchName)) {
                return false;
            }
        }
        
        // Check genre criteria (case-insensitive partial match)
        if (genre != null && !genre.trim().isEmpty()) {
            String movieGenre = movie.getGenre().toLowerCase();
            String searchGenre = genre.trim().toLowerCase();
            if (!movieGenre.contains(searchGenre)) {
                return false;
            }
        }
        
        return true;
    }
    
    /**
     * Get all unique genres from our treasure collection
     * Perfect for helping landlubbers choose what kind of treasure they want!
     */
    public List<String> getAllGenres() {
        return movies.stream()
                .map(Movie::getGenre)
                .distinct()
                .sorted()
                .collect(java.util.stream.Collectors.toList());
    }
}
