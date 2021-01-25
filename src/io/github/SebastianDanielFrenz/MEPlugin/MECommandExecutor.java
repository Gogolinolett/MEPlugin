package io.github.SebastianDanielFrenz.MEPlugin;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MECommandExecutor implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
			((Player)sender).openInventory(InventoryManager.getInv((Player) sender).getInventory());
		}
		return true;
	}

}
