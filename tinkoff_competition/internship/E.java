import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import java.util.LinkedList;

public class E {
    static class CharTree {

        static class TreeElement  implements Comparable<TreeElement> {
            public final char letter;
            public int position; // Or -1 if no associated name.

            private LinkedList<TreeElement> descendants = new LinkedList<>();

            public TreeElement(char letter, int position) {
                this.letter = letter;
                this.position = position;
            }
    
            public void addDescendant(TreeElement child) {
                descendants.add(child);
                Collections.sort(descendants);
            }
    
            public TreeElement getDescendant(char letter) {
                for (TreeElement child : descendants) {
                    if (child.letter == letter) {
                        return child;
                    }
                }
                return null;
            }

            public LinkedList<TreeElement> getDescendants() {
                return descendants;
            }
    
            @Override
            public int hashCode() {
                return letter;
            }
            
            @Override
            public boolean equals(Object obj) {
                if (this == obj)
                    return true;
                if (obj == null)
                    return false;
                if (getClass() != obj.getClass())
                    return false;
                TreeElement other = (TreeElement) obj;
                return this.letter == other.letter;
            }
    
            @Override
            public int compareTo(TreeElement o) {
                return this.letter - o.letter;
            }
        }

        private LinkedList<TreeElement> roots = new LinkedList<>();

        public void add(String name, int position) {
            if (name.length() == 1) {
                TreeElement firstLetter = new TreeElement(name.charAt(0), position);
                roots.add(firstLetter);
                Collections.sort(roots);
            } else {
                TreeElement lastElement = new TreeElement(name.charAt(0), -1);
            }

        }
    }

    

    public static void main(String[] args) {
        CharTree tree = new CharTree();
        
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