package blackjack.domain.card;

public class Card {
    private final String rank;
    private final String suit;

    public Card(String rank, String suit) {
        this.rank = rank;
        this.suit = suit;
    }

    public String getRankName() {
        return rank;
    }

    public String getSuitName() {
        return suit;
    }

    public int getScore(int currentTotalScore) {
        if (rank.equals("K") || rank.equals("Q") || rank.equals("J")) {
            return 10;
        }

        if (rank.equals("A")) {
            if (currentTotalScore <= 10) {
                return 11;
            }
            return 1;
        }

        return Integer.parseInt(rank);
    }
}
