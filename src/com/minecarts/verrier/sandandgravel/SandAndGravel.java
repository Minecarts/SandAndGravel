package com.minecarts.verrier.sandandgravel;

import java.util.logging.*;

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
    public Logger log = Logger.getLogger("com.minecarts.sandandgravel");
    private PlayerListener playerListener = new PlayerListener(this);
        
    public void onEnable() {
        PluginManager pm = this.getServer().getPluginManager();
        //Let us log all our debug messages
        for(Handler h : log.getParent().getHandlers()){
            h.setLevel(Level.ALL);
        }
        log.setLevel(Level.FINER);
        
        Game.world = this.getServer().getWorld("world");
        Game.plugin = this;
        
        //Add our listeners
        pm.registerEvent(Type.PLAYER_MOVE, this.playerListener, Event.Priority.Monitor, this);
        pm.registerEvent(Type.PLAYER_INTERACT, this.playerListener, Event.Priority.Monitor, this);

        //Also check if any players are currently standing in the position
    }
      
       
    public void onDisable(){
        //Log any current score data to the DB? Or.. whatever.
    }
    public boolean onCommand(CommandSender sender, Command cmd, String cmdLabel, String[] args){
        if(args.length >= 1){
            if(args[0].equals("clear")){
                sender.sendMessage("Cleared the board");
                Game.clearBoard();
                return true;
            } else if (args[0] == "stop"){

            }
        }
        return false;
    }
}
