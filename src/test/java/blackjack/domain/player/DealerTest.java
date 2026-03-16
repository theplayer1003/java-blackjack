package blackjack.domain.player;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import blackjack.domain.card.Card;
import blackjack.domain.card.NumberRank;
import blackjack.domain.card.Suit;
import org.junit.jupiter.api.Test;

class DealerTest {
    
    @Test
    void receiveCard_Always_DelegatesCardStorageToHand(){
        Dealer dealer = new Dealer();
        Card targetCard = new Card(new NumberRank(2), Suit.CLOVER);

        dealer.receiveCard(targetCard);

        assertThat(dealer.getCurrentCardSize()).isEqualTo(1);
        assertThat(dealer.getCards().contains(targetCard)).isTrue();
    }
}