package com.zamron.world.content.skill.impl.smithing;

import java.util.stream.IntStream;

import com.zamron.engine.task.Task;
import com.zamron.engine.task.TaskManager;
import com.zamron.model.Animation;
import com.zamron.model.Graphic;
import com.zamron.model.Item;
import com.zamron.model.Skill;
import com.zamron.model.definitions.ItemDefinition;
import com.zamron.world.World;
import com.zamron.world.content.PlayerPunishment;
import com.zamron.world.content.Sounds;
import com.zamron.world.content.Sounds.Sound;
import com.zamron.world.entity.impl.player.Player;

public class EquipmentMaking {
	
	public static void handleAnvil(Player player) {
		String bar = searchForBars(player);
		if(bar == null) {
			player.getPacketSender().sendMessage("You do not have any bars in your inventory to smith.");
			return;
		} else {
			switch(bar.toLowerCase()) {
			case "bronze bar":
				player.setSelectedSkillingItem(2349);
				SmithingData.showBronzeInterface(player);
				break;
			case "iron bar":
				player.setSelectedSkillingItem(2351);
				SmithingData.makeIronInterface(player);
				break;
			case "steel bar":
				player.setSelectedSkillingItem(2353);
				SmithingData.makeSteelInterface(player);
				break;
			case "mithril bar":
				player.setSelectedSkillingItem(2359);
				SmithingData.makeMithInterface(player);
				break;
			case "adamant bar":
				player.setSelectedSkillingItem(2361);
				SmithingData.makeAddyInterface(player);
				break;
			case "rune bar":
				player.setSelectedSkillingItem(2363);
				SmithingData.makeRuneInterface(player);
				break;
			}
		}
	}
	
	public static String searchForBars(Player player) {
		for(int bar : SmithingData.SMELT_BARS) {
			if(player.getInventory().contains(bar)) {
				return ItemDefinition.forId(bar).getName();
			}
		}
		return null;
	}
	
	public static void smithItem(final Player player, final Item bar, final Item itemToSmith, final int x) {
		int[] list = new int[] {1205, 1351, 1422, 1139, 9375, 1277, 4819, 1794, 819, 39, 1321, 1265, 1291, 9420, 1155, 864, 1173,
				1337, 1375, 1103, 1189, 3095, 1307, 1087, 1075, 1117, 1203, 15298, 1420, 7225, 1137, 9140, 1279, 4820, 820, 40, 1323, 1267,
				1293, 1153, 863, 1175, 9423, 1335, 1363, 1101, 4540, 1191, 3096, 1309, 1081, 1067, 1115, 1207, 1353, 1424, 1141, 9141, 1539,
				1281, 821, 41, 1325, 1269, 1295, 2370, 9425, 1157, 865, 1177, 1339, 1365, 1105, 1193, 3097, 1311, 1084, 1069, 1119, 1209,
				1355, 1428, 1143, 9142, 1285, 4822, 822, 42, 1329, 1273, 1299, 9427, 1159, 866, 1181, 1343, 9416, 1369, 1109, 1197, 3099,
				1315, 1085, 1071, 1121, 1211, 1357, 1430, 1145, 9143, 1287, 4823, 823, 43, 1331, 1271, 1301, 9429, 1161, 867, 1183, 1345, 1371,
				1111, 1199, 3100, 1317, 1091, 1073, 1123, 1213, 1359, 1432, 1147, 9144, 1289, 4824, 824, 44, 1333, 1275, 1303, 9431, 1163, 868,
				1185, 1347, 1373, 1113, 1201, 3101, 1319, 1093, 1079, 1127, 2, 1083, 1349};
		if(bar.getId() < 0)
			return;
		player.getSkillManager().stopSkilling();
		if(!player.getInventory().contains(2347)) {
			player.getPacketSender().sendMessage("You need a Hammer to smith items.");
			player.getPacketSender().sendInterfaceRemoval();
			return;
		}
		if(player.getInventory().getAmount(bar.getId()) < bar.getAmount() || x <= 0) {
			player.getPacketSender().sendMessage("You do not have enough bars to smith this item.");
			return;
		}
		if(SmithingData.getData(itemToSmith, "reqLvl") > player.getSkillManager().getCurrentLevel(Skill.SMITHING)) {
			player.getPacketSender().sendMessage("You need a Smithing level of at least "+SmithingData.getData(itemToSmith, "reqLvl")+" to make this item.");
			return;
		}
		player.getPacketSender().sendInterfaceRemoval();
		player.setCurrentTask(new Task(3, player, true) {
			int amountMade = 0;
			@Override
			public void execute() {
				if(player.getInventory().getAmount(bar.getId()) < bar.getAmount() || !player.getInventory().contains(2347) || amountMade >= x) {
					this.stop();
					return;
				}
				if(player.getInteractingObject() != null)
					if (IntStream.of(list).anyMatch(id -> id == itemToSmith.getId())) {
						if(ItemDefinition.forId(itemToSmith.getId()).getName().contains("platebody")) {
							player.getSkillManager().addExperience(Skill.SMITHING, (int) (SmithingData.getData(itemToSmith, "xp") * 2.5));
						}
						if(ItemDefinition.forId(itemToSmith.getId()).getName().contains("platelegs") || ItemDefinition.forId(itemToSmith.getId()).getName().contains("plateskirt")) {
							player.getSkillManager().addExperience(Skill.SMITHING, (int) (SmithingData.getData(itemToSmith, "xp") * 1.5));
						} else {
							player.getSkillManager().addExperience(Skill.SMITHING, (int) (SmithingData.getData(itemToSmith, "xp") * 1.5));
						}

					} else {
						player.getPacketSender().sendMessage("You have been caught using cheating software goodbye.");
						//PlayerPunishment.addBannedIP(player.getHostAddress());
						World.deregister(player);
						//World.getPlayerByName
						PlayerPunishment.ban(player.getUsername());

					}
					player.getInteractingObject().performGraphic(new Graphic(2123));
				player.performAnimation(new Animation(898));
				amountMade++;
				Sounds.sendSound(player, Sound.SMITH_ITEM);
				player.getInventory().delete(bar);
				player.getInventory().add(itemToSmith);
				player.getInventory().refreshItems();
				player.getSkillManager().addExperience(Skill.SMITHING, (int) (SmithingData.getData(itemToSmith, "xp") * 1.5));
			}
		});
		TaskManager.submit(player.getCurrentTask());
	}
					/************************* Potential Dupe Fix ***************************************/
	/*public static void smithItem(final Player player, final Item bar, final Item itemToSmith, final int x) {
		if(bar.getId() < 0)
			return;
		player.getSkillManager().stopSkilling();
		if(!player.getInventory().contains(2347)) {
			player.getPacketSender().sendMessage("You need a Hammer to smith items.");
			player.getPacketSender().sendInterfaceRemoval();
			return;
		}
		if(player.getInventory().getAmount(bar.getId()) < bar.getAmount() || x <= 0) {
			player.getPacketSender().sendMessage("You do not have enough bars to smith this item.");
			return;
		}
		if(SmithingData.getData(itemToSmith, "reqLvl") > player.getSkillManager().getCurrentLevel(Skill.SMITHING)) {
			player.getPacketSender().sendMessage("You need a Smithing level of at least "+SmithingData.getData(itemToSmith, "reqLvl")+" to make this item.");
			return;
		}
		player.getPacketSender().sendInterfaceRemoval();
		player.setCurrentTask(new Task(3, player, true) {
			int amountMade = 0;
			@Override
			public void execute() {
				if(player.getInventory().getAmount(bar.getId()) < bar.getAmount() || !player.getInventory().contains(2347) || amountMade >= x) {
					this.stop();
					return;
				}
				if(player.getInteractingObject() != null)
					player.getInteractingObject().performGraphic(new Graphic(2123));
				player.performAnimation(new Animation(898));
				amountMade++;
				Sounds.sendSound(player, Sound.SMITH_ITEM);
				player.getInventory().delete(bar);
				player.getInventory().add(itemToSmith);
				player.getInventory().refreshItems();
				player.getSkillManager().addExperience(Skill.SMITHING, (int) (SmithingData.getData(itemToSmith, "xp") * 1.5));
			}
		});
		TaskManager.submit(player.getCurrentTask());
	}
	
	private static final int[][] BRONZE_ITEMS = {
		{
			1205, 1277, 1321, 1291, 1307
		},
		{
			1351, 1422, 1337, 1375, 3095
		},
		{
			1103, 1075, 1087, 1117, -1
		},
		{
			1139, 1155, 1173, 1189, 4819
		},
		{
			819, 39, 864, -1, -1, 
		}
	};
	
	private static final int[][] IRON_ITEMS = {
			{
				1203, 1349, 1101, 1137, 9140
			},
			{
				1349, 1420, 1335, 1363, 3096 
			},
			{
				1101, 1067, 1081, 1115, 4540
			},
			{
				1137, 1153, 1175, 1191, 4820
			},
			{
				9140, 40, 863, -1, -1 //etc
			},
		};
	private static final int[][] STEEL_ITEMS = {
			{
				1207, 1281, 1325, 1295, 1311
			},
			{
				1353, 1424, 1339, 1365, 3097 
			},
			{
				1105, 1069, 1083, 1119, -1
			},
			{
				1141, 1157, 1177, 1193, 1539
			},
			{
				9141, 41, 865, 2, 2370 
			},
		};
	private static final int[][] MITHRIL_ITEMS = {
			{
				1209, 1285, 1329, 1299, 1315
			},
			{
				1355, 1428, 1343, 1369, 3099 
			},
			{
				1109, 1071, 1085, 1121, -1
			},
			{
				1143, 1159, 1181, 1197, 4822
			},
			{
				9142, 42, 866, -1, -1 
			},
		};
	private static final int[][] ADAMANT_ITEMS = {
			{
				1211, 1287, 1331, 1301, 1317
			},
			{
				1357, 1430, 1345, 1371, 3100 
			},
			{
				1111, 1073, 1091, 1123, -1
			},
			{
				1145, 1161, 1183, 1199, 4823
			},
			{
				9143, 43, 867, -1, -1 
			},
		};
	private static final int[][] RUNE_ITEMS = {
			{
				1213, 1289, 1333, 1303, 1319
			},
			{
				1359, 1432, 1347, 1373, 3101 
			},
			{
				1113, 1079, 1093, 1127, -1
			},
			{
				1147, 1163, 1185, 1201, 4824
			},
			{
				9144, 44, 868, -1, -1 
			},
		};
	
	public int slotInArray;
	
	public static boolean valid(final Player player, final int interfaceId, final int barId, final int itemId, final int slot) {
		int slotInArray = interfaceId - 1119;
		
		int[][] array = null;
		switch (barId) {
		case 2349:
			array = BRONZE_ITEMS;
			break;
		case 2351:
			array = IRON_ITEMS;
			break;
		case 2353:
			array = STEEL_ITEMS;
			break;
		case 2359:
			array = MITHRIL_ITEMS;
			break;
		case 2361:
			array = ADAMANT_ITEMS;
			break;
		case 2363:
			array = RUNE_ITEMS;
			break;
			}
		return false;
		}*/
}
