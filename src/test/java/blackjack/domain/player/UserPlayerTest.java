package blackjack.domain.player;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import blackjack.domain.card.Card;
import blackjack.domain.card.NumberRank;
import blackjack.domain.card.Suit;
import org.junit.jupiter.api.Test;

class UserPlayerTest {

    @Test
    void receiveCard_Always_DelegatesCardStorageToHand(){
        UserPlayer userPlayer = new UserPlayer();
        Card targetCard = new Card(new NumberRank(2), Suit.CLOVER);

        userPlayer.receiveCard(targetCard);

        assertThat(userPlayer.getCurrentCardSize()).isEqualTo(1);

        assertThat(userPlayer.getCards().contains(targetCard)).isTrue();
    }
}