import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;

public class F {
    private static final int MAX_VALUE = 1000;

    private static enum Side {
        RIGHT, DOWN
    }

    private static abstract class RectangleSize implements Comparable<RectangleSize> {
        protected int a, b;
        
        public int getHor() { return a; }
        
        public int getVert() { return b; }
        
        public int getScale() {return a * b; }

        @Override
        public int compareTo(RectangleSize other) {
            return this.getScale() - other.getScale();
        }
    }

    private static final class Laptop extends RectangleSize{
        /**
         * Create new instance of Laptop.
         * @param a Horizontal value.
         * @param b Vertical value.
         */
        public Laptop(int a, int b) {
            this.a = a;
            this.b = b;
        }
        public Laptop rotate() { return new Laptop(this.b, this.a); }
    }

    private static final class Table extends RectangleSize {
        public Table(Laptop laptop1, Laptop laptop2, Side side) {
            if (side == Side.RIGHT) {
                a = laptop1.getHor() + laptop2.getHor();
                b = Integer.max(laptop1.getVert(), laptop2.getVert());
            } else if (side == Side.DOWN) {
                a = Integer.max(laptop1.getHor(), laptop2.getHor());
                b = laptop1.getVert() + laptop2.getVert();
            }
        }
    }

    public static void main(String[] args) {
        int a1, b1, a2, b2;
        try (var input = new Scanner(System.in)) {
            a1 = input.nextInt();
            b1 = input.nextInt();
            a2 = input.nextInt();
            b2 = input.nextInt();
            for (int i : new int[] { a1, b1, a2, b2 }) {
                assert 0 < i && i <= MAX_VALUE : "Invalid input value: " + i;
            }
        }
        ArrayList<RectangleSize> variants = new ArrayList<>(4);
        Laptop l1 = new Laptop(a1, b1);
        Laptop l2 = new Laptop(a2, b2);
        variants.add(new Table(l1, l2, Side.RIGHT));
        variants.add(new Table(l1, l2, Side.DOWN));
        variants.add(new Table(l1, l2.rotate(), Side.RIGHT));
        variants.add(new Table(l1, l2.rotate(), Side.DOWN));
        RectangleSize max = Collections.min(variants);
        System.out.println(max.getHor() + " " + max.getVert());
    }
}

class E {
    private static final int MAX_VALUE = 1000000;

    private static final class Position {
        public int floor;
        public int entrance;
        public Position(int floor, int entrance) {
            this.floor = floor;
            this.entrance = entrance;
        }
    }

    private static Position getEntranceAndFloor(int target_flat, int max_floors, int flat_per_floor) {
        int target_multiplier = target_flat / flat_per_floor;
        int n1 = target_multiplier % max_floors + 1;
        int p1 = target_multiplier / max_floors + 1;
        return new Position(n1, p1);
    }

    private static void main(String[] args) {
        int k1, M, k2, p2, n2;
        try (var input = new Scanner(System.in)) {
            k1 = input.nextInt();
            M = input.nextInt();
            k2 = input.nextInt();
            p2 = input.nextInt();
            n2 = input.nextInt();
            for (int i : new int[] { k1, M, k2, p2, n2 }) {
                assert 0 < i && i <= MAX_VALUE : "Invalid input value: " + i;
            }
        }
        int flat_number = k2 - 1;
        int multiplier = (p2 - 1) * M + (n2 - 1);
        if (n2 <= M) {
            if (multiplier != 0) {
                if (flat_number < multiplier) {
                    System.out.println("-1 -1");
                } else {
                    int flat_per_floor = flat_number / multiplier;
                    if (flat_per_floor != 0 && flat_number / flat_per_floor == multiplier) {
                        int target_flat = k1 - 1;
                        int flat_per_floor_other = flat_number / (multiplier + 1) + 1;
                        if (flat_per_floor_other == 0 || flat_number / flat_per_floor_other != multiplier) {
                            Position result = getEntranceAndFloor(target_flat, M, flat_per_floor);
                            System.out.println(Integer.toString(result.entrance) + " " + Integer.toString(M == 1 ? 1 : result.floor));
                        } else {
                            Position var1 = getEntranceAndFloor(target_flat, M, flat_per_floor);
                            Position var2 = getEntranceAndFloor(target_flat, M, flat_per_floor_other);
                            System.out.println(Integer.toString(var1.entrance != var2.entrance ? 0 : var1.entrance) + " " + Integer.toString(M == 1 ? 1 : (var1.floor != var2.floor ? 0 : var1.floor)));
                        }
                    } else {
                        System.out.println("-1 -1");
                    }
                }
            } else {
                if (k1 > k2) {
                    System.out.println(Integer.toString(k1 <= M ? 1 : 0) + " " + Integer.toString(M == 1 ? 1 : 0));
                } else {
                    System.out.println(Integer.toString(p2) + " " + Integer.toString(n2));
                }
            }
        } else {
            System.out.println("-1 -1");
        }
    }
}

class D {
    private static void main(String[] args) {
        BigInteger a, b, c;
        try (var input = new Scanner(System.in)) {
            a = new BigInteger(input.next());
            b = new BigInteger(input.next());
            c = new BigInteger(input.next());
        }
        var eq = c.pow(2).subtract(b);
        if (c.compareTo(BigInteger.ZERO) >= 0) {
            if (a.compareTo(BigInteger.ZERO) != 0) {
                BigInteger[] result = eq.divideAndRemainder(a);
                if (result[1].compareTo(BigInteger.ZERO) == 0) {
                    System.out.println(result[0].toString());
                } else {
                    System.out.println("NO SOLUTION");
                }
            } else if (a.compareTo(BigInteger.ZERO) == 0 && eq.compareTo(BigInteger.ZERO) == 0) {
                System.out.println("MANY SOLUTIONS");
            } else {
                System.out.println("NO SOLUTION");
            }
        } else {
            System.out.println("NO SOLUTION");
        }
    }
}

class C {
    private static void main(String[] args) {
        Number origin;
        try (var input = new Scanner(System.in)) {
            origin = new Number(input.next());
            for (int i = 0; i < 3 && input.hasNext(); i++) {
                var toCompare = new Number(input.next());
                System.out.println(origin.compareTo(toCompare) == 0 ? "YES" : "NO");
            }
        }
    }

    static class Number implements Comparable<Number> {
        private String code;
        private String number;

        public Number(String from) {
            from = from.strip().replaceAll("[-\\(\\)\\+]", "");
            if (from.length() > 10 && (from.startsWith("7") || from.startsWith("8"))) {
                from = from.substring(1);
            }
            switch (from.length()) {
                case 10:
                    code = from.substring(0, 3);
                    number = from.substring(3);
                    break;
                case 7:
                    code = "495";
                    number = from;
                    break;
                default:
                    throw new IllegalArgumentException("Invalid number string passed!");
            }
            assert code.length() == 3;
            assert number.length() == 7;
        }

        @Override
        public int compareTo(Number o) {
            var diff = this.code.compareTo(o.code);
            if (diff == 0)
                return this.number.compareTo(o.number);
            return diff;
        }
    }
}

class B {
    private static void main(String[] args) {
        BigInteger x, y, z;
        try (var input = new Scanner(System.in)) {
            x = new BigInteger(input.next());
            y = new BigInteger(input.next());
            z = new BigInteger(input.next());
        }
        BigInteger[][] array = { { x, y, z }, { y, z, x }, { z, x, y } };
        if (Arrays.stream(array).allMatch((BigInteger[] sides) -> sides[0].compareTo(sides[1].add(sides[2])) < 0)) {
            System.out.println("YES");
        } else {
            System.out.println("NO");
        }
    }
}

class A {
    private static void main(String[] args) {
        int troom, tcond;
        String state;
        try (var input = new Scanner(System.in)) {
            troom = input.nextInt();
            tcond = input.nextInt();
            state = input.next().strip();
        }
        assert -50 <= troom && troom <= 50 && -50 <= tcond && tcond <= 50;
        switch (state) {
            case "freeze":
                System.out.println(tcond < troom ? tcond : troom);
                break;
            case "heat":
                System.out.println(troom < tcond ? tcond : troom);
                break;
            case "auto":
                System.out.println(tcond);
                break;
            default:
                System.out.println(troom);
                break;
        }
    }
}