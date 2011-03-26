package com.minecarts.verrier.sandandgravel.game;

import com.minecarts.verrier.sandandgravel.*;
import com.minecarts.verrier.sandandgravel.game.*;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.util.Vector;

public class CheckWinThread implements Runnable {
    private SandAndGravel plugin;
    
    private Game.State nextTurn = Game.State.TURN_GRAVEL;
    private int column; //Column where the last block was placed

    private final Vector checkHorizRight = new Vector(1,0,0); 
    private final Vector checkHorizLeft = new Vector(-1,0,0);
    private final Vector checkVertUp = new Vector(0,1,0);
    private final Vector checkVertDown = new Vector(0,-1,0);
    private final Vector checkUpLeft = new Vector(-1,1,0);
    private final Vector checkUpRight = new Vector(1,1,0);
    private final Vector checkDownLeft = new Vector(1,-1,0);
    private final Vector checkDownRight = new Vector(-1,-1,0);
    
    
    public CheckWinThread(SandAndGravel plugin, Game.State nextTurn, int column){
        this.plugin = plugin;
        this.nextTurn = nextTurn;
        this.column = column;
    }
    
    private boolean findMove(Vector position, Vector direction, int moves, Material blockType) throws WinnerException{
        Block b = Game.world.getBlockAt(position.toLocation(Game.world));
        //plugin.log.info(String.format("Looking for [%s]: %s, %s, %s (%s) [Move: %s]",blockType,position.getBlockX(), position.getBlockY(), position.getBlockZ(),b.getType(),moves));
        if(b.getType() == blockType){
            if(moves == 4){
                plugin.log.info("WINNING MOVE FOUND!" + direction);
                throw new WinnerException(blockType, direction);
            } else {
                return findMove(position.add(direction),direction,++moves,blockType);
            }
        }
        return false;
    }
    
    class WinnerException extends Exception{
        Material blockType;
        Vector direction;
        public WinnerException(Material blockType, Vector direction){
            super();
            this.blockType = blockType;
            this.direction = direction;
        }
    }
    
    
    public void run(){
        int usedBlocks = 0;
        int z = Locations.gridTopLeft.getBlockZ() + 1;
        
        int sandCounter, gravelCounter;
        
        //Find the top most block of the column (the one the player just placed)
        for(int y = Locations.gridTopLeft.getBlockY(), yMin = y-6; y>yMin; y--){
            Block b = Game.world.getBlockAt(column,y,z);
            Material blockType = b.getType();
            if(blockType == Material.SAND || blockType == Material.GRAVEL){
                //We have our top block, the one that was just placed
                try{
                    findMove(b.getLocation().toVector(),checkHorizRight,1,blockType); // +x
                    findMove(b.getLocation().toVector(),checkHorizLeft,1,blockType); // -x
    
                    findMove(b.getLocation().toVector(),checkVertUp,1,blockType); // +y
                    findMove(b.getLocation().toVector(),checkVertDown,1,blockType); // -y
                    
                    findMove(b.getLocation().toVector(),checkUpLeft,1,blockType);
                    findMove(b.getLocation().toVector(),checkUpRight,1,blockType); 
                    findMove(b.getLocation().toVector(),checkDownLeft,1,blockType); 
                    findMove(b.getLocation().toVector(),checkDownRight,1,blockType); 
                } catch(WinnerException e){ //We have a wining move!
                    //Change the blocks to gold blocks
                    for(int i = 0; i<4;i++){
                        Vector temp = e.direction.clone();
                        Block bl = Game.world.getBlockAt(b.getLocation().toVector().add(temp.multiply(i)).toLocation(Game.world));
                        //plugin.log.info(String.format("[%s] Setting block %s, %s, %s [%s]", i, bl.getLocation().getBlockX(), bl.getLocation().getBlockY(), bl.getLocation().getBlockZ(), e.direction));
                        bl.setType(Material.GOLD_BLOCK);
                       
                    }                    
                    
                    if(e.blockType == Material.GRAVEL){
                        Game.changeState(Game.State.WINNER_GRAVEL);
                    } else if (e.blockType == Material.SAND){
                        Game.changeState(Game.State.WINNER_SAND);
                    } else {
                        plugin.log.warning("Unknown SandAndGravel block for winner: " + e.blockType);
                    }
                }

                //Check all the directionals
                    //
            }
        }
        
        //Check for any matches to the block just placed
        
        
        /*
        //Check for horizontal 4 in a row, from the bottom up
        for(int y = Locations.gridBottomRight.getBlockY(), yMax = y+6; y<yMax; y++){
            sandCounter = 0;
            gravelCounter = 0;
            for(int x = Locations.gridTopLeft.getBlockX(), xMax = x+7; x<xMax; x++){
                Block b = Game.world.getBlockAt(x,y,z);
                if(b.getType() == Material.SAND){
                    if(++sandCounter == 4){ 
                        //Change the 4 blocks to gold
                        for(int i=x;i>x-4;i--){
                            Game.world.getBlockAt(i,y,z).setType(Material.GOLD_BLOCK);
                        }
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
            }
        }
       
        
        //Check for vertical wins
        for(int x = Locations.gridTopLeft.getBlockX(), xMax = x+7; x<xMax; x++){
            sandCounter = 0;
            gravelCounter = 0;
            for(int y = Locations.gridBottomRight.getBlockY(), yMax = y+6; y<yMax; y++){
                Block b = Game.world.getBlockAt(x,y,z);
                if(b.getType() == Material.SAND){
                    if(++sandCounter == 4){
                        //Change the 4 blocks to gold
                        for(int i=y;i>y-4;i--){
                            Game.world.getBlockAt(x,i,z).setType(Material.GOLD_BLOCK);
                        }
                       
                            
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
            }
        }
        
        
        //Check diagonals
        */
         
           
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
