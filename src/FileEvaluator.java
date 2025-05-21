public class FileEvaluator {
    public static int line = 0; //INTENDED TO BE CHANGED INSIDE LINES
    public static boolean running = true;
    public static boolean move = true;
    public static boolean lastConditional = true;
    public static void evaluate(String file) {
        String[] lines = file.split("\n");
        while (running) {
            LineEvaluator.evaluate(lines[line]);
            line += move ? 1 : 0;
            move = true;
        }
    }
}
