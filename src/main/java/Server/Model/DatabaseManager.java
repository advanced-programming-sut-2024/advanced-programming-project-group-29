package Server.Model;

import Server.Enum.Faction;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.sql.*;
import java.util.ArrayList;

public class DatabaseManager {
    private static final String URL = "jdbc:sqlite:" + "src/main/resources/sqlite/Users.db";

    public synchronized static void insertUser(User user) {
        if (userExists(user.getUsername()))
            return;

        String sql = "INSERT INTO User(username, password, nickname, email, questionNumber, answer, faction, commander, deck, gameHistory, friends, friendRequests) VALUES(?,?,?,?,?,?,?,?,?,?,?,?)";

        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getPassword());
            pstmt.setString(3, user.getNickname());
            pstmt.setString(4, user.getEmail());
            pstmt.setInt(5, user.getQuestionNumber());
            pstmt.setString(6, user.getAnswer());
            pstmt.setString(7, user.getFaction().getName());
            pstmt.setString(8, user.getCommander().getName());
            ArrayList<String> deckNames = user.getDeckNames();
            String jsonDeck = new Gson().toJson(deckNames);
            pstmt.setString(9, jsonDeck);
            ArrayList<GameHistory> gameHistories = user.getGameHistory();
            String jsonGameHistory = new Gson().toJson(gameHistories);
            pstmt.setString(10, jsonGameHistory);
            pstmt.setString(11, new Gson().toJson(user.getFriends()));
            pstmt.setString(12, new Gson().toJson(user.getFriendRequests()));
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public synchronized static boolean userExists(String username) {
        String sql = "SELECT COUNT(*) FROM User WHERE username = ?";
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    public synchronized static void updateUser(User user, String oldUsername) {
        String sql = "UPDATE User SET username = ?, password = ?, nickname = ?, email = ?, questionNumber = ?, answer = ?, faction = ?, commander = ?, deck = ?, gameHistory = ?, friends = ?, friendRequests = ? WHERE username = ?";

        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getPassword());
            pstmt.setString(3, user.getNickname());
            pstmt.setString(4, user.getEmail());
            pstmt.setInt(5, user.getQuestionNumber());
            pstmt.setString(6, user.getAnswer());
            pstmt.setString(7, user.getFaction().getName());
            pstmt.setString(8, user.getCommander().getName());
            ArrayList<String> deckNames = user.getDeckNames();
            String jsonDeck = new Gson().toJson(deckNames);
            pstmt.setString(9, jsonDeck);
            ArrayList<GameHistory> gameHistories = user.getGameHistory();
            String jsonGameHistory = new Gson().toJson(gameHistories);
            pstmt.setString(10, jsonGameHistory);
            pstmt.setString(11, new Gson().toJson(user.getFriends()));
            pstmt.setString(12, new Gson().toJson(user.getFriendRequests()));
            pstmt.setString(13, oldUsername);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public synchronized static ArrayList<User> loadUsers() {
        String sql = "SELECT * FROM User";
        ArrayList<User> users = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String username = rs.getString("username");
                String password = rs.getString("password");
                String nickname = rs.getString("nickname");
                String email = rs.getString("email");
                int questionNumber = rs.getInt("questionNumber");
                String answer = rs.getString("answer");
                String faction = rs.getString("faction");
                String commander = rs.getString("commander");
                String jsonDeck = rs.getString("deck");
                ArrayList<String> deckNames = new Gson().fromJson(jsonDeck, ArrayList.class);
                String jsonGameHistory = rs.getString("gameHistory");
                Type gameHistoryListType = new TypeToken<ArrayList<GameHistory>>() {
                }.getType();
                ArrayList<GameHistory> gameHistories = new Gson().fromJson(jsonGameHistory, gameHistoryListType);
                ArrayList<String> friends = new Gson().fromJson(rs.getString("friends"), ArrayList.class);
                ArrayList<String> friendRequests = new Gson().fromJson(rs.getString("friendRequests"), ArrayList.class);

                User user = new User(username, password, nickname, email);
                user.setQuestion(questionNumber, answer);
                user.setFaction(Faction.getFactionFromString(faction));
                user.setCommander(new Commander(commander, user));
                user.extractDeckFromDeckNames(deckNames);
                user.setGameHistory(gameHistories);
                user.setFriends(friends);
                user.setFriendRequests(friendRequests);
                users.add(user);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return users;
    }
}
