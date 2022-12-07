package martinez1337.Reversi.ViewHandlers;

import martinez1337.Reversi.Game.GameMode;

import java.util.Scanner;

public class ConsoleGameInput {
    private static final Scanner INPUT_SCANNER = new Scanner(System.in);

    public static GameMode readGameModeChoice() {
        String input;
        do {
            System.out.print("\n>>>> ");
            input = INPUT_SCANNER.nextLine().trim();
            input = input.replace(")", "");
            switch (input) {
                case "1":
                    return GameMode.PlayerVsComputer;
                case "2":
                    return GameMode.PlayerVsProComputer;
                case "3":
                    return GameMode.PlayerVsPlayer;
                default:
                    System.out.print("Select number from the list!");
            }
        } while (true);
    }

    public static int readMainMenuChoice() {
        String input;
        do {
            System.out.print("\n>>>> ");
            input = INPUT_SCANNER.nextLine().trim();
            input = input.replace(")", "");
            switch (input) {
                case "1":
                    return 1;
                case "2":
                    return 2;
                default:
                    System.out.print("Select number from the list!");
            }
        } while (true);
    }

    public static int readNextMoveChoice(int numOfChoices) {
        String input;
        int res = 0;
        do {
            System.out.print("\n>>>> ");
            input = INPUT_SCANNER.nextLine().trim();
            input = input.replace(")", "");

            try {
                res = Integer.parseInt(input);
            } catch (NumberFormatException ex) {
                System.out.println("Try input number of choice list again...");
                continue;
            }

            if (res < 1 || res > numOfChoices) {
                System.out.println("Select number from the list!");
            }
        } while (res < 1 || res > numOfChoices);

        return res;
    }

    public static boolean readRestartChoice() {
        return readMainMenuChoice() == 1;
    }
}
