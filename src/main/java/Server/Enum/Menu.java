package Server.Enum;

public enum Menu {
    CHEAT_MENU,
    CHOOSE_MENU,
    IN_GAME_MENU,
    GAME_MENU,
    LOGIN_MENU,
    MAIN_MENU,
    REGISTER_MENU,
    PROFILE_MENU,
    FRIENDS_MENU,
    FRIEND_REQUESTS_MENU,
    RANKING_MENU,
    TOURNAMENT_MENU,
    WAITING_MENU,
    BROADCAST_MENU,
    ONLINE_STREAM_MENU;

    public boolean isOkToAuthenticate() {
        return this != GAME_MENU  && this != IN_GAME_MENU &&
                this != CHEAT_MENU && this != CHOOSE_MENU &&
                this != BROADCAST_MENU && this != ONLINE_STREAM_MENU &&
                this != WAITING_MENU;
    }
}
