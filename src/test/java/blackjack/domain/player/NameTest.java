package blackjack.domain.player;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.ValueSource;

class NameTest {

    @Test
    void constructor_ThrowsIllegalArgumentException_NameIsNull() {
        assertThatThrownBy(() -> new Name(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("이름에 null 값이 올 수 없습니다");
    }

    @ParameterizedTest
    @EmptySource
    @ValueSource(strings = {" ", "  "})
    void constructor_ThrowsIllegalArgumentException_NameIsBlank(String blank) {
        assertThatThrownBy(() -> new Name(blank))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("이름은 공백일 수 없습니다");
    }

    @ParameterizedTest
    @ValueSource(strings = {"p obi", "p  obi", " pobi", "pobi "})
    void constructor_ThrowsIllegalArgumentException_NameHasNoWhiteSpace(String whitespace) {
        assertThatThrownBy(() -> new Name(whitespace))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("이름 사이에 공백이 포함될 수 없습니다");
    }

    @Test
    void constructor_DoesNotThrowException_NameContainsDealerInTheMiddle() {
        String allowedName = "우테코_딜러_포비";

        Name name = new Name(allowedName);

        assertThat(name.name()).isEqualTo(allowedName);
    }
}