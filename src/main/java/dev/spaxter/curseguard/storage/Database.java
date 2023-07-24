package dev.spaxter.curseguard.storage;

import dev.spaxter.curseguard.logging.Logger;
import dev.spaxter.curseguard.models.QueryResult;
import dev.spaxter.curseguard.util.Ansi;

import java.sql.*;

public class Database {

    public static Connection databaseConnection;

    public static void initDatabaseConnection(final String folder) throws SQLException {
        databaseConnection = DriverManager.getConnection("jdbc:sqlite:" + folder + "curseguard.sql");
        databaseConnection.setAutoCommit(false);
        Logger.debug("Initialized SQLite database");
    }

    public static void closeDatabaseConnection() {
        try {
            if (!databaseConnection.isClosed()) {
                databaseConnection.close();
                Logger.debug("Closed SQLite database connection");
            }
        } catch (SQLException ignored) {};
    }

    public static void createInitialTables() {
        try {
            executeUpdate(
                "CREATE TABLE IF NOT EXISTS word_list" +
                    "(id blob PRIMARY KEY NOT NULL, word text NOT NULL UNIQUE, \"action\" text CHECK(UPPER(\"action\") IN ('BLOCK', 'CENSOR')) NOT NULL)"
            );
            executeUpdate(
                "CREATE TABLE IF NOT EXISTS exempt_words" +
                    "(id blob PRIMARY KEY NOT NULL, word text NOT NULL UNIQUE)"
            );
        } catch (SQLException e) {
            Logger.error("Failed to create required database tables: " + e.getMessage());
        }
    }

    public static void executeUpdate(final String sql, String[] parameters) throws SQLException {
        PreparedStatement statement = databaseConnection.prepareStatement(sql);
        for (int i = 0; i < parameters.length; i++) {
            statement.setString(i + 1, parameters[i]);
        }
        Logger.debug("Executing update statement: " + Ansi.BLUE + statement);
        statement.executeUpdate();
        databaseConnection.commit();
    }

    public static void executeUpdate(final String sql) throws SQLException {
        PreparedStatement statement = databaseConnection.prepareStatement(sql);
        Logger.debug("Executing update statement: " + Ansi.BLUE + statement);
        statement.executeUpdate();
        databaseConnection.commit();
    }

    public static QueryResult executeFetch(final String sql, String[] parameters) throws SQLException {
        PreparedStatement statement = databaseConnection.prepareStatement(sql);
        for (int i = 0; i < parameters.length; i++) {
            statement.setString(i + 1, parameters[i]);
        }
        Logger.debug("Executing fetch statement: " + Ansi.BLUE + statement);
        ResultSet results = statement.executeQuery();

        return new QueryResult(results);
    }

    public static QueryResult executeFetch(final String sql) throws SQLException {
        PreparedStatement statement = databaseConnection.prepareStatement(sql);
        Logger.debug("Executing fetch statement: " + Ansi.BLUE + statement);
        ResultSet results = statement.executeQuery();

        return new QueryResult(results);
    }

}
