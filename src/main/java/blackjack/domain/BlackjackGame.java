package blackjack.domain;

import blackjack.domain.card.Deck;
import blackjack.domain.player.Participants;

public class BlackjackGame {
    private final Deck deck;
    private final Participants participants;

    public BlackjackGame(Deck deck, Participants participants) {
        this.deck = deck;
        this.participants = participants;
    }

    public void distributeTwoCards() {
        participants.initHand(deck);
    }
}
