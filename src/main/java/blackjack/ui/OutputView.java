package blackjack.ui;

import blackjack.ui.dto.DealerResultDto;
import blackjack.ui.dto.FinalScoreDto;
import blackjack.ui.dto.GameResultDto;
import blackjack.ui.dto.InitialDistributionDto;
import blackjack.ui.dto.ParticipantDto;
import blackjack.ui.dto.ParticipantScoreDto;
import blackjack.ui.dto.PlayerResultDto;
import java.util.List;
import java.util.stream.Collectors;

public class OutputView {

    public void printInitialDistribution(InitialDistributionDto initialDistributionDto) {
        String formatted = formatInitialDistribution(initialDistributionDto);
        System.out.println(formatted);
    }

    String formatInitialDistribution(InitialDistributionDto initialDistributionDto) {
        ParticipantDto dealer = initialDistributionDto.dealer();
        List<ParticipantDto> participantDtos = initialDistributionDto.userPlayers();

        String header = "딜러와 "
                + participantDtos.stream()
                .map(ParticipantDto::name)
                .collect(Collectors.joining(", "))
                + "에게 2장을 나누었습니다.";

        String dealerPart = dealer.name() + ": " + String.join(", ", dealer.cards());

        String playersPart = participantDtos.stream()
                .map(this::formatPlayerCards)
                .collect(Collectors.joining("\n"));

        return header + "\n" + dealerPart + "\n" + playersPart;
    }

    public void printPlayerCards(ParticipantDto participantDto) {
        String formatted = formatPlayerCards(participantDto);
        System.out.println(formatted);
    }

    String formatPlayerCards(ParticipantDto participantDto) {
        return participantDto.name() + "카드: " + String.join(", ", participantDto.cards());
    }

    public void printDealerHit() {
        System.out.println(formatDealerHit());
    }

    String formatDealerHit() {
        return "딜러는 16이하라 한장의 카드를 더 받았습니다.";
    }

    public void printFinalHandsAndScores(FinalScoreDto finalScoreDto) {
        String formatted = formatFinalHandsAndScores(finalScoreDto);
        System.out.println(formatted);
    }

    String formatFinalHandsAndScores(FinalScoreDto finalScoreDto) {
        ParticipantScoreDto dealer = finalScoreDto.dealer();
        List<ParticipantScoreDto> participantScoreDtos = finalScoreDto.userPlayers();

        String dealerPart = dealer.name() + " 카드: " + String.join(", ", dealer.cards()) + " - 결과: " + dealer.score();

        String playersPart = participantScoreDtos.stream()
                .map(this::formatPlayerCards)
                .collect(Collectors.joining("\n"));

        return dealerPart + "\n" + playersPart;
    }

    String formatPlayerCards(ParticipantScoreDto participantScoreDto) {
        return participantScoreDto.name() + "카드: " + String.join(", ", participantScoreDto.cards()) + " - 결과: "
                + participantScoreDto.score();
    }

    public void printFinalWinningResult(GameResultDto gameResultDto) {
        String formatted = formatFinalWinningResult(gameResultDto);
        System.out.println(formatted);
    }

    String formatFinalWinningResult(GameResultDto gameResultDto) {
        String dealerPart = formatDealerResult(gameResultDto.dealerResult());

        String playerPart = gameResultDto.playerResults().stream()
                .map(this::formatPlayerResult)
                .collect(Collectors.joining("\n"));

        return "## 최종 승패\n\n" + dealerPart + "\n" + playerPart;
    }

    private String formatDealerResult(DealerResultDto dealerResultDto) {
        if (dealerResultDto.tieCount() > 0) {
            return "딜러: " + dealerResultDto.winCount() + "승 " + dealerResultDto.tieCount() + "무 "
                    + dealerResultDto.loseCount() + "패";
        }

        return "딜러: " + dealerResultDto.winCount() + "승 " + dealerResultDto.loseCount() + "패";
    }

    private String formatPlayerResult(PlayerResultDto playerResultDto) {
        return playerResultDto.name() + ": " + playerResultDto.result();
    }
}
