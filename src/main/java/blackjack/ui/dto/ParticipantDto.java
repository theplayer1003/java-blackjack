package blackjack.ui.dto;

import blackjack.domain.card.Card;
import blackjack.domain.player.Dealer;
import blackjack.domain.player.UserPlayer;
import java.util.List;

public record ParticipantDto(String name, List<String> cards) {

    public static ParticipantDto fromPlayer(UserPlayer userPlayer) {
        return new ParticipantDto(userPlayer.getName().name(), toCardStrings(userPlayer.getCards()));
    }

    public static ParticipantDto fromDealerInitialDraw(Dealer dealer) {
        Card firstCard = dealer.getFirstCard();
        return new ParticipantDto("딜러", toCardStrings(List.of(firstCard)));
    }

    private static List<String> toCardStrings(List<Card> cards) {
        return cards.stream()
                .map(card -> card.getRankName() + card.getSuitName())
                .toList();
    }
}
