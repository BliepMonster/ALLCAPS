import java.util.HashMap;

public class ArrayEvaluator {
    public static int setAddress = 0;
    public static int listAddress = 0;
    public static HashMap<Integer, Integer> lists = new HashMap<>();
    public static void evaluateSet(String line) throws Exception {
        String[] keywords = line.split(" ");
        switch(keywords[0]) {
            case "NEW":
                Stack.put(setAddress);
                setAddress++;
                if (keywords.length != 1) {
                    throw new Exception();
                }
                break;
            case "GET":
                int id = FileEvaluator.evaluateInt(keywords[1])+1000000;
                int index = FileEvaluator.evaluateInt(keywords[2]);
                if (Memory.memory.get(id, index) != null) {
                    Stack.put(Memory.memory.get(id, index));
                } else {
                    Stack.put(0);
                }
                if (keywords.length != 3) {
                    throw new Exception();
                }
                break;
            case "PUT":
                int ID = FileEvaluator.evaluateInt(keywords[1])+1000000;
                int place = FileEvaluator.evaluateInt(keywords[2]);
                int item = FileEvaluator.evaluateInt(keywords[3]);
                Memory.memory.place(ID, place, item);
                if (keywords.length != 4) {
                    throw new Exception();
                }
                break;
        }
    }
    public static void evaluateList(String line) throws Exception {
        String[] keywords = line.split(" ");
        switch (keywords[0]) {
            case "NEW":
                Stack.put(listAddress);
                lists.put(listAddress, 0);
                listAddress++;
                if (keywords.length != 1) {
                    throw new Exception();
                }
                break;
            case "GET":
                int id = FileEvaluator.evaluateInt(keywords[1])+2000000;
                int index = FileEvaluator.evaluateInt(keywords[2]);
                if (Memory.memory.get(id, index) != null) {
                    Stack.put(Memory.memory.get(id, index));
                } else {
                    Stack.put(0);
                }
                if (keywords.length != 3) {
                    throw new Exception();
                }
                break;
            case "ADD":
                int ID = FileEvaluator.evaluateInt(keywords[1])+2000000;
                int item = FileEvaluator.evaluateInt(keywords[2]);
                Memory.memory.place(ID, lists.get(ID-2000000), item);
                lists.put(ID-2000000, lists.get(ID-2000000)+1);
                if (keywords.length != 3) {
                    throw new Exception();
                }
                break;
            case "SIZE":
                int list = FileEvaluator.evaluateInt(keywords[1]);
                Stack.put(lists.get(list));
                if (keywords.length != 2) {
                    throw new Exception();
                }
                break;
            case "CLEAR":
                int toClear = FileEvaluator.evaluateInt(keywords[1]);
                int length = lists.get(toClear);
                if (keywords.length != 2) {
                    throw new Exception();
                }
                for (int i = 0; i < length; i++) {
                    Memory.memory.place(toClear+2000000, i, 0);
                }
                break;
            case "CONTAINS":
                int listID = FileEvaluator.evaluateInt(keywords[1]);
                int contained = FileEvaluator.evaluateInt(keywords[2]);
                if (keywords.length != 3) {
                    throw new Exception();
                }
                for (int i = 0; i < lists.get(listID); i++) {
                    if (Memory.memory.get(listID+2000000, i) == contained) {
                        Stack.put(1);
                        return;
                    }
                }
                Stack.put(0);
                break;
        }
    }
}
