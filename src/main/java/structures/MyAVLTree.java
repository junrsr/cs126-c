package structures;

public class MyAVLTree<T extends Comparable<T>> {
    
    /**
     * an interface defining a method for extracting the rating
     * 
     * @param <T> the type of item from which to extract the rating
     */
    public interface RatingExtractor<T> {
        // extracts a float rating from the given item
        float extract(T item);
    }

    /**
     * Class to store each node in the AVL tree
     * 
     * @param <T> the type of data stored in the node
     */
    private class Node {
        T data; // the data stored in the node
        Node left, right; // references to the left and right children
        int height; // the height of the tree
        float rating; // the rating associated with the data

        Node(T data, float rating) {
            // initialise variables
            this.data = data;
            this.rating = rating;
            this.height = 1; // initialise the height of the node to 1
        }
    }

    
    private Node root; // the root of the AVL tree
    private int size = 0; // the size of the AVL tree
    private float sum = 0; // the sum of each element in the AVL tree

    /**
     * Calculates the height of a given node in the AVL tree
     * 
     * @param node the node which we are calculating the height for
     * @return the height of the current node
     */
    private int height(Node node) {
        // return 0 if the node doesn't exist
        if (node == null){
            return 0;
        }
        
        // return the height of the current node
        return node.height;
    }

    
    /**
     * Calculates the balance factor for the given node in teh AVL tree
     * 
     * @param node the node which we are calculating the balance factor for
     * @return the difference between the height of each subtree
     */
    private int balanceFactor(Node node) {
        // return 0 if the node doesn't exist
        if (node == null){
            return 0;
        }

        // store the difference in height between child nodes
        return height(node.left) - height(node.right);
    }

    /**
     * Performs a left rotation on the given node in the AVL tree
     * 
     * @param y the node to perform the right rotation on
     * @return the new root node after rotation
     */
    private Node rotateRight(Node y) {
        // store the left child and the right child of the left child
        Node x = y.left;
        Node T = x.right;

        // rotate
        x.right = y; // update right child of left child to current ndoe
        y.left = T; // update left child to be its right child

        // update the height of rotated nodes
        y.height = Math.max(height(y.left), height(y.right)) + 1;
        x.height = Math.max(height(x.left), height(x.right)) + 1;

        // return new root node
        return x;
    }

    /**
     * Performs a left rotation on the given node in the AVL tree
     * 
     * @param x the node to perform the left rotation on
     * @return the new root node after rotation
     */
    private Node rotateLeft(Node x) {
        // store the right child and left child of the right child
        Node y = x.right;
        Node T = y.left;

        // rotate
        y.left = x; // update left child of right child to current node
        x.right = T; // update the right child to be be its left child

        // update the height of rotated nodes
        x.height = Math.max(height(x.left), height(x.right)) + 1;
        y.height = Math.max(height(y.left), height(y.right)) + 1;

        // return the new root node after rotation
        return y;
    }

    /**
     * Inserts a new node into the AVL tree with the specified data
     * 
     * @param data the data to insert into the tree
     * @param rating the rating associated with the data
     */
    public void insert(T data, float rating) {
        // recursively insert the data into the tree
        root = insert(root, data, rating);
        
        // update size and rating
        size++;
        sum += rating;
    }

    /**
     * Helper method to recursively insert a new node into the AVL tree
     */
    private Node insert(Node node, T data, float rating) {
        // if the node doesn't exist, create a new node
        if (node == null){
            return new Node(data, rating);
        }
        
        // if the data is less than the data for the node
        if (data.compareTo(node.data) < 0){
            // insert into the left subtree
            node.left = insert(node.left, data, rating);
        }
        // if the data is greater than the data for the current node
        else if (data.compareTo(node.data) > 0){
            // insert into the right subtree
            node.right = insert(node.right, data, rating);
        }
        // if there are duplicate values
        else{
            // decrement the size and sum
            size--;
            sum -= rating;

            // return the current node
            return node;
        }

        // balance the tree
        return balanceNode(node);
    }

    /**
     * Removes the specified data from the AVL tree
     * 
     * @param data the data we want to remove from the tree
     * @return whether or not the removal was successful
     */
    public boolean remove(T data) {
        // return false if the AVL tree is empty
        if (root == null){
            return false;
        }

        // recursively remove starting from the root
        root = remove(root, data);

        // return true if the removal is successful
        return true;
    }

    /**
     * Helper method to recursively remove from the AVL tree
     * 
     * @param node the current node we are checking
     * @param data the data we want to remove from the AVL tree
     * @return the root of the subtree after removal and balancing
     */
    private Node remove(Node node, T data) {
        // return null if not found in tree
        if (node == null){
            return null;
        }

        // if the data is less than the data for the current node
        if (data.compareTo(node.data) < 0){
            // remove from the left subtree
            node.left = remove(node.left, data);
        }
        // if the data is greater than the data for the current node
        else if (data.compareTo(node.data) > 0){
            // remove from the right subtree
            node.right = remove(node.right, data);
        }
        // if the data is found
        else {
            // if there is at most one child node
            if (node.left == null || node.right == null) {
                // get the other subtree value
                if(node.left != null){
                    node = node.left;
                }
                else{
                    node = node.right;
                }
            }
            // if there are two child nodes
            else {
                // get the minimum value in the right subtree
                Node temp = minValueNode(node.right);
                // update the node so that it stores the current data
                node.data = temp.data;
                // remove the minimum value from the right subtree
                node.right = remove(node.right, temp.data);
            }
        }

        // return null if the node doesn't exist
        if (node == null){
            return null;
        }

        // update the size and rating
        size--;
        sum -= node.rating;

        // balance the tree
        return balanceNode(node);
    }

    /**
     * Balances a given node in the AVL tree
     * 
     * @param node the node to be balanced
     * @return the balanced node after rotations
     */
    private Node balanceNode(Node node) {
        // update the height of the node based on the height of its children
        node.height = 1 + Math.max(height(node.left), height(node.right));

        // calculate the balance factor for the node
        int balance = balanceFactor(node);

        // if unbalanced via the left node
        if (balance > 1) {
            if (balanceFactor(node.left) < 0) {
                // perform a left rotation on the left child if necessary
                node.left = rotateLeft(node.left);
            }
            // rotate the tree right
            return rotateRight(node);
        }
        // if unbalanced via the right node
        else if (balance < -1) {
            if (balanceFactor(node.right) > 0) {
                // perform a right rotation on the right child if necessary
                node.right = rotateRight(node.right);
            }
            // rotate the tree left
            return rotateLeft(node);
        }
        
        // return the node after balancing
        return node;
    }
    
    /**
     * Finds and returns the minimum value in the subtree at the given node.
     * 
     * @param node the root of the subtree 
     * @return the node with the minimum value in the subtree
     */
    private Node minValueNode(Node node) {
        Node current = node;

        // find the leftmost leaf node
        while (current.left != null)
            current = current.left;

        // return the leftmost tree node
        return current;
    }

    /**
     * Checks whether the AVL tree contains the given data element
     * 
     * @param data the data we are searching for
     * @return whether or not the data has been found in the tree
     */
    public boolean contains(T data) {
        return contains(root, data);
    }

    /**
     * A helper method to recursively check if an element is in the AVL tree
     * 
     * @param node the current node being checked
     * @param data the data we are searching for
     * @return whether or not the data has been found
     */
    private boolean contains(Node node, T data) {
        // return false if we reach a leaf
        if (node == null) {
            return false;
        }

        // store the result of the comparison
        int comparison = data.compareTo(node.data);

        // if the data is less than the current nodes data
        if (comparison < 0) {
            return contains(node.left, data); // search in the left subtree
        }
        // if the data is greater than the current nodes data
        else if (comparison > 0) {
            return contains(node.right, data); // search in the right subtree
        }
        // if the data is the same as the current nodes data
        else {
            // return true
            return true;
        }
    }

    /**
     * A method which contains the ratings from the AVL tree in descending order.
     * 
     * @param extractor an implementation defining hwo to extract ratings from elements in the AVL tree
     * @return an array containing the extracted ratings
     */
    public float[] getRatingsArray(RatingExtractor<T> extractor){
        // a array to store the expected ratings
        float[] ratingsArray = new float[size];
        // initialise array to hold current index in the ratingsArray
        int[] index = {0};

        // traverse the tree starting at the root, updating ratingsArray as you go
        treeTraversal(root, ratingsArray, index, extractor);

        // return rating array
        return ratingsArray;
    }

    /**
     * Performs a reverse in-order traversal of the AVL tree
     * 
     * @param node the current node in the traversal
     * @param ratingsArray the array of ratings of each node
     * @param index integer that points to the current index of ratingsArray
     * @param extractor RatingExtractor implementation which defines how to get rating data from the node
     */
    private void treeTraversal(Node node, float[] ratingsArray, int[] index, RatingExtractor<T> extractor){
        if (node != null){
            // recursively search right subtree first
            treeTraversal(node.right, ratingsArray, index, extractor);

            // add the current node
            ratingsArray[index[0]++] = extractor.extract(node.data);

            // recursively traverse the left subtree
            treeTraversal(node.left, ratingsArray, index, extractor);
        }
    }

    /**
     * @return the size of the avl tree
     */
    public int size(){
        return size;
    }

    /**
     * @return the average of each rating in the AVL tree
     */
    public float getAverage(){
        // return 0 if there are no elemetns in the list
        if (size == 0){
            return 0;
        }

        // return the average otherwise
        return sum / size;
    }
}
