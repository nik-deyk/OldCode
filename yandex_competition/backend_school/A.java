import java.util.Scanner;

public class A {
    static String A, B;
    static char[] output;

    public static void main(String[] args) {
        try (Scanner myInput = new Scanner(System.in)) {
            A = myInput.next();
            B = myInput.next();
        }
        assert A.length() == B.length() : "Strings must be the same size!";
        output = new char[A.length()];

        int[] letters = new int[27]; // Number of capital letters.
        
        for (int i = 0; i < A.length(); i++) {
            if (A.charAt(i) == B.charAt(i)) {
                output[i] = 'P';
            } else {
                letters[A.charAt(i) - 'A']++;
            }
        }

        for (int i = 0; i < B.length(); i++) {
            if (output[i] != 'P') {
                if (letters[B.charAt(i) - 'A'] > 0) {
                    letters[B.charAt(i) - 'A']--;
                    output[i] = 'S';
                } else {
                    output[i] = 'I';
                }
            }
        }

        for (char c : output) {
            System.out.print(c);
        }
        System.out.println();
    }
}