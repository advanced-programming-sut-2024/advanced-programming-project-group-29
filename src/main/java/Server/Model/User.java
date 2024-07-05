package Server.Model;

import Server.Enum.Faction;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
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
    private ArrayList<Card> preDeck = new ArrayList<>();
    private transient ArrayList<Card> discardPile = new ArrayList<>();
    private Faction faction;
    private Commander commander;
    private Sender sender;
    private transient GameBoard currentGameBoard;
    private final ArrayList<GameHistory> gameHistory = new ArrayList<>();
    private final ArrayList<User> friends = new ArrayList<>();
    private final ArrayList<User> friendRequests = new ArrayList<>();
    private final HashMap<String, SavedDeck> savedDecks = new HashMap<>();
    private boolean inProcess = false;

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

    public void setQuestion(int questionNumber, String answer) {
        this.questionNumber = questionNumber;
        this.answer = answer;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Faction getFaction() {
        return faction;
    }

    public void setFaction(Faction faction) {
        this.faction = faction;
        this.commander = new Commander(faction.getCommanders().get(0), this);
    }

    public Commander getCommander() {
        return commander;
    }

    public void setCommander(Commander commander) {
        this.commander = commander;
    }

    public ArrayList<String> getFriendRequests() {
        ArrayList<String> friendRequestsNames = new ArrayList<>();
        for (User user : friendRequests) friendRequestsNames.add(user.username);
        return friendRequestsNames;
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

    public void addCardToDiscardPile(Card card) {
        this.discardPile.add(card);
    }

    public void removeCardFromDiscardPile(Card card) {
        this.discardPile.remove(card);
    }

    public ArrayList<Card> getDiscardPile() {
        return discardPile;
    }

    public void addGameHistory(GameHistory gameHistory) {
        this.gameHistory.add(gameHistory);
    }

    public ArrayList<GameHistory> getGameHistory() {
        return gameHistory;
    }

    public String getQuestion() {
        return securityQuestions[questionNumber];
    }

    public String getAnswer() {
        return answer;
    }

    public boolean hasUserAnswerTheQuestion() {
        return answer != null;
    }

    public int getHighestScore() {
        int highestScore = 0;
        for (GameHistory gameHistory : this.gameHistory) {
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
        for (GameHistory gameHistory : this.gameHistory) {
            int playerNumber = gameHistory.getPlayerNumber(this);
            if (gameHistory.getWinner() == playerNumber)
                numberOfWins++;
        }
        return numberOfWins;
    }

    public int getNumberOfDraws() {
        int numberOfDraws = 0;
        for (GameHistory gameHistory : this.gameHistory) {
            if (gameHistory.getWinner() == -1)
                numberOfDraws++;
        }
        return numberOfDraws;
    }

    public int getNumberOfLosses() {
        return this.gameHistory.size() - this.getNumberOfWins() - this.getNumberOfDraws();
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

    public static <Gson> void saveUser() throws IOException {
        FileWriter fileWriter = new FileWriter("src/main/resources/JSON/allUsers.json");
        com.google.gson.Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(allUsers);
        fileWriter.write(json);
        fileWriter.close();
    }


    public static void loadUser(){
        System.out.println("haaaaaa");
        try {
            Gson gson = new Gson();
            String text = new String(Files.readAllBytes(Paths.get("src/main/resources/JSON/allUsers.json")));
            ArrayList<User> users = gson.fromJson(text, new TypeToken<List<User>>() {
            }.getType());
            System.out.println(users.size());
            if (users == null)
                users = new ArrayList<>();
            for (User user : users) {
                user.hand = new ArrayList<>();
                user.deck = new ArrayList<>();
                user.discardPile = new ArrayList<>();
                user.commander.setUser(user);
                user.commander.setGameBoard(null);
                user.currentGameBoard = null;
                if (user.preDeck != null) {
                    for (Card card : user.preDeck) {
                        card.setUser(user);
                        user.deck.add(card);
                    }
                }
            }
            User.setAllUsers(users);
        } catch (Exception e) {
            System.out.println("hey that wasnt' completed");
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
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

    public void addFriend(User user) {
        friends.add(user);
    }

    public void acceptFriendRequest(User user) {
        friendRequests.remove(user);
        friends.add(user);
        user.addFriend(this);
    }

    public void rejectFriendRequest(User user) {
        friendRequests.remove(user);
    }

    public void receiveFriendRequest(User user) {
        if (!friends.contains(user) && !friendRequests.contains(user))
            friendRequests.add(user);
    }

    public void sendFriendRequest(User user) {
        user.receiveFriendRequest(this);
    }

    public String getStatusFriendRequest(User user) {
        if (friends.contains(user))
            return "Friend";
        if (friendRequests.contains(user))
            return "Pending";
        return "Not Sent Yet";
    }

    public void createHand() { // have all cards in deck, it will remove all in hand
        System.out.println("creating hand for " + username);
        hand.clear();
        ArrayList<Card> keepDeck = new ArrayList<>(deck);
        System.out.println("we have deck size: " + deck.size());
        Collections.shuffle(keepDeck);
        for (int i = 0; i < Math.min(10, keepDeck.size()); i++) {
            hand.add(keepDeck.get(i));
        }
        for (Card card : hand) {
            deck.remove(card);
        }
        System.out.println("and finaly hand size" + hand.size());
    }

    public void setAllCardsSenders(Sender sender) {
        for (Card card : discardPile)
            card.setSender(sender);
        for (Card card : hand)
            card.setSender(sender);
        for (Card card : deck)
            card.setSender(sender);
        if (currentGameBoard != null)
            currentGameBoard.setAllCardsForUserSender(sender, this);
    }

    public String getJWT() {
        System.out.println("oh all hre");
        String subject = username + "%&*" + password;
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        long expMillis = nowMillis + 1000 * EXPIATION_TIME;
        Date exp = new Date(expMillis);
        System.out.println("well done?");
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
}
