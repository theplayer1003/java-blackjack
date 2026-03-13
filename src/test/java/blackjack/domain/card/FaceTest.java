package blackjack.domain.card;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EnumSource;

class FaceTest {

    @ParameterizedTest(name = "{0} 카드는 현재 점수와 무관하게 항상 10점을 반환한다")
    @EnumSource(value = Face.class, names = {"KING", "QUEEN", "JACK"})
    void getScore_KQJ_AlwaysReturns10(Face face) {
        int currentTotal = 15;

        int score = face.getScore(currentTotal);

        assertThat(score).isEqualTo(10);
    }

    @ParameterizedTest(name = "현재 점수가 {0} 점 일 때, ACE 카드는 {1} 점을 반환한다")
    @CsvSource(value = {
            "9, 11",
            "10, 11",
            "11, 1"
    })
    void getSocre_Ace_ReturnsDynamicScoreBasedOnBoundary(int currentTotal, int expected) {
        int score = Face.ACE.getScore(currentTotal);

        assertThat(score).isEqualTo(expected);
    }

    @ParameterizedTest(name = "{0} 의 디스플레이 이름은 {1} 이다")
    @CsvSource(value = {
            "KING, K",
            "QUEEN, Q",
            "JACK, J",
            "ACE, A"
    })
    void getDisplayName_ReturnsCorrectString(Face face, String expected){
        assertThat(face.getDisplayName()).isEqualTo(expected);
    }
}