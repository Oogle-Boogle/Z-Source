package com.zamron.world.content.combat.weapon;

import com.zamron.world.content.BonusManager;

/**
 * A collection of constants that each represent a different fighting type.
 * 
 * @author lare96
 */
public enum FightType {

    STAFF_BASH(401, 43, 0, BonusManager.ATTACK_CRUSH, FightStyle.ACCURATE),
    STAFF_POUND(406, 43, 1, BonusManager.ATTACK_CRUSH, FightStyle.AGGRESSIVE),
    STAFF_FOCUS(406, 43, 2, BonusManager.ATTACK_CRUSH, FightStyle.DEFENSIVE),
    WARHAMMER_POUND(401, 43, 0, BonusManager.ATTACK_CRUSH, FightStyle.ACCURATE),
    WARHAMMER_PUMMEL(401, 43, 1, BonusManager.ATTACK_CRUSH, FightStyle.AGGRESSIVE),
    WARHAMMER_BLOCK(401, 43, 2, BonusManager.ATTACK_CRUSH, FightStyle.DEFENSIVE),
    SCYTHE_REAP(414, 43, 0, BonusManager.ATTACK_SLASH, FightStyle.ACCURATE),
    SCYTHE_CHOP(382, 43, 1, BonusManager.ATTACK_STAB, FightStyle.AGGRESSIVE),
    SCYTHE_JAB(2066, 43, 2, BonusManager.ATTACK_CRUSH, FightStyle.CONTROLLED),
    SCYTHE_BLOCK(382, 43, 3, BonusManager.ATTACK_SLASH, FightStyle.DEFENSIVE),
    BATTLEAXE_CHOP(401, 43, 0, BonusManager.ATTACK_SLASH, FightStyle.ACCURATE),
    BATTLEAXE_HACK(401, 43, 1, BonusManager.ATTACK_SLASH, FightStyle.AGGRESSIVE),
    BATTLEAXE_SMASH(401, 43, 2, BonusManager.ATTACK_CRUSH, FightStyle.AGGRESSIVE),
    BATTLEAXE_BLOCK(401, 43, 3, BonusManager.ATTACK_SLASH, FightStyle.DEFENSIVE),
    CROSSBOW_ACCURATE(4230, 43, 0, BonusManager.ATTACK_RANGE, FightStyle.ACCURATE),
    CROSSBOW_RAPID(4230, 43, 1, BonusManager.ATTACK_RANGE, FightStyle.AGGRESSIVE),
    CROSSBOW_LONGRANGE(4230, 43, 2, BonusManager.ATTACK_RANGE, FightStyle.DEFENSIVE),
    SHORTBOW_ACCURATE(426, 43, 0, BonusManager.ATTACK_RANGE, FightStyle.ACCURATE),
    SHORTBOW_RAPID(426, 43, 1, BonusManager.ATTACK_RANGE, FightStyle.AGGRESSIVE),
    SHORTBOW_LONGRANGE(426, 43, 2, BonusManager.ATTACK_RANGE, FightStyle.DEFENSIVE),
    MINIGUN_ACCURATE(426, 43, 0, BonusManager.ATTACK_RANGE, FightStyle.ACCURATE),
    MINIGUN_RAPID(426, 43, 1, BonusManager.ATTACK_RANGE, FightStyle.AGGRESSIVE),
    MINIGUN_LONGRANGE(426, 43, 2, BonusManager.ATTACK_RANGE, FightStyle.DEFENSIVE),
    LONGBOW_ACCURATE(426, 43, 0, BonusManager.ATTACK_RANGE, FightStyle.ACCURATE),
    LONGBOW_RAPID(426, 43, 1, BonusManager.ATTACK_RANGE, FightStyle.AGGRESSIVE),
    LONGBOW_LONGRANGE(426, 43, 2, BonusManager.ATTACK_RANGE, FightStyle.DEFENSIVE),
    DAGGER_STAB(13049, 43, 0, BonusManager.ATTACK_STAB, FightStyle.ACCURATE),
    DAGGER_LUNGE(13049, 43, 1, BonusManager.ATTACK_STAB, FightStyle.AGGRESSIVE),
    DAGGER_SLASH(13048, 43, 2, BonusManager.ATTACK_STAB, FightStyle.AGGRESSIVE),
    DAGGER_BLOCK(13049, 43, 3, BonusManager.ATTACK_STAB, FightStyle.DEFENSIVE),
    SWORD_STAB(15072, 43, 0, BonusManager.ATTACK_STAB, FightStyle.ACCURATE),
    SWORD_LUNGE(12310, 43, 1, BonusManager.ATTACK_STAB, FightStyle.CONTROLLED),
    SWORD_SLASH(12310, 43, 2, BonusManager.ATTACK_SLASH, FightStyle.AGGRESSIVE),
    SWORD_BLOCK(12310, 43, 3, BonusManager.ATTACK_STAB, FightStyle.DEFENSIVE),
    SCIMITAR_CHOP(15071, 43, 0, BonusManager.ATTACK_SLASH, FightStyle.ACCURATE),
    SCIMITAR_SLASH(15071, 43, 1, BonusManager.ATTACK_SLASH, FightStyle.AGGRESSIVE),
    SCIMITAR_LUNGE(15072, 43, 2, BonusManager.ATTACK_STAB, FightStyle.CONTROLLED),
    SCIMITAR_BLOCK(15071, 43, 3, BonusManager.ATTACK_SLASH, FightStyle.DEFENSIVE),
    LONGSWORD_CHOP(12310, 43, 0, BonusManager.ATTACK_SLASH, FightStyle.ACCURATE),
    LONGSWORD_SLASH(12310, 43, 1, BonusManager.ATTACK_SLASH, FightStyle.AGGRESSIVE),
    LONGSWORD_LUNGE(12310, 43, 2, BonusManager.ATTACK_STAB, FightStyle.CONTROLLED),
    LONGSWORD_BLOCK(12310, 43, 3, BonusManager.ATTACK_SLASH, FightStyle.DEFENSIVE),
    MACE_POUND(1665, 43, 0, BonusManager.ATTACK_CRUSH, FightStyle.ACCURATE),
    MACE_PUMMEL(1665, 43, 1, BonusManager.ATTACK_CRUSH, FightStyle.AGGRESSIVE),
    MACE_SPIKE(13036, 43, 2, BonusManager.ATTACK_STAB, FightStyle.CONTROLLED),
    MACE_BLOCK(1665, 43, 3, BonusManager.ATTACK_CRUSH, FightStyle.DEFENSIVE),
    KNIFE_ACCURATE(929, 43, 0, BonusManager.ATTACK_RANGE, FightStyle.ACCURATE),
    KNIFE_RAPID(929, 43, 1, BonusManager.ATTACK_RANGE, FightStyle.AGGRESSIVE),
    KNIFE_LONGRANGE(929, 43, 2, BonusManager.ATTACK_RANGE, FightStyle.DEFENSIVE),
    SPEAR_LUNGE(13045, 43, 0, BonusManager.ATTACK_STAB, FightStyle.CONTROLLED),
    SPEAR_SWIPE(13047, 43, 1, BonusManager.ATTACK_SLASH, FightStyle.CONTROLLED),
    SPEAR_POUND(13044, 43, 2, BonusManager.ATTACK_CRUSH, FightStyle.CONTROLLED),
    SPEAR_BLOCK(13044, 43, 3, BonusManager.ATTACK_STAB, FightStyle.DEFENSIVE),
    TWOHANDEDSWORD_CHOP(11981, 43, 0, BonusManager.ATTACK_SLASH, FightStyle.ACCURATE),
    TWOHANDEDSWORD_SLASH(11979, 43, 1, BonusManager.ATTACK_SLASH, FightStyle.AGGRESSIVE),
    TWOHANDEDSWORD_SMASH(11979, 43, 2, BonusManager.ATTACK_CRUSH, FightStyle.AGGRESSIVE),
    TWOHANDEDSWORD_BLOCK(11979, 43, 3, BonusManager.ATTACK_SLASH, FightStyle.DEFENSIVE),
    PICKAXE_SPIKE(400, 43, 0, BonusManager.ATTACK_STAB, FightStyle.ACCURATE),
    PICKAXE_IMPALE(400, 43, 1, BonusManager.ATTACK_STAB, FightStyle.AGGRESSIVE),
    PICKAXE_SMASH(401, 43, 2, BonusManager.ATTACK_CRUSH, FightStyle.AGGRESSIVE),
    PICKAXE_BLOCK(400, 43, 3, BonusManager.ATTACK_STAB, FightStyle.DEFENSIVE),
    CLAWS_CHOP(393, 43, 0, BonusManager.ATTACK_SLASH, FightStyle.ACCURATE),
    CLAWS_SLASH(393, 43, 1, BonusManager.ATTACK_SLASH, FightStyle.AGGRESSIVE),
    CLAWS_LUNGE(393, 43, 2, BonusManager.ATTACK_STAB, FightStyle.CONTROLLED),
    CLAWS_BLOCK(393, 43, 3, BonusManager.ATTACK_SLASH, FightStyle.DEFENSIVE),
    HALBERD_JAB(440, 43, 0, BonusManager.ATTACK_STAB, FightStyle.CONTROLLED),
    HALBERD_SWIPE(440, 43, 1, BonusManager.ATTACK_SLASH, FightStyle.AGGRESSIVE),
    HALBERD_FEND(440, 43, 2, BonusManager.ATTACK_STAB, FightStyle.DEFENSIVE),
    UNARMED_PUNCH(422, 43, 0, BonusManager.ATTACK_CRUSH, FightStyle.ACCURATE),
    UNARMED_KICK(423, 43, 1, BonusManager.ATTACK_CRUSH, FightStyle.AGGRESSIVE),
    UNARMED_BLOCK(422, 43, 2, BonusManager.ATTACK_CRUSH, FightStyle.DEFENSIVE),
    WHIP_FLICK(11968, 43, 0, BonusManager.ATTACK_SLASH, FightStyle.ACCURATE),
    WHIP_LASH(11969, 43, 1, BonusManager.ATTACK_SLASH, FightStyle.CONTROLLED),
    WHIP_DEFLECT(11970, 43, 2, BonusManager.ATTACK_SLASH, FightStyle.DEFENSIVE),
    THROWNAXE_ACCURATE(929, 43, 0, BonusManager.ATTACK_RANGE, FightStyle.ACCURATE),
    THROWNAXE_RAPID(929, 43, 1, BonusManager.ATTACK_RANGE, FightStyle.AGGRESSIVE),
    THROWNAXE_LONGRANGE(929, 43, 2, BonusManager.ATTACK_RANGE, FightStyle.DEFENSIVE),
    DART_ACCURATE(929, 43, 0, BonusManager.ATTACK_RANGE, FightStyle.ACCURATE),
    DART_RAPID(929, 43, 1, BonusManager.ATTACK_RANGE, FightStyle.AGGRESSIVE),
    DART_LONGRANGE(929, 43, 2, BonusManager.ATTACK_RANGE, FightStyle.DEFENSIVE),
    JAVELIN_ACCURATE(929, 43, 0, BonusManager.ATTACK_RANGE, FightStyle.ACCURATE),
    JAVELIN_RAPID(929, 43, 2, BonusManager.ATTACK_RANGE, FightStyle.AGGRESSIVE),
    JAVELIN_LONGRANGE(929, 43, 3, BonusManager.ATTACK_RANGE, FightStyle.DEFENSIVE);

    /** The animation this fight type holds. */
    private int animation;

    /** The parent config id. */
    private int parentId;

    /** The child config id. */
    private int childId;

    /** The bonus type. */
    private int bonusType;

    /** The fighting style. */
    private FightStyle style;

    /**
     * Create a new {@link FightType}.
     * 
     * @param animation
     *            the animation this fight type holds.
     * @param trainType
     *            the train type this fight type holds.
     * @param parentId
     *            the parent config id.
     * @param childId
     *            the child config id.
     * @param bonusType
     *            the bonus type.
     * @param fightStyle
     *            the fighting style.
     */
    private FightType(int animation, int parentId, int childId, int bonusType,
        FightStyle style) {
        this.animation = animation;
        this.parentId = parentId;
        this.childId = childId;
        this.bonusType = bonusType;
        this.style = style;
    }

    /**
     * Gets the animation this fight type holds.
     * 
     * @return the animation.
     */
    public int getAnimation() {
        return animation;
    }

    /**
     * Gets the parent config id.
     * 
     * @return the parent id.
     */
    public int getParentId() {
        return parentId;
    }

    /**
     * Gets the child config id.
     * 
     * @return the child id.
     */
    public int getChildId() {
        return childId;
    }

    /**
     * Gets the bonus type.
     * 
     * @return the bonus type.
     */
    public int getBonusType() {
        return bonusType;
    }

    /**
     * Gets the fighting style.
     * 
     * @return the fighting style.
     */
    public FightStyle getStyle() {
        return style;
    }

    /**
     * Determines the corresponding bonus for this fight type.
     * 
     * @return the corresponding bonus for this fight type.
     */
    public int getCorrespondingBonus() {
        switch (bonusType) {
        case BonusManager.ATTACK_CRUSH:
            return BonusManager.DEFENCE_CRUSH;
        case BonusManager.ATTACK_MAGIC:
            return BonusManager.DEFENCE_MAGIC;
        case BonusManager.ATTACK_RANGE:
            return BonusManager.DEFENCE_RANGE;
        case BonusManager.ATTACK_SLASH:
            return BonusManager.DEFENCE_SLASH;
        case BonusManager.ATTACK_STAB:
            return BonusManager.DEFENCE_STAB;
        default:
            return BonusManager.DEFENCE_CRUSH;
        }
    }
}