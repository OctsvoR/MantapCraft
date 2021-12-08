package com.vulkan1k.functions;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;

public class FarmFunction 
{
	
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

	public boolean clickedBlockIsSign(Block clickedBlock) 
	{
		for(int i = 0; i < signTypes.length; i++) 
		{
			if(clickedBlock.getType() == signTypes[i]) 
			{
				return true;
			}
		}
		
		return false;
	}
	
	public boolean signIsFarmSign(Block clickedBlock) 
	{
		Sign sign = (Sign)clickedBlock.getState();
		String[] signLines = sign.getLines();
		
		for(int i = 0; i < signLines.length; i++) 
		{
			if(signLines[i].equalsIgnoreCase("[farm]")) 
			{
				return true;
			}
		}
		
		return false;
	}
}
