package blackjack.domain.card;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

class FaceRankTest {

    @Test
    void getDisplayName_Always_ReturnsStringValueOfFaceCharInitial() {
        FaceRank ace = FaceRank.ACE;

        assertThat(ace.getDisplayName()).isEqualTo("A");
    }

    @ParameterizedTest(name = "{0} 카드는 항상 10점을 반환한다")
    @EnumSource(value = FaceRank.class, names = {"KING", "QUEEN", "JACK"})
    void getScore_KingQueenJack_AlwaysReturns10(FaceRank faceRank) {
        assertThat(faceRank.getScore()).isEqualTo(10);
    }

    @Test
    void getScore_Ace_Returns11() {
        FaceRank aceCard = FaceRank.ACE;

        assertThat(aceCard.getScore()).isEqualTo(11);
    }

    @ParameterizedTest(name = "{0} 카드는 에이스가 아니므로 false 를 반환한다")
    @EnumSource(value = FaceRank.class, names = {
            "KING",
            "QUEEN",
            "JACK"
    })
    void isAce_ReturnFalse_WhenKQJ(FaceRank faceRank) {
        assertThat(faceRank.isAce()).isFalse();
    }

    @Test
    void isAce_ReturnTrue_WhenAce() {
        assertThat(FaceRank.ACE.isAce()).isTrue();
    }
}