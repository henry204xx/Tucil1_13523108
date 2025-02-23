import java.util.List;
import java.io.FileWriter;
import java.io.IOException;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class Pyramid {
    public char[][][] pyramid; 
    public int stacks; 
    public int baseRows, baseCols; 

    public Pyramid(int baseRows, int baseCols) {
        this.baseRows = baseRows;
        this.baseCols = baseCols;
        this.stacks = Math.min(baseRows, baseCols); // Jumlah stack/tingkat
        this.pyramid = new char[stacks][][];

        // Inisialisasi pyramid
        for (int stack = 0; stack < stacks; stack++) {
            int rows = baseRows - stack;
            int cols = baseCols - stack;
            pyramid[stack] = new char[rows][cols]; //ukuran tiap tingkat
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    pyramid[stack][i][j] = ' ';
                }
            }
        }
    }

    // mengecek apakah block fit di suatu stack
    public boolean BlockFitCheckPyramid(int bottomStack, int topStack, int pivotX, int pivotY, List<int[]> block) {
        for (int stack = bottomStack; stack <= topStack; stack++) {
            for (int[] coord : block) {
                int x = pivotX + coord[0];
                int y = pivotY + coord[1];
                
                if (x < 0 || x >= pyramid[stack].length || y < 0 || y >= pyramid[stack][0].length || pyramid[stack][x][y] != ' ') {
                    return false;
                }
            }
        }
        return true;
    }

    
    public boolean addBlockPyramid(int bottomStack, int topStack, int pivotX, int pivotY, List<int[]> block, char alphabet) {
        if (BlockFitCheckPyramid(bottomStack, topStack, pivotX, pivotY, block)) {
            for (int stack = bottomStack; stack <= topStack; stack++) {
                for (int[] coord : block) {
                    int x = pivotX + coord[0];
                    int y = pivotY + coord[1];
                    pyramid[stack][x][y] = alphabet;
                }
            }
            return true;
        }
        return false;
    }


    public void eraseBlockPyramid(int stack, int pivotX, int pivotY, List<int[]> block) {
        for (int[] coord : block) {
            int x = pivotX + coord[0];
            int y = pivotY + coord[1];
            pyramid[stack][x][y] = ' ';
        }
    }


    public void printPyramid() {
        for (int stack = 0; stack < stacks; stack++) {
            for (int i = 0; i < pyramid[stack].length; i++) {
                for (int j = 0; j < pyramid[stack][i].length; j++) {
                    char cell = pyramid[stack][i][j];
                    if (cell == ' ') {
                        System.out.print("  ");
                    } else {
                        String hexColor = BlockInput.letterColors.getOrDefault(cell, "#FFFFFF");
                        String rgbColor = TransformMatrix.getRGBFromHex(hexColor);
                        System.out.print("\u001B[38;2;" + rgbColor + "m" + cell + "\u001B[0m ");
                    }
                }
                System.out.println();
            }
        }
    }

    public boolean PyramidFullCheck() {
        for (int stack = 0; stack < stacks; stack++) {
            for (int i = 0; i < pyramid[stack].length; i++) {
                for (int j = 0; j < pyramid[stack][i].length; j++) {
                    if (pyramid[stack][i][j] == ' ') {
                        return false;
                    }
                }
            }
        }
        return true;
    }


    public void savePyramidMatrix(String filename, boolean solutionFound) {
        try (FileWriter writer = new FileWriter(filename)) {
            if (solutionFound) {
                for (int stack = 0; stack < stacks; stack++) { 
                    for (int row = 0; row < pyramid[stack].length; row++) {
                        for (int col = 0; col < pyramid[stack][row].length; col++) {
                            char elmt = pyramid[stack][row][col];
                            if (elmt == ' ') {
                                writer.write(' ');
                            } else {
                                writer.write(elmt);
                            }
                        }
                        writer.write(System.lineSeparator()); 
                    }
                }
            } else {
                writer.write("Tidak ada solusi");
            }

            System.out.println("Solusi disimpan sebagai: " + filename);
        } catch (IOException e) {
            System.out.println("Error dalam menyimpan solusi");
            e.printStackTrace();
        }
    }

    public void savePyramidAsImage(String filename) {
        int size = 30;
        int width = pyramid[0][0].length * size; 
        int height = 0;

       
        for (int stack = 0; stack < stacks; stack++) {
            height += pyramid[stack].length * size;
        }

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D result = image.createGraphics();

        result.setColor(Color.WHITE);
        result.fillRect(0, 0, width, height);
        result.setFont(new Font("Arial", Font.BOLD, size - 5));
        FontMetrics metrics = result.getFontMetrics();

        int Y = 0; 

        for (int stack = 0; stack < stacks; stack++) {
            int rows = pyramid[stack].length;
            int cols = pyramid[stack][0].length;

            for (int row = 0; row < rows; row++) {
                for (int col = 0; col < cols; col++) {
                    char cell = pyramid[stack][row][col];
                    String hexColor = BlockInput.letterColors.getOrDefault(cell, "#000000");
                    Color textColor = Color.decode(hexColor);

                    result.setColor(textColor);

                    int x = col * size + (size - metrics.charWidth(cell)) / 2;
                    int y = Y + (row + 1) * size - (size / 4);

                    result.drawString(String.valueOf(cell), x, y);
                }
            }
            Y += rows * size;
        }

        result.dispose();

        try {
            ImageIO.write(image, "png", new File(filename));
            System.out.println("Piramid disimpan sebagai: " + filename);
        } catch (IOException e) {
            System.out.println("Error dalam menyimpan gambar");
            e.printStackTrace();
        }
    }

}
