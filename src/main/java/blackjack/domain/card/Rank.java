package blackjack.domain.card;

public interface Rank {
    int getScore(int currentTotalScore);

    String getDisplayName();
}
