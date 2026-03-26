package blackjack.domain.player;

import blackjack.domain.card.Card;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Participants {
    private static final int INITIAL_DISTRIBUTION_COUNT = 2;

    private final Dealer dealer;
    private final List<UserPlayer> players;

    public Participants(Dealer dealer, List<UserPlayer> players) {
        validateDealerNotNull(dealer);
        validatePlayersNotEmpty(players);
        validatePlayersSize(players);
        validateNoDuplicateNames(players);

        this.dealer = dealer;
        this.players = new ArrayList<>(players);
    }

    private void validateDealerNotNull(Dealer dealer) {
        if (dealer == null) {
            throw new IllegalArgumentException("참가자들 중 딜러는 반드시 1명 존재해야 합니다");
        }
    }

    private void validatePlayersNotEmpty(List<UserPlayer> players) {
        if (players == null || players.isEmpty()) {
            throw new IllegalArgumentException("플레이어는 null 이거나 비어있을 수 없습니다");
        }
    }

    private void validatePlayersSize(List<UserPlayer> players) {
        if (players.size() > 8) {
            throw new IllegalArgumentException("플레이어는 최대 8명까지 참가할 수 있습니다");
        }
    }

    private void validateNoDuplicateNames(List<UserPlayer> players) {
        final Set<String> uniqueNames = players.stream()
                .map(player -> player.getName().name())
                .collect(Collectors.toSet());

        if (uniqueNames.size() != players.size()) {
            throw new IllegalArgumentException("참여자들의 이름은 중복될 수 없습니다");
        }
    }

//    public void initHand(Deck deck) {
//        for (int i = 0; i < INITIAL_DISTRIBUTION_COUNT; i++) {
//            allPlayerReceiveOneCard(deck);
//        }
//    }
//
//    private void allPlayerReceiveOneCard(Deck deck) {
//        for (Participant participant : getAllParticipants()) {
//            participant.receiveCard(deck.draw());
//        }
//    }
//
//    private List<Participant> getAllParticipants() {
//        List<Participant> all = new ArrayList<>();
//        all.add(dealer);
//        all.addAll(players);
//        return all;
//    }

    public int getAllParticipantsSize() {
        return players.size() + 1;
    }

    public List<String> getPlayerNames() {
        return players.stream()
                .map(player -> player.getName().name())
                .toList();
    }

    public boolean isDealerDrawable() {
        return dealer.isDrawable();
    }

    public void drawDealerCard(Card card) {
        if (!dealer.isDrawable()) {
            throw new IllegalStateException("딜러는 17점 이상이므로 카드를 더 이상 받을 수 없습니다");
        }
        dealer.receiveCard(card);
    }

    public Dealer getDealer() {
        return dealer;
    }

    public List<UserPlayer> getPlayers() {
        return Collections.unmodifiableList(players);
    }

    public void giveCardToDealer(Card drawCard) {
        dealer.receiveCard(drawCard);
    }
}
