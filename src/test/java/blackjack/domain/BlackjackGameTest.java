package blackjack.domain;

import static org.assertj.core.api.Assertions.assertThat;

import blackjack.domain.card.StandardDeck;
import blackjack.domain.player.Dealer;
import blackjack.domain.player.Participants;
import blackjack.domain.player.UserPlayer;
import blackjack.fixture.BlackjackFixture;
import java.util.List;
import org.junit.jupiter.api.Test;

public class BlackjackGameTest {

    @Test
    void distributeTwoCards_Always_Delegate() {
        Dealer dealer = BlackjackFixture.createDealer();
        List<UserPlayer> oneUserPlayerList = List.of(BlackjackFixture.createDefaultUserPlayer());
        Participants participants = new Participants(dealer, oneUserPlayerList);
        StandardDeck deck = new StandardDeck();

        BlackjackGame blackjackGame = new BlackjackGame(deck, participants);
        blackjackGame.distributeTwoCards();

        assertThat(deck.getCurrentSize()).isEqualTo(52 - (participants.getAllParticipantsSize() * 2));
        assertThat(dealer.getCurrentCardSize()).isEqualTo(2);
        assertThat(oneUserPlayerList.getFirst().getCurrentCardSize()).isEqualTo(2);
    }
}
