package sortAlgorithms.permutations;

public class AllPermutations {
	private final int elementsCount;
	private long variants[][];

	private static int factorial(int x) {
		int res = 1;
		for (int i = 2; i <= x; i++) {
			res *= i;
		}
		return res;
	}

	private int findZeroAtPosition(int indexInArray, int positionToFind) {
		long[] currentVariant = this.variants[indexInArray];
		final int unitPosition = indexInArray % elementsCount;
		int currentPosition = (unitPosition + 1) % elementsCount;
		for (; currentPosition != unitPosition &&
				(currentVariant[currentPosition] != 0 || positionToFind-- != 0); currentPosition = (currentPosition + 1)
						% elementsCount)
			;
		return currentPosition;
	}

	public AllPermutations(int elementsCount) {
		if (elementsCount < 1 || 7 < elementsCount) {
			throw new IllegalArgumentException("Since we are going to create a huge array" +
					" you can select only small positive count values.");
		}
		this.elementsCount = elementsCount;
	}

	public long[][] getResult() {
		if (this.variants == null || this.variants[0][0] == 0L) {
			throw new IllegalStateException("Call calculate function firstly!");
		}
		return this.variants;
	}

	public long[][] calculate() {
		int chunkSizeMultiplier = elementsCount;
		int previousChunkSize = elementsCount;
		int chunkSize = previousChunkSize * --chunkSizeMultiplier;
		this.variants = new long[factorial(elementsCount)][elementsCount];

		for (int i = 0; i < this.variants.length; i++) {
			this.variants[i][i % elementsCount] = 1;
		}
		for (long element = 2; element <= elementsCount; element++) {
			for (int indexInArray = 0; indexInArray < this.variants.length; indexInArray++) {
				int indexInChunk = indexInArray % chunkSize;
				int positionToFind = indexInChunk / previousChunkSize;
				this.variants[indexInArray][findZeroAtPosition(indexInArray, positionToFind)] = element;
			}
			previousChunkSize = chunkSize;
			chunkSize *= --chunkSizeMultiplier;
		}

		return this.variants;
	}
}
