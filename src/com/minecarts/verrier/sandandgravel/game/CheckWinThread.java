package com.minecarts.verrier.sandandgravel.game;

import com.minecarts.verrier.sandandgravel.*;
import com.minecarts.verrier.sandandgravel.game.*;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

public class CheckWinThread implements Runnable {
    private SandAndGravel plugin;
    
    private Game.State nextTurn = Game.State.TURN_GRAVEL;

    public CheckWinThread(SandAndGravel plugin, Game.State nextTurn){
        this.plugin = plugin;
        this.nextTurn = nextTurn;
    }
        
    public void run(){
        int usedBlocks = 0;
        int z = Locations.gridTopLeft.getBlockZ() + 1;
        
        int sandCounter, gravelCounter;
        
        //Check for horizontal 4 in a row, from the bottom up
        for(int y = Locations.gridBottomRight.getBlockY(), yMax = y+6; y<yMax; y++){
            sandCounter = 0;
            gravelCounter = 0;
            for(int x = Locations.gridTopLeft.getBlockX(), xMax = x+7; x<xMax; x++){
                Block b = Game.world.getBlockAt(x,y,z);
                if(b.getType() == Material.SAND){
                    if(++sandCounter == 4){
                        Game.changeState(Game.State.WINNER_SAND);
                        return;
                    }
                    gravelCounter = 0;
                }
                
                if(b.getType() == Material.GRAVEL){
                    if(++gravelCounter == 4){
                        Game.changeState(Game.State.WINNER_GRAVEL);
                        return;
                    }
                    sandCounter = 0;
                }
                
                plugin.log.info(String.format("Counters: Sand [%s] Gravel [%s]", sandCounter, gravelCounter));
            }
        }
           
        //Check to see if the board is full
        //  we can do this by seeing if there are any spaces free at the top most layer
        for(int x = Locations.gridTopLeft.getBlockX(), xMax = x+7; x<xMax; x++){
            Block b = Game.world.getBlockAt(x,Locations.gridTopLeft.getBlockY(),z);
            if(b.getType() == Material.SAND || b.getType() == Material.GRAVEL){
                if(++usedBlocks == 7) Game.currentState = Game.State.GAME_TIE;
            }
        }
        
        
        
        //Else, no one won, so next turn it up
        Game.currentState = nextTurn;
    }
}
