package blackjack.ui;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

import blackjack.ui.dto.FinalScoreDto;
import blackjack.ui.dto.GameResultDto;
import blackjack.ui.dto.InitialDistributionDto;
import blackjack.ui.dto.ParticipantDto;
import blackjack.ui.dto.PlayerResultDto;
import blackjack.ui.view.InputView;
import blackjack.ui.view.OutputView;
import java.util.List;
import java.util.Scanner;
import org.junit.jupiter.api.Test;

class BlackjackControllerTest {

    @Test
    void run_ExecutesGameFlowCorrectly() {
        InputView stubInputView = new InputView(new Scanner(System.in)) {
            @Override
            public List<String> inputPlayerNames() {
                return List.of("pobi", "jason");
            }

            @Override
            public boolean inputHitOrStand(String playerName) {
                return false;
            }
        };

        var spyOutputView = new OutputView() {
            public GameResultDto capturedResult = null;

            @Override
            public void printInitialDistribution(InitialDistributionDto dto) {
            }

            @Override
            public void printPlayerCards(ParticipantDto dto) {
            }

            @Override
            public void printDealerHit() {
            }

            @Override
            public void printFinalHandsAndScores(FinalScoreDto dto) {
            }

            @Override
            public void printEmptyLine() {
            }

            @Override
            public void printFinalWinningResult(GameResultDto dto) {
                this.capturedResult = dto;
            }
        };

        BlackjackController blackjackController = new BlackjackController(stubInputView,
                spyOutputView);

        blackjackController.run();

        assertThat(spyOutputView.capturedResult).isNotNull();

        List<PlayerResultDto> playerResults = spyOutputView.capturedResult.playerResults();
        assertThat(playerResults).hasSize(2);
        assertThat(playerResults.get(0).name()).isEqualTo("pobi");
        assertThat(playerResults.get(1).name()).isEqualTo("jason");

        assertThat(spyOutputView.capturedResult.dealerResult()).isNotNull();
    }
}
