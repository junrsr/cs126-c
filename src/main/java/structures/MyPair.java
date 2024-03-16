package structures;

// class used to store pairs of identifiers and values (such as size and average)
public class MyPair<K, V extends Number> implements Comparable<MyPair<K, V>>{
    public K identifier;
    public V value;

    // pair constructor
    public MyPair(K identifier, V value){
        this.identifier = identifier;
        this.value = value;
    }

    // comparison method based on the value stored for each pair
    public int compareTo(MyPair other){
        return Double.compare(this.value.doubleValue(), other.value.doubleValue());
    }
}