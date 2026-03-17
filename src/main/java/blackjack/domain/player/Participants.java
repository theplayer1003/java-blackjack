package blackjack.domain.player;

import blackjack.domain.card.Deck;
import java.util.List;

public class Participants {
    private static final int INITIAL_DISTRIBUTION_COUNT = 2;

    private final List<Participant> players;

    public Participants(List<Participant> players) {
        if (players.isEmpty()) {
            new IllegalArgumentException("게임 참여자가 존재 하지 않습니다");
        }
        this.players = players;
    }

    public void initHand(Deck deck) {
        for (int i = 0; i < INITIAL_DISTRIBUTION_COUNT; i++) {
            allPlayerReceiveOneCard(deck);
        }
    }

    private void allPlayerReceiveOneCard(Deck deck) {
        for (Participant participant : players) {
            participant.receiveCard(deck.draw());
        }
    }

}
