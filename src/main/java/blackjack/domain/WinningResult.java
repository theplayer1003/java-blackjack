package blackjack.domain;

import blackjack.domain.player.Hand;

public enum WinningResult {
    WIN,
    DRAW,
    LOSE;

    public static WinningResult of(Hand dealerHand, Hand playerHand) {
        if (playerHand.isBusted()) {
            return LOSE;
        }

        if (dealerHand.isBusted()) {
            return WIN;
        }

        int dealerScore = dealerHand.calculateScore();
        int playerScore = playerHand.calculateScore();
        if (dealerScore > playerScore) {
            return LOSE;
        }
        if (dealerScore == playerScore) {
            return DRAW;
        }

        return WIN;
    }
}
