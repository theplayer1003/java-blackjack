package blackjack.domain.card;

import java.util.Objects;

public class Card {
    private final Rank rank;
    private final Suit suit;

    public Card(Rank rank, Suit suit) {
        this.rank = rank;
        this.suit = suit;
    }

    public String getRankName() {
        return rank.getDisplayName();
    }

    public String getSuitName() {
        return suit.getDisplayName();
    }

    public int getScore() {
        return rank.getScore();
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Card card)) {
            return false;
        }
        return Objects.equals(rank, card.rank) && suit == card.suit;
    }

    @Override
    public int hashCode() {
        return Objects.hash(rank, suit);
    }
}
