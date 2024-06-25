package Controller;

import Model.Result;
import Model.User;

import java.awt.print.Printable;
import java.util.ArrayList;
import java.util.regex.Matcher;

public class ProfileMenuController {
    public static Result changeUsername(Matcher matcher) {
        String username = matcher.group("username");
        if (username.equals(ApplicationController.getCurrentUser().getUsername())) {
            return new Result(false, "You entered your current username.");
        }
        if (User.getUserByUsername(username) != null) {
            return new Result(false, "Username is already taken.");
        }
        if (!username.matches("[a-zA-Z0-9-]+")) {
            return new Result(false, "Username is invalid. It should contain only letters, numbers and hyphens.");
        }
        ApplicationController.getCurrentUser().setUsername(username);
        return new Result(true, "Username changed successfully.");
    }

    public static Result changeNickname(Matcher matcher) {
        String nickname = matcher.group("nickname");
        if (nickname.equals(ApplicationController.getCurrentUser().getNickname())) {
            return new Result(false, "You entered your current nickname.");
        }
        ApplicationController.getCurrentUser().setNickname(nickname);
        return new Result(true, "Nickname changed successfully.");
    }

    public static Result changeEmail(Matcher matcher) {
        String email = matcher.group("email");
        if (email.equals(ApplicationController.getCurrentUser().getEmail())) {
            return new Result(false, "You entered your current email.");
        }
        if (!email.matches("[a-zA-Z0-9]+@[a-zA-Z0-9]+\\.[a-zA-Z0-9]+")) {
            return new Result(false, "Email is invalid.");
        }
        ApplicationController.getCurrentUser().setEmail(email);
        return new Result(true, "Email changed successfully.");
    }

    public static Result changePassword(Matcher matcher) {
        String password = matcher.group("password");
        String oldPassword = matcher.group("oldPassword");
        if (!oldPassword.equals(ApplicationController.getCurrentUser().getPassword())) {
            return new Result(false, "Old password is incorrect.");
        }
        if (password.equals(oldPassword)) {
            return new Result(false, "You entered your current password.");
        }
        if (!password.matches("\\S+")) {
            return new Result(false, "Password is invalid.");
        }
        if (!RegisterMenuController.checkPassword(password)) {
            return new Result(false, "Password is weak.");
        }
        ApplicationController.getCurrentUser().setPassword(password);
        return new Result(true, "Password changed successfully.");
    }

    public static ArrayList<String> showInfo() {
        //TODO
        return null;
    }

    public static Result gameHistory() {
        //TODO
        return null;
    }
}
