package dev.spaxter.curseguard.core;

import dev.spaxter.curseguard.logging.Logger;
import dev.spaxter.curseguard.models.Action;
import dev.spaxter.curseguard.storage.Database;

import java.sql.SQLException;
import java.util.UUID;

public class Words {

    public static void addWord(String word, Action action) {
        UUID wordId = UUID.randomUUID();
        try {
            Database.executeUpdate(
                "INSERT INTO word_list VALUES (CAST(? AS TEXT), CAST(? AS TEXT), CAST(? as TEXT))",
                new String[]{wordId.toString(), word.toLowerCase(), action.name()}
            );
        } catch (SQLException e) {
            Logger.error("Failed to add word to database: " + e.getMessage());
        }
    }

    public static void removeWord(String word) {
        try {
            Database.executeUpdate(
            "DELETE FROM word_list WHERE word = (CAST(? AS TEXT))",
                new String[]{word.toLowerCase(),}
            );
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
