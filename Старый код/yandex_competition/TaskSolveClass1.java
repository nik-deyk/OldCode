
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
import java.util.stream.Stream;
import java.util.stream.Collectors;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.Callable;

public final class TaskSolveClass1 {
    static final int THREADS_COUNT = 10;
    static final Logger LOGGER = Logger.getLogger(TaskSolveClass1.class.getName());

    final static class Suffix implements Comparable<Suffix> {
        private BigInteger hash;
        private final int position;

        public Suffix(BigInteger hash, int index) {
            assert index >= 0 && hash != null;
            this.position = index;
            this.hash = hash;
        }

        public Suffix(BigInteger hash) {
            this(hash, 0);
        }

        @Override
        public int compareTo(Suffix other) {
            return hash.compareTo(other.hash);
        }

        public int get() {
            return this.position;
        }

        public Suffix expand(TailId tail, BigInteger base) {
            assert tail != null && tail.length() > 0;
            var shift = base.pow(tail.length());
            var expandArea = tail.toBigInteger(base).multiply(base.pow(this.get()));
            this.hash = this.hash.multiply(shift).add(expandArea);
            return this;
        }

        final public static class Expander {
            private TailId tail;
            private BigInteger base;

            Expander(TailId tail, BigInteger base) {
                this.tail = tail;
                this.base = base;
            }

            public Suffix expand(Suffix s) {
                return s.expand(tail, base);
            }
        }
    }

    final static class TailId implements Iterable<Integer> {
        private final int[] tailId;
        private final BigInteger[] maxBits;

        private TailId(int[] tailId) {
            assert tailId != null : "Passed tail id must be non-null!";
            this.tailId = tailId;
            this.maxBits = new BigInteger[tailId.length];
        }

        final public class IdIterator implements Iterator<Integer> {
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

        public static TailId singleTail(int number) {
            return new TailId(new int[]{number});
        }

        public TailId prolong(int next) {
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

        public BigInteger toBigInteger(BigInteger base) {
            assert this.length() > 0 : "Only non-empty tail can be converted to BigInteger";
            var result = BigInteger.valueOf(this.tailId[this.length() - 1]);
            for (int i = this.length() - 2; i >= 0; i--) {
                result = result.multiply(base).add(BigInteger.valueOf(this.tailId[i]));
            }
            return result;
        }

        private void fillMaxBits(BigInteger maxBit) {
            for (int i = 0; i < this.length(); i++) {
                this.maxBits[i] = maxBit.multiply(BigInteger.valueOf(this.tailId[i]));
            }
        }

        public Suffix[] suffixes(BigInteger base, BigInteger maxBit, int origLength) {
            this.fillMaxBits(maxBit);
            var result = new Suffix[this.length()];
            int i = this.length() - 1;
            var hash = maxBits[i];
            result[i] = new Suffix(hash, i);
            i--;
            while (i >= 0) {
                hash = hash.divide(base).add(maxBits[i]);
                result[i] = new Suffix(hash, i + origLength);
                i--;
            }
            return result;
        }

        public BigInteger[] maxBits() {
            return this.maxBits;
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
            this.suffixes.add(new Suffix(hash, i));
            i--;
            while (i >= 0) {
                hash = hash.divide(BASE).add(maxBits[i]);
                this.suffixes.add(new Suffix(hash, i));
                i--;
            }
        }

        static private int minUniqueSuffixLength(NavigableSet<Suffix> suffixes, 
                                                 BigInteger[] maxBits, 
                                                 BigInteger maxBit, 
                                                 BigInteger base) {
            var length = suffixes.size();
            int i = length - 1;
            var rangeStart = maxBits[i];
            var rangeEnd = rangeStart.add(maxBit);
            LOGGER.finest("Start searching suffix length.");
            while (i > 0) {
                LOGGER.finest(String.format("Next search iteration: %d / %d.", length - i, length));
                if (suffixes
                        .subSet(new Suffix(rangeStart), false,
                                new Suffix(rangeEnd), false)
                        .size() == 0) {
                    LOGGER.finest(String.format("End searching suffix length, result: %d.", length - i));
                    return length - i;
                }
                i--;
                var nextBit = maxBits[i];
                rangeStart = rangeStart.divide(base).add(nextBit);
                rangeEnd = rangeEnd.divide(base).add(nextBit);
            }
            return length;
        }

        public int minUniqueSuffixLength() {
            return ExpandableSuffixArray.minUniqueSuffixLength(this.suffixes, this.maxBits, this.MAX_BIT, this.BASE);
        }

        public int minUniqueSuffixLength(TailId tail) {
            var e = new Suffix.Expander(tail, BASE);
            var multiplier = BASE.pow(tail.length());
            var newMaxBit = MAX_BIT.multiply(multiplier);
            return ExpandableSuffixArray.minUniqueSuffixLength(
                Stream.<Suffix>concat(
                    this.suffixes.stream().<Suffix>map(e::expand),
                    Arrays.stream(tail.suffixes(BASE, newMaxBit, this.LENGTH))
                ).collect(Collectors.toCollection(() -> new TreeSet<Suffix>())),
                Stream.<BigInteger>concat(
                    Arrays.stream(this.maxBits).<BigInteger>map(multiplier::multiply),
                    Arrays.stream(tail.maxBits())
                ).toArray(BigInteger[]::new),
                newMaxBit,
                BASE);
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
    
    final static class SearchResult {
        public int length;
        public int symbols;
        SuffixResult(int length, int symbols) {
            this.length = length;
            this.symbols = symbols;
        }
    }

    static class SuffixSearcher implements Callable<SearchResult> {
        private ExpandableSuffixArray resolver;
        private TailId id;
        private int depth;

        @Override
        public SearchResult call() {
            return 12;
        }
    }

    public static ExpandableSuffixArray read() {
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
        
        return new ExpandableSuffixArray(inputArray, k);
    }

    public static void main(String[] args) {
        var resolver = read();
        ExecutorService executor = Executors.newFixedThreadPool(THREADS_COUNT);
        var tasks = new ArrayList<Callable<Integer>>();
        try {
            List<Future<Integer>> results = executor.<Integer>invokeAll(tasks);
            executor.shutdown();
            for (var result : results) System.out.println(result.get());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.exit(0);
        }
    }
}