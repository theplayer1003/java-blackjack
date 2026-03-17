package blackjack.domain;

import blackjack.domain.card.Deck;
import blackjack.domain.player.Participant;
import blackjack.domain.player.Participants;
import java.util.List;

public class BlackjackGame {
    private final Deck deck;
    private final Participants participants;

    public BlackjackGame(Deck deck, Participant[] participants) {
        this.deck = deck;
        this.participants = new Participants(List.of(participants));
    }

    public void distributeTwoCards() {
        participants.initHand(deck);
    }
}
