package blackjack.domain.card;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class StandardDeck implements Deck {
    private final List<Card> cards = new ArrayList<>();

    public StandardDeck() {
        initializeCards();
        Collections.shuffle(cards);
    }

    StandardDeck(List<Card> fixedCards) {
        this.cards.addAll(List.copyOf(fixedCards));
    }

    private void initializeCards() {
        for (Suit suit : Suit.values()) {

            for (int i = 2; i <= 10; i++) {
                cards.add(new Card(new NumberRank(i), suit));
            }

            for (FaceRank face : FaceRank.values()) {
                cards.add(new Card(face, suit));
            }
        }
    }

    @Override
    public Card draw() {
        if (cards.isEmpty()) {
            throw new IllegalStateException("덱에 더 이상 카드가 남아있지 않습니다");
        }
        return cards.removeFirst();
    }

    public int getCurrentSize() {
        return cards.size();
    }

    public List<Card> getCards() {
        return cards;
    }
}
