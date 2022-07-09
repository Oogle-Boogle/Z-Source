package com.zamron.world.content.combat.strategy.impl;

import com.zamron.engine.task.Task;
import com.zamron.engine.task.TaskManager;
import com.zamron.model.Animation;
import com.zamron.model.Graphic;
import com.zamron.model.GraphicHeight;
import com.zamron.model.Locations;
import com.zamron.model.Position;
import com.zamron.model.Projectile;
import com.zamron.util.Misc;
import com.zamron.world.World;
import com.zamron.world.content.combat.CombatContainer;
import com.zamron.world.content.combat.CombatHitTask;
import com.zamron.world.content.combat.CombatType;
import com.zamron.world.content.combat.strategy.CombatStrategy;
import com.zamron.world.entity.impl.Character;
import com.zamron.world.entity.impl.npc.NPC;
import com.zamron.world.entity.impl.player.Player;
/*
 * cB Script.
 * @author live:nrpker7839
 */

public class Icedemon implements CombatStrategy {

	private static int animID;
	private static int health;
	private static int attack;
	private static int defence;
	public int undefined = 1;

	public static NPC cB;

	private int gfxID;

	public void setGfxID(int gfxID) {
		this.gfxID = gfxID;
	}

	public static int getAnimID() {
		return animID;
	}

	public static int getAttack() {
		return attack;
	}

	public static int getDefence() {
		return defence;
	}

	public static int getHealth() {
		return health;
	}

	private static final Animation attack_anim = new Animation(13791);// but low anim ids
	private static final Animation Stage2 = new Animation(13790);

	// private static final Graphic graphic1 = new Graphic(1212);
	// private static final Graphic gfx2= new Graphic(1213);
	private static final Graphic gfx2 = new Graphic(606, 3, GraphicHeight.LOW);

	public static String[] messages = { "You will fear the demon powers!", "I can only think of destroying you!!"

	};

	@Override
	public boolean customContainerAttack(Character entity, Character victim) {
		NPC cB = (NPC) entity;
		Player target = (Player) victim;
		if (cB.isChargingAttack() || cB.getConstitution() <= 0) {
			return true;
		}
		boolean melee = Misc.getRandom(3) == 1 && Locations.goodDistance(target.getPosition(), cB.getPosition(), 1);
		if (melee) {

			cB.getCombatBuilder().setVictim(target);
			cB.performAnimation(new Animation(69));
			new CombatHitTask(cB.getCombatBuilder(), new CombatContainer(cB, target, 1, CombatType.MELEE, true))
					.handleAttack();
		}
		cB.performAnimation(attack_anim);
		cB.performGraphic(gfx2);
		TaskManager.submit(new Task(1, victim, false) {
			int tick = 0;

			@Override
			public void execute() {
				switch (tick) {
				case 1:

					int random = Misc.getRandom(100);

					if (random >= 80) {
						if (random >= 80 && Misc.getRandom(10) == 4) {
							cB.forceChat(messages[Misc.getRandom(messages.length - 1)]);
						}
						if (cB.getConstitution() <= 0) {
							cB.forceChat("oh fuck you, you absolute wank stain.");
							return;
						}
						if (victim.getConstitution() <= 0) {
							cB.forceChat("Valar moulghulis");
							return;
						}
						cB.forceChat("You Shall Die!");
						new Projectile(cB, target, 606, 44, 3, 43, 43, 0).sendProjectile();
					} else {
						cB.performAnimation(Stage2);
					}
					break;
				case 2:
					new CombatHitTask(cB.getCombatBuilder(),
							new CombatContainer(cB, target, 1, CombatType.MAGIC, false)).handleAttack();
					stop();
					break;
				}
				tick++;
			}

		});
		return true;
	}

	protected static void forceChat(String string) {
		setForcedChat(messages);

	}

	private static void setForcedChat(String[] messages2) {
		// this.forcedChat = forcedChat;
		// return this;
	}

	@Override
	public boolean canAttack(Character entity, Character victim) {
		return victim.isPlayer();
	}

	@Override
	public CombatContainer attack(Character entity, Character victim) {
		return null;
	}

	@Override
	public int attackDelay(Character entity) {
		return entity.getAttackSpeed();
	}

	@Override
	public int attackDistance(Character entity) {
		return 4;
	}

	public static void spawn() {
		cB = new NPC(10051, new Position(0, 0));

		World.register(cB);
	}

	public static void despawn(boolean nex) {

		if (cB != null && cB.isRegistered())
			World.deregister(cB);
	}

	public int getGfxID() {
		return gfxID;
	}

	@Override
	public CombatType getCombatType() {
		return CombatType.MIXED;
	}

	/*
	 * 
	 * private static int animID; private static int health; private static int
	 * attack; private static int defence; public int undefined = 1;
	 * 
	 * public static NPC cB; private int gfxID;
	 * 
	 * public void setGfxID(int gfxID) { this.gfxID = gfxID; } public static int
	 * getAnimID() { return animID; }
	 * 
	 * public static int getAttack() { return attack; }
	 * 
	 * public static int getDefence() { return defence; }
	 * 
	 * 
	 * public static int getHealth() { return health; } private static final
	 * Animation Stage1 = new Animation(13791); private static final Animation
	 * Stage2 = new Animation(12062);
	 * 
	 * private static final Graphic gfx12 = new Graphic(606, 3, GraphicHeight.LOW);
	 * 
	 * 
	 * 
	 * // public Animation Stage1 = new Animation(13791); // public Animation Stage2
	 * = new Animation(12062);
	 * 
	 * // public Animation Stage3 = new Animation(12061);
	 * 
	 * public boolean canAttack(Character entity, Character victim) { return true; }
	 * 
	 * public CombatContainer attack(Character entity, Character victim) { return
	 * null; }
	 * 
	 * 
	 * 
	 * 
	 * 
	 * public boolean customContainerAttack(Character entity, Character victim) {
	 * NPC uc = (NPC)entity; if(uc.getConstitution() <= 0) {
	 * cB.forceChat("oh fuck you, you absolute wank stain."); return true; }
	 * if(victim.getConstitution() <= 0) { cB.forceChat("Valar moulghulis"); return
	 * true; } if(uc.isChargingAttack() || victim.getConstitution() <= 0) { return
	 * true; } if(uc.isChargingAttack()) { return true; }
	 * 
	 * if(Locations.goodDistance(uc.getPosition().copy(),
	 * victim.getPosition().copy(), 1) && Misc.getRandom(6) <= 4) {
	 * uc.performAnimation(Stage1); victim.performGraphic(gfx12); cB.
	 * forceChat("lord of dicks.. Show me the fire *lord of light shows them tyrion jerking off whatever he has to jerk off*"
	 * ); uc.getCombatBuilder().setContainer(new CombatContainer(uc, victim, 1, 2,
	 * CombatType.MELEE, true)); } else if(Misc.getRandom(10) <= 7) {
	 * uc.performAnimation(Stage2); uc.setChargingAttack(true); cB.
	 * forceChat("Why are all the gods such vicious c*nts? Where's the god of tits and wine? - tyrion"
	 * ); uc.getCombatBuilder().setContainer(new CombatContainer(uc, victim, 1, 5,
	 * CombatType.MAGIC, false)); TaskManager.submit(new Task(1, uc, false) {
	 * 
	 * 
	 * @Override protected void execute() { // new Projectile(uc, victim, 606, 44,
	 * 1, 4, 4, 0).sendProjectile(); new Projectile(uc, victim, 0, 100, 1, 4, 4,
	 * 0).sendProjectile();
	 * 
	 * cB.forceChat("bend the knee jon snow"); uc.setChargingAttack(false); stop();
	 * } }); } else { //System.out.println("Attacking now");
	 * uc.setChargingAttack(true); uc.performAnimation(new
	 * Animation(uc.getDefinition().getAttackAnimation()));
	 * 
	 * final Position start = victim.getPosition().copy(); final Position second =
	 * new Position(start.getX() + 2, start.getY() + Misc.getRandom(2)); final
	 * Position last = new Position (start.getX() - 2, start.getY() -
	 * Misc.getRandom(2));
	 * 
	 * 
	 * final Player p = (Player)victim; final List<Player> list =
	 * Misc.getCombinedPlayerList(p); uc.getCombatBuilder().setContainer(new
	 * CombatContainer(uc, victim, 1, 5, CombatType.MAGIC, true));
	 * TaskManager.submit(new Task(1, uc, false) { int tick = 0;
	 * 
	 * @Override public void execute() { if(tick == 0) {
	 * p.getPacketSender().sendGlobalGraphic(new Graphic(2440), start);
	 * p.getPacketSender().sendGlobalGraphic(new Graphic(606), second);
	 * p.getPacketSender().sendGlobalGraphic(new Graphic(2441), last); } else
	 * if(tick == 3) { for(Player t : list) { if(t == null) continue;
	 * 
	 * } uc.setChargingAttack(false); stop(); } tick++; } }); } return true; }
	 * 
	 * 
	 * 
	 * 
	 * public CombatType getCombatType() { return CombatType.MIXED; }
	 * 
	 * 
	 * 
	 * 
	 * public int attackDelay(Character entity) { return entity.getAttackSpeed(); }
	 * 
	 * public int attackDistance(Character entity) { return 1; }
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * public static void spawn() { cB = new NPC(10051, new Position(0, 0));
	 * 
	 * World.register(cB); } public static void despawn(boolean nex) {
	 * 
	 * if(cB != null && cB.isRegistered()) World.deregister(cB); }
	 * 
	 * 
	 * public int getGfxID() { return gfxID; }
	 */
}
