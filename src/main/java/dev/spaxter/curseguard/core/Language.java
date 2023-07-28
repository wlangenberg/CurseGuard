package dev.spaxter.curseguard.core;

import dev.spaxter.curseguard.CurseGuard;
import org.bukkit.ChatColor;

public class Language {

    public static class Notification {
        public static String PERMISSION_DENIED = ChatColor.translateAlternateColorCodes('&', CurseGuard.languageConfig.getString("general.permission-denied"));
        public static String MESSAGE_BLOCKED_STAFF = ChatColor.translateAlternateColorCodes('&', CurseGuard.languageConfig.getString("general.message-blocked-staff-notification"));
        public static String MESSAGE_BLOCKED = ChatColor.translateAlternateColorCodes('&', CurseGuard.languageConfig.getString("general.message-blocked"));
        public static String MESSAGE_CENSORED_STAFF = ChatColor.translateAlternateColorCodes('&', CurseGuard.languageConfig.getString("general.message-censored-staff-notification"));
        public static String ADD_WORD_INVALID_ACTION = ChatColor.translateAlternateColorCodes('&', CurseGuard.languageConfig.getString("general.add-word-invalid-action"));
        public static String ADD_WORD_WORD_EXISTS = ChatColor.translateAlternateColorCodes('&', CurseGuard.languageConfig.getString("general.add-word-word-exists"));
        public static String ADD_EXEMPT_WORD_SUCCESS = ChatColor.translateAlternateColorCodes('&', CurseGuard.languageConfig.getString("general.add-exempt-word-success"));
        public static String ADD_EXEMPT_WORD_WORD_EXISTS = ChatColor.translateAlternateColorCodes('&', CurseGuard.languageConfig.getString("general.add-exempt-word-word-exists"));
        public static String ADD_EXEMPT_WORD_WORD_EXISTS_IN_FILTER = ChatColor.translateAlternateColorCodes('&', CurseGuard.languageConfig.getString("general.add-exempt-word-word-exists-in-filter"));
        public static String ADD_WORD_SUCCESS = ChatColor.translateAlternateColorCodes('&', CurseGuard.languageConfig.getString("general.add-word-success"));
        public static String REMOVE_WORD_SUCCESS = ChatColor.translateAlternateColorCodes('&', CurseGuard.languageConfig.getString("general.remove-word-success"));
        public static String REMOVE_WORD_NOT_FOUND = ChatColor.translateAlternateColorCodes('&', CurseGuard.languageConfig.getString("general.remove-word-not-found"));
        public static String SIGN_BLOCKED = ChatColor.translateAlternateColorCodes('&', CurseGuard.languageConfig.getString("general.sign-blocked"));
        public static String SIGN_BLOCKED_STAFF = ChatColor.translateAlternateColorCodes('&', CurseGuard.languageConfig.getString("general.sign-blocked-staff"));
        public static String SIGN_CENSORED_STAFF = ChatColor.translateAlternateColorCodes('&', CurseGuard.languageConfig.getString("general.sign-censored-staff"));
        public static String ITEM_BLOCKED = ChatColor.translateAlternateColorCodes('&', CurseGuard.languageConfig.getString("general.item-name-blocked"));
        public static String ITEM_BLOCKED_STAFF = ChatColor.translateAlternateColorCodes('&', CurseGuard.languageConfig.getString("general.item-name-blocked-staff"));
        public static String ITEM_CENSORED_STAFF = ChatColor.translateAlternateColorCodes('&', CurseGuard.languageConfig.getString("general.item-name-censored-staff"));
        public static String SPAM_BLOCKED = ChatColor.translateAlternateColorCodes('&', CurseGuard.languageConfig.getString("general.spam-blocked"));
    }

    public static class Prefix {
        public static String NOTIFICATION_PREFIX = ChatColor.translateAlternateColorCodes('&', CurseGuard.languageConfig.getString("prefix.notification-prefix")) + " ";
    }
}
