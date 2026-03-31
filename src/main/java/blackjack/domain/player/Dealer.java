package blackjack.domain.player;

import blackjack.domain.card.Card;
import java.util.List;

public class Dealer implements Participant {
    private final Name name;
    private final Hand hand = new Hand();

    public Dealer(Name name) {
        this.name = name;
    }

    @Override
    public void receiveCard(Card card) {
        hand.add(card);
    }

    public int getCurrentCardSize() {
        return hand.getSize();
    }

    @Override
    public List<Card> getCards() {
        return hand.getCards();
    }

    public String getName() {
        return name.name();
    }

    @Override
    public boolean isDrawable() {
        return hand.calculateScore() < 17;
    }

    public Card getFirstCard() {
        return hand.getFirstCard();
    }

    public Hand getHand() {
        return hand;
    }
}
