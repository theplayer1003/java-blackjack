package blackjack.ui;

import static org.assertj.core.api.Assertions.assertThat;

import blackjack.domain.card.Card;
import blackjack.domain.card.FaceRank;
import blackjack.domain.card.NumberRank;
import blackjack.domain.card.Suit;
import blackjack.domain.player.Dealer;
import blackjack.domain.player.Participants;
import blackjack.domain.player.UserPlayer;
import blackjack.fixture.BlackjackFixture;
import blackjack.ui.dto.DealerResultDto;
import blackjack.ui.dto.FinalScoreDto;
import blackjack.ui.dto.GameResultDto;
import blackjack.ui.dto.InitialDistributionDto;
import blackjack.ui.dto.ParticipantDto;
import blackjack.ui.dto.PlayerResultDto;
import java.util.List;
import org.junit.jupiter.api.Test;

class OutputViewTest {

    @Test
    void formatInitialDistribution_CreatesValidString() {
        OutputView outputView = new OutputView();

        final Dealer dealer = BlackjackFixture.createDealer();
        dealer.receiveCard(new Card(FaceRank.KING, Suit.DIAMOND));

        final UserPlayer defaultUserPlayer = BlackjackFixture.createDefaultUserPlayer();
        defaultUserPlayer.receiveCard(new Card(new NumberRank(7), Suit.DIAMOND));
        defaultUserPlayer.receiveCard(new Card(new NumberRank(6), Suit.DIAMOND));

        String formatted = outputView.formatInitialDistribution(InitialDistributionDto.of(
                new Participants(dealer, List.of(defaultUserPlayer))
        ));

        assertThat(formatted).isEqualTo("딜러와 defaultName에게 2장을 나누었습니다.\n딜러: K다이아몬드\ndefaultName카드: 7다이아몬드, 6다이아몬드");
    }

    @Test
    void formatPlayerCards_CreatesValidString() {
        OutputView outputView = new OutputView();

        UserPlayer defaultUserPlayer = BlackjackFixture.createDefaultUserPlayer();
        defaultUserPlayer.receiveCard(new Card(new NumberRank(7), Suit.DIAMOND));
        defaultUserPlayer.receiveCard(new Card(new NumberRank(6), Suit.DIAMOND));

        ParticipantDto participantDto = ParticipantDto.fromPlayer(defaultUserPlayer);

        String formatted = outputView.formatPlayerCards(participantDto);

        assertThat(formatted).isEqualTo("defaultName카드: 7다이아몬드, 6다이아몬드");
    }

    @Test
    void formatDealerHit_CreatesValidString() {
        OutputView outputView = new OutputView();

        assertThat(outputView.formatDealerHit()).isEqualTo("딜러는 16이하라 한장의 카드를 더 받았습니다.");
    }

    @Test
    void formatFinalHandsAndScores_CreatesValidString() {
        OutputView outputView = new OutputView();

        final Dealer dealer = BlackjackFixture.createDealer();
        dealer.receiveCard(new Card(FaceRank.KING, Suit.DIAMOND));
        dealer.receiveCard(new Card(FaceRank.KING, Suit.DIAMOND));

        final UserPlayer defaultUserPlayer = BlackjackFixture.createDefaultUserPlayer();
        defaultUserPlayer.receiveCard(new Card(new NumberRank(7), Suit.DIAMOND));
        defaultUserPlayer.receiveCard(new Card(new NumberRank(6), Suit.DIAMOND));

        String formatted = outputView.formatFinalHandsAndScores(FinalScoreDto.of(
                new Participants(dealer, List.of(defaultUserPlayer)))
        );

        assertThat(formatted).isEqualTo("딜러 카드: K다이아몬드, K다이아몬드 - 결과: 20\ndefaultName카드: 7다이아몬드, 6다이아몬드 - 결과: 13");
    }

    @Test
    void formatFinalWinningResult_CreatesValidString() {
        OutputView outputView = new OutputView();
        GameResultDto gameResultDto = new GameResultDto(
                new DealerResultDto(1, 0, 1), List.of(new PlayerResultDto("pobi", "승"),
                new PlayerResultDto("jason", "승")));

        String formatted = outputView.formatFinalWinningResult(gameResultDto);

        assertThat(formatted).isEqualTo("## 최종 승패\n\n딜러: 1승 1패\npobi: 승\njason: 승");
    }
}
