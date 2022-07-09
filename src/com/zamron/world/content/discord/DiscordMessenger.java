package com.zamron.world.content.discord;

import ca.momoperes.canarywebhooks.DiscordMessage;
import ca.momoperes.canarywebhooks.WebhookClient;
import ca.momoperes.canarywebhooks.WebhookClientBuilder;
import ca.momoperes.canarywebhooks.embed.DiscordEmbed;
import com.zamron.GameSettings;
import com.zamron.util.Misc;
import com.zamron.world.entity.impl.player.Player;
import org.json.simple.JSONObject;

import java.awt.*;
import java.net.URI;

@SuppressWarnings("all")


public class DiscordMessenger extends JSONObject {

    private static final long serialVersionUID = 6042467462151070915L;

    private static String rareDrop = "https://discord.com/api/webhooks/934446341436891146/s2PkWSHyUPdqgkEEDXo3xcErc4HwN9ITb1a5okcGv3hy3gE0k-0VWNx0VAWLNyJcP6SQ";
    private static String staffAlerts = "https://discord.com/api/webhooks/934446495254577182/rurbwXUrdtupomWinzp1JhIwMgX4pmm3WquZvfSnNkr3_-2f1it0TWaJX_0eWmKcb_Zi";
    private static String newPlayers = "https://discord.com/api/webhooks/934446654327771136/BEHrQJSDs6HsBH-uDKFNVm-y9hhMEwAJL8gOZbVujPhyjVDO8IRmLpDacRMy9-Arz394";
    private static String inGameMessages = "https://discord.com/api/webhooks/934445397479407666/eHq4TKOb-uLrFWLaTCnQG09Mern9uIDN60ibJKTf_3nivSZ76u_hjiOnPae4pGmCfic3";
    private static String bugChannel = "https://discord.com/api/webhooks/934446741925789697/kPq7zGjLUq_ytttBgls2YiJ9VM6rOhjCMqzAQkNzhWl8u6G5A5SWWTXAf_wIsVggCoSV";
    private static String generalChat = "https://discord.com/api/webhooks/934446836276658238/4vXggZq6L-ZsCEM9gQuI5lvoQLTiKGW9zOYyMxc15YqlWMIVARw5zyN8wBQw_0Hf-Swv";
    private static String donationDeals = "https://discord.com/api/webhooks/950003563650744331/IRVhYCx_sTH-LtXiRVOjRDNCWNCmNrQp3OLWUtzRNdy61BIwHj_uLcUqKb74rA4pSruK";


    public static void sendDonationDeals(String msg) {
        if (GameSettings.DEVELOPERSERVER) {
            return;
        }
        try {

            String webhook = donationDeals;

            WebhookClient client = new WebhookClientBuilder()
                    .withURI(new URI(webhook))
                    .build(); // Create the webhook client

            @SuppressWarnings("unused")
            DiscordEmbed embed = new DiscordEmbed.Builder()
                    .withTitle("Donation Deals") // The title of the embed element
                    .withURL("https://zamron.net") // The URL of the embed element
                    .withColor(Color.RED) // The color of the embed. You can leave this at null for no color
                    .withDescription(msg) // The description of the embed object
                    .build(); // Build the embed element

            DiscordMessage message = new DiscordMessage.Builder("") // The content of the message
                    .withEmbed(embed) // Add our embed object
                    .withUsername("Donations") // Override the username of the bot
                    .build(); // Build the message

            client.sendPayload(message);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void sendBug(String bug, Player player) {
        if (GameSettings.DEVELOPERSERVER) {
            return;
        }
        try {

            String webhook = bugChannel;

            WebhookClient client = new WebhookClientBuilder()
                    .withURI(new URI(webhook))
                    .build(); // Create the webhook client

            DiscordEmbed embed = new DiscordEmbed.Builder()
                    .withTitle("BUG REPORT") // The title of the embed element
                    .withURL("https://zamron.net") // The URL of the embed element
                    .withColor(Color.RED) // The color of the embed. You can leave this at null for no color
                    .withDescription(Misc.stripIngameFormat(bug)) // The description of the embed object
                    .build(); // Build the embed element

            DiscordMessage message = new DiscordMessage.Builder(Misc.stripIngameFormat(bug)) // The content of the message
                    //.withEmbed(embed) // Add our embed object
                    .withUsername("["+player.getUsername()+"] "+player.getPosition()) // Override the username of the bot

                    .build(); // Build the message

            client.sendPayload(message);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void sendRareDrop(String player, String msg) {
        if (GameSettings.DEVELOPERSERVER) {
            return;
        }
        try {

            String webhook = rareDrop;

            WebhookClient client = new WebhookClientBuilder()
                    .withURI(new URI(webhook))
                    .build(); // Create the webhook client

            @SuppressWarnings("unused")
            DiscordEmbed embed = new DiscordEmbed.Builder()
                    .withTitle(player + " has just received..") // The title of the embed element
                    .withURL("https://zamron.net") // The URL of the embed element
                    .withColor(Color.RED) // The color of the embed. You can leave this at null for no color
                    .withDescription(msg) // The description of the embed object
                    .build(); // Build the embed element

            DiscordMessage message = new DiscordMessage.Builder("") // The content of the message
                    .withEmbed(embed) // Add our embed object
                    .withUsername("Rare Drop Alert!") // Override the username of the bot
                    .build(); // Build the message

            client.sendPayload(message);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void sendGeneralChat(String title, String msg) {
        if (GameSettings.DEVELOPERSERVER) {
            return;
        }
        try {

            String webhook = generalChat;

            WebhookClient client = new WebhookClientBuilder()
                    .withURI(new URI(webhook))
                    .build(); // Create the webhook client

            @SuppressWarnings("unused")
            DiscordEmbed embed = new DiscordEmbed.Builder()
                    .withTitle(title) // The title of the embed element
                    .withURL("https://zamron.net") // The URL of the embed element
                    .withColor(Color.RED) // The color of the embed. You can leave this at null for no color
                    .withDescription(msg) // The description of the embed object
                    .build(); // Build the embed element

            DiscordMessage message = new DiscordMessage.Builder("") // The content of the message
                    .withEmbed(embed) // Add our embed object
                    .build(); // Build the message

            client.sendPayload(message);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void sendStaffMessage(String msg) {
        if (GameSettings.DEVELOPERSERVER) {
            return;
        }
        try {

            String webhook = staffAlerts;

            WebhookClient client = new WebhookClientBuilder()
                    .withURI(new URI(webhook))
                    .build(); // Create the webhook client

            @SuppressWarnings("unused")
            DiscordEmbed embed = new DiscordEmbed.Builder()
                    .withTitle("Staff Alert!") // The title of the embed element
                    .withURL("https://Zamron.net") // The URL of the embed element
                    .withColor(Color.RED) // The color of the embed. You can leave this at null for no color
                    .withDescription(msg) // The description of the embed object
                    .build(); // Build the embed element

            DiscordMessage message = new DiscordMessage.Builder("") // The content of the message
                    .withEmbed(embed) // Add our embed object
                    .withUsername("In Game Staff Alert!") // Override the username of the bot
                    .build(); // Build the message

            client.sendPayload(message);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void sendNewPlayer(String msg) {
        if (GameSettings.DEVELOPERSERVER) {
            return;
        }
        try {

            String webhook = newPlayers;

            WebhookClient client = new WebhookClientBuilder()
                    .withURI(new URI(webhook))
                    .build(); // Create the webhook client

            @SuppressWarnings("unused")
            DiscordEmbed embed = new DiscordEmbed.Builder()
                    .withTitle("New Player!") // The title of the embed element
                    .withURL("https://zamron.net") // The URL of the embed element
                    .withColor(Color.RED) // The color of the embed. You can leave this at null for no color
                    .withDescription(msg) // The description of the embed object
                    .build(); // Build the embed element

            DiscordMessage message = new DiscordMessage.Builder("") // The content of the message
                    .withEmbed(embed) // Add our embed object
                    .withUsername("In Game Staff Alert!") // Override the username of the bot
                    .build(); // Build the message

            client.sendPayload(message);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void sendInGameMessage(String msg) {
        if (GameSettings.DEVELOPERSERVER) {
            return;
        }
        try {

            String webhook = inGameMessages;

            WebhookClient client = new WebhookClientBuilder()
                    .withURI(new URI(webhook))
                    .build(); // Create the webhook client

            @SuppressWarnings("unused")
            DiscordEmbed embed = new DiscordEmbed.Builder()
                    .withTitle("In Game Bot!") // The title of the embed element
                    .withURL("https://zamron.net") // The URL of the embed element
                    .withColor(Color.RED) // The color of the embed. You can leave this at null for no color
                    .withDescription(msg) // The description of the embed object
                    .build(); // Build the embed element

            DiscordMessage message = new DiscordMessage.Builder("") // The content of the message
                    .withEmbed(embed) // Add our embed object
                    .withUsername("In Game Bot!") // Override the username of the bot
                    .build(); // Build the message

            client.sendPayload(message);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }






}
