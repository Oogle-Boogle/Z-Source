package com.zamron.world.content.fuser;

import com.zamron.model.Item;
import com.zamron.model.definitions.ItemDefinition;
import com.zamron.world.World;
import com.zamron.world.entity.impl.player.Player;
import lombok.Getter;
import lombok.Setter;

public enum CombineEnum {
    /**
     * @Author Oogle
     * Make sure to search Update this every time VVVVVV and update in ButtonClickPacketListener.java
     * :)
     */

    AURA(new Item[]{new Item(10835, 850000), new Item(12845, 500),
            new Item(12846, 500), new Item(12847, 500), new Item(19886, 1), new Item(15454, 1)}, 19156, 100, 3600000), //3600000

    DROPRATE_AURA(new Item[]{new Item(10835, 200000), new Item(12845, 25),
            new Item(12846, 25), new Item(12847, 25), new Item(19886, 2)}, 15454, 100, 3600000),

    PERMENANT_DR(new Item[]{new Item(10835, 100000), new Item(12845, 1),
            new Item(12846, 1), new Item(12847, 1), new Item(18401, 2)}, 5197, 100, 3600000),

    COLLECTOR(new Item[]{new Item(10835, 100000), new Item(12845, 10),
            new Item(12846, 10), new Item(12847, 10)}, 19886, 100, 3600000),

    NEWAURA(new Item[]{new Item(10835, 350000), new Item(3310, 1),
            new Item(3308, 1), new Item(3307, 1)}, 3309, 100, 3600000),

    SULPHUR_HELMET(new Item[]{new Item(10835, 1000000), new Item(12845, 300),
            new Item(12846, 300), new Item(12847, 300), new Item(19619, 1), new Item(13992, 1)}, 3322, 100, 3600000),

    SULPHUR_PLATEBODY(new Item[]{new Item(10835, 1000000), new Item(12845, 300),
            new Item(12846, 300), new Item(12847, 300), new Item(19470, 1), new Item(13994, 1)}, 3313, 100, 3600000),

    SULPHUR_PLATELEGS(new Item[]{new Item(10835, 1000000), new Item(12845, 300),
            new Item(12846, 300), new Item(12847, 300), new Item(19471, 1), new Item(13993, 1)}, 3314, 100, 3600000),

    SULPHUR_GLOVES(new Item[]{new Item(10835, 1000000), new Item(12845, 300),
            new Item(12846, 300), new Item(12847, 300), new Item(19472, 1), new Item(14447,1)}, 3318, 100, 3600000),

    SULPHUR_BOOTS(new Item[]{new Item(10835, 1000000), new Item(12845, 300),
            new Item(12846, 300), new Item(12847, 300), new Item(14448,1)}, 3315, 100, 3600000),

    SULPHUR_SCYTHE(new Item[]{new Item(10835, 1000000), new Item(12845, 250),
            new Item(12846, 250), new Item(12847, 250), new Item(13995,1), new Item(19154, 1), new Item(4796, 1), new Item(20700, 1)}
            , 1413, 100, 3600000),

    DEMI_STAFF(new Item[]{new Item(10835, 850000), new Item(12845, 250),
            new Item(12846, 250), new Item(12847, 250), new Item(8656, 1), new Item(8664, 1)},
            3920, 100, 3600000),

    SKATEBOARD(new Item[]{new Item(10835, 25000), new Item(12845, 1),
            new Item(12846, 1), new Item(12847, 1)}, 3063, 100, 3600000);


    CombineEnum(Item[] requirements, int endItem, int chance, long timer) {
        this.requirements = requirements;
        this.endItem = endItem;
        this.chance = chance;
        this.timer = timer;
    }

    Item[] requirements;
    int endItem;
    int chance;
    @Getter
    @Setter
    long timer; //TIME IS IN MILLISECONDS.. 1h = 3600000

    public int getChance() {
        return chance;
    }

    public void setChance(int chance) {
        this.chance = chance;
    }

    public Item[] getRequirements() {
        return requirements;
    }

    public int getEndItem() {
        return endItem;
    }
    public static boolean checkRequirements(CombineEnum combine, Player player) {
        Item[] reqs = combine.getRequirements();
        for (Item req : reqs) {
            if (player.getInventory().contains(req.getId()) && player.getInventory().getAmount(req.getId()) >= req.getAmount()) {
            } else {
                return false;

            }
        }
        return true;

    }
    public static void removeRequirements(CombineEnum combine, Player player){
        Item[] reqs = combine.getRequirements();
        for(Item req : reqs) {
            if(player.getInventory().contains(new Item[] {req})) {
                player.getInventory().delete(req.getId(),req.getAmount());
                player.sendMessage("@bla@ Removed "+req.getAmount()+"x "+ ItemDefinition.forId(req.getId()).getName() + " From your inventory!");
            }
        }
    }
}


/**    public static boolean checkRequirements(CombineEnum combine, Player player) {
        if (!(System.currentTimeMillis() >= player.getFuseCombinationTimer())) {
            player.sendMessage("You have @red@" + CombineHandler.timeLeft(player) + "@bla@ until you can claim this item.");
            player.getPA().closeAllWindows();
            return false;
        }
        //Checking if the player has the required items. Doesn't actually check the correct amount
        Item[] reqs = combine.getRequirements();
        if (player.getInventory().contains(reqs)) {
            System.out.println("True! Had all items");
        } else {
            System.out.println("FALSE! Had all items");
            return false;
        }
        return player.getInventory().contains(reqs);
    }

    public static void handlerFuser(Player player, CombineEnum chosenItem) {
        claimItem(player);

        if (player.isFuseInProgress() && player.getFuseCombinationTimer() > 0) {
            player.getPacketSender()
                    .sendMessage("@red@You have not finished fusing your @blu@"
                            + ItemDefinition.forId(player.getFuseItemSelected()).getName()
                            + "@red@ yet!");
            return;
        }

        if (!player.isClaimedFuseItem() && player.getFuseItemSelected() > 0) {
            player.getPacketSender()
                    .sendMessage("@red@You haven't claimed your @blu@"
                            + ItemDefinition.forId(player.getFuseItemSelected()).getName()
                            + "@red@ yet!");
            return;
        }
        if (checkRequirements(chosenItem, player)) {
            removeRequirements(chosenItem, player);
            player.setFuseCombinationTimer(System.currentTimeMillis() + (chosenItem.getTimer()));
            player.setClaimedFuseItem(false);
            player.setFuseInProgress(true);
            player.setFuseItemSelected(chosenItem.getEndItem());
            player.getPacketSender().sendString(43541, CombineHandler.timeLeft(player));
        } else
            player.sendMessage("You don't meet the requirements for this item!");
    }


    public static void removeRequirements(CombineEnum combine, Player player){
      Item[] reqs = combine.getRequirements();
      for(Item req : reqs) {
          if(player.getInventory().contains(new Item[] {req})) {
              player.getInventory().delete(req.getId(), req.getAmount());
              player.sendMessage("@bla@ Removed "+req.getAmount()+"x "+ ItemDefinition.forId(req.getId()).getName() + " from your inventory!");
          } else
              player.sendMessage("You don't meet the requirements for this item!");
          return;
      }
    }

    // OLD AND SHIT
    public static void claimItem(Player player) {
        if (System.currentTimeMillis() >= player.getFuseCombinationTimer()){
            player.setFuseInProgress(false);
        }
        if (!player.isClaimedFuseItem() && !player.isFuseInProgress() && player.getFuseItemSelected() > 0){ //If the player has an unclaimed item, and a fuse is not in progress
            if (player.getInventory().getFreeSlots() <= 1){
                player.getPacketSender().sendMessage("You need 1 free slot to claim your fused item!");
                return;
            }
            player.getInventory().addItem(player.getFuseItemSelected(), 1);
            World.sendMessageDiscord("[News] " + player.getUsername() + " has Fused a " + ItemDefinition.forId(player.getFuseItemSelected()).getName() + "!");
            player.setClaimedFuseItem(true);
            player.setFuseInProgress(false);
            player.setFuseItemSelected(0);
        }
    }
}**/
