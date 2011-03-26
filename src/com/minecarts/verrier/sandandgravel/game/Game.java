package com.minecarts.verrier.sandandgravel.game;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.Material;
import org.bukkit.World;
import java.util.logging.Logger;

public class Game {
    
    public static boolean gameStarted = false;
    public static State currentState = State.WAITING_PLAYERS;
    
    public static Player playerSand = null;
    public static Player playerGravel = null;
    
    public static World world = null;
    public static final Logger log = Logger.getLogger("Minecraft.SandAndGravel.Game");
    
    public static enum State {
        WAITING_PLAYERS,
        TURN_SAND,
        TURN_GRAVEL,
        CHECK_WIN,
        SCORE_UPDATE,
        GAME_TIE,
        WINNER_SAND,
        WINNER_GRAVEL,
    };
    
    public static void changeState(State newState){
        log.info(String.format("Changed Game State: %s, New State: %s",Game.currentState, newState));
        switch(State.valueOf(newState.name())){
            case WAITING_PLAYERS:
                gameStarted = false;
                break;
            case TURN_SAND:
                if(currentState == State.WAITING_PLAYERS){
                    gameStarted = true;
                }
                playerSand.sendMessage("It's your turn!");
                break;
            case TURN_GRAVEL:
                if(currentState == State.WAITING_PLAYERS){
                    gameStarted = true;
                }
                playerGravel.sendMessage("It's your turn!");
                break;
            case CHECK_WIN:
                //TODO: Check to see if a player won
                log.info("TODO: Check to see if a player won");
                break;
            default:
                log.info(String.format("Unhandled game state: %s",newState));
                break;
        }

        currentState = newState;
    }
    
    public static void clearBoard(){
        int z = Locations.gridTopLeft.getBlockZ() + 1;
        //Check for horizontal 4 in a row, from the bottom up
        for(int y = Locations.gridBottomRight.getBlockY(), yMax = y+6; y<yMax; y++){
            for(int x = Locations.gridTopLeft.getBlockX(), xMax = x+7; x<xMax; x++){
                Block b = Game.world.getBlockAt(x,y,z);
                b.setType(Material.AIR);
            }
        }
    }
}
