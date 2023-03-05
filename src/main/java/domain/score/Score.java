package domain.score;

import domain.player.DealerStatus;

public class Score {

    private static final int BUSTED_SCORE = -1;
    private static final int MAX_SCORE = 21;
    private final int value;

    private Score(final int value) {
        this.value = value;
    }

    public static Score from(final int value) {
        if (value > MAX_SCORE) {
            return new Score(BUSTED_SCORE);
        }
        return new Score(value);
    }

    public DealerStatus compareScore(final Score score) {
        if (this.value > score.value) {
            return DealerStatus.WIN;
        }
        return DealerStatus.LOSE;
    }

    public int getValue() {
        return value;
    }

    public boolean isBusted() {
        return value == BUSTED_SCORE;
    }
}
