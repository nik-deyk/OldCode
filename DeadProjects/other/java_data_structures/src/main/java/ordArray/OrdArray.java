/**
* @file OrdArray.java
* Demonstrates ordered array class.
* Example from "Data Structures and Algorithms in Java" by Robert Lafore.
* I made it to use binary search when inserting an element and added
* some good ways to interact with the data structure in main function.
*/
package ordArray;

/**
 * The data structure representing one fixes-size sorted array.
 */
public class OrdArray {
	private long[] a;
	private int elementsCount;

	public OrdArray(int max) {
		a = new long[max];
		elementsCount = 0;
	}

	public int size() {
		return elementsCount;
	}

	private static class FindResult {
		public final int index;
		public final boolean success;

		FindResult(int index, boolean success) {
			this.index = index;
			this.success = success;
		}
	}

	private FindResult binarySearch(long searchKey) {
		int lowerBound = 0;
		int upperBound = elementsCount - 1;
		int curIn;

		while (lowerBound <= upperBound) {
			curIn = (lowerBound + upperBound) / 2;
			if (a[curIn] == searchKey)
				return new FindResult(curIn, true);
			else if (a[curIn] < searchKey)
				lowerBound = curIn + 1;
			else
				upperBound = curIn - 1;
		}
		return new FindResult(lowerBound, false);
	}

	public int find(long searchKey) {
		var res = this.binarySearch(searchKey);
		return (res.success) ? res.index : this.elementsCount;
	}

	public boolean insert(long value) {
		var searchRes = this.binarySearch(value);
		if (this.elementsCount == this.a.length || searchRes.success) {
			return false;
		}
		for (int k = elementsCount; k > searchRes.index; k--)
			a[k] = a[k - 1];
		a[searchRes.index] = value;
		elementsCount++;
		return true;
	}

	public boolean delete(long value) {
		var searchRes = this.binarySearch(value);
		if (!searchRes.success) {
			return false;
		} else {
			for (int k = searchRes.index; k < elementsCount - 1; k++)
				a[k] = a[k + 1];
			elementsCount--;
			return true;
		}
	}

	public String toString() {
		StringBuilder representation = new StringBuilder();
		for (int j = 0; j < elementsCount; j++) {
			representation.append(a[j] + " ");
		}
		return representation.toString();
	}

	public boolean merge(OrdArray other) {
		long[] result = new long[this.a.length];
		int i = 0, j = 0, k = 0;
		boolean oneElementCanNotBeAdded = false;
		while ((i < this.elementsCount || j < other.elementsCount) && 
				!oneElementCanNotBeAdded) {
			long valueToAdd = 0;
			if (i < this.elementsCount && (j >= other.elementsCount || this.a[i] < other.a[j])) {
				valueToAdd = this.a[i++];
			} else {
				valueToAdd = other.a[j++];
			}
			if (k == 0 || result[k - 1] != valueToAdd) {
				if (k < result.length) {
					result[k++] = valueToAdd;
				} else {
					oneElementCanNotBeAdded = true;
				}
			}
		}
		this.a = result;
		this.elementsCount = k;
		return oneElementCanNotBeAdded;
	}
}
