package blackjack.fixture;

import blackjack.domain.player.Dealer;
import blackjack.domain.player.Name;
import blackjack.domain.player.UserPlayer;

public class BlackjackFixture {
    public static UserPlayer createDefaultUserPlayer() {
        return new UserPlayer(new Name("defaultName"));
    }

    public static Dealer createDealer() {
        return new Dealer((new Name("딜러")));
    }
}
