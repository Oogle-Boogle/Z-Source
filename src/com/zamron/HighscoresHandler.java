package com.zamron;

import com.zamron.model.PlayerRights;
import com.zamron.model.Skill;
import com.zamron.mysql.MySQLController;
import com.zamron.mysql.MySQLDatabase;
import com.zamron.world.entity.impl.player.Player;

import java.sql.PreparedStatement;


public class HighscoresHandler {

    public static void save(Player player) {
        if(!GameSettings.MYSQL_ENABLED) {
            return;
        }
        if(player.getRights() == PlayerRights.DEVELOPER || player.getRights() == PlayerRights.ADMINISTRATOR || player.getRights() == PlayerRights.OWNER)
            return;
        if(player.getSkillManager().getTotalLevel() <= 34)
            return;
        MySQLDatabase highscores = MySQLController.getController().getDatabase(MySQLController.Database.HIGHSCORES);
        if(!highscores.active || highscores.getConnection() == null) {
            return;
        }
        GameServer.getLoader().getEngine().submit(() -> {
            try {
                PreparedStatement preparedStatement = highscores.getConnection().prepareStatement("DELETE FROM hs_users WHERE USERNAME = ?");
                preparedStatement.setString(1, player.getUsername());
                preparedStatement.executeUpdate();
                preparedStatement = highscores.getConnection().prepareStatement("INSERT INTO hs_users (username,rights,overall_xp,attack_xp,defence_xp,strength_xp,constitution_xp,ranged_xp,prayer_xp,magic_xp,cooking_xp,woodcutting_xp,fletching_xp,fishing_xp,firemaking_xp,crafting_xp,smithing_xp,mining_xp,herblore_xp,agility_xp,thieving_xp,slayer_xp,farming_xp,runecrafting_xp,hunter_xp,construction_xp,summoning_xp,dungeoneering_xp) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
                preparedStatement.setString(1, player.getUsername());
                preparedStatement.setInt(2, player.getRights().ordinal());
                preparedStatement.setLong(3, player.getSkillManager().getTotalExp());
                for (int i = 4; i <= 28; i++) {
                    preparedStatement.setInt(i, player.getSkillManager().getExperience(Skill.forId(i - 4)));
                }
                preparedStatement.executeUpdate();
            } catch(Exception e) {
                e.printStackTrace();
            }
        });
    }
}