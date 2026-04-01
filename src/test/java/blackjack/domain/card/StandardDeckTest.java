package blackjack.domain.card;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;

class StandardDeckTest {

    @Test
    void standardDeck_New_AlwaysHaveUnique52Cards() {
        StandardDeck standardDeck = new StandardDeck();

        int deckSize = standardDeck.getCurrentSize();

        assertThat(deckSize).isEqualTo(52);

        Set<Card> uniqueCards = new HashSet<>(standardDeck.getCards());
        assertThat(uniqueCards).hasSize(52);
    }

    @Test
    void draw_ReducesDeckSizeByOne_AndReturnsCard() {
        StandardDeck standardDeck = new StandardDeck();
        int initialSize = standardDeck.getCurrentSize();

        Card drawnCard = standardDeck.draw();

        assertThat(drawnCard).isNotNull();
        assertThat(standardDeck.getCurrentSize()).isEqualTo(initialSize - 1);
    }

    @Test
    void draw_Always_ReturnsTheTopCardOfDeck() {
        Card firstCard = new Card(new NumberRank(2), Suit.HEART);
        Card secondCard = new Card(FaceRank.ACE, Suit.HEART);
        List<Card> fixedCards = List.of(firstCard, secondCard);

        StandardDeck standardDeck = new StandardDeck(fixedCards);

        Card drawnFirstCard = standardDeck.draw();
        assertThat(drawnFirstCard).isEqualTo(firstCard);

        Card drawnSecondCard = standardDeck.draw();
        assertThat(drawnSecondCard).isEqualTo(secondCard);
    }

    @Test
    void draw_DeckIsEmpty_ThenThrowException() {
        List<Card> emptyCard = new ArrayList<>();
        StandardDeck standardDeck = new StandardDeck(emptyCard);

        assertThatThrownBy(() -> standardDeck.draw())
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("덱에 더 이상 카드가 남아있지 않습니다");
    }
}