import java.util.Scanner;

public class C {
    public static void main(String[] args) {
        int sum = 0, minPositive, maxNegative;
        try (Scanner in = new Scanner(System.in)) {
            int N = in.nextInt();
            minPositive = in.nextInt();
            maxNegative = in.nextInt();
            sum += minPositive - maxNegative;
            boolean isPositive = true;
            for (int i = 2; i < N; i++) {
                int newData = in.nextInt();
                sum += newData * (isPositive ? 1 : -1);
                if (isPositive) {
                    if (newData < minPositive) {
                        minPositive = newData;
                    }
                } else {
                    if (newData > maxNegative) {
                        maxNegative = newData;
                    }
                }
                isPositive ^= true;
            }
        }

        System.out.println(Integer.max(sum, sum - 2 * (minPositive - maxNegative)));
    }
}