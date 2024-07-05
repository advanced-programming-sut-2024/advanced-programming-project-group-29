package Server.Model;

import Server.Controller.ApplicationController;

import java.util.ArrayList;

public class GameBoardin {
    int player1Crystal = 2;
    int player2Crystal = 2;
    int currentPlayerIndex = 1;
    ArrayList<Cardin> player1Hand;
    ArrayList<Cardin> player2Hand;
    ArrayList<Cardin> player1Deck;
    ArrayList<Cardin> player2Deck;
    ArrayList<Cardin> player1Discard;
    ArrayList<Cardin> player2Discard;
    ArrayList<Cardin> row11;
    ArrayList<Cardin> row12;
    ArrayList<Cardin> row13;
    ArrayList<Cardin> row21;
    ArrayList<Cardin> row22;
    ArrayList<Cardin> row23;
    String player1Username;
    String player2Username;
    String player1Faction;
    String player2Faction;
    String player1Commander;
    String player2Commander;
    boolean player1CommanderHasAction;
    boolean player2CommanderHasAction;
    int row11XP;
    int row12XP;
    int row13XP;
    int row21XP;
    int row22XP;
    int row23XP;
    int player1XP;
    int player2XP;
    boolean inProcess;

    public GameBoardin(User user) {
        try {
            GameBoard gameBoard = user.getCurrentGameBoard();
            int playerIndex = gameBoard.getPlayerNumber(user);
            int opponentIndex = 1 - playerIndex;
            User user1 = gameBoard.getPlayer(playerIndex);
            User user2 = gameBoard.getPlayer(opponentIndex);
            inProcess = user1.getInProcess() && user2.getInProcess();
            player1Hand = new ArrayList<>();
            player2Hand = new ArrayList<>();
            player1Deck = new ArrayList<>();
            player2Deck = new ArrayList<>();
            player1Discard = new ArrayList<>();
            player2Discard = new ArrayList<>();
            row11 = new ArrayList<>();
            row12 = new ArrayList<>();
            row13 = new ArrayList<>();
            row21 = new ArrayList<>();
            row22 = new ArrayList<>();
            row23 = new ArrayList<>();
            player1Crystal = gameBoard.getPlayerCrystals(playerIndex);
            player2Crystal = gameBoard.getPlayerCrystals(opponentIndex);
            System.out.println(user1.getUsername());
            System.out.println(user1.getHand().size());
            for (Card card : user1.getHand())
                player1Hand.add(new Cardin(card));
            for (Card card : user2.getHand())
                player2Hand.add(new Cardin(card));
            for (Card card : user1.getDeck())
                player1Deck.add(new Cardin(card));
            for (Card card : user2.getDeck())
                player2Deck.add(new Cardin(card));
            for (Card card : user1.getDiscardPile())
                player1Discard.add(new Cardin(card));
            for (Card card : user2.getDiscardPile())
                player2Discard.add(new Cardin(card));
            for (Card card : gameBoard.getRows()[playerIndex][2])
                row11.add(new Cardin(card));
            for (Card card : gameBoard.getRows()[playerIndex][1])
                row12.add(new Cardin(card));
            for (Card card : gameBoard.getRows()[playerIndex][0])
                row13.add(new Cardin(card));
            for (Card card : gameBoard.getRows()[opponentIndex][2])
                row21.add(new Cardin(card));
            for (Card card : gameBoard.getRows()[opponentIndex][1])
                row22.add(new Cardin(card));
            for (Card card : gameBoard.getRows()[opponentIndex][0])
                row23.add(new Cardin(card));
            player1Username = user1.getUsername();
            player2Username = user2.getUsername();
            player1Faction = user1.getFaction().getName();
            player2Faction = user2.getFaction().getName();
            player1Commander = user1.getCommander().getName();
            player2Commander = user2.getCommander().getName();
            player1CommanderHasAction = user1.getCommander().hasAction();
            player2CommanderHasAction = user2.getCommander().hasAction();
            row11XP = gameBoard.getRowShownScore(playerIndex, 2);
            row12XP = gameBoard.getRowShownScore(playerIndex, 1);
            row13XP = gameBoard.getRowShownScore(playerIndex, 0);
            row21XP = gameBoard.getRowShownScore(opponentIndex, 2);
            row22XP = gameBoard.getRowShownScore(opponentIndex, 1);
            row23XP = gameBoard.getRowShownScore(opponentIndex, 0);
            player1XP = gameBoard.getPlayerScore(playerIndex);
            player2XP = gameBoard.getPlayerScore(opponentIndex);
            currentPlayerIndex = gameBoard.getCurrentPlayerIndex() + 1;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Cardin> getPlayer2Deck() {
        return player2Deck;
    }

    public ArrayList<Cardin> getPlayer1Discard() {
        return player1Discard;
    }

    public ArrayList<Cardin> getPlayer2Discard() {
        return player2Discard;
    }

    public String getPlayer1Username() {
        return player1Username;
    }

    public String getPlayer2Username() {
        return player2Username;
    }

    public String getPlayer1Faction() {
        return player1Faction;
    }

    public String getPlayer2Faction() {
        return player2Faction;
    }

    public String getPlayer1Commander() {
        return player1Commander;
    }

    public String getPlayer2Commander() {
        return player2Commander;
    }

    public boolean isPlayer1CommanderHasAction() {
        return player1CommanderHasAction;
    }

    public boolean isPlayer2CommanderHasAction() {
        return player2CommanderHasAction;
    }

    public int getRow11XP() {
        return row11XP;
    }

    public int getRow12XP() {
        return row12XP;
    }

    public int getRow13XP() {
        return row13XP;
    }

    public int getRow21XP() {
        return row21XP;
    }

    public int getRow22XP() {
        return row22XP;
    }

    public int getRow23XP() {
        return row23XP;
    }

    public int getPlayer1XP() {
        return player1XP;
    }

    public int getPlayer2XP() {
        return player2XP;
    }

    public int getPlayer1Crystal() {
        return player1Crystal;
    }

    public int getPlayer2Crystal() {
        return player2Crystal;
    }

    public ArrayList<Cardin> getPlayer1Hand() {
        return player1Hand;
    }

    public ArrayList<Cardin> getPlayer2Hand() {
        return player2Hand;
    }

    public ArrayList<Cardin> getPlayer1Deck() {
        return player1Deck;
    }

    public ArrayList<Cardin> getRow11() {
        return row11;
    }

    public ArrayList<Cardin> getRow12() {
        return row12;
    }

    public ArrayList<Cardin> getRow13() {
        return row13;
    }

    public ArrayList<Cardin> getRow21() {
        return row21;
    }

    public ArrayList<Cardin> getRow22() {
        return row22;
    }

    public ArrayList<Cardin> getRow23() {
        return row23;
    }

    public boolean isInProcess() {
        return inProcess;
    }
}
