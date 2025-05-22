import java.util.HashSet;
import java.util.Set;

public class Skimmer {
    public Set<Integer> skipped = new HashSet<>();
    public void peek(String file, FileEvaluator evaluator) {
        String[] lines = file.split("\n");
        boolean function = false;
        String funcName = "";
        StringBuilder funcBody = new StringBuilder();
        for (int lineNumber = 0; lineNumber < lines.length; lineNumber++) {
            String line = lines[lineNumber];
            String[] keywords = line.split(" ");
            if (keywords[0].equals("LABEL")) {
                evaluator.labels.put(keywords[1], lineNumber+1);
            }
            else if (keywords[0].equals("FUNCTION")) {
                function = true;
                funcName = keywords[1];
                evaluator.funcLines.put(funcName, lineNumber);
                skipped.add(lineNumber);
            }
            else if (keywords[0].equals("QUIT")) {
                function = false;
                funcBody.append(line).append("\n");
                evaluator.functions.put(funcName, funcBody.toString());
                funcName = "";
                funcBody = new StringBuilder();
                skipped.add(lineNumber);
            } else if (function) {
                funcBody.append(line).append("\n");
                skipped.add(lineNumber);
            }
        }
    }
}
