package stores;

import structures.*;

import interfaces.ICredits;

public class Credits implements ICredits{
    Stores stores;

    private MyHashMap<Integer, Credit> creditsMap;
    
    private MyHashMap<Integer, CastMember> castMap;
    private MyHashMap<Integer, Member> crewMap;

    // todo create hashmaps to store each cast and crew member

    /**
     * The constructor for the Credits data store. This is where you should
     * initialise your data structures.
     * 
     * @param stores An object storing all the different key stores, 
     *               including itself
     */
    public Credits (Stores stores) {
        this.stores = stores;
        this.creditsMap = new MyHashMap<>();
        this.castMap = new MyHashMap<>();
        this.crewMap = new MyHashMap<>();
        // TODO Add initialisation of data structure here
    }

    /**
     * Adds data about the people who worked on a given film. The movie ID should be
     * unique
     * 
     * @param cast An array of all cast members that starred in the given film
     * @param crew An array of all crew members that worked on a given film
     * @param id   The (unique) movie ID
     * @return TRUE if the data able to be added, FALSE otherwise
     */
    @Override
    public boolean add(CastCredit[] cast, CrewCredit[] crew, int id) {
        // create a new credits object
        Credit credit = new Credit(cast, crew, id);

        // add each cast member to the hashmap
        for (CastCredit castMember : cast){
            // create a new person and a new cast member
            Person newPerson = new Person(castMember.getID(), castMember.getName(), castMember.getProfilePath());
            CastMember newMember = new CastMember(newPerson);
            
            // if a cast member exists
            if(castMap.get(castMember.getID()) != null){
                // update the member value
                newMember = castMap.get(castMember.getID());
            }
            
            
            // increment number of appearances + add to appearances map
            newMember.appearances++;
            newMember.films.put(id, id);

            // if the cast member stars in the film
            if(castMember.getOrder() <= 2){
                // add the starred film to the hashmap
                newMember.starredFilms.put(id, id);
            }

            //todo add the film to the hashmap

            // put the member in the hashmap
            castMap.put(castMember.getID(), newMember);
        }

        // add each crew member to the crewmap
        for (CrewCredit crewMember : crew){
            // todo modify here
            // create a new person and a new cast member
            Person newPerson = new Person(crewMember.getID(), crewMember.getName(), crewMember.getProfilePath());
            Member newMember = new Member(newPerson);

            if(crewMap.get(crewMember.getID()) != null){
                newMember = crewMap.get(crewMember.getID());
            }

            // incremement number of appearances + add to appearances map
            newMember.films.put(id, id);
            
            // add the member to the hashmap
            crewMap.put(crewMember.getID(), newMember);
        }
        
        // insert the credit into the hashmap
        return creditsMap.put(id, credit);
    }

    /**
     * Remove a given films data from the data structure
     * 
     * @param id The movie ID
     * @return TRUE if the data was removed, FALSE otherwise
     */
    @Override
    public boolean remove(int id) {
        // remove the id from the hashmap
        return creditsMap.remove(id);

        // todo remove also from the cast map
    }

    /**
     * Gets all the cast members for a given film
     * 
     * @param filmID The movie ID
     * @return An array of CastCredit objects, one for each member of cast that is 
     *         in the given film. The cast members should be in "order" order. If
     *         there is no cast members attached to a film, or the film canot be 
     *         found, then return an empty array
     */
    @Override
    public CastCredit[] getFilmCast(int filmID) {
        // store the credit associated with the film
        Credit filmCredit = creditsMap.get(filmID);
        
        // return the cast if the film exists
        if(filmCredit != null){
            return filmCredit.cast;
        }

        // return an empty array if can't find film
        return new CastCredit[0];
    }

    /**
     * Gets all the crew members for a given film
     * 
     * @param filmID The movie ID
     * @return An array of CrewCredit objects, one for each member of crew that is
     *         in the given film. The crew members should be in ID order. If there 
     *         is no crew members attached to a film, or the film canot be found, 
     *         then return an empty array
     */
    @Override
    public CrewCredit[] getFilmCrew(int filmID) {
        // store the credit associated with the film
        Credit filmCredit = creditsMap.get(filmID);
        
        // return the crew if the film exists
        if(filmCredit != null){
            return filmCredit.crew;
        }

        // return an empty array if can't find film
        return new CrewCredit[0];
    }

    /**
     * Gets the number of cast that worked on a given film
     * 
     * @param filmID The movie ID
     * @return The number of cast member that worked on a given film. If the film
     *         cannot be found, then return -1
     */
    @Override
    public int sizeOfCast(int filmID) {
        // store the credit associated with the film
        Credit filmCredit = creditsMap.get(filmID);
        
        // return the size of the cast if the film exists
        if(filmCredit != null){
            return filmCredit.castSize;
        }

        // return -1 if can't find film
        return -1;
    }

    /**
     * Gets the number of crew that worked on a given film
     * 
     * @param filmID The movie ID
     * @return The number of crew member that worked on a given film. If the film
     *         cannot be found, then return -1
     */
    @Override
    public int sizeofCrew(int filmID) {
        // store the credit associated with the film
        Credit filmCredit = creditsMap.get(filmID);
        
        // return the size of the crew if the film exists
        if(filmCredit != null){
            return filmCredit.crewSize;
        }

        // return -1 if can't find film
        return -1;
    }

    /**
     * Gets the number of films stored in this data structure
     * 
     * @return The number of films in the data structure
     */
    @Override
    public int size() {
        // return the size of the hashmap
        return creditsMap.size();
    }

    /**
     * Gets a list of all unique cast members present in the data structure
     * 
     * @return An array of all unique cast members as Person objects. If there are 
     *         no cast members, then return an empty array
     */
    @Override
    public Person[] getUniqueCast() {
        // get a list of all keys entered into the hashmap
        Integer[] keys = castMap.keys();
        Person[] uniqueCast = new Person[castMap.size()];
        
        // get the person associated with the hashmap
        for (int i = 0; i < castMap.size(); i++){
            // add the person to the unique cast list
            uniqueCast[i] = castMap.get(keys[i]).person;
        }

        // return the list of unique cast members
        return uniqueCast;
    }

    /**
     * Gets a list of all unique crew members present in the data structure
     * 
     * @return An array of all unique crew members as Person objects. If there are
     *         no crew members, then return an empty array
     */
    @Override
    public Person[] getUniqueCrew() {
        // get a list of all crew members added
        Integer[] keys = crewMap.keys();
        Person[] uniqueCrew = new Person[crewMap.size()];

        // check each crew member
        for (int i = 0; i < crewMap.size(); i++){
            // get the value associated with the given key
            uniqueCrew[i] = crewMap.get(keys[i]).person;
        }
        
        // return the list of unique crew members
        return uniqueCrew;
    }

    /**
     * Get all the cast members that have the given string within their name
     * 
     * @param cast The string that needs to be found
     * @return An array of unique Person objects of all cast members that have the 
     *         requested string in their name
     */
    @Override
    public Person[] findCast(String cast) {
        // get all keys associated
        Integer[] keys = castMap.keys();
        Person[] matchingCast = new Person[castMap.size()];
        int index = 0;

        // check each cast member
        for (int i = 0; i < castMap.size(); i++){
            // get the cast member
            Person castMember = castMap.get(keys[i]).person;

            // if the search term can be found in the name
            if (castMember.getName().contains(cast)){
                // add the cast member to the matching cast array
                matchingCast[index++] = castMember;
            }

        }

        // remove null keys by creating a subarray
        Person[] finalArrayOfCast = new Person[index];
        System.arraycopy(matchingCast, 0, finalArrayOfCast, 0, index);

        return finalArrayOfCast;

        // todo iterate through each cast member in the hashmap O(n)

        // todo check to see if the string contains the given values

        // todo add to an array O(n)


        
        // TODO Implement this function
    }

    /**
     * Get all the crew members that have the given string within their name
     * 
     * @param crew The string that needs to be found
     * @return An array of unique Person objects of all crew members that have the 
     *         requested string in their name
     */
    @Override
    public Person[] findCrew(String crew) {
        // get all keys associated
        Integer[] keys = crewMap.keys();
        Person[] matchingCrew = new Person[crewMap.size()];
        int index = 0;

        // check each cast member
        for (int i = 0; i < crewMap.size(); i++){
            // get the cast member
            Person crewMember = crewMap.get(keys[i]).person;

            // if the search term can be found in the name
            if (crewMember.getName().contains(crew)){
                // add the cast member to the matching cast array
                matchingCrew[index++] = crewMember;
            }

        }

        // remove null keys by creating a subarray
        Person[] finalArrayOfCrew = new Person[index];
        System.arraycopy(matchingCrew, 0, finalArrayOfCrew, 0, index);

        // TODO Implement this function
        return finalArrayOfCrew;
    }

    /**
     * Gets the Person object corresponding to the cast ID
     * 
     * @param castID The cast ID of the person to be found
     * @return The Person object corresponding to the cast ID provided. 
     *         If a person cannot be found, then return null
     */
    @Override
    public Person getCast(int castID) {
        // get the member with the associated id
        CastMember member = castMap.get(castID);

        // check if they exist -> return null if not
        if(member == null){
            return null;
        }
        
        // get the person object associated with the crewID
        return member.person;
    }

    /**
     * Gets the Person object corresponding to the crew ID
     * 
     * @param crewID The crew ID of the person to be found
     * @return The Person object corresponding to the crew ID provided. 
     *         If a person cannot be found, then return null
     */
    @Override
    public Person getCrew(int crewID){
        // get the member with the associated id
        Member member = crewMap.get(crewID);

        // check if they exist -> return null if not
        if(member == null){
            return null;
        }
        
        // get the person object associated with the crewID
        return member.person;
    }

    
    /**
     * Get an array of film IDs where the cast member has starred in
     * 
     * @param castID The cast ID of the person
     * @return An array of all the films the member of cast has starred
     *         in. If there are no films attached to the cast member, 
     *         then return an empty array
     */
    @Override
    public int[] getCastFilms(int castID){
        // get the member associated with the cast id
        CastMember member = castMap.get(castID);

        // return an empty array if there are no filsm for the cast member
        if (member == null){
            return new int[0];
        }
        
        // get the keys for the films hashmap
        Integer[] keys = member.films.keys();
        int[] films = new int[member.films.size()];

        // map each key to the film array
        for (int i = 0; i < member.films.size(); i++){
            films[i] = keys[i];
        }
        
        // return the list of films
        return films;
    }

    /**
     * Get an array of film IDs where the crew member has starred in
     * 
     * @param crewID The crew ID of the person
     * @return An array of all the films the member of crew has starred
     *         in. If there are no films attached to the crew member, 
     *         then return an empty array
     */
    @Override
    public int[] getCrewFilms(int crewID) {
        // get the member associated with the crew id
        Member member = crewMap.get(crewID);

        // return an empty array if there are no filsm for the cast member
        if (member == null){
            return new int[0];
        }
        
        // get the keys for the films hashmap
        Integer[] keys = member.films.keys();
        int[] films = new int[member.films.size()];

        // map each key to the film array
        for (int i = 0; i < member.films.size(); i++){
            films[i] = keys[i];
        }
        
        // return the list of films
        return films;
    }

    /**
     * Get the films that this cast member stars in (in the top 3 cast
     * members/top 3 billing). This is determined by the order field in
     * the CastCredit class
     * 
     * @param castID The cast ID of the cast member to be searched for
     * @return An array of film IDs where the the cast member stars in.
     *         If there are no films where the cast member has starred in,
     *         or the cast member does not exist, return an empty array
     */
    @Override
    public int[] getCastStarsInFilms(int castID){
        // get the member associated with the cast id
        CastMember member = castMap.get(castID);

        // return an empty array if there are no filsm for the cast member
        if (member == null){
            return new int[0];
        }
        
        // get the keys for the films hashmap
        Integer[] keys = member.starredFilms.keys();
        int[] films = new int[member.starredFilms.size()];

        // map each key to the film array
        for (int i = 0; i < member.starredFilms.size(); i++){
            films[i] = keys[i];
        }
        
        // return the list of films
        return films;
    }
    
    /**
     * Get Person objects for cast members who have appeared in the most
     * films. If the cast member has multiple roles within the film, then
     * they would get a credit per role played. For example, if a cast
     * member performed as 2 roles in the same film, then this would count
     * as 2 credits. The list should be ordered by the highest number of credits.
     * 
     * @param numResults The maximum number of elements that should be returned
     * @return An array of Person objects corresponding to the cast members
     *         with the most credits, ordered by the highest number of credits.
     *         If there are less cast members that the number required, then the
     *         list should be the same number of cast members found.
     */
    @Override
    public Person[] getMostCastCredits(int numResults) {
        // if there is no cast, return an empty array
        if(castMap.size() == 0){
            return new Person[0];
        }
        
        // get list of all keys
        Integer[] keys = castMap.keys();
        MyPair<Person, Integer>[] castMemberPair = new MyPair[keys.length];

        // pair each person object with the number of appearances they have
        for (int i = 0; i < keys.length; i++){
            if(castMap.get(keys[i]) != null){
                // create a new pair to represent
                int numCredits = getNumCastCredits(keys[i]);
                castMemberPair[i] = new MyPair<>(castMap.get(keys[i]).person, numCredits);
            }
        }

        // quicksort it
        quickSort(castMemberPair, 0, castMemberPair.length - 1);
        
        // create an array of num elements
        Person[] result = new Person[numResults];

        // map the person object of the first n elements to our new arary
        for (int i = 0; i < numResults; i++){
            result[i] = castMemberPair[i].identifier;
        }

        // return
        return result;
    }

    /**
     * Get the number of credits for a given cast member. If the cast member has
     * multiple roles within the film, then they would get a credit per role
     * played. For example, if a cast member performed as 2 roles in the same film,
     * then this would count as 2 credits.
     * 
     * @param castID A cast ID representing the cast member to be found
     * @return The number of credits the given cast member has. If the cast member
     *         cannot be found, return -1
     */
    @Override
    public int getNumCastCredits(int castID) {
        // get the cast member
        CastMember member = castMap.get(castID);

        // return -1 if the cast member cannot be found
        if(member == null){
            return -1;
        }
        
        // return the number of appearances the individual has
        return member.appearances;
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




}



// object to store information about each credit
class Credit {
    CastCredit[] cast;
    CrewCredit[] crew;
    int id;
    int castSize;
    int crewSize;

    // create a new instance of the rating
    Credit(CastCredit[] cast, CrewCredit[] crew, int id){
        this.cast = cast;
        this.crew = crew;
        this.id = id;
        this.castSize = cast.length;
        this.crewSize = crew.length;
    }
}


class Member {
    Person person;
    MyHashMap<Integer, Integer> films = new MyHashMap<>();

    public Member(Person person){
        this.person = person;
    }
}


class CastMember extends Member{
    MyHashMap<Integer, Integer> starredFilms = new MyHashMap<>();
    int appearances = 0;

    public CastMember(Person person){
        // call constructor class from Member
        super(person);
    }
}