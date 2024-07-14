package de.dhbw.advancewars.event;

import de.dhbw.advancewars.character.CharacterType;
import de.dhbw.advancewars.character.ICharacter;
import de.dhbw.advancewars.maps.data.TileType;

public class DamageUtils {

    /**
     * The damage matrix which shows if and how strongly
     * one character type deals damage to another character type.
     */
    private final static int[][] damageMatrix = {
    //   I  M  T  Ar AA F  B  BC      -> Defender
        {3, 3, 1, 2, 1, 0, 0, 1}, // Infantry       |
        {3, 3, 3, 4, 3, 0, 0, 1}, // Mech           |
        {4, 4, 2, 4, 3, 0, 0, 1}, // Tank           |
        {5, 5, 4, 4, 3, 0, 0, 0}, // Artillery      |
        {5, 5, 2, 3, 2, 3, 3, 5}, // AntiAir         > Attacker
        {0, 0, 0, 0, 0, 3, 5, 5}, // Fighter        |
        {5, 5, 5, 5, 5, 0, 0, 0}, // Bomber         |
        {4, 4, 3, 3, 2, 0, 0, 3}, // BattleCopter   |
    };
    private final static float attackFactor = 1.5f;
    private final static float defendFactor = 1.0f;


    /**
     * Calculate the damage dealt by one character to another.
     *
     * @param attacker The character that deals the damage.
     * @param defender The character that receives the damage.
     *
     * @return The damage dealt by characterA to characterB.
     */
    public static int calculateDamage(ICharacter attacker, ICharacter defender, boolean isAttack) {
        float terrainMultiplier = getTerrainMultiplier(attacker, defender);

        int baseDamage = damageMatrix[attacker.getIndex()][defender.getIndex()];
        float damage = (baseDamage * (isAttack?attackFactor:defendFactor) * terrainMultiplier * (attacker.getHealth()/10f));
        if (damage < 1 && damage > 0) {
            damage = 1;
        }
        return (int) damage;
    }

    /**
     * Calculate the damage multiplier based on the terrain of attacker and defender.
     *
     * @param attacker The character that deals the damage.
     * @param defender The character that receives the damage.
     *
     * @return The damage dealt by characterA to characterB.
     */
    private static float getTerrainMultiplier(ICharacter attacker, ICharacter defender) {
        TileType attackerTileType = attacker.getPosition().type();
        TileType defenderTileType = defender.getPosition().type();

        if (attacker.getType() == CharacterType.AIR) {
            return 1.0f;
        }

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
}
