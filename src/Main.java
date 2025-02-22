import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Main {

    private static TransformMatrix matrix;
    private static List<List<String>> blocks;
    private static List<List<int[]>> blockCoordinates;
    private static boolean solutionFound = false;
    private static long iterations = 0;
    private static long startTime;

    public static void main(String[] args) {
        // Baca input blocks
        blocks = BlockInput.ReadInputFromFile();
        if (blocks == null) {
            System.out.println("Periksa file input.");
            return;
        }

        // Ambil koordinat dari tiap blok
        blockCoordinates = new ArrayList<>();
        for (List<String> block : blocks) {
            blockCoordinates.add(BlockInput.BlockToCoordinates(block));
        }


        matrix = new TransformMatrix(BlockInput.M, BlockInput.N);

        // bruteforce
        startTime = System.currentTimeMillis();
        solvePuzzle(0);
        long endTime = System.currentTimeMillis();
        long runtime = endTime - startTime;

        // solusi ditemukan (print)
        if (solutionFound) {
            System.out.println("Solusi");
            System.out.println();
            matrix.printMatrix();

        } else {
            System.out.println("Tidak ditemukan solusi.");
        }


        System.out.println("\nWaktu eksekusi: " + runtime + " ms\n");
        System.out.println("Banyak kasus yang ditinjau: " + iterations);

        Scanner saveScanner = new Scanner(System.in);
        boolean userInputCorrect = false;
        boolean userInputImgCorrect = false;
        while (!userInputCorrect) {
            System.out.print("Apakah anda ingin menyimpan solusi? (ya/tidak): ");

            String userInput = saveScanner.nextLine().trim().toLowerCase();

            if (userInput.equals("ya")) {
                System.out.print("Masukkan nama file untuk menyimpan solusi: ");
                String saveFilename = saveScanner.nextLine().trim();
                matrix.saveMatrix(saveFilename + ".txt", solutionFound);
                userInputCorrect = true;

                //simpan gambar
                if (solutionFound) {
                    while (!userInputImgCorrect) {
                        System.out.print("Apakah anda ingin menyimpan solusi sebagai gambar? (ya/tidak): ");
                        String userInputImg = saveScanner.nextLine().trim().toLowerCase();

                        if (userInputImg.equals("ya")) {
                            System.out.print("Masukkan nama file untuk menyimpan solusi gambar: ");
                            String saveFilenameImg = saveScanner.nextLine().trim();
                            matrix.saveMatrixAsImage(saveFilenameImg + ".png");
                            userInputImgCorrect = true;
                        }

                        else if (userInputImg.equals("tidak")) {
                            System.out.println("Solusi tidak disimpan sebagai gambar.");
                            userInputImgCorrect = true;
                        } else {
                            System.out.println("Input tidak valid. Harap masukkan 'ya' atau 'tidak'.");
                        }
                    }
                }
            }

        else if (userInput.equals("tidak")) {
            System.out.println("Solusi tidak disimpan.");
            userInputCorrect = true;
        } else {
            System.out.println("Input tidak valid. Harap masukkan 'ya' atau 'tidak'.");
        }
    } }

    private static boolean BoardFullCheck() {
        for (int i = 0; i < matrix.rows; i++) {
            for (int j = 0; j < matrix.cols; j++) {
                if (matrix.matrix[i][j] == ' ') {
                    return false;
                }
            }
        }
        return true;
    }

    private static void solvePuzzle(int blockIndex) {
        if (blockIndex >= blockCoordinates.size()) {
            if (BoardFullCheck()) {
                solutionFound = true;
            }
            return;
        }

        List<int[]> currentBlock = blockCoordinates.get(blockIndex);
        char currentLetter = BlockInput.blockLetters.get(blockIndex);

        // Coba setiap posisi dan rotasi
        for (int rotation = 0; rotation < 4; rotation++) {
            List<int[]> rotatedBlock = Rotate.RotateBlocks(currentBlock, rotation);

            for (int x = 0; x < matrix.rows; x++) {
                for (int y = 0; y < matrix.cols; y++) {
                    iterations++;
                    if (matrix.BlockFitCheck(x, y, rotatedBlock)) {
                        matrix.addBlock(x, y, rotatedBlock, currentLetter);

                        solvePuzzle(blockIndex + 1);

                        if (solutionFound) {
                            return;
                        }

                        // backtrack jika gagal
                        matrix.eraseBlock(x, y, rotatedBlock);
                    }
                }
            }
        }
    }

}