package martinez1337.Reversi.Models;

public class Score {
    private int whites;
    private int blacks;

    public Score() {
        whites = 2;
        blacks = 2;
    }

    public int getWhites() {
        return whites;
    }

    public int getBlacks() {
        return blacks;
    }

    public void update(int whites, int blacks) {
        this.whites = whites;
        this.blacks = blacks;
    }
}
