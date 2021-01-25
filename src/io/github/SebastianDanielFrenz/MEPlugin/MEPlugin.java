package io.github.SebastianDanielFrenz.MEPlugin;

import org.bukkit.plugin.java.JavaPlugin;

public class MEPlugin extends JavaPlugin {

	public static MEPlugin plugin;

	@Override
	public void onEnable() {
		plugin = this;
		getCommand("me").setExecutor(new MECommandExecutor());
		getServer().getPluginManager().registerEvents(new GUIManager(), this);
	}

}
