package de.dhbw.advancewars.character;

import de.dhbw.advancewars.character.air.BattleCopter;
import de.dhbw.advancewars.character.air.Bomber;
import de.dhbw.advancewars.character.air.Fighter;
import de.dhbw.advancewars.character.land.*;

/**
 * Represents the class of a character.
 */
public enum CharacterClass {
    INFANTRY,
    MECH,
    TANK,
    ARTILLERY,
    ANTI_AIR,

    FIGHTER,
    BOMBER,
    BATTLE_COPTER;

    /**
     * @param character The character to get the class for.
     * @return The class of the character.
     */
    public static CharacterClass getClass(ICharacter character) {
        if (character instanceof Infantry) {
            return CharacterClass.INFANTRY;
        } else if (character instanceof Mech) {
            return CharacterClass.MECH;
        } else if (character instanceof Tank) {
            return CharacterClass.TANK;
        } else if (character instanceof Artillery) {
            return CharacterClass.ARTILLERY;
        } else if (character instanceof AntiAir) {
            return CharacterClass.ANTI_AIR;
        } else if (character instanceof Fighter) {
            return CharacterClass.FIGHTER;
        } else if (character instanceof Bomber) {
            return CharacterClass.BOMBER;
        } else if (character instanceof BattleCopter) {
            return CharacterClass.BATTLE_COPTER;
        }
        return null;
    }
}
