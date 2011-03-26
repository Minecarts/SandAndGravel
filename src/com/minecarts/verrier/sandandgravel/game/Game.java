package com.minecarts.verrier.sandandgravel.game;

import org.bukkit.entity.Player;
import org.bukkit.World;

public class Game {
    
    public static boolean gameStarted = false;
    public static State currentState = State.WAITING_PLAYERS;
    
    public static Player playerSand = null;
    public static Player playerGravel = null;
    
    public static World world = null;
    
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
}
