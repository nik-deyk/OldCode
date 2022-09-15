import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.LongAdder;

public class B {
    public static final int THREADS_COUNT = 12;
    public static final int SPLIT_COUNT = 2000;
    public static final int MIN_LENGTH_PER_TASK = 10;

    static class GetSumTask implements Runnable {
        private final int start;
        private final int end;
        private final int a;
        private final int b;
        public static volatile LongAdder sum;
    
        public GetSumTask(int start, int end, int A, int B) {
            this.start = start;
            this.end = end;
            this.a = A;
            this.b = B;
        }
    
        @Override
        public void run() {
            int remainder = (start % a);
            int a_even = (remainder > 0) ? (start - remainder + a) : start;
            remainder = (start % b);
            int b_even = (remainder > 0) ? (start - remainder + b) : start;
            for (int i = start; i <= end; i++) {
                boolean wasAnyEven = false;
                if (i == a_even) {
                    a_even += a;
                    wasAnyEven = true;
                } 
                if (i == b_even) {
                    b_even += b;
                    wasAnyEven = true;
                } 
                
                if (!wasAnyEven){
                    sum.add(i);
                }
            }
        }
    }

    public static void main(String[] args) {
        int N, A, B;
        try (Scanner input = new Scanner(System.in)) {
            N = input.nextInt();
            A = input.nextInt();
            B = input.nextInt();
        }
        GetSumTask.sum = new LongAdder();
        
        if (N < SPLIT_COUNT * MIN_LENGTH_PER_TASK) {
            new GetSumTask(1, N, A, B).run();
        } else {
            ExecutorService executorService = Executors.newFixedThreadPool(THREADS_COUNT);
            int splitPart = N / SPLIT_COUNT;
            for (int i = 0; i < SPLIT_COUNT; i++) {
                int newStart = 1 + splitPart * i;
                int newEnd = splitPart * (i + 1);
                if (newEnd > N) newEnd = N;
                executorService.submit(new GetSumTask(newStart, newEnd, A, B));
            }
            executorService.shutdown();

            try {
                if (!executorService.awaitTermination(3, TimeUnit.SECONDS)) {
                    executorService.shutdownNow();
                    System.out.println("Time out!");
                    System.exit(1);
                }
            } catch (InterruptedException ex) {
                executorService.shutdownNow();
                System.out.println("Interrupt occurred!");
                System.exit(1);
            }
        }


        System.out.println(GetSumTask.sum.sum());
    }
}