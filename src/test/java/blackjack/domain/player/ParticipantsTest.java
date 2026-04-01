package blackjack.domain.player;

import static blackjack.fixture.BlackjackFixture.createDealer;
import static blackjack.fixture.BlackjackFixture.createDefaultUserPlayer;
import static java.util.List.of;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import blackjack.domain.card.Card;
import blackjack.domain.card.Deck;
import blackjack.domain.card.FaceRank;
import blackjack.domain.card.NumberRank;
import blackjack.domain.card.StandardDeck;
import blackjack.domain.card.Suit;
import blackjack.fixture.BlackjackFixture;
import java.util.List;
import org.junit.jupiter.api.Test;

class ParticipantsTest {

//    @Test
//    void initHand_Always_DecreasesDeckSizeAndIncreasesHandSize() {
//        Dealer dealer = createDealer();
//        List<UserPlayer> oneUserPlayerList = of(createDefaultUserPlayer());
//        StandardDeck deck = new StandardDeck();
//
//        Participants participants = new Participants(dealer, oneUserPlayerList);
//        participants.initHand(deck);
//
//        assertThat(deck.getCurrentSize()).isEqualTo(52 - (participants.getAllParticipantsSize() * 2));
//        assertThat(dealer.getCurrentCardSize()).isEqualTo(2);
//        assertThat(oneUserPlayerList.getFirst().getCurrentCardSize()).isEqualTo(2);
//    }

    @Test
    void give() {
        Participants onePlayerParticipants = BlackjackFixture.createOnePlayerParticipants();

        //onePlayerParticipants.
    }

    @Test
    void constructor_ThrowsIAE_DealerIsNull() {
        assertThatThrownBy(() -> new Participants(null, of(createDefaultUserPlayer())))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("참가자들 중 딜러는 반드시 1명 존재해야 합니다");
    }

    @Test
    void constructor_ThrowsIAE_PlayersIsNull() {
        assertThatThrownBy(() -> new Participants(createDealer(), null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("플레이어는 null 이거나 비어있을 수 없습니다");
    }

    @Test
    void constructor_ThrowsIAE_PlayersIsEmpty() {
        assertThatThrownBy(() -> new Participants(createDealer(), List.of()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("플레이어는 null 이거나 비어있을 수 없습니다");
    }

    @Test
    void constructor_ThrowsIAE_PlayersSizeExceedsMaximum() {
        assertThatThrownBy(() -> new Participants(createDealer(), of(
                new UserPlayer(new Name("pobi1")),
                new UserPlayer(new Name("pobi2")),
                new UserPlayer(new Name("pobi3")),
                new UserPlayer(new Name("pobi4")),
                new UserPlayer(new Name("pobi5")),
                new UserPlayer(new Name("pobi6")),
                new UserPlayer(new Name("pobi7")),
                new UserPlayer(new Name("pobi8")),
                new UserPlayer(new Name("pobi9"))
        )))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("플레이어는 최대 8명까지 참가할 수 있습니다");
    }

    @Test
    void constructor_ThrowsIAE_PlayersNameDuplicated() {
        assertThatThrownBy(() -> new Participants(createDealer(), of(
                new UserPlayer(new Name("pobi")),
                new UserPlayer(new Name("pobi"))
        )))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("참여자들의 이름은 중복될 수 없습니다");
    }

    @Test
    void isDealerDrawable_ReturnsTrue_WhenDealerScoreIs16OrUnder() {
        Participants participants = BlackjackFixture.createOnePlayerParticipants();
        Dealer dealer = participants.getDealer();
        dealer.receiveCard(new Card(FaceRank.KING, Suit.DIAMOND));
        dealer.receiveCard(new Card(new NumberRank(6), Suit.DIAMOND));

        assertThat(participants.isDealerDrawable()).isTrue();
    }

    @Test
    void isDealerDrawable_ReturnsFalse_WhenDealerScoreIs16Over() {
        Participants participants = BlackjackFixture.createOnePlayerParticipants();
        Dealer dealer = participants.getDealer();
        dealer.receiveCard(new Card(FaceRank.KING, Suit.DIAMOND));
        dealer.receiveCard(new Card(new NumberRank(7), Suit.DIAMOND));

        assertThat(participants.isDealerDrawable()).isFalse();
    }

    @Test
    void drawDealerCard_DrawOneCard_WhenDealerisDrawable() {
        Participants participants = BlackjackFixture.createOnePlayerParticipants();
        Dealer dealer = participants.getDealer();
        dealer.receiveCard(new Card(FaceRank.KING, Suit.DIAMOND));
        dealer.receiveCard(new Card(new NumberRank(6), Suit.DIAMOND));
        Deck deck = new StandardDeck();

        participants.drawDealerCard(deck.draw());

        assertThat(participants.getDealer().getCurrentCardSize()).isEqualTo(3);
    }

    @Test
    void drawDealerCard_ThrowsISE_WhenDealerisNotDrawable() {
        Participants participants = BlackjackFixture.createOnePlayerParticipants();
        Dealer dealer = participants.getDealer();
        dealer.receiveCard(new Card(FaceRank.KING, Suit.DIAMOND));
        dealer.receiveCard(new Card(new NumberRank(7), Suit.DIAMOND));
        Deck deck = new StandardDeck();

        assertThatThrownBy(() -> participants.drawDealerCard(deck.draw()))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("딜러는 17점 이상이므로 카드를 더 이상 받을 수 없습니다");
    }
}