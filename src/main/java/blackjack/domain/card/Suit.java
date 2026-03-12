package blackjack.domain.card;

public enum Suit {
    SPADES("스페이드"),
    HEART("하트"),
    DIAMOND("다이아몬드"),
    CLUBS("클로버");

    private final String displayName;

    Suit(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
