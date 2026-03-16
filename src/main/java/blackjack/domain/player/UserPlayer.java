package blackjack.domain.player;

import blackjack.domain.card.Card;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UserPlayer implements Participant{
    private final List<Card> cards = new ArrayList<>();

    public UserPlayer() {
    }

    @Override
    public void receiveCard(Card card) {
        cards.add(card);
    }

    public int getCurrentCardSize() {
        return cards.size();
    }

    public List<Card> getCards() {
        return Collections.unmodifiableList(cards);
    }
}
