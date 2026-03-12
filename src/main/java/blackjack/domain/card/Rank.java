package blackjack.domain.card;

public interface Rank {
    String getDisplayName();
    int getScore(int currentTotal);
}
