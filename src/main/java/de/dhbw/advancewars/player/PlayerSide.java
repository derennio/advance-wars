package de.dhbw.advancewars.player;

/**
 * Represents the side of a player.
 */
public enum PlayerSide {
    PLAYER_1,
    PLAYER_2;

    @Override
    public String toString() {
        return switch (this) {
            case PLAYER_1 -> "Player 1";
            case PLAYER_2 -> "Player 2";
        };
    }
}
