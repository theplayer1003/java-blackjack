package blackjack.domain.player;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import blackjack.domain.card.Card;
import blackjack.domain.card.NumberRank;
import blackjack.domain.card.Suit;
import blackjack.fixture.BlackjackFixture;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class UserPlayerTest {

    @Test
    void receiveCard_Always_DelegatesCardStorageToHand() {
        UserPlayer userPlayer = BlackjackFixture.createDefaultUserPlayer();
        Card targetCard = new Card(new NumberRank(2), Suit.CLOVER);

        userPlayer.receiveCard(targetCard);

        assertThat(userPlayer.getCurrentCardSize()).isEqualTo(1);

        assertThat(userPlayer.getCards().contains(targetCard)).isTrue();
    }

    @ParameterizedTest
    @ValueSource(strings = {"딜러", "우테코딜러", "딜러우테코"})
    void constructor_ThrowsIllegalArgumentException_CanNotUseNameDealer(String invalidName) {
        assertThatThrownBy(() -> new UserPlayer(new Name(invalidName)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("플레이어 이름에 '딜러'를 사용할 수 없습니다");
    }
}