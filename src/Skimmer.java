public class Skimmer {
    public static void peek(String file) {
        String[] lines = file.split("\n");
        for (int lineNumber = 0; lineNumber < lines.length; lineNumber++) {
            String line = lines[lineNumber];
            String[] keywords = line.split(" ");
            if (keywords[0].equals("LABEL")) {
                FileEvaluator.labels.put(keywords[1], lineNumber+1);
            }
        }
    }
}
