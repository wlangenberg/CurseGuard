package dev.spaxter.curseguard.command;

import dev.spaxter.curseguard.logging.Logger;
import dev.spaxter.curseguard.storage.GlobalMemory;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

public class RemoveWordTabComplete implements TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] args) {
        if (args.length == 1) {
            ArrayList<String> completions = new ArrayList<>();
            StringUtil.copyPartialMatches(args[0], new ArrayList<>(GlobalMemory.wordActions.keySet()), completions);
            return completions;
        }
        return null;
    }
}
