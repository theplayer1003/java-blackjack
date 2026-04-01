package blackjack.ui.dto;

import blackjack.domain.card.Card;
import blackjack.domain.player.Dealer;
import blackjack.domain.player.UserPlayer;
import java.util.List;

public record ParticipantScoreDto(String name, List<String> cards, int score) {

    public static ParticipantScoreDto fromPlayer(UserPlayer userPlayer) {
        return new ParticipantScoreDto(userPlayer.getName().name(), toCardStrings(userPlayer.getCards()),
                userPlayer.getHand().calculateScore());
    }

    public static ParticipantScoreDto fromDealer(Dealer dealer) {
        return new ParticipantScoreDto("딜러", toCardStrings(dealer.getCards()), dealer.getHand().calculateScore());
    }

    private static List<String> toCardStrings(List<Card> cards) {
        return cards.stream()
                .map(card -> card.getRankName() + card.getSuitName())
                .toList();
    }
}
