package com.zamron.world.content.combat.strategy.impl;

import com.zamron.model.Animation;
import com.zamron.model.CombatIcon;
import com.zamron.model.Graphic;
import com.zamron.model.Hit;
import com.zamron.model.Hitmask;
import com.zamron.model.Position;
import com.zamron.util.Misc;
import com.zamron.world.content.combat.CombatContainer;
import com.zamron.world.content.combat.CombatHitTask;
import com.zamron.world.content.combat.CombatType;
import com.zamron.world.content.combat.strategy.CombatStrategy;
import com.zamron.world.entity.impl.Character;
import com.zamron.world.entity.impl.npc.NPC;
import com.zamron.world.entity.impl.player.Player;

/** @author live:nrpker7839 <-- skype. **/

public class Lexicus implements CombatStrategy	 {

	public int phase = 0;
	public int i = 0;
	public int attackSpeed = 5;
	public static NPC Lexicus;
	private static final Animation anim = new Animation(13471);
	@SuppressWarnings("unused")
	private static final Animation Standanim = new Animation(13467);


	
	public static void createInstance(Player player) {	
		player.getPacketSender().sendInterfaceRemoval();
		player.moveTo(new Position(3696, 5807, player.getIndex() * 4+1));
	//	player.setRegionInstance(new RegionInstance(player, RegionInstanceType.Lexicus));
	//	DialogueManager.start(player, LexicusSpawning.get(player, 0));
	}

	
	@Override
	public boolean canAttack(Character entity, Character victim) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public CombatContainer attack(Character entity, Character victim) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean customContainerAttack(Character entity, Character victim) {
		int random = Misc.getRandom(10);
		Player player = (Player)victim;
		
		if(phase == 0) {

			new CombatHitTask(entity.getCombatBuilder(), new CombatContainer(entity, player, 1, CombatType.MAGIC, true)).handleAttack();
			
			entity.performAnimation(anim);
			player.performGraphic(new Graphic(435));
			player.performGraphic(new Graphic(1207));
			if(random == 5) {
				entity.forceChat("I will feast on your body!");
			}
			if(entity.getConstitution() < 7000) {
				phase = 1;
				attackSpeed = 2;
			}
			
		} else if(phase == 1) {
			i++;
			entity.forceChat("Experience my blood lust!");
			entity.performAnimation(anim);
			player.dealDamage(new Hit(80, Hitmask.RED, CombatIcon.NONE));
			
			if(i == 6) {
				phase = 2;
				i = 0;
				attackSpeed = 3;
			}
			
		} else if(phase == 2) {
			if(player.getConstitution() > 0) {
				if(i == 0) {
					entity.forceChat("How did you survive?");
				} else if(i == 1) {
					entity.forceChat("You think you're tough, dont you?");
				} else if(i == 2) {
					entity.forceChat("I think you should put pray range on!");
				}
				
				if(i > 2 && i < 10) {
					new CombatHitTask(entity.getCombatBuilder(), new CombatContainer(entity, player, 1, CombatType.MAGIC, true)).handleAttack();
					new CombatHitTask(entity.getCombatBuilder(), new CombatContainer(entity, player, 1, CombatType.MELEE, true)).handleAttack();
entity.performAnimation(anim);
					player.performGraphic(new Graphic(1194));
				} else if(i == 10) {
					entity.forceChat("Whats going on! I feel weakened...");
				} else if(i == 11) {
					entity.forceChat("Let me steal some blood from you real quick...");
					player.dealDamage(new Hit(500, Hitmask.RED, CombatIcon.NONE));
					entity.performAnimation(anim);
				} else if(i >= 12) {
					player.getPacketSender().sendMessage("@red@Lexicus steals some blood from you");
					entity.forceChat("My powers have returned! Time for you to feel my wrath!");
					new CombatHitTask(entity.getCombatBuilder(), new CombatContainer(entity, player, 1, CombatType.MAGIC, true)).handleAttack();
					player.performGraphic(new Graphic(434));
					player.performGraphic(new Graphic(1207));
					entity.performAnimation(anim);


				}
				i++;
				if(entity.getConstitution() < 4000) {
					phase = 3;
					i = 0;
				}
			}

		} else if(phase == 3) {
			if(i == 0) {
				entity.forceChat("Fear me, "+player.getUsername());
				entity.performAnimation(anim);

			} else if(i >= 7 && i < 11) {
				attackSpeed = 1;
				new CombatHitTask(entity.getCombatBuilder(), new CombatContainer(entity, player, 1, CombatType.MAGIC, true)).handleAttack();
				player.getPacketSender().sendMessage("@blu@Lexicus starts dealing super fast magic damage!");				
				player.performAnimation(anim);
				entity.performAnimation(anim);
				player.performGraphic(new Graphic(433));
				player.performGraphic(new Graphic(1207));


			} else if(entity.getConstitution() < 2000 && i >= 11) {
				if(i == 12) {
					entity.forceChat("What is this? My powers are draining, Pls kms");
				}
				attackSpeed = 1;
				new CombatHitTask(entity.getCombatBuilder(), new CombatContainer(entity, player, 1, CombatType.MELEE, true)).handleAttack();
				entity.performAnimation(anim);
				player.performGraphic(new Graphic(433));
				player.performGraphic(new Graphic(1194));
				//yeah its this one.

			} else {
				attackSpeed = 5;
				new CombatHitTask(entity.getCombatBuilder(), new CombatContainer(entity, player, 1, CombatType.MAGIC, true)).handleAttack();
				entity.performAnimation(anim);
				player.performGraphic(new Graphic(435));
				player.performGraphic(new Graphic(665));

				if(random == 5) {
					entity.forceChat("I will feast on your body!");
				}
			}
				
			i++;
		}
		if(player.getConstitution() <= 0) {
			phase = 0;
			attackSpeed = 5;
		}
		return false;
	}

	@Override
	public int attackDelay(Character entity) {
		// TODO Auto-generated method stub
		return attackSpeed;
	}

	@Override
	public int attackDistance(Character entity) {
		// TODO Auto-generated method stub
		return 3;
	}


	@Override
	public CombatType getCombatType() {
		// TODO Auto-generated method stub
		return null;
	}

}
