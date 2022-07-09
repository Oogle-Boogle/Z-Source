package com.zamron.world.content.fuser;

import com.zamron.model.definitions.ItemDefinition;
import com.zamron.world.entity.impl.player.Player;

public class CombineHandler {


    /** User to display time on the interface **/

    public static String timeLeft(Player player) {
        long durationInMillis = player.getFuseCombinationTimer() - System.currentTimeMillis();
        long minute = (durationInMillis / (1000 * 60)) % 60;
        long hour = (durationInMillis / (1000 * 60 * 60)) % 24;

        if (durationInMillis <= 0 && player.isClaimedFuseItem() && !player.isFuseInProgress()) {
            return "@gre@Start!";
        } else if (durationInMillis <= 0 && !player.isClaimedFuseItem()) {
            return "@gre@Claim!";
        } else {
            return String.format("@red@%02d:%02d", hour, minute);
        }
    }


    public static void openInterface(CombineEnum combine, Player player){
        player.combineIndex = combine.ordinal();
        player.combiner = 0;
        resetInterface(player);
        fillList(player);
        fillInterface(combine,player);
        player.getPacketSender().sendInterface(43500);
    }

    public static void resetInterface(Player player){

        int interfaceId = 43513;
        for(int i = 0; i < 20; i++) {
            player.getPacketSender().sendItemOnInterface(interfaceId,0,0);
            interfaceId++;
        }
        player.getPacketSender().sendItemOnInterface(43537,0,0);

         interfaceId = 43502;
        for(int i = 0; i < 10; i++) {
            player.getPacketSender().sendString(interfaceId,"");
            interfaceId++;
        }
        player.getPacketSender().sendString(43542,"");
        player.getPacketSender().sendString(43541, CombineHandler.timeLeft(player));

    }

    public static void fillList(Player player){
        int interfaceId = 53301;
        for(CombineEnum combine : CombineEnum.values()) {
            player.getPacketSender().sendString(interfaceId,""+ItemDefinition.forId(combine.getEndItem()).getName());
            interfaceId++;
        }
    }

    public static void fillInterface(CombineEnum combineEnum, Player player){

        int interfaceId = 43513;
        for(int i = 0; i < combineEnum.requirements.length; i++) {
            player.getPacketSender().sendItemOnInterface(interfaceId,combineEnum.requirements[i].getId(),combineEnum.requirements[i].getAmount());
            interfaceId++;
        }

       // player.getPacketSender().sendString(43542,"Fusion success rate: @mag@"+(combineEnum.getChance()
        	//s	+player.getRights().getLuckChance())+"%");

        player.getPacketSender().sendItemOnInterface(43537,combineEnum.getEndItem(),1);

    }
}
