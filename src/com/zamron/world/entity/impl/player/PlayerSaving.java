package com.zamron.world.entity.impl.player;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.ObjectOutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;

import com.zamron.GameServer;
import com.zamron.tools.Encryptor;
import com.zamron.util.Misc;
import com.zamron.world.content.ClueScrolls;
import com.zamron.world.content.skill.impl.construction.ConstructionSave;
import com.google.common.collect.Multiset.Entry;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import java.util.List;

public class PlayerSaving {

	public static void save(Player player) {
		if (player.newPlayer())
			return;
		if (player.isMiniMe)
			return;
		// Create the path and file objects.
		Path path = Paths.get("./data/saves/characters/", player.getUsername() + ".json");
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
			object.addProperty("total-play-time-ms", player.getTotalPlayTime());
			object.addProperty("username", player.getUsername().trim());
			object.addProperty("placeholders", player.isPlaceholders());
			object.addProperty("password", Encryptor.encrypt(player.getPassword().trim(), Encryptor.globalKey));
			object.addProperty("email", player.getEmailAddress() == null ? "null" : player.getEmailAddress().trim());
			object.addProperty("staff-rights", player.getRights().name());
			object.addProperty("secondary-rights", player.getSecondaryPlayerRights().name());
			object.addProperty("game-mode", player.getGameMode().name());
			object.addProperty("difficulty", player.getDifficulty().toString());
			object.addProperty("cleansing-timerDR", player.getCleansingTime());
			object.addProperty("last-known-daily-task-npc", player.getCurrentDailyNPC());
			object.addProperty("daily-NPC-task-kc", player.getCurrentDailyNPCKills());
			object.add("progression-task-progress",
					builder.toJsonTree(player.getProgressionManager().getProgressions()));
			object.addProperty("raid-points", player.getPointsHandler().getRaidPoints());
			object.addProperty("among-points", player.getPointsHandler().getAmongPoints());
			object.addProperty("boss-points", player.getBossPoints());
			object.addProperty("lms-points", player.getLmsPoints());
			object.addProperty("minions-kc", player.getMinionsKC());
			object.addProperty("olm-kc", player.getcustomOlmKC());
			object.addProperty("boss-kills", player.getTotalBossKills());
			object.add("starter-zone-map", builder.toJsonTree(player.starterZone.getKeyToKillCountMap()));

			object.addProperty("custom-boss-points", player.getCustomPoints());
			object.addProperty("platinum-points", player.getRuneUnityPoints());
			object.addProperty("loyalty-title", player.getLoyaltyTitle().name());
			object.addProperty("crashGameBalance", new Long(player.getCrashGameBalance()));
			object.add("position", builder.toJsonTree(player.getPosition()));
			object.addProperty("online-status", player.getRelations().getStatus().name());
			object.addProperty("given-starter", new Boolean(player.didReceiveStarter()));
			object.addProperty("money-pouch", new Long(player.getMoneyInPouch()));
			object.addProperty("donated", new Long(player.getAmountDonated()));
			object.addProperty("referral", new Boolean(player.hasReferral));
			object.addProperty("indragon", new Boolean(player.inDragon));
			object.addProperty("starterclaimed", new Boolean(player.starterClaimed));
			object.addProperty("bravek-difficulty", player.getBravekDifficulty());
			object.addProperty("saved-pin", player.getSavedPin());
			object.addProperty("has-pin", player.getHasPin());
			object.addProperty("saved-ip", player.getSavedIp());
			object.addProperty("has-used-boss-tier-tp", new Boolean(player.isUsedBossTeleport()));
			object.addProperty("should-give-boss-reward", new Boolean(player.isShouldGiveBossReward()));
			object.addProperty("currentBossWave", new Integer(player.getCurrentBossWave()));
			object.addProperty("hercules-kc", player.getHerculesKC());
			object.addProperty("lucario-kc", player.getLucarioKC());
			object.addProperty("hades-kc", player.getHadesKC());
			object.addProperty("charizard-kc", player.getCharizardKC());
			object.addProperty("charizard-kc", player.getDefendersKC());
			object.addProperty("godzilla-kc", player.getGodzillaKC());
			object.addProperty("demonolm-kc", player.getDemonolmKC());
			object.addProperty("cerb-kc", player.getCerbKC());
			object.addProperty("zeus-kc", player.getZeusKC());
			object.addProperty("infartico-kc", player.getInfarticoKC());
			object.addProperty("valor-kc", player.getValorKC());
			object.addProperty("hw-kc", player.getHwKC());
			object.addProperty("dzanth-kc", player.getDzanthKC());
			object.addProperty("kong-kc", player.getKongKC());
			object.addProperty("corp-kc", player.getCorpKC());
			object.addProperty("lucid-kc", player.getLucidKC());
			object.addProperty("hulk-kc", player.getHulkKC());
			object.addProperty("darkblue-kc", player.getDarkblueKC());
			object.addProperty("pyro-kc", player.getPyroKC());
			object.addProperty("wyrm-kc", player.getWyrmKC());
			object.addProperty("exoden-kc", player.getExodenKC());
			object.addProperty("trinity-kc", player.getTrinityKC());
			object.addProperty("cloud-kc", player.getCloudKC());
			object.addProperty("herbal-kc", player.getHerbalKC());
			object.addProperty("nox-kc", player.getNoxKC());
			object.addProperty("azazel-kc", player.getAzazelKC());
			object.addProperty("supreme-kc", player.getSupremeKC());
			object.addProperty("breaker-kc", player.getBreakerKC());
			object.addProperty("apollo-kc", player.getApolloKC());
			object.addProperty("minutes-bonus-exp", new Integer(player.getMinutesBonusExp()));
			object.addProperty("total-gained-exp", new Long(player.getSkillManager().getTotalGainedExp()));
			object.addProperty("prestige-points", new Integer(player.getPointsHandler().getPrestigePoints()));
			object.addProperty("achievement-points", new Integer(player.getPointsHandler().getAchievementPoints()));
			object.addProperty("dung-tokens", new Integer(player.getPointsHandler().getDungeoneeringTokens()));
			object.addProperty("custom-pest-control-points",
					new Integer(player.getPointsHandler().getCustompestcontrolpoints()));
			object.addProperty("loyalty-points", new Integer(player.getPointsHandler().getLoyaltyPoints()));
			object.addProperty("total-loyalty-points",
					new Double(player.getAchievementAttributes().getTotalLoyaltyPointsEarned()));
			object.addProperty("voting-points", new Integer(player.getPointsHandler().getVotingPoints()));
			object.addProperty("slayer-points", new Integer(player.getPointsHandler().getSlayerPoints()));
			object.addProperty("npc-kills", player.getNpcKills());
			
			
			object.add("presetItems", builder.toJsonTree(player.presetItems));
			object.add("presetGear", builder.toJsonTree(player.presetGear));
			object.addProperty("PrayerBook", new Integer(player.getPrayerBook()));
			object.addProperty("MageBook", new Integer(player.getMageBook()));

			object.add("presetItems2", builder.toJsonTree(player.presetItems2));
			object.add("presetGear2", builder.toJsonTree(player.presetGear2));
			object.addProperty("PrayerBook2", new Integer(player.getPrayerBook2()));
			object.addProperty("MageBook2", new Integer(player.getMageBook2()));

			object.add("presetItems3", builder.toJsonTree(player.presetItems3));
			object.add("presetGear3", builder.toJsonTree(player.presetGear3));
			object.addProperty("PrayerBook3", new Integer(player.getPrayerBook3()));
			object.addProperty("MageBook3", new Integer(player.getMageBook3()));

			object.add("presetItems4", builder.toJsonTree(player.presetItems4));
			object.add("presetGear4", builder.toJsonTree(player.presetGear4));
			object.addProperty("PrayerBook4", new Integer(player.getPrayerBook4()));
			object.addProperty("MageBook4", new Integer(player.getMageBook4()));

			object.add("presetItems5", builder.toJsonTree(player.presetItems5));
			object.add("presetGear5", builder.toJsonTree(player.presetGear5));
			object.addProperty("PrayerBook5", new Integer(player.getPrayerBook5()));
			object.addProperty("MageBook5", new Integer(player.getMageBook5()));
			object.addProperty("preset", new Boolean(Player.preset));
			object.addProperty("preset2", new Boolean(Player.preset2));
			object.addProperty("preset3", new Boolean(Player.preset3));
			object.addProperty("preset4", new Boolean(Player.preset4));
			object.addProperty("preset5", new Boolean(Player.preset5));

			object.addProperty("dungeon-points", new Integer(player.getDungeonPoints()));

			object.addProperty("pk-points", new Integer(player.getPointsHandler().getPkPoints()));
			object.addProperty("donation-points", new Integer(player.getPointsHandler().getDonationPoints()));
			object.addProperty("bravek-tasks-completed", player.getBravekTasksCompleted());
			object.addProperty("custom-well-donated", player.getCustomDonations());
			object.addProperty("box-points", new Integer(player.getPointsHandler().getboxPoints()));
			object.addProperty("minigamepoints-1", new Integer(player.getPointsHandler().getminiGamePoints1()));
			object.addProperty("minigamepoints-2", new Integer(player.getPointsHandler().getminiGamePoints2()));
			object.addProperty("minigamepoints-3", new Integer(player.getPointsHandler().getminiGamePoints3()));
			object.addProperty("minigamepoints-4", new Integer(player.getPointsHandler().getminiGamePoints4()));
			object.addProperty("minigamepoints-5", new Integer(player.getPointsHandler().getminiGamePoints5()));
			object.addProperty("skill-points", new Integer(player.getPointsHandler().getSkillPoints()));
			object.addProperty("trivia-points", new Integer(player.getPointsHandler().getTriviaPoints()));
			object.addProperty("cluescompleted", new Integer(ClueScrolls.getCluesCompleted()));

			object.addProperty("player-kills", new Integer(player.getPlayerKillingAttributes().getPlayerKills()));
			object.addProperty("player-killstreak",
					new Integer(player.getPlayerKillingAttributes().getPlayerKillStreak()));
			object.addProperty("player-deaths", new Integer(player.getPlayerKillingAttributes().getPlayerDeaths()));
			object.addProperty("target-percentage",
					new Integer(player.getPlayerKillingAttributes().getTargetPercentage()));
			object.addProperty("bh-rank", new Integer(player.getAppearance().getBountyHunterSkull()));
			object.addProperty("gender", player.getAppearance().getGender().name());
			object.addProperty("spell-book", player.getSpellbook().name());
			object.addProperty("shop-updated", new Boolean(player.isShopUpdated()));
			object.addProperty("shop-earnings", new Long(player.getPlayerOwnedShopManager().getEarnings()));
			object.addProperty("prayer-book", player.getPrayerbook().name());
			object.addProperty("running", new Boolean(player.isRunning()));
			object.addProperty("run-energy", new Integer(player.getRunEnergy()));
			object.addProperty("music", new Boolean(player.musicActive()));
			object.addProperty("sounds", new Boolean(player.soundsActive()));
			object.addProperty("auto-retaliate", new Boolean(player.isAutoRetaliate()));
			object.addProperty("xp-locked", new Boolean(player.experienceLocked()));
			object.addProperty("veng-cast", new Boolean(player.hasVengeance()));
			object.addProperty("last-veng", new Long(player.getLastVengeance().elapsed()));
			object.addProperty("fight-type", player.getFightType().name());
			//object.add("equipmentWings", builder.toJsonTree(player.getEquipmentWings().getItems()));
			object.addProperty("sol-effect", new Integer(player.getStaffOfLightEffect()));
			object.addProperty("skull-timer", new Integer(player.getSkullTimer()));
			object.addProperty("fuse-combination-timer", new Long(player.getFuseCombinationTimer()));
			object.addProperty("fuse-item-selected", new Integer(player.getFuseItemSelected()));
			object.addProperty("claimed-fuse-item", new Boolean(player.isClaimedFuseItem()));
			object.addProperty("fuse-in-progress", new Boolean(player.isFuseInProgress()));
			object.addProperty("accept-aid", new Boolean(player.isAcceptAid()));
			object.addProperty("poison-damage", new Integer(player.getPoisonDamage()));
			object.addProperty("poison-immunity", new Integer(player.getPoisonImmunity()));
			object.addProperty("overload-timer", new Integer(player.getOverloadPotionTimer()));
			object.addProperty("fire-immunity", new Integer(player.getFireImmunity()));
			object.addProperty("fire-damage-mod", new Integer(player.getFireDamageModifier()));
			object.addProperty("prayer-renewal-timer", new Integer(player.getPrayerRenewalPotionTimer()));
			object.addProperty("teleblock-timer", new Integer(player.getTeleblockTimer()));
			object.addProperty("special-amount", new Integer(player.getSpecialPercentage()));
			object.addProperty("entered-gwd-room",
					new Boolean(player.getMinigameAttributes().getGodwarsDungeonAttributes().hasEnteredRoom()));
			object.addProperty("gwd-altar-delay",
					new Long(player.getMinigameAttributes().getGodwarsDungeonAttributes().getAltarDelay()));
			object.add("gwd-killcount",
					builder.toJsonTree(player.getMinigameAttributes().getGodwarsDungeonAttributes().getKillcount()));
			object.addProperty("effigy", new Integer(player.getEffigy()));
			object.addProperty("summon-npc",
					new Integer(player.getSummoning().getFamiliar() != null
							? player.getSummoning().getFamiliar().getSummonNpc().getId()
							: -1));
			object.addProperty("summon-death",
					new Integer(player.getSummoning().getFamiliar() != null
							? player.getSummoning().getFamiliar().getDeathTimer()
							: -1));
			object.addProperty("process-farming", new Boolean(player.shouldProcessFarming()));
			object.addProperty("clanchat", player.getClanChatName() == null ? "null" : player.getClanChatName().trim());
			object.addProperty("autocast", new Boolean(player.isAutocast()));
			object.addProperty("autocast-spell",
					player.getAutocastSpell() != null ? player.getAutocastSpell().spellId() : -1);
			object.addProperty("dfs-charges", player.getDfsCharges());
			object.addProperty("coins-gambled", new Integer(player.getAchievementAttributes().getCoinsGambled()));
			object.addProperty("slayer-master", player.getSlayer().getSlayerMaster().name());
			object.addProperty("slayer-task", player.getSlayer().getSlayerTask().name());
			object.addProperty("prev-slayer-task", player.getSlayer().getLastTask().name());
			object.addProperty("task-amount", player.getSlayer().getAmountToSlay());
			object.addProperty("task-streak", player.getSlayer().getTaskStreak());
			object.addProperty("duo-partner",
					player.getSlayer().getDuoPartner() == null ? "null" : player.getSlayer().getDuoPartner());
			object.addProperty("double-slay-xp", player.getSlayer().doubleSlayerXP);
			object.addProperty("recoil-deg", new Integer(player.getRecoilCharges()));
			object.add("brawler-deg", builder.toJsonTree(player.getBrawlerChargers()));
			object.add("void-top-deg", builder.toJsonTree(player.getVoidTopChargers()));
			object.add("void-leg-deg", builder.toJsonTree(player.getVoidLegChargers()));
			object.add("killed-players", builder.toJsonTree(player.getPlayerKillingAttributes().getKilledPlayers()));
			object.add("killed-gods", builder.toJsonTree(player.getAchievementAttributes().getGodsKilled()));
			object.add("blocked-collectors-items", builder.toJsonTree(player.getBlockedCollectorsList()));
			object.addProperty("cleansing-time", player.getCleansingTime());
			object.addProperty("amount-donated-today", player.getAmountDonatedToday());
			object.addProperty("claimed-first", player.claimedFirst);
			object.addProperty("claimed-second", player.claimedSecond);
			object.addProperty("claimed-third", player.claimedThird);
			object.addProperty("last-donation", player.lastDonation);
			object.addProperty("last-time-reset", player.lastTimeReset);
			object.addProperty("praise-time", player.getPraiseTime());
			object.add("barrows-brother",
					builder.toJsonTree(player.getMinigameAttributes().getBarrowsMinigameAttributes().getBarrowsData()));
			object.addProperty("random-coffin",
					new Integer(player.getMinigameAttributes().getBarrowsMinigameAttributes().getRandomCoffin()));
			object.addProperty("barrows-killcount",
					new Integer(player.getMinigameAttributes().getBarrowsMinigameAttributes().getKillcount()));
			object.add("nomad",
					builder.toJsonTree(player.getMinigameAttributes().getNomadAttributes().getQuestParts()));
			object.add("recipe-for-disaster", builder
					.toJsonTree(player.getMinigameAttributes().getRecipeForDisasterAttributes().getQuestParts()));
			object.addProperty("recipe-for-disaster-wave",
					new Integer(player.getMinigameAttributes().getRecipeForDisasterAttributes().getWavesCompleted()));
			object.add("dung-items-bound",
					builder.toJsonTree(player.getMinigameAttributes().getDungeoneeringAttributes().getBoundItems()));

			object.add("collection-data", builder.toJsonTree(player.getCollectionLogData()));
			object.addProperty("rune-ess", new Integer(player.getStoredRuneEssence()));
			object.addProperty("pure-ess", new Integer(player.getStoredPureEssence()));
			object.addProperty("has-bank-pin", new Boolean(player.getBankPinAttributes().hasBankPin()));
			object.addProperty("last-pin-attempt", new Long(player.getBankPinAttributes().getLastAttempt()));
			object.addProperty("invalid-pin-attempts", new Integer(player.getBankPinAttributes().getInvalidAttempts()));
			object.add("bank-pin", builder.toJsonTree(player.getBankPinAttributes().getBankPin()));
			object.add("appearance", builder.toJsonTree(player.getAppearance().getLook()));
			object.add("agility-obj", builder.toJsonTree(player.getCrossedObstacles()));
			object.add("skills", builder.toJsonTree(player.getSkillManager().getSkills()));
			object.add("inventory", builder.toJsonTree(player.getInventory().getItems()));
			object.add("equipment", builder.toJsonTree(player.getEquipment().getItems()));
			object.add("bank-0", builder.toJsonTree(player.getBank(0).getValidItems()));
			object.add("bank-1", builder.toJsonTree(player.getBank(1).getValidItems()));
			object.add("bank-2", builder.toJsonTree(player.getBank(2).getValidItems()));
			object.add("bank-3", builder.toJsonTree(player.getBank(3).getValidItems()));
			object.add("bank-4", builder.toJsonTree(player.getBank(4).getValidItems()));
			object.add("bank-5", builder.toJsonTree(player.getBank(5).getValidItems()));
			object.add("bank-6", builder.toJsonTree(player.getBank(6).getValidItems()));
			object.add("bank-7", builder.toJsonTree(player.getBank(7).getValidItems()));
			object.add("bank-8", builder.toJsonTree(player.getBank(8).getValidItems()));

			object.add("ge-slots", builder.toJsonTree(player.getGrandExchangeSlots()));

			/** STORE SUMMON **/
			if (player.getSummoning().getBeastOfBurden() != null) {
				object.add("store", builder.toJsonTree(player.getSummoning().getBeastOfBurden().getValidItems()));
			}
			object.add("charm-imp", builder.toJsonTree(player.getSummoning().getCharmImpConfigs()));

			for (Entry<Integer> dartItem : player.getBlowpipeLoading().getContents().entrySet()) {
				object.addProperty("blowpipe-charge-item", new Integer(dartItem.getElement()));
				object.addProperty("blowpipe-charge-amount",
						new Integer(player.getBlowpipeLoading().getContents().count(dartItem.getElement())));
			}

			object.add("friends", builder.toJsonTree(player.getRelations().getFriendList().toArray()));
			object.add("ignores", builder.toJsonTree(player.getRelations().getIgnoreList().toArray()));
			object.add("loyalty-titles", builder.toJsonTree(player.getUnlockedLoyaltyTitles()));
			object.add("kills", builder.toJsonTree(player.getKillsTracker().toArray()));
			object.add("drops", builder.toJsonTree(player.getDropLog().toArray()));
			object.add("achievements-completion",
					builder.toJsonTree(player.getAchievementAttributes().getCompletion()));
			object.add("achievements-progress", builder.toJsonTree(player.getAchievementAttributes().getProgress()));
			object.add("tasks-completion", builder.toJsonTree(player.getStarterTaskAttributes().getCompletion()));
			object.add("npc-task-completion", builder.toJsonTree(player.getNpcTaskAttributes().getCompletion()));
			object.add("npc-tasks-progress", builder.toJsonTree(player.getNpcTaskAttributes().getProgress()));
			object.add("tasks-progress", builder.toJsonTree(player.getStarterTaskAttributes().getProgress()));
			object.add("max-cape-colors", builder.toJsonTree(player.getMaxCapeColors()));
			// object.add("santa-colors", builder.toJsonTree(player.getSantaColors()));
			object.add("comp-cape-colors", builder.toJsonTree(player.getCompCapeColors()));
			object.addProperty("player-title", new String(player.getTitle()));
			object.addProperty("player-custom-yell",
					new String(player.getRights().customYellTitle == null ? "" : player.getRights().customYellTitle));
			object.addProperty("lastDonationClaim", new Long(player.lastDonationClaim));
			object.addProperty("lastOpPotion", new Long(player.lastOpPotion));
			object.addProperty("lastHpRestore", new Long(player.lastHpRestore));
			object.addProperty("lastPrayerRestore", new Long(player.lastPrayerRestore));
			object.addProperty("lastCashClaim", new Long(player.lastCashClaim));
			object.addProperty("lastSpecialClaim", new Long(player.lastSpecialClaim));
			object.addProperty("lastSpecialPotClaim", new Long(player.lastSpecialRestoreClaim));

			object.addProperty("roulette-balance", new Long(player.getRouletteBalance()));
			object.addProperty("roulette-bet-amount", new Long(player.getRouletteBet()));

			object.add("npc-kc-data", builder.toJsonTree(player.getNpcKillCount()));

			object.addProperty("savedKeyCount1", new Integer(player.savedKeyCount1));
			object.addProperty("savedKeyCount2", new Integer(player.savedKeyCount2));
			object.addProperty("savedKeyCount3", new Integer(player.savedKeyCount3));

			object.add("starterprogressioncompletions", builder.toJsonTree(player.getStarterProgressionCompletions()));
			
            object.addProperty("group-owner-name", player.getGroupOwnerName());
            object.addProperty("is-gim",player.isGim());
			List<String> teleNames = player.getTeleportInterface().getFavourites();
			if (teleNames != null) {
				object.add("favourite-teles", builder.toJsonTree(teleNames));
			}
			object.addProperty("knockedHweenDoor", player.isKnockedDoor());

			writer.write(builder.toJson(object));
			writer.close();
		} catch (Exception e) {
			// An error happened while saving.
			GameServer.getLogger().log(Level.WARNING, "An error has occured while saving a character file!", e);
		}
		try {

			File file2 = new File("./data/saves/construction/" + player.getUsername() + ".obj");

			if (!file2.exists()) {
				file2.createNewFile();
			}

			FileOutputStream fileOut = new FileOutputStream(file2);
			ConstructionSave save = new ConstructionSave();
			save.supply(player);
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(save);
			out.close();
			fileOut.close();

		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

	public static boolean playerExists(String p) {
		p = Misc.formatPlayerName(p.toLowerCase());
		return new File("./data/saves/characters/" + p + ".json").exists();
	}
}
