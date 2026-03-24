package blackjack.domain.card;

public interface Rank {
    int getScore();

    String getDisplayName();

    boolean isAce();
}
