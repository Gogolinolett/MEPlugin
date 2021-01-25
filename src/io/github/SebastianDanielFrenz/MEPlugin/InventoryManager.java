package io.github.SebastianDanielFrenz.MEPlugin;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class InventoryManager {

	public static List<MEInventory> inventories = new ArrayList<MEInventory>();

	public static MEInventory getInv(Player player) {
		for (MEInventory inv : inventories) {
			if (inv.owner == player) {
				return inv;
			}
		}
		return null;
	}

	public static void createInv(Player player) {
		if (getInv(player) == null) {
			inventories.add(new MEInventory(player, new ArrayList<ItemStack>(), new ArrayList<Integer>()));
		}
	}

}
