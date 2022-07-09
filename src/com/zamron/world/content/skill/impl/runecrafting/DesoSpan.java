package com.zamron.world.content.skill.impl.runecrafting;

import com.zamron.engine.task.Task;
import com.zamron.engine.task.TaskManager;
import com.zamron.model.Animation;
import com.zamron.model.CombatIcon;
import com.zamron.model.Graphic;
import com.zamron.model.GraphicHeight;
import com.zamron.model.Hit;
import com.zamron.model.Hitmask;
import com.zamron.model.Position;
import com.zamron.model.Projectile;
import com.zamron.model.Skill;
import com.zamron.model.movement.MovementQueue;
import com.zamron.util.Misc;
import com.zamron.world.World;
import com.zamron.world.entity.impl.npc.NPC;
import com.zamron.world.entity.impl.player.Player;

public class DesoSpan {

	private static final Animation SIPHONING_ANIMATION = new Animation(9368);
	private static final int ENERGY_FRAGMENT = 13653;

	enum Energy {
		GREEN_ENERGY(8028, 1, 638, 912, 551, 999),
		YELLOW_ENERGY(8022, 66, 6500, 913, 554, 1006);

		Energy(int npcId, int levelReq, int experience, int playerGraphic, int projectileGraphic, int npcGraphic) {
			this.npcId = npcId;
			this.levelReq = levelReq;
			this.experience = experience;
			this.playerGraphic = playerGraphic;
			this.projectileGraphic = projectileGraphic;
			this.npcGraphic = npcGraphic;
		}

		public int npcId, levelReq, experience;
		public int playerGraphic, projectileGraphic, npcGraphic;

		static Energy forId(int npc) {
			for(Energy e : Energy.values()) {
				if(e.npcId == npc) 
					return e;
			}
			return null;
		}
	}

	public static void spawn() {
		int lastX = 0;
		for(int i = 0; i < 6; i++) {
			int randomX = 2595 + Misc.getRandom(12);
			if(randomX == lastX || randomX == lastX + 1 || randomX == lastX - 1)
				randomX++;
			int randomY = 4772 + Misc.getRandom(8);
			lastX = randomX;
			World.register(new NPC(i <= 3 ? 8028 : 8022, new Position(randomX, randomY)));
		}
	}

	public static void siphon(final Player player, final NPC n) {
		final Energy energyType = Energy.forId(n.getId());
		if(energyType != null) {
			player.getSkillManager().stopSkilling();
			if(player.getPosition().equals(n.getPosition()))
				MovementQueue.stepAway(player);
			player.setEntityInteraction(n);
			if(player.getSkillManager().getCurrentLevel(Skill.RUNECRAFTING) < energyType.levelReq) {
				player.getPacketSender().sendMessage("You need a Runecrafting level of at least "+energyType.levelReq+" to siphon this energy source.");
				return;
			}
			if(!player.getInventory().contains(ENERGY_FRAGMENT) && player.getInventory().getFreeSlots() == 0) {
				player.getPacketSender().sendMessage("You need some free inventory space to do this.");
				return;
			}
			player.performAnimation(SIPHONING_ANIMATION);
			new Projectile(player, n, energyType.projectileGraphic, 15, 44, 43, 31, 0).sendProjectile();			
			int cycle = 2 + Misc.getRandom(2);
			player.setCurrentTask(new Task(cycle, player, false) {
				@Override
				public void execute() {
					if(n.getConstitution() <= 0) {
						player.getPacketSender().sendMessage("This energy source has died out.");
						stop();
						return;
					}
					player.getSkillManager().addExperience(Skill.RUNECRAFTING, energyType.experience + Misc.getRandom(30));
					player.performGraphic(new Graphic(energyType.playerGraphic, GraphicHeight.HIGH));
					n.performGraphic(new Graphic(energyType.npcGraphic, GraphicHeight.HIGH));
					n.dealDamage(new Hit(Misc.getRandom(12), Hitmask.RED, CombatIcon.MAGIC));
					if(Misc.getRandom(30) <= 10) {
						player.dealDamage(new Hit(1 + Misc.getRandom(48), Hitmask.RED, CombatIcon.DEFLECT));
						player.getPacketSender().sendMessage("You accidently attempt to siphon too much energy, and get hurt.");
					} else {
						player.getPacketSender().sendMessage("You siphon some energy ..");
						player.getInventory().add(ENERGY_FRAGMENT, 1);
					}
					if(n.getConstitution() > 0 && player.getConstitution() > 0)
						siphon(player, n);
					stop();
				}
			});
			TaskManager.submit(player.getCurrentTask());
		}
	}

}
