package dev.spaxter.curseguard.events;

import dev.spaxter.curseguard.core.Guardian;
import dev.spaxter.curseguard.logging.Logger;
import dev.spaxter.curseguard.util.Ansi;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

public class OnSignPlace implements Listener {

    public OnSignPlace() {
        Logger.debug("Registered event handler " + Ansi.PURPLE + "OnSignPlace");
    }

    @EventHandler
    public void onSignChange(SignChangeEvent event) {
        Player player = event.getPlayer();
        if (player.hasPermission("curseguard.exempt")) {
            Logger.debug(player.getDisplayName() + " is exempt from the sign filter. Ignoring...");
            return;
        }

        Guardian.handleSignChange(event);
    }

}
