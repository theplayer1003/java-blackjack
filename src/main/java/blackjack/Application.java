package blackjack;

import blackjack.ui.BlackjackController;
import blackjack.ui.view.InputView;
import blackjack.ui.view.OutputView;
import java.util.Scanner;

public class Application {

    public static void main(String[] args) {
        BlackjackController blackjackController = new BlackjackController(new InputView(new Scanner(System.in)),
                new OutputView());

        blackjackController.run();
    }
}
