import java.util.Random;
import java.util.Scanner;

public class LineEvaluator {
    public static Scanner scanner = new Scanner(System.in);
    public static Random random = new Random();
    public static void evaluate(String line) {
        String[] keywords = line.split(" ");
        switch (keywords[0]) {
            case "ALLOC":
                int xAlloc = Integer.parseInt(keywords[1]);
                int yAlloc = Integer.parseInt(keywords[2]);
                Memory.memory[xAlloc][yAlloc] = Stack.get();
                Stack.pop();
                break;
            case "RETRIEVE":
                int xRetrieve = Integer.parseInt(keywords[1]);
                int yRetrieve = Integer.parseInt(keywords[2]);
                Stack.put(Memory.memory[xRetrieve][yRetrieve]);
                break;
            case "PRINT":
                if (keywords[1].equals("CHAR") && keywords[2].equals("ALL")) {
                    while (Stack.hasItem()) {
                        System.out.print((char) Stack.get());
                        Stack.pop();
                    }
                } else if (keywords[1].equals("CHAR")) {
                    int count = Integer.parseInt(keywords[2]);
                    for (int i = 0; i < count; i++) {
                        System.out.print((char) Stack.get());
                        Stack.pop();
                    }
                } else if (keywords[1].equals("INT") && keywords[2].equals("ALL")) {
                    while (Stack.hasItem()) {
                        System.out.print(Stack.get());
                        Stack.pop();
                    }
                } else if (keywords[1].equals("INT")) {
                    int count = Integer.parseInt(keywords[2]);
                    for (int i = 0; i < count; i++) {
                        System.out.print(Stack.get());
                        Stack.pop();
                    }
                }
                break;
            case "PUSH":
                switch (keywords[1]) {
                    case "INT" -> Stack.put(Integer.parseInt(keywords[2]));
                    case "CHAR" -> Stack.put(keywords[2].toCharArray()[0]);
                    case "STRING" -> {
                        StringBuilder builder = new StringBuilder();
                        for (int i = 2; i < keywords.length; i++) {
                            builder.append(keywords[i]);
                            builder.append(" ");
                        }
                        String string = builder.toString().trim();
                        char[] pushArray = string.toCharArray();
                        for (int i = 0; i < pushArray.length; i++) {
                            Stack.put(pushArray[pushArray.length - 1 - i]);
                        }
                    }
                }
                break;
            case "POP":
                Stack.pop();
                break;
            case "DUPLICATE":
                Stack.duplicate();
                break;
            case "SWAP":
                Stack.swap();
                break;
            case "ADD":
                int i1 = Stack.get();
                Stack.pop();
                int i2 = Stack.get();
                Stack.pop();
                Stack.put(i1+i2);
                break;
            case "INPUT":
                if (keywords[1].equals("INT")) {
                    Stack.put(scanner.nextInt());
                } else if (keywords[1].equals("CHAR")) {
                    Stack.put(scanner.next().charAt(0));
                }
                break;
            case "WAIT":
                try {
                    Thread.sleep(Integer.parseInt(keywords[1]));
                } catch (InterruptedException e) {
                    System.out.println("[ERROR]: INTERRUPTED");
                    evaluate("END");
                }
                break;
            case "RANDOM":
                Stack.put(random.nextInt(256));
                break;
            case "SUBTRACT":
                int i1s = Stack.get(); // switch needs different names for variables
                Stack.pop();
                int i2s = Stack.get();
                Stack.pop();
                Stack.put(i1s - i2s);
                break;
            case "CLEAR":
                switch (keywords[1]) {
                    case "STACK" -> Stack.clear();
                    case "MEMORY" -> Memory.clear();
                    case "CELL" -> {
                        int xClear = Integer.parseInt(keywords[2]);
                        int yClear = Integer.parseInt(keywords[3]);
                        Memory.memory[xClear][yClear] = 0;
                    }
                }
                break;
            case "GOTO":
                if (keywords[1].equals("STACK")) {
                    FileEvaluator.line = Stack.get()-1;
                    FileEvaluator.move = false;
                    Stack.pop();
                    break;
                }
                int lineToGoTo = Integer.parseInt(keywords[1]);
                FileEvaluator.line = lineToGoTo-1;
                FileEvaluator.move = false;
                break;
            case "MULTIPLY":
                int m1 = Stack.get();
                Stack.pop();
                int m2 = Stack.get();
                Stack.pop();
                Stack.put(m1*m2);
                break;
            case "IF":
                if (keywords[1].equals("EXPRESSION")) {
                    StringBuilder conjoined = new StringBuilder();
                    for (int i = 2; i < keywords.length; i++) {
                        conjoined.append(keywords[i]);
                    }
                    String expression = conjoined.toString();
                    String[] ifThen = expression.split(":");
                    int right = Stack.get();
                    Stack.pop();
                    int left = Stack.get();
                    Stack.pop();

                    boolean execute = switch (ifThen[0]) {
                        case "==" -> left == right;
                        case ">" -> left > right;
                        case "<" -> left < right;
                        case "<=" -> left <= right;
                        case ">=" -> left >= right;
                        default -> false;
                    };
                    FileEvaluator.lastConditional = execute;
                    if (!execute) {
                        break;
                    }
                    evaluate(line.split(":")[1]);
                } else if (keywords[1].equals("STACK")) {
                    StringBuilder exprBuilder = new StringBuilder();
                    for (int i = 2; i < keywords.length; i++) {
                        exprBuilder.append(keywords[i]);
                    }
                    String expr = exprBuilder.toString();
                    int lastStack = Stack.get();
                    Stack.pop();
                    boolean eval = lastStack != 0;
                    FileEvaluator.lastConditional = eval;
                    if (eval) {
                        evaluate(expr);
                    }
                }
                break;
            case "DIVIDE":
                int d1 = Stack.get();
                Stack.pop();
                int d2 = Stack.get();
                Stack.pop();
                Stack.put(d1/d2);
                break;
            case "ELSE":
                StringBuilder builder = new StringBuilder();
                for (int i = 1; i < keywords.length; i++) {
                    builder.append(keywords[i]);
                    builder.append(" ");
                }
                if (!FileEvaluator.lastConditional) {
                    evaluate(builder.toString());
                } FileEvaluator.lastConditional = !FileEvaluator.lastConditional;
                break;
            case "DEBUG":
                if (keywords[1].equals("STACK")) {
                    System.out.println(Stack.getAll());
                } else if (keywords[1].equals("MEMORY")) {
                    System.out.println(Memory.debug());
                }
                break;
            case "NEWLINE":
                System.out.println();
            case "AND":
                boolean bool1 = Stack.get() != 0;
                Stack.pop();
                boolean bool2 = Stack.get() != 0;
                Stack.pop();
                Stack.put(bool1 && bool2 ? 1 : 0);
                break;
            case "OR":
                boolean bool3 = Stack.get() != 0;
                Stack.pop();
                boolean bool4 = Stack.get() != 0;
                Stack.pop();
                Stack.put(bool3 || bool4 ? 1 : 0);
                break;
            case "INVERT":
                boolean bool = Stack.get() != 0;
                Stack.pop();
                Stack.put(!bool ? 1 : 0);
                break;
            case "SIZE":
                Stack.put(Stack.size());
                break;
            case "XOR":
                boolean bool5 = Stack.get() != 0;
                Stack.pop();
                boolean bool6 = Stack.get() != 0;
                Stack.pop();
                Stack.put((bool5 || bool6) && !(bool5 && bool6) ? 1 : 0);
                break;
            case "PRINTLN":
                evaluate("PRINT " + keywords[1] + " " + keywords[2]);
                evaluate("NEWLINE");
                break;
            case "NEGATE":
                int got = Stack.get();
                Stack.pop();
                Stack.put(-got);
                break;
            case "ERROR":
                System.out.println("[ERROR] Unexpected error on line " + (FileEvaluator.line+1));
            case "END":
                FileEvaluator.running = false;
                break;
        }
    }
}
