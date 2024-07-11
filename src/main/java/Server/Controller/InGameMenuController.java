package Server.Controller;

import Server.Enum.*;
import Server.Model.*;
import Server.Model.Commander;
import Server.Model.GameBoardin;
import Server.Regex.InGameMenuRegex;

import java.util.ArrayList;
import java.util.regex.Matcher;

public class InGameMenuController extends Thread {
    private static final ArrayList<InGameMenuController> controllers = new ArrayList<>();
    private int currentUser;
    private final User[] users = new User[2];

    public static Object processRequest(ApplicationController applicationController, String inputCommand) {
        try {
            Object result = null;
            Matcher matcher;
            User user = applicationController.getCurrentUser();
            Sender sender = applicationController.getSender();
            Sender opponentSender = user.getOpponent().getSender();
            if ((matcher = InGameMenuRegex.PLACE_SOLDIER.getMatcher(inputCommand)).matches()) {
                placeSoldier(applicationController, matcher);
            } else if ((matcher = InGameMenuRegex.PLACE_DECOY.getMatcher(inputCommand)).matches()) {
                placeDecoy(applicationController, matcher);
            } else if ((matcher = InGameMenuRegex.PLACE_WEATHER.getMatcher(inputCommand)).matches()) {
                placeWeather(applicationController, matcher);
            } else if ((matcher = InGameMenuRegex.PLACE_SPECIAL.getMatcher(inputCommand)).matches()) {
                    placeSpecial(applicationController, matcher);
            } else if ((matcher = InGameMenuRegex.COMMANDER_POWER_PLAY.getMatcher(inputCommand)).matches()) {
                commanderPowerPlay(user);
            } else if ((matcher = InGameMenuRegex.APPLY_CHEAT_CODE.getMatcher(inputCommand)).matches()) {
                CheatMenuController.processRequest(applicationController, matcher.group("cheatCode"));
            } else if ((matcher = InGameMenuRegex.START_GAME.getMatcher(inputCommand)).matches()) {
                startGame(user);
            } else if ((matcher = InGameMenuRegex.GET_GAME_BOARDIN.getMatcher(inputCommand)).matches()) {
                result = getGameBoardin(user, sender, Integer.parseInt(matcher.group("new")));
            } else if ((matcher = InGameMenuRegex.SEND_REACTION.getMatcher(inputCommand)).matches()) {
                opponentSender.sendCommand(inputCommand);
            } else if ((matcher = InGameMenuRegex.SEND_EMOJI_REACTION.getMatcher(inputCommand)).matches()) {
                opponentSender.sendCommand(inputCommand);
            } else if ((matcher = InGameMenuRegex.PASS_TURN.getMatcher(inputCommand)).matches()) {
                passTurnCalled(applicationController);
            } else if ((matcher = InGameMenuRegex.SEND_MESSAGE.getMatcher(inputCommand)).matches()) {
                saveMessage(applicationController, matcher.group("message"));
            } else if ((matcher = InGameMenuRegex.GET_CHAT_BOX.getMatcher(inputCommand)).matches()){
                result = getChatBox(applicationController);
            } else if((matcher = InGameMenuRegex.ONE_CARD_CHOSEN.getMatcher(inputCommand)).matches()){
                moveCardFromDiscardToHandForMedic(sender, Integer.parseInt(matcher.group("cardNumber")));
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static void passTurnCalled(ApplicationController applicationController) {
        GameBoard gameBoard = applicationController.getCurrentUser().getCurrentGameBoard();
        changeTurn(applicationController);
        gameBoard.passTurnCalled();
        Result result = gameBoard.passTurn(applicationController);
        if(result == null || result.isSuccessful())
            return;
        applicationController.getCurrentUser().getSender().sendCommand("end round " + result.getMessage().get(0));
        if(gameBoard.isGameOnline())
            applicationController.getCurrentUser().getOpponent().getSender().sendCommand("end round " + result.getMessage().get(0));
    }

    private static void changeTurn(ApplicationController applicationController){
        User user = applicationController.getCurrentUser();
        GameBoard gameBoard = user.getCurrentGameBoard();
        if(gameBoard.isPassTurnCalled())
            return;
        user.saveGameBoardin();
        user.getOpponent().saveGameBoardin();
        gameBoard.changeTurn(applicationController);
        user.getSender().sendCommandWithOutResponse("change turn");
        if(gameBoard.isGameOnline())
            user.getOpponent().getSender().sendCommandWithOutResponse("change turn");
    }

    private static Object getChatBox(ApplicationController applicationController) {
        ChatBox chatBox = applicationController.getCurrentUser().getCurrentGameBoard().getChatBox();
        chatBox.setCurrentUsername(applicationController.getCurrentUser().getUsername());
        return chatBox;
    }

    private static void saveMessage(ApplicationController applicationController, String message) {
        GameBoard gameBoard = applicationController.getCurrentUser().getCurrentGameBoard();
        gameBoard.addMessage(new Message(applicationController.getCurrentUser().getUsername(), message));
        if(gameBoard.isGameOnline())
            applicationController.getCurrentUser().getOpponent().getSender().sendCommandWithOutResponse("refresh chat box");
    }

    /* // TODO: it shall be removed
    private static Result passTurn(ApplicationController applicationController) {
        User user = applicationController.getCurrentUser();
        boolean isOnline = user.getCurrentGameBoard().isGameOnline();
        user.getCurrentGameBoard().changeTurn();
        Result result = user.getCurrentGameBoard().passTurn();
        if(user.getCurrentGameBoard() == null)
            return null;
        if(isOnline)
            user.getOpponent().getSender().sendCommand("pass turn " + (result.isSuccessful() ? "" : result.getMessage().get(0)));
        else
            applicationController.setCurrentUser(user.getOpponent());
        return result;
    }

     */

    private static void commanderPowerPlay(User user) {
        Commander commander = user.getCommander();
        commander.executeAction();
    }

    private static void placeSpecial(ApplicationController applicationController, Matcher matcher) {
        User user = applicationController.getCurrentUser();
        int cardNumber = Integer.parseInt(matcher.group("cardNumber"));
        String rowNumberString = matcher.group("rowNumber");
        int rowNumber = -1;
        if(rowNumberString != null)
            rowNumber = Integer.parseInt(rowNumberString);
        int playerIndex = user.getCurrentGameBoard().getPlayerNumber(user);
        GameBoard gameBoard = user.getCurrentGameBoard();
        Spell spell = (Spell)user.getHand().get(cardNumber);
        user.getHand().remove(cardNumber);
        gameBoard.addSpecialCard(playerIndex, rowNumber, spell);
        if(user.getCurrentGameBoard().isGameOnline())
            user.getOpponent().getSender().sendCommandWithOutResponse("place special " + cardNumber + " in row " + rowNumber + " 1");
        gameBoard.addLog("place special " + cardNumber + " in row " + rowNumber + " 0", gameBoard.getPlayerNumber(user));
        gameBoard.addLog("place special " + cardNumber + " in row " + rowNumber + " 1", 1 - gameBoard.getPlayerNumber(user));
        spell.executeAction();
        changeTurn(applicationController);
    }

    private static void placeWeather(ApplicationController applicationController, Matcher matcher) {
        try {
            User user = applicationController.getCurrentUser();
            int cardNumber = Integer.parseInt(matcher.group("cardNumber"));
            int playerIndex = user.getCurrentGameBoard().getPlayerNumber(user);
            GameBoard gameBoard = user.getCurrentGameBoard();
            Spell spell = (Spell) user.getHand().get(cardNumber);
            user.getHand().remove(cardNumber);
            if(spell.getName().matches("(S|s)corch")) {
                spell.getUser().getDiscardPile().add(spell);
                // TODO: if you want you can call a function to add this spell from opponent's hand to discard
            }
            else if(user.getCurrentGameBoard().isGameOnline())
                user.getOpponent().getSender().sendCommandWithOutResponse("place weather " + cardNumber + " 1");
            gameBoard.addLog("place weather " + cardNumber + " 0", gameBoard.getPlayerNumber(user));
            gameBoard.addLog("place weather " + cardNumber + " 1", 1 - gameBoard.getPlayerNumber(user));
            spell.executeAction();
            changeTurn(applicationController);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void placeSoldier(ApplicationController applicationController, Matcher matcher) {
        try {
            User user = applicationController.getCurrentUser();
            int cardNumber = Integer.parseInt(matcher.group("cardNumber"));
            String rowNumberString = matcher.group("rowNumber");
            int rowNumber = -1;
            if (rowNumberString != null)
                rowNumber = Integer.parseInt(rowNumberString);
            int playerIndex = user.getCurrentGameBoard().getPlayerNumber(user);
            GameBoard gameBoard = user.getCurrentGameBoard();
            Soldier soldier = (Soldier) user.getHand().get(cardNumber);
            user.getHand().remove(cardNumber);
            if (soldier.hasAttribute(Attribute.SPY)) {
                playerIndex = 1 - playerIndex;
            }
            gameBoard.addSoldierToRow(playerIndex, rowNumber, soldier);
            gameBoard.setPlayerScore(playerIndex, gameBoard.getPlayerScore(playerIndex) + soldier.getShownHp());
            if(user.getCurrentGameBoard().isGameOnline()){
                if(soldier.hasAttribute(Attribute.SPY)) {
                    user.getOpponent().getSender().sendCommandWithOutResponse("move soldier " + cardNumber + " from opponent's hand to my row " + rowNumber + " 0");
                    gameBoard.addLog("move soldier " + cardNumber + " from opponent's hand to my row 0" + rowNumber, playerIndex);
                    gameBoard.addLog("move soldier " + cardNumber + " from opponent's hand to my row 1" + rowNumber, 1 - playerIndex);
                }
                else {
                    user.getOpponent().getSender().sendCommandWithOutResponse("place soldier " + cardNumber + " in row " + rowNumber + " 1");
                    gameBoard.addLog("place soldier " + cardNumber + " in row " + rowNumber + " 0", playerIndex);
                    gameBoard.addLog("place soldier " + cardNumber + " in row " + rowNumber + " 1", 1 - playerIndex);
                }
            }
            soldier.executeAction();
            changeTurn(applicationController);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void placeDecoy(ApplicationController applicationController, Matcher matcher){
        User user = applicationController.getCurrentUser();
        int thisCardNumber = Integer.parseInt(matcher.group("thisCardNumber"));
        int cardNumber = Integer.parseInt(matcher.group("cardNumber"));
        int rowNumber = Integer.parseInt(matcher.group("rowNumber"));
        int playerIndex = user.getCurrentGameBoard().getPlayerNumber(user);
        GameBoard gameBoard = user.getCurrentGameBoard();
        Soldier soldier = gameBoard.getRows()[playerIndex][rowNumber].get(cardNumber);
        gameBoard.getRows()[playerIndex][rowNumber].remove(cardNumber);
        Spell decoy = (Spell) user.getHand().get(thisCardNumber);
        user.getHand().set(thisCardNumber, soldier);
        user.getDiscardPile().add(decoy);
        if(gameBoard.isGameOnline())
            user.getOpponent().getSender().sendCommand("place decoy " + thisCardNumber + " to card in " + rowNumber + " " + cardNumber + " 1");
        gameBoard.addLog("place decoy " + thisCardNumber + " to card in " + rowNumber + " " + cardNumber + " 0", playerIndex);
        gameBoard.addLog("place decoy " + thisCardNumber + " to card in " + rowNumber + " " + cardNumber + " 1", 1 - playerIndex);
        changeTurn(applicationController);
    }


    public static void startGame(User user){ // it is only for offline games
        User opponent = user.getOpponent();
        user.createHand();
        user.setInProcess(false);
        opponent.createHand();
        user.getCurrentGameBoard().addLog("start game", 0);
        user.getCurrentGameBoard().addLog("start game", 1);
    }

    public static boolean getOneCardFromDiscardPile(Sender sender, User user){
        if(user.getDiscardPile().size() == 0)
            return false;
        user.setOptionsType(0);
        sender.sendCommandWithOutResponse("show pile type " + 0 + " and let user choose " + 1);
        return true;
    }

    public static void moveCardFromDiscardToHandForMedic(Sender sender, int cardNumber){
        User user = sender.getUser();
        Card card = user.getDiscardPile().get(cardNumber);
        user.getDiscardPile().remove(cardNumber);
        user.getHand().add(card);
        sender.sendCommandWithOutResponse("move soldier " + cardNumber + " from discard pile to hand 0");
        if(user.getCurrentGameBoard().isGameOnline())
            sender.getUser().getOpponent().getSender().sendCommandWithOutResponse("move soldier " + cardNumber + " from discard pile to hand 1");
        user.getCurrentGameBoard().addLog("move soldier " + cardNumber + " from discard pile to hand 0",
                user.getCurrentGameBoard().getPlayerNumber(user));
        user.getCurrentGameBoard().addLog("move soldier " + cardNumber + " from discard pile to hand 1",
                1 - user.getCurrentGameBoard().getPlayerNumber(user));
    }

    public static void addCardToHand(Sender sender, GameBoard gameBoard, Card card, int playerIndex) {
        gameBoard.getPlayers()[playerIndex].getHand().add(card);
        sender.sendCommandWithOutResponse("add card to hand " + card.getSendableCardin() + " " + 0);
        if(gameBoard.isGameOnline())
            sender.getUser().getOpponent().getSender().sendCommandWithOutResponse("add card to hand " + card.getSendableCardin() + " " + 1);
        gameBoard.addLog("add card to hand " + card.getSendableCardin() + " 0", playerIndex);
        gameBoard.addLog("add card to hand " + card.getSendableCardin() + " 1", 1 - playerIndex);
    }

    public static void removeCardFromHand(Sender sender, GameBoard gameBoard, Card card, int playerIndex) {
        int cardNumber = gameBoard.getPlayers()[playerIndex].getHand().indexOf(card);
        gameBoard.getPlayers()[playerIndex].getHand().remove(card);
        sender.sendCommandWithOutResponse("remove card from hand " + cardNumber + " " + 0);
        if(gameBoard.isGameOnline())
            sender.getUser().getOpponent().getSender().sendCommandWithOutResponse("remove card from hand " + cardNumber + " " + 1);
        gameBoard.addLog("remove card from hand " + cardNumber + " 0", playerIndex);
        gameBoard.addLog("remove card from hand " + cardNumber + " 1", 1 - playerIndex);
    }

    public static void changeCardInGraphic(Sender sender, int rowNumber, int cardNumber, Soldier soldier) {
        sender.sendCommandWithOutResponse("change card in " + rowNumber + " " + cardNumber + " to " + soldier.getSendableCardin() + " " + 0);
        if(sender.getUser().getCurrentGameBoard().isGameOnline())
            sender.getUser().getOpponent().getSender().sendCommandWithOutResponse("change card in " + rowNumber + " " + cardNumber + " to " + soldier.getSendableCardin() + " " + 1);
        soldier.getGameBoard().addLog("change card in " + rowNumber + " " + cardNumber + " to " + soldier.getSendableCardin() + " 0",
                soldier.getGameBoard().getPlayerNumber(soldier.getUser()));
        soldier.getGameBoard().addLog("change card in " + rowNumber + " " + cardNumber + " to " + soldier.getSendableCardin() + " 1",
                1 - soldier.getGameBoard().getPlayerNumber(soldier.getUser()));
    }

    public static void destroySoldier(Sender sender, GameBoard gameBoard, Soldier soldier) {
        if(soldier == null)
            return;
        int playerIndex = gameBoard.getPlayerNumber(soldier.getUser());
        int rowNumber = Soldier.getPlacedRowNumber(soldier, gameBoard);
        int placedNumber = soldier.getPlacedNumber();
        gameBoard.getRows()[playerIndex][rowNumber].remove(soldier);
        soldier.getUser().getDiscardPile().add(soldier);
        gameBoard.setPlayerScore(playerIndex, gameBoard.getPlayerScore(playerIndex) - soldier.getShownHp());
        playerIndex = getClientVersionOfPlayerIndex(sender.getUser(), gameBoard.getPlayer(playerIndex));
        sender.sendCommandWithOutResponse("destroy soldier " + playerIndex + " " + rowNumber + " " + placedNumber);
        if(gameBoard.isGameOnline())
            sender.getUser().getOpponent().getSender().sendCommandWithOutResponse("destroy soldier " + (1 - playerIndex) + " " + rowNumber + " " + placedNumber);
        gameBoard.addLog("destroy soldier " + playerIndex + " " + rowNumber + " " + placedNumber, playerIndex);
        gameBoard.addLog("destroy soldier " + (1 - playerIndex) + " " + rowNumber + " " + placedNumber, 1 - playerIndex);
    }

    private static int getClientVersionOfPlayerIndex(User user, User player) {
        if(user == player)
            return 0;
        return 1;
    }

    public static void removeAllWeatherInGraphic(Sender sender) {
        sender.sendCommandWithOutResponse("clear all weather cards");
        if(sender.getUser().getCurrentGameBoard().isGameOnline())
            sender.getUser().getOpponent().getSender().sendCommandWithOutResponse("clear all weather cards");
        GameBoard gameBoard = sender.getUser().getCurrentGameBoard();
        gameBoard.addLog("clear all weather cards", 0);
        gameBoard.addLog("clear all weather cards", 1);
    }

    public static void moveDiscardPileToDeckForBoth(User user, Sender sender) {
        ArrayList<Card> discardPile = user.getDiscardPile();
        user.getDeck().addAll(discardPile);
        user.getDiscardPile().clear();
        User opponent = user.getOpponent();
        ArrayList<Card> opponentDiscardPile = opponent.getDiscardPile();
        opponent.getDeck().addAll(opponentDiscardPile);
        opponent.getDiscardPile().clear();
        sender.sendCommandWithOutResponse("move discard pile to deck");
        if(user.getCurrentGameBoard().isGameOnline())
            sender.getUser().getOpponent().getSender().sendCommandWithOutResponse("move discard pile to deck");
        user.getCurrentGameBoard().addLog("move discard pile to deck", 0);
        user.getCurrentGameBoard().addLog("move discard pile to deck", 1);
    }

    public static void moveSoldier(Sender sender, Soldier soldier, int playerNumber, int rowNumber) {
        GameBoard gameBoard = soldier.getGameBoard();
        int previousRowNumber = Soldier.getPlacedRowNumber(soldier, gameBoard);
        if (previousRowNumber == rowNumber)
            return;
        int placedNumber = soldier.getPlacedNumber();
        gameBoard.getRows()[playerNumber][previousRowNumber].remove(soldier);
        gameBoard.getRows()[playerNumber][rowNumber].add(soldier);
        sender.sendCommandWithOutResponse("move soldier " + previousRowNumber + " " + placedNumber + " to " + rowNumber + " " + 0);
        if(gameBoard.isGameOnline())
            sender.getUser().getOpponent().getSender().sendCommandWithOutResponse("move soldier " + previousRowNumber + " " + placedNumber + " to " + rowNumber + " " + 1);
        gameBoard.addLog("move soldier " + previousRowNumber + " " + placedNumber + " to " + rowNumber + " 0", playerNumber);
        gameBoard.addLog("move soldier " + previousRowNumber + " " + placedNumber + " to " + rowNumber + " 1", 1 - playerNumber);
    }

    public static void changeHpForSoldier(GameBoard gameBoard, Soldier soldier, int hp){
        int playerIndex = gameBoard.getPlayerNumber(soldier.getUser());
        int previousHp = soldier.getShownHp();
        soldier.setHp(hp);
        gameBoard.setPlayerScore(playerIndex, gameBoard.getPlayerScore(playerIndex) - previousHp + soldier.getShownHp());
    }

    public static Result vetoCard(User user, Matcher matcher){
        if(!matcher.matches())
            return new Result(false, "Invalid command");
        Card card = user.getHand().get(Integer.parseInt(matcher.group("cardNumber")));
        user.getHand().remove(card);
        Card anotherCard = user.getCardFromDeckRandomly();
        user.getHand().add(anotherCard);
        user.getDeck().add(card);
        return new Result(true, "Card vetoed successfully");
    }

    public static Result showInHandDeck(User user, Matcher matcher){
        if(!matcher.matches())
            return new Result(false, "Invalid command");
        ArrayList<String> cardsInformation = new ArrayList<>();
        String cardNumber = matcher.group("cardNumber");
        if (cardNumber != null) {
            cardsInformation.add(user.getHand().get(Integer.parseInt(cardNumber)).getInformation());
        } else {
            for(Card card : user.getHand())
                cardsInformation.add(card.getInformation());
        }
        return new Result(true, cardsInformation);
    }

    public static Result showRemainingCardsNumber(User user){
        if(user == null)
            return new Result(false);
        return new Result(true, user.getDeck().size() + "");
    }

    public static Result showOutOfPlayCards(User user){
        if(user == null)
            return new Result(false);
        StringBuilder usersCards = new StringBuilder();
        for(Card card : user.getDiscardPile())
            usersCards.append(card.getName());
        StringBuilder opponentsCards = new StringBuilder();
        for(Card card : user.getOpponent().getDiscardPile())
            opponentsCards.append(card.getName());
        return new Result(true, usersCards.toString(), opponentsCards.toString());
    }

    public static Result showCardsInRow(User user, Matcher matcher){
        int playerIndex = user.getCurrentGameBoard().getPlayerNumber(user);
        if(!matcher.matches())
            return new Result(false, "Invalid command");
        int rowNumber = Integer.parseInt(matcher.group("rowNumber"));
        if(rowNumber >= 6)
            return new Result(false, "Invalid row number");
        if(rowNumber >= 3){
            playerIndex = 1 - playerIndex;
            rowNumber -= 3;
        }
        ArrayList<String> cards = new ArrayList<>();
        cards.add(user.getCurrentGameBoard().getRowShownScore(playerIndex, rowNumber) + "");
        cards.add(user.getCurrentGameBoard().getSpecialCard(playerIndex, rowNumber).getInformation() + "");
        for(Card card : user.getCurrentGameBoard().getRows()[playerIndex][rowNumber]){
            cards.add(card.getInformation());
        }
        return new Result(true, cards);
    }

    public static Result showSpellsInPlay(User user){
        GameBoard gameBoard = user.getCurrentGameBoard();
        ArrayList<String> spells = new ArrayList<>();
        for(Spell spell : gameBoard.getWeather())
            spells.add(spell.getInformation());
        return new Result(true, spells);
    }

    public static void placeCard(User user, Matcher matcher){
        int cardNumber = Integer.parseInt(matcher.group("cardNumber"));
        String rowNumberString = matcher.group("rowNumber");
        int rowNumber = -1;
        if(rowNumberString != null)
            rowNumber = Integer.parseInt(rowNumberString);
        int playerIndex = user.getCurrentGameBoard().getPlayerNumber(user);
        GameBoard gameBoard = user.getCurrentGameBoard();
        Card card = user.getHand().get(cardNumber);
        if(card instanceof Soldier){
            Soldier soldier = (Soldier) card;
            if(soldier.hasAttribute(Attribute.SPY))
                playerIndex = 1 - playerIndex;
            gameBoard.addSoldierToRow(playerIndex, rowNumber, soldier);

        } else{
            Spell spell = (Spell) card;
            if(!spell.isWeather())
                gameBoard.addSpecialCard(playerIndex, rowNumber, spell);
        }
        card.executeAction();
    }

    public static void addWeatherAndRemoveFromDeck (Spell spell){
        if(spell == null)
            return;
        GameBoard gameBoard = spell.getGameBoard();
        int placedNumber = spell.getPlacedNumberInDeck();
        gameBoard.addWeather(spell);
        spell.getUser().getDeck().remove(spell);
        spell.getSender().sendCommandWithOutResponse("move weather from deck to it's place and play it " + placedNumber + " 0");
        if(gameBoard.isGameOnline())
            spell.getSender().getUser().getOpponent().getSender().sendCommandWithOutResponse("move weather from deck to it's place and play it " + placedNumber + " 1");
        gameBoard.addLog("move weather from deck to it's place and play it " + placedNumber + " 0", gameBoard.getPlayerNumber(spell.getUser()));
        gameBoard.addLog("move weather from deck to it's place and play it " + placedNumber + " 1", 1 - gameBoard.getPlayerNumber(spell.getUser()));
    }

    public static void seeThreeRandomCardsFromOpponentsHand(Sender sender){
        sender.sendCommand("see three random cards from opponent's hand");
    }

    public static GameBoardin getGameBoardin(User user, Sender sender, int wantsNew){
        try {
            User lastUser = user.getCurrentGameBoard().isGameOnline() ? user : user.getOpponent();
            if(wantsNew == 0 && lastUser.getSavedGameBoardin() != null)
                return lastUser.getSavedGameBoardin();
            GameBoardin gameBoardin = new GameBoardin(user);
            return gameBoardin;
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static void moveCardFromDeckToHand(Sender sender, Card card) {
        int placedNumber = card.getPlacedNumberInDeck();
        card.getUser().getDeck().remove(placedNumber);
        card.getUser().getHand().add(card);
        sender.sendCommandWithOutResponse("move soldier " + placedNumber + " from deck to hand 0");
        if(card.getUser().getCurrentGameBoard().isGameOnline())
            sender.getUser().getOpponent().getSender().sendCommandWithOutResponse("move soldier " + placedNumber + " from deck to hand 1");
        card.getGameBoard().addLog("move soldier " + placedNumber + " from deck to hand 0",
                card.getGameBoard().getPlayerNumber(card.getUser()));
        card.getGameBoard().addLog("move soldier " + placedNumber + " from deck to hand 1",
                1 - card.getGameBoard().getPlayerNumber(card.getUser()));
    }

    public static void moveCardFromDiscardToHand(Sender sender, Card card) {
        int placedNumber = card.getPlacedNumberInDeck();
        card.getUser().getDiscardPile().remove(placedNumber);
        card.getUser().getHand().add(card);
        sender.sendCommandWithOutResponse("move soldier " + placedNumber + " from discard pile to hand 0");
        if(card.getUser().getCurrentGameBoard().isGameOnline())
            sender.getUser().getOpponent().getSender().sendCommandWithOutResponse("move soldier " + placedNumber + " from discard pile to hand 1");
        card.getGameBoard().addLog("move soldier " + placedNumber + " from discard pile to hand 0",
                card.getGameBoard().getPlayerNumber(card.getUser()));
        card.getGameBoard().addLog("move soldier " + placedNumber + " from discard pile to hand 1",
                1 - card.getGameBoard().getPlayerNumber(card.getUser()));
    }

    public static void moveCardFromOpponentDiscardPileToHand(Sender sender, Card card) {
        int placedNumber = card.getPlacedNumberInDiscardPile();
        card.getUser().getOpponent().getDiscardPile().remove(placedNumber);
        card.getUser().getHand().add(card);
        sender.sendCommandWithOutResponse("move soldier " + placedNumber + " from discard pile to hand 1");
        if(card.getUser().getCurrentGameBoard().isGameOnline())
            sender.getUser().getOpponent().getSender().sendCommandWithOutResponse("move soldier " + placedNumber + " from discard pile to hand 0");
        card.getGameBoard().addLog("move soldier " + placedNumber + " from discard pile to hand 1",
                card.getGameBoard().getPlayerNumber(card.getUser()));
        card.getGameBoard().addLog("move soldier " + placedNumber + " from discard pile to hand 0",
                1 - card.getGameBoard().getPlayerNumber(card.getUser()));
    }

    public static void moveCardFromDeckToRow(Sender sender, Card card, int rowNumber) {
        User user = card.getUser();
        GameBoard gameBoard = user.getCurrentGameBoard();
        int placedNumber = card.getPlacedNumberInDeck();
        user.getDeck().remove(placedNumber);
        gameBoard.addSoldierToRow(gameBoard.getPlayerNumber(user), rowNumber, (Soldier) card);
        gameBoard.setPlayerScore(gameBoard.getPlayerNumber(user), gameBoard.getPlayerScore(gameBoard.getPlayerNumber(user)) + ((Soldier) card).getShownHp());
        sender.sendCommandWithOutResponse("move soldier " + placedNumber + " from deck to row " + rowNumber + " " + 0);
        if(gameBoard.isGameOnline())
            sender.getUser().getOpponent().getSender().sendCommandWithOutResponse("move soldier " + placedNumber + " from deck to row " + rowNumber + " " + 1);
        gameBoard.addLog("move soldier " + placedNumber + " from deck to row " + rowNumber + " 0", gameBoard.getPlayerNumber(user));
        gameBoard.addLog("move soldier " + placedNumber + " from deck to row " + rowNumber + " 1", 1 - gameBoard.getPlayerNumber(user));
    }

    public static void moveCardFromHandToRow(Sender sender, Card card, int rowNumber) {
        User user = card.getUser();
        GameBoard gameBoard = user.getCurrentGameBoard();
        int placedNumber = user.getHand().indexOf(card);
        user.getHand().remove(placedNumber);
        gameBoard.addSoldierToRow(gameBoard.getPlayerNumber(user), rowNumber, (Soldier) card);
        gameBoard.setPlayerScore(gameBoard.getPlayerNumber(user), gameBoard.getPlayerScore(gameBoard.getPlayerNumber(user)) + ((Soldier) card).getShownHp());
        sender.sendCommandWithOutResponse("move soldier " + placedNumber + " from hand to row " + rowNumber + " " + 0);
        if(gameBoard.isGameOnline())
            sender.getUser().getOpponent().getSender().sendCommandWithOutResponse("move soldier " + placedNumber + " from hand to row " + rowNumber + " " + 1);
        gameBoard.addLog("move soldier " + placedNumber + " from hand to row " + rowNumber + " 0", gameBoard.getPlayerNumber(user));
        gameBoard.addLog("move soldier " + placedNumber + " from hand to row " + rowNumber + " 1", 1 - gameBoard.getPlayerNumber(user));
    }

    public static void endGame(String winner, User user1, User user2) {
        user1.getSender().sendCommandWithOutResponse("end game " + winner);
        if(user1.getCurrentGameBoard().isGameOnline())
            user2.getSender().sendCommandWithOutResponse("end game " + winner);
        GameBoard gameBoard = user1.getCurrentGameBoard();
        gameBoard.addLog("end game " + winner, 0);
        gameBoard.addLog("end game " + winner, 1);
    }

    public static void clearGame(User player1, User player2) {
        GameBoard gameBoard = player1.getCurrentGameBoard();
        gameBoard.clearGameBoard();
        /* // will be called in graphic
        player1.getSender().sendCommandWithOutResponse("clear game");
        if(gameBoard.isGameOnline())
            player2.getSender().sendCommandWithOutResponse("clear game");

         */
        gameBoard.addLog("clear game", 0);
        gameBoard.addLog("clear game", 1);
    }
}
