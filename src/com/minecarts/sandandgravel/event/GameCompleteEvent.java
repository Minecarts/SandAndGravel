package com.minecarts.sandandgravel.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;

import com.minecarts.sandandgravel.game.Game.State;

public class GameCompleteEvent extends Event implements Cancellable
{
    private boolean cancel = false;
    private Player sand;
    private Player gravel;
    private State gameState;
    
    public GameCompleteEvent(State gameState, Player sand, Player gravel){
      super("GameCompleteEvent");
      this.gameState = gameState;
      this.sand = sand;
      this.gravel = gravel;
    }
    
    public Player getPlayerSand(){
        return this.sand;
    }
    public Player getPlayerGravel(){
        return this.gravel;
    }
    
    public State getGameState(){
        return this.gameState;
    }
    
    public boolean isCancelled(){ return this.cancel; }
    public void setCancelled(boolean cancel){ this.cancel = cancel; }
}
