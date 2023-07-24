package dev.spaxter.curseguard.storage;

import dev.spaxter.curseguard.core.Words;
import dev.spaxter.curseguard.logging.Logger;
import dev.spaxter.curseguard.models.Action;
import dev.spaxter.curseguard.models.QueryResult;
import dev.spaxter.curseguard.util.Ansi;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class GlobalMemory {

    public static HashMap<String, Action> wordActions = new HashMap<String, Action>();
    public static HashMap<UUID, Integer> rateLimitMap = new HashMap<>();
    public static HashMap<UUID, Long> timeLeftMap = new HashMap<>();
    public static ArrayList<String> exemptWords = new ArrayList<String>();
    public static char censorCharacter;

    public static void addBlockedWord(final String word, final Action action) {
        Words.addWord(word, action);
        wordActions.put(word, action);
    }

    public static void removeBlockedWord(final String word) {
        Words.removeWord(word);
        wordActions.remove(word);
    }

    public static void loadWordList() {
        try {
            QueryResult res = Database.executeFetch("SELECT * FROM word_list;");
            for (ArrayList<Map<String, Object>> rows : res) {
                for (Map<String, Object> row : rows) {
                    String word = (String)row.get("word");
                    Action action = Action.fromString((String)row.get("action"));
                    wordActions.put(word.toLowerCase(), action);
                }
            }
            Logger.debug("Loaded " + Ansi.BLUE + wordActions.keySet().size() + " words from the wordlist");
        } catch (SQLException e) {
            Logger.error("Failed to load wordlist from database: " + e.getMessage());
        }
    }

    public static void loadExemptWords() {
        try {
            QueryResult res = Database.executeFetch("SELECT * FROM exempt_words;");
            for (ArrayList<Map<String, Object>> rows : res) {
                for (Map<String, Object> row : rows) {
                    String word = (String)row.get("word");
                    exemptWords.add(word.toLowerCase());
                }
            }
            Logger.debug("Loaded " + Ansi.BLUE + exemptWords.size() + " exempt words");
        } catch (SQLException e) {
            Logger.error("Failed to load wordlist from database: " + e.getMessage());
        }
    }

    public static void setCensorCharacter(final Character character) {
        censorCharacter = character;
    }
}
