package martinez1337.Reversi.Models;

public class Chip {
    private ChipColor color;
    private final Coordinates coordinates;

    public Chip(ChipColor color, int x, int y) {
        this.color = color;
        coordinates = new Coordinates(x, y);
    }

    public ChipColor getColor() {
        return color;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void reverseColor() {
        if (color == ChipColor.black) {
            color = ChipColor.white;
        } else {
            color = ChipColor.black;
        }
    }

    @Override
    public String toString() {
        if (color == ChipColor.black) {
            return "\u25EF";
        } else {
            return "\u25CF";
        }
    }
}
