package dev.spaxter.curseguard.command;

import dev.spaxter.curseguard.CurseGuard;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class MainCommand implements CommandExecutor {

    public static Map<String, CommandExecutor> commandMap = new HashMap<>();

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (args.length > 0) {
            String subCommand = args[0];
            CommandExecutor subCommandExecutor = commandMap.get(subCommand);
            if (subCommandExecutor == null) {
                return false;
            } else {
                subCommandExecutor.onCommand(commandSender, command, s, Arrays.copyOfRange(args, 1, args.length));
            }
        } else {
            String message = "§4--------§8[§cCurse§bGuard§8]§4--------\n"
                            + "§bVersion: §e" + CurseGuard.version + "\n"
                            + "§bRun §e/cg help §bto see available commands\n"
                            + "§bDeveloped by: §cSpaxter";
            commandSender.sendMessage(message);
        }
        return true;
    }
}
