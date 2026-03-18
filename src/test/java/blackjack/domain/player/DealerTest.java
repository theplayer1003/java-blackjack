package blackjack.domain.player;

import static org.assertj.core.api.Assertions.assertThat;

import blackjack.domain.card.Card;
import blackjack.domain.card.NumberRank;
import blackjack.domain.card.Suit;
import blackjack.fixture.BlackjackFixture;
import org.junit.jupiter.api.Test;

class DealerTest {

    @Test
    void receiveCard_Always_DelegatesCardStorageToHand() {
        Dealer dealer = BlackjackFixture.createDealer();
        Card targetCard = new Card(new NumberRank(2), Suit.CLOVER);

        dealer.receiveCard(targetCard);

        assertThat(dealer.getCurrentCardSize()).isEqualTo(1);
        assertThat(dealer.getCards().contains(targetCard)).isTrue();
    }

    @Test
    void constructor_DealerNameIsAlways_딜러() {
        Dealer dealer = BlackjackFixture.createDealer();

        assertThat(dealer.getName()).isEqualTo("딜러");
    }
}