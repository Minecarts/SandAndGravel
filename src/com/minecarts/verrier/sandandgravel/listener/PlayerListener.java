package com.minecarts.verrier.sandandgravel.listener;

import com.minecarts.verrier.sandandgravel.*;
import com.minecarts.verrier.sandandgravel.game.Game;
import com.minecarts.verrier.sandandgravel.game.Locations;

import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.util.Vector;
import org.bukkit.entity.Player;
import org.bukkit.block.*;

public class PlayerListener extends org.bukkit.event.player.PlayerListener{
    private SandAndGravel plugin;
    
    private Vector playerLocationSand;
    private Vector playerLocationGravel;
    
    public PlayerListener(SandAndGravel plugin){
        this.plugin = plugin;
        
        //Create actual Locations from our vectors
        playerLocationSand = com.minecarts.verrier.sandandgravel.game.Locations.playerLocationSand;
        playerLocationGravel = com.minecarts.verrier.sandandgravel.game.Locations.playerLocationGravel;
        
    }
    
    private void checkWin(Game.State nextTurn, int column){
        Runnable checkWin = new com.minecarts.verrier.sandandgravel.game.CheckWinThread(plugin, nextTurn, column);
        plugin.getServer().getScheduler().scheduleAsyncDelayedTask(plugin, checkWin, 20); //1 second later, should be more for fall time
    }
        
    public void onPlayerInteract(PlayerInteractEvent event){
        Action playerAction = event.getAction();
        if(playerAction == Action.LEFT_CLICK_AIR){
            Player player = event.getPlayer();
            if(Game.playerSand != null && Game.playerGravel != null){
                if(player == Game.playerSand && Game.currentState == Game.State.TURN_SAND){
                    Block targetBlock = player.getTargetBlock(null,40);
                    
                    //Check that they clicked on the board (TODO: Y direction)
                    if(targetBlock.getX() >= Locations.fallBarLeft.getX() && targetBlock.getX() <= Locations.fallBarRight.getX()){
                        Block dropBlock = Game.world.getBlockAt(
                                targetBlock.getX(), 
                                (int)Locations.fallBarLeft.getY(), 
                                (int)Locations.fallBarLeft.getZ() + 1);
                        dropBlock.setType(Material.SAND);
                        Game.changeState(Game.State.CHECK_WIN);
                        checkWin(Game.State.TURN_GRAVEL, targetBlock.getX());
                    }
                    return;
                }
                if(player == Game.playerGravel && Game.currentState == Game.State.TURN_GRAVEL){
                    Block targetBlock = player.getTargetBlock(null,40);
                    //Check that they clicked on the board (TODO: Y direction)
                    if(targetBlock.getX() >= Locations.fallBarLeft.getX() && targetBlock.getX() <= Locations.fallBarRight.getX()){
                        Block dropBlock = Game.world.getBlockAt(
                                targetBlock.getX(), 
                                (int)Locations.fallBarLeft.getY(), 
                                (int)Locations.fallBarLeft.getZ() + 1);
                        dropBlock.setType(Material.GRAVEL);
                        Game.changeState(Game.State.CHECK_WIN);
                        checkWin(Game.State.TURN_SAND, targetBlock.getX());
                    }
                    return;
                }
            } else if(Game.playerSand == event.getPlayer() && Game.playerGravel == null){
                player.sendMessage("Waiting for a gravel player.");
            } else if (Game.playerGravel == event.getPlayer() && Game.playerSand == null){
                player.sendMessage("Waiting for a sand player.");
            }
        }
    }//onPlayerInteract()
    
    
    public void onPlayerMove(PlayerMoveEvent event){
        //Check to see if they're our existing player first
        Player player = event.getPlayer();
        String playerName = player.getName();
        Location newLocation = event.getTo();
        Vector newVector = newLocation.toVector(); 
        
        
        Double distanceFromSand = newVector.distance(playerLocationSand);
        Double distanceFromGravel = newVector.distance(playerLocationGravel);
       
        //player.sendMessage(String.format("%sDistance from sand: %.3f, gravel: %.3f",ChatColor.DARK_GRAY,distanceFromSand, distanceFromGravel));
       
        //Check if they're a current player in the game, if so.. make sure they're still in their spots
        //  and they didn't want to leave
            if(Game.playerSand != null && playerName.equals(Game.playerSand.getName())){
                if(distanceFromSand > 2.3){
                    //they left
                    Game.playerSand = null;
                    player.sendMessage("You are no longer the sand player!");
                    
                    Game.changeState(Game.State.WAITING_PLAYERS);
                }
            }
            if(Game.playerGravel != null &&  playerName.equals(Game.playerGravel.getName())){
                if(distanceFromGravel > 2.3){
                    //they left
                    Game.playerGravel = null;
                    player.sendMessage("You are no longer the gravel player!");
                    //Check to see if the game is running before calling stop? Doesn't matter
                    //  but might be a better option
                    
                    Game.changeState(Game.State.WAITING_PLAYERS);
                }
            }
        
        if(!Game.gameStarted){ //If the game hasn't started
            //If they weren't one of our players, lets see if they're within range of the spot now
                if(distanceFromSand <= 2.3 && Game.playerSand == null){
                    //They're our new sand player!
                    Game.playerSand = player;
                    player.sendMessage("You are now the sand player!");
                    plugin.log.info(String.format("%s is now the sand player.",player.getName()));
                } 
                else if (distanceFromGravel <= 2.3 && Game.playerGravel == null){
                    Game.playerGravel = player;
                    player.sendMessage("You are now the gravel player!");
                    plugin.log.info(String.format("%s is now the gravel player.",player.getName()));
                }
                
            //Start the game if both plays are there
                if(!Game.gameStarted && Game.playerGravel != null && Game.playerSand!= null){
                    Game.changeState(Game.State.TURN_SAND); //Sand goes first, but maybe this will be random
                }
        }
    }//onPlayerMove()
    
}
