/*package com.platinum.world.content;

import com.platinum.model.Graphic;
import com.platinum.model.Item;
import com.platinum.model.Skill;
import com.platinum.model.definitions.ItemDefinition;
import com.platinum.util.Misc;
import com.platinum.world.content.Achievements.AchievementData;
import com.platinum.world.entity.impl.player.Player;


public class ItemForging {

	public static void forgeItem(final Player p, final int item1, final int item2) {
		if(item1 == item2)
			return;
		ItemForgeData data = ItemForgeData.getDataForItems(item1, item2);
		if(data == null || !p.getInventory().contains(item1) || !p.getInventory().contains(item2))
			return;
		if(!p.getClickDelay().elapsed(3000)) 
			return;
		if(p.getInterfaceId() > 0) {
			p.getPacketSender().sendMessage("Please close the interface you have open before doing this.");
			return;
		}
		Skill skill = Skill.forId(data.skillRequirement[0]);
		int skillReq = data.skillRequirement[1];
		if(p.getSkillManager().getCurrentLevel(skill) >= skillReq) {
			for(Item reqItem : data.requiredItems) {
				if(!p.getInventory().contains(reqItem.getId()) || p.getInventory().getAmount(reqItem.getId()) < reqItem.getAmount()) {
					p.getPacketSender().sendMessage("You need "+Misc.anOrA(reqItem.getDefinition().getName())+" "+reqItem.getDefinition().getName()+" to forge a new item.");
					return;
				}
			}
			p.performGraphic(new Graphic(2010));
			for(Item reqItem : data.requiredItems) {
				if(reqItem.getId() == 1755 || reqItem.getId() == 1595)
					continue;
				p.getInventory().delete(reqItem);
			}
			p.getInventory().add(data.product, true);
			final String itemName = Misc.formatText(ItemDefinition.forId(data.product.getId()).getName().toLowerCase());
			p.getPacketSender().sendMessage("You make "+Misc.anOrA(itemName)+" "+itemName+".");
			p.getClickDelay().reset();
			p.getSkillManager().addExperience(skill, data.skillRequirement[2]);
			//if(data == ItemForgeData.ARMADYL_GODSWORD || data == ItemForgeData.BANDOS_GODSWORD || data == ItemForgeData.ZAMORAK_GODSWORD || data == ItemForgeData.SARADOMIN_GODSWORD) {
				//Achievements.finishAchievement(p, AchievementData.ASSEMBLE_A_GODSWORD);
				//Achievements.doProgress(p, AchievementData.ASSEMBLE_5_GODSWORDS);
			}
			return;
		} else {
			p.getPacketSender().sendMessage("You need "+Misc.anOrA(skill.getFormatName())+" "+skill.getFormatName()+" level of at least "+skillReq+" to forge this item.");
			return;
		}
	}


	private static enum ItemForgeData {
		BLESSED_SPIRIT_SHIELD(new Item[] {new Item(13754), new Item(13734)}, new Item(13736), new int[] {1, -1, 0}),
		FORGER(new Item[] {new Item(9906, 1), new Item(11789, 5)}, new Item(16139), new int[] {1, -1, 0}),
		SPECTRAL_SPIRIT_SHIELD(new Item[] {new Item(13752), new Item(13736)}, new Item(13744), new int[] {13, 85, 40000}),
		ARCANE_SPIRIT_SHIELD(new Item[] {new Item(13746), new Item(13736)}, new Item(13738), new int[] {13, 85, 40000}),
		ELYSIAN_SPIRIT_SHIELD(new Item[] {new Item(13750), new Item(13736)}, new Item(13742), new int[] {13, 85, 40000}),
		DIVINE_SPIRIT_SHIELD(new Item[] {new Item(13748), new Item(13736)}, new Item(13740), new int[] {13, 85, 40000}),

		DRAGON_SQ_SHIELD(new Item[] {new Item(2368), new Item(2366)}, new Item(1187), new int[] {13, 60, 10000}),
		DRAGON_PLATEBY(new Item[] {new Item(14472), new Item(14474), new Item(14476)}, new Item(14479), new int[] {13, 92, 120000}),
		DRAGONFIRE_SHIELD(new Item[] {new Item(11286), new Item(1540)}, new Item(11283), new int[] {13, 82, 36000}),

		//CRYSTAL_KEY(new Item[] {new Item(985), new Item(987)}, new Item(989), new int[] {1, -1, 0}),
		

		GODSWORD_BLADE(new Item[] {new Item(11710), new Item(11712), new Item(11714)}, new Item(11690), new int[] {1, -1, 0}),
		ARMADYL_GODSWORD(new Item[] {new Item(11702), new Item(11690)}, new Item(11694), new int[] {1, -1, 0}),
		BANDOS_GODSWORD(new Item[] {new Item(11704), new Item(11690)}, new Item(11696), new int[] {1, -1, 0}),
		SARADOMIN_GODSWORD(new Item[] {new Item(11706), new Item(11690)}, new Item(11698), new int[] {1, -1, 0}),
		ZAMORAK_GODSWORD(new Item[] {new Item(11708), new Item(11690)}, new Item(11700), new int[] {1, -1, 0}),

		AMULET_OF_FURY(new Item[] {new Item(1755), new Item(6573), new Item(1595)}, new Item(6585), new int[] {12, 90, 100000}),


		AMULET_OF_FURY_ORNAMENT(new Item[] {new Item(19333), new Item(6585)}, new Item(19335), new int[] {1, -1, 0}),
		DRAGON_FULL_HELM_SPIKE(new Item[] {new Item(19354), new Item(11335)}, new Item(19341), new int[] {1, -1, 0}),
		DRAGON_PLATELEGS_SPIKE(new Item[] {new Item(19356), new Item(4087)}, new Item(19343), new int[] {1, -1, 0}),
		DRAGON_PLATEBODY_SPIKE(new Item[] {new Item(19358), new Item(14479)}, new Item(19342), new int[] {1, -1, 0}),
		DRAGON_SQUARE_SHIELD_SPIKE(new Item[] {new Item(19360), new Item(1187)}, new Item(19345), new int[] {1, -1, 0}),
		DRAGON_FULL_HELM_GOLD(new Item[] {new Item(19346), new Item(11335)}, new Item(19336), new int[] {1, -1, 0}),
		DRAGON_PLATELEGS_GOLD(new Item[] {new Item(19348), new Item(4087)}, new Item(19338), new int[] {1, -1, 0}),
		DRAGON_PLATEBODY_GOLD(new Item[] {new Item(19350), new Item(14479)}, new Item(19337), new int[] {1, -1, 0}),
		DRAGON_SQUARE_SHIELD_GOLD(new Item[] {new Item(19352), new Item(1187)}, new Item(19340), new int[] {1, -1, 0}),

		FULL_SLAYER_HELMET(new Item[] {new Item(13263), new Item(15490), new Item(15488)}, new Item(15492), new int[] {18, 75, 0}),
		
		ICYGLAIVE(new Item[] {new Item(12426),new Item(4670, 3),new Item(4671, 3),new Item(4672, 3),new Item(4673, 3),new Item(10835, 20000), new Item(933)}, new Item(5150), new int[] {1, -1, 0}),
		
		SABER(new Item[] {new Item(3276),new Item(10835, 50000), new Item(14639, 15000)}, new Item(3274), new int[] {1, -1, 0}),
		DUNGEONKEY(new Item[] {new Item(14469),new Item(14470)}, new Item(14471), new int[] {1, -1, 0}),
		DELUXEBONG(new Item[] {new Item(6191),new Item(17413, 5), new Item(9943, 5)}, new Item(6192), new int[] {1, -1, 0}),
		SWLAYER2(new Item[] {new Item(15105),new Item(15106),new Item(15104),new Item(15103), new Item(14639, 7500)}, new Item(18811), new int[] {1, -1, 0}),
		LEAFWING(new Item[] {new Item(3444), new Item(18392)}, new Item(18401), new int[] {1, -1, 0}),
		BLAZECAPE(new Item[] {new Item(18401),new Item(19011), new Item(18384)}, new Item(18402), new int[] {1, -1, 0}),
		INFERIORHELMET(new Item[] {new Item(19619),new Item(10835, 20000), new Item(569,2500)}, new Item(9500), new int[] {1, -1, 0}),
		INFERIORBODY(new Item[] {new Item(19470),new Item(10835, 20000), new Item(569,2500)}, new Item(9501), new int[] {1, -1, 0}),
		INFERIORLEGS(new Item[] {new Item(19471),new Item(10835, 20000), new Item(569,2500)}, new Item(9502), new int[] {1, -1, 0}),
		FATESNECK(new Item[] {new Item(19886),new Item(10835, 25000), new Item(19106)}, new Item(4780), new int[] {1, -1, 0}),
		
		DREAMERSCAPE(new Item[] {new Item(926), new Item(931),new Item(10835,75000), new Item(930)}, new Item(10902), new int[] {1, -1, 100000}),
		OWNERSCAPE(new Item[] {new Item(15566), new Item(10835,500000)}, new Item(10902), new int[] {1, -1, 100000}),
		FANTASYGLOVES(new Item[] {new Item(15026), new Item(15032),new Item(4670, 5),new Item(4671, 5),new Item(10835,50000)}, new Item(3918), new int[] {1, -1, 100000}),
		CHUCKYDOLL(new Item[] {new Item(5184), new Item(2547),new Item(2546),new Item(2545),new Item(10835,50000)}, new Item(15660), new int[] {1, -1, 100000}),
		OP_SHIELD(new Item[] {new Item(15660),new Item(10835,250000),new Item(13999,10), new Item(4279)}, new Item(6482), new int[] {1, -1, 100000}),
		HWEEN_SCYTHE(new Item[] {new Item(1413), new Item(1959,500)}, new Item(13094), new int[] {1, -1, 100000}),
		SKELETON_MASK(new Item[] {new Item(9925), new Item(1959,250)}, new Item(3874), new int[] {1, -1, 100000}),
		SKELETON_BODY(new Item[] {new Item(9924), new Item(1959,250)}, new Item(3875), new int[] {1, -1, 100000}),
		SKELETON_LEGS(new Item[] {new Item(10726), new Item(1959,250)}, new Item(3876), new int[] {1, -1, 100000}),
		
		DROPRATEBOOTS(new Item[] {new Item(19821),new Item(10835, 250000), new Item(13270)}, new Item(4803), new int[] {1, -1, 0}),
		
		
		DROPRATEBOOTSOP(new Item[] {new Item(4803),new Item(10835, 300000), new Item(13999,10)}, new Item(5052), new int[] {1, -1, 0}),
		
		
		
		SUPREME_HERBAL_BOW(new Item[] {new Item(19727),new Item(10835,200000), new Item(9492)}, new Item(13201), new int[] {1, -1, 100000}),
		
		RAINBOW_EEVEE(new Item[] {new Item(4743), new Item(4744),new Item(4786),new Item(4787),new Item(10835,150000)}, new Item(19753), new int[] {1, -1, 100000}),

		AURA(new Item[] {new Item(3277),new Item(10835,200000), new Item(13095)}, new Item(19156), new int[] {1, -1, 100000}),
		
		OPULANT_CAPE(new Item[] {new Item(14019),new Item(10835,500000), new Item(10902)}, new Item(3267), new int[] {1, -1, 100000}),
		IMBUED_SLAYER_HELMET(new Item[] {new Item(3949),new Item(10835,100000),new Item(3950),new Item(3952), new Item(934)}, new Item(15497), new int[] {1, -1, 100000}),
		OP_CAPE(new Item[] {new Item(3267),new Item(10835,750000),new Item(13999,10), new Item(15566)}, new Item(6482), new int[] {1, -1, 100000}),
		LUMINITA_HELM(new Item[] {new Item(18784),new Item(10835,50000),new Item(13999,10)}, new Item(19742), new int[] {1, -1, 100000}),
		LUMINITA_BODY(new Item[] {new Item(18815),new Item(10835,50000),new Item(13999,10)}, new Item(19744), new int[] {1, -1, 100000}),
		LUMINITA_LEGS(new Item[] {new Item(18785),new Item(10835,50000),new Item(13999,10)}, new Item(19743), new int[] {1, -1, 100000}),
		LUMINITA_CAPE(new Item[] {new Item(18754),new Item(10835,50000),new Item(13999,10)}, new Item(19741), new int[] {1, -1, 100000}),
		
		FUZED_GOKU(new Item[] {new Item(5127), new Item(5128),new Item(16581),new Item(16579),new Item(5163),new Item(16580),new Item(10835,50000)}, new Item(5135), new int[] {1, -1, 100000}),

		
		
		
		JOLTEON(new Item[] {new Item(4742), new Item(10835,25000),new Item(4792,100)}, new Item(4743), new int[] {1, -1, 100000}),
		FLAREEON(new Item[] {new Item(4742), new Item(10835,25000),new Item(4790,100)}, new Item(4744), new int[] {1, -1, 100000}),
		VAPEREON(new Item[] {new Item(4742), new Item(10835,25000),new Item(4791,100)}, new Item(4786), new int[] {1, -1, 100000}),
		SYLVELON(new Item[] {new Item(4742), new Item(10835,25000),new Item(4789,100)}, new Item(4787), new int[] {1, -1, 100000}),
		
		
		ROYAL_HELMET(new Item[] {new Item(6449), new Item(19730),new Item(3810),new Item(4672,5),new Item(10835,50000)}, new Item(16558), new int[] {1, -1, 100000}),
		ROYAL_BODY(new Item[] {new Item(19732), new Item(3811),new Item(6450),new Item(4672,5),new Item(10835,50000)}, new Item(16551), new int[] {1, -1, 100000}),
		ROYAL_LEGS(new Item[] {new Item(19731), new Item(3812),new Item(6451),new Item(4672,5),new Item(10835,50000)}, new Item(16549), new int[] {1, -1, 100000}),
		ROYALG_LOVES(new Item[] {new Item(19729), new Item(3813),new Item(6452),new Item(4672,5),new Item(10835,50000)}, new Item(16556), new int[] {1, -1, 100000}),
		ROYAL_BOOTS(new Item[] {new Item(19728), new Item(3814),new Item(6480),new Item(4672,5),new Item(10835,50000)}, new Item(16553), new int[] {1, -1, 100000}),

		
		CONQORUR_HELMET(new Item[] {new Item(3937), new Item(5118),new Item(6927),new Item(4670,10),new Item(10835,50000)}, new Item(5042), new int[] {1, -1, 100000}),
		CONQORUR_BODY(new Item[] {new Item(3938), new Item(5119),new Item(6928),new Item(4670,10),new Item(10835,50000)}, new Item(5043), new int[] {1, -1, 100000}),
		CONQORUR_LEGS(new Item[] {new Item(3939), new Item(5120),new Item(6929),new Item(4670,10),new Item(10835,50000)}, new Item(5044), new int[] {1, -1, 100000}),
		
		
		DONATORAURA(new Item[] {new Item(4780),new Item(10835, 100000), new Item(15454)}, new Item(3277), new int[] {1, -1, 0}),
		;


		ItemForgeData(Item[] requiredItems, Item product, int[] skillRequirement) {
			this.requiredItems = requiredItems;
			this.product = product;
			this.skillRequirement = skillRequirement;
		}

		private Item[] requiredItems;
		private Item product;
		private int[] skillRequirement;

		public static ItemForgeData getDataForItems(int item1, int item2) {
			for(ItemForgeData shieldData : ItemForgeData.values()) {
				int found = 0;
				for(Item it : shieldData.requiredItems) {
					if(it.getId() == item1 || it.getId() == item2)
						found++;
				}
				if(found >= 2)
					return shieldData;
			}
			return null;
		}
	}
}*/
