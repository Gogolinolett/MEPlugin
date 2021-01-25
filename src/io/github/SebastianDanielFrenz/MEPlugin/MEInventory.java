package io.github.SebastianDanielFrenz.MEPlugin;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class MEInventory implements InventoryHolder {

	public static final ItemStack ITEM_BACK = new ItemStack(Material.GOLD_NUGGET, 1);
	public static final ItemStack ITEM_NEXT = new ItemStack(Material.GOLD_NUGGET, 1);

	public static void init() {
		ItemMeta meta = ITEM_BACK.getItemMeta();
		meta.setDisplayName("Back");
		ITEM_BACK.setItemMeta(meta);
		meta = ITEM_NEXT.getItemMeta();
		meta.setDisplayName("Next");
		ITEM_NEXT.setItemMeta(meta);
	}

	public MEInventory(Player owner, List<ItemStack> stacks, List<Integer> stack_sizes) {
		this.owner = owner;
		this.stacks = stacks;
		this.stack_sizes = stack_sizes;
	}

	protected Player owner;
	protected List<ItemStack> stacks;
	protected List<Integer> stack_sizes;

	private List<Inventory> open_invs = new ArrayList<Inventory>();
	private List<Integer> open_inv_pages = new ArrayList<Integer>();

	public void registerInv(Inventory inv) {
		open_invs.add(inv);
		open_inv_pages.add(0);
	}

	public void removeInv(Inventory inv) {
		int i = open_invs.indexOf(inv);
		open_invs.remove(i);
		open_inv_pages.remove(i);
	}

	public void nextPage(Inventory inv) {
		int i = open_invs.indexOf(inv);
		int page = open_inv_pages.get(i);
		if (stacks.size() > 54) {
			int page_count = Utils.roundUp((float) stacks.size() / 45);
			if (page + 1 < page_count) {
				open_inv_pages.set(i, page + 1);
				renderPage(inv, page + 1);
			}
		}
	}

	public void backPage(Inventory inv) {
		int i = open_invs.indexOf(inv);
		int page = open_inv_pages.get(i);
		if (page != 0) {
			open_inv_pages.set(i, page - 1);
			renderPage(inv, page - 1);
		}
	}

	public void renderPage(Inventory inv, int page) {
		inv.clear();

		if (stacks.size() < 54) {
			for (int i = 0; i < stacks.size(); i++) {
				inv.setItem(i, convertForGUI(stacks.get(i), stack_sizes.get(i)));
			}
			return;
		}

		for (int i = 0; i < stacks.size() - page * 45; i++) {
			inv.setItem(i + 9, convertForGUI(stacks.get(i + page * 45), stack_sizes.get(i + page * 45)));
		}

		inv.setItem(0, ITEM_BACK);
		inv.setItem(8, ITEM_NEXT);
	}

	public ItemStack convertForGUI(ItemStack stack, int amount) {
		ItemStack out = new ItemStack(stack);
		ItemMeta meta = out.getItemMeta();
		meta.setDisplayName(amount + "x " + stack.getItemMeta().getDisplayName());
		out.setItemMeta(meta);
		return out;
	}

	public static ItemStack convertToInternal(ItemStack stack) {
		ItemStack out = new ItemStack(stack);
		ItemMeta meta = out.getItemMeta();
		int i;
		char[] chars = meta.getDisplayName().toCharArray();
		for (i = 0; chars[i] != ' '; i++) {
		}
		meta.setDisplayName(meta.getDisplayName().substring(i + 1));
		out.setItemMeta(meta);
		return out;
	}

	public void add(ItemStack stack) {
		ItemStack _stack;
		for (int i = 0; i < stacks.size(); i++) {
			_stack = stacks.get(i);
			if (_stack.isSimilar(stack)) {
				stack_sizes.set(i, stack_sizes.get(i) + stack.getAmount());
				return;
			}
		}

		stack_sizes.add(stack.getAmount());
		stack.setAmount(1);
		stacks.add(stack);
	}

	public ItemStack getSingle(ItemStack stack) {
		ItemStack _stack;
		for (int i = 0; i < stacks.size(); i++) {
			_stack = stacks.get(i);
			if (_stack.isSimilar(stack)) {
				if (stack_sizes.get(i) > 1) {
					stack_sizes.set(i, stack_sizes.get(i) - 1);
					ItemStack out = new ItemStack(_stack);
					out.setAmount(1);
					return out;
				} else {
					stacks.remove(i);
					stack_sizes.remove(i);
					return _stack;
				}
			}
		}
		return null;
	}

	public ItemStack getMax(ItemStack stack) {
		ItemStack _stack;
		for (int i = 0; i < stacks.size(); i++) {
			_stack = stacks.get(i);
			if (_stack.isSimilar(stack)) {
				if (stack_sizes.get(i) > _stack.getMaxStackSize()) {
					stack_sizes.set(i, stack_sizes.get(i) - _stack.getMaxStackSize());
					ItemStack out = new ItemStack(_stack);
					out.setAmount(_stack.getMaxStackSize());
					return out;
				} else {
					stacks.remove(i);
					stack_sizes.remove(i);
					return _stack;
				}
			}
		}
		return null;
	}

	@Override
	public Inventory getInventory() {
		Inventory inv = Bukkit.createInventory(owner, 54, "Digital Storage");
		int size;
		int offset;
		if (stacks.size() <= 54) {
			size = stacks.size();
			offset = 0;
		} else {
			size = 45;
			offset = 9;
		}
		for (int i = 0; i < size; i++) {
			inv.setItem(i + offset, convertForGUI(stacks.get(i), stack_sizes.get(i)));
		}
		if (offset == 9) {
			inv.setItem(0, ITEM_BACK);
			inv.setItem(8, ITEM_NEXT);
		}
		return inv;
	}

}
