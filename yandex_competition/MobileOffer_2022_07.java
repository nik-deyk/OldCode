import java.util.Scanner;

public class Num1 {

    static private boolean atEnd(int index, char[] array) {
        return index < 0 || index >= array.length;
    }

    static private boolean isEqual(char[] first, int firstIndex, char[] second, int secondIndex) {
        int originFirst = firstIndex;
        int originSecond = secondIndex;
        int lastBadFirstIndex = -1;
        int lastBadSecondIndex = -1;

        int badLetters = 0;
        int firstSizeMismatch = -1;
        boolean firstSizeMismatchWasOnFirstArray = false;

        while (!atEnd(firstIndex, first) || !atEnd(secondIndex, second)) {
            if (!atEnd(firstIndex, first) && !atEnd(secondIndex, second)) {
                char firstChar = first[firstIndex];
                if (firstChar == '.') {
                    firstIndex++;
                    secondIndex = Integer.max(secondIndex - 1, originSecond);
                    continue;
                }
                char secondChar = second[secondIndex];
                if (secondChar == '.') {
                    secondIndex++;
                    firstIndex = Integer.max(firstIndex - 1, originFirst);
                    continue;
                }
                if (firstChar != secondChar) {
                    if (lastBadFirstIndex == -1 && lastBadSecondIndex == -1) {
                        lastBadFirstIndex = firstIndex;
                        lastBadSecondIndex = secondIndex;
                    }
                } else if (lastBadFirstIndex >= firstIndex || lastBadSecondIndex >= secondIndex) {
                    lastBadFirstIndex = -1;
                    lastBadSecondIndex = -1;
                }
                secondIndex++; firstIndex++;
            } else if (atEnd(firstIndex, first) && !atEnd(secondIndex, second)) {
                if (firstSizeMismatch == -1) {
                    firstSizeMismatch = secondIndex;
                    firstSizeMismatchWasOnFirstArray = true;
                }
                char secondChar = second[secondIndex++];
                if (secondChar == '.') {
                    badLetters--;
                } else {
                    badLetters++;
                }
            } else {
                if (firstSizeMismatch == -1) {
                    firstSizeMismatch = firstIndex;
                    firstSizeMismatchWasOnFirstArray = false;
                }
                char firstChar = first[firstIndex++];
                if (firstChar == '.') {
                    badLetters--;
                } else {
                    badLetters++;
                }
            }
        }

        if (badLetters > 0) {
            return false;
        } else {
            if (lastBadFirstIndex == -1 && lastBadSecondIndex == -1) {
                return true;
            } else {
                if (firstSizeMismatchWasOnFirstArray) {
                    return lastBadSecondIndex >= firstSizeMismatch + badLetters;
                } else {
                    return lastBadFirstIndex >= firstSizeMismatch + badLetters;
                }
            }
        }
    }

    static public void main(String[] args) {
        try (Scanner myInput = new Scanner(System.in)) {
            System.out.println(Num1.isEqual(myInput.next().toCharArray(), 0,
                                            myInput.next().toCharArray(), 0));
        }
    }
}
