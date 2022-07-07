package ordArray;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Collections;

public class OrdArrayTest {
    private ArrayList<Long> initArray(int size) {
        ArrayList<Long> numbers = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            numbers.add(Long.valueOf(i));
        }
        return numbers;
    }

    @Test
    void newArrayHaveZeroSize() {
        OrdArray arr = new OrdArray(1);
        assertEquals(arr.size(), 0, "New array must have zero size");
    }
    
    @ParameterizedTest
    @ValueSource(ints = {1, 3, 5, 15}) 
    void sizeIsCorrectAfterInsertingSeveralValues(int number) {
        OrdArray arr = new OrdArray(number);
        for (int i = 0; i < number; i++) {
            arr.insert(i);
        }
        assertEquals(arr.size(), number, "Array must have correct size after inserting" +
                                         " multiple different values");
    }

    @ParameterizedTest
    @ValueSource(longs = {-1, 0, 1, 3, 5, 15, Long.MIN_VALUE, Long.MAX_VALUE}) 
    void findReturnsZeroIndexForArrayWithOneValue(long key) {
        OrdArray arr = new OrdArray(1);
        arr.insert(key);
        assertEquals(arr.find(key), 0, "Index of found element must be zero");
    }

    @Test
    void findReturnsCorrectIndexesForDifferentPlacesInArrayAndCorrectDisplay() {
        OrdArray arr = new OrdArray(10);
        arr.insert(0);
        arr.insert(12);
        arr.insert(-3);
        arr.insert(44);
        arr.insert(30);
        // Actual -3 0 12 30 44
        assertEquals(arr.find(-3), 0, "The minimum key must be in the beginning");
        assertEquals(arr.find(12), 2, "The middle key must appear in the middle of array");
        assertEquals(arr.find(44), 4, "The maximum key must be in the end of array");
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 3, 5, 15, 1000}) 
    void insertingAndDeletingReturnsTrueButTheSizeBecomeZero(int testSize) {
        var numbers = this.initArray(testSize);
        Collections.shuffle(numbers);
        OrdArray arr = new OrdArray(testSize);
        for (Long value : numbers) {
            assertTrue(arr.insert(value), "Insert must return true");
        }
        Collections.shuffle(numbers);
        for (Long value : numbers) {
            assertTrue(arr.delete(value), "Delete must return true");
        }
        assertEquals(arr.size(), 0, "Result size must become zero");
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 3, 5, 15, 1000, 10000}) 
    void valueCanNotBeFoundAfterDeleting(int testSize) {
        var numbers = this.initArray(testSize);
        Collections.shuffle(numbers);
        Long first = numbers.get(0);
        OrdArray arr = new OrdArray(testSize);
        for (Long value: numbers) {
            arr.insert(value);
        }
        arr.delete(first);
        assertEquals(arr.find(first), arr.size(), "Result of find method should be the " +
                                                  "size of array in case of find failure");
    }

    @ParameterizedTest
    @ValueSource(ints = {15, 1000, 10000})
    void eachFindCallReturnsCorrectIndex(int testSize) {
        var numbersSorted = this.initArray(testSize);
        Collections.sort(numbersSorted); 
        ArrayList<Long> numbersShuffled = new ArrayList<Long>(testSize);
        numbersShuffled.addAll(numbersSorted);
        Collections.shuffle(numbersShuffled);
        OrdArray arr = new OrdArray(testSize);
        for (Long value: numbersShuffled) {
            arr.insert(value);
        }
        Collections.shuffle(numbersShuffled);
        for (Long value: numbersShuffled) {
            int index = arr.find(value);
            assertEquals(numbersSorted.get(index), value, "Find return correct value");
        }
    }
}
