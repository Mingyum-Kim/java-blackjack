package domain.game;

import static domain.participant.Participants.CACHED_DEALER;

import controller.dto.response.PlayerOutcome;
import domain.participant.Participants;
import java.util.List;

public class Referee {
    private final Participants participants;

    public Referee(final Participants participants) {
        this.participants = participants;
    }

    public List<PlayerOutcome> judge() {

        if (CACHED_DEALER.isBlackJack()) {
            return participants.getPlayersOutcomeIf(
                    GameRule::judgeWhenDealerIsBlackJack
            );
        }

        if (CACHED_DEALER.isBusted()) {
            return participants.getPlayersOutcomeIf(
                    GameRule::judgeWhenDealerIsBusted
            );
        }

        return participants.getPlayersOutcomeIf(
                GameRule::judgeWhenDealerIsNotBustedAndNotBlackJack
        );
    }
}
