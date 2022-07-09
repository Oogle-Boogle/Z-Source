package com.zamron.world.content.guidesInterface;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

import com.zamron.GameSettings;
import com.zamron.world.entity.impl.player.Player;

public class GuideBook {
	
	/**
	 * 
	 */

	/**
	 * Store the titles of the guides.
	 */
	public static ArrayList<String> titles = new ArrayList<String>();

	/**
	 * Store the content of the guides.
	 */
	public static ArrayList<String> content = new ArrayList<String>();

	public static void loadGuideDataFile() {
		try {
			content.clear();
			titles.clear();
			BufferedReader file = new BufferedReader(new FileReader(GameSettings.getFileLocationData() + "guide.txt"));
			String line;
			String contentStrings = "";
			while ((line = file.readLine()) != null) {
				if (!line.isEmpty()) {
					if (line.contains("#")) {
						if (!contentStrings.isEmpty()) {
							content.add(contentStrings);
						}
						contentStrings = "";
						titles.add(line.substring(1));
					} else {
						line = line.replace("--", " ");
						contentStrings = contentStrings + line + ";";
					}
				}
			}
			content.add(contentStrings); // Has to be here so the last guide can be saved properly.
			file.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void displayGuideInterface(Player player) {
		for (int index = 0; index < titles.size(); index++) {
			player.getPA().sendFrame126(titles.get(index), 22609 + index);
		}
		for (int i = 0; i <= 41; i++) {
			player.getPA().sendFrame126("", 22558 + i);
		}
		player.getPA().sendFrame126("", 22637);
		player.getPA().sendFrame126("", 22638);
		player.getPA().sendInterface(22550);
	}

	public static boolean isGuideInterfaceButton(Player player, int buttonId) {
		if (buttonId >= 22609 && buttonId <= 22638) {
			for (int i = 0; i <= 41; i++) {
				player.getPA().sendFrame126("", 22558 + i);
			}
			// Pvm Rare Drops.
			if (buttonId == 22609) {
				player.getPA().sendInterface(37600);
				return true;
			}
			int indexButton = (buttonId - 22609);
			if (indexButton > titles.size() - 1) {
				return true;
			}
			if (indexButton > content.size() - 1) {
				return false;
			}
			String[] parseContent = content.get(indexButton).split(";");
			int lastIndexUsed = 0;
			player.getPA().sendFrame126(titles.get(indexButton), 22556);
			for (int index = 0; index < parseContent.length; index++) {
				String string = parseContent[index];
				if (string.isEmpty()) {
					break;
				}
				player.getPA().sendFrame126(string, 22558 + index);
				lastIndexUsed = 22558 + index;
			}
			lastIndexUsed++;
			player.getPA().setTextClicked(22609 + indexButton, true);
		}

		return false;
	}
}

