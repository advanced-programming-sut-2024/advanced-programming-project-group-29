package Server.Model;

import com.google.gson.Gson;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import Server.Enum.Faction;

public class DatabaseManager {
    private static final String URL = "jdbc:sqlite:/home/radal/IdeaProjects/advanced-programming-project-group-29/src/main/resources/sqlite/Users.db";

    public static void connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(URL);
            if (conn != null) {
                System.out.println("Connected to the database.");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    public static void insertUser(User user) {
        if (userExists(user.getUsername()))
            return;

        String sql = "INSERT INTO User(username, password, nickname, email, questionNumber, answer, faction, commander, deck) VALUES(?,?,?,?,?,?,?,?,?)";

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
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static boolean userExists(String username) {
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

    public static void updateUser(User user, String oldUsername) {
        String sql = "UPDATE User SET username = ?, password = ?, nickname = ?, email = ?, questionNumber = ?, answer = ?, faction = ?, commander = ?, deck = ? WHERE username = ?";

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
            pstmt.setString(10, oldUsername);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static ArrayList<User> loadUsers() {
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

                User user = new User(username, password, nickname, email);
                user.setQuestion(questionNumber, answer);
                user.setFaction(Faction.getFactionFromString(faction));
                user.setCommander(new Commander(commander, user));
                user.extractDeckFromDeckNames(deckNames);
                users.add(user);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return users;
    }
}
