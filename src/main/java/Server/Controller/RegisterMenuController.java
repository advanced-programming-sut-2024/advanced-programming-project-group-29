package Server.Controller;

import Server.Model.DatabaseManager;
import Server.Model.EmailUtil;
import Server.Model.Result;
import Server.Model.User;
import Server.Regex.LoginMenuRegex;
import Server.Regex.RegisterMenuRegex;

import java.util.Random;
import java.util.regex.Matcher;

public class RegisterMenuController {

    public static Object processRequest(ApplicationController applicationController, String inputCommand) {
        if (inputCommand.matches(RegisterMenuRegex.REGISTER.getRegex())) {
            return register(RegisterMenuRegex.REGISTER.getMatcher(inputCommand));
        }
        if (inputCommand.matches(RegisterMenuRegex.GET_SECURITY_QUESTIONS.getRegex())) {
            return new Result(true, User.getSecurityQuestions());
        }
        if (inputCommand.matches(RegisterMenuRegex.HAS_ANSWERED_QUESTION.getRegex())) {
            return hasAnsweredQuestion(RegisterMenuRegex.HAS_ANSWERED_QUESTION.getMatcher(inputCommand));
        }
        if (inputCommand.matches(RegisterMenuRegex.GENERATE_RANDOM_PASSWORD.getRegex())) {
            return generateRandomPassword();
        }
        if (inputCommand.matches(RegisterMenuRegex.PICK_QUESTION.getRegex())) {
            return answerSecurityQuestion(RegisterMenuRegex.PICK_QUESTION.getMatcher(inputCommand));
        }
        if (inputCommand.matches(LoginMenuRegex.SEND_EMAIL.getRegex())) {
            return sendEmail(LoginMenuRegex.SEND_EMAIL.getMatcher(inputCommand));
        }
        if (inputCommand.matches(LoginMenuRegex.VERIFY_EMAIL.getRegex())) {
            return verifyEmail(LoginMenuRegex.VERIFY_EMAIL.getMatcher(inputCommand));
        }
        return null;
    }

    public static Result register(Matcher matcher) {
        String username = matcher.group("username");
        String password = matcher.group("password");
        String passwordConfirm = matcher.group("passwordConfirm");
        String nickname = matcher.group("nickname");
        String email = matcher.group("email");
        if (User.getUserByUsername(username) != null) {
            String newUsername;
            do {
                newUsername = username + new Random().nextInt(1000);
            } while (User.getUserByUsername(newUsername) != null);
            return new Result(false, "Username is already taken. We suggest you to use " + newUsername + " instead.", newUsername);
        }
        if (!username.matches("[a-zA-Z0-9-]+")) {
            return new Result(false, "Username is invalid. It should contain only letters, numbers and hyphens.");
        }
        if (!email.matches("[a-zA-Z0-9]+@[a-zA-Z0-9]+\\.[a-zA-Z0-9]+")) {
            return new Result(false, "Email is invalid.");
        }
        if (!password.matches("\\S+")) {
            return new Result(false, "Password is invalid.");
        }
        if (!checkPassword(password)) {
            return new Result(false, "Password is weak.");
        }
        if (!password.equals(passwordConfirm)) {
            return new Result(false, "Passwords do not match.");
        }
        new User(username, password, nickname, email);
        return new Result(true, "User created successfully. check your email!");
    }

    public static String generateRandomPassword() {
        String password = "";
        Random random = new Random();
        for (int i = 0; i < 8; i++) {
            int type = random.nextInt(4);
            switch (type) {
                case 0:
                    password += (char) (random.nextInt(26) + 'a');
                    break;
                case 1:
                    password += (char) (random.nextInt(26) + 'A');
                    break;
                case 2:
                    password += (char) (random.nextInt(10) + '0');
                    break;
                case 3:
                    password += "!@#$%^&*".charAt(random.nextInt(7));
                    break;
            }
        }
        while (!checkPassword(password)) {
            password = generateRandomPassword();
        }
        return password;
    }

    public static Result answerSecurityQuestion(Matcher matcher) {
        int question = Integer.parseInt(matcher.group("question"));
        String answer = matcher.group("answer");
        String confirm = matcher.group("confirm");
        String username = matcher.group("username");
        if (question > User.getSecurityQuestions().length) {
            return new Result(false, "Invalid question number.");
        }
        if (!answer.equals(confirm)) {
            return new Result(false, "Answers do not match.");
        }
        User user = User.getUserByUsername(username);
        user.setQuestion(question, answer);
        if (DatabaseManager.userExists(username)) {
            DatabaseManager.updateUser(user, username);
            return new Result(true, "Question set successfully.");
        }
        DatabaseManager.insertUser(user);
        return new Result(true, "Question set successfully.");
    }

    protected static boolean checkPassword(String password) {
        if (password.length() < 8)
            return false;
        if (!password.matches(".*[a-z].*") || !password.matches(".*[A-Z].*")
                || !password.matches(".*[0-9].*") || !password.matches(".*[!@#$%^&*].*"))
            return false;
        return true;
    }

    public static boolean hasAnsweredQuestion(Matcher matcher) {
        String username = matcher.group("username");
        User user = User.getUserByUsername(username);
        return user.hasUserAnswerTheQuestion();
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
}
