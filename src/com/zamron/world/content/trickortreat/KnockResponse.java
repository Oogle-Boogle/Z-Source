package com.zamron.world.content.trickortreat;

import com.zamron.world.content.dialogue.DialogueExpression;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum KnockResponse {
    ONE("Take the sweets and leave!", DialogueExpression.THINKING_STILL, true),
    TWO("Go away!", DialogueExpression.ANGRY, false),
    THREE("Ah lovely, trick or treaters!", DialogueExpression.CALM, true),
    FOUR("STUPID KIDS! LEAVE ME ALONE!", DialogueExpression.VERY_ANGRY, false),
    FIVE("The voices.. They won't stop...... The VOICESSS!!!", DialogueExpression.MANIC_FACE, false),
    SIX("God damn! More trick or treaters!", DialogueExpression.MIDLY_ANGRY, false),
    SEVEN("Hey! I love halloween.. here are some treats!", DialogueExpression.NORMAL, true);

    public String response;
    public DialogueExpression expression;
    public boolean treat;

}