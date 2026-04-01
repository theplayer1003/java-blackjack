package blackjack.domain;

import blackjack.domain.card.Deck;
import blackjack.domain.player.Dealer;
import blackjack.domain.player.Participants;
import blackjack.domain.player.UserPlayer;
import java.util.List;

public class BlackjackGame {
    private final Deck deck;
    private final Participants participants;

    public BlackjackGame(Deck deck, Participants participants) {
        this.deck = deck;
        this.participants = participants;
    }

    public void distributeTwoCards() {
        participants.giveCardToDealer(deck.draw());
        participants.giveCardToDealer(deck.draw());

        for (UserPlayer player : participants.getPlayers()) {
            player.receiveCard(deck.draw());
            player.receiveCard(deck.draw());
        }
    }

    public boolean isDealerDrawable() {
        return participants.isDealerDrawable();
    }

    public void drawDealerCard() {
        if (!participants.isDealerDrawable()) {
            throw new IllegalStateException("딜러는 17점 이상이므로 카드를 더 이상 받을 수 없습니다");
        }
        participants.drawDealerCard(deck.draw());
    }

    public void drawUserPlayerCard(UserPlayer userPlayer) {
        if (!userPlayer.isDrawable()) {
            throw new IllegalStateException("Bust 상태에서는 카드를 더 받을 수 없습니다");
        }
        userPlayer.receiveCard(deck.draw());
    }

    public List<UserPlayer> getUserPlayerList() {
        return participants.getPlayers();
    }

    public Dealer getDealer() {
        return participants.getDealer();
    }

    public GameResult calculateGameResult() {
        return new GameResult(participants);
    }

    public Participants getParticipants() {
        return this.participants;
    }
}
