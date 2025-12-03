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
                model.addAttribute("title", "Invalid Search Parameters");
                model.addAttribute("message", "Arrr! That treasure map ID be invalid, matey! Please provide a valid ID greater than 0.");
                return "error";
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
            
        } catch (Exception e) {
            logger.error("Batten down the hatches! Error during treasure hunt: {}", e.getMessage());
            model.addAttribute("title", "Search Error");
            model.addAttribute("message", 
                "Shiver me timbers! Something went wrong during the treasure hunt. " +
                "Please try again, or the crew will have to investigate this scurvy bug!");
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