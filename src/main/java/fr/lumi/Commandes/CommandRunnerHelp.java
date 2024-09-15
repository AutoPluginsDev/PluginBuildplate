package fr.lumi.Commandes;

import fr.lumi.Main;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class CommandRunnerHelp implements CommandExecutor, TabCompleter {


    Main plugin;

    public CommandRunnerHelp(Main plg) {

        plugin = plg;
    }


    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {
        List<String> l = new ArrayList<>();
        if (cmd.getName().equalsIgnoreCase("ahelp")) {
            if (sender instanceof Player) {
                List<String> list = new ArrayList<>();

            }
        }
        return l;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            String message =
                    "§e-----------§aAgalia-Help§e--------------\n"
                            + "§6" + ChatColor.translateAlternateColorCodes('&', plugin.VerifyPluginVersion()) + "\n"
                            + "§6Agalia Version : " + plugin.getDescription().getVersion() + "\n"
                            + "§e------------------------------------------\n";

            player.sendMessage(message);
        }
        return true;
    }
}