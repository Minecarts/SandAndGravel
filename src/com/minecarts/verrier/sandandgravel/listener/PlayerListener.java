package com.minecarts.verrier.sandandgravel.listener;

import com.minecarts.verrier.sandandgravel.*;

import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.Location;
import org.bukkit.util.Vector;
import org.bukkit.entity.Player;

public class PlayerListener extends org.bukkit.event.player.PlayerListener{
    private SandAndGravel plugin;
    
    private Player sandPlayer;
    private Player gravelPlayer;
    
    private Vector playerLocationSand;
    private Vector playerLocationGravel;
    
    public PlayerListener(SandAndGravel plugin){
        this.plugin = plugin;
        
        //Create actual Locations from our vectors
        playerLocationSand = com.minecarts.verrier.sandandgravel.game.Locations.playerLocationSand;
        playerLocationGravel = com.minecarts.verrier.sandandgravel.game.Locations.playerLocationGravel;
        
        sandPlayer = null;
        gravelPlayer = null;
    }
    
    public void onPlayerMove(PlayerMoveEvent event){
        //Check to see if they're our existing player first
        Player player = event.getPlayer();
        String playerName = player.getName();
        Location newLocation = event.getTo();
        Vector newVector = newLocation.toVector(); 
        
        
        Double distanceFromSand = newVector.distance(playerLocationSand);
        Double distanceFromGravel = newVector.distance(playerLocationGravel);
       
        player.sendMessage(String.format("Distance from sand: %.3f, gravel: %.3f",distanceFromSand, distanceFromGravel));
       
        //Check if they're a current player in the game, if so.. make sure they're still in their spots
        //  and they didn't want to leave
            if(sandPlayer != null && playerName.equals(sandPlayer.getName())){
                if(distanceFromSand > 2){
                    //they left
                    sandPlayer = null;
                    plugin.stopGame();
                }
            }
            if(gravelPlayer != null &&  playerName.equals(gravelPlayer.getName())){
                if(distanceFromGravel > 2){
                    //they left
                    gravelPlayer = null;
                    plugin.stopGame();
                }
            }
        
        //If they weren't one of our players, lets see if they're within range of the spot now
            if(distanceFromSand <= 2 && sandPlayer == null){
                //They're our new sand player!
                sandPlayer = player;
                player.sendMessage("You are now the sand player!");
                plugin.log.info(String.format("%s is now the sand player.",player.getName()));
            } 
            else if (distanceFromGravel <= 2 && gravelPlayer == null){
                gravelPlayer = player;
                player.sendMessage("You are now the gravel player!");
                plugin.log.info(String.format("%s is now the gravel player.",player.getName()));
            }
            
        //Start the game if both plays are there
            if(!plugin.gameStarted && gravelPlayer != null && sandPlayer != null){
                plugin.startGame();
            }
    }
    
}
