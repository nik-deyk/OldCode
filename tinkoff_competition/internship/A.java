import java.util.Scanner;

public class A {
    static class Point {
        public int x, y;

        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
    
    static class Rectangle {
        public Point leftDown, rightTop;

        public Rectangle(int leftDown_x, int leftDown_y, int rightTop_x, int rightTop_y) {
            this.leftDown = new Point(leftDown_x, leftDown_y);
            this.rightTop = new Point(rightTop_x, rightTop_y);
        }
    }

    public static void main(String[] args) {
        Rectangle first, second;

        try (Scanner in = new Scanner(System.in)) {
            first = new Rectangle(in.nextInt(), in.nextInt(), in.nextInt(), in.nextInt());
            second = new Rectangle(in.nextInt(), in.nextInt(), in.nextInt(), in.nextInt());
        }

        int resultWidth = Integer.max(first.rightTop.x, second.rightTop.x) - Integer.min(first.leftDown.x, second.leftDown.x);
        int resultHeight = Integer.max(first.rightTop.y, second.rightTop.y) - Integer.min(first.leftDown.y, second.leftDown.y);

        System.out.println((int)Math.pow(Integer.max(resultWidth, resultHeight), 2));
    }
}