package dev.spaxter.curseguard.command;

import dev.spaxter.curseguard.core.Language;
import dev.spaxter.curseguard.models.Action;
import dev.spaxter.curseguard.storage.GlobalMemory;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class AddWordCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        if (commandSender.hasPermission("curseguard.words.add")) {
            if (args.length != 2) {
                return false;
            }
            String word = args[0];
            Action action = Action.fromString(args[1]);
            if (action.equals(Action.NONE)) {
                commandSender.sendMessage(Language.Prefix.NOTIFICATION_PREFIX + Language.Notification.ADD_WORD_INVALID_ACTION);
                return false;
            }
            if (GlobalMemory.wordActions.containsKey(word)) {
                commandSender.sendMessage(Language.Prefix.NOTIFICATION_PREFIX + Language.Notification.ADD_WORD_WORD_EXISTS.replace("%word%", word));
                return true;
            }
            GlobalMemory.addBlockedWord(word, action);
            commandSender.sendMessage(Language.Prefix.NOTIFICATION_PREFIX + Language.Notification.ADD_WORD_SUCCESS.replace("%word%", word));
        } else {
            commandSender.sendMessage(Language.Notification.PERMISSION_DENIED);
        }
        return true;
    }
}
