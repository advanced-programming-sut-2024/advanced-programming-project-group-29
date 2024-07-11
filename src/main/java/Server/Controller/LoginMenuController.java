package Server.Controller;

import Server.Model.EmailUtil;
import Server.Model.Result;
import Server.Model.User;
import Server.Regex.LoginMenuRegex;

import java.util.regex.Matcher;

public class LoginMenuController {
    public static Object processRequest(ApplicationController applicationController, String inputCommand) {
        if (inputCommand.matches(LoginMenuRegex.LOGIN.getRegex())) {
            return login(applicationController, LoginMenuRegex.LOGIN.getMatcher(inputCommand));
        }
        if (inputCommand.matches(LoginMenuRegex.FORGET_PASSWORD.getRegex())) {
            return forgetPassword(LoginMenuRegex.FORGET_PASSWORD.getMatcher(inputCommand));
        }
        if (inputCommand.matches(LoginMenuRegex.ANSWER.getRegex())) {
            return answerQuestion(LoginMenuRegex.ANSWER.getMatcher(inputCommand));
        }
        if (inputCommand.matches(LoginMenuRegex.CHANGE_PASSWORD.getRegex())) {
            return changePassword(LoginMenuRegex.CHANGE_PASSWORD.getMatcher(inputCommand));
        }
        if (inputCommand.matches(LoginMenuRegex.LOAD_USER.getRegex())) {
            return loadUsers();
        }
        if (inputCommand.matches(LoginMenuRegex.SEND_EMAIL.getRegex())) {
            return sendEmail(LoginMenuRegex.SEND_EMAIL.getMatcher(inputCommand));
        }
        if (inputCommand.matches(LoginMenuRegex.VERIFY_EMAIL.getRegex())) {
            return verifyEmail(LoginMenuRegex.VERIFY_EMAIL.getMatcher(inputCommand));
        }
        if (inputCommand.matches(LoginMenuRegex.GET_USER_EMAIL.getRegex())) {
            return getEmailUser(LoginMenuRegex.GET_USER_EMAIL.getMatcher(inputCommand));
        }
        return null;
    }

    public static Result login(ApplicationController applicationController, Matcher matcher) {
        String username = matcher.group("username");
        String password = matcher.group("password");
        User user = User.getUserByUsername(username);
        if (user == null) {
            return new Result(false, "Username does not exist.");
        }
        if (!user.getPassword().equals(password)) {
            return new Result(false, "Password is incorrect.");
        }
        applicationController.setCurrentUser(user);
        applicationController.setLoggedInUser(user);
        Result result = new Result(true, "Now please check your email");
        result.setToken(user.getJWT());
        return result;
    }

    private static Result forgetPassword(Matcher matcher) {
        String username = matcher.group("username");
        User user = User.getUserByUsername(username);
        if (user == null)
            return new Result(false, "Username does not exist.");
        return new Result(true, user.getQuestion());
    }

    private static Result answerQuestion(Matcher matcher) {
        String answer = matcher.group("answer");
        String username = matcher.group("username");
        User user = User.getUserByUsername(username);
        if (user == null)
            return new Result(false, "Username does not exist.");
        if (!user.getAnswer().equals(answer))
            return new Result(false, "Answer is incorrect.");
        return new Result(true, "Select a new password.");
    }

    private static Result changePassword(Matcher matcher) {
        String password = matcher.group("password");
        String passwordConfirm = matcher.group("passwordConfirm");
        String username = matcher.group("username");
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

    private static Result loadUsers() {
        try {
            User.loadUser();
            return new Result(true, "Users loaded successfully.");
        } catch (Exception e) {
            return new Result(false, "Error loading users.");
        }
    }

    private static Result sendEmail(Matcher matcher) {
        String email = matcher.group("email");
        EmailUtil.generateAndSendVerificationCode(email);
        return new Result(true, "Email sent successfully.");
    }

    private static Result verifyEmail(Matcher matcher) {
        String email = matcher.group("email");
        int code = Integer.parseInt(matcher.group("code"));
        if (EmailUtil.verifyCode(email, code))
            return new Result(true, "Email verified successfully.");
        return new Result(false, "Code is incorrect.");
    }

    private static String getEmailUser(Matcher matcher) {
        User user = User.getUserByUsername(matcher.group("username"));
        if (user == null)
            return null;
        return user.getEmail();
    }
}