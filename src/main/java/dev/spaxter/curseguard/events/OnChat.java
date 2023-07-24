package dev.spaxter.curseguard.events;

import dev.spaxter.curseguard.core.Guardian;
import dev.spaxter.curseguard.logging.Logger;
import dev.spaxter.curseguard.storage.Config;
import dev.spaxter.curseguard.util.Ansi;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class OnChat implements Listener {

    public OnChat() {
        Logger.debug("Registered event handler " + Ansi.PURPLE + "OnChat");
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent e) {
        Player player = e.getPlayer();
        if (player.hasPermission("curseguard.exempt")) {
            Logger.debug(player.getDisplayName() + " is exempt from the chat filter. Ignoring...");
            return;
        }

        if (Config.config.getBoolean("anti-spam.enabled", true)) {
            Guardian.spamHandler(e);
        }
        Guardian.handleMessage(e);
    }

}
