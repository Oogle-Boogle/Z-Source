package com.zamron.world.entity.impl.player;

import com.zamron.model.*;
import com.zamron.world.content.DPSTask;
import com.zamron.GameServer;
import com.zamron.GameSettings;
import com.zamron.engine.task.TaskManager;
import com.zamron.engine.task.impl.BonusExperienceTask;
import com.zamron.engine.task.impl.CleansingTask;
import com.zamron.engine.task.impl.CombatSkullEffect;
import com.zamron.engine.task.impl.FireImmunityTask;
import com.zamron.engine.task.impl.OverloadPotionTask;
import com.zamron.engine.task.impl.PlayerSkillsTask;
import com.zamron.engine.task.impl.PlayerSpecialAmountTask;
import com.zamron.engine.task.impl.PraiseTask;
import com.zamron.engine.task.impl.PrayerRenewalPotionTask;
import com.zamron.engine.task.impl.StaffOfLightSpecialAttackTask;
import com.zamron.model.container.impl.Bank;
import com.zamron.model.container.impl.Equipment;
import com.zamron.model.definitions.NpcDefinition;
import com.zamron.model.definitions.WeaponAnimations;
import com.zamron.model.definitions.WeaponInterfaces;
import com.zamron.model.input.impl.EnterPinPacketListener;
import com.zamron.net.PlayerSession;
import com.zamron.net.SessionState;
import com.zamron.net.security.ConnectionHandler;
import com.zamron.util.Misc;
import com.zamron.world.World;
import com.zamron.world.content.Achievements;
import com.zamron.world.content.BonusManager;
import com.zamron.world.content.Lottery;
import com.zamron.world.content.PlayerLogs;
import com.zamron.world.content.PlayerPanel;
import com.zamron.world.content.PlayersOnlineInterface;
import com.zamron.world.content.StaffList;
import com.zamron.world.content.StartScreen;
import com.zamron.world.content.clan.ClanChatManager;
import com.zamron.world.content.combat.DailyNPCTask;
import com.zamron.world.content.combat.TenKMassacre;
import com.zamron.world.content.combat.effect.CombatPoisonEffect;
import com.zamron.world.content.combat.effect.CombatTeleblockEffect;
import com.zamron.world.content.combat.magic.Autocasting;
import com.zamron.world.content.combat.prayer.CurseHandler;
import com.zamron.world.content.combat.prayer.PrayerHandler;
import com.zamron.world.content.combat.pvp.BountyHunter;
import com.zamron.world.content.combat.range.DwarfMultiCannon;
import com.zamron.world.content.combat.weapon.CombatSpecial;
import com.zamron.world.content.dropchecker.NPCDropTableChecker;
import com.zamron.world.content.grandexchange.GrandExchange;
import com.zamron.world.content.groupironman.GroupIronmanGroup;
import com.zamron.world.content.minigames.impl.Barrows;
import com.zamron.world.content.raids.OldRaidParty;
import com.zamron.world.content.skill.impl.dungeoneering.Dungeoneering;
import com.zamron.world.content.skill.impl.hunter.Hunter;
import com.zamron.world.content.skill.impl.slayer.Slayer;

public class PlayerHandler {

	/**
	 * Gets the player according to said name.
	 * 
	 * @param name The name of the player to search for.
	 * @return The player who has the same name as said param.
	 */

	public static Player getPlayerForName(String name) {
		for (Player player : World.getPlayers()) {
			if (player == null)
				continue;
			if (player.getUsername().equalsIgnoreCase(name))
				return player;
		}
		return null;
	}

	public static void handleLogin(Player player) {
		//System.out.println("[World] Registering player - [username, host] : [" + player.getUsername() + ", "
				//+ player.getHostAddress() + "]");
		player.getSession().setState(SessionState.LOGGED_IN);
		player.setPlaceholders(false);
		player.getPacketSender().sendConfig(111, player.isPlaceholders() ? 1 : 0);
		if (player.getHasPin() == true && !player.getSavedIp().equalsIgnoreCase(player.getHostAddress())) {
			player.setPlayerLocked(true);
		}
		PlayerPanel.refreshPanel(player);
		if (Slayer.duoPartner != null) {
			Slayer.resetDuo(player, player);
		}
		player.getPlayerOwnedShopManager().hookShop();
		player.setActive(true);
		ConnectionHandler.add(player.getHostAddress());
		World.getPlayers().add(player);
		World.updatePlayersOnline();
		PlayersOnlineInterface.add(player);
		if (player.getCleansingTime() > 1) {
			TaskManager.submit(new CleansingTask(player));
			player.setDoubleRateActive(true);
		}


		/*** DISCORD PRESENCE ***/
		player.getPacketSender().sendRichPresenceDetails("IGN: " + player.getUsername() + " | Total Lvl: " + player.getSkillManager().getTotalLevel());
		player.getPacketSender().sendRichPresenceBigPictureText("https://zamron.net");
		player.getPacketSender().sendRichPresenceState("Exploring Zamron..");
		player.getPacketSender().sendRichPresenceSmallPictureText("Combat Lvl: " + player.getSkillManager().getCombatLevel());
		player.getPacketSender().sendSmallImageKey("home");
		/** End Presence **/

		player.getPacketSender().sendMapRegion().sendDetails();

		player.getRecordedLogin().reset();

		player.getPacketSender().sendTabs();
		if (GroupIronmanGroup.getGroups().containsKey(player.getUsername())) {
            GroupIronmanGroup gim = GroupIronmanGroup.getGroups().get(player.getUsername());
            gim.setOwner(player);
            player.setGroupIronmanGroup(GroupIronmanGroup.getGroups().get(player.getUsername()));
            ////System.out.println("Group set ->");
        } else {
            ////System.out.println("No cuz -> " + GroupIronmanGroup.getGroups());
        }

        if (GroupIronmanGroup.getGroups().containsKey(player.getGroupOwnerName())) {
            player.setGroupIronmanGroup(GroupIronmanGroup.getGroups().get(player.getGroupOwnerName()));
        }
		for (int i = 0; i < player.getBanks().length; i++) {
			if (player.getBank(i) == null) {
				player.setBank(i, new Bank(player));
			}
		}
		player.getInventory().refreshItems();
		player.getEquipment().refreshItems();
		//player.getEquipmentWings().refreshItems();

		WeaponAnimations.assign(player, player.getEquipment().get(Equipment.WEAPON_SLOT));
		WeaponInterfaces.assign(player, player.getEquipment().get(Equipment.WEAPON_SLOT));
		CombatSpecial.updateBar(player);
		BonusManager.update(player);

		player.getSummoning().login();
		player.getFarming().load();
		TenKMassacre.checkOnLogin(player);

		//Slayer.checkDuoSlayer(player, true);
		player.getBestItems().fillDefinitions();
		for (Skill skill : Skill.values()) {
			player.getSkillManager().updateSkill(skill);
		}

		player.getRelations().setPrivateMessageId(1).onLogin(player).updateLists(true);

		player.getPacketSender().sendConfig(172, player.isAutoRetaliate() ? 1 : 0)
				.sendTotalXp(player.getSkillManager().getTotalGainedExp())
				.sendConfig(player.getFightType().getParentId(), player.getFightType().getChildId()).sendRunStatus()
				.sendRunEnergy(player.getRunEnergy()).sendString(8135, "" + player.getMoneyInPouch())
				.sendInteractionOption("Follow", 3, false).sendInteractionOption("Trade With", 4, false)
				.sendInteractionOption("Gamble With", 6, false).sendInterfaceRemoval()
				.sendString(39173, "@or2@Server time: @or2@[ @yel@" + Misc.getCurrentServerTime() + "@or2@ ]");

		player.getPacketSender().sendRights();
		Autocasting.onLogin(player);
		PrayerHandler.deactivateAll(player);
		CurseHandler.deactivateAll(player);
		BonusManager.sendCurseBonuses(player);
		Achievements.updateInterface(player);
		Barrows.updateInterface(player);

		if (player.getDonationDeals().shouldReset()) {
			//System.out.println("Resetting");
		} else {
			//System.out.println("Not resetting");
		}

		TaskManager.submit(new PlayerSkillsTask(player));
		if (player.isPoisoned()) {
			TaskManager.submit(new CombatPoisonEffect(player));
		}
		if (player.getPrayerRenewalPotionTimer() > 0) {
			TaskManager.submit(new PrayerRenewalPotionTask(player));
		}
		if (player.getOverloadPotionTimer() > 0) {
			TaskManager.submit(new OverloadPotionTask(player));
		}
		if (player.getTeleblockTimer() > 0) {
			TaskManager.submit(new CombatTeleblockEffect(player));
		}
		if (player.getSkullTimer() > 0) {
			player.setSkullIcon(1);
			TaskManager.submit(new CombatSkullEffect(player));
		}

		if (player.getProgressionManager().getProgressions() == null
				|| player.getProgressionManager().getProgressions().size() < 1) {
			//player.sendMessage("Handled this data");
			player.getProgressionManager().loadData(); // this loads all the ones incl new ones
		}
		// player.sendMessage("Called it");
		// TaskManager.submit(new TimelyReward1(player));
		if (player.getFireImmunity() > 0) {
			FireImmunityTask.makeImmune(player, player.getFireImmunity(), player.getFireDamageModifier());
		}
		if (player.getSpecialPercentage() < 100) {
			TaskManager.submit(new PlayerSpecialAmountTask(player));
		}
		if (player.hasStaffOfLightEffect()) {
			TaskManager.submit(new StaffOfLightSpecialAttackTask(player));
		}
		if (player.getMinutesBonusExp() >= 0) {
			TaskManager.submit(new BonusExperienceTask(player));
		}

		if (player.getPraiseTime() >= 100) {
			player.setDoubleRateActive(true);
			TaskManager.submit(new PraiseTask(player));
		}

		if (player.getPosition().getX() >= 2088 && player.getPosition().getX() <= 2107
				&& player.getPosition().getY() >= 4420 && player.getPosition().getY() <= 4438) {
			player.moveTo(new Position(3087, 3495));
			int[] keyIds = { 1544, 1545, 1546, 1547, 1548 };
			if (player.getInventory().containsAny(1544, 1545, 1546, 1547, 1548)) {
				for (int i = 0; i < keyIds.length; i++) {
					player.getInventory().delete(keyIds[i], 1);
				}
			}
		}

		if (player.getHasPin() == true && !player.getSavedIp().equalsIgnoreCase(player.getHostAddress())) {
			//System.out.println("Current ip: " + player.getHostAddress());
			//System.out.println("Saved ip: " + player.getSavedIp());
			player.setInputHandling(new EnterPinPacketListener());
			player.getPacketSender().sendEnterInputPrompt("Enter your pin to play#confirmstatus");
		} else {
			//System.out.println("Player: " + player.getUsername() + " Didn't have pin set");
		}

		player.getUpdateFlag().flag(Flag.APPEARANCE);

		Lottery.onLogin(player);
		Locations.login(player);
		
		player.getPacketSender().sendMessage("<img=12><col=368a24><shad=555>Welcome To Zamron!");
		player.getPacketSender()
				.sendMessage("<img=12><col=187518><shad=555>Join Discord to stay up to date with updates / information ::discord");
		player.getPacketSender()
				.sendMessage("<img=12><col=7838a3><shad=555>For any questions contact <col=dddddd><shad=111><img=3>Oogle");
		player.getPacketSender()
		.sendMessage("<img=12><shad=20><col=b96900>Did you know? We reward players who report bugs! <img=392>");

		if (player.getHasPin() == false) {
			player.getPacketSender().sendMessage("<img=18>Your account isn't safe! Make sure to ::setpin");
		}

		if (player.experienceLocked()) {
			player.getPacketSender().sendMessage("@red@Warning: your experience is currently locked.");
		}
		ClanChatManager.handleLogin(player);
		PlayerPanel.refreshPanel(player);
		
		player.getPacketSender().sendWalkableInterface(23998, true);
		TaskManager.submit(new DPSTask(player));


		// New player
		if (player.newPlayer()) {
			StartScreen.open(player);
			player.setPlayerLocked(true);
		}

		if (player.getDifficulty().equals(Difficulty.Default) && !player.newPlayer()) {
			DifficultyHandler.openInterface(player);
			player.getPacketSender().sendMessage("Please choose a difficulty!");
		}

		player.getPacketSender().updateSpecialAttackOrb().sendIronmanMode(player.getGameMode().ordinal());

		if (player.getRights() == PlayerRights.OWNER || player.getRights() == PlayerRights.SUPPORT
				|| player.getRights() == PlayerRights.MODERATOR || player.getRights() == PlayerRights.ADMINISTRATOR
						|| player.getRights() == PlayerRights.COMMUNITY_MANAGER
								|| player.getRights() == PlayerRights.HADMIN
						
				|| player.getRights() == PlayerRights.DEVELOPER) {
			World.sendMessageNonDiscord("<img=14><shad=20><col=1eaa08> " + Misc.formatText(player.getRights().toString().toLowerCase()) + " " + player.getUsername()
			+ " has just logged in, feel free to message them for support.");
		}
		if (player.getRights() == PlayerRights.MODERATOR || player.getRights() == PlayerRights.ADMINISTRATOR
				|| player.getRights() == PlayerRights.COMMUNITY_MANAGER
				|| player.getRights() == PlayerRights.SUPPORT || player.getRights() == PlayerRights.DEVELOPER|| player.getRights() == PlayerRights.HADMIN
				|| player.getRights() == PlayerRights.OWNER) {
			if (!StaffList.staff.contains(player.getUsername())) {
				StaffList.login(player);
			}
		}
		
		GrandExchange.onLogin(player);

		if (player.getPointsHandler().getAchievementPoints() == 0) {
			Achievements.setPoints(player);
		}

		if (player.getPlayerOwnedShopManager().getEarnings() > 0) {
			player.sendMessage("<col=FF0000>You have unclaimed earnings in your player owned shop!");
		}

		NPCDropTableChecker.getSingleton().refreshDropTableChilds(player);
		PlayerLogs.log(player.getUsername(),
				"Login from host " + player.getHostAddress() + ", serial number: " + player.getSerialNumber());

		if (DailyNPCTask.CHOSEN_NPC_ID != 0) {
			player.getPacketSender().sendMessage("@red@Today's Daily NPC task is :"
					+ DailyNPCTask.KILLS_REQUIRED
					+ " x "
					+ NpcDefinition.forId(DailyNPCTask.CHOSEN_NPC_ID).getName());
		}

	}

	public static boolean handleLogout(Player player, boolean fromUpdate) {
		try {

			if (player.isMiniMe) {
				return true;
			}

			PlayerSession session = player.getSession();

			if (session.getChannel().isOpen()) {
				session.getChannel().close();
			}

			if (!player.isRegistered()) {
				return true;
			}

			boolean exception = GameServer.isUpdating()
					|| World.getLogoutQueue().contains(player) && player.getLogoutTimer().elapsed(600000);
			if (player.logout() || exception) {
				//System.out.println("[World] Deregistering player - [username, host] : [" + player.getUsername() + ", "
						//+ player.getHostAddress() + "]");
				player.getSession().setState(SessionState.LOGGING_OUT);
				ConnectionHandler.remove(player.getHostAddress());
				player.setTotalPlayTime(player.getTotalPlayTime() + player.getRecordedLogin().elapsed());
				player.getPacketSender().sendInterfaceRemoval();

				if (player.getNpcTransformationId() > 1) {
					player.setNpcTransformationId(-1);
				}
				
				if (player.getCannon() != null) {
					DwarfMultiCannon.pickupCannon(player, player.getCannon(), true);
				}

				PlayerPanel.refreshPanel(player);
				if (Slayer.duoPartner != null) {
					Slayer.resetDuo(player, player);
				}
				if (exception && player.getResetPosition() != null) {
					player.moveTo(player.getResetPosition());
					player.setResetPosition(null);
				}
				if (player.getRegionInstance() != null) {
					player.getRegionInstance().destruct();
				}

				if (player.getRights() == PlayerRights.MODERATOR || player.getRights() == PlayerRights.ADMINISTRATOR
						|| player.getRights() == PlayerRights.SUPPORT || player.getRights() == PlayerRights.DEVELOPER
								|| player.getRights() == PlayerRights.COMMUNITY_MANAGER
										|| player.getRights() == PlayerRights.HADMIN
						|| player.getRights() == PlayerRights.OWNER) {
					StaffList.logout(player);
				}
				if (player.getOldRaidParty() != null && player.getOldRaidParty().getLeader() != player) {
					player.getOldRaidParty().leave(player);
					player.setOldRaidParty(null);
					System.err.println("leave player raid party");
				}
				else if (player.getOldRaidParty() != null && player.getOldRaidParty().getLeader() == player) {
					OldRaidParty.disband();
					player.setOldRaidParty(null);
					System.err.println("leave leader raid party");
				}
				
				player.endKeyRoom(GameServer.isUpdating() || fromUpdate);
				player.endCustomBossRoom();
				Hunter.handleLogout(player);
				Locations.logout(player);
				player.getSummoning().unsummon(false, false);
				player.getFarming().save();
				player.getPlayerOwnedShopManager().unhookShop();
				BountyHunter.handleLogout(player);
				ClanChatManager.save();
				// player.attackable = true;
				ClanChatManager.leave(player, false);
				player.getRelations().updateLists(false);
				PlayersOnlineInterface.remove(player);
				TaskManager.cancelTasks(player.getCombatBuilder());
				TaskManager.cancelTasks(player);
				player.save();
				World.getPlayers().remove(player);
				session.setState(SessionState.LOGGED_OUT);
				World.updatePlayersOnline();
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	public static boolean handleLogout(Player player) {
		try {
			if (player.isMiniMe) {
				//System.out.println("Trying to get rid of bot: " + player.getUsername());
				return true;
			}
			PlayerSession session = player.getSession();

			if (session != null) {
				if (session.getChannel().isOpen()) {
					//System.out.println(player.getUsername() + " closed channel 1");
					session.getChannel().close();
				}
			}

			if (!player.isRegistered()) {
				//System.out.println(player.getUsername() + " returned true 1");
				return true;
			}

			boolean exception = GameServer.isUpdating()
					|| World.getLogoutQueue().contains(player) && player.getLogoutTimer().elapsed(600000);
			//System.out.println(player.getUsername() + " exception: " + exception);
			if (player.logout() || exception) {
				// new Thread(new HighscoresHandler(player)).start();
				//System.out.println("[World] Deregistering player - [username, host] : [" + player.getUsername() + ", "
						//+ player.getHostAddress() + "]");
				player.getSession().setState(SessionState.LOGGING_OUT);
				ConnectionHandler.remove(player.getHostAddress());
				player.setTotalPlayTime(player.getTotalPlayTime() + player.getRecordedLogin().elapsed());
				player.getPacketSender().sendInterfaceRemoval();
				if (player.getCannon() != null) {
					DwarfMultiCannon.pickupCannon(player, player.getCannon(), true);
				}
				if (exception && player.getResetPosition() != null) {
					player.moveTo(player.getResetPosition());
					player.setResetPosition(null);
				}

				if (player.getRegionInstance() != null) {
					player.getRegionInstance().destruct();
				}

				if (player.getRights() == PlayerRights.MODERATOR || player.getRights() == PlayerRights.ADMINISTRATOR
						|| player.getRights() == PlayerRights.DEVELOPER
								|| player.getRights() == PlayerRights.HADMIN
								|| player.getRights() == PlayerRights.COMMUNITY_MANAGER
								|| player.getRights() == PlayerRights.SUPPORT || player.getRights() == PlayerRights.DEVELOPER
						|| player.getRights() == PlayerRights.OWNER) {
					StaffList.logout(player);
				}
				if (Dungeoneering.doingDungeoneering(player)) {
					player.getEquipment().resetItems();
					player.getInventory().resetItems();
					player.getMinigameAttributes().getDungeoneeringAttributes().getParty().remove(player, true, true);
					player.moveTo(GameSettings.DEFAULT_POSITION);
				}
				if(player.getPosition().getX() >= 2380 && player.getPosition().getX() <= 2430) {
					if(player.getPosition().getY() >= 3465 && player.getPosition().getY() <= 3514) {
						player.moveTo(GameSettings.DEFAULT_POSITION);
					}
				}
				if (player.getOldRaidParty() != null) {
					player.getOldRaidParty().leave(player);
				}
				if (player.getGodPotionStatus()) {
					player.setGodPotionStatus(false);
					player.setGodPotionStatus(0);
					//System.out.println("Ended god potion for " + player.getUsername());
				}
				Hunter.handleLogout(player);
				Locations.logout(player);
				player.getSummoning().unsummon(false, false);
				player.getFarming().save();
				player.getPlayerOwnedShopManager().unhookShop();
				BountyHunter.handleLogout(player);
				ClanChatManager.leave(player, false);
				player.getRelations().updateLists(false);
				PlayersOnlineInterface.remove(player);
				
				if(player.getBank() != null) {
					Bank bankHolder = player.getBank();
					for (int i = 0; i < bankHolder.getBankTabs().length; i++) {
						Bank bank = new Bank(player);
						if (bank != null) {
							for (Item item : bankHolder.getBank(i).getItems()) {
								bank.add(item);
							}
							player.setBank(i, bank);
						}
					}
					player.setBankHolder(null);
				}
				TaskManager.cancelTasks(player.getCombatBuilder());
				TaskManager.cancelTasks(player);
				
				player.save();
				World.getPlayers().remove(player);
				session.setState(SessionState.LOGGED_OUT);
				World.updatePlayersOnline();
				//System.out.println(player.getUsername() + " returned true 2");
				return true;
			}
			//System.out.println(player.getUsername() + " returned false 1");
			return false;

		} catch (Exception e) {
			e.printStackTrace();
		}
		//System.out.println(player.getUsername() + " returned true 3");
		return true;
	}
}
