package blackjack.domain;

import blackjack.domain.player.Dealer;
import blackjack.domain.player.Hand;
import blackjack.domain.player.Participants;
import blackjack.domain.player.UserPlayer;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class GameResult {
    private final Map<UserPlayer, WinningResult> playerResults;

    public GameResult(Participants participants) {
        this.playerResults = calculateResults(participants);
    }

    private Map<UserPlayer, WinningResult> calculateResults(Participants participants) {
        Map<UserPlayer, WinningResult> results = new LinkedHashMap<>();

        Dealer dealer = participants.getDealer();
        Hand dealerHand = dealer.getHand();

        for (UserPlayer player : participants.getPlayers()) {
            Hand playerHand = player.getHand();
            WinningResult winningResult = WinningResult.of(dealerHand, playerHand);

            results.put(player, winningResult);
        }

        return results;
    }

    public Map<UserPlayer, WinningResult> getPlayerResults() {
        return Collections.unmodifiableMap(playerResults);
    }

    public Map<WinningResult, Integer> getDealerResults() {
        Map<WinningResult, Integer> dealerResults = new EnumMap<>(WinningResult.class);
        dealerResults.put(WinningResult.WIN, 0);
        dealerResults.put(WinningResult.DRAW, 0);
        dealerResults.put(WinningResult.LOSE, 0);

        for (WinningResult playerResult : playerResults.values()) {
            WinningResult dealerResult = reverse(playerResult);
            dealerResults.put(dealerResult, dealerResults.get(dealerResult) + 1);
        }

        return dealerResults;
    }

    private WinningResult reverse(WinningResult playerResult) {
        if (playerResult == WinningResult.WIN) {
            return WinningResult.LOSE;
        }
        if (playerResult == WinningResult.LOSE) {
            return WinningResult.WIN;
        }
        return WinningResult.DRAW;
    }
}
