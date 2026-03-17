package blackjack.domain.player;

import java.util.regex.Pattern;

public record Name(String name) {
    private static final Pattern FORBIDDEN_PATTERN = Pattern.compile("^딜러.*|.*딜러$");

    public Name(String name) {
        validateNotNullAndNotBlank(name);
        validateNoWhitespace(name);
        validateNotReservedWord(name);
        this.name = name;
    }

    private void validateNotReservedWord(String name) {
        if (FORBIDDEN_PATTERN.matcher(name).matches()) {
            throw new IllegalArgumentException("플레이어 이름에 '딜러'를 사용할 수 없습니다");
        }
    }

    private void validateNoWhitespace(String name) {
        if (name.chars().anyMatch(Character::isWhitespace)) {
            throw new IllegalArgumentException("이름 사이에 공백이 포함될 수 없습니다");
        }
    }

    private void validateNotNullAndNotBlank(String name) {
        if (name == null) {
            throw new IllegalArgumentException("이름에 null 값이 올 수 없습니다");
        }
        if (name.isBlank()) {
            throw new IllegalArgumentException("이름은 공백일 수 없습니다");
        }
    }
}
