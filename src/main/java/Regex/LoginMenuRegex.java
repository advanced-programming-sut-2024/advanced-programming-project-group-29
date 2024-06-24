package Regex;

public enum LoginMenuRegex {
    LOGIN("login -u (?<username>\\S+) -p (?<password>\\S+) (?<stayLoggedIn>-stay-logged-in)?"),
    FORGETPASSWORD("forget password -u (?<username>\\S+)"),
    ANSWER("answer -a (?<answer>\\S+)"),
    CHANGEPASSWORD("change password -p (?<password>\\S+) -c (?<passwordConfirm>\\S+)");

    private final String regex;
    LoginMenuRegex(String regex) {
        this.regex = regex;
    }
    public String getRegex() {
        return regex;
    }
}
