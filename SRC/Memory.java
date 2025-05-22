import java.util.HashMap;

public class Memory {
    public static TileCoordinateMap<Integer> memory = new TileCoordinateMap<>();
    public static HashMap<String, Integer> associated = new HashMap<>();
    public static void clear() {
        memory.clear();
        associated.clear();
    }
    public static String debug() {
        return memory.getAll().toString() + "\n" + associated.toString();
    }
}
