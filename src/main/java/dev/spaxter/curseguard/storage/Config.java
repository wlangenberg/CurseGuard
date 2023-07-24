package dev.spaxter.curseguard.storage;

import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashMap;
import java.util.Map;

public class Config {

    public static FileConfiguration config;

    public static void fixMissingDefaults() {
        Map<String, Object> configDefaults = new HashMap<>(config.getValues(true));
        config.options().copyDefaults(true);

        configDefaults.putIfAbsent("version", 1.0);
        configDefaults.putIfAbsent("debug", false);
        configDefaults.putIfAbsent("ignore-spaces", true);
        configDefaults.putIfAbsent("censor-character", "*");
        configDefaults.putIfAbsent("partial-censor", false);
        configDefaults.putIfAbsent("notify-staff", true);
        configDefaults.putIfAbsent("developer-mode", false);

        config.addDefaults(configDefaults);
    }
}
