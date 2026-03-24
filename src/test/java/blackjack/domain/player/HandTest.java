package blackjack.domain.player;

import static org.assertj.core.api.Assertions.assertThat;

import blackjack.domain.card.Card;
import blackjack.domain.card.FaceRank;
import blackjack.domain.card.NumberRank;
import blackjack.domain.card.Suit;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class HandTest {

    private Hand hand;
    private Card targetCard;

    private static Stream<Arguments> provideCardsForScoreAndBustValidation() {
        return Stream.of(
                // 1. 순서 독립성 증명 (A가 먼저 계산될 때 vs 나중에 계산될 때)
                Arguments.of(List.of(
                        new Card(FaceRank.ACE, Suit.CLOVER), new Card(new NumberRank(9), Suit.HEART),
                        new Card(new NumberRank(2), Suit.DIAMOND)
                ), 12, false),
                Arguments.of(List.of(
                        new Card(new NumberRank(9), Suit.HEART), new Card(new NumberRank(2), Suit.DIAMOND),
                        new Card(FaceRank.ACE, Suit.CLOVER)
                ), 12, false),

                // 2. 다중 Ace 보정 증명 (A, A, 9 -> 11 + 1 + 9 = 21)
                Arguments.of(List.of(
                        new Card(FaceRank.ACE, Suit.CLOVER), new Card(FaceRank.ACE, Suit.HEART),
                        new Card(new NumberRank(9), Suit.DIAMOND)
                ), 21, false),

                // 3. 보정 후에도 Bust가 발생하는 케이스 (A, K, Q, 2 -> 1 + 10 + 10 + 2 = 23)
                Arguments.of(List.of(
                        new Card(FaceRank.ACE, Suit.CLOVER), new Card(FaceRank.KING, Suit.HEART),
                        new Card(FaceRank.QUEEN, Suit.DIAMOND), new Card(new NumberRank(2), Suit.SPADE)
                ), 23, true),

                // 4. 일반적인 카드 합산 케이스 (K, Q -> 20)
                Arguments.of(List.of(
                        new Card(FaceRank.KING, Suit.CLOVER), new Card(FaceRank.QUEEN, Suit.HEART)
                ), 20, false)
        );
    }

    @BeforeEach
    void setup() {
        hand = new Hand();
    }

    @Test
    void add_Always_AddsTheGivenCardToCards() {
        targetCard = new Card(FaceRank.ACE, Suit.CLOVER);
        hand.add(targetCard);

        assertThat(hand.getSize()).isEqualTo(1);
        assertThat(hand.getCards().contains(targetCard)).isTrue();
    }

    @ParameterizedTest(name = "카드가 주어졌을 때 총점은 {0} 점이며, Bust 상태는 {1} 이다")
    @MethodSource("provideCardsForScoreAndBustValidation")
    void calculateScore_And_isBusted_ValidatesCorrectly(List<Card> handCards, int expected, boolean expectedBustState) {
        for (Card handCard : handCards) {
            hand.add(handCard);
        }

        assertThat(hand.calculateScore()).isEqualTo(expected);
        assertThat(hand.isBusted()).isEqualTo(expectedBustState);
    }
}