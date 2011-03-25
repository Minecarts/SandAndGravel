package com.minecarts.verrier.sandandgravel.game;

import com.minecarts.verrier.sandandgravel.*;

public class GameThread implements Runnable {
    private SandAndGravel plugin;
    private enum gameState {
        WAITING_PLAYERS,
        TURN_SAND,
        TURN_GRAVEL,
        CHECK_WIN,
        SCORE_UPDATE,
        GAME_OVER
    };
    
    private boolean gameInProgress = false; //If the game is in progress or not
    private String PlayerSand;
    private String PlayerGravel;
    
    public gameState currentState = gameState.WAITING_PLAYERS;
    public GameThread(SandAndGravel plugin){
        this.plugin = plugin;
    }
    
    public void run(){
        //Switch based upon game state
        plugin.log.info(String.format("Game State: %s",currentState.name()));
        switch(gameState.valueOf(currentState.name())){
            case WAITING_PLAYERS:
                //We're still waiting for more players
                break;
            default:
                plugin.log.info(String.format("Unhandled game state: %s",currentState.name()));
                break;
            
        }
    }
}
