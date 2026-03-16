package blackjack.domain;

import blackjack.domain.card.Card;
import blackjack.domain.card.Deck;
import blackjack.domain.player.Participant;

public class BlackjackGame {
    private final Deck deck;
    private final Participant participant;

    public BlackjackGame(Deck deck, Participant participant) {
        this.deck = deck;
        this.participant = participant;
    }

    public void start() {
        Card firstCard = deck.draw();
        participant.receiveCard(firstCard);

        Card secondCard = deck.draw();
        participant.receiveCard(secondCard);
    }
}
