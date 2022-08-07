package sortAlgorithms;

public class BubbleSorter implements Sorter {
	private long[] currentSortingArray;
	
	private void swap(int i, int j) {
		long temp = currentSortingArray[i];
		currentSortingArray[i] = currentSortingArray[j];
		currentSortingArray[j] = temp;
	}

	@Override
	public void sort(final long[] arr) {
		this.currentSortingArray = arr;
		
		for (int lastSorted = arr.length; lastSorted > 1; lastSorted--) {
			for (int i = 0; i < lastSorted - 1; i++) {
				if (arr[i] > arr[i + 1]) {
					this.swap(i, i + 1);
				}
			}
		}
	}
}
