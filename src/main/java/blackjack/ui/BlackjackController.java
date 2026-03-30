package blackjack.ui;

import blackjack.domain.BlackjackGame;

public class BlackjackController {
    private final BlackjackGame blackjackGame;
    private final InputView inputView;
    private final Outputview outputview;

    public BlackjackController(BlackjackGame blackjackGame, InputView inputView, Outputview outputview) {
        this.blackjackGame = blackjackGame;
        this.inputView = inputView;
        this.outputview = outputview;
    }
}
