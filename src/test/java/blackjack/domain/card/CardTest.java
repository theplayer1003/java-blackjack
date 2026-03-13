package blackjack.domain.card;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class CardTest {

    @Test
    void getRankName() {
        Card card = new Card("2", "하트");

        assertThat(card.getRankName()).isEqualTo("2");
    }

    @Test
    void getSuitName() {
        Card card = new Card("2", "하트");

        assertThat(card.getSuitName()).isEqualTo("하트");
    }

    @Test
    void getScore_Ace_ReturnDynamicScoreBasedOnBoundary() {
        Card card = new Card("A", "하트");
        int currentTotalScore10 = 10;
        int currentTotalScore11 = 11;

        assertThat(card.getScore(currentTotalScore10)).isEqualTo(11);
        assertThat(card.getScore(currentTotalScore11)).isEqualTo(1);
    }
}