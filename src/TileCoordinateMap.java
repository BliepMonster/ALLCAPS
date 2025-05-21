import java.util.*;

public class TileCoordinateMap<T> {
    protected Map<String, T> map = new HashMap<>();
    protected String encode(int x, int y) {
        return "X:"+x+" Y:"+y;
    }
    public void place(int x, int y, T tile) {
        map.put(encode(x,y), tile);
    }
    public T get(int x, int y) {
        return map.get(encode(x,y));
    }
    public Collection<T> getAll() {
        return map.values();
    }
    public void clear() {
        map.clear();
    }
}
