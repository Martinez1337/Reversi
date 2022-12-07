package martinez1337.Reversi.ViewHandlers;

import martinez1337.Reversi.Models.*;

import java.util.List;

public class ConsoleGameOutput {
    private static final String GREETING = "\t\t\t\tWelcome to Reversi!";
    private static final String FAREWELL = "\t\t\t\tGoodbye!";

    private static final String PLAYER_VS_PC_EASY_HEADER = "Player VS Computer [EASY]";
    private static final String PLAYER_VS_PC_HARD_HEADER = "Player VS Computer [HARD]";
    private static final String PLAYER_VS_PLAYER_HEADER = "Player VS Player";

    private static final String GAME_START_MESSAGE = "The game has been started.";
    private static final String NEXT_MOVE_REQUEST = "Choose your next move: ";
    private static final String NO_MOVES_LEFT_MESSAGE = "No moves left...";
    private static final String GAME_ENDING_MESSAGE = "The game has been ended.";

    private static final String[] LETTERS = {" ", "A", "B", "C", "D", "E", "F", "G", "H"};

    public static void printGreetings() {
        System.out.println(GREETING);
    }

    public static void printFarewell() {
        System.out.println(FAREWELL);
    }

    public static void printMainMenu() {
        System.out.println("""
                Main menu:
                1) New game
                2) Exit""");
    }

    public static void printGameModeMenu() {
        System.out.println("Choose game mode:\n"
                + "1) " + PLAYER_VS_PC_EASY_HEADER + "\n"
                + "2) " + PLAYER_VS_PC_HARD_HEADER + "\n"
                + "3) " + PLAYER_VS_PLAYER_HEADER + "\n"
        );
    }

    public static void printGameStartMessage() {
        System.out.println(GAME_START_MESSAGE);
    }

    public static void printGameBoard(Board board) {
        Chip[][] chips = board.getChips();

        System.out.println("\n");

        for (int i = 0; i < Board.SIZE + 1; i++) {
            for (int j = 0; j < Board.SIZE + 1; j++) {
                if (i == 0) {
                    if (j == Board.SIZE) {
                        System.out.println(j + "  |  ");
                    } else if (j == 0) {
                        System.out.print("   |  ");
                    } else {
                        System.out.print(j + "  |  ");
                    }
                } else if (j == 0) {
                    System.out.print(LETTERS[i] + "  |  ");
                } else if (j == Board.SIZE && chips[i - 1][j - 1] != null) {
                    System.out.println(chips[i - 1][j - 1] + "  |  ");
                } else if (j == Board.SIZE) {
                    System.out.println("   |  ");
                } else {
                    if (chips[i - 1][j - 1] != null) {
                        System.out.print(chips[i - 1][j - 1] + "  |  ");
                    } else {
                        System.out.print("   |  ");
                    }
                }
            }

            printBoardLine(i);
        }
    }

    public static void printGameBoardWithPossibleMoves(Board board,
                                                       List<Coordinates> possibleMoves) {
        Chip[][] chips = board.getChips();

        System.out.println("\n");

        for (int i = 0; i < Board.SIZE + 1; i++) {
            for (int j = 0; j < Board.SIZE + 1; j++) {
                Coordinates possibleMove = null;
                for (Coordinates c : possibleMoves) {
                    if (c.getX() == i && c.getY() == j) {
                        possibleMove = c;
                        break;
                    }
                }
                if (i == 0) {
                    if (j == Board.SIZE) {
                        System.out.println(j + "  |  ");
                    } else if (j == 0) {
                        System.out.print("   |  ");
                    } else {
                        System.out.print(j + "  |  ");
                    }
                } else if (j == 0) {
                    System.out.print(LETTERS[i] + "  |  ");
                } else {
                    if (chips[i - 1][j - 1] != null) {
                        System.out.print(chips[i - 1][j - 1] + "  |  ");
                    } else if (possibleMove != null) {
                        System.out.print("\u2217" + "  |  ");
                    } else {
                        System.out.print("   |  ");
                    }
                    if (j == Board.SIZE) {
                        System.out.println();
                    }
                }
            }

            printBoardLine(i);
        }
    }

    public static void printGameInfo(Score gameScore, ChipColor currentTurnColor) {
        printGameScore(gameScore);
        printCurrentTurnColor(currentTurnColor);
    }

    public static void printBestSessionScore(int score) {
        System.out.println("Best session score - " + score);
    }

    public static void printPossibleMoves(List<Coordinates> coordinatesList) {
        System.out.println("Possible moves: ");

        for (int i = 0; i < coordinatesList.size(); i++) {
            int x = coordinatesList.get(i).getX();
            int y = coordinatesList.get(i).getY();

            System.out.println((i + 1) + ") (" + LETTERS[x] + "; " + y + ")");
        }
    }

    public static void printNextMoveRequest() {
        System.out.print(NEXT_MOVE_REQUEST);
    }

    public static void printRestartRequest() {
        System.out.println("""
                1) Go to main menu
                2) Exit game""");
    }

    public static void printErrorMessage(String mg) {
        System.out.println(mg);
    }

    public static void printNoMovesForColor(ChipColor color) {
        System.out.print("There are no moves for ");
        if (color.equals(ChipColor.black)) {
            System.out.println("blacks");
        } else {
            System.out.println("whites");
        }
    }

    public static void printNoMovesLeftMessage() {
        System.out.println(NO_MOVES_LEFT_MESSAGE);
    }

    public static void printGameEndingMessage() {
        System.out.println(GAME_ENDING_MESSAGE);
    }

    public static void printGameResults(Score finalScore) {
        if (finalScore.getWhites() > finalScore.getBlacks()) {
            System.out.println("\t\t\tWhites win!");
        } else if (finalScore.getWhites() < finalScore.getBlacks()) {
            System.out.println("\t\t\tBlacks win!");
        } else {
            System.out.println("\t\t\tDraw!");
        }

        System.out.println("Final score: Whites - " + finalScore.getWhites()
                + "; Blacks - " + finalScore.getBlacks());
        System.out.println();
    }

    private static void printBoardLine(int i) {
        if (i == Board.SIZE) {
            for (int j = 0; j < 52; j++) {
                System.out.print("-");
            }
        } else {
            System.out.print("---+");
            for (int j = 0; j < 8; j++) {
                System.out.print("-----+");
            }
        }

        System.out.println();
    }

    private static void printGameScore(Score gameScore) {
        System.out.println("\t\u25CF - " + gameScore.getWhites());
        System.out.println("\t\u25CB - " + gameScore.getBlacks());
    }

    private static void printCurrentTurnColor(ChipColor color) {
        System.out.println("It's " + color.toString() + " turn!");
    }
}
