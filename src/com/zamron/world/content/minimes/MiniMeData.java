package com.zamron.world.content.minimes;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.zamron.GameServer;
import com.zamron.model.*;
import com.zamron.util.Misc;
import com.zamron.world.content.combat.magic.CombatSpells;
import com.zamron.world.content.combat.weapon.FightType;
import com.zamron.world.content.skill.SkillManager;
import com.zamron.world.content.skill.impl.slayer.SlayerMaster;
import com.zamron.world.content.skill.impl.slayer.SlayerTasks;
import com.zamron.world.entity.impl.player.Player;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;

public class MiniMeData {

        public static void save(Player player) {
            if (!player.isMiniMe)
                return;
            Player owner = player.getMinimeOwner();

            // Create the path and file objects.
            Path path = Paths.get("./data/saves/minimes/", player.getUsername() + ".json");
            File file = path.toFile();
            file.getParentFile().setWritable(true);

            // Attempt to make the player save directory if it doesn't
            // exist.
            if (!file.getParentFile().exists()) {
                try {
                    file.getParentFile().mkdirs();
                } catch (SecurityException e) {
                    //System.out.println("Unable to create directory for player data!");
                }
            }
            try (FileWriter writer = new FileWriter(file)) {

                Gson builder = new GsonBuilder().setPrettyPrinting().create();
                JsonObject object = new JsonObject();
                object.addProperty("total-play-time-ms", owner.getTotalPlayTime());

                object.add("position", builder.toJsonTree(player.getPosition()));
                object.addProperty("money-pouch", new Long(player.getMoneyInPouch()));

                object.addProperty("gender", player.getAppearance().getGender().name());
                object.addProperty("spell-book", player.getSpellbook().name());
                object.addProperty("prayer-book", player.getPrayerbook().name());
                object.addProperty("running", new Boolean(player.isRunning()));
                object.addProperty("run-energy", new Integer(owner.getRunEnergy()));
                object.addProperty("auto-retaliate", new Boolean(owner.isAutoRetaliate()));
                object.addProperty("xp-locked", new Boolean(owner.experienceLocked()));
                object.addProperty("veng-cast", new Boolean(player.hasVengeance()));
                object.addProperty("last-veng", new Long(player.getLastVengeance().elapsed()));
                object.addProperty("fight-type", player.getFightType().name());

                object.addProperty("accept-aid", new Boolean(owner.isAcceptAid()));
                object.addProperty("poison-damage", new Integer(player.getPoisonDamage()));
                object.addProperty("poison-immunity", new Integer(player.getPoisonImmunity()));
                object.addProperty("overload-timer", new Integer(player.getOverloadPotionTimer()));
                object.addProperty("fire-immunity", new Integer(player.getFireImmunity()));
                object.addProperty("fire-damage-mod", new Integer(player.getFireDamageModifier()));
                object.addProperty("prayer-renewal-timer", new Integer(player.getPrayerRenewalPotionTimer()));
                object.addProperty("teleblock-timer", new Integer(player.getTeleblockTimer()));
                object.addProperty("special-amount", new Integer(player.getSpecialPercentage()));

                object.addProperty("autocast", new Boolean(player.isAutocast()));
                object.addProperty("autocast-spell",
                        owner.getAutocastSpell() != null ? player.getAutocastSpell().spellId() : -1);
                object.add("appearance", builder.toJsonTree(player.getAppearance().getLook()));
                object.add("skills", builder.toJsonTree(player.getSkillManager().getSkills()));
                object.add("inventory", builder.toJsonTree(player.getInventory().getItems()));
                object.add("equipment", builder.toJsonTree(player.getEquipment().getItems()));
                writer.write(builder.toJson(object));
            } catch (Exception e) {
                // An error happened while saving.
                GameServer.getLogger().log(Level.WARNING, "An error has occured while saving a minime file!", e);
            }
        }

    public static void load(Player player) {
        if (!player.isMiniMe)
            return;

        Player owner = player.getMinimeOwner();

        // Create the path and file objects.
        Path path = Paths.get("./data/saves/minimes/", player.getUsername() + ".json");
        File file = path.toFile();

        // If the file doesn't exist, we're logging in for the first
        // time and can skip all of this.
        if (!file.exists()) {
            return;
        }

        // Now read the properties from the json parser.
        try (FileReader fileReader = new FileReader(file)) {
            JsonParser fileParser = new JsonParser();
            Gson builder = new GsonBuilder().create();
            JsonObject reader = (JsonObject) fileParser.parse(fileReader);

            if (reader.has("total-play-time-ms")) {
                player.setTotalPlayTime(reader.get("total-play-time-ms").getAsLong());
            }

            if (reader.has("staff-rights")) {
                player.setRights(PlayerRights.valueOf(reader.get("staff-rights").getAsString()));
            }

            if (reader.has("secondary-rights")) {
                player.setSecondaryPlayerRights(SecondaryPlayerRights.valueOf(reader.get("secondary-rights").getAsString()));
            }

            player.setGameMode(GameMode.valueOf(owner.getGameMode().toString()));


            if (reader.has("position")) {
                player.getPosition().setAs(builder.fromJson(reader.get("position"), Position.class));
            }


            if (reader.has("money-pouch")) {
                player.setMoneyInPouch(reader.get("money-pouch").getAsLong());
            }

            if (reader.has("gender")) {
                player.getAppearance().setGender(Gender.valueOf(reader.get("gender").getAsString()));
            }
            if (reader.has("spell-book")) {
                player.setSpellbook(MagicSpellbook.valueOf(reader.get("spell-book").getAsString()));
            }

            if (reader.has("prayer-book")) {
                player.setPrayerbook(Prayerbook.valueOf(reader.get("prayer-book").getAsString()));
            }
            if (reader.has("running")) {
                player.setRunning(reader.get("running").getAsBoolean());
            }
            if (reader.has("run-energy")) {
                player.setRunEnergy(reader.get("run-energy").getAsInt());
            }
            if (reader.has("auto-retaliate")) {
                player.setAutoRetaliate(reader.get("auto-retaliate").getAsBoolean());
            }
            if (reader.has("xp-locked")) {
                player.setExperienceLocked(reader.get("xp-locked").getAsBoolean());
            }
            if (reader.has("veng-cast")) {
                player.setHasVengeance(reader.get("veng-cast").getAsBoolean());
            }
            if (reader.has("last-veng")) {
                player.getLastVengeance().reset(reader.get("last-veng").getAsLong());
            }
            if (reader.has("fight-type")) {
                player.setFightType(FightType.valueOf(reader.get("fight-type").getAsString()));
            }
            if (reader.has("accept-aid")) {
                player.setAcceptAid(reader.get("accept-aid").getAsBoolean());
            }
            if (reader.has("poison-damage")) {
                player.setPoisonDamage(reader.get("poison-damage").getAsInt());
            }
            if (reader.has("poison-immunity")) {
                player.setPoisonImmunity(reader.get("poison-immunity").getAsInt());
            }
            if (reader.has("overload-timer")) {
                player.setOverloadPotionTimer(reader.get("overload-timer").getAsInt());
            }
            if (reader.has("fire-immunity")) {
                player.setFireImmunity(reader.get("fire-immunity").getAsInt());
            }
            if (reader.has("fire-damage-mod")) {
                player.setFireDamageModifier(reader.get("fire-damage-mod").getAsInt());
            }
            if (reader.has("overload-timer")) {
                player.setOverloadPotionTimer(reader.get("overload-timer").getAsInt());
            }
            if (reader.has("prayer-renewal-timer")) {
                player.setPrayerRenewalPotionTimer(reader.get("prayer-renewal-timer").getAsInt());
            }
            if (reader.has("teleblock-timer")) {
                player.setTeleblockTimer(reader.get("teleblock-timer").getAsInt());
            }
            if (reader.has("special-amount")) {
                player.setSpecialPercentage(reader.get("special-amount").getAsInt());
            }

            if (reader.has("autocast")) {
                player.setAutocast(reader.get("autocast").getAsBoolean());
            }
            if (reader.has("autocast-spell")) {
                int spell = reader.get("autocast-spell").getAsInt();
                if (spell != -1)
                    player.setAutocastSpell(CombatSpells.getSpell(spell));
            }

            player.getSlayer().setSlayerMaster(SlayerMaster.valueOf(owner.getSlayer().getSlayerMaster().name()));
            player.getSlayer().setSlayerTask(SlayerTasks.valueOf(owner.getSlayer().getSlayerTask().name()));
            player.getSlayer().setAmountToSlay(owner.getSlayer().getAmountToSlay());
            player.getSlayer().setTaskStreak(owner.getSlayer().getTaskStreak());

            if (reader.has("appearance")) {
                player.getAppearance().set(builder.fromJson(reader.get("appearance").getAsJsonArray(), int[].class));
            }

            if (reader.has("skills")) {
                player.getSkillManager().setSkills(builder.fromJson(reader.get("skills"), SkillManager.Skills.class));
            }
            if (reader.has("inventory")) {
                player.getInventory()
                        .setItems(builder.fromJson(reader.get("inventory").getAsJsonArray(), Item[].class));
            }
            if (reader.has("equipment")) {
                player.getEquipment()
                        .setItems(builder.fromJson(reader.get("equipment").getAsJsonArray(), Item[].class));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

        public static boolean miniMeExists(String p) {
            p = Misc.formatPlayerName(p.toLowerCase());
            return new File("./data/saves/minimes/" + p + ".json").exists();
        }
    }
