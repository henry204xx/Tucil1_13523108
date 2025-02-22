import java.util.*;

public class Main {

    private static Pyramid pyramid;
    private static TransformMatrix matrix;
    private static List<List<String>> blocks;
    private static List<List<int[]>> blockCoordinates;
    private static boolean puzzleSolved = false;
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

        pyramid = new Pyramid(BlockInput.M, BlockInput.N);
        matrix = new TransformMatrix(BlockInput.M, BlockInput.N);

        // bruteforce
        startTime = System.currentTimeMillis();
        if (BlockInput.Type.equals("DEFAULT")) {
            solvePuzzle(0);
        } else {
            solvePuzzlePyramid(0);
        }

        long endTime = System.currentTimeMillis();
        long runtime = endTime - startTime;

        // solusi ditemukan (print)
        if (puzzleSolved) {
            System.out.println("Solusi");
            System.out.println();

            if (BlockInput.Type.equals("DEFAULT")) {
                matrix.printMatrix();
            } else {
                pyramid.printPyramid();
            }


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

            //simpan txt
            if (userInput.equals("ya")) {
                System.out.print("Masukkan nama file untuk menyimpan solusi: ");
                String saveFilename = "../test/" + saveScanner.nextLine().trim();
                if (BlockInput.Type.equals("DEFAULT")) {
                    matrix.saveMatrix(saveFilename + ".txt", puzzleSolved);
                } else {
                    pyramid.savePyramidMatrix(saveFilename + ".txt", puzzleSolved);
                }
                userInputCorrect = true;

                //simpan gambar (png)
                if (puzzleSolved) {
                    while (!userInputImgCorrect) {
                        System.out.print("Apakah anda ingin menyimpan solusi sebagai gambar? (ya/tidak): ");
                        String userInputImg = saveScanner.nextLine().trim().toLowerCase();

                        if (userInputImg.equals("ya")) {
                            System.out.print("Masukkan nama file untuk menyimpan solusi gambar: ");
                            String saveFilenameImg = "../test/" + saveScanner.nextLine().trim();
                            if (BlockInput.Type.equals("DEFAULT")) {
                                matrix.saveMatrixAsImage(saveFilenameImg + ".png");
                            } else {
                                pyramid.savePyramidAsImage(saveFilenameImg + ".png");
                            }

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
                puzzleSolved = true;
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

                        if (puzzleSolved) {
                            return;
                        }

                        // backtrack jika gagal
                        matrix.eraseBlock(x, y, rotatedBlock);
                    }
                }
            }
        }
    }

    private static void solvePuzzlePyramid(int blockIndex) {
        if (blockIndex >= blockCoordinates.size()) {
            if (pyramid.PyramidFullCheck()) {
                puzzleSolved = true;
            }
            return;
        }

        List<int[]> currentBlock = blockCoordinates.get(blockIndex);
        char currentLetter = BlockInput.blockLetters.get(blockIndex);

        // coba setiap posisi dan rotasi 3D
        for (int rotation = 0; rotation < 8; rotation++) {
            List<int[]> rotatedBlock = Rotate.RotateBlocksBy45(currentBlock, rotation);

            for (int bottomStack = 0; bottomStack < pyramid.stacks; bottomStack++) {
                for (int endStack = bottomStack; endStack < pyramid.stacks; endStack++) {
                    for (int x = 0; x < pyramid.pyramid[bottomStack].length; x++) {
                        for (int y = 0; y < pyramid.pyramid[bottomStack][x].length; y++) {
                            iterations++;
                            if (pyramid.BlockFitCheckPyramid(bottomStack, endStack, x, y, rotatedBlock)) {
                                pyramid.addBlockPyramid(bottomStack, endStack, x, y, rotatedBlock, currentLetter);
                                solvePuzzlePyramid(blockIndex + 1);

                                if (puzzleSolved) {
                                    return;
                                }
                                // Backtrack
                                pyramid.eraseBlockPyramid(bottomStack, x, y, rotatedBlock);
                            }
                        }
                    }
                }
            }
        }
    }

}