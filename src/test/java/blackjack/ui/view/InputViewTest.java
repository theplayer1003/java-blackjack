package blackjack.ui.view;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.io.ByteArrayInputStream;
import java.util.Scanner;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class InputViewTest {

    @Test
    void inputPlayerNames_ValidInput() {
        String simulatedInput = "pobi,james\n";
        Scanner dummyScanner = new Scanner(new ByteArrayInputStream(simulatedInput.getBytes()));

        InputView inputView = new InputView(dummyScanner);

        assertThat(inputView.inputPlayerNames()).containsExactly("pobi", "james");
    }

    @Test
    void inputPlayerNames_ThrowsIAE_WhenUserInputIsEmpty() {
        String simulatedInput = "\n";
        Scanner dummyScanner = new Scanner(new ByteArrayInputStream(simulatedInput.getBytes()));

        InputView inputView = new InputView(dummyScanner);

        assertThatThrownBy(() -> inputView.inputPlayerNames())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("입력 값이 존재하지 않습니다");
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "pobi,,james\n",
            ",pobi\n",
            "pobi,\n"
    })
    void getPlayerNames_ThrowsIAE_WhenInvalidFormat(String testSource) {
        Scanner scanner = new Scanner(testSource);
        InputView inputView = new InputView(scanner);

        assertThatThrownBy(() -> inputView.inputPlayerNames())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("입력 형식이 올바르지 않습니다. 이름은 쉼표를 구분자로 연속해서 입력해주세요");
    }

    @Test
    void inputHitOrStand_ReturnTrue_WhenInputIsY() {
        Scanner scanner = new Scanner("y\n");
        InputView inputView = new InputView(scanner);

        assertThat(inputView.inputHitOrStand("pobi")).isTrue();
    }

    @Test
    void inputHitOrStand_ReturnFalse_WhenInputIsN() {
        Scanner scanner = new Scanner("n\n");
        InputView inputView = new InputView(scanner);

        assertThat(inputView.inputHitOrStand("pobi")).isFalse();
    }

    @Test
    void inputHitOrStand_ThrowIAE_WhenUserInputIsNotYorN() {
        Scanner scanner = new Scanner("a\n");
        InputView inputView = new InputView(scanner);

        assertThatThrownBy(() -> inputView.inputHitOrStand("pobi"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("y 또는 n 으로만 입력 가능합니다");
    }
}
