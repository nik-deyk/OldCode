import java.util.Scanner;
import java.util.concurrent.Callable;

public class C {
    static class Reel {
        int[] numbers;
    
        public Reel(String numbers) {
            this.numbers = new int[10];
            for (int i = 0; i < numbers.length(); i++) {
                this.numbers[numbers.charAt(i) - '0'] = i;
            }
        }
    
        public int getIndex(int number) {
            return numbers[number];
        }
    }

    public static final int THREADS_COUNT = 12;
    public static Reel[] casino;

    public static final class GetMinJackpotTime implements Callable<Integer> {
        private int luckyNumber;

        public GetMinJackpotTime(int number) {
            this.luckyNumber = number;
        }

        @Override
        public Integer call() {
            int[] cases = new int[10];
            int max_cases = -1;
            int max_cases_index = -1;
            for (int i = 0; i < casino.length; i++) {
                int position = casino[i].getIndex(luckyNumber);
                if (++cases[position] > max_cases || max_cases == -1) {
                    max_cases_index = position;
                    max_cases = cases[position];
                } else if (cases[position] == max_cases && max_cases_index < position) {
                    max_cases_index = position;
                }
            }
            return max_cases_index + (max_cases - 1) * 10;
        }
    }

    public static void main(String[] args) {
        int N;
        try (Scanner input = new Scanner(System.in)) {
            N = input.nextInt();
            casino = new Reel[N];
            for (int i = 0; i < N; i++) {
                casino[i] = new Reel(input.next());
            }
        }

        int minTime = -1;
        for (int luckyNumber = 0; luckyNumber < 10; luckyNumber++) {
            int time = new GetMinJackpotTime(luckyNumber).call();
            if (time < minTime || minTime == -1) {
                minTime = time;
            }
        }

        System.out.println(minTime);
    }
}