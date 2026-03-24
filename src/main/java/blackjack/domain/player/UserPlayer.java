package blackjack.domain.player;

import blackjack.domain.card.Card;
import java.util.List;
import java.util.regex.Pattern;

public class UserPlayer implements Participant {
    private static final Pattern FORBIDDEN_PATTERN = Pattern.compile("^딜러.*|.*딜러$");

    private final Name name;
    private final Hand hand = new Hand();

    public UserPlayer(Name name) {
        validateNotReservedWord(name);
        this.name = name;
    }

    private void validateNotReservedWord(Name name) {
        if (FORBIDDEN_PATTERN.matcher(name.name()).matches()) {
            throw new IllegalArgumentException("플레이어 이름에 '딜러'를 사용할 수 없습니다");
        }
    }

    @Override
    public void receiveCard(Card card) {
        hand.add(card);
    }

    public int getCurrentCardSize() {
        return hand.getSize();
    }

    public List<Card> getCards() {
        return hand.getCards();
    }

    public Name getName() {
        return name;
    }

    @Override
    public boolean isDrawable() {
        return hand.calculateScore() < 21;
    }
}
