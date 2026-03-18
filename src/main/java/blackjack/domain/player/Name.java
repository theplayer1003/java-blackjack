package blackjack.domain.player;

public record Name(String name) {

    public Name(String name) {
        validateNotNullAndNotBlank(name);
        validateNoWhitespace(name);
        this.name = name;
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
