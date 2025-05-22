import java.io.BufferedReader;
import java.util.HashMap;

public class FileEvaluator {
    public int line = 0; //INTENDED TO BE CHANGED INSIDE LINES
    public boolean running = true;
    public boolean move = true;
    public boolean lastConditional = true;
    public HashMap<String, Integer> labels = new HashMap<>();
    public HashMap<Integer, BufferedReader> readers = new HashMap<>();
    public HashMap<String, String> functions = new HashMap<>();
    public HashMap<String, Integer> funcLines = new HashMap<>();
    public HashMap<Integer, String> linked = new HashMap<>();
    public Skimmer skimmer = new Skimmer();
    public String root;
    public void evaluate(String file) {
        skimmer.peek(file, this);
        if (checkString(file)) {
            throw new RuntimeException("File contains lowercase letters.");
        }
        String[] lines = file.split("\n");
        while (running) {
            try {
                if (!skimmer.skipped.contains(line)) {
                    LineEvaluator.evaluate(lines[line].trim(), this);
                }
                line += move ? 1 : 0;
                move = true;
            } catch (RuntimeException e) {
                System.out.println("Exception on line " + (line+1));
                e.printStackTrace();
                return;
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
