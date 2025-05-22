import java.util.ArrayList;
import java.util.Collections;

public class Stack {
    private static final ArrayList<Integer> stack = new ArrayList<>();
    public static int get() {
        if (stack.isEmpty()) {
            return 0;
        }
        return stack.getFirst();
    }
    public static void pop() {
        if (stack.isEmpty()) {
            return;
        }
        stack.removeFirst();
    }
    public static void put(int i) {
        stack.addFirst(i);
    }
    public static void swap() {
        if (stack.size() < 2) {
            return;
        }
        int i1 = stack.getFirst();
        int i2 = stack.get(1);
        stack.set(0, i2);
        stack.set(1, i1);
    }
    public static void duplicate() {
        if (stack.isEmpty()) {
            return;
        }
        stack.addFirst(stack.getFirst());
    }
    public static void flip() {
        Collections.reverse(stack);
    }
    public static void clear() {
        stack.clear();
    }
    public static boolean hasItem() {
        return !stack.isEmpty();
    }
    public static int size() {
        return stack.size();
    }
    public static int getBottom() {
        if (stack.isEmpty()) {
            return 0;
        }
        return stack.getLast();
    }
    public static void placeBottom(int i) {
        stack.add(i);
    }
    public static void popBottom() {
        if (stack.isEmpty()) {
            return;
        }
        stack.removeLast();
    }

    /**
     * USE WITH CAUTION
     * @return THE STACK OBJECT
     */
    public static ArrayList<Integer> getAll() {
        return stack;
    }
}
