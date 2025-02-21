import java.util.ArrayList;
import java.util.List;

public class Rotate {

    // Print blok dan koordinat
    public static void printBlock(List<int[]> block) {
        for (int[] coord : block) {
            System.out.println("(" + coord[0] + ", " + coord[1] + ")");
        }
        System.out.println();
    }

    // Rotate blok 90 derajat counterclockwise
    private static List<int[]> rotate90(List<int[]> block) {
        List<int[]> rotated = new ArrayList<>();

        // rotasi setiap koordinat pada blok
        for (int[] coord : block) {
            rotated.add(new int[]{-coord[1], coord[0]});
        }

        return NormalizeToZero(rotated);
    }

    // Normalisasi koordinat agar mulai dari (0,0)
    private static List<int[]> NormalizeToZero(List<int[]> block) {
        int minX = Integer.MAX_VALUE, minY = Integer.MAX_VALUE;

        for (int[] coord : block) {
            minX = Math.min(minX, coord[0]);
            minY = Math.min(minY, coord[1]);
        }

        List<int[]> normalized = new ArrayList<>();
        for (int[] coord : block) {
            normalized.add(new int[]{coord[0] - minX, coord[1] - minY});
        }

        return normalized;
    }

    // Rotasi blok (0,90,180,270) dengan n kali rotasi 90 counterclockwise
    public static List<int[]> RotateBlocks(List<int[]> block, int NumofRotationBy90) {
        List<int[]> rotated = block;
        for (int i = 0; i < NumofRotationBy90; i++) {
            rotated = rotate90(rotated);
        }
        return rotated;
    }

//    public static void main(String[] args) {
//        List<List<String>> blocks = BlockInput.ReadInputFromFile(); //List of Blocks
//
//    }

}