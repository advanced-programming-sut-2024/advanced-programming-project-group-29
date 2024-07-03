package Client.Regex;

public enum RankingMenuRegex {
    GET_RANKING("get ranking");

    private final String regex;

    RankingMenuRegex(String regex) {
        this.regex = regex;
    }

    public String getRegex() {
        return regex;
    }
}
