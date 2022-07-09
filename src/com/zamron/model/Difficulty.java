package com.zamron.model;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import lombok.AllArgsConstructor;
import lombok.Getter;


@AllArgsConstructor
public enum Difficulty {

    Default(0, 0, "", "", ""),
    Easy(3, 0, "<col=00e62b>", "@gre@x3", "@blu@300%"),
    Medium(1, 1, "<col=ad820a>", "@gre@x1", "@blu@100%"),
    Hard(0.5, 2, "<col=f76472>", "@red@÷2", "@red@50%"),
    Insane(0.25, 3, "<col=d81124>", "@red@÷4", "@red@25%"),
    Zezima(0.1, 5, "<col=ff031b>", "@red@÷10", "@red@10%");


    @Getter
    public double xpRate;
    @Getter
    public int drBoost;
    @Getter
    public String maxAlertColour;
    @Getter
    public String interfaceString;
    @Getter
    public String dialogueString;


    private static final ImmutableSet<Difficulty> LOW = Sets.immutableEnumSet(Default, Easy, Medium);
    private static final ImmutableSet<Difficulty> HIGH = Sets.immutableEnumSet(Hard, Insane, Zezima);



    public boolean lowDifficulty() {
        return LOW.contains(this);
    }

    public boolean highDifficulty() {
        return HIGH.contains(this);
    }


}
