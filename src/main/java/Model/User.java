package Model;

import java.util.ArrayList;
import java.util.Random;

import Enum.Faction;

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
    private String username;
    private String password;
    private String nickname;
    private String email;
    private int questionNumber;
    private String answer;
    private final ArrayList<Card> hand = new ArrayList<>();
    private final ArrayList<Card> deck = new ArrayList<>();
    private final ArrayList<Card> discardPile = new ArrayList<>();
    private Faction faction;
    private Commander commander;
    ArrayList<GameHistory> gameHistory = new ArrayList<>();

    public User(String username, String password, String nickname, String email) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.email = email;
        allUsers.add(this);
    }

    public static User getUserByUsername(String username) {
        for (User user : allUsers) {
            if (user.getUsername().equals(username))
                return user;
        }
        return null;
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
    }

    public Commander getCommander() {
        return commander;
    }

    public void setCommander(Commander commander) {
        this.commander = commander;
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

    public boolean hasUserAnswerTheQuestion(){
        return answer != null;
    }
}
