package com.zamron.model;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import lombok.Getter;
import lombok.RequiredArgsConstructor;


/**
 * Represents a player's privilege rights.
 * @author Gabriel Hannason
 */

@RequiredArgsConstructor
@Getter
public enum SecondaryPlayerRights {

    DEFAULT(0,0,0,0),

    DONATOR(1.25,0,1,1),

    SUPER_DONATOR(1.5,1,1,1),

    EXTREME_DONATOR(1.8,2,2,2),

    LEGENDARY_DONATOR(2.2,3,2,2),

    UBER_DONATOR(2.5,4,3,3),
	
	DELUXE_DONATOR(3,5,3,3),

    VIP_DONATOR(4, 6, 5, 5);

    SecondaryPlayerRights(double xpModifier, int votePtModifier, int bonusBossPts, int bonusTriviaPts) {
        this.xpMofifier = xpMofifier;
        this.votePtModifier = votePtModifier;
        this.bonusBossPts = bonusBossPts;
        this.bonusTriviaPts = bonusTriviaPts;
    }

    @Getter
    public double xpMofifier;
    @Getter
    public int votePtModifier;
    @Getter
    public int bonusBossPts;
    @Getter
    public int bonusTriviaPts;


    private static final ImmutableSet<SecondaryPlayerRights> MEMBERS = Sets.immutableEnumSet(DONATOR, SUPER_DONATOR, EXTREME_DONATOR, LEGENDARY_DONATOR, UBER_DONATOR, DELUXE_DONATOR, VIP_DONATOR);


    public boolean isSecondaryMember() {
        return MEMBERS.contains(this);
    }

}