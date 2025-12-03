# Movie Service - Spring Boot Demo Application 🏴‍☠️

Ahoy matey! Welcome to the Pirate's Movie Treasure Collection - a swashbuckling movie catalog web application built with Spring Boot, demonstrating Java application development best practices with a pirate twist!

## Features

- **Movie Treasure Collection**: Browse 12 classic movie treasures with detailed information
- **Treasure Hunt (Search & Filter)**: 🔍 Search for movie treasures by name, ID, or genre with our advanced treasure hunting system!
- **Movie Details**: View comprehensive information including captain (director), year discovered, treasure type (genre), adventure length (duration), and description
- **Customer Reviews**: Each movie includes authentic customer reviews with ratings and avatars
- **Responsive Design**: Mobile-first design that works on all devices
- **Modern UI**: Dark theme with gradient backgrounds and smooth animations
- **Pirate Language**: Full pirate-themed interface with "Arrr!" and nautical terminology

## Technology Stack

- **Java 8**
- **Spring Boot 2.0.5**
- **Maven** for dependency management
- **Thymeleaf** for server-side templating
- **Log4j 2.20.0**
- **JUnit 5.8.2**

## Quick Start

### Prerequisites

- Java 8 or higher
- Maven 3.6+

### Run the Application

```bash
git clone https://github.com/<youruser>/sample-qdev-movies.git
cd sample-qdev-movies
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

### Access the Application

- **Movie Treasure Collection**: http://localhost:8080/movies
- **Movie Details**: http://localhost:8080/movies/{id}/details (where {id} is 1-12)
- **Treasure Hunt (Search)**: Use the search form on the main page or directly access http://localhost:8080/movies/search

## Building for Production

```bash
mvn clean package
java -jar target/sample-qdev-movies-0.1.0.jar
```

## Project Structure

```
src/
├── main/
│   ├── java/
│   │   └── com/amazonaws/samples/qdevmovies/
│   │       ├── MoviesApplication.java    # Main Spring Boot application
│   │       ├── MoviesController.java     # REST controller for movie endpoints
│   │       ├── MovieService.java         # Business logic with search capabilities
│   │       ├── Movie.java                # Movie data model
│   │       ├── Review.java               # Review data model
│   │       └── utils/
│   │           ├── MovieIconUtils.java   # Movie icon utilities
│   │           └── MovieUtils.java       # Movie validation utilities
│   └── resources/
│       ├── templates/
│       │   ├── movies.html               # Main treasure collection page with search
│       │   ├── movie-details.html        # Individual movie details
│       │   └── error.html                # Pirate-themed error page
│       ├── application.yml               # Application configuration
│       ├── movies.json                   # Movie treasure data
│       ├── mock-reviews.json             # Mock review data
│       └── log4j2.xml                    # Logging configuration
└── test/                                 # Comprehensive unit tests
```

## API Endpoints

### Get All Movie Treasures
```
GET /movies
```
Returns an HTML page displaying all movie treasures with ratings, basic information, and a treasure hunt search form.

### Search Movie Treasures (NEW! 🔍)
```
GET /movies/search
```
Ahoy! Search through our treasure collection using various criteria!

**Query Parameters:**
- `name` (optional): Movie name to search for (case-insensitive partial match)
  - Example: `name=prison` will find "The Prison Escape"
- `id` (optional): Specific movie ID to find (1-12)
  - Example: `id=1` will find the movie with ID 1
- `genre` (optional): Genre to filter by (case-insensitive partial match)
  - Example: `genre=drama` will find all Drama movies

**Examples:**
```bash
# Search by movie name
http://localhost:8080/movies/search?name=family

# Search by specific treasure ID
http://localhost:8080/movies/search?id=5

# Search by treasure type (genre)
http://localhost:8080/movies/search?genre=action

# Combine multiple search criteria
http://localhost:8080/movies/search?name=the&genre=crime

# Search with partial matches
http://localhost:8080/movies/search?name=war&genre=sci
```

**Response Features:**
- 🏴‍☠️ Pirate-themed success and error messages
- 📊 Search result summaries with treasure count
- 🔍 Maintains search parameters in form for easy refinement
- ⚠️ Handles edge cases like invalid IDs and empty results
- 🎯 Returns filtered movie list in the same format as main page

### Get Movie Details
```
GET /movies/{id}/details
```
Returns an HTML page with detailed movie information and customer reviews.

**Parameters:**
- `id` (path parameter): Movie ID (1-12)

**Example:**
```
http://localhost:8080/movies/1/details
```

## Search Features

### Treasure Hunt Capabilities 🔍

Our advanced treasure hunting system allows ye to:

1. **Search by Movie Name**: Find treasures by partial name matches (case-insensitive)
2. **Search by Treasure ID**: Locate specific treasures using their unique ID
3. **Filter by Genre**: Discover treasures by their type (Drama, Action, Sci-Fi, etc.)
4. **Combine Criteria**: Use multiple search parameters for precise treasure hunting
5. **Smart Validation**: Handles invalid inputs with helpful pirate-themed error messages

### Search Form Features

- 🎬 **Movie Name Field**: Text input with placeholder guidance
- 🗺️ **Treasure Map ID**: Number input with validation (1-12)
- 🏴‍☠️ **Treasure Type Dropdown**: Populated with all available genres
- 🔍 **Search Button**: Initiates the treasure hunt
- 🧭 **Clear Button**: Returns to full treasure collection

### Edge Case Handling

- **Empty Results**: Displays pirate-themed "no treasures found" message
- **Invalid ID**: Shows error page with helpful guidance
- **Server Errors**: Graceful error handling with pirate flair
- **Parameter Validation**: Client and server-side validation

## Available Movie Treasures

Our treasure chest contains 12 classic movies:

1. **The Prison Escape** (Drama) - 1994
2. **The Family Boss** (Crime/Drama) - 1972
3. **The Masked Hero** (Action/Crime) - 2008
4. **Urban Stories** (Crime/Drama) - 1994
5. **Life Journey** (Drama/Romance) - 1994
6. **Dream Heist** (Action/Sci-Fi) - 2010
7. **The Virtual World** (Action/Sci-Fi) - 1999
8. **The Wise Guys** (Crime/Drama) - 1990
9. **The Quest for the Ring** (Adventure/Fantasy) - 2001
10. **Space Wars: The Beginning** (Adventure/Sci-Fi) - 1977
11. **The Factory Owner** (Drama/History) - 1993
12. **Underground Club** (Drama/Thriller) - 1999

## Troubleshooting

### Port 8080 already in use

Run on a different port:
```bash
mvn spring-boot:run -Dspring-boot.run.arguments=--server.port=8081
```

### Build failures

Clean and rebuild:
```bash
mvn clean compile
```

### Search not working

1. Check that all movie data is loaded properly
2. Verify search parameters are correctly formatted
3. Check application logs for any treasure hunting errors

## Testing

Run the comprehensive test suite:
```bash
mvn test
```

Our test coverage includes:
- **MovieService Tests**: Search functionality, edge cases, and data validation
- **MoviesController Tests**: Endpoint behavior, parameter handling, and error scenarios
- **Integration Tests**: End-to-end treasure hunting workflows

## Contributing

This project is designed as a demonstration application. Feel free to:
- Add more movie treasures to the collection
- Enhance the pirate-themed UI/UX
- Improve search functionality (pagination, advanced filters)
- Add new features like user reviews or treasure favorites
- Enhance the responsive design for mobile treasure hunters

## Pirate Language Guide 🏴‍☠️

Throughout the application, you'll encounter these pirate terms:
- **Treasures**: Movies
- **Treasure Hunt**: Search functionality
- **Treasure Chest**: Movie collection
- **Treasure Map ID**: Movie ID
- **Captain**: Director
- **Adventure Length**: Movie duration
- **Treasure Type**: Genre
- **Year Discovered**: Release year

## License

This sample code is licensed under the MIT-0 License. See the LICENSE file.

---

*Arrr! May fair winds fill yer sails as ye explore our movie treasure collection, matey! 🏴‍☠️*
