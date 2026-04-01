package blackjack.domain.card;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

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

        assertThat(card.getScore()).isEqualTo(10);
    }

    @ParameterizedTest
    @EnumSource(value = FaceRank.class, names = {
            "KING",
            "QUEEN",
            "JACK"
    })
    void isAce_ReturnsFalse_WhenFaceRankIsKQJ(FaceRank faceRank) {
        Card card = new Card(faceRank, Suit.CLOVER);

        assertThat(card.isAce()).isFalse();
    }

    @Test
    void isAce_ReturnsTrue_WhenFaceRankIsAce() {
        Card card = new Card(FaceRank.ACE, Suit.CLOVER);

        assertThat(card.isAce()).isTrue();
    }

    @Test
    void isAce_ReturnsFalse_WhenRankIsNumberRank() {
        Card card = new Card(new NumberRank(2), Suit.CLOVER);

        assertThat(card.isAce()).isFalse();
    }
}