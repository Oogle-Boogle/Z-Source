package com.zamron.world.content.combat;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.Consumer;

import com.zamron.model.CombatIcon;
import com.zamron.model.Hit;
import com.zamron.model.Hitmask;
import com.zamron.util.Misc;
import com.zamron.world.content.combat.weapon.CombatSpecial;
import com.zamron.world.content.raids.RaidNpc;
import com.zamron.world.entity.impl.Character;
import com.zamron.world.entity.impl.player.Player;

/**
 * A container that holds all of the data needed for a single combat hook.
 * 
 * @author lare96
 */
public class CombatContainer {

	/** The attacker in this combat hook. */
	private Character attacker;

	/** The victim in this combat hook. */
	private Character victim;

	/** The hits that will be dealt during this combat hook. */
	private CombatHit[] hits;

	/** The skills that will be given experience. */
	private int[] experience;

	/** The combat type that is being used during this combat hook. */
	private CombatType combatType;

	/** If accuracy should be taken into account. */
	private boolean checkAccuracy;

	/** If at least one hit in this container is accurate. */
	private boolean accurate;
	
	/** The modified damage, used for bolt effects etc **/
	private int modifiedDamage;
	
	/** The delay before the hit is executed **/
	private int hitDelay;

	/**
	 * Create a new {@link CombatContainer}.
	 * 
	 * @param attacker
	 *            the attacker in this combat hook.
	 * @param victim
	 *            the victim in this combat hook.
	 * @param hitAmount
	 *            the amount of hits to deal this combat hook.
	 * @param hitType
	 *            the combat type that is being used during this combat hook
	 * @param checkAccuracy
	 *            if accuracy should be taken into account.
	 */
	public CombatContainer(Character attacker, Character victim, int hitAmount, CombatType hitType, boolean checkAccuracy) {
		this.attacker = attacker;
		this.victim = victim;
		this.combatType = hitType;
		this.checkAccuracy = checkAccuracy;
		this.hits = prepareHits(hitAmount);
		this.experience = getSkills(hitType);
		this.hitDelay = hitType == CombatType.MELEE ? 0 : hitType == CombatType.RANGED ? 2 : hitType == CombatType.MAGIC || hitType == CombatType.DRAGON_FIRE ? 3 : 1;
	}
	
	public CombatContainer(Character attacker, Character victim, int hitAmount, int hitDelay, CombatType hitType, boolean checkAccuracy) {
		this.attacker = attacker;
		this.victim = victim;
		this.combatType = hitType;
		this.checkAccuracy = checkAccuracy;
		this.hits = prepareHits(hitAmount);
		this.experience = getSkills(hitType);
		this.hitDelay = hitDelay;
	}

	/**
	 * Create a new {@link CombatContainer} that will deal no hit this turn.
	 * Used for things like spells that have special effects but don't deal
	 * damage.
	 * 
	 * @param checkAccuracy
	 *            if accuracy should be taken into account.
	 */
	public CombatContainer(Character attacker, Character victim, CombatType hitType, boolean checkAccuracy) {
		this(attacker, victim, 0, hitType, checkAccuracy);
	}

	/**
	 * Prepares the hits that will be dealt this combat hook.
	 * 
	 * @param hitAmount
	 *            the amount of hits to deal, maximum 4 and minimum 0.
	 * @return the hits that will be dealt this combat hook.
	 */
	private final CombatHit[] prepareHits(int hitAmount) {

		// Check the hit amounts.
		if (hitAmount > 4) {
			throw new IllegalArgumentException(
					"Illegal number of hits! The maximum number of hits per turn is 4.");
		} else if (hitAmount < 0) {
			throw new IllegalArgumentException(
					"Illegal number of hits! The minimum number of hits per turn is 0.");
		}

		// No hit for this turn, but we still need to calculate accuracy.
		if (hitAmount == 0) {
			accurate = !checkAccuracy || CombatFactory.rollAccuracy(attacker, victim, combatType);
			return new CombatHit[] {};
		}
		if (attacker.isPlayer()) {
			if (((Player)attacker).getEquipment().containsAny( 3082,14006,3928,18865,18468,16137,3951,14559,7806)) {
				hitAmount = 1; //Client doesn't support more than 4
 			}
		}
		if (attacker.isPlayer()) {
			if (((Player)attacker).getEquipment().containsAny( 19004,16429, 18398, 17931 ,3651,18385,7682,7688,19620,19123,6930,3276,16550,5115,18683,1667,2543,6320,13094,6483,5131,6197,923,5130,13207,5132,15653,5195, 13201, 3072,14559,3941,896,5125,5130,5134,5195,5129,19618)) {
				hitAmount = 2; //Client doesn't support more than 4
 			}
		}
		if (attacker.isPlayer()) {
			if (((Player)attacker).getEquipment().containsAny( 19163,12428,16429,18398,3250,3279,3282,8001,9492,2760,19727,10905,19154,3072,6197,19720,5132,5089,3991,5133,15656,2760)) {
				hitAmount = 3; //Client doesn't support more than 4
 			}
		}
		if (attacker.isPlayer()) {
			if (((Player)attacker).getEquipment().containsAny( 1413,3274,3920,22196, 3077, 8654, 8655, 8656,923,3990,5081,18919,8664,4796,18931,13995,20427, 4059, 12426)) {
				hitAmount = 4; //Client doesn't support more than 4
 			}
		}
		// Create the new array of hits, and populate it. Here we do the maximum
		// hit and accuracy calculations.
		CombatHit[] array = new CombatHit[hitAmount];
		for (int i = 0; i < array.length; i++) {
			boolean accuracy = !checkAccuracy || CombatFactory.rollAccuracy(attacker, victim, combatType);
			array[i] = new CombatHit(CombatFactory.getHit(attacker, victim, combatType), accuracy);
			if (array[i].isAccurate()) {
				accurate = true;
			}
			if(attacker instanceof RaidNpc) {
				accurate = true;
			}
		}

		/** SPECS **/
		
		if(attacker.isPlayer() && ((Player)attacker).isSpecialActivated()) {
			if(((Player)attacker).getCombatSpecial() == CombatSpecial.DRAGON_CLAWS && hitAmount == 4) {
				int first = array[0].getHit().getDamage();
				if(first > 360) {
					first = 360 + Misc.getRandom(10);
				}
				int second = first <= 0 ? array[1].getHit().getDamage() : (int) (first/2);
				int third = first <= 0 && second > 0 ? (int) (second/2) :  first <= 0 && second <= 0 ? array[2].getHit().getDamage() :  Misc.getRandom(second);
				int fourth = first <= 0 && second <= 0 && third <= 0 ? (int) array[3].getHit().getDamage() + Misc.getRandom(7) : first <= 0 && second <= 0 ? array[3].getHit().getDamage() : third;
				array[0].getHit().setDamage(first);
				array[1].getHit().setDamage(second);
				array[2].getHit().setDamage(third);
				array[3].getHit().setDamage(fourth);
			} else if(((Player)attacker).getCombatSpecial() == CombatSpecial.DARK_BOW && hitAmount == 2) {
				for(int i = 0; i < hitAmount; i++) {
					if(array[i].getHit().getDamage() < 80) {
						array[i].getHit().setDamage(80);
					}
					array[i].setAccurate(true);
				}
			}
		}
		return array;
	}

	public void setHits(CombatHit[] hits) {
		this.hits = hits;
		prepareHits(hits.length);
	}

	/**
	 * Performs an action on every single hit in this container.
	 * 
	 * @param action
	 *            the action to perform on every single hit.
	 */
	protected final void allHits(Consumer<CombatHit> c) {
		Arrays.stream(hits).filter(Objects::nonNull).forEach(c);
	}

	public final int getDamage() {
		int damage = 0;
		for (CombatHit hit : hits) {
			if (hit == null)
				continue;
			if (!hit.accurate) {
				int absorb = hit.getHit().getAbsorb();
				hit.hit = new Hit(0, Hitmask.RED, CombatIcon.BLOCK);
				hit.hit.setAbsorb(absorb);
			}
			damage += hit.hit.getDamage();
		}
		return damage;
	}
	
	public final void dealDamage() {
		if (hits.length == 1) {
			victim.dealDamage(hits[0].getHit());
		} else if (hits.length == 2) {
			victim.dealDoubleDamage(hits[0].getHit(), hits[1].getHit());
		} else if (hits.length == 3) {
			victim.dealTripleDamage(hits[0].getHit(), hits[1].getHit(), hits[2].getHit());
		} else if (hits.length == 4) {
			victim.dealQuadrupleDamage(hits[0].getHit(), hits[1].getHit(), hits[2].getHit(), hits[3].getHit());
		}
	}

	/**
	 * Gets all of the skills that will be trained.
	 * 
	 * @param type
	 *            the combat type being used.
	 * 
	 * @return an array of skills that this attack will train.
	 */
	private final int[] getSkills(CombatType type) {
		if (attacker.isNpc()) {
			return new int[] {};
		}
		return ((Player) attacker).getFightType().getStyle().skill(type);
	}
	
	public void setModifiedDamage(int modifiedDamage) {
		this.modifiedDamage = modifiedDamage;
	}
	
	public int getModifiedDamage() {
		return modifiedDamage;
	}

	/**
	 * A dynamic method invoked when the victim is hit with an attack. An
	 * example of usage is using this to do some sort of special effect when the
	 * victim is hit with a spell. <b>Do not reset combat builder in this
	 * method!</b>
	 * 
	 * @param damage
	 *            the damage inflicted with this attack, always 0 if the attack
	 *            isn't accurate.
	 * @param accurate
	 *            if the attack is accurate.
	 */
	public void onHit(int damage, boolean accurate) {}

	/**
	 * Gets the hits that will be dealt during this combat hook.
	 * 
	 * @return the hits that will be dealt during this combat hook.
	 */
	public final CombatHit[] getHits() {
		return hits;
	}

	/**
	 * Gets the skills that will be given experience.
	 * 
	 * @return the skills that will be given experience.
	 */
	public final int[] getExperience() {
		return experience;
	}

	/**
	 * Sets the amount of hits that will be dealt during this combat hook.
	 * 
	 * @param hitAmount
	 *            the amount of hits that will be dealt during this combat hook.
	 */
	public final void setHitAmount(int hitAmount) {
		this.hits = prepareHits(hitAmount);
	}

	/**
	 * Gets the combat type that is being used during this combat hook.
	 * 
	 * @return the combat type that is being used during this combat hook.
	 */
	public final CombatType getCombatType() {
		return combatType;
	}

	/**
	 * Sets the combat type that is being used during this combat hook.
	 * 
	 * @param combatType
	 *            the combat type that is being used during this combat hook.
	 */
	public final void setCombatType(CombatType combatType) {
		this.combatType = combatType;
	}

	/**
	 * Gets if accuracy should be taken into account.
	 * 
	 * @return true if accuracy should be taken into account.
	 */
	public final boolean isCheckAccuracy() {
		return checkAccuracy;
	}

	/**
	 * Sets if accuracy should be taken into account.
	 * 
	 * @param checkAccuracy
	 *            true if accuracy should be taken into account.
	 */
	public final void setCheckAccuracy(boolean checkAccuracy) {
		this.checkAccuracy = checkAccuracy;
	}

	/**
	 * Gets if at least one hit in this container is accurate.
	 * 
	 * @return true if at least one hit in this container is accurate.
	 */
	public final boolean isAccurate() {
		return accurate;
	}

	/**
	 * Gets the hit delay before the hit is executed.
	 * 
	 * @return the hit delay.
	 */
	public int getHitDelay() {
		return hitDelay;
	}

	/**
	 * A single hit that is dealt during a combat hook.
	 * 
	 * @author lare96
	 */
	public static class CombatHit {

		/** The actual hit that will be dealt. */
		private Hit hit;

		/** The accuracy of the hit to be dealt. */
		private boolean accurate;

		/**
		 * Create a new {@link CombatHit}.
		 * 
		 * @param hit
		 *            the actual hit that will be dealt.
		 * @param accurate
		 *            the accuracy of the hit to be dealt.
		 */
		public CombatHit(Hit hit, boolean accurate) {
			this.hit = hit;
			this.accurate = accurate;
		}

		/**
		 * Gets the actual hit that will be dealt.
		 * 
		 * @return the actual hit that will be dealt.
		 */
		public Hit getHit() {
			return hit;
		}

		/**
		 * Sets the actual hit that will be dealt.
		 * 
		 * @param hit
		 *            the actual hit that will be dealt.
		 */
		public void setHit(Hit hit) {
			this.hit = hit;
		}

		/**
		 * Gets the accuracy of the hit to be dealt.
		 * 
		 * @return the accuracy of the hit to be dealt.
		 */
		public boolean isAccurate() {
			return accurate;
		}

		/**
		 * Sets the accuracy of the hit to be dealt.
		 * 
		 * @param accurate
		 *            the accuracy of the hit to be dealt.
		 */
		public void setAccurate(boolean accurate) {
			this.accurate = accurate;
		}
	}
}
