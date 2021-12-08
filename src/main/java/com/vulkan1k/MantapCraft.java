package com.vulkan1k;

import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.vulkan1k.listeners.PlayerListener;

public class MantapCraft extends JavaPlugin 
{
	@Override
	public void onEnable() 
	{
		PluginManager pluginManager = getServer().getPluginManager();
		
		pluginManager.registerEvents(new PlayerListener(this), this);
	}
}
