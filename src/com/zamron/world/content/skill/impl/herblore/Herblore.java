package com.zamron.world.content.skill.impl.herblore;

import com.zamron.engine.task.Task;
import com.zamron.engine.task.TaskManager;
import com.zamron.model.Animation;
import com.zamron.model.Item;
import com.zamron.model.Skill;
import com.zamron.model.definitions.ItemDefinition;
import com.zamron.util.Misc;
import com.zamron.world.content.Achievements;
import com.zamron.world.content.Achievements.AchievementData;
import com.zamron.world.entity.impl.player.Player;

public class Herblore {

	public static final int VIAL = 227;
	private static final Animation ANIMATION = new Animation(363);

	public static boolean cleanHerb(final Player player, final int herbId) {
		Herbs herb = Herbs.forId(herbId);
		if (herb == null) {
			return false;
		}
		if(player.getInventory().contains(herb.getGrimyHerb())) {
			if (player.getSkillManager().getCurrentLevel(Skill.HERBLORE) < herb.getLevelReq()) {
				player.getPacketSender().sendMessage("You need a Herblore level of at least " + herb.getLevelReq() + " to clean this leaf.");
				return false;
			}
			player.getInventory().delete(herb.getGrimyHerb(), 1);
			player.getInventory().add(herb.getCleanHerb(), 1);
			player.getSkillManager().addExperience(Skill.HERBLORE, herb.getExp());
			player.getPacketSender().sendMessage("You clean the dirt off the leaf.");
			return true;
		}
		return false;
	}

	public static boolean makeUnfinishedPotion(final Player player, final int herbId) {
		final UnfinishedPotions unf = UnfinishedPotions.forId(herbId);
		if (unf == null)
			return false;
		if (player.getSkillManager().getCurrentLevel(Skill.HERBLORE) < unf.getLevelReq()) {
			player.getPacketSender().sendMessage("You need a Herblore level of at least " + unf.getLevelReq() + " to make this potion.");
			return false;
		}
		if (player.getInventory().contains(VIAL) && player.getInventory().contains(unf.getHerbNeeded())) {
			player.getSkillManager().stopSkilling();
			player.performAnimation(ANIMATION);
			TaskManager.submit(new Task(1, player, false) {
				public void execute() {
					player.getInventory().delete(VIAL, 1).delete(unf.getHerbNeeded(), 1).add(unf.getUnfPotion(), 1);
					player.getPacketSender().sendMessage("You put the " + ItemDefinition.forId(unf.getHerbNeeded()).getName() + " into the vial of water.");
					player.getSkillManager().addExperience(Skill.HERBLORE, 1);
					this.stop();
				}
			});
			return true;
		}
		return false;
	}

	public static boolean finishPotion(final Player player, final int itemUsed, final int usedWith) {
		final FinishedPotions pot = FinishedPotions.forId(itemUsed, usedWith);
		if(pot == FinishedPotions.MISSING_INGRIDIENTS) {
			player.getPacketSender().sendMessage("You don't have the required items to make this potion.");
			return false;
		}
		if (pot == null) {
			handleSpecialPotion(player, itemUsed, usedWith);
			return false;
		}
		if (player.getSkillManager().getCurrentLevel(Skill.HERBLORE) < pot.getLevelReq()) {
			player.getPacketSender().sendMessage("You need a Herblore level of at least " + pot.getLevelReq() + " to make this potion.");
			return false;
		}
		if(player.getInventory().contains(pot.getUnfinishedPotion()) && player.getInventory().contains(pot.getItemNeeded())) {
			player.getSkillManager().stopSkilling();
			player.performAnimation(ANIMATION);
			TaskManager.submit(new Task(1, player, false) {
				public void execute() {
					player.getInventory().delete(pot.getUnfinishedPotion(), 1).delete(pot.getItemNeeded(), 1).add(pot.getFinishedPotion(), 1);
					player.getSkillManager().addExperience(Skill.HERBLORE, (pot.getExpGained() * 80));
					String name = ItemDefinition.forId(pot.getFinishedPotion()).getName();
					player.getPacketSender().sendMessage("You combine the ingredients to make "+Misc.anOrA(name)+" "+name+".");
					Achievements.finishAchievement(player, AchievementData.MIX_A_POTION);
					this.stop();
				}
			});
			return true;
		} else
			player.getPacketSender().sendMessage("You don't have the required items to make this potion.");
		return false;
	}

	enum SpecialPotion {
		EXTREME_ATTACK(new Item[] {new Item(145), new Item(261)}, new Item(15309), 88, 4430),
		EXTREME_STRENGTH(new Item[] {new Item(157), new Item(267)}, new Item(15313), 88, 4753),
		EXTREME_DEFENCE(new Item[] {new Item(163), new Item(2481)}, new Item(15317), 90, 5002),
		EXTREME_MAGIC(new Item[] {new Item(3042), new Item(9594)}, new Item(15321), 91, 5408),
		EXTREME_RANGED(new Item[] {new Item(169), new Item(12539, 5)}, new Item(15325), 92, 5924),
		OVERLOAD(new Item[] {new Item(15309), new Item(15313), new Item(15317), new Item(15321), new Item(15325)}, new Item(15333), 96, 13103);

		SpecialPotion(Item[] ingridients, Item product, int lvlReq, int exp) {
			this.ingridients = ingridients;
			this.product = product;
			this.lvlReq = lvlReq;
			this.exp = exp;
		}

		private Item[] ingridients;

		public Item[] getIngridients() {
			return ingridients;
		}

		private Item product;

		public Item getProduct() {
			return product;
		}

		private int lvlReq, exp;

		public int getLevelReq() {
			return lvlReq;
		}

		public int getExperience() {
			return exp;
		}

		public static SpecialPotion forItems(int item1, int item2) {
			for(SpecialPotion potData : SpecialPotion.values()) {
				int found = 0;
				for(Item it : potData.getIngridients()) {
					if(it.getId() == item1 || it.getId() == item2)
						found++;
				}
				if(found >= 2)
					return potData;
			}
			return null;
		}
	}

	public static void handleSpecialPotion(Player p, int item1, int item2) {
		if(item1 == item2)
			return;
		if(!p.getInventory().contains(item1) || !p.getInventory().contains(item2)) 
			return;
		SpecialPotion specialPotData = SpecialPotion.forItems(item1, item2);
		if(specialPotData == null)
			return;
		if(p.getSkillManager().getCurrentLevel(Skill.HERBLORE) < specialPotData.getLevelReq()) {
			p.getPacketSender().sendMessage("You need a Herblore level of at least "+specialPotData.getLevelReq()+" to make this potion.");
			return;
		}
		if(!p.getClickDelay().elapsed(500))
			return;
		for(Item ingridients : specialPotData.getIngridients()) {
			if(!p.getInventory().contains(ingridients.getId()) || p.getInventory().getAmount(ingridients.getId()) < ingridients.getAmount()) {
				p.getPacketSender().sendMessage("You do not have all ingridients for this potion.");
				p.getPacketSender().sendMessage("Remember: You can purchase an Ingridient's book from the Druid Spirit.");
				return;
			}
		}
		for(Item ingridients : specialPotData.getIngridients())
			p.getInventory().delete(ingridients);
		p.getInventory().add(specialPotData.getProduct());
		p.performAnimation(new Animation(363));
		p.getSkillManager().addExperience(Skill.HERBLORE, specialPotData.getExperience());
		String name = specialPotData.getProduct().getDefinition().getName();
		p.getPacketSender().sendMessage("You make "+Misc.anOrA(name)+" "+ name + ".");
		p.getClickDelay().reset();
		if(specialPotData == SpecialPotion.OVERLOAD) {
			Achievements.finishAchievement(p, AchievementData.MIX_AN_OVERLOAD_POTION);
			Achievements.doProgress(p, AchievementData.MIX_100_OVERLOAD_POTIONS);
		}
	}
}
