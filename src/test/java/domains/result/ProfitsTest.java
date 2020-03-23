package domains.result;

import domains.card.Deck;
import domains.user.Player;
import domains.user.money.ProfitMoney;
import domains.user.name.PlayerName;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class ProfitsTest {
    @DisplayName("게임결과를 받아 수익을 계산")
    @Test
    void constructor_GivenGameResult_CreateProfits() {
        Deck deck = new Deck();
        Player ddoring = new Player(new PlayerName("또링"), "1000", deck);
        Player smallbear = new Player(new PlayerName("작은곰"), "50000", deck);
        Map<Player, ResultType> playerResult = new HashMap<>();
        playerResult.put(ddoring, ResultType.WIN);
        playerResult.put(smallbear, ResultType.BLACKJACK);
        Profits profits = new Profits(new GameResult(playerResult));

        Map<Player, ProfitMoney> expectedProfit = new HashMap<>();
        expectedProfit.put(ddoring, new ProfitMoney("1000"));
        expectedProfit.put(smallbear, new ProfitMoney("75000"));

        assertThat(profits.getPlayerProfits()).isEqualTo(expectedProfit);
    }

    @DisplayName("딜러 수익 계산")
    @Test
    void constructor_GivenGameResult_CreateDealerProfit() {
        Deck deck = new Deck();
        Player ddoring = new Player(new PlayerName("또링"), "1000", deck);
        Player smallbear = new Player(new PlayerName("작은곰"), "50000", deck);
        Map<Player, ResultType> playerResult = new HashMap<>();
        playerResult.put(ddoring, ResultType.WIN);
        playerResult.put(smallbear, ResultType.BLACKJACK);
        Profits profits = new Profits(new GameResult(playerResult));

        ProfitMoney expectDealerProfit = new ProfitMoney(-76000);

        assertThat(profits.createDealerProfit()).isEqualTo(expectDealerProfit);
    }
}