package com.zamron.world.content.pet;

import com.zamron.model.Graphic;
import com.zamron.model.Item;
import com.zamron.model.Locations;
import com.zamron.model.Position;
import com.zamron.world.World;
//import com.platinum.world.content.pet.effect.Effect;
//import com.platinum.world.content.pet.effect.EffectType;
import com.zamron.world.entity.impl.npc.NPC;
import com.zamron.world.entity.impl.player.Player;



import java.util.ArrayList;
import java.util.List;


public class Pet {

    public static List<Pet> pets = new ArrayList<>();

    public Pet(String name, int itemId, int npcId, int npcZoom) {
        this.name = name;
        this.itemId = itemId;
        this.npcId = npcId;
        this.npcZoom = npcZoom;
      
    }
     protected String name;

    protected int npcZoom;

   
    protected int itemId;

    protected int npcId;






    public static void dropPet(Player player, Pet pet) {
        if(player.activePet != null) {
            player.sendMessage("You already have a pet out.");
            return;
        }

        player.getInventory().delete(pet.itemId, 1);
        player.activePet = new NPC(pet.npcId, new Position(player.getPosition().getX() - 1, player.getPosition().getY(), player.getPosition().getZ()));

        player.activePet.performGraphic(new Graphic(1315));
        player.activePet.setPositionToFace(player.getPosition());
        World.register(player.activePet);
        player.activePet.setEntityInteraction(player);
        player.activePet.getMovementQueue().setFollowCharacter(player);
        player.getPacketSender().sendSpriteChange(27661, 508);
        player.getPacketSender().sendItemOnInterface(27662, pet.itemId, 1);
       
    }
    public static void pickupPet(Player player) {
        World.deregister(player.activePet);
        player.addItemUnderAnyCircumstances(new Item(Pet.getPetFromNpc(player.activePet.getId()).itemId, 1));
        player.activePet = null;
        player.getPacketSender().sendItemOnInterface(27662, -1, -1);
        player.getPacketSender().sendSpriteChange(27661, 1260);
    }

    public static void movePet(Player player) {
        if(player.activePet != null) {
            player.activePet.moveTo(new Position(player.getPosition().getX() - 1, player.getPosition().getY(), player.getPosition().getZ()));
            player.activePet.performGraphic(new Graphic(1315));
            player.activePet.setLocation(Locations.Location.getLocation(player.activePet));
        }
    }

    public static Pet getPetFromItem(int item) {
        return pets.stream().filter(data -> data.itemId == item).findAny().orElse(null);
    }
    public static Pet getPetFromNpc(int npc) {
        return pets.stream().filter(data -> data.npcId == npc).findAny().orElse(null); //you havnt got my updates lol
    }

    @Override
    public String toString() {
        return "Name: " + name + ", itemId: " + itemId + ", npcId: " + npcId + " - " ;
    }
}
