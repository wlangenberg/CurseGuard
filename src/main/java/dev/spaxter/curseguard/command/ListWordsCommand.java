package dev.spaxter.curseguard.command;

import dev.spaxter.curseguard.core.Language;
import dev.spaxter.curseguard.storage.GlobalMemory;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ListWordsCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (commandSender.hasPermission("curseguard.words.read")) {
            StringBuilder message = new StringBuilder();
            for (String word : GlobalMemory.wordActions.keySet()) {
                message.append("§b").append(word).append("  -  §e").append(GlobalMemory.wordActions.get(word)).append("\n");
            }
            commandSender.sendMessage(message.toString());
        } else {
            commandSender.sendMessage(Language.Notification.PERMISSION_DENIED);
        }
        return true;
    }
}
