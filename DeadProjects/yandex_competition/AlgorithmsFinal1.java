
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NavigableSet;
import java.util.Queue;
import java.util.Scanner;
import java.util.TreeSet;
import java.util.logging.Logger;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.UnaryOperator;

public final class AlgorithmsFinal1 {
    static final int THREADS_COUNT = 10;
    static final Logger LOGGER = Logger.getLogger(AlgorithmsFinal1.class.getName());

    final static class Suffix implements Comparable<Suffix>, Cloneable {
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
            return position;
        }

        public void rehash(UnaryOperator<BigInteger> op) {
            hash = op.apply(hash);
        }

        @Override
        protected Object clone() throws CloneNotSupportedException {
            Suffix clone = null;
            try
            {
                clone = (Suffix) super.clone();
                clone.hash = this.hash;
            } 
            catch (CloneNotSupportedException e) 
            {
                throw new RuntimeException(e);
            }
            return clone;
        }
     
        @Override
        public String toString() {
            return "Suffix[pos=" + position + ", hash=" + hash.toString() + "]";
        }
    }

    final static class ExpandableSuffixArray {
        private final int LENGTH;
        private final BigInteger BASE;
        private final BigInteger MAX_BIT;
        private final ArrayList<BigInteger> maxBits;
        private final NavigableSet<Suffix> suffixes;

        private ExpandableSuffixArray(int length, BigInteger base, BigInteger max, ArrayList<BigInteger> bits,
                NavigableSet<Suffix> suffixes) {
            this.LENGTH = length;
            this.BASE = base;
            this.MAX_BIT = max;
            this.maxBits = bits;
            this.suffixes = suffixes;
        }

        static private NavigableSet<Suffix> deepCopyTreeSet(NavigableSet<Suffix> suffixes) {
            var result = new TreeSet<Suffix>();
            for (var suffix: suffixes) {
                Suffix suffixCopy = null;
                try {
                    suffixCopy = (Suffix) suffix.clone();
                } catch (CloneNotSupportedException e) {
                    LOGGER.throwing(ExpandableSuffixArray.class.getName(), "deepCopyTreeSet", e);
                }
                result.add(suffixCopy);
            }
            return result;
        }

        public ExpandableSuffixArray(List<Integer> array, int maxValue) {
            this(array.size(), BigInteger.valueOf(maxValue + 2), BigInteger.valueOf(maxValue + 2).pow(array.size() - 1),
                    new ArrayList<BigInteger>(array.size()), new TreeSet<Suffix>());
            fillMaxBits(array);
            fillSuffixArray();
        }

        public ExpandableSuffixArray(ExpandableSuffixArray other, int newValue) {
            this(other.LENGTH + 1, other.BASE, other.MAX_BIT.multiply(other.BASE),
                    new ArrayList<>(other.maxBits), deepCopyTreeSet(other.suffixes));
            expandMaxBits(newValue);
            expandSuffixArray(newValue);
        }

        private void expandMaxBits(int newValue) {
            for (int i = 0; i < maxBits.size(); i++) {
                maxBits.set(i, maxBits.get(i).multiply(BASE));
            }
            maxBits.add(BigInteger.valueOf(newValue).multiply(MAX_BIT));
        }

        private void expandSuffixArray(int newValue) {
            var newValueBigInteger = BigInteger.valueOf(newValue);
            for (var suffix : suffixes) {
                suffix.rehash(
                        oldHash -> oldHash.multiply(BASE).add(newValueBigInteger.multiply(BASE.pow(suffix.get()))));
            }
            suffixes.add(new Suffix(newValueBigInteger.multiply(MAX_BIT), LENGTH - 1));
            LOGGER.finest(String.format("Add new hash while expanding: %d.", newValueBigInteger.multiply(MAX_BIT)));
        }

        private void fillMaxBits(List<Integer> array) {
            for (Integer value : array) {
                maxBits.add(MAX_BIT.multiply(BigInteger.valueOf(value.intValue())));
            }
        }

        private void fillSuffixArray() {
            int i = LENGTH - 1;
            var hash = maxBits.get(i);
            suffixes.add(new Suffix(hash, i));
            LOGGER.finest(String.format("Add new hash: %d.", hash));
            i--;
            while (i >= 0) {
                hash = hash.divide(BASE).add(maxBits.get(i));
                suffixes.add(new Suffix(hash, i));
                LOGGER.finest(String.format("Add new hash: %d.", hash));
                i--;
            }
        }

        private int minUniqueSuffixLength() {
            int i = LENGTH - 1;
            var rangeStart = maxBits.get(i);
            var rangeEnd = rangeStart.add(MAX_BIT);
            LOGGER.finest("Start searching suffix length.");

            while (i > 0) {
                LOGGER.finest(String.format("Next search iteration: %d / %d.", LENGTH - i, LENGTH));
                if (suffixes
                        .subSet(new Suffix(rangeStart), false,
                                new Suffix(rangeEnd), false)
                        .size() == 0) {
                    LOGGER.finest(String.format("End searching suffix length, result: %d.", LENGTH - i));
                    return LENGTH - i;
                }
                i--;
                var nextBit = maxBits.get(i);
                rangeStart = rangeStart.divide(BASE).add(nextBit);
                rangeEnd = rangeEnd.divide(BASE).add(nextBit);
            }
            LOGGER.finest(String.format("No suitable suffix found. Return sequence length: %d.", LENGTH));
            return LENGTH;
        }

        public int[] dumpSuffixArray() {
            var result = new int[LENGTH];
            int i = 0;
            for (Suffix s : suffixes) {
                result[i++] = s.get();
            }
            return result;
        }
    }

    final static class SearchResult {
        public int length;
        public int symbols;

        public SearchResult(int length, int symbols) {
            this.length = length;
            this.symbols = symbols;
        }
    }

    static class SuffixSearcher implements Callable<SearchResult> {
        static public ExecutorService executor = null;
        static public Queue<Future<SearchResult>> tasks = null;

        static public int maxValue;

        private ExpandableSuffixArray resolver;
        private int depth;
        private int symbols;

        public SuffixSearcher(ExpandableSuffixArray resolver, int depth, int symbols) {
            this.resolver = resolver;
            this.depth = depth;
            this.symbols = symbols;
        }

        @Override
        public SearchResult call() {
            LOGGER.finest(String.format("Searching, symbols num: %d, depth: %d", symbols, depth));
            int L = resolver.minUniqueSuffixLength();
            int newDepth = Integer.min(depth, L);
            SuffixSearcher.addTasks(resolver, newDepth, symbols);
            LOGGER.finest(String.format("New result: %d length, %d symbols.", L, symbols));
            return new SearchResult(L, symbols);
        }

        static public void addTasks(ExpandableSuffixArray resolver, int depth, int symbols) {
            if (depth > 1) {
                for (int i = 1; i <= maxValue; i++) {
                    SuffixSearcher.tasks
                            .add(SuffixSearcher.executor.submit(new SuffixSearcher(
                                    new ExpandableSuffixArray(resolver, i), depth - 1, symbols + 1)));
                }
            }
        }
    }

    public static ExpandableSuffixArray readArray(Scanner scan, int N) {
        assert 1 <= N && N <= 1000000 && 1 <= SuffixSearcher.maxValue && SuffixSearcher.maxValue <= 1000000;
        var inputArray = new ArrayList<Integer>(N);
        for (int i = 0; i < N; i++) {
            var t = scan.nextInt();
            assert 1 <= t && t <= SuffixSearcher.maxValue : "Invalid number in array!";
            inputArray.add(t);
        }

        return new ExpandableSuffixArray(inputArray, SuffixSearcher.maxValue);
    }

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        int N = scan.nextInt();
        SuffixSearcher.maxValue = scan.nextInt();
        var resolver = readArray(scan, N);
        scan.close();

        int L = resolver.minUniqueSuffixLength();
        int[] results = new int[L + 1]; // 0th element is not used.
        Arrays.fill(results, -1);
        results[L] = 0;

        LOGGER.finest(String.format("Starting threads..."));
        SuffixSearcher.tasks = new ConcurrentLinkedQueue<Future<SearchResult>>();
        try {
            SuffixSearcher.executor = Executors.newFixedThreadPool(THREADS_COUNT);
            SuffixSearcher.addTasks(resolver, L, 0);
            while (SuffixSearcher.tasks.size() > 0) {
                SearchResult r = SuffixSearcher.tasks.poll().get();
                if (r.length < L) {
                    int oldLength = results[r.length];
                    if (oldLength < 0) {
                        results[r.length] = r.symbols;
                    } else {
                        results[r.length] = Integer.min(oldLength, r.symbols);
                    }
                }
            }
            SuffixSearcher.executor.shutdown();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.exit(1);
        }

        for (int i = 1; i <= L; i++) {
            if (results[i] >= 0) {
                System.out.println(i + " " + results[i]);
                break;
            }
        }
    }
}