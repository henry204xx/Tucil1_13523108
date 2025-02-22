import java.util.List;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;

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
    public void eraseBlock(int pivotX, int pivotY, List<int[]> block) {
        for (int[] coord : block) {
            int x = pivotX + coord[0];
            int y = pivotY + coord[1];

            matrix[x][y] = ' ';

        }
    }

    // cek apakah posisi blok melebihi dimensi matriks
    private boolean isEffective(int x, int y) {
        return x >= 0 && x < rows && y >= 0 && y < cols;
    }

    // cek apakah blok bisa ditambahkan
    public boolean BlockFitCheck(int pivotX, int pivotY, List<int[]> block) {
        for (int[] coord : block) {
            int x = pivotX + coord[0];
            int y = pivotY + coord[1];

            if (!isEffective(x, y) || matrix[x][y] != ' ') {
                return false;
            }
        }
        return true;
    }

    public boolean addBlock(int pivotX, int pivotY, List<int[]> block, char alphabet) {
        if (BlockFitCheck(pivotX, pivotY, block)) {
            for (int[] coord : block) {
                int x = pivotX + coord[0];
                int y = pivotY + coord[1];

                matrix[x][y] = alphabet;

            }
            return true;
        }
        return false;
    }

    // mendapatkan warna teks
    private static String getColoredText(String text, String colorCode) {
        return "\u001B[38;2;" + getRGBFromHex(colorCode) + "m" + text + "\u001B[0m";
    }

    private static String getRGBFromHex(String hex) {
        int r = Integer.valueOf(hex.substring(1, 3), 16);
        int g = Integer.valueOf(hex.substring(3, 5), 16);
        int b = Integer.valueOf(hex.substring(5, 7), 16);
        return r + ";" + g + ";" + b;
    }

    // Print matrix
    public void printMatrix() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                char cell = matrix[i][j];

                if (cell == ' ') {
                    System.out.print("  ");
                } else {

                    String hexColor = BlockInput.letterColors.getOrDefault(cell, "#FFFFFF");
                    String rgbColor = getRGBFromHex(hexColor);
                    System.out.print("\u001B[38;2;" + rgbColor + "m" + cell + "\u001B[0m ");
                }
            }
            System.out.println();
        }
    }

    public void saveMatrix(String filename, boolean solutionFound) {
        try (FileWriter writer = new FileWriter(filename)) {
            if (solutionFound) {
                for (int i = 0; i < rows; i++) {
                    for (int j = 0; j < cols; j++) {
                        char elmt = matrix[i][j];
                        if (elmt == ' ') {
                            writer.write(' ');
                        } else {
                            writer.write(elmt);
                        }
                    }
                    if (i < rows-1) {
                        writer.write(System.lineSeparator());
                    }
                }
            }
            else {
                writer.write("Tidak ada solusi");
            }
            System.out.println("Solusi disimpan sebagai: " + filename);
        } catch (IOException e) {
            System.out.println("Error dalam menyimpan solusi");
            e.printStackTrace();
        }
    }

    public void saveMatrixAsImage(String filename) {
        int cellSize = 30;
        int width = cols * cellSize;
        int height = rows * cellSize;

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D result = image.createGraphics();


        result.setColor(Color.WHITE);
        result.fillRect(0, 0, width, height);
        result.setFont(new Font("Arial", Font.BOLD, cellSize - 5));
        FontMetrics metrics = result.getFontMetrics();

        // tulis matriks
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                char cell = matrix[i][j];
                String hexColor = BlockInput.letterColors.getOrDefault(cell, "#000000");
                Color textColor = Color.decode(hexColor);

                result.setColor(textColor);

                int x = j * cellSize + (cellSize - metrics.charWidth(cell)) / 2;
                int y = (i + 1) * cellSize - (cellSize / 4);

                result.drawString(String.valueOf(cell), x, y);
            }
        }

        result.dispose();

        // Save
        try {
            ImageIO.write(image, "png", new File(filename));
            System.out.println("Matriks disimpan sebagai: " + filename);
        } catch (IOException e) {
            System.out.println("Error dalam menyimpan gambar");
            e.printStackTrace();
        }
    }
}
