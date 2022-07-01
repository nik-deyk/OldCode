import java.util.Arrays;
import java.util.Scanner;

public class E {
    public static final class SimpleMatrix {
        long[][] data;
        SimpleMatrix(int n, int m) {
            data = new long[n][m];
        }

        //! Numeration from 1;
        long get(int i, int j) {
            return data[i - 1][j - 1];
        }

        void replaceRow(int i, long x) {
            Arrays.fill(data[i - 1], x);
        }

        void add(int L, int R, long x) {
            L --; R --;
            for (int i = 0; i < data.length; i++) {
                for (int j = L; j <= R; j++) {
                    data[i][j] += x;
                }
            }
        }
    }

    public static void main(String[] args) {
        try (Scanner input = new Scanner(System.in)) {
            int N = input.nextInt();
            int M = input.nextInt();
            int Q = input.nextInt();
            
            SimpleMatrix m = new SimpleMatrix(N, M);

            for (int k = 0; k < Q; k++) {
                int command = input.nextInt();
                long x; int i, j;
                switch (command) {
                    case 1:
                        int l = input.nextInt();
                        int r = input.nextInt();
                        x = input.nextLong();
                        m.add(l, r, x);
                        break;
                    case 2:
                        i = input.nextInt();
                        x = input.nextLong();
                        m.replaceRow(i, x);
                        break; 
                    default:
                    // 3
                        i = input.nextInt();
                        j = input.nextInt();
                        System.out.println(m.get(i, j));
                        break;
                }
            }
        }
    }
}
