package dev.spaxter.curseguard.core;

import dev.spaxter.curseguard.storage.Config;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;

public class Notifier {

    private static boolean shouldNotify = Config.config.getBoolean("notify-staff", true);
    public static void notifyStaffMembers(final String message) {
        if (shouldNotify) {
            List<Player> onlineStaff = Bukkit.getOnlinePlayers()
                                                .stream()
                                                .filter(e -> e.hasPermission("curseguard.staff.notifications"))
                                                .collect(Collectors.toList());
            for (Player player : onlineStaff) {
                player.sendMessage(message);
            }
        }
    }
}
