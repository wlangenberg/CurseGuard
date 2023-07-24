package dev.spaxter.curseguard.events;

import dev.spaxter.curseguard.core.Guardian;
import dev.spaxter.curseguard.logging.Logger;
import dev.spaxter.curseguard.util.Ansi;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;

public class OnItemNameChange implements Listener {

    public OnItemNameChange() {
        Logger.debug("Registered event handler " + Ansi.PURPLE + "OnItemNameChange");
    }

    @EventHandler
    public void onItemNameChange(InventoryClickEvent e) {
        Player player = (Player) e.getView().getPlayer();
        if (player.hasPermission("curseguard.exempt")) {
            Logger.debug(player.getDisplayName() + " is exempt from the item name filter. Ignoring...");
            return;
        }

        if (e.getInventory().getType() == InventoryType.ANVIL) {
            Guardian.handleAnvilRename(e);
        }
    }

}
