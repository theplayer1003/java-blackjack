package blackjack.domain.card;

public enum Number implements Rank {
    TWO("2", 2),
    THREE("3", 3),
    FOUR("4", 4),
    FIVE("5", 5),
    SIX("6", 6),
    SEVEN("7", 7),
    EIGHT("8", 8),
    NINE("9", 9),
    TEN("10", 10);

    private final String displayName;
    private final int score;

    Number(String displayName, int score) {
        this.displayName = displayName;
        this.score = score;
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }

    @Override
    public int getScore(int currentTotal) {
        return score;
    }
}
