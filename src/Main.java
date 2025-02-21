import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Main {

    private static TransformMatrix matrix;
    private static List<List<String>> blocks;
    private static List<List<int[]>> blockCoordinates;
    private static boolean solutionFound = false;
    private static int iterations = 0;
    private static long startTime;

    public static void main(String[] args) {
        // Read input blocks from file
        blocks = BlockInput.ReadInputFromFile();
        if (blocks == null) {
            System.out.println("Periksa file input.");
            return;
        }

        // Initialize block coordinates and letters
        blockCoordinates = new ArrayList<>();
        for (List<String> block : blocks) {
            blockCoordinates.add(BlockInput.BlockToCoordinates(block));
        }

        // Initialize the matrix
        matrix = new TransformMatrix(BlockInput.M, BlockInput.N);

        // Start the backtracking algorithm
        startTime = System.currentTimeMillis(); // Record start time
        solvePuzzle(0);
        long endTime = System.currentTimeMillis(); // Record end time
        long runtime = endTime - startTime; // Calculate runtime in milliseconds

        // Print the final matrix if a solution is found
        if (solutionFound) {
            System.out.println("Solusi");
            System.out.println();
            matrix.printMatrix();

        } else {
            System.out.println("Tidak ditemukan solusi.");
        }

        // Print the number of iterations and runtime
        System.out.println("\nWaktu eksekusi: " + runtime + " ms\n");
        System.out.println("Banyak kasus yang ditinjau: " + iterations);
        Scanner saveScanner = new Scanner(System.in);
        System.out.print("Apakah anda ingin menyimpan solusi? (ya/tidak): ");

        String userInput = saveScanner.nextLine().trim().toLowerCase();

        if (userInput.equals("ya")) {
            System.out.print("Masukkan nama file untuk menyimpan solusi: ");
            String saveFilename = saveScanner.nextLine().trim();
            matrix.saveMatrixToHTML(saveFilename + ".html");
        }
        else if (userInput.equals("tidak")) {
            System.out.println("Solusi tidak disimpan.");
        } else {
            System.out.println("Input tidak valid. Harap masukkan 'ya' atau 'tidak'.");
        }
    }

    private static void solvePuzzle(int blockIndex) {
        if (blockIndex >= blockCoordinates.size()) {
            // Check if the board is fully filled
            if (isBoardFullyFilled()) {
                solutionFound = true; // Solution is valid
            }
            return;
        }

        List<int[]> currentBlock = blockCoordinates.get(blockIndex);
        char currentLetter = BlockInput.blockLetters.get(blockIndex);

        // Try placing the block in every possible position and rotation
        for (int rotation = 0; rotation < 4; rotation++) {
            List<int[]> rotatedBlock = Rotate.RotateBlocks(currentBlock, rotation);

            for (int x = 0; x < matrix.rows; x++) {
                for (int y = 0; y < matrix.cols; y++) {
                    iterations++; // Increment iteration counter
                    if (matrix.BlockFitCheck(x, y, rotatedBlock)) {
                        // Place the block
                        matrix.addBlock(x, y, rotatedBlock, currentLetter);

                        // Recur for the next block
                        solvePuzzle(blockIndex + 1);

                        if (solutionFound) {
                            return; // Stop if a valid solution is found
                        }

                        // Backtrack: remove the block
                        matrix.eraseBlock(x, y, rotatedBlock);
                    }
                }
            }
        }
    }

    // Check if the board is fully filled (no ' ' characters)
    private static boolean isBoardFullyFilled() {
        for (int i = 0; i < matrix.rows; i++) {
            for (int j = 0; j < matrix.cols; j++) {
                if (matrix.matrix[i][j] == ' ') {
                    return false; // Found an empty space
                }
            }
        }
        return true; // Board is fully filled
    }
}