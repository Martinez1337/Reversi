package martinez1337.Reversi.Game.Handlers;

import martinez1337.Reversi.Game.GameMode;
import martinez1337.Reversi.Models.*;
import martinez1337.Reversi.ViewHandlers.ConsoleGameInput;
import martinez1337.Reversi.ViewHandlers.ConsoleGameOutput;

import java.util.List;

public class GameProcessHandler {
    private static GameMode gameMode;
    private static Board gameBoard;
    private static Score gamesScore;
    private static int bestSessionScore = 0;

    public static void startSession() {
        boolean isRestarting;
        do {
            ConsoleGameOutput.printGreetings();
            ConsoleGameOutput.printMainMenu();
            ConsoleGameOutput.printBestSessionScore(bestSessionScore);

            if (ConsoleGameInput.readMainMenuChoice() == 2) {
                break;
            }

            ConsoleGameOutput.printGameModeMenu();
            gameMode = ConsoleGameInput.readGameModeChoice();

            try {
                startGame();
            } catch (Exception ex) {
                ConsoleGameOutput.printErrorMessage(ex.getMessage());
                exitGame();
            }

            ConsoleGameOutput.printRestartRequest();
            isRestarting = ConsoleGameInput.readRestartChoice();
        } while (isRestarting);

        exitGame();
    }

    private static void startGame() throws InterruptedException {
        ConsoleGameOutput.printGameStartMessage();

        ChipColor currentTurnColor = ChipColor.black;
        gamesScore = new Score();

        initializeGameBoard();

        int turnSkipCount = 0;
        do {
            List<Coordinates> possibleMoves = GameLogicController.getPossibleMoves(
                    currentTurnColor, gameBoard);

            if (possibleMoves.size() == 0) {
                ++turnSkipCount;

                ConsoleGameOutput.printNoMovesForColor(currentTurnColor);
            } else {
                turnSkipCount = 0;

                Coordinates nextMoveCoordinates = null;
                switch (gameMode) {
                    case PlayerVsComputer, PlayerVsProComputer -> {
                        if (currentTurnColor.equals(ChipColor.black)) {
                            ConsoleGameOutput.printGameBoardWithPossibleMoves(gameBoard,
                                    possibleMoves);
                            ConsoleGameOutput.printGameInfo(gamesScore, currentTurnColor);
                            nextMoveCoordinates = getPlayerNextMove(possibleMoves);
                        } else {
                            ConsoleGameOutput.printGameBoard(gameBoard);
                            ConsoleGameOutput.printGameInfo(gamesScore, currentTurnColor);
                            nextMoveCoordinates = GameLogicController.getComputerMove(
                                    gameMode, possibleMoves, gameBoard);

                            // Small pause for better experience (optional)
                            Thread.sleep(700);
                        }
                    }
                    case PlayerVsPlayer -> {
                        ConsoleGameOutput.printGameBoardWithPossibleMoves(gameBoard, possibleMoves);
                        ConsoleGameOutput.printGameInfo(gamesScore, currentTurnColor);
                        nextMoveCoordinates = getPlayerNextMove(possibleMoves);
                    }
                }

                makeMove(currentTurnColor, nextMoveCoordinates);
            }

            if (turnSkipCount >= 2) {
                ConsoleGameOutput.printNoMovesLeftMessage();
                break;
            }

            currentTurnColor = GameLogicController.switchTurnColor(currentTurnColor);
            gamesScore.update(countChipByColor(ChipColor.white), countChipByColor(ChipColor.black));
        } while (!isGameEnded());

        endMatch();
    }

    private static void endMatch() {
        ConsoleGameOutput.printGameBoard(gameBoard);
        ConsoleGameOutput.printGameEndingMessage();
        ConsoleGameOutput.printGameResults(gamesScore);

        updateBestSessionScore(gamesScore, gameMode);
    }

    private static void exitGame() {
        ConsoleGameOutput.printFarewell();
        System.exit(0);
    }

    private static void initializeGameBoard() {
        gameBoard = new Board();
        gameBoard.addStartingChips();

        gamesScore.update(2,2);
    }

    private static void updateBestSessionScore(Score resultScore, GameMode mode) {
        switch (mode) {
            case PlayerVsComputer, PlayerVsProComputer -> {
                if (resultScore.getBlacks() > bestSessionScore) {
                    bestSessionScore = resultScore.getBlacks();
                }
            }

            case PlayerVsPlayer -> {
                if (Math.max(resultScore.getBlacks(), resultScore.getWhites()) > bestSessionScore) {
                    bestSessionScore = Math.max(resultScore.getBlacks(), resultScore.getWhites());
                }
            }
        }
    }

    private static Coordinates getPlayerNextMove(List<Coordinates> possibleMoves) {
        Coordinates nextMoveCoordinates;
        ConsoleGameOutput.printPossibleMoves(possibleMoves);
        ConsoleGameOutput.printNextMoveRequest();

        nextMoveCoordinates = possibleMoves.get(
                ConsoleGameInput.readNextMoveChoice(possibleMoves.size()) - 1);

        return nextMoveCoordinates;
    }

    private static void makeMove(ChipColor color, Coordinates coordinates) {
        Chip newChip = new Chip(color, coordinates.getX(), coordinates.getY());
        gameBoard.addNewChip(newChip);

        Chip[][] newChips = GameLogicController.getNewChipsCondition(
                newChip.getCoordinates(), gameBoard);

        gameBoard = new Board(newChips);
    }

    private static int countChipByColor(ChipColor color) {
        Chip[][] chips = gameBoard.getChips();

        int count = 0;
        for (Chip[] chip : chips) {
            for (int j = 0; j < chips.length; j++) {
                if (chip[j] != null && chip[j].getColor() == color) {
                    ++count;
                }
            }
        }

        return count;
    }

    private static boolean isGameEnded() {
        return gamesScore.getBlacks() + gamesScore.getWhites() >= 64;
    }
}
