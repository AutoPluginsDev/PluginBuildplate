package fr.lumi.FileVerifiers;

import fr.lumi.Main;

import java.util.HashMap;

public class ConfigFileVerification extends cfgFileVerification {

    private HashMap<String, Object> keys = new HashMap<>();


    public ConfigFileVerification(Main plg) {
        super(plg, plg.getConfig(), "| ERROR IN CONFIG.YML |", "config.yml");

        keys.put("ConfigVersion", 2);
        keys.put("ConsolePrefix", "&eAgalia |");
        keys.put("Prefix", "&eacmd | ");

        super.setKeys(keys);
    }

    @Override
    public void savemodif() {
        //plugin.getConfig().set("ConfigVersion",2);
        //plugin.saveConfig();
    }
}