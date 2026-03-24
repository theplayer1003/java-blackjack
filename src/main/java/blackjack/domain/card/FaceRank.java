package blackjack.domain.card;

public enum FaceRank implements Rank {
    KING("K", 10),
    QUEEN("Q", 10),
    JACK("J", 10),
    ACE("A", 11);

    private final String displayName;
    private final int score;

    FaceRank(String displayName, int score) {
        this.displayName = displayName;
        this.score = score;
    }

    @Override
    public int getScore() {
        return score;
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }

    @Override
    public boolean isAce() {
        return this == ACE;
    }
}
