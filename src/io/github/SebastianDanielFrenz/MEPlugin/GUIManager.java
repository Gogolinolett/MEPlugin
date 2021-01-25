package io.github.SebastianDanielFrenz.MEPlugin;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public class GUIManager implements Listener {

	@EventHandler
	public void onInventoryClose(InventoryCloseEvent event) {
		if (event.getInventory().getHolder() instanceof MEInventory) {
			((MEInventory) event.getInventory().getHolder()).removeInv(event.getInventory());
		}
	}

	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		event.getWhoClicked().sendMessage("click event");
		if (event.getInventory().getHolder() instanceof MEInventory) {
			MEInventory inv = (MEInventory) event.getInventory().getHolder();
			if (event.getAction() == InventoryAction.PICKUP_ALL) {
				if (event.getRawSlot() < 54) { // see if slot in chest
					if (event.getCursor() != null) {
						event.setCursor(inv.getMax(MEInventory.convertToInternal(event.getCursor())));
					}
				}
			} else if (event.getAction() == InventoryAction.PLACE_ALL) {
				if (event.getRawSlot() < 54) {
					event.setCancelled(true);
					inv.add(event.getCursor());
					event.setCursor(null);
					event.getWhoClicked().sendMessage("placed");
				}
			} else {
				event.getWhoClicked().sendMessage("illegal: " + event.getAction());
				event.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {

		InventoryManager.createInv(event.getPlayer());

		boolean hasPlayedBefore = false;
		for (OfflinePlayer player : Bukkit.getOfflinePlayers()) {
			if (event.getPlayer().getUniqueId().equals(player.getUniqueId())) {
				hasPlayedBefore = true;
				break;
			}
		}

		if (!hasPlayedBefore) {
			event.getPlayer().sendMessage("Welcome!");
		}
	}

}
