public class Memory {
    public static int[][] memory = new int[100][100];
    public static void clear() {
        for (int i = 0; i < 100; i++) {
            for (int j = 0; j < 100; j++) {
                memory[i][j] = 0;
            }
        }
    }
    public static String debug() {
        StringBuilder builder = new StringBuilder();
        builder.append("\n");
        for (int i = 0; i < 100; i++) {
            for (int j = 0; j < 100; j++) {
                builder.append(memory[i][j]);
                builder.append(" ");
            } builder.append("\n");
        }
        return builder.toString();
    }
}
