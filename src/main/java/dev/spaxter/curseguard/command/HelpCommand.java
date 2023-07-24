package dev.spaxter.curseguard.command;

import dev.spaxter.curseguard.core.Language;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class HelpCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender.hasPermission("curseguard.help")) {
            String helpMessage = "§b-----------§8[§cCurse§bGuard§8]§b-----------\n"
                                + "§e/cg help §b - See all available commands\n"
                                + "§e/cg addword <word> [block/censor] §b - Add a word to the wordlist\n"
                                + "§e/cg removeword <word> §b - Remove a word from the wordlist\n"
                                + "§e/cg wordlist <word> §b - See the wordlist";
            commandSender.sendMessage(helpMessage);
        } else {
            commandSender.sendMessage(Language.Notification.PERMISSION_DENIED);
        }
        return true;
    }
}
