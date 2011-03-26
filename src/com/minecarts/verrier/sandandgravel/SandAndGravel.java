package com.minecarts.verrier.sandandgravel;

import java.util.logging.Logger;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Event;
import org.bukkit.event.Event.Type;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.PluginManager;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import com.minecarts.verrier.sandandgravel.listener.*;
import com.minecarts.verrier.sandandgravel.game.*;

public class SandAndGravel extends JavaPlugin  {
    public final Logger log = Logger.getLogger("Minecraft.SandAndGravel");
    private int taskId = 0;
    private PlayerListener playerListener = new PlayerListener(this);
        
    public void onEnable() {
        PluginManager pm = this.getServer().getPluginManager();
    
        Game.world = this.getServer().getWorld("world");
        
        //Add our listeners
        pm.registerEvent(Type.PLAYER_MOVE, this.playerListener, Event.Priority.Monitor, this);
        pm.registerEvent(Type.PLAYER_INTERACT, this.playerListener, Event.Priority.Monitor, this);
    }
      
       
    public void onDisable(){
        
    }
    public boolean onCommand(CommandSender sender, Command cmd, String cmdLabel, String[] args){
        if(args.length >= 1){
            if(args[0] == "clear"){
                //Start
                if(this.taskId == 0){
                    sender.sendMessage("Cleared the board");
                    return true;
                }
            } else if (args[0] == "stop"){
                if(this.taskId != 0){
                    sender.sendMessage("Canceled game task!");
                    return true;
                }
            }
            
        }
        return false;
    }
}
