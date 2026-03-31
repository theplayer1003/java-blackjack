package blackjack.ui.dto;

import blackjack.domain.GameResult;
import blackjack.domain.WinningResult;
import java.util.List;
import java.util.Map;

public record GameResultDto(DealerResultDto dealerResult, List<PlayerResultDto> playerResults) {

    public static GameResultDto of(GameResult gameResult) {
        Map<WinningResult, Integer> dealerResults = gameResult.getDealerResults();

        int dealerWinCount = dealerResults.getOrDefault(WinningResult.WIN, 0);
        int dealerDrawCount = dealerResults.getOrDefault(WinningResult.DRAW, 0);
        int dealerLoseCount = dealerResults.getOrDefault(WinningResult.LOSE, 0);

        List<PlayerResultDto> playerResultDtos = gameResult.getPlayerResults().entrySet().stream()
                .map(entry -> new PlayerResultDto(
                        entry.getKey().getName().name(),
                        checkStatus(entry.getValue())
                ))
                .toList();

        final DealerResultDto dealerResultDto = new DealerResultDto(dealerWinCount, dealerDrawCount, dealerLoseCount);

        return new GameResultDto(dealerResultDto, playerResultDtos);
    }

    private static String checkStatus(WinningResult value) {
        return switch (value) {
            case WIN -> "승";
            case DRAW -> "무";
            case LOSE -> "패";
        };
    }
}
