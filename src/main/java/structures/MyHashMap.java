package structures;

import java.lang.reflect.Array;

public class MyHashMap <K extends Integer, V>{
    // constant variables regarding hashmap capacity and resizing
    private static final int INITIAL_CAPACITY = 257; //todo fuck with capacity
    private static final double LOAD_FACTOR = 0.75;
    private static final int[] PRIMES = { 31, 61, 127, 257, 509, 1021, 2053, 4093, 8191, 16381, 32749, 65521, 131059};

    /**
     * Class to store each entry in a hashmap.
     * 
     * @param <K> the type of keys in this this hashmap
     * @param <V> the type of mapped values
     */
    private class Entry<K, V> {
        // store the key and value
        K key;
        V value;
        
        // store a reference to the next entry in a hashmap. creates a linked list at each bucket
        Entry<K, V> next;

        Entry(K key, V value){
            // initialise values
            this.key = key;
            this.value = value;
        }
    }

    // store an array of entries (buckets)
    private Entry<K, V>[] buckets;

    // store the capacity and size of the hashmap
    private int capacity; //todo change size
    private int size = 0;
    
    // contain an array of all keys in the hashmap
    Integer[] entrySet;


    public MyHashMap() {
        // initialise the buckets, entry set and the capacity
        this.capacity = INITIAL_CAPACITY;
        this.buckets = new Entry[capacity];
        this.entrySet = new Integer[capacity];

        // todo modify hashmap so it takes in an initial capacity variable
    }

    private int getBucketIndex(K key) {
        // calculate the bucket index for the key given and return it
        return key.hashCode() % capacity;
    }

    /**
     * Returns the value for a given key in a hashmap
     * 
     * @param key the key we want the value for
     * @return the value associated with that key. returns null if unable to find.
     */
    public V get(K key) {
        // get the bucket index for the given key
        int bucketIndex = getBucketIndex(key);

        // get the first entry in the bucket
        Entry<K, V> current = buckets[bucketIndex];
        
        // traverse the linked list
        while (current != null) {
            // return the value if the keys match
            if (current.key.equals(key)) {
                return current.value;
            }
            
            // move onto the next value in the linked list
            current = current.next;
        }

        // returns null if unable to find the key
        return null;
    }

    /**
     * Inserts a new key/pair value into the hashmap
     * 
     * @param key the key we want to insert to the hashmap
     * @param value the value we want to insert to the hashmap
     * @return whether or not this insertion was successful
     */
    public boolean put(K key, V value) {
        // resize if the current capacity exceeds the loadfactor
        if( ((double) size / capacity) > LOAD_FACTOR){
            System.out.println("Resizing!!!!!!!!");
            resize();
        }
        
        // add the key to the entry set if it doesn't already exist
        if (!containsKey(key)){
            entrySet[size++] = key;
        }
        
        // calculate the bucket index based on the key
        int bucketIndex = getBucketIndex(key);

        // create a new entry
        Entry<K, V> newEntry = new Entry<>(key, value);

        // get the entry stored at the calculated bucket
        Entry<K, V> current = buckets[bucketIndex];
        
        // if the bucket is empty, add the new entry
        if (current == null) {
            buckets[bucketIndex] = newEntry;
        }
        // if the bucket isn't empty, add to linked list
        else {
            // store the previous node in the linked list
            Entry<K, V> prev = null;
            
            // traverse linked list
            while (current != null) {
                if (current.key.equals(key)) {
                    // if the key already exists, update value and return false
                    current.value = value;
                    return false;
                }
                
                // move onto the next node
                prev = current;
                current = current.next;
            }
            
            // add the new entry to the end of the linked list in the bucket
            prev.next = newEntry;
        }

        // return true if successful insertion
        return true;
    }

    /**
     * Resize the hashmap once the load factor has been reached
     */
    private void resize() {
        // double the hash map capacity and find nearest prime
        int newCapacity = findNextPrime(capacity * 2);
        capacity = newCapacity;
        // todo reset the .next values to null ?

        // resize the hashmap
        Entry<K, V>[] newBuckets = new Entry[newCapacity];

        // add the elements back into the hashmap
        for (Entry<K, V> bucket : buckets){
            // traverse linked list at the current bucket
            Entry<K, V> current = bucket;

            while (current != null){
                // reference next entry
                Entry<K, V> next = current.next;

                // calcualte bucket index for current key
                int index = getBucketIndex(current.key);

                // insert the current entry into the new bucket array
                current.next = newBuckets[index];
                newBuckets[index] = current;

                // move onto the next entry
                current = next;
            }
        }

        
        // store buckets
        buckets = newBuckets;

        // resize the entry set
        Integer[] newEntrySet = new Integer[newCapacity];
        
        // add contents of entry set to the new entry set
        for (int i = 0; i < size; i++){
            newEntrySet[i] = entrySet[i];
        }
        
        // store the updated entry set
        entrySet = newEntrySet;
    }

    /**
     * A method to remove a given key from the hashmap
     * 
     * @param key the key of the object we want to remove
     * @return whether the removal was successful
     */
    public boolean remove(K key) {
        // get the bucket index for the key given
        int bucketIndex = getBucketIndex(key);

        // store the current and previous
        Entry<K, V> current = buckets[bucketIndex];
        Entry<K, V> prev = null;
        
        // iterate through each element in the linked list at the bucket index
        while (current != null) {
            // if the keys match
            if (current.key.equals(key)) {
                // if at the beginning of the linked list
                if (prev == null) {
                    // set the second element to be the first
                    buckets[bucketIndex] = current.next;
                } else {
                    // set the previous element to point to the next
                    prev.next = current.next;
                }
                
                // decrease size
                size--;
                // update the entry set to remove the key
                updateEntrySetAfterRemoval(key);
                
                
                //  return true on completion
                return true;
            }
            
            // move onto the next element in the linked list
            prev = current;
            current = current.next;
        }

        
        // return false if the key isn't found
        return false;
    }

    /**
     * method to remove the key from the entry set
     * 
     * @param key the key we want to remove from the entry set
     */
    private void updateEntrySetAfterRemoval(K key) {
        // iterate through entry set
        for (int i = 0; i < size; i++) {
            // if the key is found
            if (entrySet[i].equals(key)) {
                // shift the following elements left by 1 position
                System.arraycopy(entrySet, i + 1, entrySet, i, size - i - 1);
                // remove the last element to avoid duplicates
                entrySet[size] = null;
                break;
            }
        }
    }

    /**
     * A check to see if a given key is in the hashmap
     * 
     * @param key the key we want to find in the hashmap
     * @return whether or not the key is in the hashmap
     */
    public boolean containsKey(K key){
        //todo: optimise? use a different search algorithm
        // linear search used to check each key
        for (int i = 0; i < size; i++){
            if (entrySet[i].equals(key)){
                // return true upon finding
                return true;
            }
        }

        // return false otherwise
        return false;
    }

    /**
     * Calculates the next capacity by finding the next prime number in the primes
     * array
     * 
     * @param n the minimum number capacity we are searching for
     * @return the new prime number we are using for out capacity
     */
    private int findNextPrime(int n){
        // iterate through each prime number stored
        for(int prime : PRIMES){
            //  return the first prime number greater than n
            if (prime >= n){
                return prime;
            }
        }

        // return largest prime number if exceeding capacity
        return PRIMES[PRIMES.length - 1];
    }

    /**
     * Return the list of keys in the hashmap
     * 
     * @return array of keys
     */
    public Integer[] keys(){
        // create a new array based on the size of the integer
        Integer[] keys = new Integer[size];
        int keyIndex = 0;

        System.out.println("Initial Size: " + size());

        // map each non-null key in the entry set to the keys array
        for (int i = 0; i < size; i++){
            K entry = (K) entrySet[i];
            if(get((K) entrySet[i]) !=  null){
                keys[keyIndex] = entrySet[i];
                keyIndex++;
            }
        }

        // create a new array for new keys
        Integer[] newKeys = new Integer[keyIndex];

        // remove null keys by creating a subarray
        System.arraycopy(keys, 0, newKeys, 0, keyIndex);
        
        System.out.println("Final Size: " + newKeys.length);

        // return  list of keys
        return newKeys;
    }

    /**
     * Returns the number of keys in the hashmap
     * 
     * @return the size of the hashmap
     */
    public int size(){
        // returns the size of the hashmap
        return size;
    }
}



//!! error
// todo hashmap is cooked -> rework?
// todo error when handling collisions?
// todo possibly update .next value for each node at hashmap? -> reset?