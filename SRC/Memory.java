public class Memory {
    public static TileCoordinateMap<Integer> memory = new TileCoordinateMap<>();
    public static void clear() {
        memory.clear();
    }
    public static String debug() {
        return memory.getAll().toString();
    }
}
