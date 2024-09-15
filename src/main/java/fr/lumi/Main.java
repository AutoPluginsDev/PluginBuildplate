package fr.lumi;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import fr.lumi.Commandes.*;
import fr.lumi.Metrics.Metrics;
import fr.lumi.FileVerifiers.ConfigFileVerification;
import fr.lumi.FileVerifiers.LangFileVerification;
import fr.lumi.Util.*;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.Bukkit;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.logging.Level;
import com.google.gson.JsonParser;
import java.util.Objects;

public final class Main extends JavaPlugin {

    private final String[] Logo = {
            "&e&9     &6__     __ &e ",
            "&e&9 /\\ &6/  |\\/||  \\&e|  &9Agalia &aVersion &e" + this.getDescription().getVersion(),
            "&e&9/--\\&6\\__|  ||__/&e|  &8running on bukkit - paper",
            ""};

    private void printLogo() {
        for (String s : Logo)//print the logo
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', s));
    }


    boolean papiPresent = false;

    public boolean isPapiPresent() {
        return papiPresent;
    }

    FileConfiguration config = getConfig();

    static String lastTagMessage = "";
    public static boolean isNewVersionAvailable() {
        return !lastTagMessage.isEmpty();
    }


    //translatefile gestion
    private File Langfile = new File(getDataFolder(), "lang.yml");
    private FileConfiguration Langconfig = YamlConfiguration.loadConfiguration(Langfile);

    public FileConfiguration getLangConfig() {
        Langconfig = YamlConfiguration.loadConfiguration(getLangFile());
        return Langconfig;
    }


    public File getLangFile() {
        Langfile = new File(getDataFolder(), "lang.yml");
        return Langfile;
    }

    public File getConfigFile() {
        Langfile = new File(getDataFolder(), "config.yml");
        return Langfile;
    }


    public void addBstatsMetrics() {
        int pluginId = 21737;
        Metrics metrics = new Metrics(this, pluginId);
    }


    private Utilities m_ut;
    public Utilities getUt() {
        return m_ut;
    }


    //FileVerifiers
    private LangFileVerification LangVerif = new LangFileVerification(this);
    private ConfigFileVerification ConfigVerif = new ConfigFileVerification(this);
    public void init() {

        m_ut = new Utilities(this);
        config = getConfig();
        Langfile = new File(getDataFolder(), "lang.yml");
        Langconfig = YamlConfiguration.loadConfiguration(Langfile);
        //FileVerifiers
        LangVerif = new LangFileVerification(this);
        ConfigVerif = new ConfigFileVerification(this);
    }


    public String callGithubForTag() {
        StringBuilder response = new StringBuilder();
        try {
            // Make HTTP GET request
            URL url = new URL("https://api.github.com/repos/AutoPluginsDev/PluginBuildplate/tags");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");

            // Check response code
            if (connection.getResponseCode() != 200) {
                throw new IOException("Failed to get response from GitHub API");
            }

            // Read response
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();
        } catch (IOException e) {
            getLogger().log(Level.WARNING, "Failed to check for a new version on spigot.", e);
        }
        return response.toString();
    }

    public String VerifyPluginVersion() {
        // check cached msg
        if (!lastTagMessage.isEmpty()) {
            return lastTagMessage;
        }

        String spigotResponse = "";
        String currentVersion = this.getDescription().getVersion();

        String response = callGithubForTag();

        // Parse JSON response
        JsonParser parser = new JsonParser();
        JsonElement jsonElement = parser.parse(response);
        if (jsonElement.isJsonArray()) {
            JsonArray jsonArray = jsonElement.getAsJsonArray();
            if (jsonArray.size() > 0) {
                JsonObject latestTag = jsonArray.get(0).getAsJsonObject();
                spigotResponse = latestTag.get("name").getAsString();
            }
        }

        if (spigotResponse.isEmpty()) {
            lastTagMessage = "&cFailed to check for a new version on spigot.";
        }
        else if (spigotResponse.equals(currentVersion)) {
            lastTagMessage = "&aYou are running the latest version of AutoCommands " + currentVersion + " !";
        }
        else {
            lastTagMessage = "&eAutoCommands &a&l" + spigotResponse + " &eis available! &chttps://www.spigotmc.org/resources/acmd-%E2%8F%B0-%E2%8F%B3-autocommands-1-13-1-20-4.100090";
        }
        return lastTagMessage;
    }

    /**
     * This method is called when the plugin is enabled
     */
    @Override
    public void onEnable() {
        printLogo();
        // verify if the plugin is up-to-date and send a message to the admins
        String broadcastMessage = ChatColor.translateAlternateColorCodes('&', getConfig().getString("Prefix") + VerifyPluginVersion());
        Bukkit.broadcast(broadcastMessage, "bukkit.broadcast.admin");

        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            papiPresent = true;
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("ConsolePrefix") + " &aPlaceholderAPI found"));
        } else {
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("ConsolePrefix") + " &cPlaceholderAPI not found"));
        }

        // add bstat metrics
        addBstatsMetrics();

        long start = System.currentTimeMillis();
        init();

        saveDefaultConfig();
        //getRessourceFile(getLangFile(), "lang.yml", this);
        boolean verified = Load();

        // Registering the event
        Bukkit.getPluginManager().registerEvents(new JoinServer(), this);

        // End of the loading
        long exeTime = System.currentTimeMillis() - start;
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("ConsolePrefix") + " &aOn (took " + exeTime + " ms)"));
    }

    public boolean verifyFiles() {
        boolean verified = false;
        verified = ConfigVerif.Verif();
        verified = verified && LangVerif.Verif();
        ConfigUtil.mergeConfig(this, "lang.yml", getLangFile());
        ConfigUtil.mergeConfig(this, "config.yml", getConfigFile());

        return verified;
    }

    @Override
    public void onDisable() {
        Unload();
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("ConsolePrefix") + " &cOff"));
    }

    public boolean Load() {
        boolean verified = false;

        verified = verifyFiles();
        if (!verified) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("ConsolePrefix") + "&4Ignore these errors if this is the first time you are running the plugin."));
        }

        reloadConfig();
        config = getConfig();
        Objects.requireNonNull(this.getCommand("ahelp")).setExecutor(new CommandRunnerHelp(this));
        Objects.requireNonNull(this.getCommand("agalia")).setExecutor(new CommandRunnerCommand(this));
        Objects.requireNonNull(this.getCommand("areload")).setExecutor(new CommandRunnerReload(this));
        Objects.requireNonNull(this.getCommand("aTime")).setExecutor(new CommandRunnerTime(this));

        return verified;
    }

    public void Unload() {
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes(
                '&', config.getString("ConsolePrefix") + " &cUnloaded"));
    }
}
