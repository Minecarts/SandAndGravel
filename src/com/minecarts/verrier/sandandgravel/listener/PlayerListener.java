package com.minecarts.verrier.sandandgravel.listener;

import com.minecarts.verrier.sandandgravel.*;

import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerListener extends org.bukkit.event.player.PlayerListener{
    private SandAndGravel plugin;
    public PlayerListener(SandAndGravel plugin){
        this.plugin = plugin;
    }
    
    public void onPlayerMove(PlayerMoveEvent event){
        //Check to see if this player is in range of our player positions
        //If they are, see if there's already another player there.. if there is, ignore it
        //else... set this player to the current player
        
        //If both players have someone there, start the game! 
        plugin.log.info("Move: " + plugin.game.currentState);
    }
    
}
