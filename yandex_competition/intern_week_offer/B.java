import java.util.LinkedList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class B {
    private static class Node {
        public final int count;
        public final boolean isBlack;

        public Node(int count, boolean isBlack) {
            this.count = count;
            this.isBlack = isBlack;
        }
    }
    
    private static boolean isBlack(int element) {
        return element % 2 == 0;
    }

    private static class NodeList {
        private final List<Node> nodes = new LinkedList<Node>();
        private final int blackDotsCount;
        private final int moveCount;
        private final int elementsCount;

        private static final HashMap<RecursiveArguments, Integer> cache = new HashMap<>();

        public NodeList(int elementsCount, final int moveCount, final Scanner scanner) {
            this.elementsCount = elementsCount;
            this.moveCount = moveCount;
            this.blackDotsCount = readNodes(scanner);
        }

        private int readNodes(final Scanner scanner) {
            int blackDots = 0;
            // List contains at least one element:
            int lastElement = scanner.nextInt();
            boolean isCurrentNodeBlack = isBlack(lastElement);
            int currentNodeCount = 1;
            if (isCurrentNodeBlack) {
                blackDots++;
            }
            for (int i = 1; i < elementsCount; i++) {
                lastElement = scanner.nextInt();
                boolean isNewElementBlack = isBlack(lastElement);
                if (isNewElementBlack == isCurrentNodeBlack) {
                    // Add to current node.
                    currentNodeCount++;
                } else {
                    // Dump current node.
                    nodes.add(new Node(currentNodeCount, isCurrentNodeBlack));
                    // Start reading new node.
                    isCurrentNodeBlack = isNewElementBlack;
                    currentNodeCount = 1;
                }

                if (isNewElementBlack) {
                    blackDots++;
                }
            }
            // Add last node.
            nodes.add(new Node(currentNodeCount, isCurrentNodeBlack));
            // Returns the number of elements that are black.
            return blackDots;
        }

        private int getNodeCount(int index) {
            return (0 <= index && index < nodes.size() ? nodes.get(index).count : 0);
        }

        public int getMaxBeautified() { 
            if (blackDotsCount == 0) {
                return 0;
            }
            if (blackDotsCount == elementsCount) {
                return elementsCount;
            }

            int maxBeauty = 1;
            for (int i = 0; i < nodes.size(); i++) {
                int currentBeauty = getBeautified(i);
                if (currentBeauty > maxBeauty) {
                    maxBeauty = currentBeauty;
                }
                if (maxBeauty >= blackDotsCount) { 
                    return blackDotsCount;
                }
            }
            return maxBeauty;
        }

        private int getBeautified(final int index) { 
            if (nodes.get(index).isBlack) {
                return 0; // Allow only for non-black elements.
            }
            // Nearby nodes are always black.
            if (nodes.get(index).count == moveCount) {
                return getNodeCount(index - 1) + nodes.get(index).count + getNodeCount(index + 1);
            }
            if (nodes.get(index).count > moveCount) {
                if (getNodeCount(index - 1) > getNodeCount(index + 1)) {
                    return getNodeCount(index - 1) + moveCount;
                }
                return moveCount + getNodeCount(index + 1);
            }
            int accessibleMoves = moveCount - nodes.get(index).count;
            int leftNodeIndex = index - 2;
            int rightNodeIndex = index + 2;

            return getSideNodesSum(leftNodeIndex, rightNodeIndex, getNodeCount(index - 1) + nodes.get(index).count + getNodeCount(index + 1), accessibleMoves);
        }

        private int getSideNodesSum(final int leftIndex, final int rightIndex, final int currentSum, int accessibleMoves) {
            if ((leftIndex < 0 && rightIndex >= nodes.size()) || accessibleMoves == 0) {
                return currentSum;
            }

            RecursiveArguments r = new RecursiveArguments(leftIndex, rightIndex, currentSum, accessibleMoves);
            Integer i = cache.get(r);
            if (i != null) {
                return i.intValue();
            }

            final int result;
            if ((leftIndex >= 0 && rightIndex < nodes.size()) && (accessibleMoves < getNodeCount(leftIndex) && accessibleMoves < getNodeCount(rightIndex))) {
                result = currentSum + accessibleMoves;
            } else if ((leftIndex >= 0 && rightIndex < nodes.size()) && (accessibleMoves >= getNodeCount(leftIndex) + getNodeCount(rightIndex))) {
                accessibleMoves -= getNodeCount(leftIndex) + getNodeCount(rightIndex);
                int newSum = getNodeCount(leftIndex - 1) + getNodeCount(leftIndex) + currentSum + getNodeCount(rightIndex) + getNodeCount(rightIndex + 1);
                result = getSideNodesSum(leftIndex - 2, rightIndex + 2, newSum, accessibleMoves);
            } else if ((leftIndex >= 0 && rightIndex >= nodes.size()) || 
                    (accessibleMoves >= getNodeCount(leftIndex) && accessibleMoves < getNodeCount(rightIndex))) {
                accessibleMoves -= getNodeCount(leftIndex);
                result = getSideNodesSum(leftIndex - 2, rightIndex, getNodeCount(leftIndex - 1) + getNodeCount(leftIndex) + currentSum, accessibleMoves);
            } else if ((leftIndex < 0 && rightIndex < nodes.size()) || 
                    (accessibleMoves < getNodeCount(leftIndex) && accessibleMoves >= getNodeCount(rightIndex))) {
                accessibleMoves -= getNodeCount(rightIndex);
                result = getSideNodesSum(leftIndex, rightIndex + 2, currentSum + getNodeCount(rightIndex) + getNodeCount(rightIndex + 1), accessibleMoves);
            } else if (accessibleMoves >= getNodeCount(leftIndex) && accessibleMoves >= getNodeCount(rightIndex)) {
                // Place data for the left:
                int leftSum = getSideNodesSum(leftIndex - 2, rightIndex, getNodeCount(leftIndex - 1) + getNodeCount(leftIndex) + currentSum, accessibleMoves - getNodeCount(leftIndex));
                int rightSum = getSideNodesSum(leftIndex, rightIndex + 2, currentSum + getNodeCount(rightIndex) + getNodeCount(rightIndex + 1), accessibleMoves - getNodeCount(rightIndex));
                result = Integer.max(leftSum, rightSum);
            } else {
                result = 0;
            }

            cache.put(r, Integer.valueOf(result));

            return 0;
        }

        private static class RecursiveArguments {
            public final int leftIndex;
            public final int rightIndex;
            public final int currentSum;
            public final int accessibleMoves;

            public RecursiveArguments(final int leftIndex, final int rightIndex, final int currentSum, final int accessibleMoves) {
                this.leftIndex = leftIndex;
                this.rightIndex = rightIndex;
                this.currentSum = currentSum;
                this.accessibleMoves = accessibleMoves;
            }
        }
    }

    public static void main(String[] args) {
        try (Scanner myInput = new Scanner(System.in)) {
            int arraySize = myInput.nextInt();
            int moveCount = myInput.nextInt();
            NodeList n = new NodeList(arraySize, moveCount, myInput);

            System.out.println(n.getMaxBeautified());
        }
    }
}
