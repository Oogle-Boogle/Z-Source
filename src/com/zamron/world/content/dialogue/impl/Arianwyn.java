/*package com.Zamron.world.content.dialogue.impl;

import com.Zamron.util.Misc;
import com.Zamron.world.content.dialogue.Dialogue;
import com.Zamron.world.content.dialogue.DialogueExpression;
import com.Zamron.world.content.dialogue.DialogueType;
import com.Zamron.world.entity.impl.player.Player;

public class Arianwyn {

	public static Dialogue getDialogue(Player player, int stage) {
		Dialogue dialogue = null;
		switch (stage) {
		case 0:
			dialogue = new Dialogue() {

				@Override
				public DialogueType type() {
					return DialogueType.OPTION;
				}

				@Override
				public DialogueExpression animation() {
					return DialogueExpression.NORMAL;
				}

				@Override
				public int npcId() {
					return 2358;
				}

				@Override
				public String[] dialogue() {
					return new String[] { "Create raid party", "Join raid party", "Choose raid", "Leave raid party"};
				}
				
				@Override
				public void specialAction() {
					player.setDialogueActionId(2500);
				}
			};
			break;
		case 1: // selecting raid
			dialogue = new Dialogue() {

				@Override
				public DialogueType type() {
					return DialogueType.OPTION;
				}

				@Override
				public DialogueExpression animation() {
					return DialogueExpression.NORMAL;
				}

				@Override
				public int npcId() {
					return 2358;
				}

				@Override
				public String[] dialogue() {
					return new String[] { "Raid 1", "Raid 2", "Raid 3", "None", "None"};
				}
				
				@Override
				public void specialAction() {
					player.setDialogueActionId(2501);
				}
			};
			break;
		}
		return dialogue;
	}

	public static Dialogue foundDrop(String itemName, String npcName) {
		return new Dialogue() {

			@Override
			public DialogueType type() {
				return DialogueType.NPC_STATEMENT;
			}

			@Override
			public DialogueExpression animation() {
				return DialogueExpression.NORMAL;
			}

			@Override
			public int npcId() {
				return 7969;
			}

			@Override
			public String[] dialogue() {
				return new String[] { "Ah, yes! The " + Misc.formatText(itemName) + ".",
						"I believe the " + Misc.anOrA(npcName) + " " + npcName + "", "drops this item!",
						"Good luck in your adventure Simplician!" };
			}
		};
	}
}
*/