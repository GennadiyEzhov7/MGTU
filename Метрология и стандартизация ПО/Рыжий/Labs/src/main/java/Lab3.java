import java.util.Arrays;
import java.util.Scanner;

public class Lab3 {
    public static void main(String[] args) {
        int M, N;
        Scanner scanner = new Scanner(System.in);
        System.err.println("Enter M:");
        M = scanner.nextInt();
        System.err.println("Enter N:");
        N = scanner.nextInt();

        int mass[][] = new int[M][N];
        randomizeMass(mass);
        for (int[] m : mass)
            System.err.println(Arrays.toString(m));
        int minElement = findMinElement(mass);
        int minElementCount = findElementCount(mass, minElement);
        System.err.println("Min element: " + minElement + " Count: " + minElementCount);
    }

    private static int findElementCount(int[][] mass, int element) {
        int count = 0;
        for (int[] ints : mass)
            for (int anInt : ints)
                if (anInt == element)
                    ++count;

        return count;
    }

    private static int findMinElement(int[][] mass) {
        int min = Integer.MAX_VALUE;
        for (int[] ints : mass)
            for (int anInt : ints)
                if (anInt < min)
                    min = anInt;

        return min;
    }

    public static void randomizeMass(int[][] mass) {
        for (int i = 0; i < mass.length; i++) {
            for (int j = 0; j < mass[i].length; j++) {
                mass[i][j] = (int) (Math.random() * 100);
            }
        }
    }
}

