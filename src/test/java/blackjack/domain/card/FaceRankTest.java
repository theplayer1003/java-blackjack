package blackjack.domain.card;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EnumSource;

class FaceRankTest {

    @Test
    void getDisplayName_Always_ReturnsStringValueOfFaceCharInitial() {
        FaceRank ace = FaceRank.ACE;

        assertThat(ace.getDisplayName()).isEqualTo("A");
    }

    @ParameterizedTest(name = "{0} 카드는 현재 점수와 무관하게 항상 10점을 반환한다")
    @EnumSource(value = FaceRank.class, names = {"KING", "QUEEN", "JACK"})
    void getScore_KingQueenJack_AlwaysReturns10(FaceRank faceRank) {
        int anyCurrentTotalScore = 11;

        assertThat(faceRank.getScore(anyCurrentTotalScore)).isEqualTo(10);
    }

    @ParameterizedTest(name = "현재 점수가 {0} 점 일 경우 ACE 카드는 {1} 을 반환한다")
    @CsvSource(value = {
            "10,11",
            "11,1"
    })
    void getScore_Ace_ReturnsDynamicScoreNumber(int currentTotalScore, int expected) {
        FaceRank aceCard = FaceRank.ACE;

        assertThat(aceCard.getScore(currentTotalScore)).isEqualTo(expected);
    }
}