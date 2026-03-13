package blackjack.domain.card;

import java.util.function.ToIntFunction;

public enum FaceRank implements Rank {
    KING("K", total -> 10),
    QUEEN("Q", total -> 10),
    JACK("J", total -> 10),
    ACE("A", total -> {
        if (total <= 10) {
            return 11;
        }
        return 1;
    });

    private final String displayName;
    private final ToIntFunction<Integer> aceRule;

    FaceRank(String displayName, ToIntFunction<Integer> aceRule) {
        this.displayName = displayName;
        this.aceRule = aceRule;
    }

    @Override
    public int getScore(int currentTotalScore) {
        return aceRule.applyAsInt(currentTotalScore);
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }
}
