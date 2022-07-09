package com.zamron.model;

import com.zamron.util.Misc;
import com.zamron.world.entity.impl.player.Player;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import lombok.Getter;

public enum PlayerRights {
	PLAYER(60, "", 1, 1, 1, 0, 0),
	MODERATOR(-1, "<col=7838a3>", 1, 1.5,1, 0, 0),
	ADMINISTRATOR(-1, "<col=7838a3>", 1, 1.5,1, 0,0),
	OWNER(-1, "<col=7838a3>", 1, 1.5,1,0,0),
	DEVELOPER(-1, "<col=7838a3>", 1, 1.5,1,0,0),
	DONATOR(50, "<shad=FF7F00>", 1.5, 1.25,1,1,1),
	SUPER_DONATOR(40, "<col=787878>", 1.5, 1.5,1,1,1),
 	EXTREME_DONATOR(20, "<col=D9D919>", 2, 1.8, 2,2,2),
	LEGENDARY_DONATOR(10, "<shad=697998>", 2.5, 2.2, 3,2,2),
	UBER_DONATOR(0, "<col=0EBFE9>", 3, 2.5, 4,3,3),
	DELUXE_DONATOR(0, "<col=8600CC>", 6, 3, 5,3,3),
	VIP_DONATOR(0, "<col=8600CC>", 10, 4,6,4,4),
	SUPPORT(-1, "<col=7838a3>", 1, 1.5, 1,0,0),
	YOUTUBER(20, "<col=CD661D>", 1, 1.1, 1,0,0),
	COMMUNITY_MANAGER(0, "<col=7838a3>", 1, 1.5, 1,0,0),
	HADMIN(-1, "<col=FFFF64>", 1, 1.5, 1,0,0);

	PlayerRights(int yellDelaySeconds, String yellHexColor, double loyaltyPointsGainModifier, double xpMofifier, int votePtModifier, int bonusBossPts, int bonusTriviaPts) {
		this.yellDelay = yellDelaySeconds;
		this.yellHexColor = yellHexColor;
		this.loyaltyPtModifier = loyaltyPointsGainModifier;
		this.xpMofifier = xpMofifier;
		this.votePtModifier = votePtModifier;
		this.bonusBossPts = bonusBossPts;
		this.bonusTriviaPts = bonusTriviaPts;
	}

	@Getter
	public int yellDelay;
	@Getter
	public String yellHexColor;
	@Getter
	public double loyaltyPtModifier;
	@Getter
	public double xpMofifier;
	@Getter
	public int votePtModifier;
	@Getter
	public int bonusBossPts;
	@Getter
	public int bonusTriviaPts;
	
	private static final ImmutableSet<PlayerRights> SENIOR_STAFF = Sets.immutableEnumSet(ADMINISTRATOR, OWNER, DEVELOPER, HADMIN);
	private static final ImmutableSet<PlayerRights> MODS = Sets.immutableEnumSet(SUPPORT, MODERATOR);
	private static final ImmutableSet<PlayerRights> HIGH_RANK_STAFF = Sets.immutableEnumSet(ADMINISTRATOR, OWNER, DEVELOPER, COMMUNITY_MANAGER,HADMIN);
	private static final ImmutableSet<PlayerRights> HAS_BAN_RIGHTS = Sets.immutableEnumSet(OWNER, DEVELOPER,ADMINISTRATOR, HADMIN);
	private static final ImmutableSet<PlayerRights> MEMBERS = Sets.immutableEnumSet(DONATOR, SUPER_DONATOR, EXTREME_DONATOR, LEGENDARY_DONATOR, UBER_DONATOR, DELUXE_DONATOR, VIP_DONATOR);
	private static final ImmutableSet<PlayerRights> ALL_STAFF = Sets.immutableEnumSet(SUPPORT, MODERATOR, ADMINISTRATOR, OWNER, DEVELOPER, COMMUNITY_MANAGER, HADMIN);
	
	public String customYellTitle;
	
	public String getCustomYellPrefix(boolean owner) {
        if (customYellTitle == null || customYellTitle.isEmpty()) {
            if (this == OWNER)
                return "Owner";
            else if (this == MODERATOR)
                return "Moderator";
        }
        return customYellTitle;
    }
	
	public String getYellPrefix() {
        if (customYellTitle == null || customYellTitle.isEmpty())
            return "";
        return yellHexColor;
    }
	
	public boolean isSeniorStaff() {
		return SENIOR_STAFF.contains(this);
	}

	public boolean isStaff() {
		return ALL_STAFF.contains(this);
	}
	
	public boolean isMods() {
		return MODS.contains(this);
	}
	
	public boolean isMember() {
		return MEMBERS.contains(this);
	}
	
	public boolean isAdmin() {
		return HIGH_RANK_STAFF.contains(this);
	}
	
	public boolean isHighDonator() {
		return this == DELUXE_DONATOR || this == UBER_DONATOR | this == EXTREME_DONATOR|| this == LEGENDARY_DONATOR|| this == VIP_DONATOR;
	}
	
	public boolean isDeluxe() {
		return this == DELUXE_DONATOR;
	}
	
	public boolean isVip() {
		return this == VIP_DONATOR;
	}
	
	
	public boolean isDonator(Player player) {
		return player.getAmountDonated() >= 10;
	}
	
	public boolean isSuperDonator(Player player) {
		return player.getAmountDonated() >= 25;
	}

	public boolean isExtremeDonator(Player player) {
		return player.getAmountDonated() >= 50;
	}
	
	public boolean isLegendaryDonator(Player player) {
		return player.getAmountDonated() >= 125;
	}
	
	public boolean isUberDonator(Player player) {
		return player.getAmountDonated() >= 200;
	}
	
	public boolean isDeluxeDonator(Player player) {
		return player.getAmountDonated() >= 500;
	}
	
	public boolean isVipDonator(Player player) {
		return player.getAmountDonated() >= 1000;
	}
	
	public boolean isInvestorDonator(Player player) {
		return player.getAmountDonated() >= 2500;
	}
	
	/**
	 * Gets the rank for a certain id.
	 * 
	 * @param id	The id (ordinal()) of the rank.
	 * @return		rights.
	 */
	public static PlayerRights forId(int id) {
		for (PlayerRights rights : PlayerRights.values()) {
			if (rights.ordinal() == id) 
				return rights;
		}
		return null;
	}
	@Override
	public String toString() {
		return Misc.ucFirst(name().replaceAll("_", " "));
	}

	/**
	 * @return
	 */
	public boolean hasBanRights() {
		return HAS_BAN_RIGHTS.contains(this);
	}
}