package blackjack.domain.player;

import blackjack.domain.card.Card;
import java.util.List;

public interface Participant {
    void receiveCard(Card card);

    boolean isDrawable();

    List<Card> getCards();
}
