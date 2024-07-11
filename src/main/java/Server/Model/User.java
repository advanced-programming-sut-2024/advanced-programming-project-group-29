package Server.Model;

import Server.Controller.ApplicationController;
import Server.Controller.GameMenuController;
import Server.Enum.Faction;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.nio.charset.StandardCharsets;
import java.util.*;

public class User {
    private static final String[] securityQuestions = {
            "What is your favorite color?",
            "What is your favorite movie?",
            "What is your favorite book?",
            "What is your favorite food?",
            "What is your favorite animal?",
            "What is your favorite game?",
            "What is your favorite sport?",
            "What is your favorite hobby?"
    };

    private static ArrayList<User> allUsers = new ArrayList<>();
    private static User[] pendingUserForRandomPlay = new User[2];
    private static final int EXPIATION_TIME = 30;
    private static final String SECRET_KEY = "For the rest of your days, you will be known as Robin Hood.";
    private String username;
    private String password;
    private String nickname;
    private String email;
    private int questionNumber;
    private String answer;
    private transient ArrayList<Card> hand = new ArrayList<>();
    private transient ArrayList<Card> deck = new ArrayList<>();
    private transient ArrayList<Card> discardPile = new ArrayList<>();
    private Faction faction;
    private Commander commander;
    private transient Sender sender;
    private transient GameBoard currentGameBoard;
    private final ArrayList<GameHistory> gameHistories = new ArrayList<>();
    private final ArrayList<String> friends = new ArrayList<>();
    private final ArrayList<String> friendRequests = new ArrayList<>();
    private final HashMap<String, SavedDeck> savedDecks = new HashMap<>();
    private transient boolean inProcess = false;
    private transient int optionsType;
    private transient boolean waitForGame = false;
    private Tournament tournament;
    private GameBoardin savedGameBoardin;
    private ArrayList<ApplicationController> onlineStreamAudiences = new ArrayList<>();

    public User(String username, String password, String nickname, String email) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.email = email;
        this.faction = Faction.SCOIATAELL;
        this.commander = new Commander("queen of dol blathanna", this);
        allUsers.add(this);
    }

    public static User getUserByUsername(String username) {
        for (User user : allUsers) {
            if (user.getUsername().equals(username))
                return user;
        }
        return null;
    }

    public static ArrayList<String> getAllUsersByRank() {
        ArrayList<User> users = new ArrayList<>();
        for (User user : allUsers)
            users.add(user);
        users.sort((user1, user2) -> user2.getNumberOfWins() - user1.getNumberOfWins());
        ArrayList<String> usernames = new ArrayList<>();
        for (User user : users)
            usernames.add(user.getUsername());
        return usernames;
    }

    public static String[] getSecurityQuestions() {
        return securityQuestions;
    }

    public static void addUserToQueueForRandomPlay(User user) {
        if (pendingUserForRandomPlay[0] == user || pendingUserForRandomPlay[1] == user)
            return;
        if (pendingUserForRandomPlay[0] == null)
            pendingUserForRandomPlay[0] = user;
        else pendingUserForRandomPlay[1] = user;
    }

    public static void checkForPendingOpponentsFound() {
        if (pendingUserForRandomPlay[0] != null && pendingUserForRandomPlay[1] != null) {
            GameMenuController.startNewRandomGame(pendingUserForRandomPlay[0], pendingUserForRandomPlay[1]);
            pendingUserForRandomPlay[0] = null;
            pendingUserForRandomPlay[1] = null;
        }
    }

    public void setQuestion(int questionNumber, String answer) {
        this.questionNumber = questionNumber;
        this.answer = answer;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        String oldUsername = this.username;
        this.username = username;
        DatabaseManager.updateUser(this, oldUsername);
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
        DatabaseManager.updateUser(this, this.username);
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
        DatabaseManager.updateUser(this, this.username);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
        DatabaseManager.updateUser(this, this.username);
    }

    public Faction getFaction() {
        return faction;
    }

    public void setFaction(Faction faction) {
        this.faction = faction;
        this.commander = new Commander(faction.getCommanders().getFirst(), this);
        DatabaseManager.updateUser(this, this.username);
    }

    public Commander getCommander() {
        return commander;
    }

    public void setCommander(Commander commander) {
        this.commander = commander;
        DatabaseManager.updateUser(this, this.username);
    }

    public ArrayList<String> getFriendRequests() {
        return friendRequests;
    }

    public void addCardToHand(Card card) {
        this.hand.add(card);
    }

    public void removeCardFromHand(Card card) {
        this.hand.remove(card);
    }

    public ArrayList<Card> getHand() {
        return hand;
    }

    public void addCardToDeck(Card card) {
        this.deck.add(card);
        DatabaseManager.updateUser(this, this.username);
    }

    public void removeCardFromDeck(Card card) {
        this.deck.remove(card);
    }

    public ArrayList<Card> getDeck() {
        return deck;
    }

    public ArrayList<String> getDeckNames() {
        ArrayList<String> names = new ArrayList<>();
        for (Card card : deck)
            names.add(card.getName());
        return names;
    }

    public Card getCardFromDeckWithNumber(int number) {
        return deck.get(number);
    }

    public Card getCardFromDeckRandomly() {
        return deck.get((new Random()).nextInt(deck.size()));
    }

    public void replaceCardFromDeck(Card card) {
        int index = this.hand.indexOf(card);
        Card replaceCard = getCardFromDeckRandomly();
        this.hand.remove(card);
        this.hand.set(index, replaceCard);
        this.deck.remove(replaceCard);
        this.deck.add(card);
    }

    public ArrayList<Card> getDiscardPile() {
        return discardPile;
    }

    public void addGameHistory(GameHistory gameHistory) {
        this.gameHistories.add(gameHistory);
        DatabaseManager.updateUser(this, this.username);
    }

    public void setGameHistory(ArrayList<GameHistory> gameHistories) {
        this.gameHistories.clear();
        this.gameHistories.addAll(gameHistories);
        DatabaseManager.updateUser(this, this.username);
    }

    public ArrayList<GameHistory> getGameHistory() {
        return this.gameHistories;
    }

    public String getQuestion() {
        return securityQuestions[questionNumber];
    }

    public int getQuestionNumber() {
        return questionNumber;
    }

    public String getAnswer() {
        return answer;
    }

    public boolean hasUserAnswerTheQuestion() {
        return answer != null;
    }

    public int getHighestScore() {
        if (this.gameHistories.isEmpty())
            return 0;
        int highestScore = 0;
        for (GameHistory gameHistory : this.gameHistories) {
            int playerNumber = gameHistory.getPlayerNumber(this);
            for (int score : gameHistory.getScorePerRound(playerNumber)) {
                if (score > highestScore)
                    highestScore = score;
            }
        }
        return highestScore;
    }

    public int getNumberOfWins() {
        int numberOfWins = 0;
        for (GameHistory gameHistory : this.gameHistories) {
            int playerNumber = gameHistory.getPlayerNumber(this);
            if (gameHistory.getWinner() == playerNumber)
                numberOfWins++;
        }
        return numberOfWins;
    }

    public int getNumberOfDraws() {
        int numberOfDraws = 0;
        for (GameHistory gameHistory : this.gameHistories) {
            if (gameHistory.getWinner() == -1)
                numberOfDraws++;
        }
        return numberOfDraws;
    }

    public int getNumberOfLosses() {
        return this.gameHistories.size() - this.getNumberOfWins() - this.getNumberOfDraws();
    }

    public int getRank() {
        int rank = 1;
        for (User user : allUsers)
            if (user.getNumberOfWins() > this.getNumberOfWins())
                rank++;
        return rank;
    }

    public GameBoard getCurrentGameBoard() {
        return currentGameBoard;
    }

    public void setCurrentGameBoard(GameBoard currentGameBoard) {
        this.currentGameBoard = currentGameBoard;
    }

    public int getNumberOfOccurrenceInDeck(String cardName) {
        int count = 0;
        for (Card cardInDeck : deck) {
            if (cardInDeck.getName().equals(cardName))
                count++;
        }
        return count;
    }

    private static int getNumberOfOccurrenceInDeck(ArrayList<Card> deck, String cardName) {
        int count = 0;
        for (Card cardInDeck : deck) {
            if (cardInDeck.getName().equals(cardName))
                count++;
        }
        return count;
    }

    public void addCardToDeck(String cardName) {
        boolean isSoldier = Soldier.isSoldier(cardName);
        boolean isSpell = Spell.isSpell(cardName);
        if (!isSoldier && !isSpell)
            return;
        if (isSoldier) {
            Soldier soldier = new Soldier(cardName, this);
            this.deck.add(soldier);
        } else {
            Spell spell = new Spell(cardName, this);
            this.deck.add(spell);
        }
    }

    public void removeCardFromDeck(String cardName) {
        for (Card card : deck) {
            if (card.getName().equals(cardName)) {
                deck.remove(card);
                return;
            }
        }
    }

    public static void loadUser() {
        DatabaseManager.loadUsers();
    }

    public static void setAllUsers(ArrayList<User> allUsers) {
        User.allUsers = allUsers;
    }

    public int getNumberOfSpellsInDeck() {
        int count = 0;
        for (Card card : deck) {
            if (card instanceof Spell)
                count++;
        }
        return count;
    }

    public int getNumberOfSoldiersInDeck() {
        int count = 0;
        for (Card card : deck) {
            if (card instanceof Soldier)
                count++;
        }
        return count;
    }

    public SavedDeck getDeckByName(String name) {
        return savedDecks.get(name);
    }

    public void saveDeck(String name) {
        ArrayList<String> cardNames = new ArrayList<>();
        for (Card card : deck) {
            cardNames.add(card.getName());
        }
        SavedDeck savedDeck = new SavedDeck(cardNames, commander.getName(), faction.getName());
        savedDecks.put(name, savedDeck);
    }

    public void loadDeck(String name) {
        SavedDeck savedDeck = savedDecks.get(name);
        extractDataFromSavedDeck(savedDeck);
    }

    public void loadDeck(SavedDeck savedDeck) {
        extractDataFromSavedDeck(savedDeck);
    }

    public void extractDeckFromDeckNames(ArrayList<String> deckNames) {
        this.deck = new ArrayList<>();
        for (String cardName : deckNames) {
            if (Soldier.isSoldier(cardName))
                this.deck.add(new Soldier(cardName, this));
            else if (Spell.isSpell(cardName))
                this.deck.add(new Spell(cardName, this));
        }
        DatabaseManager.updateUser(this, this.username);
    }

    public boolean extractDataFromSavedDeck(SavedDeck savedDeck) {
        this.deck = new ArrayList<>();
        for (String cardName : savedDeck.getDeck()) {
            if (Soldier.isSoldier(cardName))
                this.deck.add(new Soldier(cardName, this));
            else if (Spell.isSpell(cardName))
                this.deck.add(new Spell(cardName, this));
            else
                return false;
        }
        this.commander = new Commander(savedDeck.getCommander(), this);
        this.faction = Faction.getFactionFromString(savedDeck.getFaction());
        DatabaseManager.updateUser(this, this.username);
        return true;
    }

    private static boolean isDeckValid(ArrayList<Card> deck) {
        int numberOfSpells = 0;
        for (Card card : deck) {
            if (card instanceof Spell)
                numberOfSpells++;
            if (!Card.checkIfValidCard(card))
                return false;
            if (getNumberOfOccurrenceInDeck(deck, card.getName()) > Card.getAllowedNumberByCardName(card.getName()))
                return false;
        }
        return numberOfSpells <= 10;
    }

    public void resetDeck() {
        deck.clear();
    }

    public User getOpponent() {
        return currentGameBoard.getPlayer(1 - currentGameBoard.getPlayerNumber(this));
    }

    public void addFriend(String username) {
        friends.add(username);
    }

    public void acceptFriendRequest(User user) {
        friendRequests.remove(user.getUsername());
        friends.add(user.getUsername());
        user.addFriend(this.username);
    }

    public void rejectFriendRequest(User user) {
        friendRequests.remove(user.getUsername());
    }

    public void receiveFriendRequest(User user) {
        if (!friends.contains(user.getUsername()) && !friendRequests.contains(user.username))
            friendRequests.add(user.getUsername());
    }

    public void sendFriendRequest(User user) {
        user.receiveFriendRequest(this);
    }

    public String getStatusFriendRequest(User user) {
        if (friends.contains(user.username))
            return "Friend";
        if (friendRequests.contains(user.getUsername()))
            return "Pending";
        return "Not Sent Yet";
    }

    public void createHand() { // have all cards in deck, it will remove all in hand
        hand.clear();
        ArrayList<Card> keepDeck = new ArrayList<>(deck);
        Collections.shuffle(keepDeck);
        for (int i = 0; i < Math.min(10, keepDeck.size()); i++) {
            hand.add(keepDeck.get(i));
        }
        for (Card card : hand) {
            deck.remove(card);
        }
    }

    public String getJWT() {
        String subject = username + "%&*" + password;
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        long expMillis = nowMillis + 1000 * EXPIATION_TIME;
        Date exp = new Date(expMillis);
        try {
            String jwt = Jwts.builder()
                    .setSubject(subject)
                    .setIssuedAt(now)
                    //.setExpiration(exp) // TODO: set this line when pop up menu completed
                    .signWith(SignatureAlgorithm.HS256, SECRET_KEY.getBytes(StandardCharsets.UTF_8))
                    .compact();
            System.out.println(jwt);
            return jwt;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean checkJWT(String jwt) {
        try {
            Jwts.parser().setSigningKey(SECRET_KEY.getBytes(StandardCharsets.UTF_8)).parseClaimsJws(jwt);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void setSender(Sender sender) {
        this.sender = sender;
    }

    public Sender getSender() {
        return sender;
    }

    public void setInProcess(boolean inProcess) {
        this.inProcess = inProcess;
    }

    public boolean getInProcess() {
        return inProcess;
    }

    public void setOptionsType(int optionsType) {
        this.optionsType = optionsType;
    }

    public int getOptionsType() {
        return optionsType;
    }

    public void setWaitForGame(boolean waitForGame) {
        this.waitForGame = waitForGame;
    }

    public boolean getWaitForGame() {
        return waitForGame;
    }

    public Tournament getTournament() {
        return tournament;
    }

    public void setTournament(Tournament tournament) {
        this.tournament = tournament;
    }

    public void saveGameBoardin() {
        this.savedGameBoardin = new GameBoardin(this);
    }

    public GameBoardin getSavedGameBoardin() {
        return savedGameBoardin;
    }

    public void endGame() {
        currentGameBoard = null;
        hand.clear();
        deck.clear();
        discardPile.clear();
        inProcess = false;
        savedGameBoardin = null;
        onlineStreamAudiences.clear();
    }

    public boolean checkIfFriend (String username) {
        return friends.contains(username);
    }

    public ArrayList<String> getFriends() {
        return friends;
    }

    public void setFriends(ArrayList<String> friends) {
        this.friends.clear();
        this.friends.addAll(friends);
    }

    public void setFriendRequests(ArrayList<String> friendRequests) {
        this.friendRequests.clear();
        this.friendRequests.addAll(friendRequests);
    }

    public ArrayList<ApplicationController> getOnlineStreamAudiences() {
        return onlineStreamAudiences;
    }

    public void addOnlineStreamAudience(ApplicationController applicationController) {
        onlineStreamAudiences.add(applicationController);
    }


}
