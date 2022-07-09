package com.zamron.world.entity.impl.player;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.zamron.engine.task.impl.FamiliarSpawnTask;
import com.zamron.model.*;
import com.zamron.tools.Encryptor;
import com.zamron.world.content.collectionlog.CollectionEntry;
import com.zamron.model.PlayerRelations.PrivateChatStatus;
import com.zamron.model.container.impl.Bank;
import com.zamron.net.login.LoginResponses;
import com.zamron.world.content.ClueScrolls;
import com.zamron.world.content.DropLog;
import com.zamron.world.content.KillsTracker;
import com.zamron.world.content.DropLog.DropLogEntry;
import com.zamron.world.content.KillsTracker.KillsEntry;
import com.zamron.world.content.LoyaltyProgramme.LoyaltyTitles;
import com.zamron.world.content.combat.magic.CombatSpells;
import com.zamron.world.content.combat.weapon.FightType;
import com.zamron.world.content.ProgressionTask;
import com.zamron.world.content.grandexchange.*;
import com.zamron.world.content.skill.SkillManager.Skills;
import com.zamron.world.content.skill.impl.construction.ConstructionSave;
import com.zamron.world.content.skill.impl.slayer.SlayerMaster;
import com.zamron.world.content.skill.impl.slayer.SlayerTasks;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;




public class PlayerLoading {
	

	public static int getResult(Player player) {
		return getResult(player, false);
	}
	
	public static int getResult(Player player, boolean force) {

		// Create the path and file objects.
		Path path = Paths.get("./data/saves/characters/", player.getUsername() + ".json");
		File file = path.toFile();


		// If the file doesn't exist, we're logging in for the first
		// time and can skip all of this.
		if (!file.exists()) {
			return LoginResponses.NEW_ACCOUNT;
		}

		// Now read the properties from the json parser.
		try (FileReader fileReader = new FileReader(file)) {
			JsonParser fileParser = new JsonParser();
			Gson builder = new GsonBuilder().create();
			JsonObject reader = (JsonObject) fileParser.parse(fileReader);

			if (reader.has("total-play-time-ms")) {
				player.setTotalPlayTime(reader.get("total-play-time-ms").getAsLong());
			}

			if (reader.has("username")) {
				player.setUsername(reader.get("username").getAsString());
			}
			
		       if (reader.has("progression-task-progress")) {
	                player.getProgressionManager().loadData();
	                List<ProgressionTask> loadedProgressions = builder.fromJson(reader.get("progression-task-progress"),
	                        new TypeToken<List<ProgressionTask>>() {
	                        }.getType());
	                for (int i = 0; i < loadedProgressions.size(); i++) {
	                    player.getProgressionManager().setProgression(i, loadedProgressions.get(i));
	                }
	            }


			if (reader.has("password")) {
				String password = reader.get("password").getAsString();
				byte[] passBytes = password.getBytes();
				if (passBytes.length >= 16) { //This is included so that it can encrypt passwords that are not currently encrypted.
					password = Encryptor.decrypt(password, Encryptor.globalKey);
					//System.out.println("Decryption Success");
				}
				if(!force) {
					if (!player.getPassword().equals(password)) {
						return LoginResponses.LOGIN_INVALID_CREDENTIALS;
					}
				}
				player.setPassword(password);
	}
			if (reader.has("referral")) {
				player.hasReferral = reader.get("referral").getAsBoolean();
			}
			if (reader.has("placeholders")) {
				player.setPlaceholders(reader.get("placeholders").getAsBoolean());
			}
			if (reader.has("starterclaimed")) {
				player.starterClaimed = reader.get("starterclaimed").getAsBoolean();
			}
			
			if (reader.has("starter-zone-map")) {
				Map<Integer, Integer> data = builder.fromJson(reader.get("starter-zone-map"), new TypeToken<Map<Integer, Integer>>() {}.getType());
				player.starterZone.loadData(data);
			}
			if (reader.has("favourite-teles")) {
				player.getTeleportInterface().setFavourites(builder.fromJson(reader.get("favourite-teles").getAsJsonArray(), String[].class));
			}

			if (reader.has("knockedHweenDoor")){
				player.setKnockedDoor(reader.get("knockedHweenDoor").getAsBoolean());
			}
	
			if(reader.has("crashGameBalance"))
                player.addToCrashBalance(reader.get("crashGameBalance").getAsLong());
			
			if (reader.has("indragon")) {
				player.inDragon = reader.get("indragon").getAsBoolean();
			}
			if (reader.has("among-points")) {
				player.getPointsHandler().setAmongPoints(reader.get("among-points").getAsInt(), false);
			}
			
			if (reader.has("boss-points")) {
				player.setBossPoints(reader.get("boss-points").getAsInt());
			}
			if (reader.has("lms-points")) {
				player.setLmsPoints(reader.get("lms-points").getAsInt());
			}
			if (reader.has("minions-kc")) {
				player.setMinionsKC(reader.get("minions-kc").getAsInt());
			}
			if (reader.has("olm-kc")) {
				player.setCustomOlmKC(reader.get("olm-kc").getAsInt());
			}
			if(reader.has("boss-kills")) {
				player.setTotalBossKills(reader.get("boss-kills").getAsInt());
			}
			
			if (reader.has("raid-points")) {
				player.getPointsHandler().setRaidPoints(reader.get("raid-points").getAsLong(), false);
			}
			
			if (reader.has("bravek-difficulty")) {
				player.setBravekDifficulty(reader.get("bravek-difficulty").getAsString());
			}
			
			if (reader.has("has-pin")) {
				player.setHasPin(reader.get("has-pin").getAsBoolean());
			}
			
			if (reader.has("npc-kills")) {
				player.setNpcKills(reader.get("npc-kills").getAsInt());
			}

			if (reader.has("saved-ip")) {
				player.setSavedIp(reader.get("saved-ip").getAsString());
			}
			if (reader.has("has-used-boss-tier-tp")) {
				player.setUsedBossTeleport(reader.get("has-used-boss-tier-tp").getAsBoolean());
			}
			if (reader.has("should-give-boss-reward")) {
				player.setShouldGiveBossReward(reader.get("should-give-boss-reward").getAsBoolean());
			}
			if (reader.has("currentBossWave")) {
				player.setCurrentBossWave(reader.get("currentBossWave").getAsInt());
			}
			
            if (reader.has("group-owner-name")) {
                player.setGroupOwnerName(reader.get("group-owner-name").getAsString());
            }

            if (reader.has("is-gim")) {
                player.setGim(reader.get("is-gim").getAsBoolean());
            }
			
			if (reader.has("saved-pin")) {
				player.setSavedPin(reader.get("saved-pin").getAsString());
			}
			if (reader.has("custom-boss-points")) {
				player.setCustomPoints(reader.get("custom-boss-points").getAsInt());
			}
			if (reader.has("platinum-points")) {
				player.setRuneUnityPoints(reader.get("platinum-points").getAsInt());
			}
			if (reader.has("email")) {
				player.setEmailAddress(reader.get("email").getAsString());
			}

			if (reader.has("staff-rights")) {
				player.setRights(PlayerRights.valueOf(reader.get("staff-rights").getAsString()));
			}

			if (reader.has("secondary-rights")) {
				player.setSecondaryPlayerRights(SecondaryPlayerRights.valueOf(reader.get("secondary-rights").getAsString()));
			}

			if (reader.has("game-mode")) {
				player.setGameMode(GameMode.valueOf(reader.get("game-mode").getAsString()));
			}

			if (reader.has("difficulty")) {
				player.setDifficulty(Difficulty.valueOf(reader.get("difficulty").getAsString()));
			}

			if (reader.has("last-known-daily-task-npc")) {
				player.setCurrentDailyNPC(reader.get("last-known-daily-task-npc").getAsInt());
			}

			if (reader.has("daily-NPC-task-kc")) {
				player.setCurrentDailyNPCKills(reader.get("daily-NPC-task-kc").getAsInt());
			}

		    if (reader.has("collection-data")) {
                Type collectionLogType = new TypeToken<List<CollectionEntry>>() {
                }.getType();
                player.setCollectionLogData(new Gson().fromJson(reader.get("collection-data"), collectionLogType));
            }
			

			if (reader.has("loyalty-title")) {
				player.setLoyaltyTitle(LoyaltyTitles.valueOf(reader.get("loyalty-title").getAsString()));
			}

			if (reader.has("position")) {
				player.getPosition().setAs(builder.fromJson(reader.get("position"), Position.class));
			}

			if (reader.has("online-status")) {
				player.getRelations().setStatus(PrivateChatStatus.valueOf(reader.get("online-status").getAsString()),
						false);
			}

			if (reader.has("money-pouch")) {
				player.setMoneyInPouch(reader.get("money-pouch").getAsLong());
			}

			if (reader.has("given-starter")) {
				player.setReceivedStarter(reader.get("given-starter").getAsBoolean());
			}

			if (reader.has("donated")) {
				player.incrementAmountDonated(reader.get("donated").getAsInt());
			}
			
			if (reader.has("minutes-bonus-exp")) {
				player.setMinutesBonusExp(reader.get("minutes-bonus-exp").getAsInt(), false);
			}

			if (reader.has("total-gained-exp")) {
				player.getSkillManager().setTotalGainedExp(reader.get("total-gained-exp").getAsInt());
			}

		
			if (reader.has("dung-tokens")) {
				player.getPointsHandler().setDungeoneeringTokens(reader.get("dung-tokens").getAsInt(), false);
			}

			if (reader.has("prestige-points")) {
				player.getPointsHandler().setPrestigePoints(reader.get("prestige-points").getAsInt(), false);
			}

			if (reader.has("achievement-points")) {
				player.getPointsHandler().setAchievementPoints(reader.get("achievement-points").getAsInt(), false);
			}

			if (reader.has("custom-pest-control-points")) {
				player.getPointsHandler().setCustompestcontrolpoints(reader.get("custom-pest-control-points").getAsInt(), false);
			}

			if (reader.has("loyalty-points")) {
				player.getPointsHandler().setLoyaltyPoints(reader.get("loyalty-points").getAsInt(), false);
			}

			if (reader.has("total-loyalty-points")) {
				player.getAchievementAttributes()
						.incrementTotalLoyaltyPointsEarned(reader.get("total-loyalty-points").getAsDouble());
			}

			if (reader.has("voting-points")) {
				player.getPointsHandler().setVotingPoints(reader.get("voting-points").getAsInt(), false);
			}

			if (reader.has("slayer-points")) {
				player.getPointsHandler().setSlayerPoints(reader.get("slayer-points").getAsInt(), false);
			}
			if (reader.has("dungeon-points")) {
				player.setDungeonPoints((reader.get("dungeon-points").getAsInt()));
			}
			if (reader.has("pk-points")) {
				player.getPointsHandler().setPkPoints(reader.get("pk-points").getAsInt(), false);
			}
			if (reader.has("donation-points")) {
				player.getPointsHandler().setDonationPoints(reader.get("donation-points").getAsInt(), false);
			}
			
			if (reader.has("bravek-tasks-completed")) {
				player.setBravekTasksCompleted(reader.get("bravek-tasks-completed").getAsInt(), false);
			}
			
			if (reader.has("custom-well-donated")) {
				player.setCustomWellDonated(reader.get("custom-well-donated").getAsInt(), false);
			}
			
			if (reader.has("box-points")) {
				player.getPointsHandler().setboxPoints(reader.get("box-points").getAsInt(), false);
			}
			if (reader.has("minigamepoints-1")) {
				player.getPointsHandler().setMinigamePoints1(reader.get("minigamepoints-1").getAsInt(), false);
			}
			if (reader.has("minigamepoints-2")) {
				player.getPointsHandler().setMinigamePoints2(reader.get("minigamepoints-2").getAsInt(), false);
			}
			if (reader.has("minigamepoints-3")) {
				player.getPointsHandler().setminiGamePoints3(reader.get("minigamepoints-3").getAsInt(), false);
			}
			if (reader.has("minigamepoints-4")) {
				player.getPointsHandler().setminiGamePoints4(reader.get("minigamepoints-4").getAsInt(), false);
			}
			if (reader.has("minigamepoints-5")) {
				player.getPointsHandler().setminiGamePoints5(reader.get("minigamepoints-5").getAsInt(), false);
			}
			if (reader.has("skill-points")) {
				player.getPointsHandler().setSkillPoints(reader.get("skill-points").getAsInt(), false);
			}
			if (reader.has("trivia-points")) {
				player.getPointsHandler().setTriviaPoints(reader.get("trivia-points").getAsInt(), false);
			}

			if (reader.has("cluescomplted")) {
				ClueScrolls.setCluesCompleted(reader.get("cluescompleted").getAsInt(), false);
			}
			
			if (reader.has("player-kills")) {
				player.getPlayerKillingAttributes().setPlayerKills(reader.get("player-kills").getAsInt());
			}
			
			
			if (reader.has("hercules-kc")) {
				player.setHerculesKC(reader.get("hercules-kc").getAsInt());
			}
			
			if (reader.has("lucario-kc")) {
				player.setLucarioKC(reader.get("lucario-kc").getAsInt());
			}	
			
			if (reader.has("hades-kc")) {
				player.setHadesKC(reader.get("hades-kc").getAsInt());
			}
			
			if (reader.has("charizard-kc")) {
				player.setCharizardKC(reader.get("charizard-kc").getAsInt());
			}
			
			if (reader.has("defenders-kc")) {
				player.setDefendersKC(reader.get("defenders-kc").getAsInt());
			}
			
			if (reader.has("godzilla-kc")) {
				player.setGodzillaKC(reader.get("godzilla-kc").getAsInt());
			}
			
			if (reader.has("demonolm-kc")) {
				player.setDemonolmKC(reader.get("demonolm-kc").getAsInt());
			}
			if (reader.has("cerb-kc")) {
				player.setCerbKC(reader.get("cerb-kc").getAsInt());
			}
			
			if (reader.has("zeus-kc")) {
				player.setZeusKC(reader.get("zeus-kc").getAsInt());
			}
			
			if (reader.has("infartico-kc")) {
				player.setInfarticoKC(reader.get("infartico-kc").getAsInt());
			}
			if (reader.has("valor-kc")) {
				player.setValorKC(reader.get("valor-kc").getAsInt());
			}
			if (reader.has("hw-kc")) {
				player.setHwKC(reader.get("hw-kc").getAsInt());
			}
			if (reader.has("dzanth-kc")) {
				player.setDzanthKC(reader.get("dzanth-kc").getAsInt());
			}
			if (reader.has("kong-kc")) {
				player.setKongKC(reader.get("kong-kc").getAsInt());
			}
			if (reader.has("corp-kc")) {
				player.setCorpKC(reader.get("corp-kc").getAsInt());
			}
			
			if (reader.has("lucid-kc")) {
				player.setLucidKC(reader.get("lucid-kc").getAsInt());
			}
			
			if (reader.has("hulk-kc")) {
				player.setHulkKC(reader.get("hulk-kc").getAsInt());
			}
			if (reader.has("darkblue-kc")) {
				player.setDarkblueKC(reader.get("darkblue-kc").getAsInt());
			}
			if (reader.has("pyro-kc")) {
				player.setPyroKC(reader.get("pyro-kc").getAsInt());
			}
			if (reader.has("wyrm-kc")) {
				player.setWyrmKC(reader.get("wyrm-kc").getAsInt());
			}
			if (reader.has("exoden-kc")) {
				player.setExodenKC(reader.get("exoden-kc").getAsInt());
			}
			if (reader.has("trinity-kc")) {
				player.setTrinityKC(reader.get("trinity-kc").getAsInt());
			}
			if (reader.has("cloud-kc")) {
				player.setCloudKC(reader.get("cloud-kc").getAsInt());
			}
			if (reader.has("herbal-kc")) {
				player.setHerbalKC(reader.get("herbal-kc").getAsInt());
			}
			if (reader.has("breaker-kc")) {
				player.setBreakerKC(reader.get("breaker-kc").getAsInt());
			}
			
			if (reader.has("apollo-kc")) {
				player.setApolloKC(reader.get("apollo-kc").getAsInt());
			}
			if (reader.has("nox-kc")) {
				player.setNoxKC(reader.get("nox-kc").getAsInt());
			}
			if (reader.has("azazel-kc")) {
				player.setAzazelKC(reader.get("azazel-kc").getAsInt());
			}
			if (reader.has("ravana-kc")) {
				player.setRavanaKC(reader.get("ravana-kc").getAsInt());
			}
			
			if (reader.has("supreme-kc")) {
				player.setSupremeKC(reader.get("supreme-kc").getAsInt());
			}
			if (reader.has("player-killstreak")) {
				player.getPlayerKillingAttributes().setPlayerKillStreak(reader.get("player-killstreak").getAsInt());
			}

			if (reader.has("player-deaths")) {
				player.getPlayerKillingAttributes().setPlayerDeaths(reader.get("player-deaths").getAsInt());
			}

			if (reader.has("target-percentage")) {
				player.getPlayerKillingAttributes().setTargetPercentage(reader.get("target-percentage").getAsInt());
			}

			if (reader.has("bh-rank")) {
				player.getAppearance().setBountyHunterSkull(reader.get("bh-rank").getAsInt());
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
			if (reader.has("skillPointArray")) {
                int[] skillPointArray = builder.fromJson(reader.get("skillPointArray").getAsJsonArray(), int[].class);
                player.skillPoints = skillPointArray;
            }
			if (reader.has("run-energy")) {
				player.setRunEnergy(reader.get("run-energy").getAsInt());
			}
			if (reader.has("music")) {
				player.setMusicActive(reader.get("music").getAsBoolean());
			}
			if (reader.has("sounds")) {
				player.setSoundsActive(reader.get("sounds").getAsBoolean());
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
			if (reader.has("sol-effect")) {
				player.setStaffOfLightEffect(Integer.valueOf(reader.get("sol-effect").getAsInt()));
			}
			if (reader.has("skull-timer")) {
				player.setSkullTimer(reader.get("skull-timer").getAsInt());
			}
			if (reader.has("fuse-combination-timer")) {
				player.setFuseCombinationTimer(reader.get("fuse-combination-timer").getAsLong());
			}
			if (reader.has("fuse-item-selected")) {
				player.setFuseItemSelected(reader.get("fuse-item-selected").getAsInt());
			}
			if (reader.has("claimed-fuse-item")) {
				player.setClaimedFuseItem(reader.get("claimed-fuse-item").getAsBoolean());
			}
			if (reader.has("fuse-in-progress")) {
				player.setFuseInProgress(reader.get("fuse-in-progress").getAsBoolean());
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

			if (reader.has("entered-gwd-room")) {
				player.getMinigameAttributes().getGodwarsDungeonAttributes()
						.setHasEnteredRoom(reader.get("entered-gwd-room").getAsBoolean());
			}

			if (reader.has("gwd-altar-delay")) {
				player.getMinigameAttributes().getGodwarsDungeonAttributes()
						.setAltarDelay(reader.get("gwd-altar-delay").getAsLong());
			}

			if (reader.has("gwd-killcount")) {
				player.getMinigameAttributes().getGodwarsDungeonAttributes()
						.setKillcount(builder.fromJson(reader.get("gwd-killcount"), int[].class));
			}

			if (reader.has("effigy")) {
				player.setEffigy(reader.get("effigy").getAsInt());
			}

			if (reader.has("summon-npc")) {
				int npc = reader.get("summon-npc").getAsInt();
				if (npc > 0)
					player.getSummoning().setFamiliarSpawnTask(new FamiliarSpawnTask(player)).setFamiliarId(npc);
			}
			if (reader.has("summon-death")) {
				int death = reader.get("summon-death").getAsInt();
				if (death > 0 && player.getSummoning().getSpawnTask() != null)
					player.getSummoning().getSpawnTask().setDeathTimer(death);
			}
			if (reader.has("process-farming")) {
				player.setProcessFarming(reader.get("process-farming").getAsBoolean());
			}

			if (reader.has("clanchat")) {
				String clan = reader.get("clanchat").getAsString();
				if (!clan.equals("null"))
					player.setClanChatName(clan);
			}
			if (reader.has("autocast")) {
				player.setAutocast(reader.get("autocast").getAsBoolean());
			}
			if (reader.has("autocast-spell")) {
				int spell = reader.get("autocast-spell").getAsInt();
				if (spell != -1)
					player.setAutocastSpell(CombatSpells.getSpell(spell));
			}

			if (reader.has("dfs-charges")) {
				player.incrementDfsCharges(reader.get("dfs-charges").getAsInt());
			}
			if (reader.has("kills")) {
				KillsTracker.submit(player, builder.fromJson(reader.get("kills").getAsJsonArray(), KillsEntry[].class));
			}

			if (reader.has("drops")) {
				DropLog.submit(player, builder.fromJson(reader.get("drops").getAsJsonArray(), DropLogEntry[].class));
			}
			
			if(reader.has("shop-updated")) {
				player.setShopUpdated(reader.get("shop-updated").getAsBoolean());
			}
			
			if(reader.has("shop-earnings")) {
				player.getPlayerOwnedShopManager().setEarnings(reader.get("shop-earnings").getAsLong());
			}

			if (reader.has("coins-gambled")) {
				player.getAchievementAttributes().setCoinsGambled(reader.get("coins-gambled").getAsInt());
			}

			if (reader.has("slayer-master")) {
				player.getSlayer().setSlayerMaster(SlayerMaster.valueOf(reader.get("slayer-master").getAsString()));
			}

			if (reader.has("slayer-task")) {
				player.getSlayer().setSlayerTask(SlayerTasks.valueOf(reader.get("slayer-task").getAsString()));
			}

			if (reader.has("prev-slayer-task")) {
				player.getSlayer().setLastTask(SlayerTasks.valueOf(reader.get("prev-slayer-task").getAsString()));
			}

			if (reader.has("task-amount")) {
				player.getSlayer().setAmountToSlay(reader.get("task-amount").getAsInt());
			}

			if (reader.has("task-streak")) {
				player.getSlayer().setTaskStreak(reader.get("task-streak").getAsInt());
			}

			if (reader.has("duo-partner")) {
				String partner = reader.get("duo-partner").getAsString();
				player.getSlayer().setDuoPartner(partner.equals("null") ? null : partner);
			}

			if (reader.has("double-slay-xp")) {
				player.getSlayer().doubleSlayerXP = reader.get("double-slay-xp").getAsBoolean();
			}

			if (reader.has("recoil-deg")) {
				player.setRecoilCharges(reader.get("recoil-deg").getAsInt());
			}

			if (reader.has("brawler-deg")) {
				player.setBrawlerCharges(builder.fromJson(reader.get("brawler-deg").getAsJsonArray(), int[].class));
			}

			if (reader.has("void-top-deg")) {
				player.setVoidTopCharges(builder.fromJson(reader.get("void-top-deg").getAsJsonArray(), int[].class));
			}

			if (reader.has("void-leg-deg")) {
				player.setVoidLegCharges(builder.fromJson(reader.get("void-leg-deg").getAsJsonArray(), int[].class));
			}

			if (reader.has("killed-players")) {
				List<String> list = new ArrayList<String>();
				String[] killed_players = builder.fromJson(reader.get("killed-players").getAsJsonArray(),
						String[].class);
				for (String s : killed_players)
					list.add(s);
				player.getPlayerKillingAttributes().setKilledPlayers(list);
			}

			if (reader.has("killed-gods")) {
				player.getAchievementAttributes()
						.setGodsKilled(builder.fromJson(reader.get("killed-gods").getAsJsonArray(), boolean[].class));
			}
			
			if (reader.has("blocked-collectors-items")) {
				List<Integer> list = new ArrayList<Integer>();
				Integer[] blocked_items = builder.fromJson(reader.get("blocked-collectors-items").getAsJsonArray(),
						Integer[].class);
				for (int items : blocked_items)
					list.add(items);
				player.getBlockedCollectorsList().addAll(list);
			}

			if (reader.has("barrows-brother")) {
				player.getMinigameAttributes().getBarrowsMinigameAttributes().setBarrowsData(
						builder.fromJson(reader.get("barrows-brother").getAsJsonArray(), int[][].class));
			}

			if (reader.has("random-coffin")) {
				player.getMinigameAttributes().getBarrowsMinigameAttributes()
						.setRandomCoffin((reader.get("random-coffin").getAsInt()));
			}

			if (reader.has("barrows-killcount")) {
				player.getMinigameAttributes().getBarrowsMinigameAttributes()
						.setKillcount((reader.get("barrows-killcount").getAsInt()));
			}

			if (reader.has("nomad")) {
				player.getMinigameAttributes().getNomadAttributes()
						.setQuestParts(builder.fromJson(reader.get("nomad").getAsJsonArray(), boolean[].class));
			}

			if (reader.has("recipe-for-disaster")) {
				player.getMinigameAttributes().getRecipeForDisasterAttributes().setQuestParts(
						builder.fromJson(reader.get("recipe-for-disaster").getAsJsonArray(), boolean[].class));
			}

			if (reader.has("recipe-for-disaster-wave")) {
				player.getMinigameAttributes().getRecipeForDisasterAttributes()
						.setWavesCompleted((reader.get("recipe-for-disaster-wave").getAsInt()));
			}

			if (reader.has("dung-items-bound")) {
				player.getMinigameAttributes().getDungeoneeringAttributes()
						.setBoundItems(builder.fromJson(reader.get("dung-items-bound").getAsJsonArray(), int[].class));
			}

			if (reader.has("rune-ess")) {
				player.setStoredRuneEssence((reader.get("rune-ess").getAsInt()));
			}

			if (reader.has("pure-ess")) {
				player.setStoredPureEssence((reader.get("pure-ess").getAsInt()));
			}

			if (reader.has("bank-pin")) {
				player.getBankPinAttributes()
						.setBankPin(builder.fromJson(reader.get("bank-pin").getAsJsonArray(), int[].class));
			}

			if (reader.has("has-bank-pin")) {
				player.getBankPinAttributes().setHasBankPin(reader.get("has-bank-pin").getAsBoolean());
			}
			if (reader.has("last-pin-attempt")) {
				player.getBankPinAttributes().setLastAttempt(reader.get("last-pin-attempt").getAsLong());
			}
			if (reader.has("invalid-pin-attempts")) {
				player.getBankPinAttributes().setInvalidAttempts(reader.get("invalid-pin-attempts").getAsInt());
			}

			if (reader.has("bank-pin")) {
				player.getBankPinAttributes()
						.setBankPin(builder.fromJson(reader.get("bank-pin").getAsJsonArray(), int[].class));
			}

			if (reader.has("appearance")) {
				player.getAppearance().set(builder.fromJson(reader.get("appearance").getAsJsonArray(), int[].class));
			}

			if (reader.has("agility-obj")) {
				player.setCrossedObstacles(
						builder.fromJson(reader.get("agility-obj").getAsJsonArray(), boolean[].class));
			}

			if (reader.has("skills")) {
				player.getSkillManager().setSkills(builder.fromJson(reader.get("skills"), Skills.class));
			}
			if (reader.has("inventory")) {
				player.getInventory()
						.setItems(builder.fromJson(reader.get("inventory").getAsJsonArray(), Item[].class));
			}
			if (reader.has("equipment")) {
				player.getEquipment()
						.setItems(builder.fromJson(reader.get("equipment").getAsJsonArray(), Item[].class));
			}

			/** BANK **/
			for (int i = 0; i < 9; i++) {
				if (reader.has("bank-" + i + ""))
					player.setBank(i, new Bank(player)).getBank(i).addItems(
							builder.fromJson(reader.get("bank-" + i + "").getAsJsonArray(), Item[].class), false);
			}

			if (reader.has("bank-0")) {
				player.setBank(0, new Bank(player)).getBank(0)
						.addItems(builder.fromJson(reader.get("bank-0").getAsJsonArray(), Item[].class), false);
			}
			if (reader.has("bank-1")) {
				player.setBank(1, new Bank(player)).getBank(1)
						.addItems(builder.fromJson(reader.get("bank-1").getAsJsonArray(), Item[].class), false);
			}
			if (reader.has("bank-2")) {
				player.setBank(2, new Bank(player)).getBank(2)
						.addItems(builder.fromJson(reader.get("bank-2").getAsJsonArray(), Item[].class), false);
			}
			if (reader.has("bank-3")) {
				player.setBank(3, new Bank(player)).getBank(3)
						.addItems(builder.fromJson(reader.get("bank-3").getAsJsonArray(), Item[].class), false);
			}
			if (reader.has("bank-4")) {
				player.setBank(4, new Bank(player)).getBank(4)
						.addItems(builder.fromJson(reader.get("bank-4").getAsJsonArray(), Item[].class), false);
			}
			if (reader.has("bank-5")) {
				player.setBank(5, new Bank(player)).getBank(5)
						.addItems(builder.fromJson(reader.get("bank-5").getAsJsonArray(), Item[].class), false);
			}
			if (reader.has("bank-6")) {
				player.setBank(6, new Bank(player)).getBank(6)
						.addItems(builder.fromJson(reader.get("bank-6").getAsJsonArray(), Item[].class), false);
			}
			if (reader.has("bank-7")) {
				player.setBank(7, new Bank(player)).getBank(7)
						.addItems(builder.fromJson(reader.get("bank-7").getAsJsonArray(), Item[].class), false);
			}
			if (reader.has("bank-8")) {
				player.setBank(8, new Bank(player)).getBank(8)
						.addItems(builder.fromJson(reader.get("bank-8").getAsJsonArray(), Item[].class), false);
			}

			if (reader.has("ge-slots")) {
				GrandExchangeSlot[] slots = builder.fromJson(reader.get("ge-slots").getAsJsonArray(),
						GrandExchangeSlot[].class);
				player.setGrandExchangeSlots(slots);
			}

			if (reader.has("store")) {
				Item[] validStoredItems = builder.fromJson(reader.get("store").getAsJsonArray(), Item[].class);
				if (player.getSummoning().getSpawnTask() != null)
					player.getSummoning().getSpawnTask().setValidItems(validStoredItems);
			}

			if (reader.has("charm-imp")) {
				int[] charmImpConfig = builder.fromJson(reader.get("charm-imp").getAsJsonArray(), int[].class);
				player.getSummoning().setCharmimpConfig(charmImpConfig);
			}

			if (reader.has("blowpipe-charge-item") || reader.has("blowpipe-charge-amount")) {
				player.getBlowpipeLoading().getContents().setCount(reader.get("blowpipe-charge-item").getAsInt(),
						reader.get("blowpipe-charge-amount").getAsInt());
			}

			if (reader.has("friends")) {
				long[] friends = builder.fromJson(reader.get("friends").getAsJsonArray(), long[].class);

				for (long l : friends) {
					player.getRelations().getFriendList().add(l);
				}
			}
			if (reader.has("ignores")) {
				long[] ignores = builder.fromJson(reader.get("ignores").getAsJsonArray(), long[].class);

				for (long l : ignores) {
					player.getRelations().getIgnoreList().add(l);
				}
			}
			
			if(reader.has("cleansing-time")) {
				player.setCleansingTime(reader.get("cleansing-time").getAsInt());
			}
			
			if (reader.has("amount-donated-today")) {
				player.setAmountDonatedToday(reader.get("amount-donated-today").getAsInt());
			}
			
			if (reader.has("claimed-first")) {
				player.claimedFirst = reader.get("claimed-first").getAsBoolean();
			}
			
			if (reader.has("claimed-second")) {
				player.claimedSecond = reader.get("claimed-second").getAsBoolean();
			}
			
			if (reader.has("claimed-third")) {
				player.claimedThird = reader.get("claimed-third").getAsBoolean();
			}
			
			if(reader.has("last-donation")) {
				player.lastDonation = reader.get("last-donation").getAsLong();
			}
			
			if(reader.has("last-time-reset")) {
				player.lastDonation = reader.get("last-time-reset").getAsLong();
			}
			
			if(reader.has("praise-time")) {
				player.setPraiseTime(reader.get("praise-time").getAsInt());
			}

			if (reader.has("loyalty-titles")) {
				player.setUnlockedLoyaltyTitles(
						builder.fromJson(reader.get("loyalty-titles").getAsJsonArray(), boolean[].class));
			}
			
			if (reader.has("npc-tasks-progress")) {
				int[] loadedProgress = builder.fromJson(reader.get("npc-tasks-progress").getAsJsonArray(),
						int[].class);
				int defaultLength = player.getNpcTaskAttributes().getProgress().length;
				if (loadedProgress.length < defaultLength) {

					for (int index = 0; index < defaultLength - 1; index++) {
						if (index < loadedProgress.length) {
							player.getNpcTaskAttributes().setProgress(index, loadedProgress[index]);
						} else {
							player.getNpcTaskAttributes().setProgress(index, 0);
						}
					}
				} else {
					player.getNpcTaskAttributes().setProgress(loadedProgress);
				}
			}
			
			if (reader.has("npc-task-completion")) {
				boolean[] loadedCompletion = builder.fromJson(reader.get("npc-task-completion").getAsJsonArray(),
						boolean[].class);
				int defaultLength = player.getNpcTaskAttributes().getCompletion().length;
			//	//System.out.println("load length: "+ loadedCompletion.length + " default: " + defaultLength);
				if (loadedCompletion.length < defaultLength) {
					//System.out.println("load length: "+ loadedCompletion + " default: " + defaultLength);
					for (int index = 0; index < defaultLength - 1; index++) {
						if (index < loadedCompletion.length) {
							//System.out.println("what is this: " + index);
							player.getNpcTaskAttributes().setCompletion(index, loadedCompletion[index]);
						} else {
							player.getNpcTaskAttributes().setCompletion(index, false);
						}
					}
				} else {
					player.getNpcTaskAttributes().setCompletion(loadedCompletion);
				}
			}
			
			if (reader.has("tasks-completion")) {
				boolean[] loadedCompletion = builder.fromJson(reader.get("tasks-completion").getAsJsonArray(),
						boolean[].class);
				int defaultLength = player.getStarterTaskAttributes().getCompletion().length;
			//	//System.out.println("load length: "+ loadedCompletion.length + " default: " + defaultLength);
				if (loadedCompletion.length < defaultLength) {
					//System.out.println("load length: "+ loadedCompletion + " default: " + defaultLength);
					for (int index = 0; index < defaultLength - 1; index++) {
						if (index < loadedCompletion.length) {
							//System.out.println("what is this: " + index);
							player.getStarterTaskAttributes().setCompletion(index, loadedCompletion[index]);
						} else {
							player.getStarterTaskAttributes().setCompletion(index, false);
						}
					}
				} else {
					player.getStarterTaskAttributes().setCompletion(loadedCompletion);
				}
			}
			
			if (reader.has("npc-kc-data")) {
				Map<Integer, Integer> kcData = builder.fromJson(
					    reader.get("npc-kc-data"), 
					    new TypeToken<Map<Integer, Integer>>() {}.getType()
					);
				//System.out.println("Got this data: " + kcData);
				player.setNpcKillCount(kcData);
			}

			if (reader.has("achievements-completion")) {
				boolean[] loadedCompletion = builder.fromJson(reader.get("achievements-completion").getAsJsonArray(),
						boolean[].class);
				int defaultLength = player.getAchievementAttributes().getCompletion().length;
				if (loadedCompletion.length < defaultLength) {

					for (int index = 0; index < defaultLength - 1; index++) {
						if (index < loadedCompletion.length) {
							player.getAchievementAttributes().setCompletion(index, loadedCompletion[index]);
						} else {
							player.getAchievementAttributes().setCompletion(index, false);
						}
					}
				} else {
					player.getAchievementAttributes().setCompletion(loadedCompletion);
				}
			}
			
            if (reader.has("starterprogressioncompletions")) {
                player.setStarterProgressionCompletions(builder.fromJson(reader.get("starterprogressioncompletions"), boolean[].class));
            }
			
			if (reader.has("tasks-progress")) {
				int[] loadedProgress = builder.fromJson(reader.get("tasks-progress").getAsJsonArray(),
						int[].class);
				int defaultLength = player.getStarterTaskAttributes().getProgress().length;
				if (loadedProgress.length < defaultLength) {

					for (int index = 0; index < defaultLength - 1; index++) {
						if (index < loadedProgress.length) {
							player.getStarterTaskAttributes().setProgress(index, loadedProgress[index]);
						} else {
							player.getStarterTaskAttributes().setProgress(index, 0);
						}
					}
				} else {
					player.getStarterTaskAttributes().setProgress(loadedProgress);
				}
			}
			
			if (reader.has("achievements-progress")) {
				int[] loadedProgress = builder.fromJson(reader.get("achievements-progress").getAsJsonArray(),
						int[].class);
				int defaultLength = player.getAchievementAttributes().getProgress().length;
				if (loadedProgress.length < defaultLength) {

					for (int index = 0; index < defaultLength - 1; index++) {
						if (index < loadedProgress.length) {
							player.getAchievementAttributes().setProgress(index, loadedProgress[index]);
						} else {
							player.getAchievementAttributes().setProgress(index, 0);
						}
					}
				} else {
					player.getAchievementAttributes().setProgress(loadedProgress);
				}
			}
			
			if (reader.has("max-cape-colors")) {
				int[] colors = builder.fromJson(reader.get("max-cape-colors").getAsJsonArray(), int[].class);
				player.setMaxCapeColors(colors);
			}
			
			/*if (reader.has("santa-colors")) {
				int[] colors = builder.fromJson(reader.get("santa-colors").getAsJsonArray(), int[].class);
				player.setSantaColors(colors);
			}*/
			
			if (reader.has("comp-cape-colors")) {
                int[] colors = builder.fromJson(reader.get("comp-cape-colors").getAsJsonArray(), int[].class);
                player.setCompCapeColors(colors);
            }
			
			if (reader.has("player-title")) {
				player.setTitle(reader.get("player-title").getAsString());
			}
			
			if (reader.has("presetItems")) {
				int[] presetItems = builder.fromJson(reader.get("presetItems").getAsJsonArray(), int[].class);
				player.presetItems = presetItems;
			}
			if (reader.has("presetGear")) {
				int[] presetGear = builder.fromJson(reader.get("presetGear").getAsJsonArray(), int[].class);
				player.presetGear = presetGear;
			}
			if (reader.has("PrayerBook")) {
				player.setPrayerBook(reader.get("PrayerBook").getAsInt());
			}
			if (reader.has("MageBook")) {
				player.setMageBook(reader.get("MageBook").getAsInt());
			}

			if (reader.has("presetItems2")) {
				int[] presetItems2 = builder.fromJson(reader.get("presetItems2").getAsJsonArray(), int[].class);
				player.presetItems2 = presetItems2;
			}
			if (reader.has("presetGear2")) {
				int[] presetGear2 = builder.fromJson(reader.get("presetGear2").getAsJsonArray(), int[].class);
				player.presetGear2 = presetGear2;
			}
			if (reader.has("PrayerBook2")) {
				player.setPrayerBook2(reader.get("PrayerBook2").getAsInt());
			}
			if (reader.has("MageBook2")) {
				player.setMageBook2(reader.get("MageBook2").getAsInt());
			}

			if (reader.has("presetItems3")) {
				int[] presetItems3 = builder.fromJson(reader.get("presetItems3").getAsJsonArray(), int[].class);
				player.presetItems3 = presetItems3;
			}
			if (reader.has("presetGear3")) {
				int[] presetGear3 = builder.fromJson(reader.get("presetGear3").getAsJsonArray(), int[].class);
				player.presetGear3 = presetGear3;
			}
			if (reader.has("PrayerBook3")) {
				player.setPrayerBook3(reader.get("PrayerBook3").getAsInt());
			}
			if (reader.has("MageBook3")) {
				player.setMageBook3(reader.get("MageBook3").getAsInt());
			}

			if (reader.has("presetItems4")) {
				int[] presetItems4 = builder.fromJson(reader.get("presetItems4").getAsJsonArray(), int[].class);
				player.presetItems4 = presetItems4;
			}
			if (reader.has("presetGear4")) {
				int[] presetGear4 = builder.fromJson(reader.get("presetGear4").getAsJsonArray(), int[].class);
				player.presetGear4 = presetGear4;
			}
			if (reader.has("PrayerBook4")) {
				player.setPrayerBook4(reader.get("PrayerBook4").getAsInt());
			}
			if (reader.has("MageBook4")) {
				player.setMageBook4(reader.get("MageBook4").getAsInt());
			}

			if (reader.has("presetItems5")) {
				int[] presetItems5 = builder.fromJson(reader.get("presetItems5").getAsJsonArray(), int[].class);
				player.presetItems5 = presetItems5;
			}
			if (reader.has("presetGear5")) {
				int[] presetGear5 = builder.fromJson(reader.get("presetGear5").getAsJsonArray(), int[].class);
				player.presetGear5 = presetGear5;
			}
			if (reader.has("PrayerBook5")) {
				player.setPrayerBook5(reader.get("PrayerBook5").getAsInt());
			}
			if (reader.has("MageBook5")) {
				player.setMageBook5(reader.get("MageBook5").getAsInt());
			}

			if (reader.has("preset")) {
				Player.preset = reader.get("preset").getAsBoolean();
			}
			if (reader.has("preset2")) {
				Player.preset2 = reader.get("preset2").getAsBoolean();
			}
			if (reader.has("preset3")) {
				Player.preset3 = reader.get("preset3").getAsBoolean();
			}
			if (reader.has("preset4")) {
				Player.preset4 = reader.get("preset4").getAsBoolean();
			}
			if (reader.has("preset5")) {
				Player.preset5 = reader.get("preset5").getAsBoolean();
			}

			
			if (reader.has("player-custom-yell")) {
				if (player.getRights() != null)
				player.getRights().customYellTitle = (reader.get("player-custom-yell").getAsString());
			}
			
			if (reader.has("lastDonationClaim")) 
				player.lastDonationClaim = (reader.get("lastDonationClaim").getAsLong());
			
			if (reader.has("lastOpPotion")) 
				player.lastOpPotion = (reader.get("lastOpPotion").getAsLong());
			
			if (reader.has("lastHpRestore")) 
				player.lastHpRestore = (reader.get("lastHpRestore").getAsLong());
			
			if (reader.has("lastPrayerRestore")) 
				player.lastPrayerRestore = (reader.get("lastPrayerRestore").getAsLong());
			
			if (reader.has("lastCashClaim")) 
				player.lastCashClaim = (reader.get("lastCashClaim").getAsLong());
			
			if(reader.has("roulette-balance"))
				player.setRouletteBalance(reader.get("roulette-balance").getAsInt());
			
			if(reader.has("roulette-bet-amount"))
				player.setRouletteBet(reader.get("roulette-bet-amount").getAsInt());
			
			if(reader.has("lastSpecialPotClaim"))
				player.lastSpecialRestoreClaim = (reader.get("lastSpecialPotClaim").getAsLong());
			
			
			if (reader.has("savedKeyCount1")) 
				player.savedKeyCount1 = (reader.get("savedKeyCount1").getAsInt());
			
			if (reader.has("savedKeyCount2")) 
				player.savedKeyCount2 = (reader.get("savedKeyCount2").getAsInt());
			
			if (reader.has("savedKeyCount3")) 
				player.savedKeyCount3 = (reader.get("savedKeyCount3").getAsInt());
			
			
		} catch (Exception e) {
			e.printStackTrace();
			return LoginResponses.LOGIN_SUCCESSFUL;
		}
try {
			
			Path path2 = Paths.get("./data/saves/construction/", player.getUsername() + ".obj");
			File file2 = path2.toFile();
			
			if(file2.exists()) {
				FileInputStream fileIn = new FileInputStream(file2);
				ObjectInputStream in = new ObjectInputStream(fileIn);
				ConstructionSave save = (ConstructionSave) in.readObject();
				player.setHouseRooms(save.getHouseRooms());
				player.setHouseFurtinture(save.getHouseFurniture());
				player.setHousePortals(save.getHousePortals());
				in.close();
				fileIn.close();
			}
			
		} catch(Throwable t) {
			t.printStackTrace();
		}
		return LoginResponses.LOGIN_SUCCESSFUL;
	}
}