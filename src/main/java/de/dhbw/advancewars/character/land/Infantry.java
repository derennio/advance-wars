package de.dhbw.advancewars.character.land;

import de.dhbw.advancewars.character.CharacterType;
import de.dhbw.advancewars.character.ICharacter;
import de.dhbw.advancewars.maps.data.MapTile;
import de.dhbw.advancewars.player.PlayerSide;

public class Infantry implements ICharacter {
    @Override
    public CharacterType getType() {
        return CharacterType.LAND;
    }

    @Override
    public String getName() {
        return "Infantry";
    }

    @Override
    public int getHealth() {
        return 10;
    }

    @Override
    public int getAttackPower() {
        return 1;
    }

    @Override
    public int getDefensePower() {
        return 1;
    }

    @Override
    public int getMovementRange() {
        return 3;
    }

    @Override
    public int getVisionRange() {
        return 2;
    }

    @Override
    public String getCharacterAssetPath() {
        return "/assets/characters/infantry.png";
    }

    @Override
    public MapTile getPosition() {
        return null;
    }

    @Override
    public PlayerSide getPlayerSide() {
        return null;
    }
}
