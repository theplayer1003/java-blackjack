package blackjack.fixture;

import blackjack.domain.player.Dealer;
import blackjack.domain.player.Name;
import blackjack.domain.player.Participants;
import blackjack.domain.player.UserPlayer;
import java.util.List;

public class BlackjackFixture {
    public static UserPlayer createDefaultUserPlayer() {
        return new UserPlayer(new Name("defaultName"));
    }

    public static Dealer createDealer() {
        return new Dealer((new Name("딜러")));
    }

    public static Participants createOnePlayerParticipants() {
        return new Participants(createDealer(), List.of(createDefaultUserPlayer()));
    }
}
