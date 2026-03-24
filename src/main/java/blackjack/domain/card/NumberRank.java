package blackjack.domain.card;

import java.util.Objects;

public class NumberRank implements Rank {
    private final int number;

    public NumberRank(int number) {
        validateNumberRange(number);
        this.number = number;
    }

    private void validateNumberRange(int number) {
        if (number < 2 || number > 10) {
            throw new IllegalArgumentException("숫자 카드는 2부터 10 사이여야 합니다.");
        }
    }

    @Override
    public int getScore() {
        return number;
    }

    @Override
    public String getDisplayName() {
        return String.valueOf(number);
    }

    @Override
    public boolean isAce() {
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof NumberRank that)) {
            return false;
        }
        return number == that.number;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(number);
    }
}
