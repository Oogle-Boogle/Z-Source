package com.zamron.world.content;

import com.zamron.util.Misc;
import com.zamron.world.World;
import com.zamron.world.entity.impl.player.Player;

public class TriviaBot
{
	public static final int TIMER = 1000; //1800
	public static int botTimer = TIMER;

	public static int answerCount;
	public static String firstPlace;
	//public static String secondPlace;
	//public static String thirdPlace;

	//public static List<String> attempts = new ArrayList<>();

	public static void sequence() {

		if (botTimer > 0)
			botTimer--;
		if (botTimer <= 0) {
			botTimer = TIMER;
			didSend = false;
			askQuestion();
		}
	}

	private static final String[][] TRIVIA_DATA =
			{
					{ "<col=6666FF> [ Trivia ]:</col> @red@What is a virus but also a beer?", "corona" },
					{ "<col=6666FF> [ Trivia ]:</col> @red@What country has the world's largest population?", "china" },
					{ "<col=6666FF> [ Trivia ]:</col> @red@What is the cube root of 729?", "9" },
					{ "<col=6666FF> [ Trivia ]:</col> @red@What is the spiciest pepper in the world?", "carolina reaper" },
					{ "<col=6666FF> [ Trivia ]:</col> @red@Who is the founder of Amazon?", "Jeff Bezos" },
					{ "<col=6666FF> [ Trivia ]:</col> @red@What skill allows you to make armour and weapons?", "smithing" },
					{ "<col=6666FF> [ Trivia ]:</col> @red@What rank is Oogle?", "developer" },
					{ "<col=6666FF> [ Trivia ]:</col> @red@What is the name of this server?", "Zamron" },
					{ "<col=6666FF> [ Trivia ]:</col> @red@What is the armour set you get when you start the server?", "starter" },
					{ "<col=6666FF> [ Trivia ]:</col> @red@What skill has a magnifying glass?", "scavenging" },
					{ "<col=6666FF> [ Trivia ]:</col> @red@What skill has a wolf?", "summoning" },
					{ "<col=6666FF> [ Trivia ]:</col> @red@What is the form of currency on Zamron?", "1b coins" },
					{ "<col=6666FF> [ Trivia ]:</col> @red@How many minutes game-played do you need to participate in raids?", "120" },
					{ "<col=6666FF> [ Trivia ]:</col> @red@Who is the discord owner?", "Retro" },
					{ "<col=6666FF> [ Trivia ]:</col> @red@What is the Dzanth KC required to kill King Kong?", "750" },
					{ "<col=6666FF> [ Trivia ]:</col> @red@What is Luminitous Warrior armour called?", "luminita" },
					{ "<col=6666FF> [ Trivia ]:</col> @red@What is the last NPC called in ::train?", "martian" },
					{ "<col=6666FF> [ Trivia ]:</col> @red@When many hours does it take for the daily NPC tasks to reset?", "24" },
			};

	public static boolean acceptingQuestion() {
		return !currentQuestion.equals("");
	}
	public static boolean didSend = false;
	public static String currentQuestion;
	private static String currentAnswer;

	public static void attemptAnswer(Player p, String attempt) {

		if (!currentQuestion.equals("") && attempt.replaceAll("_", " ").equalsIgnoreCase(currentAnswer)) {

			if (answerCount == 0) {
				answerCount++;
				int pts = 10;
				pts += p.getRights().getBonusTriviaPts();
				p.getPointsHandler().incrementTriviaPoints(pts);
				p.getPointsHandler().refreshPanel();
				firstPlace = p.getUsername();
				World.sendMessageNonDiscord("<col=AD096E>[Trivia] </col>" + firstPlace + "@bla@ answered first and received 10 points!");
				currentQuestion = "";
				didSend = false;
				botTimer = TIMER;
				answerCount = 0;
			}
		} else {
			if (attempt.contains("question") || attempt.contains("repeat")) {
				p.getPacketSender().sendMessage("@bla@" + (currentQuestion));
				return;
			}

			p.getPacketSender().sendMessage("<col=AD096E>[Trivia] </col>@bla@ Sorry! Wrong answer! The current question is:");
			p.getPacketSender().sendMessage("@bla@ " + (currentQuestion));
		}

	}
	private static void askQuestion()
	{
		for (int i = 0; i < TRIVIA_DATA.length; i++) 
		{
			if (Misc.getRandom(TRIVIA_DATA.length - 1) == i) 
			{
				if (!didSend) 
				{
					didSend = true;
					currentQuestion = TRIVIA_DATA[i][0];
					currentAnswer = TRIVIA_DATA[i][1];
					World.sendMessageNonDiscord(currentQuestion);
				}
			}
		}
	}

}
