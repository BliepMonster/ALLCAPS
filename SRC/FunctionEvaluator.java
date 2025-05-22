public class FunctionEvaluator {
    public static void evaluate(String function, int returnAddress, FileEvaluator evaluator) {
        String body = evaluator.functions.get(function);
        int offset = evaluator.funcLines.get(function);
        evaluator.line = offset;
        boolean funcRunning = true;
        String[] lines = body.split("\n");
        while (funcRunning) {

            String line = lines[evaluator.line-offset].trim();
            if (line.split(" ")[0].equals("QUIT")) {
                break;
            }
            LineEvaluator.evaluate(line, evaluator);
            funcRunning = evaluator.running;
            if (evaluator.line - offset >= lines.length) {
                break;
            }


            if (evaluator.move) {
                evaluator.line++;
            }

        }
        evaluator.line = returnAddress;
    }
    public static void evaluate(String source) {
        FileEvaluator evaluator1 = new FileEvaluator();
        evaluator1.evaluate(source);
    }
    public static String trimString(String header) {
        return header.startsWith("#") ? header.substring(1) : header;
    }
}
