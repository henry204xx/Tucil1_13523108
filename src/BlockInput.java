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
    public static final Map<Character, String> letterColors = new HashMap<>();

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
                String type = fileScanner.nextLine().trim();
                if (!"STANDARD".equals(type)) {
                    System.out.println("Error: Tipe harus merupakan 'STANDARD'.");
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
                    char firstChar = findFirstLetter(line);

                    if (firstChar != currentLetter) {
                        if (currentBlock != null) {
                            blocks.add(currentBlock);
                        }
                        currentBlock = new ArrayList<>();
                        currentLetter = firstChar;
                        blockLetters.add(firstChar);
                        blockCount++;

                        // Assign a unique color
                        if (!letterColors.containsKey(currentLetter)) {
                            letterColors.put(currentLetter, COLORS[colorIndex % COLORS.length]);
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

            System.out.println("Jumlah blok: " + blockCount);

            // Validasi jumlah blok
            if (blockCount != P) {
                System.out.println("Error: Jumlah blok tidak sesuai dengan jumlah blok (" + P + ").");
                return null;
            }

//            // Print the letter-color mapping with colored letters
//            System.out.println("Warna yang diberikan untuk setiap huruf:");
//            for (Map.Entry<Character, String> entry : letterColors.entrySet()) {
//                String colorCode = entry.getValue();
//                String coloredLetter = getColoredText(entry.getKey().toString(), colorCode);
//                System.out.println(coloredLetter);
//            }

            return blocks;
        } catch (FileNotFoundException e) {
            System.out.println("File tidak ditemukan: " + filePath);
        }
        return null;
    }


    private static char findFirstLetter(String line) {
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
