package Regex;

public enum ProfileMenuRegex {
    CHANGEUSERNAME("change username -u (?<username>\\S+)"),
    CHANGENICKNAME("change nickname -u (?<nickname>\\S+)"),
    CHANGEEMAIL("change email -e (?<email>\\S+)"),
    CHANGEPASSWORD("change password -p (?<password>\\S+) -o (?<oldPassword>\\S+)"),
    SHOWINFO("menu enter user info"),
    GAMEHISTORY("game history( -n (?<numberOfGames>-?\\d+))?");

    private final String regex;

    ProfileMenuRegex(String regex) {
        this.regex = regex;
    }

    public String getRegex() {
        return regex;
    }
}
