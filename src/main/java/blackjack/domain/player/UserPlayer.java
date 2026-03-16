package blackjack.domain.player;

import blackjack.domain.card.Card;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UserPlayer implements Participant {
    private final Hand hand = new Hand();

    public UserPlayer() {
    }

    @Override
    public void receiveCard(Card card) {
        hand.add(card);
    }

    public int getCurrentCardSize() {
        return hand.getSize();
    }

    public List<Card> getCards() {
        return hand.getCards();
    }
}
