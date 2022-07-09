package com.zamron;

import com.google.gson.*;
import java.io.*;
public class ResetDonatorPoints {
    @SuppressWarnings({ "unused", "resource" })
	public static void main(String[] args) {
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            try {
                File file = new File("characters/");
                if (file.exists()) {
                    File[] listOfFiles = file.listFiles();
                    if (listOfFiles != null) {
                        for (File listOfFile : listOfFiles) {
                            JsonElement jelement = new JsonParser().parse(new FileReader(listOfFile));
                            JsonObject jobject = jelement.getAsJsonObject();
                            String username = jobject.get("username").getAsString();
                            int donatorPoints = jobject.get("donation-points").getAsInt();
                            if (donatorPoints >= 400) {
                            	PrintWriter toRewrite = new PrintWriter(new FileWriter(username + ".json", false));
                                //System.out.println(username + " - " +donatorPoints);
                            	/*jobject.remove("donation-points");
                                jobject.addProperty("donation-points", 0);
                                String newJSON = gson.toJson(jobject);
                                toRewrite.print(newJSON);
                                toRewrite.flush();
                                toRewrite.close();*/
                            	
                            }
                        }
                    }
                }
            } catch (FileNotFoundException file) {
                file.printStackTrace();
            }

        } catch(IOException io) {
            io.printStackTrace();
        }
        //System.out.println("COMPLETED");
    }
}
