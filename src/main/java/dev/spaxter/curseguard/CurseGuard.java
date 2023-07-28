package dev.spaxter.curseguard;

import dev.spaxter.curseguard.command.*;
import dev.spaxter.curseguard.events.OnChat;
import dev.spaxter.curseguard.events.OnItemNameChange;
import dev.spaxter.curseguard.events.OnSignPlace;
import dev.spaxter.curseguard.logging.Logger;
import dev.spaxter.curseguard.storage.Config;
import dev.spaxter.curseguard.storage.Database;
import dev.spaxter.curseguard.storage.GlobalMemory;
import dev.spaxter.curseguard.util.Ansi;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.SQLException;

public class CurseGuard extends JavaPlugin {

    public static FileConfiguration languageConfig;
    public static Float version = 1.0f;

    @Override
    public void onEnable() {
        // Plugin startup logic
        this.saveDefaultConfig();
        Config.config = this.getConfig();
        // Config.fixMissingDefaults();
        this.saveConfig();
        this.reloadConfig();

        languageConfig = this.loadLanguageConfig();

        Logger.setLogger(this.getLogger());
        Logger.debug("Debug logs are enabled. Set 'debug: false' in config.yml to disable these logs.");

        GlobalMemory.setCensorCharacter(Config.config.getString("censor-character", "*").charAt(0));

        Logger.debug("Registering events...");
        this.registerEventListeners();

        try {
            String databaseFolderPath = createDataFolder();
            Database.initDatabaseConnection(databaseFolderPath + File.separatorChar);
            Database.createInitialTables();
        } catch (SQLException e) {
            Logger.log(Ansi.RED + "Database init failed: " + e.getMessage() + Ansi.RESET);
        }

        GlobalMemory.loadWordList();
        GlobalMemory.loadExemptWords();

        Logger.debug("Registering plugin commands...");
        registerCommands();
        Logger.debug("Command registration complete!");

        Logger.log(Ansi.RED + "Curse" + Ansi.CYAN + "Guard" + Ansi.GREEN + " is ready! Use " + Ansi.CYAN + "/cg help"
                + Ansi.GREEN + " to see available commands." + Ansi.RESET);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        Database.closeDatabaseConnection();
    }

    private String createDataFolder() {
        Logger.debug("Attempting to create data folder...");
        File databaseDir = new File(this.getDataFolder().getAbsolutePath() + File.separatorChar + ".data");
        if (!databaseDir.exists()) {
            boolean succeeded = databaseDir.mkdirs();
            if (!succeeded) {
                throw new RuntimeException("Failed to create data folder.");
            } else {
                Logger.debug("Successfully created data folder at " + Ansi.BLUE + databaseDir.getAbsolutePath());
                try {
                    Files.setAttribute(databaseDir.toPath(), "dos:hidden", true);
                } catch (IOException ignored) {}
            }
        } else {
            Logger.debug("Data folder already exists, will not try to create a new one.");
        }
        return databaseDir.getAbsolutePath();
    }

    private YamlConfiguration loadLanguageConfig() {
        File languageFile = new File(this.getDataFolder().getAbsolutePath() + File.separatorChar + "language.yml");
        if (!languageFile.exists()) {
            try {
                if (!languageFile.createNewFile()) {
                    Logger.debug("Failed to create 'language.yml'");
                }
            } catch (IOException e) {
                Logger.error("Failed to create 'language.yml': " + e.getMessage());
                Bukkit.getServer().shutdown();
            }
        }

        this.saveResource("language.yml", true);

        return YamlConfiguration.loadConfiguration(languageFile);
    }

    private void registerCommands() {
        // Init sub commands
        MainCommand.commandMap.put("addword", new AddWordCommand());
        MainCommandTabComplete.tabCompleteMap.put("addword", new AddWordTabComplete());
        MainCommand.commandMap.put("wordlist", new ListWordsCommand());

        MainCommand.commandMap.put("removeword", new RemoveWordCommand());
        MainCommandTabComplete.tabCompleteMap.put("removeword", new RemoveWordTabComplete());

        MainCommand.commandMap.put("help", new HelpCommand());

        // Register main command
        PluginCommand mainCommand = this.getCommand("cg");
        assert mainCommand != null;
        mainCommand.setExecutor(new MainCommand());
        mainCommand.setTabCompleter(new MainCommandTabComplete());
    }

    private void registerEventListeners() {
        PluginManager pluginManager = this.getServer().getPluginManager();
        if (Config.config.getBoolean("filters.chat", true)) {
            pluginManager.registerEvents(new OnChat(), this);
            Logger.log("Chat filter: " + Ansi.GREEN + "enabled");
        } else {
            Logger.log("Chat filter: " + Ansi.RED + "disabled");
        }
        if (Config.config.getBoolean("filters.signs", true)) {
            pluginManager.registerEvents(new OnSignPlace(), this);
            Logger.log("Sign filter: " + Ansi.GREEN + "enabled");
        } else {
            Logger.log("Sign filter: " + Ansi.RED + "disabled");
        }
        if (Config.config.getBoolean("filters.item-names", true)) {
            pluginManager.registerEvents(new OnItemNameChange(), this);
            Logger.log("Item name filter: " + Ansi.GREEN + "enabled");
        } else {
            Logger.log("Item name filter: " + Ansi.RED + "disabled");
        }
    }

}
