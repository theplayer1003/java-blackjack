package blackjack.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import blackjack.domain.card.Card;
import blackjack.domain.card.Deck;
import blackjack.domain.card.FaceRank;
import blackjack.domain.card.NumberRank;
import blackjack.domain.card.StandardDeck;
import blackjack.domain.card.Suit;
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

    @Test
    void isDealerDrawable_ReturnsTrue_WhenDelegatedToParticipantsScore16() {
        Participants participants = BlackjackFixture.createOnePlayerParticipants();
        Dealer dealer = participants.getDealer();
        dealer.receiveCard(new Card(FaceRank.KING, Suit.DIAMOND));
        dealer.receiveCard(new Card(new NumberRank(6), Suit.DIAMOND));

        BlackjackGame blackjackGame = new BlackjackGame(new StandardDeck(), participants);

        assertThat(blackjackGame.isDealerDrawable()).isTrue();
    }

    @Test
    void isDealerDrawable_ReturnsFalse_WhenDelegatedToParticipantsScore17() {
        Participants participants = BlackjackFixture.createOnePlayerParticipants();
        Dealer dealer = participants.getDealer();
        dealer.receiveCard(new Card(FaceRank.KING, Suit.DIAMOND));
        dealer.receiveCard(new Card(new NumberRank(7), Suit.DIAMOND));

        BlackjackGame blackjackGame = new BlackjackGame(new StandardDeck(), participants);

        assertThat(blackjackGame.isDealerDrawable()).isFalse();
    }

    @Test
    void drawDealerCard_IncreaseDealerCardSize_WhenDealerScoreIs16() {
        Participants participants = BlackjackFixture.createOnePlayerParticipants();
        Dealer dealer = participants.getDealer();
        dealer.receiveCard(new Card(FaceRank.KING, Suit.DIAMOND));
        dealer.receiveCard(new Card(new NumberRank(6), Suit.DIAMOND));
        BlackjackGame blackjackGame = new BlackjackGame(new StandardDeck(), participants);

        blackjackGame.drawDealerCard();

        assertThat(dealer.getCurrentCardSize()).isEqualTo(3);
    }

    @Test
    void drawDealerCard_ThrowISE_WhenDealerScoreIs17OrOver() {
        Participants participants = BlackjackFixture.createOnePlayerParticipants();
        Dealer dealer = participants.getDealer();
        dealer.receiveCard(new Card(FaceRank.KING, Suit.DIAMOND));
        dealer.receiveCard(new Card(new NumberRank(7), Suit.DIAMOND));
        Deck deck = new StandardDeck();
        int initialDeckSize = deck.getCurrentSize();
        BlackjackGame blackjackGame = new BlackjackGame(deck, participants);

        assertThatThrownBy(() -> blackjackGame.drawDealerCard())
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("딜러는 17점 이상이므로 카드를 더 이상 받을 수 없습니다");

        assertThat(deck.getCurrentSize()).isEqualTo(initialDeckSize);
    }

    @Test
    void drawUserPlayerCard_IncreaseUserPlayerCardSize_WhenUserPlayerIsDrawable() {
        Participants participants = BlackjackFixture.createOnePlayerParticipants();
        List<UserPlayer> players = participants.getPlayers();
        UserPlayer player = players.getFirst();
        player.receiveCard(new Card(FaceRank.KING, Suit.DIAMOND));
        player.receiveCard(new Card(new NumberRank(7), Suit.DIAMOND));
        BlackjackGame blackjackGame = new BlackjackGame(new StandardDeck(), participants);

        blackjackGame.drawUserPlayerCard(player);

        assertThat(player.getCurrentCardSize()).isEqualTo(3);
    }

    @Test
    void drawUserPlayerCard_ThrowISE_WhenUserPlayerBusted() {
        Participants participants = BlackjackFixture.createOnePlayerParticipants();
        List<UserPlayer> players = participants.getPlayers();
        UserPlayer player = players.getFirst();
        player.receiveCard(new Card(new NumberRank(10), Suit.DIAMOND));
        player.receiveCard(new Card(new NumberRank(10), Suit.DIAMOND));
        player.receiveCard(new Card(new NumberRank(10), Suit.DIAMOND));
        Deck deck = new StandardDeck();
        int initialDeckSize = deck.getCurrentSize();
        BlackjackGame blackjackGame = new BlackjackGame(deck, participants);

        assertThatThrownBy(() -> blackjackGame.drawUserPlayerCard(player))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Bust 상태에서는 카드를 더 받을 수 없습니다");

        assertThat(deck.getCurrentSize()).isEqualTo(initialDeckSize);
    }
}
