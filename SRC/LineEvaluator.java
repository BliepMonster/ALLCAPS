import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

public class LineEvaluator {
    public static Scanner scanner = new Scanner(System.in);
    public static Random random = new Random();
    public static void evaluate(String line, FileEvaluator evaluator) {
        String[] keywords = line.split(" ");
        switch (keywords[0]) {
            case "ALLOC":
                int xAlloc = Integer.parseInt(keywords[1]);
                int yAlloc = Integer.parseInt(keywords[2]);
                Memory.memory.place(xAlloc, yAlloc, Stack.get());
                Stack.pop();
                break;
            case "RETRIEVE":
                int xRetrieve = Integer.parseInt(keywords[1]);
                int yRetrieve = Integer.parseInt(keywords[2]);
                Stack.put(Memory.memory.get(xRetrieve, yRetrieve));
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
                    int count = FileEvaluator.evaluateInt(keywords[2]);
                    for (int i = 0; i < count; i++) {
                        System.out.print(Stack.get());
                        Stack.pop();
                    }
                } else if (keywords[1].equals("LIST")) {
                    int id = FileEvaluator.evaluateInt(keywords[2]);
                    ArrayEvaluator.evaluateList("SIZE "+id);
                    int length = Stack.get();
                    Stack.pop();
                    System.out.print("[");
                    for (int i = 0; i < length; i++) {
                        ArrayEvaluator.evaluateList("GET "+id+" "+i);
                        System.out.print(Stack.get());
                        if (i != length-1) {
                            System.out.print(", ");
                        }
                        Stack.pop();
                    } System.out.print("]");
                }
                break;
            case "PUSH":
                switch (keywords[1]) {
                    case "INT" -> Stack.put(Integer.parseInt(keywords[2]));
                    case "CHAR" -> Stack.put(keywords[2].toCharArray()[0]);
                    case "STRING" -> {
                        String string = line.split("#")[1];
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
                switch (keywords[1]) {
                    case "INT" -> Stack.put(scanner.nextInt());
                    case "CHAR" -> Stack.put(scanner.next().charAt(0));
                    case "LINE" -> {
                        if (keywords[2].equals("ON") && keywords[3].equals("STACK")) {
                            String string = scanner.nextLine();
                            char[] pushArray = string.toCharArray();
                            for (int i = 0; i < pushArray.length; i++) {
                                Stack.put(pushArray[pushArray.length - 1 - i]);
                            }
                        }
                    }
                }
                break;
            case "WAIT":
                try {
                    Thread.sleep(Integer.parseInt(keywords[1]));
                } catch (InterruptedException e) {
                    System.out.println("[ERROR]: INTERRUPTED");
                    evaluate("END", evaluator);
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
                        Memory.memory.place(xClear, yClear, 0);
                    }
                }
                break;
            case "GOTO":
                if (keywords[1].equals("LINE")) {
                    if (keywords[2].equals("STACK")) {
                        evaluator.line = Stack.get() - 1;
                        evaluator.move = false;
                        Stack.pop();
                        break;
                    }
                    int lineToGoTo = Integer.parseInt(keywords[2]);
                    evaluator.line = lineToGoTo - 1;
                    evaluator.move = false;
                } else if (keywords[1].equals("LABEL")) {
                    int lineToGoTo = (evaluator.labels.get(keywords[2]));
                    evaluate("GOTO LINE "+lineToGoTo, evaluator);
                }
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
                    evaluator.lastConditional = execute;
                    if (!execute) {
                        break;
                    }
                    evaluate(line.split(":")[1], evaluator);
                } else if (keywords[1].equals("STACK")) {
                    StringBuilder exprBuilder = new StringBuilder();
                    for (int i = 2; i < keywords.length; i++) {
                        exprBuilder.append(keywords[i]);
                        exprBuilder.append(" ");
                    }
                    String expr = exprBuilder.toString().trim();
                    int lastStack = Stack.get();
                    Stack.pop();
                    boolean eval = lastStack != 0;
                    evaluator.lastConditional = eval;
                    if (eval) {
                        evaluate(expr, evaluator);
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
                if (!evaluator.lastConditional) {
                    evaluate(builder.toString(), evaluator);
                } evaluator.lastConditional = !evaluator.lastConditional;
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
                break;
            case "SPACE":
                System.out.print(" ");
                break;
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
                StringBuilder sb = new StringBuilder();
                for (int i = 1; i < keywords.length; i++) {
                    sb.append(keywords[i]);
                    sb.append(" ");
                }
                evaluate("PRINT " + sb.toString().trim(), evaluator);
                evaluate("NEWLINE", evaluator);
                break;
            case "NEGATE":
                int got = Stack.get();
                Stack.pop();
                Stack.put(-got);
                break;
            case "FLIPSTACK":
                Stack.flip();
                break;
            case "ERROR":
                System.out.println("[ERROR] Unexpected error on line " + (evaluator.line+1));
                evaluate("END", evaluator);
                break;
            case "INCREMENT":
                int i = Stack.get();
                Stack.pop();
                Stack.put(i+1);
                break;
            case "DECREMENT":
                int d = Stack.get();
                Stack.pop();
                Stack.put(d-1);
                break;
            case "LOAD":
                switch (keywords[1]) {
                    case "FILE" -> {
                        StringBuilder stb = new StringBuilder(evaluator.root);
                        if (keywords[2].equals("STACK")) {
                            while (Stack.hasItem()) {
                                stb.append((char) Stack.get());
                                Stack.pop();
                            }
                        } else {
                            for (int in = 2; in < keywords.length; in++) {
                                stb.append(keywords[in]);
                                stb.append(" ");
                            }
                        }
                        String path = stb.toString();
                        int key = evaluator.readers.size();
                        try {
                            evaluator.readers.put(key, new BufferedReader(new FileReader(path)));
                        } catch (Exception e) {
                            System.out.println("[INTERPRETER ERROR] FILE NOT FOUND: " + path);
                        }
                        Stack.put(key);
                    }
                    case "CHAR" -> {
                        if (keywords[2].equals("FROM")) {
                            int id = FileEvaluator.evaluateInt(keywords[3]);
                            BufferedReader reader = evaluator.readers.get(id);
                            try {
                                Stack.put(reader.read());
                            } catch (IOException e) {
                                Stack.put(' ');
                                System.out.println("[INTERPRETER ERROR] UNSPECIFIED");
                            }
                        }
                    }
                    case "FROM" -> {
                        if (keywords[2].equals("ASSOCIATION")) {
                            Stack.put(Memory.associated.get(keywords[3]));
                        }
                    }
                }
                break;
            case "CLOSE":
                if (keywords[1].equals("FILE")) {
                    int id = FileEvaluator.evaluateInt(keywords[2]);
                    BufferedReader br = evaluator.readers.get(id);
                    try {
                        br.close();
                    } catch (IOException e) {
                        System.out.println("[INTERPRETER ERROR] UNSPECIFIED");
                    }
                    evaluator.readers.remove(id);
                } break;
            case "EQUALS":
                int e1 = Stack.get();
                Stack.pop();
                int e2 = Stack.get();
                Stack.pop();
                Stack.put(e1 == e2 ? 1 : 0);
                break;
            case "SET":
                StringBuilder b = new StringBuilder();
                for (int index = 1; index < keywords.length; index++) {
                    b.append(keywords[index]);
                    b.append(' ');
                }
                String s = b.toString().trim();
                ArrayEvaluator.evaluateSet(s);
                break;
            case "LIST":
                StringBuilder bld = new StringBuilder();
                for (int index = 1; index < keywords.length; index++) {
                    bld.append(keywords[index]);
                    bld.append(' ');
                }
                String str = bld.toString().trim();
                ArrayEvaluator.evaluateList(str);
                break;
            case "SHIFT":
                String direction = keywords[1];
                int amount = FileEvaluator.evaluateInt(keywords[2]);
                int subject = Stack.get();
                Stack.pop();
                if (direction.equals("LEFT")) {
                    Stack.put(subject << amount);
                }
                else if (direction.equals("RIGHT")) {
                    Stack.put(subject >> amount);
                }
                break;
            case "EXECUTE":
                if (keywords[1].equals("FUNCTION")) {
                    String function = keywords[2];
                    FunctionEvaluator.evaluate(function, evaluator.line, evaluator);
                }
                else if (keywords[1].equals("PROGRAM")) {
                    int ID = FileEvaluator.evaluateInt(keywords[2]);
                    FunctionEvaluator.evaluate(evaluator.linked.get(ID));
                }
                break;
            case "INIT":
                if (keywords[1].equals("PROGRAM")) {
                    StringBuilder fileBuilder = new StringBuilder();
                    try (BufferedReader reader = new BufferedReader(new FileReader(evaluator.root+keywords[2]))) {
                        String l;
                        while ((l = reader.readLine()) != null) {
                            fileBuilder.append(l).append("\n");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    String file = fileBuilder.toString();
                    int pos = evaluator.linked.size();
                    evaluator.linked.put(pos, file);
                    Stack.put(pos);
                }
                break;
            case "ROOT":
                evaluator.root = keywords[1];
                break;
            case "ASSOCIATE":
                if (keywords[2].equals("WITH")) {
                    Memory.associated.put(keywords[3], FileEvaluator.evaluateInt(keywords[1]));
                }
                break;
            case "END":
                evaluator.running = false;
                break;
        }
    }
}
