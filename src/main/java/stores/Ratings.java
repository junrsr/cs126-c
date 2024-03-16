package stores;

import java.time.LocalDateTime;
import structures.*;
import interfaces.IRatings;

import java.util.Arrays;

public class Ratings implements IRatings {
    private Stores stores;
    
    // create two hashmaps -> one to store data about users and one to store data about movies
    private MyHashMap<Integer, MyAVLTree<Rating>> movieRatingsMap;
    private MyHashMap<Integer, MyAVLTree<Rating>> userRatingsMap;

    public Ratings(Stores stores) {
        // create new instances of the hashmaps + stores structure
        this.stores = stores;
        this.movieRatingsMap = new MyHashMap<>();
        this.userRatingsMap = new MyHashMap<>();
    }

    /**
     * Insert the rating into the HashMap / AVL Tree if it doesn't exist. This method takes O(1) when accessing the ahshmap, O(log n) time when checking the
     * avl tree and O(log n) when inserting to the avl tree. Overall, it takes O(log m + log u) time to add a rating, where m is the number of movie ratings for
     * a given movie and u is the number of ratings for a given user. This can further be simplified to O(log n) time. The space complexity of this method is O(N).
     * The space required for each AVL tree being O(n) where n is the number of rating entries and the hashmaps themselves take O(N) space, where N is the max
     * capacity of a hashmap
     * 
     * @param userid the id of the user rating the movie
     * @param movieid the id of the movie being rated
     * @param rating the actual rating of the movie
     * @param timestamp the timestamp of the rating
     * 
     * @return whether the addition was successful or not
     */
    @Override
    public boolean add(int userid, int movieid, float rating, LocalDateTime timestamp) {
        // create a new Rating object to store the rating
        Rating newRating = new Rating(userid, movieid, rating, timestamp);
        
        // update the movie hashmap

        // get the avl tree associated with the movie
        MyAVLTree<Rating> movieRatings = movieRatingsMap.get(movieid);
        // check to see if already in
        if(movieRatings != null && movieRatings.contains(newRating)){
            return false;
        }
        // if an AVL tree doesn't exist for the given movie, create a new one
        if (movieRatings == null){
            movieRatings = new MyAVLTree<>();
        }
        
        // add the rating to the avl tree, put the avl tree and movie into the hashmap
        movieRatings.insert(newRating, rating);
        movieRatingsMap.put(movieid, movieRatings);


        // update to user hashmap
        
        // get the avl tree associated with the user
        MyAVLTree<Rating> userRatings = userRatingsMap.get(userid);
        // check to see if already in
        if(userRatings != null && userRatings.contains(newRating)){
            // return false on failure
            return false;
        }
        // if avl tree doesn't exist for the given user, create a new one
        if(userRatings == null){
            userRatings = new MyAVLTree<>();
        }
        // add the rating to the avl tree, then put the avl tree and user into the hashmap
        userRatings.insert(newRating, rating);
        userRatingsMap.put(userid, userRatings);

        // return true on completion
        return true;
    }

    /**
     * This method takes a given userid and movieid, and removes the rating with correlated values. This method has a time complexity of O(log n), as accessing the
     * movie from the hashmap takes O(1) time. Searching and removing the movie AVL tree for it takes O(log m) and then updating the hashmap takes O(1) time aswell.
     * This can be simplified to O(log m). As we do this twice (for the movie map and the user map) we can write it as O(log m + log u), which can then be simplified
     * to O(log n). The space complexity is contstant, as we don't need to add any additional data structures.
     * 
     * @param userid the id of the user rating the movie
     * @param movieid the id of the movie being rated
     * 
     * @return whether or not the removal was successful
     */
    @Override
    public boolean remove(int userid, int movieid) {
        // get the AVL tree associated with the movie ID
        MyAVLTree<Rating> movieRatings = movieRatingsMap.get(movieid);

        // if there is a movie associated with it
        if (movieRatings != null){
            // replace it with null
            Rating targetRating = new Rating(userid, movieid, 0, null);

            // if you can remove the target rating
            if(movieRatings.remove(targetRating)){
                // update the hashmap on completion
                movieRatingsMap.put(movieid, movieRatings);

                // remove from userRatingsMap aswell
                MyAVLTree<Rating> userRatings = userRatingsMap.get(userid);
                if (userRatings != null){
                    userRatings.remove(targetRating);
                    userRatingsMap.put(userid, userRatings);
                }
                // return true on completion
                return true;
            }
        }
        // return false on failute
        return false;
    }

    /**
     * The set method is just a combination of the previous two, as we remove the rating if it already exists and then insert it. As a result, this has a time 
     * complexity of O(log n) and a space complexity of O(N)
     * 
     * @param userid the id of the user rating the movie
     * @param movieid the id of the movie being rated
     * @param rating the actual rating of the movie
     * @param timestamp the timestamp of the rating
     * 
     * @return whether the addition was successful or not
     */
    @Override
    public boolean set(int userid, int movieid, float rating, LocalDateTime timestamp) {
        //todo optimise?
        // remove the rating if it already exists
        remove(userid, movieid);

        // add the rating back to the structure
        return add(userid, movieid, rating, timestamp);
    }

    /**
     * Get all the ratings for a given film. This has a time complexity of O(n) and a space complexity of O(n). The time complexity comes as we have to traverse the
     * tree of a given movieid. Checking the hashmap is constant, so this tree traversal will only take O(n). The space is also O(n), as a new array will need to be
     * created to store and therefore return all of the elements in the avl tree.
     * 
     * @param movieID The movie ID
     * @return An array of ratings. If there are no ratings or the film cannot be
     *         found, then return an empty array
     */
    @Override
    public float[] getMovieRatings(int movieid) {
        // get the avl tree for the associated movieid
        MyAVLTree<Rating> movieRatings = movieRatingsMap.get(movieid);
        
        // if we can't find any ratings
        if(movieRatings == null){
            // return an empty list
            return new float [0];
        }

        // search avl tree and return array
        return movieRatings.getRatingsArray(rating -> rating.rating);
    }

    /**
     * Get all the ratings for a given user. This has a time complexity of O(n) and a space complexity of O(n). The time complexity comes as we have to traverse the
     * tree of a given userid. Checking the hashmap is constant, so this tree traversal will only take O(n). The space is also O(n), as a new array will need to be
     * created to store and therefore return all of the elements in the avl tree.
     * 
     * @param userID The user ID
     * @return An array of ratings. If there are no ratings or the user cannot be
     *         found, then return an empty array
     */
    @Override
    public float[] getUserRatings(int userid) {
        // get the avl tree for the assocaited userid
        MyAVLTree<Rating> userRatings = userRatingsMap.get(userid);

        // if we can't find any ratings
        if (userRatings == null){
            // return an empty list
            return new float[0];
        }

        // search the avl tree and return array
        return userRatings.getRatingsArray(rating -> rating.rating);
    }

    /**
     * Get the average rating for a given film. This method is constant time and space, as accessing the avl tree is O(1), checking for null is O(1) and getting the
     * average of the avl tree is constant O(1), as we are storing the size and sum variables whilst adding and just have to complete a simple division, which is
     * constant. As a result, the space complexity is also constant.
     * 
     * @param movieID The movie ID
     * @return Produces the average rating for a given film. 
     *         If the film cannot be found in ratings, but does exist in the movies store, return 0.0f. 
     *         If the film cannot be found in ratings or movies stores, return -1.0f.
     */
    @Override
    public float getMovieAverageRating(int movieid) {
        // get the avl tree associated with a movie id
        MyAVLTree<Rating> movieRatings = movieRatingsMap.get(movieid);

        // if we can't find any ratings for this movie
        if(movieRatings == null){
            // return -1
            return -1;
        }

        // return the average of the avl tree
        return movieRatings.getAverage();
        
    }

    /**
     * Get the average rating for a given user. As explained in the getMovieAverageRating method, this has O(1) time and O(1) space
     * 
     * @param userID The user ID
     * @return Produces the average rating for a given user. If the user cannot be
     *         found, or there are no rating, return -1
     */
    @Override
    public float getUserAverageRating(int userid) {
        // get the avl tree associated with a user id
        MyAVLTree<Rating> userRatings = userRatingsMap.get(userid);

        // if we can't find any ratings for that user
        if(userRatings == null){
            // return -1
            return -1;
        }

        // return the average of the avl tree
        return userRatings.getAverage();
    }

    /**
     * Gets the top N movies with the most ratings, in order from most to least. The time complexity of accessing and mapping the keys is O(n). To sort them using a
     * quick sort, however, is O(n log n) due to the fact that we calculate the pivot by getting the median of 3 values, meaning even though the actual worst case is 
     * O(n^2), it is very unlikely we will ever encounter this, so we can assume it is O(n log n). Creating the array is O(num) which can be written as O(1), as its
     * constant. Therefore, we can write the worst case time complexity for this algorithm being O(n log n). The space complexity is O(n), as we have to create new
     * arrays for the pairs of ids and sizes. As this correlates to each element in keys, we can write this as O(n).
     * 
     * @param num The number of movies that should be returned
     * @return A sorted array of movie IDs with the most ratings. The array should be
     *         no larger than num. If there are less than num movies in the store,
     *         then the array should be the same length as the number of movies
     */
    @Override
    public int[] getMostRatedMovies(int num) {
        // get a list of all the movie ids in the hashmap
        Integer[] keys = movieRatingsMap.keys();
        MyPair<Integer, Integer>[] movieSizesPairs = new MyPair[keys.length];

        // pair each movie id with its size
        for (int i = 0; i < keys.length; i++){
            if(movieRatingsMap.get(keys[i]) != null){
                int size = movieRatingsMap.get(keys[i]).size();
                movieSizesPairs[i] = new MyPair<>(keys[i], size);
            }
        }
        
        // sort sizes using quicksort
        quickSort(movieSizesPairs, 0, movieSizesPairs.length - 1);
        
        // create an array of num elements
        int[] result = new int[num];

        // map the IDs of the first num elements to our new array
        for (int i = 0; i < num; i++){
            result[i] = movieSizesPairs[i].identifier;
        }
        
        // return the first num elements of keys
        return result;

    }

    /**
     * Gets the top N users with the most ratings, in order from most to least. The time complexity of accessing and mapping the keys is O(n). To sort them using a
     * quick sort, however, is O(n log n) due to the fact that we calculate the pivot by getting the median of 3 values, meaning even though the actual worst case is 
     * O(n^2), it is very unlikely we will ever encounter this, so we can assume it is O(n log n). Creating the array is O(num) which can be written as O(1), as its
     * constant. Therefore, we can write the worst case time complexity for this algorithm being O(n log n). The space complexity is O(n), as we have to create new
     * arrays for the pairs of ids and sizes. As this correlates to each element in keys, we can write this as O(n).
     * 
     * @param num The number of users that should be returned
     * @return A sorted array of user IDs with the most ratings. The array should be
     *         no larger than num. If there are less than num users in the store,
     *         then the array should be the same length as the number of users
     */
    @Override
    public int[] getMostRatedUsers(int num) {
        // get a list of all the user ids in the hashmap
        Integer[] keys = userRatingsMap.keys();
        MyPair<Integer, Integer>[] userSizesPairs = new MyPair[keys.length];

        // pair each user id with its size
        for (int i = 0; i < keys.length; i++){
            if(userRatingsMap.get(keys[i]) != null){
                int size = userRatingsMap.get(keys[i]).size();
                userSizesPairs[i] = new MyPair<>(keys[i], size);
            }
        }
        

        // sort sizes using quicksort
        quickSort(userSizesPairs, 0, userSizesPairs.length - 1);
        
        // create an array of num elements
        int[] result = new int[num];

        // map the IDs of the first num elements to our new array
        for (int i = 0; i < num; i++){
            result[i] = userSizesPairs[i].identifier;
        }
        
        // return the first num elements of keys
        return result;
    }


    /**
     * Performs a recursive quicksort on an array. This has a time complexity of O(n log n) instead of O(n^2) as due to the fact we are calculating the pivot by getting
     * the median of 3 values, it means we are very very unlikely to encounter the worst case of O(n^2), meaning we can write it as O(n log n). The space complexity is
     * 
     * 
     * @param arr the array we wish to sort
     * @param low the lowerbound of the subarray
     * @param high the upperbound of the array
     */
    private void quickSort(MyPair[] arr, int low, int high){
        if (low < high){
            // partition the array and get the pivot index
            int pivot = partition(arr, low, high);

            // recursively sort elemetns before and after the pivot
            quickSort(arr, low, pivot - 1);
            quickSort(arr, pivot + 1, high);
        }
    }

    /**
     * Calculates the pivot for a subarray to be quicksorted. This has a time complexity of O(n) due to the fact that, we need to iterate through the entire array.
     * The space complexity for this method is constant O(1) as we are doing an in-place swap to calcaulate the pivot.
     * 
     * @param arr the array we wish to sort
     * @param low the lowerbound of the subarray
     * @param high the upperbound of the array
     */
    private int partition(MyPair[] arr, int low, int high){
        // choose the last element as the pivot
        double pivot = arr[high].value.doubleValue();
        int i = low - 1;

        // traverse the array
        for (int j = low; j < high; j++){
            // if the current eleemnt is greater than or equal to the pivot
            if(arr[j] != null && arr[j].value.doubleValue() >= pivot){
                // increment smaller element
                i++;

                // swap elements
                MyPair temp = arr[i];
                arr[i] = arr[j];
                arr[j] = temp;
            }
        }

        // swap the pivot element with the element at index (i + 1)
        MyPair temp = arr[i + 1];
        arr[i + 1] = arr[high];
        arr[high] = temp;

        // return the index of the pivot element
        return i + 1;
    }

    /**
     * Gets the number of ratings in the data structure. This has a time complexity of O(n), as getting the keys takes O(n) and iterating through the keys also takes
     * O(n), where n is the number of elements in the hashmap. From here, getting the size of each avl tree at each index in the hashmap is constant O(1), and the
     * space complexity of this method is also O(1).
     * 
     * @return The number of ratings in the data structure
     */
    @Override
    public int size() {
        int total = 0;
        
        // get a list of all the keys in the hashmap
        Integer[] keys = movieRatingsMap.keys();

        // iterate through these keys
        for (Integer key : keys){
            // add the size of each avl tree associated with a key to the total
            total += movieRatingsMap.get(key).size();
            
        }
        
        // return the total
        return total;

    }

    /**
     * Get the number of ratings that a movie has. This method has constant time and space, as getting from the hashmap is constant and getting the size of the
     * avl tree is constant due to an additional variable stored.
     * 
     * @param movieid The movie id to be found
     * @return The number of ratings the specified movie has. 
     *         If the movie exists in the movies store, but there
     *         are no ratings for it, then return 0. If the movie
     *         does not exist in the ratings or movies store, then
     *         return -1
     */
    @Override
    public int getNumRatings(int movieid) {
        // get the avl tree for the movie in question
        MyAVLTree<Rating> movieRatings = movieRatingsMap.get(movieid);

        // return -1 if there aren't any reviews associated with that movie
        if(movieRatings == null){
            return -1;
        }
        
        // return the size of the avl tree (correlating to no of reviews)
        return movieRatings.size();
    }

    /**
     * Get the highest average rated film IDs, in order of there average rating
     * (hightst first). Similar to getMostRatedUsers, this has time complexity
     * of O(n log n) and space complexity of O(n)
     * 
     * @param numResults The maximum number of results to be returned
     * @return An array of the film IDs with the highest average ratings, highest
     *         first. If there are less than num movies in the store,
     *         then the array should be the same length as the number of movies
     */
    @Override
    public int[] getTopAverageRatedMovies(int numResults) {
        // get a list of all the movie ids in the hashmap
        Integer[] keys = movieRatingsMap.keys();
        MyPair<Integer, Double>[] movieAveragePairs = new MyPair[keys.length];
        
        // pair each movie id with its average
        for (int i = 0; i < keys.length; i++){
            double average = movieRatingsMap.get(keys[i]).getAverage();
            movieAveragePairs[i] = new MyPair<>(keys[i], average);
            
        }
        
        // sort averages using quicksort
        quickSort(movieAveragePairs, 0, movieAveragePairs.length - 1);
        
        // create an array of num elements
        int[] result = new int[numResults];

        // map the IDs of the first num elements to our new array
        for (int i = 0; i < numResults; i++){
            result[i] = movieAveragePairs[i].identifier;
        }
        
        // return the first num elements of keys
        return result;
    }
}

// a class used to store each individual rating
class Rating implements Comparable<Rating> {
    int userId;
    int movieId;
    float rating;
    LocalDateTime timestamp;

    // create a new instance of the rating
    Rating(int userId, int movieId, float rating, LocalDateTime timestamp) {
        this.userId = userId;
        this.movieId = movieId;
        this.rating = rating;
        this.timestamp = timestamp;
    }

    // method used for rating comparison based on the user and movie
    @Override
    public int compareTo(Rating other){
        if (this.userId != other.userId) {
            return Integer.compare(this.userId, other.userId);
        } else {
            return Integer.compare(this.movieId, other.movieId);
        }
    }
}