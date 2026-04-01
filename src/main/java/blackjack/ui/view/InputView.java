package blackjack.ui.view;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class InputView {

    public static final String REGEX = ".*,,.*";
    private static final String DELIMITER = ",";

    private final Scanner sc;

    public InputView(Scanner sc) {
        this.sc = sc;
    }

    public List<String> inputPlayerNames() {
        System.out.println("게임에 참여할 사람의 이름을 입력하세요.(쉼표 기준으로 분리)");
        String userInput = sc.nextLine();

        validateNotBlank(userInput);
        validateFormat(userInput);

        return parseNames(userInput);
    }

    private static List<String> parseNames(String userInput) {
        return Arrays.stream(userInput.split(DELIMITER))
                .map(String::trim)
                .toList();
    }

    public boolean inputHitOrStand(String playerName) {
        System.out.println(playerName + "는 한장의 카드를 더 받겠습니까?(예는 y, 아니오는 n)");
        String userInput = sc.nextLine().trim();

        if (userInput.equals("y")) {
            return true;
        }
        if (userInput.equals("n")) {
            return false;
        }

        throw new IllegalArgumentException("y 또는 n 으로만 입력 가능합니다");
    }

    private void validateFormat(String userInput) {
        if (userInput.matches(REGEX) || userInput.startsWith(DELIMITER) || userInput.endsWith(DELIMITER)) {
            throw new IllegalArgumentException("입력 형식이 올바르지 않습니다. 이름은 쉼표를 구분자로 연속해서 입력해주세요");
        }
    }

    private void validateNotBlank(String userInput) {
        if (userInput.trim().isEmpty()) {
            throw new IllegalArgumentException("입력 값이 존재하지 않습니다");
        }
    }
}
