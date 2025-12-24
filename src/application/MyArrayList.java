package application;

public class MyArrayList<T> {

    // Internal array to store elements
    private T[] arrayList;

    // Number of elements currently stored
    private int size;

    // Maximum capacity of the array
    private int capacity;

    // Default constructor with initial capacity = 10
    public MyArrayList() {
        this.capacity = 10;
        this.size = 0;
        arrayList = (T[]) new Object[capacity];
    }

    // Constructor with custom initial capacity
    public MyArrayList(int capacity) {
        this.capacity = capacity;
        this.size = 0;
        arrayList = (T[]) new Object[capacity];
    }

    // Returns the current number of elements
    public int getSize() {
        return size;
    }

    // Adds a new element to the list
    public void add(T value) {

        // If array is full, increase its size
        if (size == capacity) {
            reSize();
        }

        // Insert element at the end
        arrayList[size] = value;

        // Increase size counter
        size++;
    }

    // Doubles the array capacity when it becomes full
    public void reSize() {

        // Increase capacity
        capacity = capacity * 2;

        // Create a new larger array
        T[] newArray = (T[]) new Object[capacity];

        // Copy old elements to the new array
        for (int i = 0; i < size; i++) {
            newArray[i] = arrayList[i];
        }

        // Replace old array with the new one
        arrayList = newArray;
    }

    // Clears the list without deleting the array
    public void clear() {

        // Reset size (elements will be overwritten later)
        size = 0;
    }

    // Returns element at given index
    public T get(int index) {

        // Check for invalid index
        if (index < 0 || index >= size)
            return null;

        return arrayList[index];
    }
}
