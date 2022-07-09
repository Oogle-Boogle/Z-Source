package com.zamron.world.content.minigames.impl;

import com.zamron.engine.task.Task;
import com.zamron.engine.task.TaskManager;
import com.zamron.model.Graphic;
import com.zamron.model.Position;
import com.zamron.model.RegionInstance;
import com.zamron.model.RegionInstance.RegionInstanceType;
import com.zamron.world.World;
import com.zamron.world.content.PlayerPanel;
import com.zamron.world.content.dialogue.DialogueManager;
import com.zamron.world.entity.impl.npc.NPC;
import com.zamron.world.entity.impl.player.Player;

/**
 * @author Gabriel Hannason
 */
public class Nomad {

	public static void startFight(final Player p) {
		if(p.getMinigameAttributes().getNomadAttributes().hasFinishedPart(1))
			return;
		p.getPacketSender().sendInterfaceRemoval();
		p.moveTo(new Position(3361, 5856, p.getIndex() * 4));
		p.setRegionInstance(new RegionInstance(p, RegionInstanceType.NOMAD));
		TaskManager.submit(new Task(1, p, false) {
			int tick = 0;
			@Override
			public void execute() {
				if(tick >= 4) {
					NPC n = new NPC(8527, new Position(p.getPosition().getX(), p.getPosition().getY() - 2, p.getPosition().getZ())).setSpawnedFor(p);
					World.register(n);
					p.getRegionInstance().getNpcsList().add(n);
					n.getCombatBuilder().attack(p);
					n.forceChat("You're no match for me, adventurer!");
					n.performGraphic(new Graphic(1295));
					stop();
				}
				tick++;
			}
		});
	}

	public static void endFight(Player p, boolean killed) {
		if(p.getRegionInstance() != null)
			p.getRegionInstance().destruct();
		p.moveTo(new Position(1889, 3177));
		if(killed) {
			p.restart();
			p.getMinigameAttributes().getNomadAttributes().setPartFinished(1, true);
			DialogueManager.start(p, 53);
			PlayerPanel.refreshPanel(p);
		}
	}

	public static void openQuestLog(Player p) {
		for(int i = 8145; i < 8196; i++)
			p.getPacketSender().sendString(i, "");
		p.getPacketSender().sendInterface(8134);
		p.getPacketSender().sendString(8136, "Close window");
		p.getPacketSender().sendString(8144, ""+questTitle);
		p.getPacketSender().sendString(8145, "");
		int questIntroIndex = 0;
		for(int i = 8147; i < 8147+questIntro.length; i++) {
			p.getPacketSender().sendString(i, "@dre@"+questIntro[questIntroIndex]);
			questIntroIndex++;
		}
		int questGuideIndex = 0;
		for(int i = 8147+questIntro.length; i < 8147+questIntro.length+questGuide.length; i++) {
			if(!p.getMinigameAttributes().getNomadAttributes().hasFinishedPart(questGuideIndex))
				p.getPacketSender().sendString(i, ""+questGuide[questGuideIndex]);
			else
				p.getPacketSender().sendString(i, "@str@"+questGuide[questGuideIndex]+"");
			questGuideIndex++;
		}
		if(p.getMinigameAttributes().getNomadAttributes().hasFinishedPart(1))
			p.getPacketSender().sendString(8147+questIntro.length+questGuide.length, "@dre@Quest complete!");
	}

	public static String getQuestTabPrefix(Player player) {
		if(player.getMinigameAttributes().getNomadAttributes().hasFinishedPart(0) && !player.getMinigameAttributes().getNomadAttributes().hasFinishedPart(1)) {
			return "@yel@";
		} else if(player.getMinigameAttributes().getNomadAttributes().hasFinishedPart(1)) {
			return "@gre@";
		}
		return "@red@";
	}

	private static final String questTitle = "Nomad's Requiem";
	private static final String[] questIntro ={
		"Nomad is searching for a worthy opponent.", 
		"Are you eligible for the job?",
		"",
	};
	private static final String[] questGuide ={
		"Talk to Nomad and accept his challenge to a fight.",
		"Defeat Nomad."
	};
}
