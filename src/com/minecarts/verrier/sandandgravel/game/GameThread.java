package com.minecarts.verrier.sandandgravel.game;

import com.minecarts.verrier.sandandgravel.*;
import com.minecarts.verrier.sandandgravel.game.Game;

import org.bukkit.World;

public class GameThread implements Runnable {
    private SandAndGravel plugin;
    public GameThread(SandAndGravel plugin, World world){
        this.plugin = plugin;
    }
        
    //Main game loop
    public void run(){
        //Switch based upon game state
        plugin.log.info(String.format("Game State: %s",Game.currentState));
        switch(Game.State.valueOf(Game.currentState.name())){
            case WAITING_PLAYERS:
                //We're still waiting for more players
                break;
            case TURN_SAND:
                Game.playerSand.sendMessage("It's your turn!");
                break;
            case TURN_GRAVEL:
                Game.playerGravel.sendMessage("It's your turn!");
                break;
            case CHECK_WIN:
                //TODO: Check to see if a player won
                plugin.log.info("TODO: Check to see if a player won");
                break;
            default:
                plugin.log.info(String.format("Unhandled game state: %s",Game.currentState));
                break;
            
        }
    }
}
