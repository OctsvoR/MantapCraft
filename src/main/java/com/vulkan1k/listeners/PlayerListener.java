package com.vulkan1k.listeners;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import com.vulkan1k.MantapCraft;
import com.vulkan1k.functions.FarmFunction;
import com.vulkan1k.utils.LocationUtil;

public class PlayerListener implements Listener 
{
	private FarmFunction farmFunction = new FarmFunction();
	private LocationUtil locationUtil = new LocationUtil();
	
	private MantapCraft plugin;
	
	private ArrayList<Location> map = new ArrayList<Location> ();
	
	private final Material[] signTypes = new Material[] {
		Material.ACACIA_SIGN,
		Material.BIRCH_SIGN,
		Material.CRIMSON_SIGN,
		Material.DARK_OAK_SIGN,
		Material.JUNGLE_SIGN,
		Material.OAK_SIGN,
		Material.SPRUCE_SIGN,
		Material.WARPED_SIGN,
	};
	
	private final Material[] farmlandCropTypes = new Material[] {
		Material.WHEAT,
		Material.BEETROOT,
		Material.POTATOES,
		Material.CARROTS,
	};
	
	private final Material[] farmlandSeedTypes = new Material[] {
		Material.WHEAT_SEEDS,
		Material.BEETROOT_SEEDS,
		Material.POTATO,
		Material.CARROT,
	};
	
	private enum CropType 
	{
		FARMLAND,
		NETHERWART
	}
	
	public PlayerListener(final MantapCraft plugin) 
	{
		this.plugin = plugin;
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onPlayerInteract(PlayerInteractEvent e) 
	{
		switch(e.getAction()) 
		{
			case LEFT_CLICK_AIR:
				break;
			case LEFT_CLICK_BLOCK:
				break;
			case PHYSICAL:
				break;
			case RIGHT_CLICK_AIR:
				break;
			case RIGHT_CLICK_BLOCK:
				break;
			default:
				break;
		}
		
    	Player player = e.getPlayer();
    	
    	if(farmFunction.clickedBlockIsSign(e.getClickedBlock())) 
    	{
    		if(farmFunction.signIsFarmSign(e.getClickedBlock())) 
    		{
    			Sign sign = (Sign)e.getClickedBlock().getState();
	    		org.bukkit.block.data.type.Sign signData = (org.bukkit.block.data.type.Sign)sign.getBlockData();
	    		
	    		switch(e.getAction()) 
	    		{
					case LEFT_CLICK_BLOCK:
						mapFarmland(
							locationUtil.getRelative(
								e.getClickedBlock().getLocation(), 
								locationUtil.round(
									locationUtil.negate(
										signData.getRotation().getDirection().toLocation(player.getWorld())
									)
								)
							)
						);
						
						for(Location l : map) 
						{
							if(!l.getBlock().getType().equals(Material.AIR)) 
							{
								harvestRipeCrop(l);
							}
						}
						break;
					case RIGHT_CLICK_BLOCK:
						mapFarmland(
							locationUtil.getRelative(
								e.getClickedBlock().getLocation(), 
								locationUtil.round(
									locationUtil.negate(
										signData.getRotation().getDirection().toLocation(player.getWorld())
									)
								)
							)
						);
						
						for(Location l : map) 
						{
							if(l.getBlock().getType().equals(Material.AIR)) 
							{
								plantCrop(player, l);
							}
						}
						break;
				}
    		}
    	}
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onPlayerExpChange(PlayerExpChangeEvent e) 
    {
    	e.setAmount(e.getAmount() * 3);
    }
    
	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onPrepareAnvil (PrepareAnvilEvent e) 
	{
		
		Map<Enchantment, Integer> enchantmentsMap = e.getInventory().getItem(0).getItemMeta().getEnchants();
		Set<Enchantment> enchantmentsSet = enchantmentsMap.keySet();
		
		plugin.getServer().broadcastMessage(enchantmentsSet.toString());
		
		e.getInventory().setMaximumRepairCost(Integer.MAX_VALUE);
		
		if (e.getInventory().getRepairCost() > 30) {
			e.getInventory().setRepairCost(30);
		}
	}

    void mapFarmland(Location location) 
    {
    	if(!locationUtil.getRelative(location, new Location(location.getWorld(), 0, -1, 0)).getBlock().getType().equals(Material.FARMLAND))
    		return;
    	
    	if(map.contains(location))
    		return;
		
    	map.add(location);
    	
		mapFarmland(locationUtil.getRelative(location, new Location(location.getWorld(), 0, 0, 1)));
		mapFarmland(locationUtil.getRelative(location, new Location(location.getWorld(), -1, 0, 0)));
		mapFarmland(locationUtil.getRelative(location, new Location(location.getWorld(), 0, 0, -1)));
		mapFarmland(locationUtil.getRelative(location, new Location(location.getWorld(), 1, 0, 0)));
    }
    
    void plantCrop(Player player, Location location) 
    {
		ItemStack handItem = player.getInventory().getItemInMainHand();
		
		if(handItem == null)
			return;
		
		if(handItem.getAmount() <= 0)
			return;
		
    	for(int i = 0; i < farmlandSeedTypes.length; i++) 
    	{
        	if(handItem.getType() == farmlandSeedTypes[i]) 
        	{
		    	location.getBlock().setType(toCrop(handItem.getType()));
		    	handItem.setAmount(handItem.getAmount() - 1);
        	}
    	}
    }
    
    void harvestRipeCrop(Location location) 
    { 
    	if(((org.bukkit.block.data.Ageable)location.getBlock().getBlockData()).getAge() == 7) 
    	{
        	location.getBlock().breakNaturally();
    	}
    }
    
    Material toCrop(Material seed) {
    	Material crop = Material.AIR;
		
		for(int i = 0; i < farmlandSeedTypes.length; i++) {
			if(farmlandSeedTypes[i].equals(seed)) {
				crop = farmlandCropTypes[i];
				break;
			}
		}
		
		return crop;
    }
    
    Material toSeed(Material crop) 
    {
    	Material seed = Material.AIR;
		
		for(int i = 0; i < farmlandCropTypes.length; i++) 
		{
			if(farmlandCropTypes[i].equals(crop)) {
				seed = farmlandSeedTypes[i];
				break;
			}
		}
		
		return seed;
    }
}
