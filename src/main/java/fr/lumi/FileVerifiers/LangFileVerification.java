package fr.lumi.FileVerifiers;

import fr.lumi.Main;

import java.io.IOException;
import java.util.HashMap;

public class LangFileVerification extends cfgFileVerification {

    private HashMap<String, Object> keys = new HashMap<>();


    public LangFileVerification(Main plg) {
        super(plg, plg.getLangConfig(), "| ERROR IN LANG.YML |", "lang.yml");
        keys.put("lang", "en");
        super.setKeys(keys);
    }

    @Override
    public void savemodif() throws IOException {
        plugin.getLangConfig().save(filename);
    }
}
