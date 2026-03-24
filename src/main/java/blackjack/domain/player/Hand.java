package blackjack.domain.player;

import blackjack.domain.card.Card;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Hand {
    private final List<Card> cards = new ArrayList<>();

    public void add(Card card) {
        cards.add(card);
    }

    public int getSize() {
        return cards.size();
    }

    public List<Card> getCards() {
        return Collections.unmodifiableList(cards);
    }

    public int calculateScore() {
        int sum = cards.stream()
                .mapToInt(Card::getScore)
                .sum();

        long count = cards.stream()
                .filter(Card::isAce)
                .count();

        while (sum > 21 && count > 0) {
            sum -= 10;
            count--;
        }

        return sum;
    }

    public boolean isBusted() {
        return calculateScore() > 21;
    }

    public Card getFirstCard() {
        return cards.getFirst();
    }
}
