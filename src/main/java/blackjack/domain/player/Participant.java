package blackjack.domain.player;

import blackjack.domain.card.Card;

public interface Participant {
    void receiveCard(Card card);

    boolean isDrawable();
}
