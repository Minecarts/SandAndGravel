package com.minecarts.sandandgravel.game;

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
    public static com.minecarts.sandandgravel.SandAndGravel plugin = null;
    public static final Logger log = Logger.getLogger("com.minecarts.sandandgravel");

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
        log.finer(String.format("Changed Game State: %s, New State: %s",Game.currentState, newState));
        switch(State.valueOf(newState.name())){
            case WAITING_PLAYERS:
                gameStarted = false;
                Game.clearBoard(); 
                if(currentState == State.TURN_GRAVEL || currentState == State.TURN_SAND){
                    //Someone left the field, notify the players
                    if(playerSand != null){
                        playerSand.sendMessage(MessageFormatter.player.opponentLeft);
                    }
                    if(playerGravel != null){
                        playerGravel.sendMessage(MessageFormatter.player.opponentLeft);
                    }
                }
                break;
            case TURN_SAND:
                if(currentState == State.WAITING_PLAYERS){
                    gameStarted = true;
                    playerSand.sendMessage(MessageFormatter.game.gameBegin);
                    playerGravel.sendMessage(MessageFormatter.game.gameBegin);
                }
                playerSand.sendMessage(MessageFormatter.game.yourTurn);
                break;
            case TURN_GRAVEL:
                if(currentState == State.WAITING_PLAYERS){
                    gameStarted = true;
                    playerSand.sendMessage(MessageFormatter.game.gameBegin);
                    playerGravel.sendMessage(MessageFormatter.game.gameBegin);
                }
                playerGravel.sendMessage(MessageFormatter.game.yourTurn);
                break;
            case CHECK_WIN:
                //Doesn't really do anything, since it's handled in a thread
                break;
            case WINNER_GRAVEL:
                //TODO: Keep track of score (per player)
                Game.playerGravel.sendMessage(MessageFormatter.game.playerWon("Congratulations! You"));
                Game.playerSand.sendMessage(MessageFormatter.game.playerWon("Gravel"));
                Game.resetGame(5);
                break;
            case WINNER_SAND:
                //TODO: Keep track of score (per player)
                Game.playerSand.sendMessage(MessageFormatter.game.playerWon("Congratulations! You"));
                Game.playerGravel.sendMessage(MessageFormatter.game.playerWon("Sand"));
                Game.resetGame(5);
                break;
            default:
                log.info(String.format("Unhandled game state: %s",newState));
                break;
        }
        currentState = newState;
    }

    public static void checkWin(Game.State nextTurn, int column){
        Game.changeState(Game.State.CHECK_WIN);
        Runnable checkWin = new com.minecarts.sandandgravel.game.CheckWinThread(plugin, nextTurn, column);
        plugin.getServer().getScheduler().scheduleAsyncDelayedTask(plugin, checkWin, 20); //1 second later, should be more for fall time
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

    //Clear the game after X seconds
    public static void resetGame(int seconds){
        ResetGameDelay resetGameDelay = new ResetGameDelay();
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, resetGameDelay, seconds * 20);
    }
    private static class ResetGameDelay implements Runnable{
        public void run(){
            Game.clearBoard();
            if(Game.playerGravel != null && Game.playerSand != null){
                Game.changeState(Game.State.TURN_SAND);
            }
        }
    }
}
