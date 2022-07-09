package com.zamron.world.content;

import com.zamron.model.Animation;
import com.zamron.model.Direction;
import com.zamron.model.Graphic;
import com.zamron.model.Hit;
import com.zamron.model.Position;
import com.zamron.model.Projectile;
import com.zamron.util.Misc;
import com.zamron.util.RandomUtility;
import com.zamron.world.World;
import com.zamron.world.content.dialogue.DialogueManager;
import com.zamron.world.entity.impl.npc.NPC;
import com.zamron.world.entity.impl.player.Player;

public class NpcBattles {

	private static NPC npc1, npc2;
	
	private static Player player1, player2;
	
	public static boolean player1Turn = true;
	
	private static int dmgDone1, dmgDone2;
	
	private static boolean hasWon = false;
	
	public static void startBattle(Player p1, Player p2) {
		player1 = p1;
		player2 = p2;
		player1.moveTo(new Position(3330, 3335));
		player2.moveTo(new Position(3326, 3335));
		player1.setDirection(Direction.WEST);
		player2.setDirection(Direction.EAST);
		player1.setPlayerLocked(true);
		player2.setPlayerLocked(true);
		npc1 = new NPC(6306, new Position(3330, 3334));
		npc2 = new NPC(6306, new Position(3326, 3334));
		npc1.setDirection(Direction.WEST);
		npc2.setDirection(Direction.EAST);
		World.register(npc1);
		World.register(npc2);
		
		player1.setDialogueActionId(811);
		
		DialogueManager.start(player1, 184);
		
	}
	
	private static void declareWinner() {
		
		if(dmgDone1 >= 25000) {
			//System.out.println("Winner: " + player1.getUsername());
			npc1.setDying(true);
			World.deregister(npc1);
			npc2.setDying(true);
			World.deregister(npc2);
			player1.forceChat("I win!");
			player2.forceChat("I lose :(");
			hasWon = true;
		} else if(dmgDone2 >= 25000) {
			npc1.setDying(true);
			World.deregister(npc1);
			npc2.setDying(true);
			World.deregister(npc2);
			player2.forceChat("I win!");
			player1.forceChat("I lose :(");
			player1.setPlayerLocked(false);
			player2.setPlayerLocked(false);
			hasWon = true;
		}
		
	}
	
	public static void attack(int attackNumber) {
		
		int random = RandomUtility.inclusiveRandom(100);
		
		if(attackNumber == 1) {
			if(random >= 10) {
				if(player1Turn) {
					npc1.performAnimation(new Animation(1979));
					new Projectile(npc1, npc2, 500, 44, 3, 65, 15, 0).sendProjectile();
					int randomHit = Misc.random(10, 1000);
					npc2.dealDamage(new Hit(randomHit));
					dmgDone1 += randomHit;
				} else {
					npc2.performAnimation(new Animation(1979));
					new Projectile(npc2, npc1, 500, 44, 3, 65, 15, 0).sendProjectile();
					int randomHit = Misc.random(10, 1000);
					npc1.dealDamage(new Hit(Misc.random(randomHit)));
					dmgDone2 += randomHit;
				}
			} else {
				if(player1Turn) {
					npc1.performAnimation(new Animation(1979));
					new Projectile(npc1, npc2, 500, 44, 3, 65, 15, 0).sendProjectile();
					npc2.dealDamage(new Hit(0));
				} else {
					npc2.performAnimation(new Animation(1979));
					new Projectile(npc2, npc1, 500, 44, 3, 65, 15, 0).sendProjectile();
					npc1.dealDamage(new Hit(0));
				}
			}
		}
		
		if(attackNumber == 2) {
			if(random >= 50) {
				if(player1Turn) {
					npc1.performAnimation(new Animation(4947));
					npc2.performGraphic(new Graphic(3004));
					int randomHit = Misc.random(500, 2500);
					npc2.dealDamage(new Hit(Misc.random(randomHit)));
					dmgDone1 += randomHit;
				} else {
					npc2.performAnimation(new Animation(4947));
					npc1.performGraphic(new Graphic(3004));
					int randomHit = Misc.random(500, 2500);
					npc1.dealDamage(new Hit(Misc.random(randomHit)));
					dmgDone2 += randomHit;
				}
			} else {
				if(player1Turn) {
					npc1.performAnimation(new Animation(4947));
					npc2.performGraphic(new Graphic(3004));
					npc2.dealDamage(new Hit(0));
				} else {
					npc2.performAnimation(new Animation(4947));
					npc1.performGraphic(new Graphic(3004));
					npc1.dealDamage(new Hit(0));
				}
			}
		}
		
		if(attackNumber == 3) {
			if(random >= 80) {
				if(player1Turn) {
					npc1.performAnimation(new Animation(4939));
					npc2.performGraphic(new Graphic(2981));
					int randomHit = Misc.random(1000, 6000);
					npc2.dealDamage(new Hit(Misc.random(randomHit)));
					dmgDone1 += randomHit;
				} else {
					npc2.performAnimation(new Animation(4939));
					npc1.performGraphic(new Graphic(2981));
					int randomHit = Misc.random(1000, 6000);
					npc1.dealDamage(new Hit(Misc.random(randomHit)));
					dmgDone2 += randomHit;
				}
			} else {
				if(player1Turn) {
					npc1.performAnimation(new Animation(4939));
					npc2.performGraphic(new Graphic(2981));
					npc2.dealDamage(new Hit(0));
				} else {
					npc2.performAnimation(new Animation(4939));
					npc1.performGraphic(new Graphic(2981));
					npc1.dealDamage(new Hit(0));
				}
			}
		}
		
		if(player1Turn) {
			player1.getPacketSender().sendInterfaceRemoval();
		} else {
			player2.getPacketSender().sendInterfaceRemoval();
		}
		
		player1Turn = player1Turn ? false : true;
		//System.out.println("DAMAGE DONE SO FAR for each: " + dmgDone1 + " and " + dmgDone2);
		player1.forceChat("My total damage done: " + dmgDone1);
		player2.forceChat("My total damage done: " + dmgDone2);
		declareWinner();
		if(hasWon)
			return;
		
		if(player1Turn) {
			player1.setDialogueActionId(811);
			DialogueManager.start(player1, 184);
		} else {
			player2.setDialogueActionId(811);
			DialogueManager.start(player2, 184);
		}
	}
	
	
}
