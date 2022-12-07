package martinez1337.Reversi.Game.Handlers;

import martinez1337.Reversi.Game.GameMode;
import martinez1337.Reversi.Models.*;

import java.util.*;
import java.util.stream.Stream;

public class GameLogicController {
    // All shifts of moves in arrays of coordinates deltas
    private static final int[] X_MOVES_DELTA = new int []{-1, -1, 0, 1, 1, 1, 0, -1};
    private static final int[] Y_MOVES_DELTA = new int []{0, 1, 1, 1, 0, -1, -1, -1};

    public static Chip[][] getNewChipsCondition(Coordinates addedChipCoordinates, Board board) {
        Chip[][] boardChips = board.getChips();

        int x = addedChipCoordinates.getX();
        int y = addedChipCoordinates.getY();
        ChipColor addedChipColor = boardChips[x - 1][y - 1].getColor();

        for (int i = 0; i < 8; i++) {
            List<Chip> chipList = getEnemyChipsInDirection(x, y, X_MOVES_DELTA[i], Y_MOVES_DELTA[i],
                    addedChipColor, boardChips);
            if (chipList.size() != 0) {
                for (Chip ch : chipList) {
                    Coordinates coordinates = ch.getCoordinates();
                    boardChips[coordinates.getX() - 1][coordinates.getY() - 1].reverseColor();
                }
            }
        }

        return boardChips;
    }

    public static List<Coordinates> getPossibleMoves(ChipColor turnColor, Board board) {
        List<Coordinates> resultList = new ArrayList<>();

        Chip[][] boardChips = board.getChips();

        for (int i = 0; i < Board.SIZE; i++) {
            for (int j = 0; j < Board.SIZE; j++) {
                if (boardChips[i][j] != null && boardChips[i][j].getColor() != turnColor) {
                    for (int k = 0; k < 8; k++) {
                        boolean isPossibleMove = false;
                        var moveCoordinates = new Coordinates(
                                i + X_MOVES_DELTA[k] + 1, j + Y_MOVES_DELTA[k] + 1);
                        if (isInBoarders(moveCoordinates.getX(), moveCoordinates.getY())
                                && boardChips[i + X_MOVES_DELTA[k]][j + Y_MOVES_DELTA[k]] == null) {
                            isPossibleMove = isValidChipMove(moveCoordinates, turnColor, board);
                        }

                        if (isPossibleMove && !isInCoordinateList(resultList, moveCoordinates)) {
                            resultList.add(moveCoordinates);
                        }
                    }
                }
            }
        }

        return resultList;
    }

    public static ChipColor switchTurnColor(ChipColor currentTurnColor) {
        if (currentTurnColor == ChipColor.black) {
            return ChipColor.white;
        } else {
            return ChipColor.black;
        }
    }

    public static Coordinates getComputerMove(GameMode mode, List<Coordinates> possibleMoves,
                                              Board board) {
        Coordinates resultMove = null;

        Double bestValue = null;
        for (Coordinates moveCoordinates : possibleMoves) {
            double funcVal;

            if (mode.equals(GameMode.PlayerVsComputer)) {
                funcVal = countSimpleFunction(moveCoordinates, ChipColor.white, board);
            } else {
                double simpleFuncRes = countSimpleFunction(moveCoordinates, ChipColor.white, board);

                Board newBoard = new Board(board.getChips());
                newBoard.addNewChip(
                        new Chip(ChipColor.white, moveCoordinates.getX(), moveCoordinates.getY()));
                newBoard = new Board(getNewChipsCondition(moveCoordinates, newBoard));

                funcVal = countComplicatedFunction(simpleFuncRes, newBoard);
            }

            if (bestValue == null || funcVal > bestValue) {
                bestValue = funcVal;
                resultMove = moveCoordinates;
            }
        }

        return resultMove;
    }

    private static boolean isValidChipMove(Coordinates coordinates, ChipColor color, Board board) {
        boolean result;
        Chip[][] boardChips = board.getChips();

        int x = coordinates.getX();
        int y = coordinates.getY();

        for (int i = 0; i < 8; i++) {
            result = checkMoveDirection(x + X_MOVES_DELTA[i], y + Y_MOVES_DELTA[i],
                    X_MOVES_DELTA[i], Y_MOVES_DELTA[i], color, boardChips);

            if (result) {
                return true;
            }
        }

        return false;
    }

    private static double countSimpleFunction(Coordinates moveCoordinates,
                                              ChipColor turnColor, Board board) {
        List<Chip> enemyChips = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            List<Chip> enemyChipsInDirection = getEnemyChipsInDirection(
                    moveCoordinates.getX(), moveCoordinates.getY(),
                    X_MOVES_DELTA[i], Y_MOVES_DELTA[i], turnColor, board.getChips());

            enemyChips = Stream.concat(enemyChips.stream(), enemyChipsInDirection.stream()).toList();
        }

        double res = 0;
        for (Chip coveringChip : enemyChips) {
            res += countCoveredChipValue(coveringChip.getCoordinates());
        }
        res += countNewChipValue(moveCoordinates);

        return res;
    }

    private static double countComplicatedFunction(double simpleRes, Board newBoard) {
        List<Coordinates> possibleEnemyMoves = getPossibleMoves(ChipColor.black, newBoard);

        double maxEnemyMoveResult = 0;

        for (Coordinates coordinates : possibleEnemyMoves) {
            double funcVal = countSimpleFunction(coordinates, ChipColor.black, newBoard);

            if (maxEnemyMoveResult < funcVal) {
                maxEnemyMoveResult = funcVal;
            }
        }

        return simpleRes - maxEnemyMoveResult;
    }

    private static double countCoveredChipValue(Coordinates coordinates) {
        if (coordinates.getX() == 1 || coordinates.getY() == 1
                || coordinates.getX() == 8 || coordinates.getY() == 8) {
            return 2;
        }

        return 1;
    }

    private static double countNewChipValue(Coordinates coordinates) {
        if ((coordinates.getX() == 1 && (coordinates.getY() == 1 || coordinates.getY() == 8))
            || (coordinates.getX() == 8 && (coordinates.getY() == 1 || coordinates.getY() == 8))) {
            return 0.8;
        } else if (coordinates.getX() == 1 || coordinates.getY() == 1
                || coordinates.getX() == 8 || coordinates.getY() == 8) {
            return 0.4;
        }

        return 0;
    }

    private static boolean checkMoveDirection(int x, int y, int dX, int dY,
                                              ChipColor color, Chip[][] chips) {
        int opponentChipsCount = 0;
        boolean foundSameColorChip = false;
        while(isInBoarders(x, y) && chips[x - 1][y - 1] != null) {
            if (chips[x - 1][y - 1].getColor() == color) {
                foundSameColorChip = true;
                break;
            } else {
                x += dX;
                y += dY;
                opponentChipsCount++;
            }
        }

        return opponentChipsCount > 0 && foundSameColorChip;
    }

    private static List<Chip> getEnemyChipsInDirection(int x, int y, int dX, int dY,
                                        ChipColor color, Chip[][] chips) {
        List<Chip> enemyChipsList = new ArrayList<>();

        x += dX;
        y += dY;

        boolean foundSameColorChip = false;
        while(isInBoarders(x, y) && chips[x - 1][y - 1] != null) {
            if (chips[x - 1][y - 1].getColor() == color) {
                foundSameColorChip = true;
                break;
            } else {
                enemyChipsList.add(chips[x - 1][y - 1]);
                x += dX;
                y += dY;
            }
        }

        if (!foundSameColorChip) {
            enemyChipsList.clear();
        }

        return enemyChipsList;
    }

    private static boolean isInCoordinateList(List<Coordinates> list, Coordinates coordinates) {
        boolean isIn = false;
        for (Coordinates value : list) {
            if (value.getX() == coordinates.getX()
                    && value.getY() == coordinates.getY()) {
                isIn = true;
                break;
            }
        }

        return isIn;
    }

    private static boolean isInBoarders(int x, int y) {
        return x <= 8 && x >= 1 && y <= 8 && y >= 1;
    }
}
