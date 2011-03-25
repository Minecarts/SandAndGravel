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

import com.minecarts.verrier.sandandgravel.listener.*;
import com.minecarts.verrier.sandandgravel.game.GameThread;

public class SandAndGravel extends JavaPlugin  {
    public final Logger log = Logger.getLogger("Minecraft");
    
    private int taskId = 0;
    
    private PlayerListener playerListener = new PlayerListener(this);

    public String playerSand;
    public String playerGravel;
    
    public GameThread game;
      
    public void onEnable() {        
        World world = getServer().getWorld("world");
        PluginManager pm = this.getServer().getPluginManager();
        
        //Field locations
        Location playerLocationSand = new Location(world,45,59,18);
        Location playerLocationGravel = new Location(world,48,59,18);
        
        Location fallBarLeft = new Location(world,44,67,11);
        Location fallBarRight = new Location(world,50,67,11);
        
        Location gridTopLeft = new Location(world,44,66,11);
        Location gridBottomRight = new Location(world,50,61,11);
        
        //Add our listeners
        pm.registerEvent(Type.PLAYER_MOVE, this.playerListener, Event.Priority.Monitor, this);
        pm.registerEvent(Type.PLAYER_INTERACT, this.playerListener, Event.Priority.Monitor, this);
        
        startGame();
    }
    
    private void startGame(){
        log.info("Starting game task");
        //Launch the async task that handles the game itself
        game = new com.minecarts.verrier.sandandgravel.game.GameThread(this);
        this.taskId = getServer().getScheduler().scheduleAsyncRepeatingTask(this, game, 1 * 20, 5 * 20);
    }
    
    public void onDisable(){
        
    }
    public boolean onCommand(CommandSender sender, Command cmd, String cmdLabel, String[] args){
        if(args.length >= 1){
            if(args[0] == "start"){
                //Start
                if(this.taskId == 0){
                    startGame();
                    sender.sendMessage("Started game task!");
                    return true;
                }
            } else if (args[0] == "stop"){
                if(this.taskId != 0){
                    getServer().getScheduler().cancelTask(this.taskId);
                    sender.sendMessage("Canceled game task!");
                    this.taskId = 0;
                    return true;
                }
            }
            
        }
        return false;
    }
}
