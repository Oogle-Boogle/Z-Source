package com.zamron.world.content.combat.magic;

import java.util.Optional;

import com.zamron.model.Animation;
import com.zamron.model.Graphic;
import com.zamron.model.GraphicHeight;
import com.zamron.model.Item;
import com.zamron.model.PlayerRights;
import com.zamron.model.Skill;
import com.zamron.model.Locations.Location;
import com.zamron.world.entity.impl.Character;
import com.zamron.world.entity.impl.player.Player;

/**
 * Holds data for all no-combat spells
 * @author Gabriel Hannason
 */
public enum MagicSpells {
	
	BONES_TO_BANANAS(new Spell() {

		@Override
		public int spellId() {
			return 1159;
		}

		@Override
		public int levelRequired() {
			return 15;
		}

		@Override
		public int baseExperience() {
			return 650;
		}

		@Override
		public Optional<Item[]> itemsRequired(Player player) {
			return Optional.of(new Item[] {new Item(561), new Item(555, 2), new Item(557, 2)});
		}

		@Override
		public Optional<Item[]> equipmentRequired(Player player) {
			return Optional.empty();
		}

		@Override
		public void startCast(Character cast, Character castOn) {
			
			
		}
		
	}),
	LOW_ALCHEMY(new Spell() {

		@Override
		public int spellId() {
			return 1162;
		}

		@Override
		public int levelRequired() {
			return 21;
		}

		@Override
		public int baseExperience() {
			return 4000;
		}

		@Override
		public Optional<Item[]> itemsRequired(Player player) {
			return Optional.of(new Item[] {new Item(554, 3), new Item(561)});
		}

		@Override
		public Optional<Item[]> equipmentRequired(Player player) {
			return Optional.empty();
		}
		
		@Override
		public void startCast(Character cast, Character castOn) {
			
			
		}
		
	}),
	TELEKINETIC_GRAB(new Spell() {

		@Override
		public int spellId() {
			return 1168;
		}

		@Override
		public int levelRequired() {
			return 33;
		}

		@Override
		public int baseExperience() {
			return 3988;
		}

		@Override
		public Optional<Item[]> itemsRequired(Player player) {
			return Optional.of(new Item[] {new Item(563), new Item(556)});
		}

		@Override
		public Optional<Item[]> equipmentRequired(Player player) {
			return Optional.empty();
		}
		
		@Override
		public void startCast(Character cast, Character castOn) {
			
			
		}
		
	}),
	SUPERHEAT_ITEM(new Spell() {

		@Override
		public int spellId() {
			return 1173;
		}

		@Override
		public int levelRequired() {
			return 43;
		}

		@Override
		public int baseExperience() {
			return 6544;
		}

		@Override
		public Optional<Item[]> itemsRequired(Player player) {
			return Optional.of(new Item[] {new Item(554, 4), new Item(561)});
		}

		@Override
		public Optional<Item[]> equipmentRequired(Player player) {
			return Optional.empty();
		}
		
		@Override
		public void startCast(Character cast, Character castOn) {
			
			
		}
		
	}),
	HIGH_ALCHEMY(new Spell() {

		@Override
		public int spellId() {
			return 1178;
		}

		@Override
		public int levelRequired() {
			return 55;
		}

		@Override
		public int baseExperience() {
			return 20000;
		}

		@Override
		public Optional<Item[]> itemsRequired(Player player) {
			return Optional.of(new Item[] {new Item(554, 5), new Item(561)});
		}

		@Override
		public Optional<Item[]> equipmentRequired(Player player) {
			return Optional.empty();
		}

		@Override
		public void startCast(Character cast, Character castOn) {
			
			
		}
		
	}),
	BONES_TO_PEACHES(new Spell() {

		@Override
		public int spellId() {
			return 15877;
		}

		@Override
		public int levelRequired() {
			return 60;
		}

		@Override
		public int baseExperience() {
			return 4121;
		}

		@Override
		public Optional<Item[]> itemsRequired(Player player) {
			return Optional.of(new Item[] {new Item(561, 2), new Item(555, 4), new Item(557, 4)});
		}

		@Override
		public Optional<Item[]> equipmentRequired(Player player) {
			return Optional.empty();
		}

		@Override
		public void startCast(Character cast, Character castOn) {
			
			
		}
		
	}),
	BAKE_PIE(new Spell() {

		@Override
		public int spellId() {
			return 30017;
		}

		@Override
		public int levelRequired() {
			return 65;
		}

		@Override
		public int baseExperience() {
			return 5121;
		}

		@Override
		public Optional<Item[]> itemsRequired(Player player) {
			return Optional.of(new Item[] {new Item(9075, 1), new Item(554, 5), new Item(555, 4)});
		}

		@Override
		public Optional<Item[]> equipmentRequired(Player player) {
			
			return Optional.empty();
		}

		@Override
		public void startCast(Character cast, Character castOn) {
			
			
		}
	}),
	VENGEANCE_OTHER(new Spell() {

		@Override
		public int spellId() {
			return 30298;
		}

		@Override
		public int levelRequired() {
			return 93;
		}

		@Override
		public int baseExperience() {
			return 10000;
		}

		@Override
		public Optional<Item[]> itemsRequired(Player player) {
			return Optional.of(new Item[] {new Item(9075, 3), new Item(557, 10), new Item(560, 2)});
		}

		@Override
		public Optional<Item[]> equipmentRequired(Player player) {
			return Optional.empty();
		}

		@Override
		public void startCast(Character cast, Character castOn) {
			
			
		}
	}),
	VENGEANCE(new Spell() {

		@Override
		public int spellId() {
			return 30306;
		}

		@Override
		public int levelRequired() {
			return 94;
		}

		@Override
		public int baseExperience() {
			return 14000;
		}

		@Override
		public Optional<Item[]> itemsRequired(Player player) {
			return Optional.of(new Item[]{new Item(9075, 4), new Item(557, 10), new Item(560, 2)});
		}

		@Override
		public Optional<Item[]> equipmentRequired(Player player) {
			return Optional.empty();
		}

		@Override
		public void startCast(Character cast, Character castOn) {
			
			
		}
	});
	
	MagicSpells(Spell spell) {
		this.spell = spell;
	}
	
	private Spell spell;
	
	public Spell getSpell() {
		return spell;
	}
	
	public static MagicSpells forSpellId(int spellId) {
		for(MagicSpells spells : MagicSpells.values()) {
			if(spells.getSpell().spellId() == spellId)
				return spells;
		}
		return null;
	}
	
	@SuppressWarnings("incomplete-switch")
	public static boolean handleMagicSpells(Player p, int buttonId) {
		MagicSpells spell = forSpellId(buttonId);
		if(!spell.getSpell().canCast(p, false))
			return true;
		switch(spell) {
		case BONES_TO_PEACHES:
		case BONES_TO_BANANAS:
			Item item = new Item(526);
			String sa = !item.getDefinition().getName().endsWith("s") ? "s" : "";
			if(!p.getInventory().contains(item.getId())) {
				p.getPacketSender().sendMessage("You do not have any "+item.getDefinition().getName()+""+sa+" in your inventory.");
				return true;
			}
			p.getInventory().delete(557, spell == BONES_TO_PEACHES ? 4 : 2).delete(555, spell == BONES_TO_PEACHES ? 4 : 2).delete(561, spell == BONES_TO_PEACHES ? 2 : 1);
			int i = 0;
			for(Item invItem : p.getInventory().getValidItems()) {
				if(invItem.getId() == item.getId()) {
					p.getInventory().delete(item.getId(), 1).add(spell == BONES_TO_PEACHES ? 6883 : 1963, 1);
					i++;
				}
			}
			p.performGraphic(new Graphic(141, GraphicHeight.MIDDLE));
			p.performAnimation(new Animation(722));
			p.getSkillManager().addExperience(Skill.MAGIC, spell.getSpell().baseExperience() * i);
			break;
		case VENGEANCE:
			if(!p.getLocation().isAidingAllowed() || p.getLocation() == Location.DUEL_ARENA) {
				p.getPacketSender().sendMessage("This spell cannot be cast here.");
				return true;
			}
			if(p.hasVengeance()) {
				p.getPacketSender().sendMessage("You already have Vengeance's effect.");
				return true;
			}
			if (p.getRights() == PlayerRights.PLAYER || p.getRights() == PlayerRights.DONATOR || p.getRights() == PlayerRights.SUPER_DONATOR || p.getRights() == PlayerRights.YOUTUBER) {
				if (!p.getLastVengeance().elapsed(30000)) {
					p.getPacketSender().sendMessage("You must wait 30 seconds until you can cast vengance!");
					return true;
				}
				
			}
			if (p.getRights() == PlayerRights.EXTREME_DONATOR || p.getRights() == PlayerRights.SUPPORT) {
				if (!p.getLastVengeance().elapsed(27500)) {
					p.getPacketSender().sendMessage("You must wait 27.5 seconds until you can cast vengance!");
					return true;
				}
				
			}
			if (p.getRights() == PlayerRights.LEGENDARY_DONATOR || p.getRights() == PlayerRights.MODERATOR) {
				if (!p.getLastVengeance().elapsed(25000)) {
					p.getPacketSender().sendMessage("You must wait 25 seconds until you can cast vengance!");
					return true;
				}
				
			}
			if (p.getRights().isHighDonator()) {
				if (!p.getLastVengeance().elapsed(20000)) {
					p.getPacketSender().sendMessage("You must wait 20 seconds until you can cast vengance!");
					return true;
				}
				
			}
			if (p.getRights() == PlayerRights.VIP_DONATOR) {
				if (!p.getLastVengeance().elapsed(10000)) {
					p.getPacketSender().sendMessage("You must wait 10 seconds until you can cast vengance!");
					return true;
				}
				
			}
			if (p.getRights() == PlayerRights.DEVELOPER) {
				if (!p.getLastVengeance().elapsed(10000)) {
					p.getPacketSender().sendMessage("You must wait 10 seconds until you can cast vengance!");
					return true;
				}
				
			}
		
			
                        p.getPacketSender().sendPlayerHeadOnInterface(9410);
                     
			p.getInventory().deleteItemSet(spell.getSpell().itemsRequired(p));
			p.performAnimation(new Animation(4410));
			p.performGraphic(new Graphic(726, GraphicHeight.HIGH));
			p.getLastVengeance().reset();
			p.setHasVengeance(true);
			break;
		}
		return true;
	}
}
