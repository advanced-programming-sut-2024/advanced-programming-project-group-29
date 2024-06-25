package Regex;

public enum RegisterMenuRegex {
    REGISTER("register -u (?<username>\\S+) -p (?<password>\\S+) (?<passwordConfirm>\\S+) -n (?<nickname>\\S+) -e (?<email>\\S+)"),
    PICKQUESTION("pick question -q (?<question>\\d+) -a (?<answer>\\S+) -c (?<confirm>\\S+)");

    private final String regex;

    RegisterMenuRegex(String regex) {
        this.regex = regex;
    }

    public String getRegex() {
        return regex;
    }
}
