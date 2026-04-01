package blackjack.ui;

import blackjack.domain.BlackjackGame;
import blackjack.domain.GameResult;
import blackjack.domain.card.StandardDeck;
import blackjack.domain.player.Dealer;
import blackjack.domain.player.Name;
import blackjack.domain.player.Participants;
import blackjack.domain.player.UserPlayer;
import blackjack.ui.dto.FinalScoreDto;
import blackjack.ui.dto.GameResultDto;
import blackjack.ui.dto.InitialDistributionDto;
import blackjack.ui.dto.ParticipantDto;
import blackjack.ui.view.InputView;
import blackjack.ui.view.OutputView;
import java.util.List;

public class BlackjackController {
    private final InputView inputView;
    private final OutputView outputView;

    public BlackjackController(InputView inputView, OutputView outputView) {
        this.inputView = inputView;
        this.outputView = outputView;
    }

    public void run() {
        BlackjackGame blackjackGame = initGame();
        outputView.printEmptyLine();

        blackjackGame.distributeTwoCards();
        InitialDistributionDto initialDistributionDto = InitialDistributionDto.of(blackjackGame.getParticipants());
        outputView.printInitialDistribution(initialDistributionDto);

        List<UserPlayer> userPlayerList = blackjackGame.getUserPlayerList();
        for (UserPlayer userPlayer : userPlayerList) {
            checkHitOrStand(userPlayer, blackjackGame);
        }
        outputView.printEmptyLine();

        while (blackjackGame.isDealerDrawable()) {
            blackjackGame.drawDealerCard();
            outputView.printDealerHit();
        }
        outputView.printEmptyLine();

        FinalScoreDto finalScoreDto = FinalScoreDto.of(blackjackGame.getParticipants());
        outputView.printFinalHandsAndScores(finalScoreDto);

        GameResult gameResult = blackjackGame.calculateGameResult();
        outputView.printFinalWinningResult(GameResultDto.of(gameResult));
    }

    private void checkHitOrStand(UserPlayer userPlayer, BlackjackGame blackjackGame) {
        while (userPlayer.isDrawable() && inputView.inputHitOrStand(userPlayer.getName().name())) {
            blackjackGame.drawUserPlayerCard(userPlayer);
            outputView.printPlayerCards(ParticipantDto.fromPlayer(userPlayer));
        }
    }

    private BlackjackGame initGame() {
        List<Name> userNames = toNames(inputView.inputPlayerNames());
        Participants participants = createPlayers(userNames);

        return new BlackjackGame(new StandardDeck(), participants);
    }

    private Participants createPlayers(List<Name> userNames) {
        List<UserPlayer> players = userNames.stream()
                .map(UserPlayer::new)
                .toList();

        Dealer dealer = new Dealer(new Name("딜러"));

        return new Participants(dealer, players);
    }

    private List<Name> toNames(List<String> userPlayerNames) {
        return userPlayerNames.stream()
                .map(Name::new)
                .toList();
    }
}
