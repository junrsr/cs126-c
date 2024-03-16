package stores;

import java.time.LocalDate;
import java.lang.reflect.Array;

import interfaces.IMovies;
import structures.*;

public class Movies implements IMovies{
    Stores stores;
    // hashmap to store the list of movies
    private MyHashMap<Integer, Movie> movieMap;
    private MyHashMap<Integer, Collection> collectionMap;
    

    /**
     * The constructor for the Movies data store. This is where you should
     * initialise your data structures.
     * @param stores An object storing all the different key stores,
     *               including itself
     */
    public Movies(Stores stores) {
        this.stores = stores;

        this.movieMap = new MyHashMap<>();
        this.collectionMap = new MyHashMap<>();
    }

    /**
     * Adds data about a film to the data structure
     * 
     * @param id               The unique ID for the film
     * @param title            The English title of the film
     * @param originalTitle    The original language title of the film
     * @param overview         An overview of the film
     * @param tagline          The tagline for the film (empty string if there is no
     *                         tagline)
     * @param status           Current status of the film
     * @param genres           An array of Genre objects related to the film
     * @param release          The release date for the film
     * @param budget           The budget of the film in US Dollars
     * @param revenue          The revenue of the film in US Dollars
     * @param languages        An array of ISO 639 language codes for the film
     * @param originalLanguage An ISO 639 language code for the original language of
     *                         the film
     * @param runtime          The runtime of the film in minutes
     * @param homepage         The URL to the homepage of the film
     * @param adult            Whether the film is an adult film
     * @param video            Whether the film is a "direct-to-video" film
     * @param poster           The unique part of the URL of the poster (empty if
     *                         the URL is not known)
     * @return TRUE if the data able to be added, FALSE otherwise
     */
    @Override
    public boolean add(int id, String title, String originalTitle, String overview, String tagline, String status, Genre[] genres, LocalDate release, long budget, long revenue, String[] languages, String originalLanguage, double runtime, String homepage, boolean adult, boolean video, String poster) {
        // create a new movie object
        Movie newMovie = new Movie(id, title, originalTitle, overview, tagline, status, genres, release, budget, revenue, languages, originalLanguage, runtime, homepage, adult, video, poster);
        
        // add the movie to the hashmap, return success of addition
        return movieMap.put(id, newMovie);
    }

    /**
     * Removes a film from the data structure, and any data
     * added through this class related to the film
     * 
     * @param id The film ID
     * @return TRUE if the film has been removed successfully, FALSE otherwise
     */
    @Override
    public boolean remove(int id) {
        // attempt to remove the key from the hashmap, return success
        return movieMap.remove(id);
    }

    /**
     * Gets all the IDs for all films
     * 
     * @return An array of all film IDs stored
     */
    @Override
    public int[] getAllIDs() {
        // get all the keys (movies) in the hashmap
        Integer[] allFilms = movieMap.keys();

        // create a new hashmap to store ids
        int[] allIDs = new int[movieMap.size()];

        // map each entry to allIDs to the ids in allFilms
        for (int i = 0; i < movieMap.size(); i++){
            allIDs[i] = movieMap.get(allFilms[i]).id;
        }

        // return the array of IDs
        return allIDs;
    }

    /**
     * Finds the film IDs of all films released within a given range. If a film is
     * released either on the start or end dates, then that film should not be
     * included
     * 
     * @param start The start point of the range of dates
     * @param end   The end point of the range of dates
     * @return An array of film IDs that were released between start and end
     */
    @Override
    public int[] getAllIDsReleasedInRange(LocalDate start, LocalDate end) {
        Integer[] allFilms = movieMap.keys();
        int[] releasedInRange = new int[allFilms.length];
        int currentIndex = 0;

        // check each song
        for (int i = 0; i < allFilms.length; i++){
            // store the release date of the movie we are currently checking
            // System.out.println("Getting date");
            Movie targetFilm = movieMap.get(allFilms[i]);

            LocalDate target = targetFilm.release;
            // System.out.println(target.toString() + ":" + (target.isAfter(start) && target.isBefore(end)) + "\n\n\n");

            // check if the film is released between the start and end dates
            if (target != null && target.isAfter(start) && target.isBefore(end)){
                // add the film to the list and increment the pointer
                releasedInRange[currentIndex++] = targetFilm.id;
            }
        }

        
        // remove null keys by creating a subarray
        int[] finalArrayOfFilms = new int[currentIndex];
        System.arraycopy(releasedInRange, 0, finalArrayOfFilms, 0, currentIndex);

        // return the array
        return finalArrayOfFilms;
    }

    /**
     * Gets the title of a particular film, given the ID number of that film
     * 
     * @param id The movie ID
     * @return The title of the requested film. If the film cannot be found, then
     *         return null
     */
    @Override
    public String getTitle(int id) {
        // get the title of the movie with the given id in the hashmap
        Movie film = movieMap.get(id);

        // if the film doens't exist, return null
        if (film == null){
            return null;
        }

        // return the title
        return film.title;
    }

    /**
     * Gets the original title of a particular film, given the ID number of that
     * film
     * 
     * @param id The movie ID
     * @return The original title of the requested film. If the film cannot be
     *         found, then return null
     */
    @Override
    public String getOriginalTitle(int id) {
        // get the original title of the movie with the given id in the hashmap
        Movie film = movieMap.get(id);

        // if the film doens't exist, return null
        if(film == null){
            return null;
        }
        
        // return the original title
        return film.originalTitle;
    }

    /**
     * Gets the overview of a particular film, given the ID number of that film
     * 
     * @param id The movie ID
     * @return The overview of the requested film. If the film cannot be found, then
     *         return null
     */
    @Override
    public String getOverview(int id) {
        // get the overview of the movie with the given id in the hashmap
        Movie film = movieMap.get(id);

        // if the film doens't exist, return null
        if (film == null){
            return null;
        }
        
        // return the overview
        return film.overview;
    }

    /**
     * Gets the tagline of a particular film, given the ID number of that film
     * 
     * @param id The movie ID
     * @return The tagline of the requested film. If the film cannot be found, then
     *         return null
     */
    @Override
    public String getTagline(int id) {
        // get the tagline of the requested film in the hashmap
        Movie film = movieMap.get(id);

        // if the film doens't exist, return null
        if (film == null){
            return null;
        }

        // return the tagline
        return film.tagline;
    }

    /**
     * Gets the status of a particular film, given the ID number of that film
     * 
     * @param id The movie ID
     * @return The status of the requested film. If the film cannot be found, then
     *         return null
     */
    @Override
    public String getStatus(int id) {
        // get the status of the requested film in the hashmap
        Movie film = movieMap.get(id);

        // if the film doens't exist, return null
        if (film == null){
            return null;
        }
        
        // return the status
        return film.status;
    }

    /**
     * Gets the genres of a particular film, given the ID number of that film
     * 
     * @param id The movie ID
     * @return The genres of the requested film. If the film cannot be found, then
     *         return null
     */
    @Override
    public Genre[] getGenres(int id) {
        // get the genre of the requested film in the hashmap
        Movie film = movieMap.get(id);
        
        // if the film doens't exist, return null
        if (film == null){
            return null;
        }

        // return the genre
        return film.genres;
    }

    /**
     * Gets the release date of a particular film, given the ID number of that film
     * 
     * @param id The movie ID
     * @return The release date of the requested film. If the film cannot be found,
     *         then return null
     */
    @Override
    public LocalDate getRelease(int id) {
        // get the release date for the desired movie from the hashmap
        Movie film = movieMap.get(id);

        // if the film doens't exist, return null
        if (film == null){
            return null;
        }
        
        // return the release date
        return film.release;
    }

    /**
     * Gets the budget of a particular film, given the ID number of that film
     * 
     * @param id The movie ID
     * @return The budget of the requested film. If the film cannot be found, then
     *         return -1
     */
    @Override
    public long getBudget(int id) {
        // get the budget for the desired movie from the hashmap
        Movie film = movieMap.get(id);

        // if the film doens't exist, return -1
        if (film == null){
            return -1;
        }
        
        // return the budget
        return film.budget;
    }

    /**
     * Gets the revenue of a particular film, given the ID number of that film
     * 
     * @param id The movie ID
     * @return The revenue of the requested film. If the film cannot be found, then
     *         return -1
     */
    @Override
    public long getRevenue(int id) {
        // get the revenue of the desired film
        Movie film = movieMap.get(id);
        
        // if the film doens't exist, return -1
        if (film == null){
            return -1;
        }

        // return the revenue
        return film.revenue;
    }

    /**
     * Gets the languages of a particular film, given the ID number of that film
     * 
     * @param id The movie ID
     * @return The languages of the requested film. If the film cannot be found,
     *         then return null
     */
    @Override
    public String[] getLanguages(int id) {
        // get the languages stored for the given filmId in the hashmap
        Movie film = movieMap.get(id);

        // if the film doens't exist, return null
        if (film == null){
            return null;
        }

        // return the languages
        return film.languages;
        
    }

    /**
     * Gets the original language of a particular film, given the ID number of that
     * film
     * 
     * @param id The movie ID
     * @return The original language of the requested film. If the film cannot be
     *         found, then return null
     */
    @Override
    public String getOriginalLanguage(int id) {
        // get the original language assocaited with a film
        Movie film = movieMap.get(id);

        // if the film doens't exist, return null
        if (film == null){
            return null;
        }
        
        // return the original language
        return film.originalLanguage;
    }

    /**
     * Gets the runtime of a particular film, given the ID number of that film
     * 
     * @param id The movie ID
     * @return The runtime of the requested film. If the film cannot be found, then
     *         return -1
     */
    @Override
    public double getRuntime(int id) {
        // get the runtime of the assocaited film in the hashmap
        Movie film = movieMap.get(id);

        // if the film doens't exist, return -1
        if (film == null){
            return -1;
        }

        // return the runtime
        return film.runtime;
    }

    /**
     * Gets the homepage of a particular film, given the ID number of that film
     * 
     * @param id The movie ID
     * @return The homepage of the requested film. If the film cannot be found, then
     *         return null
     */
    @Override
    public String getHomepage(int id) {
        // get the homepage for the assocaited film
        Movie film = movieMap.get(id);

        // if the film doens't exist, return null
        if (film == null){
            return null;
        }
        
        // return the homepage
        return film.homepage;
    }

    /**
     * Gets weather a particular film is classed as "adult", given the ID number of
     * that film
     * 
     * @param id The movie ID
     * @return The "adult" status of the requested film. If the film cannot be
     *         found, then return false
     */
    @Override
    public boolean getAdult(int id) {
        // get whether the film is an adult film
        Movie film = movieMap.get(id);

        // if the film doens't exist, return null
        if (film == null){
            return false;
        }
        
        // return the status
        return film.adult;
    }

    /**
     * Gets weather a particular film is classed as "direct-to-video", given the ID
     * number of that film
     * 
     * @param id The movie ID
     * @return The "direct-to-video" status of the requested film. If the film
     *         cannot be found, then return false
     */
    @Override
    public boolean getVideo(int id) {
        // if the film is classes as "direct-to-video"
        Movie film = movieMap.get(id);

        // if the film doens't exist, return false
        if (film == null){
            return false;
        }
        
        // return this
        return film.video;
    }

    /**
     * Gets the poster URL of a particular film, given the ID number of that film
     * 
     * @param id The movie ID
     * @return The poster URL of the requested film. If the film cannot be found,
     *         then return null
     */
    @Override
    public String getPoster(int id) {
        // get the poster for the associated film from the hashmap
        Movie film = movieMap.get(id);

        // if the film doens't exist, return null
        if (film == null){
            return null;
        }
        
        // return the poster
        return film.poster;
    }

    /**
     * Sets the average IMDb score and the number of reviews used to generate this
     * score, for a particular film
     * 
     * @param id          The movie ID
     * @param voteAverage The average score on IMDb for the film
     * @param voteCount   The number of reviews on IMDb that were used to generate
     *                    the average score for the film
     * @return TRUE if the data able to be added, FALSE otherwise
     */
    @Override
    public boolean setVote(int id, double voteAverage, int voteCount) {
        // get the movie in question
        Movie film = movieMap.get(id);
        
        // check if the film exists
        if(film != null){
            // if the film exists, add the votes
            film.voteAverage = voteAverage;
            film.voteCount = voteCount;

            // return true as we have successfully
            return true;
        }
        
        // if the film doesn't exist, return false
        return false;
    }

    /**
     * Gets the average score for IMDb reviews of a particular film, given the ID
     * number of that film
     * 
     * @param id The movie ID
     * @return The average score for IMDb reviews of the requested film. If the film
     *         cannot be found, then return -1
     */
    @Override
    public double getVoteAverage(int id) {
        // get the vote average for the associated film
        Movie film = movieMap.get(id);
        
        // if the film doens't exist, return -1
        if (film == null){
            return -1;
        }

        // return the vote average for the film in question
        return film.voteAverage;
    }

    /**
     * Gets the amount of IMDb reviews used to generate the average score of a
     * particular film, given the ID number of that film
     * 
     * @param id The movie ID
     * @return The amount of IMDb reviews used to generate the average score of the
     *         requested film. If the film cannot be found, then return -1
     */
    @Override
    public int getVoteCount(int id) {
        // gets the number of votes a particular film has
        Movie film = movieMap.get(id);

        // if the film doesn't exist, return -1
        if (film == null){
            return -1;
        }
        
        // returns the vote count of the film
        return film.voteCount;
    }

    /**
     * Adds a given film to a collection. The collection is required to have an ID
     * number, a name, and a URL to a poster for the collection
     * 
     * @param filmID                 The movie ID
     * @param collectionID           The collection ID
     * @param collectionName         The name of the collection
     * @param collectionPosterPath   The URL where the poster can
     *                               be found
     * @param collectionBackdropPath The URL where the backdrop can
     *                               be found
     * @return TRUE if the data able to be added, FALSE otherwise
     */
    @Override
    public boolean addToCollection(int filmID, int collectionID, String collectionName, String collectionPosterPath, String collectionBackdropPath) {
        // if the film doesn't exist or the collection id is invalid
        if(movieMap.get(filmID) == null || collectionID < 0){
            return false;
        }
        
        // if the collection already exists in t he map
        if (!collectionMap.containsKey(collectionID)) {
            // create a new collection
            Collection newCollection = new Collection(collectionID, collectionName, collectionPosterPath, collectionBackdropPath);
            
            // add the new collection to the hashmap
            collectionMap.put(collectionID, newCollection); // todo getting an ArrayIndexOutOfBoundsException here
        }

        // retrieve the collection from the hashmap
        Collection collection = collectionMap.get(collectionID);
        
        // add the film to the films list in the hashmap
        collection.films.put(filmID, filmID);

        // adds the collection back to the hashap
        collectionMap.put(collectionID, collection);

        // update the collection ID for the given film
        Movie film = movieMap.get(filmID);
        film.collectionID = collectionID;
        movieMap.put(filmID, film);
        
        return true;
    }

    /**
     * Get all films that belong to a given collection
     * 
     * @param collectionID The collection ID to be searched for
     * @return An array of film IDs that correspond to the given collection ID. If
     *         there are no films in the collection ID, or if the collection ID is
     *         not valid, return an empty array.
     */
    @Override
    public int[] getFilmsInCollection(int collectionID) {
        Collection collection = collectionMap.get(collectionID);

        // return an empty array if the key is invalid
        if (collection == null){
            return new int[0];
        }

        // MyHashMap<Integer, Integer> filmCollection = collection.films;
        
        // create a new array to return the films in the collection
        Integer[] keys = collection.films.keys();
        int[] newArr = new int[keys.length];
        
        // convert each film into the appropriate integer format
        for (int i = 0; i < keys.length; i++){
            newArr[i] = keys[i];
        }
        
        // return the film
        return newArr;
    }

    /**
     * Gets the name of a given collection
     * 
     * @param collectionID The collection ID
     * @return The name of the collection. If the collection cannot be found, then
     *         return null
     */
    @Override
    public String getCollectionName(int collectionID) {
        // get the collection from the hashmap
        Collection collection = collectionMap.get(collectionID);

        // return an null if the key is invalid
        if (collection == null){
            return null;
        }

        // return the name of the collection
        return collection.collectionName;
    }

    /**
     * Gets the poster URL for a given collection
     * 
     * @param collectionID The collection ID
     * @return The poster URL of the collection. If the collection cannot be found,
     *         then return null
     */
    @Override
    public String getCollectionPoster(int collectionID) {
        // get the collection from the hashmap
        Collection collection = collectionMap.get(collectionID);

        // return an null if the key is invalid
        if (collection == null){
            return null;
        }

        // returns the collection poster path
        return collection.collectionPosterPath;
    }

    /**
     * Gets the backdrop URL for a given collection
     * 
     * @param collectionID The collection ID
     * @return The backdrop URL of the collection. If the collection cannot be
     *         found, then return null
     */
    @Override
    public String getCollectionBackdrop(int collectionID) {
        // get the collection from the hashmap
        Collection collection = collectionMap.get(collectionID);

        // return an null if the key is invalid
        if (collection == null){
            return null;
        }

        // returns the backdrop path for the collection
        return collection.collectionBackdropPath;
    }

    /**
     * Gets the collection ID of a given film
     * 
     * @param filmID The movie ID
     * @return The collection ID for the requested film. If the film cannot be
     *         found, then return -1
     */
    @Override
    public int getCollectionID(int filmID) {
        Movie film = movieMap.get(filmID);

        if (film == null){
            return -1;
        }
        
        return film.collectionID;
    }

    /**
     * Sets the IMDb ID for a given film
     * 
     * @param filmID The movie ID
     * @param imdbID The IMDb ID
     * @return TRUE if the data able to be set, FALSE otherwise
     */
    @Override
    public boolean setIMDB(int filmID, String imdbID) {
        // get the film we want to modify
        Movie film = movieMap.get(filmID);

        // if the film exists
        if (film != null){
            // update its imdb value
            film.imdbID = imdbID;

            // return true, signifying completion
            return true;
        }
        
        // return false on failure
        return false;
    }

    /**
     * Gets the IMDb ID for a given film
     * 
     * @param filmID The movie ID
     * @return The IMDb ID for the requested film. If the film cannot be found,
     *         return null
     */
    @Override
    public String getIMDB(int filmID) {
        // get the film we want to retrieve data from
        Movie film = movieMap.get(filmID);

        // if the film exists
        if (film != null){
            return film.imdbID;
        }
        
        // return null if the film doesn't exist
        return null;
    }

    /**
     * Sets the popularity of a given film. If the popularity for a film already exists, replace it with the new value
     * 
     * @param id         The movie ID
     * @param popularity The popularity of the film
     * @return TRUE if the data able to be set, FALSE otherwise
     */
    @Override
    public boolean setPopularity(int id, double popularity) {
        // get the film we want to modify information for
        Movie film = movieMap.get(id);

        // if the film exists
        if (film != null){
            // set the popularity
            film.popularity = popularity;

            // return true indicating successful addition
            return true;
        }
        
        // return false if film doesn't exist
        return false;
    }

    /**
     * Gets the popularity of a given film
     * 
     * @param id The movie ID
     * @return The popularity value of the requested film. If the film cannot be
     *         found, then return -1.0. If the popularity has not been set, return 0.0
     */
    @Override
    public double getPopularity(int id) {
        Movie film = movieMap.get(id);

        // if the film exists
        if(film != null){
            // return the popularity of the film
            return film.popularity;
        }
        
        // return -1 if the film doens't exist
        return -1;
    }

    /**
     * Adds a production company to a given film
     * 
     * @param id      The movie ID
     * @param company A Company object that represents the details on a production
     *                company
     * @return TRUE if the data able to be added, FALSE otherwise
     */
    @Override
    public boolean addProductionCompany(int id, Company company) {
        Movie film = movieMap.get(id);

        // if the film doesn't exist
        if(film == null){
            return false;
        }

        // add the comapny to the companies hashmap
        film.companies.put(company.getID(), company);
        
        // update the movie map
        movieMap.put(id, film);
        
        return true;
    }

    /**
     * Adds a production country to a given film
     * 
     * @param id      The movie ID
     * @param country A ISO 3166 string containing the 2-character country code
     * @return TRUE if the data able to be added, FALSE otherwise
     */
    @Override
    public boolean addProductionCountry(int id, String country) {
        Movie film = movieMap.get(id);

        // if the film doesn't exist, return false
        if (film == null){
            return false;
        }

        // add the country to the list
        film.countries.put(country.hashCode(), country);
        movieMap.put(id, film);

        // return true on completion
        return true;
    }

    /**
     * Gets all the production companies for a given film
     * 
     * @param id The movie ID
     * @return An array of Company objects that represent all the production
     *         companies that worked on the requested film. If the film cannot be
     *         found, then return null
     */
    @Override
    public Company[] getProductionCompanies(int id) {
        Movie film = movieMap.get(id);

        // return null if the film doesn't exist
        if(film == null){
            return null;
        }

        // get the map of companies
        MyHashMap<Integer, Company> companyMap = film.companies;

        // get a list of all company IDs in the collection
        Integer[] companyIDs = companyMap.keys();

        // create a list of all the companies
        Company[] companyList = new Company[companyMap.size()];

        // get the company object associated with each company ID and store it in the company list
        for (int i = 0; i < companyMap.size(); i++){
            companyList[i] = companyMap.get(companyIDs[i]);
        }

        // return the company list        
        return companyList;
    }

    /**
     * Gets all the production companies for a given film
     * 
     * @param id The movie ID
     * @return An array of Strings that represent all the production countries (in
     *         ISO 3166 format) that worked on the requested film. If the film
     *         cannot be found, then return null
     */
    @Override
    public String[] getProductionCountries(int id) {
        Movie film = movieMap.get(id);

        // if the film doesn't exist
        if (film == null){
            // return null
            return null;
        }
        
        Integer[] keys = film.countries.keys();

        // create a new array to return the films in the collection
        String[] newArr = new String[keys.length];
        
        // convert each film into the appropriate integer format
        for (int i = 0; i < keys.length; i++){
            newArr[i] = film.countries.get(keys[i]);
        }
        
        // return the film
        return newArr;
    }

    /**
     * States the number of movies stored in the data structure
     * 
     * @return The number of movies stored in the data structure
     */
    @Override
    public int size() {
        // return the size of the movie map
        return movieMap.size();
    }

    /**
     * Produces a list of movie IDs that have the search term in their title,
     * original title or their overview. O(n * m)
     * 
     * @param searchTerm The term that needs to be checked
     * @return An array of movie IDs that have the search term in their title,
     *         original title or their overview. If no movies have this search term,
     *         then an empty array should be returned
     */
    @Override
    public int[] findFilms(String searchTerm) {
        Integer[] listOfFilmIDs = movieMap.keys();
        
        // array to add all films which meet the criteria
        int[] matchingFilms = new int[listOfFilmIDs.length];
        int index = 0;

        // check each film
        for (int i = 0; i < listOfFilmIDs.length; i++){
            if(listOfFilmIDs[i] != null){
                // get the film we want to check
                Integer currentKey = listOfFilmIDs[i];
                
                // if the search term is in the title, original title or overview
                if (getTitle(currentKey).contains(searchTerm) || getOriginalTitle(currentKey).contains(searchTerm) || getOverview(currentKey).contains(searchTerm)){
                    // add the current key to the list of matching films
                    matchingFilms[index++] = currentKey;
                }
            }
        }
        
        // remove null keys by creating a subarray
        int[] finalArrayOfFilms = new int[index];
        System.arraycopy(matchingFilms, 0, finalArrayOfFilms, 0, index);
        
        // return the new list
        return finalArrayOfFilms;
    }
}



class Movie{
    int id;
    String title;
    String originalTitle;
    String overview;
    String tagline;
    String status;
    Genre[] genres; 
    LocalDate release;
    long budget;
    long revenue;
    String[] languages;
    String originalLanguage;
    double runtime;
    String homepage;
    boolean adult;
    boolean video;
    String poster;

    double voteAverage;
    int voteCount;
    String imdbID;
    double popularity;
    int collectionID;

    MyHashMap<Integer, Company> companies = new MyHashMap<>();
    MyHashMap<Integer, String> countries = new MyHashMap<>();


    public Movie(int id, String title, String originalTitle, String overview, String tagline, String status, Genre[] genres, LocalDate release, long budget, long revenue, String[] languages, String originalLanguage, double runtime, String homepage, boolean adult, boolean video, String poster){
        this.id = id;
        this.title = title;
        this.originalTitle = originalTitle;
        this.overview = overview;
        this.tagline = tagline;
        this.status = status;
        this.genres = genres;
        this.release = release;
        this.budget = budget;
        this.revenue = revenue;
        this.languages = languages;
        this.originalLanguage = originalLanguage;
        this.runtime = runtime;
        this.homepage = homepage;
        this.adult = adult;
        this.video = video;
        this.poster = poster;
    }

}


class Collection{
    int collectionID;
    String collectionName;
    String collectionPosterPath;
    String collectionBackdropPath;
    
    MyHashMap<Integer, Integer> films = new MyHashMap<>();


    public Collection(int collectionID, String collectionName, String collectionPosterPath, String collectionBackdropPath){
        this.collectionID = collectionID;
        this.collectionName = collectionName;
        this.collectionPosterPath = collectionPosterPath;
        this.collectionBackdropPath = collectionBackdropPath;
    }
}