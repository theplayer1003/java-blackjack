package blackjack.domain;

import static org.assertj.core.api.Assertions.assertThat;

import blackjack.domain.card.StandardDeck;
import blackjack.domain.player.Dealer;
import blackjack.domain.player.Participant;
import blackjack.domain.player.UserPlayer;
import org.junit.jupiter.api.Test;

public class BlackjackGameTest {

    @Test
    void distributeTwoCards_Always_Delegate() {
        Dealer dealer = new Dealer();
        UserPlayer userPlayer = new UserPlayer();
        Participant[] participants = {dealer, userPlayer};
        StandardDeck deck = new StandardDeck();

        BlackjackGame blackjackGame = new BlackjackGame(deck, participants);
        blackjackGame.distributeTwoCards();

        assertThat(deck.getCurrentSize()).isEqualTo(52 - (participants.length * 2));
        assertThat(dealer.getCurrentCardSize()).isEqualTo(2);
        assertThat(userPlayer.getCurrentCardSize()).isEqualTo(2);
    }
}
