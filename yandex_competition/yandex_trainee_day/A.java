import java.util.Scanner;

public class A {
    private static final int LETTERS_COUNT = 27;
    private static final int TWENTY_SEVEN_SQUARED = LETTERS_COUNT * LETTERS_COUNT; // 27**2.
    private static final int TWENTY_SEVEN_THIRD_POWER = LETTERS_COUNT * TWENTY_SEVEN_SQUARED; // 27**3.
    private static final int[] grams = new int[LETTERS_COUNT * TWENTY_SEVEN_THIRD_POWER]; // 27**4.

    private static boolean findTrigram(int codedTrigram) {
        // Check for '_abc':
        for (int current = 0; current < grams.length; current += TWENTY_SEVEN_THIRD_POWER) {
            if (grams[current + codedTrigram] > 0) {
                return true;
            }
        }
        // Check for 'abc_':
        codedTrigram *= LETTERS_COUNT;
        for (int current = 0; current < LETTERS_COUNT; current++) {
            if (grams[codedTrigram + current] > 0) {
                return true;
            }
        }
        return false;
    }

    public static void main(String[] args) {
        int totalVertexes = 0, N = 0, totalArcs = 0;
        try (Scanner myInput = new Scanner(System.in)) {
            N = myInput.nextInt();
            for (int i = 0; i < N; i++) {
                String roomName = myInput.next();
                for (int j = 0; j < roomName.length() - 3; j++) {
                    // Increase vertex count...
                    int commonPart = (int) (roomName.charAt(j + 1) - 'a') * LETTERS_COUNT
                            + (int) (roomName.charAt(j + 2) - 'a');
                    int firstTrigram = (int) (roomName.charAt(j) - 'a') * TWENTY_SEVEN_SQUARED
                            + commonPart;
                    int lastCharIndex = (int) (roomName.charAt(j + 3) - 'a');
                    int secondTrigram = commonPart * LETTERS_COUNT + lastCharIndex;
                    totalVertexes += (findTrigram(firstTrigram) ? 0 : 1)
                            + (firstTrigram == secondTrigram || findTrigram(secondTrigram) ? 0 : 1);
                    // Increase arcs count and arc weight:
                    int gramToAdd = firstTrigram * LETTERS_COUNT + lastCharIndex;
                    totalArcs += (grams[gramToAdd]++ == 0) ? 1 : 0;
                }
            }
        }

        System.out.println(totalVertexes);
        System.out.println(totalArcs);
        int i = 0;
        StringBuilder str = new StringBuilder("aaa aaa ");
        for (int firstChar = 0; firstChar < LETTERS_COUNT && totalArcs > 0; firstChar++) {
            str.setCharAt(0, (char)(firstChar + 'a'));
            for (int secondChar = 0; secondChar < LETTERS_COUNT && totalArcs > 0; secondChar++) {
                str.setCharAt(1, (char)(secondChar + 'a'));
                str.setCharAt(4, (char)(secondChar + 'a'));
                for (int thirdChar = 0; thirdChar < LETTERS_COUNT && totalArcs > 0; thirdChar++) {
                    str.setCharAt(2, (char)(thirdChar + 'a'));
                    str.setCharAt(5, (char)(thirdChar + 'a'));
                    for (int lastChar = 0; lastChar < LETTERS_COUNT && totalArcs > 0; lastChar++) {
                        str.setCharAt(6, (char)(lastChar + 'a'));
                        if (grams[i] != 0) {
                            System.out.println(str.toString() + grams[i]);
                            totalArcs--;
                        }
                        i++;
                    }
                }
            }
        }
    }
}