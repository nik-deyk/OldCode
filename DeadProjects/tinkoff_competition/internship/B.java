import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Map.Entry;

public class B {
    public static void main(String[] args) {
        HashMap<String, Short> teamsAndResults = new HashMap<>();

        try (Scanner in = new Scanner(System.in)) {
            int N = in.nextInt();

            for (int i = 0; i < N; i++) {
                String[] names = new String[]{
                    in.next(), in.next(), in.next()
                };
                Arrays.sort(names);
                teamsAndResults.merge(String. join("", names), Short.valueOf((short)1), (Short a, Short b) -> (short)(a + b));
            }
        }
        Entry<String, Short> maxEntry = Collections.max(teamsAndResults.entrySet(), (Entry<String, Short> e1, Entry<String, Short> e2) -> e1.getValue()
        .compareTo(e2.getValue()));
        
        System.out.println(maxEntry.getValue());
    }
}
