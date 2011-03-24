package com.minecarts.verrier.sandandgravel;

import java.util.logging.Logger;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import org.bukkit.Location;
import org.bukkit.World;

public class SandAndGravel extends JavaPlugin  {
	public final Logger log = Logger.getLogger("Minecraft");
	
	public String gameState;
	public String playerSand;
	public String playerGravel;
		
	public void onEnable() {
		
		World world = getServer().getWorld("world");
		//Field locations
		Location playerLocationSand = new Location(world,45,59,18);
		Location playerLocationGravel = new Location(world,48,59,18);
		
		Location fallBarLeft = new Location(world,44,67,11);
		Location fallBarRight = new Location(world,50,67,11);
		
		Location gridTopLeft = new Location(world,44,66,11);
		Location gridBottomRight = new Location(world,50,61,11);
		
		gameState = "WAITING";		
	}
	public void onDisable(){
		
	}
	public boolean onCommand(CommandSender sender, Command cmd, String cmdLabel, String[] args){
		return false;
	}
}
