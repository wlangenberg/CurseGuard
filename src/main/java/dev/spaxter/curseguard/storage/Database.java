package dev.spaxter.curseguard.storage;

import dev.spaxter.curseguard.logging.Logger;
import dev.spaxter.curseguard.models.QueryResult;
import dev.spaxter.curseguard.util.Ansi;

import java.sql.*;

public class Database {

    private static String connectionString;

    public static void setConnectionString(final String folder) throws SQLException {
        connectionString = "jdbc:sqlite:" + folder.replace("\\", "\\\\") + "curseguard.sql";
        Logger.debug("Initialized SQLite database");
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
        Connection databaseConnection = DriverManager.getConnection(connectionString);
        databaseConnection.setAutoCommit(false);
        PreparedStatement statement = databaseConnection.prepareStatement(sql);
        for (int i = 0; i < parameters.length; i++) {
            statement.setString(i + 1, parameters[i]);
        }
        Logger.debug("Executing update statement: " + Ansi.BLUE + statement);
        statement.executeUpdate();
        statement.close();
        databaseConnection.commit();
        databaseConnection.close();
    }

    public static void executeUpdate(final String sql) throws SQLException {
        Connection databaseConnection = DriverManager.getConnection(connectionString);
        databaseConnection.setAutoCommit(false);
        PreparedStatement statement = databaseConnection.prepareStatement(sql);
        Logger.debug("Executing update statement: " + Ansi.BLUE + statement);
        statement.executeUpdate();
        statement.close();
        databaseConnection.commit();
        databaseConnection.close();
    }

    public static QueryResult executeFetch(final String sql, String[] parameters) throws SQLException {
        Connection databaseConnection = DriverManager.getConnection(connectionString);
        databaseConnection.setAutoCommit(false);
        PreparedStatement statement = databaseConnection.prepareStatement(sql);
        for (int i = 0; i < parameters.length; i++) {
            statement.setString(i + 1, parameters[i]);
        }
        Logger.debug("Executing fetch statement: " + Ansi.BLUE + statement);
        ResultSet results = statement.executeQuery();
        statement.close();
        results.close();
        databaseConnection.close();
        return new QueryResult(results);
    }

    public static QueryResult executeFetch(final String sql) throws SQLException {
        Connection databaseConnection = DriverManager.getConnection(connectionString);
        databaseConnection.setAutoCommit(false);
        PreparedStatement statement = databaseConnection.prepareStatement(sql);
        Logger.debug("Executing fetch statement: " + Ansi.BLUE + statement);
        ResultSet results = statement.executeQuery();
        statement.close();
        results.close();
        databaseConnection.close();
        return new QueryResult(results);
    }

}
