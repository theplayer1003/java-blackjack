package blackjack.domain.card;

public class NumberRank implements Rank{
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
    public int getScore(int currentTotalScore) {
        return number;
    }

    @Override
    public String getDisplayName() {
        return String.valueOf(number);
    }
}
