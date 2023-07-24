package dev.spaxter.curseguard.command;

import dev.spaxter.curseguard.logging.Logger;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AddWordTabComplete implements TabCompleter {

    private static final List<String> actions = Arrays.asList("BLOCK", "CENSOR");
    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] args) {
        List<String> completions = new ArrayList<>();
        if (args.length == 2) {
            StringUtil.copyPartialMatches(args[1], actions, completions);
            return completions;
        }
        return null;
    }
}
