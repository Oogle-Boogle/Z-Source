package com.zamron.world.content.dialogue.impl;

import com.zamron.world.content.dialogue.Dialogue;
import com.zamron.world.content.dialogue.DialogueExpression;
import com.zamron.world.content.dialogue.DialogueType;
import com.zamron.world.entity.impl.player.Player;


public class RaidPartyInvitation extends Dialogue {

	public RaidPartyInvitation(Player inviter, Player p) {
		this.inviter = inviter;
		this.p = p;
	}
	
	private Player inviter, p;
	
	@Override
	public DialogueType type() {
		return DialogueType.STATEMENT;
	}

	@Override
	public DialogueExpression animation() {
		return null;
	}

	@Override
	public String[] dialogue() {
		return new String[] {
			""+inviter.getUsername()+" has invited you to join their Raid party."
		};
	}
	
	@Override
	public int npcId() {
		return -1;
	}
	
	@Override
	public Dialogue nextDialogue() {
		return new Dialogue() {

			@Override
			public DialogueType type() {
				return DialogueType.OPTION;
			}

			@Override
			public DialogueExpression animation() {
				return null;
			}

			@Override
			public String[] dialogue() {
				return new String[] {"Join "+inviter.getUsername()+"'s party", "Don't join "+inviter.getUsername()+"'s party."};
			}
			
			@Override
			public void specialAction() {
				p.addPendingRaidParty(inviter.getOldRaidParty());
				p.setDialogueActionId(2500);
			}
			
		};
	}
}