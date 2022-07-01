import java.util.List;
import java.util.Queue;
import java.util.Arrays;
import java.util.Scanner;

// TODO

public class D {

    private static final class Road implements Comparable<Road> {
        private final int A, B;
        public Road(int cityA, int cityB) {
            if (cityA < cityB) {
                this.A = cityA;
                this.B = cityB;
            } else {
                this.A = cityB;
                this.B = cityA;
            }
        }
        public int A() { return A;}
        public int B() { return B;}
        @Override 
        public int compareTo(Road o) {
            if (this.A != o.A) return this.A - o.A;
            return this.B - o.B;
        }
    }
 
    private static interface RoadGetter {
        public int getRoad(Road route);
    }
    
    private static final class Roads implements RoadGetter {
        private int[][] data;

        public Roads(int citiesNum) {
            data = new int[citiesNum - 1][];
            for (int i = 0; i < data.length; i++) {
                data[i] = new int[i + 1];
                Arrays.fill(data[i], -1);
            }
        }

        // Cities are numerated from 0.
        public void setRoad(Road route, int distance) {
            if (route.A() == route.B()) {
                throw new IllegalArgumentException("Cities must be different!");
            }
            data[route.B() - 1][route.A()] = distance;
        }

        @Override
        public int getRoad(Road route) {
            if (route.A() == route.B()) {
                return 0;
            }
            return data[route.B() - 1][route.A()];
        }
    }

    private static final class RoadsView implements RoadGetter {
        private final Roads r;
        private final Road ignoreRoad;

        public RoadsView(Roads r, Road road) { 
            this.r = r;
            this.ignoreRoad = road;
        }

        @Override
        public int getRoad(Road r) {
            if (r.compareTo(ignoreRoad) == 0) {
                return -1;
            }
            return this.r.getRoad(r);
        }
    }

    private static final class City {
        public long fullDistance;
        public int selectedNeighbor;
    }

    private static final class Cities {
        private City[] data;
        public Cities(int citiesNum) {
            data = new City[citiesNum - 1];
            for (int i = 0; i < data.length; i++) {
                data[i] = new City();
                data[i].fullDistance = -1;
                data[i].selectedNeighbor = -1;
            }
        }

        // Total number of cities.
        public int size() { return data.length + 1; }

        public D.City get(int index) {
            if (index == 0) {
                throw new IllegalArgumentException("Central city does not appear in data!");
            }
            return data[index - 1];
        }

        public City clone() { // TODO
            return null;
        }

        public void calculateFullDistances(RoadGetter r) { // TODO
            /*Queue<Integer> tasks = new ListQueue<>();
            for (int i = 1; i < this.size(); i++) {
                if (this.get(i).state == CalculateState.NONE) {
                    this.calculateFullDistances(i, r);
                }
            }*/
        }
    }

    public static void main(String[] args) {
        int N, M;
        Cities cities;
        Roads roads;
        try (Scanner input = new Scanner(System.in)) {
            N = input.nextInt();
            M = input.nextInt();
            cities = new Cities(N);
            roads = new Roads(N);
            for (int i = 0; i < M; i++) {
                roads.setRoad(new Road(input.nextInt() - 1, input.nextInt() - 1), input.nextInt());
            }
        }
        cities.calculateFullDistances(roads);
        System.out.println("minTime");
    }
}