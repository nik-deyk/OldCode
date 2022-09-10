import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Map;
import java.util.Stack;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class D {

    static private final Map<Point, Character> shiftsMap = Stream.of(new Object[][] { 
        { new Point(-1, 0), 'D' }, 
        { new Point(1, 0), 'U' }, 
        { new Point(0, -1), 'R' }, 
        { new Point(0, 1), 'L' }, 
    }).collect(Collectors.toMap(data -> (Point) data[0], data -> (Character) data[1]));

    static int N, M;

    private static class Point {
        public int i, j;

        public Point(int i, int j) {
            this.i = i;
            this.j = j;
        }

        public Point shift(Point other) {
            return new Point(this.i + other.i, this.j + other.j);
        }

        public boolean valid() {
            return 0 <= this.i && this.i < N && 0 <= this.j && this.j < M;
        }

        public char charAt() {
            assert this.valid() : "Attempt to access invalid point in maze!";
            return D.maze[i][j];
        }

        public void setChar(char c) {
            assert this.valid() : "Attempt to write to invalid point in maze!";
            D.maze[i][j] = c;
        }
    }

    static char maze[][];

    static void fillWay(Point s) {
        Stack<Point> stack = new Stack<Point>();
        stack.push(s);

        while (stack.size() > 0) {
            Point point = stack.pop();
            // Save point is already labeled. For each existing unlabeled neighbor we label it and push it to stack.
            
            for (Map.Entry<Point, Character> entry : shiftsMap.entrySet()) {
                Point neighbor = point.shift(entry.getKey());
                if (neighbor.valid() && neighbor.charAt() == '.') {
                    neighbor.setChar(entry.getValue());
                    stack.push(neighbor);
                }
            }
        }
    }

    public static void main(String[] args) {

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        try {
            String[] mazeSize = bufferedReader.readLine().split(" ");
            N = Integer.valueOf(mazeSize[0]);
            M = Integer.valueOf(mazeSize[1]);
            maze = new char[N][M];

            Point savePoint = null;
            for (int i = 0; i < N; i++) {
                for (int j = 0; j < M; j++) {
                    char c = (char) bufferedReader.read();
                    maze[i][j] = c;
                    if (c == 'S') {
                        savePoint = new Point(i, j);
                    }
                }
                bufferedReader.read(); // skip \n.
            }

            bufferedReader.close();

            fillWay(savePoint);

            final BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(System.out));
            for (int i = 0; i < N; i++) {
                for (int j = 0; j < M; j++) {
                    writer.write(maze[i][j]);
                }
                writer.write("\n");
            }
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
