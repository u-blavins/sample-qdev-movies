package com.amazonaws.samples.qdevmovies.movies;

import com.amazonaws.samples.qdevmovies.utils.MovieIconUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Optional;

@Controller
public class MoviesController {
    private static final Logger logger = LogManager.getLogger(MoviesController.class);

    @Autowired
    private MovieService movieService;

    @Autowired
    private ReviewService reviewService;

    @GetMapping("/movies")
    public String getMovies(org.springframework.ui.Model model) {
        logger.info("Fetching movies");
        model.addAttribute("movies", movieService.getAllMovies());
        model.addAttribute("genres", movieService.getAllGenres());
        return "movies";
    }

    @GetMapping("/movies/search")
    public String searchMovieTreasures(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "id", required = false) Long id,
            @RequestParam(value = "genre", required = false) String genre,
            org.springframework.ui.Model model) {
        
        logger.info("Ahoy! Treasure hunt initiated with parameters - name: '{}', id: {}, genre: '{}'", 
                   name, id, genre);
        
        try {
            // Validate ID parameter if provided
            if (id != null && id <= 0) {
                logger.warn("Blimey! Invalid treasure map ID provided: {}", id);
                throw new InvalidSearchParametersException("Arrr! That treasure map ID be invalid, matey! Please provide a valid ID greater than 0.");
            }
            
            // Perform the treasure hunt!
            List<Movie> treasureChest = movieService.searchMovieTreasures(name, id, genre);
            
            // Prepare the response with pirate flair
            model.addAttribute("movies", treasureChest);
            model.addAttribute("genres", movieService.getAllGenres());
            model.addAttribute("searchPerformed", true);
            model.addAttribute("searchName", name);
            model.addAttribute("searchId", id);
            model.addAttribute("searchGenre", genre);
            
            if (treasureChest.isEmpty()) {
                logger.info("Shiver me timbers! No treasures found matching the search criteria");
                model.addAttribute("noResultsMessage", 
                    "Blimey! No movie treasures found matching yer search criteria, matey! " +
                    "Try adjusting yer search terms and chart a new course!");
            } else {
                logger.info("Yo ho ho! Found {} movie treasures for the search", treasureChest.size());
                String resultMessage = treasureChest.size() == 1 ? 
                    "Arrr! Found 1 perfect treasure for ye!" :
                    String.format("Yo ho ho! Discovered %d movie treasures matching yer search!", treasureChest.size());
                model.addAttribute("resultsMessage", resultMessage);
            }
            
            return "movies";
            
        } catch (InvalidSearchParametersException e) {
            logger.error("Invalid search parameters provided: {}", e.getMessage());
            model.addAttribute("title", "Invalid Search Parameters");
            model.addAttribute("message", e.getMessage());
            return "error";
        } catch (MovieServiceException e) {
            logger.error("Movie service error during treasure hunt: {}", e.getMessage());
            model.addAttribute("title", "Search Service Error");
            model.addAttribute("message", 
                "Shiver me timbers! Our treasure hunting service encountered an error. " +
                "Please try again, or the crew will have to investigate this scurvy bug!");
            return "error";
        } catch (IllegalArgumentException e) {
            logger.error("Invalid argument provided for search: {}", e.getMessage());
            model.addAttribute("title", "Invalid Search Input");
            model.addAttribute("message", 
                "Arrr! Invalid search input provided, matey! " + e.getMessage());
            return "error";
        } catch (RuntimeException e) {
            logger.error("Unexpected runtime error during treasure hunt: {}", e.getMessage());
            model.addAttribute("title", "Unexpected Search Error");
            model.addAttribute("message", 
                "Batten down the hatches! An unexpected error occurred during the treasure hunt. " +
                "Please try again, or contact the crew if this problem persists!");
            return "error";
        }
    }

    @GetMapping("/movies/{id}/details")
    public String getMovieDetails(@PathVariable("id") Long movieId, org.springframework.ui.Model model) {
        logger.info("Fetching details for movie ID: {}", movieId);
        
        Optional<Movie> movieOpt = movieService.getMovieById(movieId);
        if (!movieOpt.isPresent()) {
            logger.warn("Movie with ID {} not found", movieId);
            model.addAttribute("title", "Movie Not Found");
            model.addAttribute("message", "Movie with ID " + movieId + " was not found.");
            return "error";
        }
        
        Movie movie = movieOpt.get();
        model.addAttribute("movie", movie);
        model.addAttribute("movieIcon", MovieIconUtils.getMovieIcon(movie.getMovieName()));
        model.addAttribute("allReviews", reviewService.getReviewsForMovie(movie.getId()));
        
        return "movie-details";
    }
}