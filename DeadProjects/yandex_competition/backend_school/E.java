import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Stack;

public class E {

    public static void main(String[] args) {
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader("input.txt"));

            int openedBracketsCount = 0;
            Integer firstBadOpenedBracket = null;
            Integer closedBracket = null;
            Integer firstClosedBracket = null;

            char c;
            int currentPosition = 1;
            while ((c = (char) bufferedReader.read()) != '\n') {
                if (c == '{') {
                    openedBracketsCount++;
                    if (openedBracketsCount == 1) {
                        firstBadOpenedBracket = Integer.valueOf(currentPosition);
                    }
                } else if (c == '}') {
                    if (firstClosedBracket == null) {
                        firstClosedBracket = Integer.valueOf(currentPosition);
                    }

                    if (openedBracketsCount == 0) {
                        if (closedBracket != null) {
                            // The second closed bracket we found. No matter what we will find next, it's
                            // impossible to correct the statement with only one deletion:
                            bufferedReader.close();
                            System.out.println(-1);
                            return;
                        } else {
                            closedBracket = Integer.valueOf(currentPosition);
                        }
                    } else {
                        openedBracketsCount--;
                        if (openedBracketsCount == 0) {
                            firstBadOpenedBracket = null;
                        }
                    }
                }

                currentPosition++;
            }

            bufferedReader.close();

            if (openedBracketsCount == 0 && closedBracket == null) {
                System.out.println(-1); // All ok.
            } else if (openedBracketsCount == 0 && closedBracket != null) {
                System.out.println(firstClosedBracket.intValue());
            } else if (openedBracketsCount == 1 && closedBracket == null) {
                System.out.println(firstBadOpenedBracket.intValue());
            } else {
                System.out.println(-1);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
