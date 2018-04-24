public interface Players {
    AI ai = new AI();
    pvp pvp = new pvp();

    static void iniPlayers(int numberOfPlayers) {
        move(numberOfPlayers);
    }
    static void move(int numberOfPlayers) {
        if (numberOfPlayers == 1) {
            ai.move();
        } else if (numberOfPlayers == 2) {
            pvp.move();
        }
    }

}
