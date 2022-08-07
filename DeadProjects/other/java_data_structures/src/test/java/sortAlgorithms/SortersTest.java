package sortAlgorithms;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import sortAlgorithms.permutations.AllPermutations;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import java.util.Arrays;


public class SortersTest {

	private long[] createSortedArray(int length) {
		long[] sorted = new long[length];
		for (int i = 1; i <= length; i++) {
			sorted[i - 1] = i;
		}
		return sorted;
	}

	@ParameterizedTest
	@ValueSource(ints = {1, 2, 3, 5}) 
	void insertSortWorksCorrectly(int arrayLength) {
		long[] sorted = this.createSortedArray(arrayLength);

		Sorter s = new InsertSorter();
		long[][] possibleVariants = new AllPermutations(arrayLength).calculate();
		for (int i = 0; i < possibleVariants.length; i++) {
			String originArray = Arrays.toString(possibleVariants[i]);
			s.sort(possibleVariants[i]);
			assertArrayEquals(sorted, possibleVariants[i], "Wrong answer for array: " + originArray);
		}
	}

	@ParameterizedTest
	@ValueSource(ints = {1, 2, 3, 5}) 
	void bubbleSortWorksCorrectly(int arrayLength) {
		long[] sorted = this.createSortedArray(arrayLength);
		
		Sorter s = new BubbleSorter();
		long[][] possibleVariants = new AllPermutations(arrayLength).calculate();
		for (int i = 0; i < possibleVariants.length; i++) {
			String originArray = Arrays.toString(possibleVariants[i]);
			s.sort(possibleVariants[i]);
			assertArrayEquals(sorted, possibleVariants[i], "Wrong answer for array: " + originArray);
		}
	}
}
