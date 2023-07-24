package dev.spaxter.curseguard.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.util.StringUtil;

import java.util.*;

public class MainCommandTabComplete implements TabCompleter {

    public static Map<String, TabCompleter> tabCompleteMap = new HashMap<>();

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] args) {
        if (args.length == 1) {
            ArrayList<String> completions = new ArrayList<>();
            StringUtil.copyPartialMatches(args[0], new ArrayList<>(MainCommand.commandMap.keySet()), completions);
            return completions;
        }
        if (args.length > 1) {
            String commandName = args[0];
            if (!tabCompleteMap.containsKey(commandName)) {
                return null;
            }
            TabCompleter tabCompleter = tabCompleteMap.get(commandName.toLowerCase());
            return tabCompleter.onTabComplete(commandSender, command, s, Arrays.copyOfRange(args, 1, args.length));
        }
        return null;
    }
}
