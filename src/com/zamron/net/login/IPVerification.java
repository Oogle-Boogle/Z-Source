package com.zamron.net.login;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

import com.zamron.GameSettings;
import com.zamron.world.content.discord.DiscordMessenger;
import com.zamron.world.entity.impl.player.Player;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public final class IPVerification {


	public static void manualIPCheck(Player initiator, Player suspect) {
		try {
			//Getting the target IP Address as a string to use in the URL
			String IP = suspect.getHostAddress();
			//The URl that will show the required information.
			URL url = new URL("https://ipqualityscore.com/api/json/ip/njOmfDZj2CChqDrb3sFnT1NcQRrxn43l/" + IP);

			HttpURLConnection conn = (HttpURLConnection) url.openConnection(); //Connecting to the URL
			conn.setRequestMethod("GET"); //Sending a 'GET' Request to the URL. This is the same thing that happens when you open a webpage on your PC
			conn.connect(); //Connecting to the session

			//Getting Response Code
			int responseCode = conn.getResponseCode();

			//Response code 200 means a webpage responded to our GET request successfully.
			if (responseCode != 200) {
				throw new RuntimeException("HttpResponseCode: " + responseCode);
			} else {
				Scanner sc = new Scanner(url.openStream()); //Scanner is a class which reads the results from a website response
				StringBuilder inline = new StringBuilder(); //Setting a blank string. We will add the responses to the string to build up our results
				while(sc.hasNext()) //While there are still responses, we add them to the blank string 'inline'
				{
					inline.append(sc.nextLine()); //Adding the response to nextline blank string
				}
				//System.out.println(inline); //If you want to see the full result in the console, uncomment this line (Good for debugging)
				sc.close(); //Closes the connection to prevent mem leaks!

				JSONParser parse = new JSONParser(); //Starting a new JSONParses session
				JSONObject obj = (JSONObject) parse.parse(inline.toString()); //Creating a new JSONObject and setting it equal to everything inside 'inline'

				//Using data from JSON
				String ip = obj.get("host").toString();
				boolean VPN = obj.get("vpn").toString().equals("true");
				boolean tor = obj.get("tor").toString().equals("true");
				boolean proxy = obj.get("proxy").toString().equals("true");
				boolean bot_status = obj.get("bot_status").toString().equals("true");
				int fraudScore = Integer.parseInt(obj.get("fraud_score").toString());
				String country = obj.get("country_code").toString();
				String region = obj.get("region").toString();
				String city = obj.get("city").toString();
				String isp = obj.get("ISP").toString();

				//Adding asterisks either side to make them bold in Discord
				String susUser = "**" + suspect.getUsername() + "**";
				String initUser = "**" + initiator.getUsername() + "**";

				//Formatting the results. \n is an expression that creates a new line. Purely for formatting because I have aids level OCD
				String results = "IP Scan on " + susUser + "\nIP: " + ip + "\nVPN: " + VPN + "\nTor: " + tor + "\nProxy: " + proxy + "\nBot Status: " + bot_status + "\nFraud Score: " + fraudScore + "\nCountry: " + country + "\nRegion: " + region + "\nCity: " + city + "\nISP: " + isp + "\nScan Requested by: " + initUser;
				System.out.println(results);
				DiscordMessenger.sendStaffMessage(results); //This is the location that I sent the results to, this should be changed to whatever is most relevant for you!
			}
		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}
	}

	public static boolean autoIPCheck(Player suspect, String IP) {
		boolean shouldBlock = false;

		try {
			//The URl that will show the required information.
			URL url = new URL("https://ipqualityscore.com/api/json/ip/njOmfDZj2CChqDrb3sFnT1NcQRrxn43l/" + IP);

			HttpURLConnection conn = (HttpURLConnection) url.openConnection(); //Connecting to the URL
			conn.setRequestMethod("GET"); //Sending a 'GET' Request to the URL. This is the same thing that happens when you open a webpage on your PC
			conn.connect(); //Connecting to the session

			//Getting Response Code
			int responseCode = conn.getResponseCode();

			//Response code 200 means a webpage responded to our GET request successfully.
			if (responseCode != 200) {
				throw new RuntimeException("HttpResponseCode: " + responseCode);
			} else {
				Scanner sc = new Scanner(url.openStream()); //Scanner is a class which reads the results from a website response
				StringBuilder inline = new StringBuilder(); //Setting a blank string. We will add the responses to the string to build up our results
				while(sc.hasNext()) {//While there are still responses, we add them to the blank string 'inline'
					inline.append(sc.nextLine()); //Adding the response to nextline blank string
				}
				//System.out.println(inline); //If you want to see the full result in the console, uncomment this line (Good for debugging)
				sc.close(); //Closes the connection to prevent mem leaks!

				JSONParser parse = new JSONParser(); //Starting a new JSONParses session
				JSONObject obj = (JSONObject) parse.parse(inline.toString()); //Creating a new JSONObject and setting it equal to everything inside 'inline'

				//Using data from JSON
				String ip = obj.get("host").toString();
				boolean VPN = obj.get("vpn").toString().equals("true");
				boolean tor = obj.get("tor").toString().equals("true");
				boolean proxy = obj.get("proxy").toString().equals("true");
				boolean bot_status = obj.get("bot_status").toString().equals("true");
				int fraudScore = Integer.parseInt(obj.get("fraud_score").toString());
				String country = obj.get("country_code").toString();
				String region = obj.get("region").toString();
				String city = obj.get("city").toString();
				String isp = obj.get("ISP").toString();
				//Adding asterisks either side to make them bold in Discord
				String susUser = "**" + suspect.getUsername() + "**";
				//Formatting the results. \n is an expression that creates a new line. Purely for formatting because I have aids level OCD
				String results = "LOGON BLOCKED FOR " + susUser + "\nIP: " + ip +"\nVPN: " + VPN + "\nTor: " + tor + "\nProxy: " + proxy + "\nBot Status: " + bot_status + "\nFraud Score: " + fraudScore + "\nCountry: " + country + "\nRegion: " + region + "\nCity: " + city + "\nISP: " + isp;

				if ((VPN || tor || (proxy && fraudScore > 90) || bot_status)
						&& !GameSettings.DEVELOPERSERVER
						&& !suspect.getRights().isSeniorStaff()
						&& !suspect.getRights().isMember()) { //Only sending the info to staff if these prereqs are met
					DiscordMessenger.sendStaffMessage(results); //This is the location that I sent the results to, this should be changed to whatever is most relevant for you!
					shouldBlock = true;
				}
			}
		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}
		return shouldBlock;
	}
}
