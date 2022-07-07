package ordArray;

import static java.lang.System.out;

/**
* @file orderedArray.java
* Demonstrates ordered array class.
* Example from "Data Structures and Algorithms in Java" by Robert Lafore.
* I made it to use binary search when inserting an element and added
* some good ways to interact with the data structure in main function.
*/
public class OrdArray {
	private long[] a;
	private int nElems;

	public OrdArray(int max) // constructor
	{
		a = new long[max]; // create array
		nElems = 0;
	}

	public int size() {
		return nElems;
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
		int upperBound = nElems - 1;
		int curIn;

		while (lowerBound <= upperBound) {
			curIn = (lowerBound + upperBound) / 2;
			if (a[curIn] == searchKey)
				return new FindResult(curIn, true); // found it
			else if (a[curIn] < searchKey) // divide range
				lowerBound = curIn + 1; // it's in upper half
			else
				upperBound = curIn - 1; // it's in lower half
		} // end while
		return new FindResult(lowerBound, false);
	}

	public int find(long searchKey) {
		var res = this.binarySearch(searchKey);
		return (res.success) ? res.index : this.nElems;
	}

	public boolean insert(long value) // put element into array
	{
		var searchRes = this.binarySearch(value);
		if (searchRes.success || this.nElems == this.a.length) {
			return false;
		}
		for (int k = nElems; k > searchRes.index; k--) // move bigger ones up
			a[k] = a[k - 1];
		a[searchRes.index] = value; // insert it
		nElems++; // increment size
		return true;
	}

	public boolean delete(long value) {
		var searchRes = this.binarySearch(value);
		if (!searchRes.success) // can't find it
			return false;
		else // found it
		{
			for (int k = searchRes.index; k < nElems - 1; k++) // move bigger ones down
				a[k] = a[k + 1];
			nElems--; // decrement size
			return true;
		}
	}

	public void display() // displays array contents
	{
		for (int j = 0; j < nElems; j++) // for each element,
			out.print(a[j] + " "); // display it
		out.println();
	}

}
