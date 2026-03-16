package blackjack.domain.player;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import blackjack.domain.card.Card;
import blackjack.domain.card.FaceRank;
import blackjack.domain.card.Suit;
import org.junit.jupiter.api.Test;

class HandTest {

    @Test
    void add_Always_AddsTheGivenCardToCards() {
        Hand hand = new Hand();
        Card targetCard = new Card(FaceRank.ACE, Suit.CLOVER);

        hand.add(targetCard);

        assertThat(hand.getSize()).isEqualTo(1);
        assertThat(hand.getCards().contains(targetCard)).isTrue();
    }
}