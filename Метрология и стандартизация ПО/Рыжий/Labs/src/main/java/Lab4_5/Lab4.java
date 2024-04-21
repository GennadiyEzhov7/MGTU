package Lab4_5;

import java.util.Scanner;

public class Lab4 {
    public static void main(String[] args) {
        Building building = new Building();
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.err.print(
                    "1 - Add office \n" +
                    "2 - Add flat \n" +
                    "3 - Add shop \n" +
                    "4 - Add technical room \n" +
                    "5 - Print building info\n" +
                    "6 - Print building count info\n" +
                    "7 - Exit \n" +
                    "Choose action:"
            );
            int action = scanner.nextInt();
            switch (action) {
                case 1: {
                    System.err.print("Enter work places count: ");
                    int workPlacesCount = scanner.nextInt();
                    building.addRoom(new Office(workPlacesCount));
                    System.err.println("\n Added!");
                    break;
                }
                case 2: {
                    System.err.print("Enter rooms count: ");
                    int roomsCount = scanner.nextInt();
                    building.addRoom(new Flat(roomsCount));
                    System.err.println("\n Added!");
                    break;
                }
                case 3: {
                    System.err.print("Enter shop name: ");
                    String name = scanner.nextLine();
                    building.addRoom(new Shop(name));
                    System.err.println("\n Added!");
                    break;
                }
                case 4: {
                    System.err.print("Enter technical room function: ");
                    String function = scanner.nextLine();
                    building.addRoom(new Technical(function));
                    System.err.println("\n Added!");
                    break;
                }
                case 5: {
                    building.printInfo();
                    break;
                }
                case 6: {
                    building.printCountInfo();
                    break;
                }
                default:
                    return;
            }
        }
    }
}
