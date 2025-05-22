import java.io.BufferedReader;
import java.io.FileReader;

public class Main {
    public static void main(String[] args) {
        StringBuilder builder = new StringBuilder();
        for (String s : args) {
            builder.append(s);
        }
        String path = builder.toString();
        StringBuilder fileBuilder = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = reader.readLine()) != null) {
                fileBuilder.append(line).append("\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        String file = fileBuilder.toString();
        new FileEvaluator().evaluate(file);
    }
}
