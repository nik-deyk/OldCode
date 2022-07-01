import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;


public class A {
    public static void main(String[] args) {
        int N, K;
        Set<Integer> tastyIndexes = new HashSet<Integer>();
        int maxTaste = -1;
        try (Scanner input = new Scanner(System.in)) {
            N = input.nextInt();
            K = input.nextInt();
            for (int i = 1; i <= N; i++) {
                int taste = input.nextInt();
                if (tastyIndexes.isEmpty()) {
                    tastyIndexes.add(i);
                    maxTaste = taste;
                } else if (taste > maxTaste) {
                    tastyIndexes.clear();
                    tastyIndexes.add(i);
                    maxTaste = taste;
                } else if (taste == maxTaste) {
                    tastyIndexes.add(i);
                }
            }
            for (int i = 1; i <= K; i++) {
                int missingIndex = input.nextInt();
                tastyIndexes.remove(missingIndex);
            }
        }

        System.out.println(tastyIndexes.isEmpty() ? "No" : "Yes");
    }
}