package blackjack.domain;

import blackjack.domain.card.Card;
import blackjack.domain.card.Deck;
import blackjack.domain.player.Player;

public class BlackjackGame {
    private final Deck deck;
    private final Player player;

    public BlackjackGame(Deck deck, Player player) {
        this.deck = deck;
        this.player = player;
    }

    public void start() {
        Card firstCard = deck.draw();
        player.receiveCard(firstCard);

        Card secondCard = deck.draw();
        player.receiveCard(secondCard);
    }
}
