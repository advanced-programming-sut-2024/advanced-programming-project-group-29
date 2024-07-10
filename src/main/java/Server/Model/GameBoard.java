package Server.Model;

import Server.Controller.InGameMenuController;
import Server.Enum.Attribute;

import java.util.ArrayList;
import java.util.Date;


public class GameBoard {
    private final User[] players = new User[2];
    private int currentPlayer;
    private final ArrayList<Soldier>[][] rows = new ArrayList[2][3];
    private final Spell[][] specialCards = new Spell[2][3];
    private final int[] playersScore = new int[2];
    private final int[] playerCardsNumber = new int[2];
    private final int[] playersCrystals = new int[2];
    private final boolean[] rowHasWeather = new boolean[3];
    private final ArrayList<Spell> weather = new ArrayList<>();
    private final Commander[] playersLeaders = new Commander[2];
    private final GameHistory gameHistory;
    private int passTurnCalled = 0;
    private boolean isGameOnline = false;
    private final ChatBox chatBox = new ChatBox();
    private final GameLog[] gameLog = new GameLog[2];

    public GameBoard(User player1, User player2, boolean isGameOnline) {
        player1.setCurrentGameBoard(this);
        player2.setCurrentGameBoard(this);
        this.isGameOnline = isGameOnline;
        playersCrystals[0] = 2;
        playersCrystals[1] = 2;
        players[0] = player1;
        players[1] = player2;
        gameLog[0] = new GameLog(player1.getUsername(), player2.getUsername());
        gameLog[1] = new GameLog(player2.getUsername(), player1.getUsername());
        gameHistory = new GameHistory(player1, player2, new Date(), gameLog[0], gameLog[1]);
        player1.addGameHistory(gameHistory);
        player2.addGameHistory(gameHistory);
        currentPlayer = 0;
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 3; j++) {
                rows[i][j] = new ArrayList<>();
            }
        }
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 3; j++) {
                specialCards[i][j] = null;
            }
        }
        for (int i = 0; i < 2; i++) {
            playersScore[i] = 0;
        }
        for (int i = 0; i < 2; i++) {
            playersLeaders[i] = null;
            for (Card card : players[i].getDeck())
                card.setGameBoard(this);
        }
    }

    public User getPlayer(int playerNumber) {
        return players[playerNumber];
    }

    public void setPlayer(int playerNumber, User user) {
        players[playerNumber] = user;
    }

    public ArrayList<Soldier>[][] getRows() {
        return rows;
    }

    public void addSoldierToRow(int playerNumber, int rowNumber, Soldier soldier) {
        rows[playerNumber][rowNumber].add(soldier);
    }

    public void removeSoldierFromRow(int playerNumber, int rowNumber, Soldier soldier) {
        rows[playerNumber][rowNumber].remove(soldier);
    }

    public int getPlayerScore(int playerNumber) {
        return playersScore[playerNumber];
    }

    public void setPlayerScore(int playerNumber, int score) {
        playersScore[playerNumber] = score;
    }

    public int getPlayerCardsNumber(int playerNumber) {
        return playerCardsNumber[playerNumber];
    }

    public int getPlayerCrystals(int playerNumber) {
        return playersCrystals[playerNumber];
    }

    public void setPlayerCrystals(int playerNumber, int crystals) {
        playersCrystals[playerNumber] = crystals;
    }

    public ArrayList<Spell> getWeather() {
        return weather;
    }


    public Spell getSpecialCard(int playerNumber, int rowNumber) {
        return specialCards[playerNumber][rowNumber];
    }

    public void addSpecialCard(int playerNumber, int rowNumber, Spell spell) {
        specialCards[playerNumber][rowNumber] = spell;
    }

    public Commander getPlayerLeader(int playerNumber) {
        return playersLeaders[playerNumber];
    }

    public void setPlayerLeader(int playerNumber, Commander commander) {
        playersLeaders[playerNumber] = commander;
    }

    public int getPlayerNumber(User user) {
        return players[0].equals(user) ? 0 : 1;
    }

    public boolean getCommanderOwner(Commander commander) {
        for (int i = 0; i < 2; i++) {
            if (playersLeaders[i].equals(commander)) {
                return i == currentPlayer;
            }
        }
        return false;
    }

    public void changeTurn() {
        currentPlayer = 1 - currentPlayer;
    }

    public void resetBoard() {
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 3; j++) {
                rows[i][j].clear();
            }
        }
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 3; j++) {
                specialCards[i][j] = null;
            }
        }
        for (int i = 0; i < 2; i++) {
            playersScore[i] = 0;
            playerCardsNumber[i] = 0;
            playersCrystals[i] = 0;
        }
        for (int i = 0; i < 2; i++) {
            playersLeaders[i] = null;
        }
    }

    public void getRandomCardFromDeckAndAddItToHand(Sender sender, int playerIndex) {
        Card card = players[playerIndex].getCardFromDeckRandomly();
        InGameMenuController.moveCardFromDeckToHand(sender, card);
    }

    public User[] getPlayers() {
        return players;
    }

    public void clearAllWeather(Sender sender) {
        rowHasWeather[0] = false;
        rowHasWeather[1] = false;
        rowHasWeather[2] = false;
        weather.clear();
        recalculatePlayersScore();
        InGameMenuController.removeAllWeatherInGraphic(sender);
    }

    private void recalculatePlayersScore() {
        for (int i = 0; i < 2; i++) {
            playersScore[i] = 0;
            for (int j = 0; j < 3; j++) {
                if (rowHasWeather[j]) {
                    playersScore[i] += rows[i][j].size();
                    continue;
                }
                for (Soldier soldier : rows[i][j]) {
                    playersScore[i] += soldier.getShownHp();
                }
            }
        }
    }

    public void addWeather(Spell spell) {
        weather.add(spell);
        String name = spell.getName().toLowerCase();
        if (name.matches(".*biting.+frost.*"))
            addWeatherForRow(0);
        else if (name.matches(".*impenetrable.+fog.*"))
            addWeatherForRow(1);
        else if (name.matches(".*torrential.+rain.*"))
            addWeatherForRow(2);
        else if (name.matches(".*skellige.+storm.*")) {
            addWeatherForRow(1);
            addWeatherForRow(2);
        }
    }

    private void addWeatherForRow(int i) {
        rowHasWeather[i] = true;
        recalculatePlayersScore();
    }


    public boolean rowHasWeather(int rowNumber) {
        if (rowNumber < 0 || rowNumber > 2)
            return false;
        return rowHasWeather[rowNumber];
    }

    public boolean isThereAnyCommandersHornInRow(int playerNumber, int rowNumber) {
        if (specialCards[playerNumber][rowNumber] != null &&
                specialCards[playerNumber][rowNumber].getName().matches("Commander.+Horn"))
            return true;
        for (Soldier soldier : rows[playerNumber][rowNumber]) {
            if (soldier.getAttribute() == Attribute.getAttributeFromString("commanders horn"))
                return true;
        }
        return false;
    }

    public int getCurrentPlayer() {
        return currentPlayer;
    }

    public int getRowShownScore(int playerIndex, int rowNumber) {
        int scoreSum = 0;
        for (Soldier soldier : rows[playerIndex][rowNumber])
            scoreSum += soldier.getShownHp();
        return scoreSum;
    }

    public Soldier getRandomSoldier(User user) {
        int playerIndex = getPlayerNumber(user);
        ArrayList<Soldier> soldiers = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            soldiers.addAll(rows[playerIndex][i]);
        }
        return soldiers.get((int) (Math.random() * soldiers.size()));
    }

    public int getCurrentPlayerIndex() {
        return currentPlayer;
    }

    public void passTurnCalled(){
        passTurnCalled++;
    }

    public boolean isPassTurnCalled(){
        return passTurnCalled > 0;
    }

    public Result passTurn() {
        if (passTurnCalled == 2) {
            passTurnCalled = 0;
            if (playersScore[0] <= playersScore[1]) {
                playersCrystals[0]--;
            }
            if (playersScore[1] <= playersScore[0]) {
                playersCrystals[1]--;
            }
            gameHistory.addScoreForRound(playersScore[0], 0);
            gameHistory.addScoreForRound(playersScore[1], 1);
            for (int i = 0; i < 2; i++)
                DatabaseManager.updateUser(players[i], players[i].getUsername());
            executeActionForTransformers();
            String winner = playersScore[0] > playersScore[1] ? players[0].getUsername() : (playersScore[0] == playersScore[1] ? "" : players[1].getUsername());
            if (playersCrystals[0] == 0 || playersCrystals[1] == 0) {
                InGameMenuController.endGame(winner, players[0], players[1]);
                gameHistory.setWinner(playersScore[0] > playersScore[1] ? 0 : (playersScore[0] == playersScore[1] ? -1 : 1));
                players[0].setCurrentGameBoard(null);
                players[1].setCurrentGameBoard(null);
                return null;
            }
            InGameMenuController.clearGame(players[0], players[1]);
            if (playersScore[0] == playersScore[1])
                return new Result(false, "draw");
            return new Result(false, winner);
        }
        return new Result(true);
    }

    private void executeActionForTransformers() {
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 3; j++) {
                for (Soldier soldier : rows[i][j]) {
                    if (soldier.getAttribute() == Attribute.TRANSFORMERS) {
                        soldier.executeActionForTransformers(soldier);
                    }
                }
            }
        }
    }

    public boolean isGameOnline() {
        return isGameOnline;
    }

    public void addMessage(Message message) {
        chatBox.addMessage(message);
    }

    public ChatBox getChatBox() {
        return chatBox;
    }

    public void addLog(String command, int playerIndex) {
        gameLog[playerIndex].addGameBoardin(new GameBoardin(players[playerIndex]));
        gameLog[playerIndex].addCommand(command);
    }


    public void clearGameBoard() {
        for (int i = 0; i < 2; i++) {
            playersScore[i] = 0;
            for (int j = 0; j < 3; j++) {
                players[i].getDiscardPile().addAll(rows[i][j]);
                if (specialCards[i][j] != null)
                    players[i].getDiscardPile().add(specialCards[i][j]);
                rows[i][j].clear();
                specialCards[i][j] = null;
                rowHasWeather[j] = false;
            }
        }
        weather.clear();
    }
}
