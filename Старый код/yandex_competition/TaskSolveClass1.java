
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.NavigableSet;
import java.util.Scanner;
import java.util.TreeSet;
import java.util.logging.Logger;
import java.util.logging.Level;

public final class TaskSolveClass1 {
    static final Logger LOGGER = Logger.getLogger(TaskSolveClass1.class.getName());

    final static class Suffix implements Comparable<Suffix> {
        private final BigInteger hash;
        private int position;

        public Suffix(int index, BigInteger hash) {
            assert index >= 0 && hash != null;
            this.position = index;
            this.hash = hash;
        }

        public Suffix(BigInteger hash) {
            this(0, hash);
        }

        @Override
        public int compareTo(Suffix other) {
            return hash.compareTo(other.hash);
        }

        public int get() {
            return this.position;
        }
    }

    final static class TailId implements Iterable<Integer> {
        private final int[] tailId;

        private TailId() {
            this.tailId = new int[0];
        }

        private TailId(int[] tailId) {
            assert tailId != null : "Passed tail id must be non-null!";
            this.tailId = tailId;
        }

        public class IdIterator implements Iterator<Integer> {
            private int currentPosition;

            public IdIterator() {
                this.prepare();
            }

            public void prepare() {
                this.currentPosition = 0;
            }

            public boolean hasNext() {
                return this.currentPosition < tailId.length;
            }

            public Integer next() {
                assert hasNext() : "Current position is out of bounds!";
                return tailId[this.currentPosition++];
            }
        }

        public int length() {
            return this.tailId.length;
        }

        public static TailId createEmptyTail() {
            return new TailId();
        }

        public TailId prolongTail(int next) {
            int[] newTail = new int[this.tailId.length + 1];
            System.arraycopy(this.tailId, 0, newTail, 0, this.tailId.length);
            newTail[newTail.length - 1] = next;
            return new TailId(newTail);
        }

        @Override
        public String toString() {
            return "Tail[id=" + Arrays.toString(this.tailId) + "]";
        }

        @Override
        public Iterator<Integer> iterator() {
            return new IdIterator();
        }
    }

    final static class ExpandableSuffixArray {
        private final int LENGTH;
        private final int UPPER_VALUE;
        private final BigInteger BASE;
        private final BigInteger MAX_BIT;
        private final List<Integer> elements;
        private final BigInteger[] maxBits;
        private final NavigableSet<Suffix> suffixes;

        public ExpandableSuffixArray(List<Integer> array, int maxValue) {
            this.LENGTH = array.size();
            this.UPPER_VALUE = maxValue + 2;
            this.BASE = BigInteger.valueOf(UPPER_VALUE);
            this.MAX_BIT = BASE.pow(LENGTH - 1);
            this.elements = array;
            this.maxBits = new BigInteger[LENGTH];
            this.suffixes = new TreeSet<Suffix>();
            this.fillMaxBits();
            this.calculateSuffixArray();
        }

        private void fillMaxBits() {
            for (int i = 0; i < LENGTH; i++) {
                maxBits[i] = MAX_BIT.multiply(BigInteger.valueOf(this.elements.get(i)));
            }
        }

        private void calculateSuffixArray() {
            int i = LENGTH - 1;
            var hash = maxBits[i];
            this.suffixes.add(new Suffix(i, hash));
            i--;
            while (i >= 0) {
                hash = hash.divide(BASE).add(maxBits[i]);
                this.suffixes.add(new Suffix(i, hash));
                i--;
            }
        }

        public int minUniqueSuffixLength() {
            int i = LENGTH - 1;
            var rangeStart = maxBits[i];
            var rangeEnd = rangeStart.add(MAX_BIT);
            LOGGER.finest(String.format("Start searching suffix length, i=%d.", i));
            while (i > 0) {
                if (this.suffixes
                        .subSet(new Suffix(rangeStart), false,
                                new Suffix(rangeEnd), false)
                        .size() == 0) {
                    LOGGER.finest(String.format("End searching suffix length, result=%d", LENGTH - i));
                    return LENGTH - i;
                }
                i--;
                var nextBit = maxBits[i];
                rangeStart = rangeStart.divide(BASE).add(nextBit);
                rangeEnd = rangeEnd.divide(BASE).add(nextBit);
            }
            return LENGTH;
        }

        public int maxUniqueSuffixLength(TailId tail) {

            return LENGTH;
        }

        public int[] dumpSuffixArray() {
            var result = new int[LENGTH];
            int i = 0;
            for (Suffix s : this.suffixes) {
                result[i++] = s.get();
            }
            return result;
        }
    }

    public static void main(String[] args) {
        Scanner myInput = new Scanner(System.in);
        int n = myInput.nextInt();
        int k = myInput.nextInt();
        assert 1 <= n && n <= 1000000 && 1 <= k && k <= 1000000;
        var inputArray = new ArrayList<Integer>(n);
        for (int i = 0; i < n; i++) {
            var t = myInput.nextInt();
            assert 1 <= t && t <= k : "Invalid number in array!";
            inputArray.add(t);
        }
        myInput.close();
        var res = new ExpandableSuffixArray(inputArray, k);
        System.out.println(Arrays.toString(res.dumpSuffixArray()));
        System.out.println(res.minUniqueSuffixLength());
    }
}