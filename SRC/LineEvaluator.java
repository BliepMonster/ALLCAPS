import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

public class LineEvaluator {
    public static Scanner scanner = new Scanner(System.in);
    public static Random random = new Random();
    public static void evaluate(String line, FileEvaluator evaluator) {
        if (line.startsWith("//")) {
            return;
        }
        line = line.trim();
        String[] keywords = line.split(" ");
        switch (keywords[0]) {
            case "ALLOC":
                if (keywords.length != 3) {
                    throw new RuntimeException("WHAT THE FUCK IS UP WITH LINE "+(evaluator.line+1));
                }
                int xAlloc = Integer.parseInt(keywords[1]);
                int yAlloc = Integer.parseInt(keywords[2]);
                Memory.memory.place(xAlloc, yAlloc, Stack.get());
                Stack.pop();
                break;
            case "RETRIEVE":
                if (keywords.length != 3) {
                    throw new RuntimeException("WHAT THE FUCK IS UP WITH LINE "+(evaluator.line+1));
                }
                int xRetrieve = Integer.parseInt(keywords[1]);
                int yRetrieve = Integer.parseInt(keywords[2]);
                Stack.put(Memory.memory.get(xRetrieve, yRetrieve));
                break;
            case "PRINT":
                if (keywords.length != 3) {
                    throw new RuntimeException("WHAT THE FUCK IS UP WITH LINE "+(evaluator.line+1));
                }
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
                    try {
                        ArrayEvaluator.evaluateList("SIZE " + id);
                        int length = Stack.get();
                        Stack.pop();
                        System.out.print("[");
                        for (int i = 0; i < length; i++) {
                            ArrayEvaluator.evaluateList("GET " + id + " " + i);
                            System.out.print(Stack.get());
                            if (i != length - 1) {
                                System.out.print(", ");
                            }
                            Stack.pop();
                        }
                        System.out.print("]");
                    } catch (Exception e) {

                            throw new RuntimeException("WHAT THE FUCK IS UP WITH LINE "+evaluator.line);
                    }
                }
                break;
            case "PUSH":
                switch (keywords[1]) {
                    case "INT" -> {
                        if (keywords.length != 3) {
                            throw new RuntimeException("WHAT THE FUCK IS UP WITH LINE "+(evaluator.line+1));
                        }
                        Stack.put(FileEvaluator.evaluateInt(keywords[2]));
                    }
                    case "CHAR" -> {
                        if (keywords.length != 3) {
                            throw new RuntimeException("WHAT THE FUCK IS UP WITH LINE "+(evaluator.line+1));
                        }
                        Stack.put(keywords[2].toCharArray()[0]);
                    }
                    case "STRING" -> {
                        String[] split = line.split("'");
                        String string = split[1];
                        char[] pushArray = string.toCharArray();
                        for (int i = 0; i < pushArray.length; i++) {
                            Stack.put(pushArray[pushArray.length - 1 - i]);
                        } if (split.length != 2) {
                            throw new RuntimeException("WHAT THE FUCK IS UP WITH LINE "+(evaluator.line+1));
                        }
                    }
                }
                break;
            case "POP":
                if (keywords.length != 1) {
                    throw new RuntimeException("WHAT THE FUCK IS UP WITH LINE "+(evaluator.line+1));
                }
                Stack.pop();
                break;
            case "DUPLICATE":
                if (keywords.length != 1) {
                    throw new RuntimeException("WHAT THE FUCK IS UP WITH LINE "+(evaluator.line+1));
                }
                Stack.duplicate();
                break;
            case "SWAP":
                if (keywords.length != 1) {
                    throw new RuntimeException("WHAT THE FUCK IS UP WITH LINE "+(evaluator.line+1));
                }
                Stack.swap();
                break;
            case "ADD":
                if (keywords.length != 1) {
                    throw new RuntimeException("WHAT THE FUCK IS UP WITH LINE "+(evaluator.line+1));
                }
                int i1 = Stack.get();
                Stack.pop();
                int i2 = Stack.get();
                Stack.pop();
                Stack.put(i1+i2);
                break;
            case "INPUT":
                switch (keywords[1]) {
                    case "INT" -> {
                        if (keywords.length != 2) {
                            throw new RuntimeException("WHAT THE FUCK IS UP WITH LINE "+(evaluator.line+1));
                        }
                        Stack.put(scanner.nextInt());
                    }
                    case "CHAR" -> {
                        if (keywords.length != 2) {
                            throw new RuntimeException("WHAT THE FUCK IS UP WITH LINE "+(evaluator.line+1));
                        }
                        Stack.put(scanner.next().charAt(0));
                    }
                    case "LINE" -> {
                        if (keywords.length != 4) {
                            throw new RuntimeException("WHAT THE FUCK IS UP WITH LINE "+(evaluator.line+1));
                        }
                        if (keywords[2].equals("ON") && keywords[3].equals("STACK")) {
                            String string = scanner.nextLine();
                            char[] pushArray = string.toCharArray();
                            for (int i = 0; i < pushArray.length; i++) {
                                Stack.put(pushArray[pushArray.length - 1 - i]);
                            }
                        } else if (keywords[2].equals("AS") && keywords[3].equals("LIST")) {
                            evaluate("LIST NEW", evaluator);
                            int index = Stack.get();
                            String input = scanner.nextLine();
                            char[] array = input.toCharArray();
                            for (char c : array) {
                                evaluate("LIST ADD " + index + " " + (int) c, evaluator);
                            }
                        }
                    }
                }
                break;
            case "WAIT":
                if (keywords.length != 2) {
                    throw new RuntimeException("WHAT THE FUCK IS UP WITH LINE "+(evaluator.line+1));
                }
                try {
                    Thread.sleep(FileEvaluator.evaluateInt(keywords[1]));
                } catch (InterruptedException e) {
                    System.out.println("[ERROR]: INTERRUPTED");
                    evaluate("END", evaluator);
                }
                break;
            case "RANDOM":
                if (keywords.length != 1) {
                    throw new RuntimeException("WHAT THE FUCK IS UP WITH LINE "+(evaluator.line+1));
                }
                Stack.put(random.nextInt(256));
                break;
            case "SUBTRACT":
                if (keywords.length != 1) {
                    throw new RuntimeException("WHAT THE FUCK IS UP WITH LINE "+(evaluator.line+1));
                }
                int i1s = Stack.get(); // switch needs different names for variables
                Stack.pop();
                int i2s = Stack.get();
                Stack.pop();
                Stack.put(i1s - i2s);
                break;
            case "CLEAR":
                switch (keywords[1]) {
                    case "STACK" -> {
                        if (keywords.length != 2) {
                            throw new RuntimeException("WHAT THE FUCK IS UP WITH LINE "+(evaluator.line+1));
                        }
                        Stack.clear();
                    }
                    case "MEMORY" -> {
                        if (keywords.length != 2) {
                            throw new RuntimeException("WHAT THE FUCK IS UP WITH LINE "+(evaluator.line+1));
                        }
                        Memory.clear();
                    }
                    case "CELL" -> {
                        if (keywords.length != 4) {
                            throw new RuntimeException("WHAT THE FUCK IS UP WITH LINE "+(evaluator.line+1));
                        }
                        int xClear = Integer.parseInt(keywords[2]);
                        int yClear = Integer.parseInt(keywords[3]);
                        Memory.memory.place(xClear, yClear, 0);
                    }
                }
                break;
            case "GOTO":
                if (keywords.length != 3) {
                    throw new RuntimeException("WHAT THE FUCK IS UP WITH LINE "+(evaluator.line+1));
                }
                if (keywords[1].equals("LINE")) {
                    int lineToGoTo = FileEvaluator.evaluateInt(keywords[2]);
                    evaluator.line = lineToGoTo - 1;
                    evaluator.move = false;
                } else if (keywords[1].equals("LABEL")) {
                    int lineToGoTo = (evaluator.labels.get(keywords[2]));
                    evaluate("GOTO LINE "+lineToGoTo, evaluator);
                }
                break;
            case "MULTIPLY":
                if (keywords.length != 1) {
                    throw new RuntimeException("WHAT THE FUCK IS UP WITH LINE "+(evaluator.line+1));
                }
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
                if (keywords.length != 1) {
                    throw new RuntimeException("WHAT THE FUCK IS UP WITH LINE "+(evaluator.line+1));
                }
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
                if (keywords.length != 2) {
                    throw new RuntimeException("WHAT THE FUCK IS UP WITH LINE "+(evaluator.line+1));
                }
                if (keywords[1].equals("STACK")) {
                    System.out.println(Stack.getAll());
                } else if (keywords[1].equals("MEMORY")) {
                    System.out.println(Memory.debug());
                }
                break;
            case "NEWLINE":
                if (keywords.length != 1) {
                    throw new RuntimeException("WHAT THE FUCK IS UP WITH LINE "+(evaluator.line+1));
                }
                System.out.println();
                break;
            case "SPACE":
                if (keywords.length != 1) {
                    throw new RuntimeException("WHAT THE FUCK IS UP WITH LINE "+(evaluator.line+1));
                }
                System.out.print(" ");
                break;
            case "AND":
                if (keywords.length != 1) {
                    throw new RuntimeException("WHAT THE FUCK IS UP WITH LINE "+(evaluator.line+1));
                }
                boolean bool1 = Stack.get() != 0;
                Stack.pop();
                boolean bool2 = Stack.get() != 0;
                Stack.pop();
                Stack.put(bool1 && bool2 ? 1 : 0);
                break;
            case "OR":
                if (keywords.length != 1) {
                    throw new RuntimeException("WHAT THE FUCK IS UP WITH LINE "+(evaluator.line+1));
                }
                boolean bool3 = Stack.get() != 0;
                Stack.pop();
                boolean bool4 = Stack.get() != 0;
                Stack.pop();
                Stack.put(bool3 || bool4 ? 1 : 0);
                break;
            case "INVERT":
                if (keywords.length != 1) {
                    throw new RuntimeException("WHAT THE FUCK IS UP WITH LINE "+(evaluator.line+1));
                }
                boolean bool = Stack.get() != 0;
                Stack.pop();
                Stack.put(!bool ? 1 : 0);
                break;
            case "SIZE":
                if (keywords.length != 1) {
                    throw new RuntimeException("WHAT THE FUCK IS UP WITH LINE "+(evaluator.line+1));
                }
                Stack.put(Stack.size());
                break;
            case "XOR":
                if (keywords.length != 1) {
                    throw new RuntimeException("WHAT THE FUCK IS UP WITH LINE "+(evaluator.line+1));
                }
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
                if (keywords.length != 1) {
                    throw new RuntimeException("WHAT THE FUCK IS UP WITH LINE "+(evaluator.line+1));
                }
                int got = Stack.get();
                Stack.pop();
                Stack.put(-got);
                break;
            case "FLIPSTACK":
                if (keywords.length != 1) {
                    throw new RuntimeException("WHAT THE FUCK IS UP WITH LINE "+(evaluator.line+1));
                }
                Stack.flip();
                break;
            case "ERROR":
                if (keywords.length != 1) {
                    throw new RuntimeException("WHAT THE FUCK IS UP WITH LINE "+(evaluator.line+1));
                }
                System.out.println("[ERROR] UNEXPECTED ERROR ON LINE " + (evaluator.line+1));
                evaluate("END", evaluator);
                break;
            case "INCREMENT":
                if (keywords.length != 1) {
                    throw new RuntimeException("WHAT THE FUCK IS UP WITH LINE "+(evaluator.line+1));
                }
                int i = Stack.get();
                Stack.pop();
                Stack.put(i+1);
                break;
            case "DECREMENT":
                if (keywords.length != 1) {
                    throw new RuntimeException("WHAT THE FUCK IS UP WITH LINE "+(evaluator.line+1));
                }
                int d = Stack.get();
                Stack.pop();
                Stack.put(d-1);
                break;
            case "LOAD":
                switch (keywords[1]) {
                    case "FILE" -> {
                        StringBuilder stb = new StringBuilder(evaluator.root);
                        if (keywords[2].equals("STACK")) {
                            if (keywords.length != 3) {
                                throw new RuntimeException("WHAT THE FUCK IS UP WITH LINE "+(evaluator.line+1));
                            }
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
                            if (keywords.length != 4) {
                                throw new RuntimeException("WHAT THE FUCK IS UP WITH LINE "+(evaluator.line+1));
                            }
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
                        if (keywords.length != 4) {
                            throw new RuntimeException("WHAT THE FUCK IS UP WITH LINE "+(evaluator.line+1));
                        }
                        if (keywords[2].equals("ASSOCIATION")) {
                            Stack.put(Memory.associated.get(keywords[3]));
                        }
                    }
                }
                break;
            case "CLOSE":
                if (keywords[1].equals("FILE")) {
                    if (keywords.length != 3) {
                        throw new RuntimeException("WHAT THE FUCK IS UP WITH LINE "+(evaluator.line+1));
                    }
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
                if (keywords.length != 1) {
                    throw new RuntimeException("WHAT THE FUCK IS UP WITH LINE "+(evaluator.line+1));
                }
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
                try {
                    ArrayEvaluator.evaluateSet(s);
                } catch (Exception e) {
                    throw new RuntimeException("WHAT THE FUCK IS UP WITH LINE "+(evaluator.line+1));
                }
                break;
            case "LIST":
                StringBuilder bld = new StringBuilder();
                for (int index = 1; index < keywords.length; index++) {
                    bld.append(keywords[index]);
                    bld.append(' ');
                }
                String str = bld.toString().trim();
                try {
                    ArrayEvaluator.evaluateList(str);
                } catch (Exception e) {
                    throw new RuntimeException("WHAT THE FUCK IS UP WITH LINE "+(evaluator.line+1));
                }
                break;
            case "SHIFT":
                if (keywords.length != 3) {
                    throw new RuntimeException("WHAT THE FUCK IS UP WITH LINE "+(evaluator.line+1));
                }
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
                if (keywords.length != 3) {
                    throw new RuntimeException("WHAT THE FUCK IS UP WITH LINE "+(evaluator.line+1));
                }
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
                    if (keywords.length != 3) {
                        throw new RuntimeException("WHAT THE FUCK IS UP WITH LINE "+(evaluator.line+1));
                    }
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
                if (keywords.length != 2) {
                    throw new RuntimeException("WHAT THE FUCK IS UP WITH LINE "+(evaluator.line+1));
                }
                evaluator.root = keywords[1];
                break;
            case "ASSOCIATE":
                if (keywords.length != 4) {
                    throw new RuntimeException("WHAT THE FUCK IS UP WITH LINE "+(evaluator.line+1));
                }
                if (keywords[2].equals("WITH")) {
                    Memory.associated.put(keywords[3], FileEvaluator.evaluateInt(keywords[1]));
                }
                break;
            case "END":
                if (keywords.length != 1) {
                    throw new RuntimeException("WHAT THE FUCK IS UP WITH LINE "+(evaluator.line+1));
                }
                evaluator.running = false;
                break;
            case "FUNCTION", " ", "", "QUIT", "LABEL":
                break;
            default:
                throw new RuntimeException(keywords[0] + " IS NOT VALID IN THIS CONTEXT");
        }
    }
}
