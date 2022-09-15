import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Scanner;

public class D {
    static HashMap<String, ArrayList<Integer>> map = new HashMap<>();

    static private void storeNumber(String key, int value, final int level) {
        ArrayList<Integer> list;
        if (map.containsKey(key)) {
            list = map.get(key);
            while (list.size() < level) {
                list.add(0);
            }
        } else {
            list = new ArrayList<>(Collections.nCopies(level, 0));
        }
        list.set(list.size() - 1, value);
        map.put(key, list);
    }

    public static void main(String[] args) {
        int currentLevel = 1;
        try (Scanner in = new Scanner(System.in)) {
            while (in.hasNext()) {
                String line = in.next();
                if (line.indexOf('=') != -1) {
                    String firstVariable = line.substring(0, line.indexOf('='));
                    String secondOperand = line.substring(line.indexOf('=') + 1);
                    if (Character.isLetter(secondOperand.charAt(0))) {
                        ArrayList<Integer> list = map.get(secondOperand);
                        int value = list == null ? 0 : list.get(list.size() - 1);
                        storeNumber(firstVariable, value, currentLevel);
                        System.out.println(value);
                    } else { // Its a digit.
                        storeNumber(firstVariable, Integer.parseInt(secondOperand), currentLevel);
                    }
                } else if (line.equals("{")) {
                    currentLevel++;
                } else {
                    currentLevel--;
                    for (String key : map.keySet()) {
                        ArrayList<Integer> list = map.get(key);
                        while (list.size() > currentLevel) {
                            list.remove(list.size() - 1);
                        }
                    }
                }
            }
        }
    }
}