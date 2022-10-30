import java.util.Scanner;

public class A {
    private static class Sequence {
        private byte[] sequence;

        public Sequence(byte[] sequence) {
            this.sequence = sequence;
        }

        public Sequence(final byte size, final Scanner console) {
            this.sequence = new byte[size];
            for (int i = 0; i < size; i++) {
                this.sequence[i] = console.nextByte();
            }
        }

        public Sequence getMinimalIntersection(final Sequence other) {
            if (this.sequence.length < other.sequence.length) {
                return other.getMinimalIntersection(this);
            }
            // Try out the best cases:
            int bestCases = this.sequence.length - other.sequence.length + 1;
            for (int i = 0; i < bestCases; i++) {
                boolean goodCase = true;
                for (int j = 0; j < other.sequence.length; j++) {
                    if (this.sequence[i + j] != other.sequence[j]) {
                        goodCase = false;
                        break;
                    }
                }
                if (goodCase) {
                    // other sequence is in this one.
                    return this;
                }
            }
            // Try out bad cases on the right:
            int rightCases = other.sequence.length - 1;
            for (int i = this.sequence.length - rightCases; i < this.sequence.length; i++) {
                boolean goodCase = true;
                for (int j = 0; j < rightCases; j++) {
                    if (this.sequence[i + j] != other.sequence[j]) {
                        goodCase = false;
                        break;
                    }
                }
                if (goodCase) {
                    byte[] result = new byte[this.sequence.length + other.sequence.length - rightCases];
                    System.arraycopy(this.sequence, 0, result, 0, this.sequence.length);
                    System.arraycopy(other.sequence, rightCases, result, this.sequence.length, other.sequence.length - rightCases);
                    return new Sequence(result);
                }
                // Check on the left:
                goodCase = true;
                for (int j = other.sequence.length - rightCases, k = 0; j < other.sequence.length; j++, k++) {
                    if (this.sequence[k] != other.sequence[j]) {
                        goodCase = false;
                        break;
                    }
                }
                if (goodCase) {
                    byte[] result = new byte[this.sequence.length + other.sequence.length - rightCases];
                    System.arraycopy(other.sequence, 0, result, 0, other.sequence.length);
                    System.arraycopy(this.sequence, rightCases, result, other.sequence.length, this.sequence.length - rightCases);
                    return new Sequence(result);
                }
                rightCases--;
            }

            // No any good solution:
            byte[] result = new byte[this.sequence.length + other.sequence.length];
            System.arraycopy(other.sequence, 0, result, 0, other.sequence.length);
            System.arraycopy(this.sequence, 0, result, other.sequence.length, this.sequence.length);
            return new Sequence(result);
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < this.sequence.length; i++) {
                sb.append(this.sequence[i]);
                if (i < this.sequence.length - 1)
                    sb.append(' ');
            }
            return sb.toString();
        }

        public int length() {
            return this.sequence.length;
        }
    }

    public static void main(String[] args) {
        Sequence output;
        try (Scanner myInput = new Scanner(System.in)) {
            Sequence a = new Sequence(myInput.nextByte(), myInput);
            Sequence b = new Sequence(myInput.nextByte(), myInput);
            Sequence c = new Sequence(myInput.nextByte(), myInput);

            Sequence check_a_b = a.getMinimalIntersection(b).getMinimalIntersection(c);
            Sequence check_a_c = a.getMinimalIntersection(c).getMinimalIntersection(b);
            if (check_a_b.length() < check_a_c.length()) {
                output = check_a_b;
            } else {
                output = check_a_c;
            }
        }

        System.out.println(output.length());
        System.out.println(output.toString());
    }
}