package sortAlgorithms;

public class InsertSorter implements Sorter {

	@Override
	public void sort(long[] arr) {
		for(int out = 1; out < arr.length; out++) {
			long temp = arr[out];
			int i = out - 1;
			for (; i >= 0 && arr[i] > temp; i--) {
				arr[i + 1] = arr[i];
			}
			arr[i + 1] = temp;
		}
	}

}
