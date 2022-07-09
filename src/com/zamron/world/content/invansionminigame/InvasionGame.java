package com.zamron.world.content.invansionminigame;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import com.zamron.model.Position;
import com.zamron.util.RandomUtility;
import com.zamron.world.World;
import com.zamron.world.entity.impl.npc.NPC;
import com.zamron.world.entity.impl.player.Player;

public class InvasionGame {

	public InvasionGame(Player player) {
	}

	private int cityIndex;

	public int getCityIndex() {
		return cityIndex;
	}

	private List<Position> positionData = new ArrayList<>();
	// private List<Position> positionData2 = new ArrayList<>();
	// private List<Position> positionData3 = new ArrayList<>();
	int index = 0;

	public void parsePositionData() {

		Path path = Paths.get("./src/com/Zamron/world/content/invansionminigame/posdata.txt");
		try (Stream<String> lines = Files.lines(path)) {

			lines.forEach(line -> {

				String[] data = line.split(",");
				if (data == null) {
					return;
				}
				int x = Integer.parseInt(data[0].trim());
				int y = Integer.parseInt(data[1].trim());
				positionData.add(new Position(x, y));
			});

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void setCityIndex(int index) {
		this.cityIndex = index;
	}

	private void chooseCity() {

		int random = RandomUtility.inclusiveRandom(1, 3);

		this.setCityIndex(random);

	}
	
	private Map<Player, Integer> top3;
	
	private List<NPC> spawnedNpcs = new ArrayList<>();

	public void initializeEvent() {

		chooseCity();

		//System.out.println("Chosen: " + cityIndex);

		switch (cityIndex) {

		case 1:
			initializeLumbridge();
			break;
		case 2:
			initializeVarrock();
			break;

		case 3:
			initializeFalador();
			break;
		}
	}

	public void initAll() {
		initializeLumbridge();
		initializeVarrock();
		initializeFalador();
	}

	private void initializeLumbridge() {

		for (int i = 0; i < 93; i++) {
			Position pos = positionData.get(i);
			NPC npc = new NPC(131, pos);
			spawnedNpcs.add(npc);
			World.register(npc);

		}
	}

	private void initializeVarrock() {

		for (int i = 93; i < 282; i++) {
			Position pos = positionData.get(i);
			NPC npc = new NPC(131, pos);
			spawnedNpcs.add(npc);
			World.register(npc);
		}

	}

	private void initializeFalador() {

		for (int i = 282; i < positionData.size(); i++) {
			Position pos = positionData.get(i);
			NPC npc = new NPC(131, pos);
			spawnedNpcs.add(npc);
			World.register(npc);
		}
	}

	public void stopEvent() {
		spawnedNpcs.forEach(x -> {
			World.deregister(x);
		});

		spawnedNpcs.clear();
	}



}
