package blackjack.ui;

import blackjack.domain.BlackjackGame;

public class BlackjackController {
    private final BlackjackGame blackjackGame;
    private final InputView inputView;
    private final OutputView outputview;

    public BlackjackController(BlackjackGame blackjackGame, InputView inputView, OutputView outputview) {
        this.blackjackGame = blackjackGame;
        this.inputView = inputView;
        this.outputview = outputview;
    }
}
