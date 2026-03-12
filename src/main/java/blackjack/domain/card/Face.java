package blackjack.domain.card;

import java.util.function.Predicate;
import java.util.function.ToIntFunction;

public enum Face implements Rank {
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
    private final ToIntFunction<Integer> scoreRule;

    Face(String displayName, ToIntFunction<Integer> scoreRule) {
        this.displayName = displayName;
        this.scoreRule = scoreRule;
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }

    @Override
    public int getScore(int currentTotal) {
        return scoreRule.applyAsInt(currentTotal);
    }
}
