package com.vulkan1k.utils;

import org.bukkit.Location;

public class LocationUtil
{
    public Location getRelative(Location location, Location relative) 
    {
    	return new Location(
			location.getWorld(), 
			location.getX() + relative.getX(), 
			location.getY() + relative.getY(), 
			location.getZ() + relative.getZ()
		);
    }
    
    public Location negate(Location location) 
    {
    	return new Location(
			location.getWorld(), 
			-location.getX(), 
			-location.getY(), 
			-location.getZ()
		);
    }
    
    public Location round(Location location) 
    {
    	return new Location(
			location.getWorld(), 
			Math.round(location.getX()), 
			Math.round(location.getY()),
			Math.round(location.getZ())
		);
    }
}
