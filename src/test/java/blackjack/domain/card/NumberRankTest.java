package blackjack.domain.card;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class NumberRankTest {

    @Test
    void getDisplayName_Always_ReturnsStringValueOfNumber() {
        NumberRank numberRank = new NumberRank(2);

        assertThat(numberRank.getDisplayName()).isEqualTo("2");
    }

    @Test
    void getScore_Always_ReturnsItsNumber() {
        NumberRank numberRank = new NumberRank(2);

        assertThat(numberRank.getScore()).isEqualTo(2);
    }

    @ParameterizedTest(name = "숫자 {0} 은(는) 유효한 범위로 객체가 정상 생성된다")
    @ValueSource(ints = {2, 10})
    void numberRank_Create_ValidNumberRange(int number) {
        NumberRank numberRank = new NumberRank(number);

        assertThat(numberRank).isNotNull();
    }

    @ParameterizedTest(name = "숫자 {0} 은(는) 범위를 벗어나므로 예외가 발생한다")
    @ValueSource(ints = {1, 11})
    void numberRank_Create_InvalidNumberRange(int number) {

        assertThatThrownBy(() -> new NumberRank(number))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("숫자 카드는 2부터 10 사이여야 합니다.");
    }
}