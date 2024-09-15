package fr.lumi.Commandes;
import fr.lumi.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import java.util.ArrayList;
import java.util.List;


public class CommandRunnerCommand implements CommandExecutor, TabCompleter {

    private Main plugin;

    public CommandRunnerCommand(Main plg) {
        plugin = plg;

    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {
        List<String> l = new ArrayList<>();
        return l;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(plugin.getUt().replacePlaceHoldersForPlayerPlgVar("Hello, Agalia !"));
            return true;
        }
        return true;
    }

}