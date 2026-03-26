package blackjack.domain;

public enum GameResult {
    WIN,
    DRAW,
    LOSE;

    public static GameResult of(int playerScore, int dealerScore, boolean isPlayerBusted, boolean isDealerBusted) {
        if (isPlayerBusted) {
            return LOSE;
        }
        if (isDealerBusted) {
            return WIN;
        }
        if (dealerScore > playerScore) {
            return LOSE;
        }
        if (dealerScore == playerScore) {
            return DRAW;
        }
        return WIN;
    }
}
