package domain.game;

import controller.dto.request.PlayerBettingMoney;
import controller.dto.response.InitialCardStatus;
import controller.dto.response.ParticipantHandStatus;
import controller.dto.response.ParticipantProfitResponse;
import controller.dto.response.PlayerOutcome;
import domain.constants.Outcome;
import domain.game.deck.Deck;
import domain.game.deck.DeckGenerator;
import domain.participant.Dealer;
import domain.participant.Participant;
import domain.participant.Participants;
import domain.participant.Player;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class BlackJackGame {
    public static final int BLACKJACK_SCORE = 21;
    public static final int BLACKJACK_CARD_SIZE = 2;
    private static final int INITIAL_CARD_SIZE = 2;
    private static final int CARD_PICK_SIZE = 1;

    private final Participants participants;
    private final Deck deck;

    private BlackJackGame(final Participants participants, final Deck deck) {
        this.participants = participants;
        this.deck = deck;
    }

    public static BlackJackGame from(final List<PlayerBettingMoney> requests,
                                     final DeckGenerator deckGenerator) {
        return new BlackJackGame(Participants.from(requests), deckGenerator.generate());
    }

    public InitialCardStatus initialize() {
        List<ParticipantHandStatus> status = new ArrayList<>();
        status.add(createInitialHandStatusAfterPick(participants.getDealer()));

        for (Player player : participants.getPlayers()) {
            status.add(createInitialHandStatusAfterPick(player));
        }
        return new InitialCardStatus(INITIAL_CARD_SIZE, status);
    }

    private ParticipantHandStatus createInitialHandStatusAfterPick(final Participant participant) {
        participant.pickCard(deck, INITIAL_CARD_SIZE);
        return participant.createInitialHandStatus();
    }

    public List<ParticipantHandStatus> createHandStatuses() {
        List<ParticipantHandStatus> status = new ArrayList<>();
        status.add(participants.getDealer().createHandStatus());

        for (Player player : participants.getPlayers()) {
            status.add(player.createHandStatus());
        }
        return status;
    }


    public void giveCard(
            final Participant participant,
            final ActionAfterPick action,
            final DecisionToContinue decision
    ) {
        while (participant.canPickCard(decision)) {
            participant.pickCard(deck, CARD_PICK_SIZE);
            ParticipantHandStatus currentStatus = participant.createHandStatus();
            action.accept(currentStatus);
        }
    }

    public List<ParticipantProfitResponse> judge() {
        Referee referee = new Referee(participants);
        List<PlayerOutcome> outcomes = referee.judge();

        return getParticipantProfitResponses(outcomes);
    }

    private List<ParticipantProfitResponse> getParticipantProfitResponses(final List<PlayerOutcome> outcomes) {
        return Stream.concat(
                Stream.of(generateDealerProfitResponse(outcomes)),
                generatePlayersProfitResponse(outcomes)
        ).toList();
    }

    private ParticipantProfitResponse generateDealerProfitResponse(final List<PlayerOutcome> outcomes) {
        int sumOfPlayerProfit = outcomes.stream()
                .mapToInt(outcome -> calculatePlayerProfit(outcome.player(), outcome.outcome()))
                .sum();
        int dealerProfit = (-1) * sumOfPlayerProfit;
        return new ParticipantProfitResponse(Dealer.DEALER_NAME, dealerProfit);
    }

    private Stream<ParticipantProfitResponse> generatePlayersProfitResponse(final List<PlayerOutcome> outcomes) {
        return outcomes.stream()
                .map(outcome -> new ParticipantProfitResponse(
                        outcome.player().getName(),
                        calculatePlayerProfit(outcome.player(), outcome.outcome()))
                );
    }

    private int calculatePlayerProfit(final Player player, final Outcome outcome) {
        double rates = outcome.earningRates();
        int bettingMoney = player.bettingMoney();
        return (int) Math.ceil(bettingMoney * rates);
    }

    public List<Participant> getParticipants() {
        return participants.getParticipants();
    }
}
