package fr.lumi.Util;

import fr.lumi.Main;
import org.bukkit.ChatColor;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.entity.Player;

public class Utilities {

    private Main plugin;

    public Utilities(Main plg) {
        plugin = plg;
    }

    public String replacePlaceHolders(String s) {
        try {

        } catch (Exception e) {
            String error = "Error while replacing placeholders. Have a look to you language file";
            plugin.getLogger().info(error);
            plugin.getLogger().info(e.getMessage());
            //broadcast to all admins
            plugin.getServer().broadcast(error, "acmd.admin");
        }
        return s;
    }

    public String replacePlaceHoldersPluginVars(String s) {
        s = PapiReplace(null, s);
        return s;
    }


    public String replacePlaceHoldersForPlayer(String s, Player player) {
        s = replacePlaceHolders(s);

        s = PapiReplace(player, s);
        return ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("Prefix") + s);
    }

    public String replacePlaceHoldersForPlayerPlgVar(String s) {
        s = replacePlaceHoldersPluginVars(s);
        return ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("Prefix") + s);
    }

    public String replacePlaceHoldersForConsolePlgVar(String s) {
        s = replacePlaceHoldersPluginVars(s);
        return ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("ConsolePrefix") + s);
    }

    public String PapiReplace(Player player, String s) {
        if (plugin.isPapiPresent())
            s = PlaceholderAPI.setPlaceholders(player, s);
        return s;
    }


}
