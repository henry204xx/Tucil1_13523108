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

        return NormalizeCoordinates(rotated);
    }

    // Normalisasi koordinat agar mulai dari (0,0)
    private static List<int[]> NormalizeCoordinates(List<int[]> block) {
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

    private static List<int[]> rotate45(List<int[]> block) {
        List<int[]> rotated = new ArrayList<>();

        for (int[] coord : block) {
            int x = coord[0];
            int y = coord[1];
            int z = coord[2];


            double angle = Math.toRadians(45);
            double cosTheta = Math.cos(angle);
            double sinTheta = Math.sin(angle);

            int newX = (int)Math.round(cosTheta * x - sinTheta * y);
            int newY = (int)Math.round(sinTheta * x + cosTheta * y);

            rotated.add(new int[]{newX, newY, z});
        }

        return NormalizeCoordinates3D(rotated);
    }


    private static List<int[]> NormalizeCoordinates3D(List<int[]> block) {
        int minX = Integer.MAX_VALUE, minY = Integer.MAX_VALUE, minZ = Integer.MAX_VALUE;

        for (int[] coord : block) {
            minX = Math.min(minX, coord[0]);
            minY = Math.min(minY, coord[1]);
            minZ = Math.min(minZ, coord[2]);
        }


        List<int[]> normalized = new ArrayList<>();
        for (int[] coord : block) {
            normalized.add(new int[]{coord[0] - minX, coord[1] - minY, coord[2] - minZ});
        }

        return normalized;
    }

    public static List<int[]> RotateBlocksBy45(List<int[]> block, int rotation) {
        List<int[]> rotated = new ArrayList<>();


        double angle = Math.toRadians(rotation * 45);

        // Apply the rotation around X, Y, Z axes
        for (int[] coord : block) {
            int x = coord[0];
            int y = coord[1];
            int z = 0;


            int newX = x;
            int newY = (int)(y * Math.cos(angle) - z * Math.sin(angle));
            int newZ = (int)(y * Math.sin(angle) + z * Math.cos(angle));


            int rotatedX = (int)(newX * Math.cos(angle) + newZ * Math.sin(angle));
            int rotatedY = newY;
            int rotatedZ = (int)(-newX * Math.sin(angle) + newZ * Math.cos(angle));

            int finalX = (int)(rotatedX * Math.cos(angle) - rotatedY * Math.sin(angle));
            int finalY = (int)(rotatedX * Math.sin(angle) + rotatedY * Math.cos(angle));

            rotated.add(new int[]{finalX, finalY, rotatedZ});
        }

        return NormalizeCoordinates3D(rotated);
    }
}