import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class BlockInput {


    public static int M;
    public static int N;
    public static int P;
    public static List<Character> blockLetters = new ArrayList<>();

    private static String[] COLORS = {
            "#FF0000", "#00FF00", "#0000FF", "#FFFF00", "#FF00FF", "#00FFFF", "#800000", "#808000",
            "#008000", "#800080", "#008080", "#000080", "#FFA500", "#A52A2A", "#8A2BE2", "#5F9EA0",
            "#7FFF00", "#D2691E", "#DC143C", "#FF8C00", "#8B008B", "#556B2F", "#9932CC", "#8B0000",
            "#E9967A", "#9400D3"
    };
    public static Map<Character, String> letterColors = new HashMap<>();

    public static List<List<String>> ReadInputFromFile() {
        Scanner inputScanner = new Scanner(System.in);

        // Nama file
        System.out.print("Masukkan nama file: ");
        String filePath = inputScanner.nextLine().trim();

        List<List<String>> blocks = new ArrayList<>();

        //Mapping warna
        int colorIndex = 0;

        try {
            Scanner fileScanner = new Scanner(new File(filePath));

            // Baca dimensi dan jumlah blok

            if (fileScanner.hasNextLine()) {
                String[] numbers = fileScanner.nextLine().split(" ");
                if (numbers.length != 3) {
                    System.out.println("Error: Baris pertama harus berisi 3 bilangan bulat.");
                    return null;
                }
                try {
                    M = Integer.parseInt(numbers[0]);
                    N = Integer.parseInt(numbers[1]);
                    P = Integer.parseInt(numbers[2]);
                    if (M <= 0 || N <= 0) {
                        System.out.println("Error: Dimensi matriks harus lebih besar dari 0.");
                        return null;
                    }
                    if (P <= 0 || P > 26) {
                        System.out.println("Error: Jumlah blok harus antara 1-26.");
                        return null;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Error: Baris pertama harus berisi 3 bilangan bulat.");
                    return null;
                }
            } else {
                System.out.println("Error: File harus memiliki setidaknya dua baris.");
                return null;
            }

            // Baca tipe
            if (fileScanner.hasNextLine()) {
                String type = fileScanner.nextLine().trim().toUpperCase();
                if (!"DEFAULT".equals(type)) {
                    System.out.println("Error: Tipe harus merupakan 'DEFAULT'.");
                    return null;
                }
            } else {
                System.out.println("Error: File harus memiliki setidaknya dua baris.");
                return null;
            }

            // Membaca blok
            List<String> currentBlock = null;
            char currentLetter = '\0';
            int blockCount = 0;

            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                if (!line.isEmpty()) {
                    if (!validateCharacterInput(line)) {
                        System.out.println("Error: Terdapat karakter yang tidak valid pada file. Hanya A-Z dan spasi yang diperbolehkan.");
                        return null;
                    }
                    char firstChar = getLetter(line);

                    if (firstChar != currentLetter) {
                        if (currentBlock != null) {
                            blocks.add(currentBlock);
                        }
                        currentBlock = new ArrayList<>();
                        currentLetter = firstChar;
                        blockLetters.add(firstChar);
                        blockCount++;


                        if (!letterColors.containsKey(currentLetter)) {
                            letterColors.put(currentLetter, COLORS[colorIndex]);
                            colorIndex++;
                        }
                    }

                    currentBlock.add(line);
                }
            }

            if (currentBlock != null) {
                blocks.add(currentBlock);
            }

            fileScanner.close();


            // Validasi jumlah blok
            if (blockCount != P) {
                System.out.println("Error: Jumlah blok tidak sesuai dengan jumlah blok (" + P + ").");
                return null;
            }
            return blocks;

        } catch (FileNotFoundException e) {
            System.out.println("File tidak ditemukan. Pastikan nama file ditambah dengan .txt");
        }
        return null;
    }

    private static boolean validateCharacterInput(String line) {
        for (int i = 0; i < line.length(); i++) {
            char ch = line.charAt(i);
            if (ch != ' ' && (ch < 'A' || ch > 'Z')) {
                return false;
            }
        }
        return true;
    }

    private static char getLetter(String line) {
        for (int i = 0; i < line.length(); i++) {
            if (line.charAt(i) != ' ') {
                return line.charAt(i);
            }
        }
        return '\0';
    }

    public static List<int[]> BlockToCoordinates(List<String> block) {
        List<int[]> coordinates = new ArrayList<>();
        int baseRow = 0, baseCol = 0;

        for (int row = 0; row < block.size(); row++) {
            String line = block.get(row);
            for (int col = 0; col < line.length(); col++) {
                if (line.charAt(col) != ' ') {
                    coordinates.add(new int[]{row - baseRow, col - baseCol});
                }
            }
        }
        return coordinates;
    }
}
