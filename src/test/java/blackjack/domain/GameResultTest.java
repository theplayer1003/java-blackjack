package blackjack.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import blackjack.domain.card.Card;
import blackjack.domain.card.FaceRank;
import blackjack.domain.card.NumberRank;
import blackjack.domain.card.Suit;
import blackjack.domain.player.Dealer;
import blackjack.domain.player.Participants;
import blackjack.domain.player.UserPlayer;
import blackjack.fixture.BlackjackFixture;
import java.util.Map;
import org.junit.jupiter.api.Test;

class GameResultTest {

    @Test
    void calculateGameResults_PlayerWinAndDealerLose_WhenPlayerScoreBiggerThanDealerScore() {
        Participants participants = createParticipantsPlayer20Dealer16();
        UserPlayer player = participants.getPlayers().getFirst();

        GameResult gameResult = new GameResult(participants);

        Map<UserPlayer, WinningResult> playerResults = gameResult.getPlayerResults();
        Map<WinningResult, Integer> dealerResults = gameResult.getDealerResults();

        assertAll(
                () -> assertThat(playerResults.get(player)).isEqualTo(WinningResult.WIN),

                () -> assertThat(dealerResults.get(WinningResult.LOSE)).isEqualTo(1),
                () -> assertThat(dealerResults.get(WinningResult.WIN)).isEqualTo(0),
                () -> assertThat(dealerResults.get(WinningResult.DRAW)).isEqualTo(0)
        );
    }

    private Participants createParticipantsPlayer20Dealer16() {
        Participants participants = BlackjackFixture.createOnePlayerParticipants();
        Dealer dealer = participants.getDealer();
        UserPlayer player = participants.getPlayers().getFirst();

        player.receiveCard(new Card(FaceRank.JACK, Suit.DIAMOND));
        player.receiveCard(new Card(FaceRank.QUEEN, Suit.DIAMOND));

        dealer.receiveCard(new Card(FaceRank.KING, Suit.CLOVER));
        dealer.receiveCard(new Card(new NumberRank(6), Suit.CLOVER));

        return participants;
    }
}