import java.io.BufferedReader;
import java.util.HashMap;

public class FileEvaluator {
    public static int line = 0; //INTENDED TO BE CHANGED INSIDE LINES
    public static boolean running = true;
    public static boolean move = true;
    public static boolean lastConditional = true;
    public static HashMap<String, Integer> labels = new HashMap<>();
    public static HashMap<Integer, BufferedReader> readers = new HashMap<>();
    public static void evaluate(String file) {
        Skimmer.peek(file);
        if (checkString(file)) {
            throw new RuntimeException("File contains lowercase letters.");
        }
        String[] lines = file.split("\n");
        while (running) {
            try {
                LineEvaluator.evaluate(lines[line]);
                line += move ? 1 : 0;
                move = true;
            } catch (RuntimeException e) {
                System.out.println("Exception on line " + (line+1));
                e.printStackTrace();
            }
        }
    }
    public static int evaluateInt(String s) {
        int val;
        if (s.equals("STACK")) {
            val = Stack.get();
            Stack.pop();
        } else {
            val = Integer.parseInt(s);
        }
        return val;
    }
    private static boolean checkString(String str) {
        char ch;
        for(int i=0;i < str.length();i++) {
            ch = str.charAt(i);
            if (Character.isLowerCase(ch)) {
                return true;
            }
        }
        return false;
    }
}
