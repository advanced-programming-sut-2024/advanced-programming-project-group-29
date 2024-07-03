package Server.Controller;

import Server.Model.Result;
import Server.Model.User;
import Server.Regex.RegisterMenuRegex;
import Server.Controller.ApplicationController;

import java.util.Random;
import java.util.regex.Matcher;

public class RegisterMenuController {

    public static Result processRequest(ApplicationController applicationController, String inputCommand) {
        if (inputCommand.matches(RegisterMenuRegex.REGISTER.getRegex())) {
            return register(RegisterMenuRegex.REGISTER.getMatcher(inputCommand));
        }
        if (inputCommand.matches(RegisterMenuRegex.GENERATE_RANDOM_PASSWORD.getRegex())) {
            return new Result(true, generateRandomPassword());
        }
        if (inputCommand.matches(RegisterMenuRegex.PICK_QUESTION.getRegex())) {
            return answerSecurityQuestion(RegisterMenuRegex.PICK_QUESTION.getMatcher(inputCommand));
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
            do
            {
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
        return new Result(true, "User created successfully. Please answer the security question!");
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
}
