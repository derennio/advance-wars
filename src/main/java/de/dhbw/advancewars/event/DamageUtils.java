package de.dhbw.advancewars.event;

import de.dhbw.advancewars.character.CharacterType;
import de.dhbw.advancewars.character.ICharacter;
import de.dhbw.advancewars.maps.data.TileType;

public class DamageUtils {
    /**
     * Calculate the damage dealt by one character to another.
     *
     * @param attacker The character that deals the damage.
     * @param defender The character that receives the damage.
     *
     * @return The damage dealt by characterA to characterB.
     */
    public static int calculateDamage(ICharacter attacker, ICharacter defender) {
        float terrainMultiplier = getTerrainMultiplier(attacker, defender);
        float characterTypeMultiplier = getCharacterTypeMultiplier(attacker, defender);
        int damage = (int) (attacker.getAttackPower() * terrainMultiplier * characterTypeMultiplier) - defender.getDefensePower();
        return Math.max(damage, 1);
    }

    /**
     * Calculate the damage dealt by one character to another.
     *
     * @param attacker The character that deals the damage.
     * @param defender The character that receives the damage.
     *
     * @return The damage dealt by characterA to characterB.
     */
    private static float getTerrainMultiplier(ICharacter attacker, ICharacter defender) {
        TileType attackerTileType = attacker.getPosition().type();
        TileType defenderTileType = defender.getPosition().type();

        switch (attackerTileType) {
            case PLAIN:
                if (defenderTileType == TileType.WOOD) {
                    return 0.7f;
                }
                if (defenderTileType == TileType.MOUNTAIN) {
                    return 0.6f;
                }
                if (defenderTileType == TileType.SEA) {
                    return 0.5f;
                }
                break;
            case WOOD:
                if (defenderTileType == TileType.PLAIN) {
                    return 1.3f;
                }
                if (defenderTileType == TileType.MOUNTAIN) {
                    return 0.5f;
                }
                if (defenderTileType == TileType.SEA) {
                    return 0.6f;
                }
                break;
            case MOUNTAIN:
                if (defenderTileType == TileType.PLAIN) {
                    return 1.4f;
                }
                if (defenderTileType == TileType.WOOD) {
                    return 1.6f;
                }
                if (defenderTileType == TileType.SEA) {
                    return 0.4f;
                }
                break;
            case SEA:
                if (defenderTileType == TileType.PLAIN) {
                    return 1.5f;
                }
                if (defenderTileType == TileType.WOOD) {
                    return 1.7f;
                }
                if (defenderTileType == TileType.MOUNTAIN) {
                    return 1.8f;
                }
                break;
        }

        return 1.0f;
    }

    /**
     * Calculate the damage multiplier dealt by one type of character to another.
     *
     * @param attacker The character that deals the damage.
     * @param defender The character that receives the damage.
     *
     * @return The damage dealt by characterA to characterB.
     */
    private static float getCharacterTypeMultiplier(ICharacter attacker, ICharacter defender) {
        if (attacker.getType() == defender.getType()) {
            return 1.0f;
        }

        if (attacker.getType() == CharacterType.LAND && defender.getType() == CharacterType.AIR) {
            return .8f;
        }

        if (attacker.getType() == CharacterType.AIR && defender.getType() == CharacterType.LAND) {
            return 1.2f;
        }

        return 1.0f;
    }
}
