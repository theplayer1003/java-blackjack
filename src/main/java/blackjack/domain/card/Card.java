package blackjack.domain.card;

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

    public int getScore(int currentTotalScore) {
        return rank.getScore(currentTotalScore);
    }
}
