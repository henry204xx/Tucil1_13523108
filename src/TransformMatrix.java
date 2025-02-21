import java.util.List;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

public class TransformMatrix {
    public char[][] matrix;
    public int rows, cols;


    // Inisialisasi
    public TransformMatrix(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.matrix = new char[rows][cols];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                matrix[i][j] = ' ';
            }
        }
    }

    // Hapus blok
    public void eraseBlock(int startX, int startY, List<int[]> block) {
        for (int[] coord : block) {
            int x = startX + coord[0];
            int y = startY + coord[1];

            matrix[x][y] = ' ';

        }
    }

    // Check if a block can fit in the given position
    public boolean BlockFitCheck(int startX, int startY, List<int[]> block) {
        for (int[] coord : block) {
            int x = startX + coord[0];
            int y = startY + coord[1];

            if (!isEffective(x, y) || matrix[x][y] != ' ') {
                return false;
            }
        }
        return true;
    }


    public boolean addBlock(int startX, int startY, List<int[]> block, char alphabet) {
        if (BlockFitCheck(startX, startY, block)) {
            for (int[] coord : block) {
                int x = startX + coord[0];
                int y = startY + coord[1];

                matrix[x][y] = alphabet;

            }
            return true;
        }
        return false;
    }

    private static String getColoredText(String text, String colorCode) {
        // ANSI escape codes for colors are not directly HEX, but we can use basic colors for simplicity
        return "\u001B[38;2;" + getRGBFromHex(colorCode) + "m" + text + "\u001B[0m";
    }

    private static String getRGBFromHex(String hex) {
        int r = Integer.valueOf(hex.substring(1, 3), 16);
        int g = Integer.valueOf(hex.substring(3, 5), 16);
        int b = Integer.valueOf(hex.substring(5, 7), 16);
        return r + ";" + g + ";" + b;
    }

    // Print the matrix
    public void printMatrix() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                char cell = matrix[i][j];

                if (cell == ' ') {
                    System.out.print("  "); // Print empty spaces
                } else {
                    // Retrieve the hex color
                    String hexColor = BlockInput.letterColors.getOrDefault(cell, "#FFFFFF"); // Default to white

                    // Convert hex to RGB
                    String rgbColor = getRGBFromHex(hexColor);

                    // Print with ANSI color codes
                    System.out.print("\u001B[38;2;" + rgbColor + "m" + cell + "\u001B[0m ");
                }
            }
            System.out.println();
        }
    }

    public void saveMatrixToHTML(String filename) {
        try (FileWriter writer = new FileWriter(filename)) {
            writer.write("<html><body><pre style='font-size:15px;'>\n");

            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    char cell = matrix[i][j];
                    String colorCode = BlockInput.letterColors.getOrDefault(cell, "#FFFFFF");

                    if (cell == ' ') {
                        writer.write("&nbsp;");
                    } else {
                        writer.write("<span style='color:" + colorCode + ";'>" + cell + "</span> ");
                    }
                }
                writer.write("<br>");
            }

            writer.write("</pre></body></html>");
            System.out.println("Matrix tersimpan sebagai: " + filename);
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }


    private boolean isEffective(int x, int y) {
        return x >= 0 && x < rows && y >= 0 && y < cols;
    }

//
//    public static void main(String[] args) {
//        RemoveBlock game = new RemoveBlock(10, 10);
//
//        // Define different blocks
//        List<int[]> blockL = List.of( // L-shape
//                new int[]{0, 0},
//                new int[]{1, 0},
//                new int[]{2, 0},
//                new int[]{2, 1}
//        );
//
//        List<int[]> blockSquare = List.of( // Square block
//                new int[]{0, 0}, new int[]{0, 1},
//                new int[]{1, 0}, new int[]{1, 1}
//        );
//
//        List<int[]> blockLine = List.of( // Horizontal line
//                new int[]{0, 0}, new int[]{0, 1},
//                new int[]{0, 2}, new int[]{0, 3}
//        );
//
//        List<int[]> blockT = List.of( // T-shape
//                new int[]{0, 0}, new int[]{0, 1}, new int[]{0, 2},
//                new int[]{1, 1}
//        );
//
//        System.out.println("Before placing blocks:");
//        game.printMatrix();
//
//        // Place blocks with different symbols
//        game.placeBlock(0, 0, blockL, 'L');
//        game.placeBlock(4, 4, blockSquare, 'S');
//        game.placeBlock(6, 2, blockLine, 'X');
//        game.placeBlock(8, 6, blockT, 'T');
//
//        System.out.println("\nAfter placing blocks:");
//        game.printMatrix();
//
//        // Remove the L-shape block
//        game.eraseBlock(0, 0, blockL);
//        System.out.println("\nAfter erasing L-block:");
//        game.printMatrix();
//    }
}
