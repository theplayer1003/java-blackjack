package blackjack.domain.player;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class ParticipantFactoryTest {

    @Test
    void create_ReturnsParticipants_ValidNameList() {
        Participants participants = ParticipantFactory.create("pobi,jason");

        assertThat(participants.getAllParticipantsSize()).isEqualTo(3);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "pobi,jason ",
            "pobi, jason",
            " pobi, jason"
    })
    void create_TrimsMarginSpaces_NamesContainLeadingOrTrailingSpaces(String nameInput) {
        Participants participants = ParticipantFactory.create(nameInput);
        
        assertThat(participants.getAllParticipantsSize()).isEqualTo(3);
        assertThat(participants.getPlayerNames())
                .containsExactly("pobi","jason");
    }

    @Test
    void create_ThrowsIAE_NameListIsNull() {
        assertThatThrownBy(() -> ParticipantFactory.create(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("참여자 이름들이 null 이거나 비어 있을 수 없습니다");
    }
}