package blackjack.domain.player;

import static org.assertj.core.api.Assertions.assertThat;

import blackjack.domain.card.StandardDeck;
import java.util.List;
import org.junit.jupiter.api.Test;

class ParticipantsTest {

    @Test
    void initHand_Always_DecreasesDeckSizeAndIncreasesHandSize() {
        Dealer dealer = new Dealer();
        UserPlayer userPlayer = new UserPlayer();
        Participant[] participantsAry = {dealer, userPlayer};
        StandardDeck deck = new StandardDeck();

        Participants participants = new Participants(List.of(participantsAry));
        participants.initHand(deck);

        assertThat(deck.getCurrentSize()).isEqualTo(52 - (participantsAry.length * 2));
        assertThat(dealer.getCurrentCardSize()).isEqualTo(2);
        assertThat(userPlayer.getCurrentCardSize()).isEqualTo(2);
    }

}