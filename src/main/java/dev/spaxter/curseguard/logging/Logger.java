package dev.spaxter.curseguard.logging;

import dev.spaxter.curseguard.storage.Config;
import dev.spaxter.curseguard.util.Ansi;

public class Logger {

    private static java.util.logging.Logger _logger;
    private static final boolean debugLogs = Config.config.getBoolean("debug", false);

    public static void log(final String message) {
        _logger.info(message + Ansi.RESET);
    }

    public static void debug(final String message) {
        if (debugLogs) {
            _logger.info(Ansi.GREEN + "[DEBUG] " + Ansi.CYAN + message + Ansi.RESET);
        }
    }

    public static void error(final String message) {
        _logger.info(Ansi.RED + "[ERROR] " + message + Ansi.RESET);
    }

    public static void setLogger(java.util.logging.Logger logger) {
        _logger = logger;
    }

}
