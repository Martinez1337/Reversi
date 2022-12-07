package martinez1337.Reversi.Models;

public class Board {
    public static final int SIZE = 8;
    private final Chip[][] CHIPS;

    public Board() {
        CHIPS = new Chip[SIZE][SIZE];
    }

    public Board(Chip[][] chips) {
        CHIPS = new Chip[chips.length][];

        for (int i = 0; i < CHIPS.length; i++) {
            CHIPS[i] = new Chip[chips.length];
            for (int j = 0; j < CHIPS[i].length; j++) {
                if (chips[i][j] != null) {
                    CHIPS[i][j] = new Chip(chips[i][j].getColor(), chips[i][j].getCoordinates().getX(),
                            chips[i][j].getCoordinates().getY());
                }
            }
        }
    }

    public Chip[][] getChips() {
        return CHIPS;
    }

    public void addStartingChips() {
        // Adding chips to the board center
        addNewChip(new Chip(ChipColor.white, 4, 4));
        addNewChip(new Chip(ChipColor.white, 5, 5));
        addNewChip(new Chip(ChipColor.black, 4, 5));
        addNewChip(new Chip(ChipColor.black, 5, 4));
    }

    public void addNewChip(Chip newChip) {
        Coordinates chipCoordinates = newChip.getCoordinates();

        CHIPS[chipCoordinates.getX() - 1][chipCoordinates.getY() - 1] = newChip;
    }
}
