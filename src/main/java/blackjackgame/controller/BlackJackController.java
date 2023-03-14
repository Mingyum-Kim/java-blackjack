package blackjackgame.controller;

import blackjackgame.domain.*;
import blackjackgame.view.AddCardRequest;
import blackjackgame.view.InputView;
import blackjackgame.view.OutputView;

import java.util.ArrayList;
import java.util.List;

public class BlackJackController {
    private final InputView inputView;
    private final OutputView outputView;

    public BlackJackController(final InputView inputView, final OutputView outputView) {
        this.inputView = inputView;
        this.outputView = outputView;
    }

    public void run() {
        final Deck deck = new Deck();
        final Dealer dealer = new Dealer(new Hand(deck.pickOne(), deck.pickOne()));
        final Guests guests = generateGuests(deck);
        printFirstHand(guests, dealer);

        askGuestsHitCard(guests.getGuests(), deck);
        askDealerHitCard(dealer, deck);

        printPlayersCardScore(guests, dealer);
        printBettingResult(guests, dealer);
    }

    private Guests generateGuests(final Deck deck) {
        List<Name> guestNames = askGuestNames();
        List<BettingMoney> bettingMonies = askGuestsBettingMoney(guestNames);
        return new Guests(guestNames, bettingMonies, deck);
    }

    private List<Name> askGuestNames() {
        try {
            List<String> guestNames = inputView.readGuestNames();
            Names names = new Names(guestNames);
            return names.getNames();
        } catch (IllegalArgumentException e) {
            inputView.printErrorMsg(e.getMessage());
            return askGuestNames();
        }
    }

    private List<BettingMoney> askGuestsBettingMoney(List<Name> guestNames) {
        List<BettingMoney> guestsBettingMoney = new ArrayList<>();
        for (Name name : guestNames) {
            askGuestBettingMoney(guestsBettingMoney, name.getName());
        }
        return guestsBettingMoney;
    }

    private void askGuestBettingMoney(List<BettingMoney> guestBettingMoney, String guestName) {
        try {
            BettingMoney bettingMoney = BettingMoney.of(inputView.readBettingMoney(guestName));
            guestBettingMoney.add(bettingMoney);
        } catch (IllegalArgumentException e) {
            inputView.printErrorMsg(e.getMessage());
            askGuestBettingMoney(guestBettingMoney, guestName);
        }
    }

    private void printFirstHand(final Guests guests, final Dealer dealer) {
        outputView.printFirstDealerCards(dealer.getName(), BlackJackGameDataAssembler.assembleCardDto(dealer.getHand()));
        for (final Guest guest : guests.getGuests()) {
            outputView.printCards(guest.getName(), BlackJackGameDataAssembler.assembleCardDto(guest.getHand()));
        }
    }

    private void askGuestsHitCard(final List<Guest> guests, final Deck deck) {
        for (Guest guest : guests) {
            askGuestHitCardRepeat(deck, guest);
        }
    }

    private void askGuestHitCardRepeat(final Deck deck, final Guest guest) {
        AddCardRequest addCardRequest = AddCardRequest.YES;
        while (guest.canHit() && addCardRequest == AddCardRequest.YES) {
            addCardRequest = inputView.readWantMoreCard(guest.getName());
            askGuestHitCard(deck, guest, addCardRequest);
        }
    }

    private void askGuestHitCard(final Deck deck, final Guest guest, final AddCardRequest addCardRequest) {
        if (addCardRequest == AddCardRequest.YES) {
            guest.addCard(deck.pickOne());
        }
        outputView.printCards(guest.getName(), BlackJackGameDataAssembler.assembleCardDto(guest.getHand()));
    }

    private void askDealerHitCard(final Dealer dealer, final Deck deck) {
        while (dealer.canHit()) {
            dealer.addCard(deck.pickOne());
            outputView.dealerAddCard();
        }
    }

    private void printPlayersCardScore(final Guests guests, final Dealer dealer) {
        outputView.printScore(dealer.getName(), BlackJackGameDataAssembler.assembleCardDto(dealer.getHand()), dealer.getScore());

        for (final Guest guest : guests.getGuests()) {
            outputView.printScore(guest.getName(), BlackJackGameDataAssembler.assembleCardDto(guest.getHand()), guest.getScore());
        }
    }

    private void printBettingResult(final Guests guests, final Dealer dealer) {
        outputView.printBettingResult(Result.getPlayersRevenue(guests.getGuests(), dealer));
    }
}