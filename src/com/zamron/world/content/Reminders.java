package com.zamron.world.content;

import com.zamron.util.Misc;
import com.zamron.util.Stopwatch;
import com.zamron.world.World;

public class Reminders {
	
	
    private static final int TIME = 400000; //5 minutes
	private static Stopwatch timer = new Stopwatch().reset();
	public static String currentMessage;
	
	/*
	 * Random Message Data
	 */
	private static final String[][] MESSAGE_DATA = { 
			{"<img=12><col=eaeaea>[<col=60148a>SERVER<col=eaeaea>]<col=60148a> Join 'Help' CC For Help/Tips!"},
			{"<img=12><col=eaeaea>[<col=60148a>SERVER<col=eaeaea>]<col=60148a> Use the ::help command to ask staff for help"},
			{"<img=12><col=eaeaea>[<col=60148a>SERVER<col=eaeaea>]<col=60148a>Remember to spread the word and invite your friends to play!"},
			{"<img=12><col=eaeaea>[<col=60148a>SERVER<col=eaeaea>]<col=60148a> Donate to help the server grow! ::store"},
			{"<img=12><col=eaeaea>[<col=60148a>SERVER<col=eaeaea>]<col=60148a>Use ::commands to find a list of commands"},
			{"<img=12><col=eaeaea>[<col=60148a>SERVER<col=eaeaea>]<col=60148a> Toggle your client settings to your preference in the wrench tab!"},
			{"<img=12><col=eaeaea>[<col=60148a>SERVER<col=eaeaea>]<col=60148a> Use the command ::drop for drop tables"},
			{"<img=12><col=eaeaea>[<col=60148a>SERVER<col=eaeaea>]<col=60148a>Did you know you can get paid to make videos? PM Oogle"},
			{"<img=12><col=eaeaea>[<col=60148a>SERVER<col=eaeaea>]<col=60148a>Make sure you ::vote everyday for amazing Rewards!"},
			{"<img=12><col=eaeaea>[<col=60148a>SERVER<col=eaeaea>]<col=60148a>You can checkout our donation deals ::donationdeals"},
			{"<img=12><col=eaeaea>[<col=60148a>SERVER<col=eaeaea>]<col=60148a>Remember to complete your daily task as it resets every 24hr!"},
			{"<img=12><col=eaeaea>[<col=60148a>SERVER<col=eaeaea>]<col=60148a>Did you know? Slayer is a great way to earn a passive income!"},
			{"<img=12><col=eaeaea>[<col=60148a>SERVER<col=eaeaea>]<col=60148a>If you have any suggestions post them in our discord ::discord"},
			{"<img=12><col=eaeaea>[<col=60148a>SERVER<col=eaeaea>]<col=60148a>Would you like to see something ingame? Suggest it on ::discord!"},
			
		
	};

	/*
	 * Sequence called in world.java
	 * Handles the main method
	 * Grabs random message and announces it
	 */
	public static void sequence() {
		if(timer.elapsed(TIME)) {
			timer.reset();
			{
				
			currentMessage = MESSAGE_DATA[Misc.getRandom(MESSAGE_DATA.length - 1)][0];
			World.sendMessageNonDiscord(currentMessage);
					
				}
				
			World.savePlayers();
			}
		

          }

}