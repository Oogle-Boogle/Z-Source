package com.zamron.world.content.partyroom;

import com.zamron.world.content.dialogue.Dialogue;
import com.zamron.world.content.dialogue.DialogueExpression;
import com.zamron.world.content.dialogue.DialogueType;

public class PartyRoomConfirmDepositDialogue extends Dialogue {

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
        return new String[] {
            "Yes, add the items.",
            "No, I want to keep my items."
        };
    }
}
