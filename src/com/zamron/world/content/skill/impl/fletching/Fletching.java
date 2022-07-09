package com.zamron.world.content.skill.impl.fletching;

import com.zamron.engine.task.Task;
import com.zamron.engine.task.TaskManager;
import com.zamron.model.Animation;
import com.zamron.model.Item;
import com.zamron.model.Skill;
import com.zamron.model.definitions.ItemDefinition;
import com.zamron.model.input.impl.EnterAmountOfBowsToString;
import com.zamron.model.input.impl.EnterAmountToFletch;
import com.zamron.world.content.Achievements;
import com.zamron.world.content.Sounds;
import com.zamron.world.content.Achievements.AchievementData;
import com.zamron.world.content.Sounds.Sound;
import com.zamron.world.entity.impl.player.Player;

/**
 * Handles the Fletching skill
 * @author Gabriel Hannason
 *
 */
public class Fletching {

	/**
	 * Handles the Fletching interface
	 * @param player	The player Fletching
	 * @param log	The log to fletch
	 */
	public static void openSelection(final Player player, int log) {
		player.getSkillManager().stopSkilling();
		player.setSelectedSkillingItem(log);
		BowData shortBow = BowData.forLog(log, false);
		BowData longBow = BowData.forLog(log, true);
		if(shortBow == null || longBow == null)
			return;
		if(log == 1511) {
			player.getPacketSender().sendChatboxInterface(8880);
			player.getPacketSender().sendInterfaceModel(8884, longBow.getBowID(), 250);
			player.getPacketSender().sendInterfaceModel(8883, shortBow.getBowID(), 250);
			player.getPacketSender().sendString(8889, ""+ItemDefinition.forId(shortBow.getBowID()).getName()+"");
			player.getPacketSender().sendString(8893, ""+ItemDefinition.forId(longBow.getBowID()).getName()+"");
			player.getPacketSender().sendString(8897, "Shafts");
			player.getPacketSender().sendInterfaceModel(8885, 52, 250);
		} else {
			player.getPacketSender().sendChatboxInterface(8866);
			player.getPacketSender().sendInterfaceModel(8870, longBow.getBowID(), 250);
			player.getPacketSender().sendInterfaceModel(8869, shortBow.getBowID(), 250);
			player.getPacketSender().sendString(8874, ""+ItemDefinition.forId(shortBow.getBowID()).getName()+"");
			player.getPacketSender().sendString(8878, ""+ItemDefinition.forId(longBow.getBowID()).getName()+"");
		}
		player.setInputHandling(new EnterAmountToFletch());
	}

	/**
	 * Checks if a button that was clicked is from the Fletching interface
	 * @param player	The Player clicking a button
	 * @param clickId	The button the player clicked
	 * @return
	 */
	public static boolean fletchingButton(final Player player, int button) {
		switch (button) {
		case 8889:
			switch (player.getSelectedSkillingItem()) {
			case 1511: //Normal log
				fletchBow(player, 48, 1);
				return true;
			}
			return false;
		case 8888:
			switch (player.getSelectedSkillingItem()) {
			case 1511: //Normal log
				fletchBow(player, 48, 5);
				return true;
			}
			return false;
		case 8887:
			switch (player.getSelectedSkillingItem()) {
			case 1511: //Normal log
				fletchBow(player, 48, 10);
				return true;
			}
			return false;
		case 8893:
			switch (player.getSelectedSkillingItem()) {
			case 1511: //Normal log
				fletchBow(player, 50, 1);
				return true;
			}
			return false;
		case 8892:
			switch (player.getSelectedSkillingItem()) {
			case 1511: //Normal log
				fletchBow(player, 50, 5);
				return true;
			}
			return false;
		case 8891:
			switch (player.getSelectedSkillingItem()) {
			case 1511: //Normal log
				fletchBow(player, 50, 10);
				return true;
			}
			return false;
		case 8874:
			switch (player.getSelectedSkillingItem()) {
			case 1521: //Oak log
				fletchBow(player, 56, 1);
				return true;
			case 1519: //Willow log
				fletchBow(player, 58, 1);
				return true;
			case 1517: //Maple log
				fletchBow(player, 62, 1);
				return true;
			case 1515: //Yew log
				fletchBow(player, 66, 1);
				return true;
			case 1513: //Magic logs
				fletchBow(player, 70, 1);
				return true;
			}
			return false;
		case 8873:
			switch (player.getSelectedSkillingItem()) {
			case 1521: //Oak log
				fletchBow(player, 56, 5);
				return true;
			case 1519: //Willow log
				fletchBow(player, 58, 5);
				return true;
			case 1517: //Maple log
				fletchBow(player, 62, 5);
				return true;
			case 1515: //Yew log
				fletchBow(player, 66, 5);
				return true;
			case 1513: //Magic logs
				fletchBow(player, 70, 5);
				return true;
			}
			return false;
		case 8872:
			switch (player.getSelectedSkillingItem()) {
			case 1521: //Oak log
				fletchBow(player, 56, 10);
				return true;
			case 1519: //Willow log
				fletchBow(player, 58, 10);
				return true;
			case 1517: //Maple log
				fletchBow(player, 62, 10);
				return true;
			case 1515: //Yew log
				fletchBow(player, 66, 10);
				return true;
			case 1513: //Magic logs
				fletchBow(player, 70, 10);
				return true;
			}
			return false;
		case 8878:
			switch (player.getSelectedSkillingItem()) {
			case 1521: //Oak log
				fletchBow(player, 54, 1);
				return true;
			case 1519: //Willow log
				fletchBow(player, 60, 1);
				return true;
			case 1517: //Maple log
				fletchBow(player, 64, 1);
				return true;
			case 1515: //Yew log
				fletchBow(player, 68, 1);
				return true;
			case 1513: //Magic logs
				fletchBow(player, 72, 1);
				return true;
			}
			return false;
		case 8877:
			switch (player.getSelectedSkillingItem()) {
			case 1521: //Oak log
				fletchBow(player, 54, 5);
				return true;
			case 1519: //Willow log
				fletchBow(player, 60, 5);
				return true;
			case 1517: //Maple log
				fletchBow(player, 64, 5);
				return true;
			case 1515: //Yew log
				fletchBow(player, 68, 5);
				return true;
			case 1513: //Magic logs
				fletchBow(player, 72, 5);
				return true;
			}
			return false;
		case 8876:
			switch(player.getSelectedSkillingItem()) {
			case 1521: //Oak log
				fletchBow(player, 54, 10);
				return true;
			case 1519: //Willow log
				fletchBow(player, 60, 10);
				return true;
			case 1517: //Maple log
				fletchBow(player, 64, 10);
				return true;
			case 1515: //Yew log
				fletchBow(player, 68, 10);
				return true;
			case 1513: //Magic logs
				fletchBow(player, 72, 10);
				return true;
			}
			return false;
		case 8897: //Arrow shafts
		case 8896: //Arrow shafts
		case 8895: //Arrow shafts
			if(player.getSelectedSkillingItem() == 1511) {
				int amt = button == 8897 ? 1 : button == 8896 ? 5 : 10;
				fletchBow(player, 52, amt);
				return true;
			}
			return false;
		}
		return false;
	}

	public static void fletchBow(final Player player, final int product, final int amountToMake) {
		player.getPacketSender().sendInterfaceRemoval();
		final int log = player.getSelectedSkillingItem();
		player.getSkillManager().stopSkilling();
		player.setCurrentTask(new Task(2, player, true) {
			int amount = 0;
			@Override
			public void execute() {
				BowData bow = BowData.forBow(product);
				boolean shafts = product == 52;
				if(bow == null && !shafts || !player.getInventory().contains(log)) {
					player.performAnimation(new Animation(65535));
					stop();
					return;
				}
				if(bow != null && player.getSkillManager().getCurrentLevel(Skill.FLETCHING) < bow.getLevelReq()) {
					player.getPacketSender().sendMessage("You need a Fletching level of at least "+ bow.getLevelReq()+" to make this.");
					player.performAnimation(new Animation(65535));
					stop();
					return;
				}
				if(!player.getInventory().contains(946)) {
					player.getPacketSender().sendMessage("You need a Knife to fletch this log.");
					player.performAnimation(new Animation(65535));
					stop();
					return;
				}
				player.performAnimation(new Animation(1248));
				player.getInventory().delete(log, 1);
				player.getInventory().add(product, shafts ? 15 : 1);
				player.getSkillManager().addExperience(Skill.FLETCHING, shafts ? 80 : (int) (bow.getXp() * 2.5));
				Sounds.sendSound(player, Sound.FLETCH_ITEM);
				amount++;
				if(amount >= amountToMake)
					stop();
			}
		});
		TaskManager.submit(player.getCurrentTask());
	}

	/**
	 * Bow stringing
	 */
	private static int BOW_STRING = 1777;

	public static void openBowStringSelection(Player player, int log) {
		for (final StringingData g : StringingData.values()) {
			if (log == g.unStrung()) {
				player.getSkillManager().stopSkilling();
				player.setSelectedSkillingItem(log);
				player.setInputHandling(new EnterAmountOfBowsToString());
				player.getPacketSender().sendString(2799, ItemDefinition.forId(g.Strung()).getName()).sendInterfaceModel(1746, g.Strung(), 150).sendChatboxInterface(4429);
				player.getPacketSender().sendString(2800, "How many would you like to make?");
			}
		}
	}

	public static void stringBow(final Player player, final int amount) {
		final int log = player.getSelectedSkillingItem();
		player.getSkillManager().stopSkilling();
		player.getPacketSender().sendInterfaceRemoval();
		for (final StringingData g : StringingData.values()) {
			if (log == g.unStrung()) {
				if (player.getSkillManager().getCurrentLevel(Skill.FLETCHING) < g.getLevel()) {
					player.getPacketSender().sendMessage("You need a Fletching level of at least "+ g.getLevel()+" to make this.");					
					return;
				}
				if(!player.getInventory().contains(log) || !player.getInventory().contains(BOW_STRING)) 
					return;
				player.performAnimation(new Animation(g.getAnimation()));
				player.setCurrentTask(new Task(2, player, false) {
					int amountMade = 0;
					@Override
					public void execute() {
						if(!player.getInventory().contains(log) || !player.getInventory().contains(BOW_STRING)) 
							return;
						player.getInventory().delete(BOW_STRING, 1);
						player.getInventory().delete(log, 1);
						player.getInventory().add(g.Strung(), 1);
						player.getPacketSender().sendMessage("You attach the Bow string on to the bow.");
						player.getSkillManager().addExperience(Skill.FLETCHING, (int) g.getXP() * 2);
						amountMade++;
						if(amountMade >= amount)
							stop();
					}
				});
				TaskManager.submit(player.getCurrentTask());
				break;
			}
		}
	}

	/**
	 * Arrows making
	 */
	public static int getPrimary(int item1, int item2) {
		return item1 == 52 || item1 == 53 ? item2 : item1;
	}

	public static void makeArrows(final Player player, int item1, int item2) {
		player.getSkillManager().stopSkilling();
		ArrowData arr = ArrowData.forArrow(getPrimary(item1, item2));
		if (arr != null) {
			if (player.getSkillManager().getCurrentLevel(Skill.FLETCHING) >= arr.getLevelReq()) {
				if (player.getInventory().getAmount(arr.getItem1()) >= 15 && player.getInventory().getAmount(arr.getItem2()) >= 15) {
					player.getInventory().delete(new Item(arr.getItem1()).setAmount(15), player.getInventory().getSlot(arr.getItem1()), true); 
					player.getInventory().delete(new Item(arr.getItem2()).setAmount(15), player.getInventory().getSlot(arr.getItem2()), true);
					player.getInventory().add(arr.getOutcome(), 15);
					player.getSkillManager().addExperience(Skill.FLETCHING, (int) (arr.getXp() * 2.5));
					Achievements.finishAchievement(player, AchievementData.FLETCH_SOME_ARROWS);
					if(arr == ArrowData.RUNE) {
						Achievements.doProgress(player, AchievementData.FLETCH_450_RUNE_ARROWS, 15);
						Achievements.doProgress(player, AchievementData.FLETCH_5000_RUNE_ARROWS, 15);
					}
				} else {
					player.getPacketSender().sendMessage("You must have at least 15 of each supply to make arrows.");
				}
			} else {
				player.getPacketSender().sendMessage("You need a Fletching level of at least "+arr.getLevelReq()+" to fletch this.");
			}
		}
	}
}
