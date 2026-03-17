package blackjack.domain;

import blackjack.domain.card.Deck;
import blackjack.domain.player.Participant;
import java.util.ArrayList;
import java.util.List;

public class BlackjackGame {
    public static final int INITIAL_DISTRIBUTION_COUNT = 2;

    private final Deck deck;
    private final List<Participant> participants;

    public BlackjackGame(Deck deck, Participant[] participants) {
        this.deck = deck;
        this.participants = new ArrayList<>(List.of(participants));
    }

    public void distributeTwoCards() {
        for (int i = 0; i < INITIAL_DISTRIBUTION_COUNT; i++) {
            allPlayerReceiveOneCard();
        }
    }

    private void allPlayerReceiveOneCard() {
        for (Participant participant : participants) {
            participant.receiveCard(deck.draw());
        }
    }
}
