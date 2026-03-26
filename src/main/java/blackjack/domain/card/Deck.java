package blackjack.domain.card;

public interface Deck {
    Card draw();

    int getCurrentSize();
}
