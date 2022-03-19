package blackjack.domain.state;

import blackjack.domain.card.Card;
import blackjack.domain.card.Cards;
import blackjack.domain.game.betting.BettingMoney;
import blackjack.domain.state.finished.Blackjack;

public interface State {

    State draw(final Card card);

    State stay();

    boolean isFinished();

    Cards cards();

    int getProfit(final State another);

    boolean isSameStateWith(final Class<? extends State> state);

    static State create(final Cards cards, final BettingMoney bettingMoney) {
        if (cards.isBlackJack()) {
            return new Blackjack(cards, bettingMoney);
        }
        return new Hit(cards, bettingMoney);
    }
}