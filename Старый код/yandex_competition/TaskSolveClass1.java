
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import java.util.HashMap;
import java.util.function.Consumer;

final class Element {
    private int value;
    private int prefixLength;
    private ArrayList<Element> children;

    public Element(int value) {
        assert value > 0 : "Value must be positive!";
        this.value = value;
        this.prefixLength = 0;
        this.children = null;
    }

    public Element(int value, int prefix) {
        this(value);
        this.setPrefix(prefix);
    }

    public int getValue() {
        return this.value;
    }

    public int getPrefix() {
        return this.prefixLength;
    }

    public Element setPrefix(int new_value) {
        assert new_value >= 0 : "Prefix must be non-negative!";
        this.prefixLength = new_value;
        return this;
    }

    public void initChildren(int length) {
        assert this.children == null : "Children already initialized!";
        assert length > 0 : "Length must be positive!";
        this.children = new ArrayList<Element>(length);
        for (int i = 0; i < length; i++) {
            this.children.add(new Element(i + 1));
        }
    }

    /**
     * Return element of inner array.
     * @param value The value of needed element.
     * @return Such element that getValue() returns passed value.
     */
    public Element getNext(int value) {
        assert this.children != null : "Inner array was not created!";
        assert 0 < value && value <= this.children.size() : "Value index is out of range!";
        var e = this.children.get(value - 1);
        assert e != null && e.getValue() == value : "Inner array was broken!";
        return e;
    }

    public String toString() {
        if (this.children == null || this.children.size() == 0) {
            return String.format("Elem[val=%d, prefix=%d]", this.getValue(), this.getPrefix());
        }
        return  String.format("Elem[val=%d, prefix=%d, children_num=%d]", this.getValue(),
                              this.getPrefix(), this.children.size());
    }
}

final class TailId implements Iterable<Integer> {
    private int[] tailId;
    
    private TailId() {
        this.tailId = new int[0];
    }

    private TailId(int[] tailId) {
        assert tailId != null : "Passed tail id must be non-null!";
        this.tailId = tailId;
    }
    
    public class IdIterator implements Iterator<Integer> {
        private int currentPosition;
        
        public IdIterator() {
            this.prepare();
        }
        
        public void prepare() {
            this.currentPosition = 0;
        }
    
        public boolean hasNext() {
            return this.currentPosition < tailId.length;
        }
    
        public Integer next() {
            assert hasNext() : "Current position is out of bounds!";
            return tailId[this.currentPosition++];
        }
    }

    public int length() {
        return this.tailId.length;
    }

    public static TailId createEmptyTail() {
        return new TailId();
    }

    public TailId prolongTail(int next) {
        int[] newTail = new int[this.tailId.length + 1];
        System.arraycopy(this.tailId, 0, newTail, 0, this.tailId.length);
        newTail[newTail.length - 1] = next;
        return new TailId(newTail);
    }

    public String toString() {
        return "Tail[id=" +Arrays.toString(this.tailId) + "]";
    }

    @Override
    public Iterator<Integer> iterator() {
        return new IdIterator();
    }
}

final class ElementsSequence extends AbstractList<Integer> {
    private Element[] elements;
    private int lastIndex;

    public ElementsSequence(int capacity) {
        assert capacity > 0 : "Capacity must be positive!";
        this.elements = new Element[capacity];
        this.lastIndex = -1;
    }

    /**
     * Return elements by index.
     * @param index The index of element.
     * @param tailId The tail id user to search for element in the tree.
     * @return Element object on passed index followed by passed tail.
     */
    private Element getElement(int index, TailId tailId) {
        assert 0 <= index : "Index must be non-negative!";
        if (index < this.elements.length) {
            assert index <= this.lastIndex : "Sequence not filled out!";
            return this.elements[index];
        }
        assert tailId != null : "Tail id must be initialized when accessing elements in the tail!";
        assert index - this.lastIndex <= tailId.length() : "Passed tail id is too short for required index!";
        var current = this.elements[this.lastIndex];
        var iter = tailId.iterator();
        for (index -= this.lastIndex; index > 0; index--) {
            current = current.getNext(iter.next());
        }
        return current;
    }

    /**
     * Get last element in sequence.
     * @param tailId Tail id to follow to.
     * @param handler Callback for each new element iteration.
     * @return The last element.
     */
    private Element getLastElementInTail(TailId tailId, Consumer<Element> handler) {
        var current = this.elements[this.lastIndex];
        for (var k: tailId) {
            current = current.getNext(k);
            if (handler != null)
                handler.accept(current);
        }
        return current;
    }

    private class WithTail extends AbstractList<Element> {
        private TailId tailId;
        private int offset;
        private Map<Integer, Element> cache;

        WithTail(TailId tail) {
            assert tail != null;
            this.tailId = tail;
            this.cache = new HashMap<Integer, Element>();
            this.offset = 0;
        }

        public WithTail shift(int offset) {
            assert offset >= 0;
            var other = new WithTail(this.tailId);
            other.offset = offset;
            other.cache = cache;
            return other;
        }

        @Override
        public Element get(int index) {
            index += offset;
            var e =  cache.get(index);
            if (e == null) {
                e = getElement(index, tailId);
                cache.put(index, e);
            }
            return e;
        }

        @Override
        public int size() {
            return ElementsSequence.this.size() - offset;
        }
    }

    /**
     * Calculate new value for prefix array.
     * @param currentIndex The index of element that going to be inserted.
     * @param currentValue The value of element that going to be inserted.
     * @param tailId The tail id to use when access index is bigger than array length.
     * @return The value for prefix array.
     */
    private int prefix_search(int currentIndex, int currentValue, TailId tailId) {
        var s = new WithTail(tailId);
        int j = s.get(currentIndex - 1).getPrefix();
        while (j > 0 && currentValue != s.get(j).getValue())
            j = s.get(j - 1).getPrefix();
        if (currentValue == s.get(j).getValue())
            j++;
        return j;
    }

    @Override
    public Integer get(int index) {
        return this.getElement(index, null).getValue();
    }

    /**
     * @return Size without counting the tail elements.
     */
    @Override
    public int size() {
        return this.lastIndex + 1;
    }

    @Override
    public boolean add(Integer value) {
        this.lastIndex++;
        assert this.lastIndex < this.elements.length : "Can not add more elements!";
        if (this.lastIndex <= 0) {
            this.lastIndex = 0;
            this.elements[0] = new Element(value, 0);
        } else {
            this.elements[this.lastIndex] = new Element(value,
                                                        this.prefix_search(this.lastIndex, value, null));
        }
        return true;
    }

    public TailId[] addVariants(TailId tailId, int number) {
        assert tailId != null : "Tail id must be non-null in addVariants function call!";
        var newIndex = this.lastIndex + 1 + tailId.length();
        var e = this.getLastElementInTail(tailId, null);
        e.initChildren(number);
        TailId[] tails = new TailId[number];
        for (int i = 0; i < number; i++) {
            var value = i + 1;
            tails[i] = tailId.prolongTail(value);
            e.getNext(value).setPrefix(this.prefix_search(newIndex, value, tailId));
        }
        return tails;
    }

    public ArrayList<Element> dumpSequence(TailId tailId) {
        assert tailId != null : "Tail id must be non-null!";
        var result = new ArrayList<Element>(this.size() + tailId.length());
        result.addAll(Arrays.asList(this.elements));
        this.getLastElementInTail(tailId, (element) -> result.add(element));
        return result;
    }

    /**
     * Perform Knuth–Morris–Pratt algorithm.
     * @param origin Original sequence.
     * @param pattern Pattern to find.
     * @return The first occurrence of the pattern in second parameter.
     */
    static private int firstOccurrence(AbstractList<Element> wt, AbstractList<Element> pattern) {
        
    }
}

public final class TaskSolveClass1 {
    public static void main(String[] args) {
        var e = new ElementsSequence(4);
        e.add(1);
        e.add(2);
        e.add(3);
        e.add(2);
        for (TailId i: e.addVariants(TailId.createEmptyTail(), 3)) {
            for (var el : e.dumpSequence(i)) {
                System.out.print(el.getValue() + ", ");
            }
            System.out.print("prefix: ");
            for (var el : e.dumpSequence(i)) {
                System.out.print(el.getPrefix() + ", ");
            }
            System.out.println();
        }
    }
}