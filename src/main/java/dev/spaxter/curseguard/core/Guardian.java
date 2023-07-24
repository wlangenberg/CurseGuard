package dev.spaxter.curseguard.core;

import dev.spaxter.curseguard.logging.Logger;
import dev.spaxter.curseguard.models.Action;
import dev.spaxter.curseguard.storage.Config;
import dev.spaxter.curseguard.storage.GlobalMemory;
import dev.spaxter.curseguard.util.Ansi;
import dev.spaxter.curseguard.util.AsyncExecutor;
import org.bukkit.entity.Player;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.text.DecimalFormat;
import java.util.*;

public class Guardian {

    /**
     * Replace an occurrence of a word in a string with the configured censor characters
     *
     * @param message The message
     * @param censoredWord The word to censor in the message
     * @return The new message with the censored word
     */
    public static String censorMessage(String message, String censoredWord) {
        List<Character> censorCharList = new ArrayList<Character>();
        boolean partialCensor = Config.config.getBoolean("partial-censor", false);
        int i = 0;
        for (char c : censoredWord.toCharArray()) {
            if ((i == 0 || i == censorCharList.size() - 2) && partialCensor) {
                censorCharList.add(c);
            } else {
                censorCharList.add(GlobalMemory.censorCharacter);
            }
            i++;
        }
        StringBuilder sb = new StringBuilder();
        for (Character c : censorCharList) {
            sb.append(c);
        }
        message = message.replace(censoredWord, sb.toString());
        return message;
    }

    public static void handleMessage(AsyncPlayerChatEvent e) {
        Player player = e.getPlayer();
        String message = e.getMessage();

        ArrayList<String> words = AsyncExecutor.findWordsAsync(message, GlobalMemory.wordActions.keySet());
        for (String word : words) {
            boolean exempt = false;
            for (String exemptWord : GlobalMemory.exemptWords) {
                if (exemptWord.contains(word)) {
                    exempt = true;
                    Logger.debug(Ansi.PURPLE + exemptWord + Ansi.CYAN + " is an exempt word.");
                    break;
                }
            }
            if (!exempt) {
                Action action = GlobalMemory.wordActions.get(word);
                boolean shouldContinue = true;
                switch (action) {
                    case BLOCK: {
                        player.sendMessage(Language.Prefix.NOTIFICATION_PREFIX + Language.Notification.MESSAGE_BLOCKED.replace("%word%", word));
                        e.setCancelled(true);
                        Logger.debug("Blocked word " + Ansi.PURPLE + word + Ansi.CYAN + " from " + Ansi.GREEN + player.getName() + "'s chat message");
                        Notifier.notifyStaffMembers(Language.Prefix.NOTIFICATION_PREFIX + Language.Notification.MESSAGE_BLOCKED_STAFF.replaceAll("%word%", word).replaceAll("%player%", player.getName()));
                        shouldContinue = false;
                        break;
                    }

                    case CENSOR: {
                        message = Guardian.censorMessage(message, word);
                        Logger.debug("Censored word " + Ansi.PURPLE + word + Ansi.CYAN + " from " + Ansi.GREEN + player.getName() + "'s chat message");
                        Notifier.notifyStaffMembers(Language.Prefix.NOTIFICATION_PREFIX + Language.Notification.MESSAGE_CENSORED_STAFF.replaceAll("%word%", word).replaceAll("%player%", player.getName()));
                        break;
                    }

                    default: {
                        Logger.error("Unknown word action: " + Ansi.YELLOW + action);
                        break;
                    }
                }
                if (!shouldContinue) {
                    break;
                }
            }
        }
        e.setMessage(message);
    }

    public static void handleSignChange(SignChangeEvent e) {
        String[] lines = e.getLines();
        Player player = e.getPlayer();
        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];
            ArrayList<String> words = AsyncExecutor.findWordsAsync(line, GlobalMemory.wordActions.keySet());
            for (String word : words) {
                boolean exempt = false;
                for (String exemptWord : GlobalMemory.exemptWords) {
                    if (exemptWord.contains(word)) {
                        exempt = true;
                        Logger.debug(Ansi.PURPLE + exemptWord + Ansi.CYAN + " is an exempt word.");
                        break;
                    }
                }
                if (!exempt) {
                    Action action = GlobalMemory.wordActions.get(word);
                    boolean shouldContinue = true;
                    switch (action) {
                        case BLOCK: {
                            player.sendMessage(Language.Prefix.NOTIFICATION_PREFIX + Language.Notification.SIGN_BLOCKED.replace("%word%", word));
                            e.setCancelled(true);
                            Logger.debug("Blocked word " + Ansi.PURPLE + word + Ansi.CYAN + " from " + Ansi.GREEN + player.getName() + "'s sign");
                            Notifier.notifyStaffMembers(Language.Prefix.NOTIFICATION_PREFIX + Language.Notification.SIGN_BLOCKED_STAFF.replaceAll("%word%", word).replaceAll("%player%", player.getName()));
                            shouldContinue = false;
                            break;
                        }

                        case CENSOR: {
                            line = Guardian.censorMessage(line, word);
                            e.setLine(i, line);
                            Logger.debug("Censored word " + Ansi.PURPLE + word + Ansi.CYAN + " from " + Ansi.GREEN + player.getName() + "'s sign");
                            Notifier.notifyStaffMembers(Language.Prefix.NOTIFICATION_PREFIX + Language.Notification.SIGN_CENSORED_STAFF.replaceAll("%word%", word).replaceAll("%player%", player.getName()));
                            break;
                        }

                        default: {
                            Logger.error("Unknown word action: " + Ansi.YELLOW + action);
                            break;
                        }
                    }
                    if (!shouldContinue) {
                        break;
                    }
                }
            }
        }
    }

    public static void handleAnvilRename(InventoryClickEvent e) {
        Player player = (Player) e.getView().getPlayer();
        ItemStack before = e.getInventory().getItem(0);
        ItemStack after = e.getInventory().getItem(2);
        if (before == null || after == null) {
            return;
        }

        if (e.getSlot() == 2) {
            Logger.debug("Clicked after item");
            ItemMeta afterItemMeta = after.getItemMeta();
            if (afterItemMeta == null) {
                return;
            }
            String itemName = afterItemMeta.getDisplayName();
            ArrayList<String> words = AsyncExecutor.findWordsAsync(itemName, GlobalMemory.wordActions.keySet());
            for (String word : words) {
                boolean exempt = false;
                for (String exemptWord : GlobalMemory.exemptWords) {
                    if (exemptWord.contains(word)) {
                        exempt = true;
                        Logger.debug(Ansi.PURPLE + exemptWord + Ansi.CYAN + " is an exempt word.");
                        break;
                    }
                }
                if (!exempt) {
                    Action action = GlobalMemory.wordActions.get(word);
                    boolean shouldContinue = true;
                    switch (action) {
                        case BLOCK: {
                            player.sendMessage(Language.Prefix.NOTIFICATION_PREFIX + Language.Notification.ITEM_BLOCKED.replace("%word%", word));
                            e.setCancelled(true);
                            player.closeInventory();
                            Logger.debug("Blocked word " + Ansi.PURPLE + word + Ansi.CYAN + " from " + Ansi.GREEN + player.getName() + "'s sign");
                            Notifier.notifyStaffMembers(Language.Prefix.NOTIFICATION_PREFIX + Language.Notification.ITEM_BLOCKED_STAFF.replaceAll("%word%", word).replaceAll("%player%", player.getName()));
                            shouldContinue = false;
                            break;
                        }

                        case CENSOR: {
                            Logger.debug("Censored word " + Ansi.PURPLE + word + Ansi.CYAN + " from " + Ansi.GREEN + player.getName() + "'s sign");
                            ItemMeta newItemMeta = after.getItemMeta();
                            newItemMeta.setDisplayName(Guardian.censorMessage(itemName, word));
                            after.setItemMeta(newItemMeta);
                            Notifier.notifyStaffMembers(Language.Prefix.NOTIFICATION_PREFIX + Language.Notification.ITEM_CENSORED_STAFF.replaceAll("%word%", word).replaceAll("%player%", player.getName()));
                            break;
                        }

                        default: {
                            Logger.error("Unknown word action: " + Ansi.YELLOW + action);
                            break;
                        }
                    }
                    if (!shouldContinue) {
                        break;
                    }
                }
            }
        }
    }

    public static void spamHandler(AsyncPlayerChatEvent e) {
        Player p = e.getPlayer();
        UUID uuid = p.getUniqueId();
        if (GlobalMemory.rateLimitMap.containsKey(uuid)) {
            int number = GlobalMemory.rateLimitMap.get(uuid) + 1;
            GlobalMemory.rateLimitMap.put(uuid, number);
            if (number > Config.config.getInt("anti-spam.messages", 5)) {
                DecimalFormat df = new DecimalFormat("0.00");
                Logger.debug("Prevented message from " + Ansi.PURPLE + e.getPlayer().getDisplayName() + Ansi.CYAN + " due to anti-spam");
                float timeLeft = (float) (GlobalMemory.timeLeftMap.get(uuid) - System.currentTimeMillis()) / 1000;
                p.sendMessage(Language.Notification.SPAM_BLOCKED.replaceAll("%timeleft%", df.format(timeLeft)));
                e.setCancelled(true);
            }
        } else {
            GlobalMemory.rateLimitMap.put(uuid, 1);
            Logger.debug("Added " + Ansi.PURPLE + e.getPlayer().getDisplayName() + Ansi.CYAN + " to anti-spam map");
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    Logger.debug("Removed " + Ansi.PURPLE + e.getPlayer().getDisplayName() + Ansi.CYAN + " from anti-spam map");
                    GlobalMemory.rateLimitMap.remove(uuid);
                    GlobalMemory.timeLeftMap.remove(uuid);
                }
            }, (Config.config.getInt("anti-spam.messages", 5) + 1)  * 1000L);
            GlobalMemory.timeLeftMap.put(uuid, System.currentTimeMillis() + (Config.config.getInt("anti-spam.messages", 5) + 1) * 1000L);
        }
    }
}
