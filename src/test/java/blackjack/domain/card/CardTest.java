package blackjack.domain.card;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class CardTest {

    @Test
    void getRankName_Always_ReturnsDelegatedRankDisplayName() {
        Card card = new Card(new NumberRank(2), Suit.HEART);

        assertThat(card.getRankName()).isEqualTo("2");
    }

    @Test
    void getSuitName_Always_ReturnsDelegatedSuitDisplayName() {
        Card card = new Card(new NumberRank(2), Suit.HEART);

        assertThat(card.getSuitName()).isEqualTo("하트");
    }

    @Test
    void getScore_Always_ReturnsDelegatedRankScore() {
        Card card = new Card(new NumberRank(10), Suit.HEART);
        int anyCurrentTotalScore = 15;

        assertThat(card.getScore(anyCurrentTotalScore)).isEqualTo(10);
    }
}