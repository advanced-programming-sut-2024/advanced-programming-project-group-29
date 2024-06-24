package Controller;

import Model.Result;
import Model.User;

import java.util.regex.Matcher;

public class LoginMenuController {
    public static Result login(Matcher matcher) {
        String username = matcher.group("username");
        String password = matcher.group("password");
        User user = User.getUserByUsername(username);
        if (user.equals(null)) {
            return new Result(false, "Username does not exist.");
        }
        if (!user.getPassword().equals(password)) {
            return new Result(false, "Password is incorrect.");
        }
        ApplicationController.setCurrentUser(user);
        return new Result(true, "User logged in successfully.");
    }

    public static Result forgetPassword(Matcher matcher) {
        String username = matcher.group("username");
        User user = User.getUserByUsername(username);
        if (user == null)
            return new Result(false, "Username does not exist.");
        return new Result(true, user.getQuestion());
    }

    public static Result answerQuestion(Matcher matcher, String username) {
        String answer = matcher.group("answer");
        User user = User.getUserByUsername(username);
        if (user == null)
            return new Result(false, "Username does not exist.");
        if (!user.getAnswer().equals(answer))
            return new Result(false, "Answer is incorrect.");
        return new Result(true, "Select a new password.");
    }

    public static Result changePassword(Matcher matcher, String username) {
        String password = matcher.group("password");
        String passwordConfirm = matcher.group("passwordConfirm");
        User user = User.getUserByUsername(username);
        if (user == null)
            return new Result(false, "Username does not exist.");
        if (!password.matches("\\S+"))
            return new Result(false, "Password is invalid.");
        if (!RegisterMenuController.checkPassword(password))
            return new Result(false, "Password is weak.");
        if (!password.equals(passwordConfirm))
            return new Result(false, "Passwords do not match.");
        user.setPassword(password);
        return new Result(true, "Password changed successfully.");
    }
}