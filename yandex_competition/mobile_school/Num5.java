import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class Num5 {

    public static void main(String[] args) {
        Scanner kb = new Scanner(System.in);
        String search = kb.next();
        String target = kb.next();
        kb.close();
        Map<Character, Integer> searchMultiSet = countChars(search.substring(0, target.length()));
        final Map<Character, Integer> targetMultiSet = countChars(target);
        int search_start = 0;
        int search_end = target.length();
        while (search_end <= search.length()) {
            if (smallDifference(searchMultiSet, targetMultiSet)) {
                System.out.println(search_start);
                return;
            }
            if (search_end < search.length())
                addToMultiset(search.charAt(search_end), searchMultiSet);
            removeFromMultiset(search.charAt(search_start), searchMultiSet);
            search_end++;
            search_start++;
        }
        System.out.println(-1);
    }

    public static void addToMultiset(Character c, Map<Character, Integer> multiset) {
        multiset.merge(c, 1, Integer::sum);
    }

    public static void removeFromMultiset(Character c, Map<Character, Integer> multiset) {
        multiset.merge(c, 1, (oldValue, value) -> oldValue - value == 0 ? null : oldValue - value);
    }

    public static Map<Character, Integer> countChars(String s) {
        Map<Character, Integer> map = new HashMap<Character, Integer>();
        for (int i = 0; i < s.length(); i++) {
            addToMultiset(s.charAt(i), map);
        }
        return map;
    }

    public static boolean smallDifference(Map<Character, Integer> first, Map<Character, Integer> second) {
        Set<Character> common = new HashSet<Character>(first.keySet());
        int first_difference = 0;
        int second_difference = 0;
        if (common.retainAll(second.keySet())) 
            first_difference += countOut(first, common);
        for (Character key: common) {
            int difference = first.get(key) - second.get(key);
            if (difference > 0) {
                first_difference += difference;
            } else {
                second_difference += -difference;
            }
        }
        second_difference += countOut(second, common);
        return (first_difference == 1 && second_difference == 1);
    }

    public static int countOut(Map<Character, Integer> first, Set<Character> common) {
        Set<Character> super_set = new HashSet<Character>(first.keySet());
        super_set.removeAll(common);
        return super_set.stream().<Integer>map(first::get).reduce(0, (a, b) -> a + b);
    }
}