package blackjack.domain.user;

import blackjack.domain.card.Card;
import blackjack.domain.card.GamePoint;

public interface Player {

    void draw(Card card);

    boolean canReceive();

    GamePoint getGamePoint();

}
