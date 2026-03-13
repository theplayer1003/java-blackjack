package blackjack.domain;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import blackjack.domain.card.Card;
import blackjack.domain.card.Deck;
import blackjack.domain.card.NumberRank;
import blackjack.domain.card.Suit;
import blackjack.domain.player.Player;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class BlackjackGameTest {

    @Mock
    private Deck deck;

    @Mock
    private Player player;

    @InjectMocks
    private BlackjackGame blackjackGame;

    @Test
    void start_DistributesTwoCardsToEachParticipant() {
        Card dummyCard = new Card(new NumberRank(2), Suit.HEART);
        given(deck.draw()).willReturn(dummyCard);

        blackjackGame.start();

        //verify(deck, times(2)).draw();
        then(deck).should(times(2)).draw();

        //verify(player, times(2)).receiveCard(dummyCard);
        then(player).should(times(2)).receiveCard(dummyCard);

    }
}
