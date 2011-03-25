package com.minecarts.verrier.sandandgravel.game;

import com.minecarts.verrier.sandandgravel.*;

public class GameThread implements Runnable {
    private SandAndGravel plugin;
    public GameThread(SandAndGravel plugin){
        this.plugin = plugin;
    }
    
    public void run(){
        plugin.log.info("DoGameStuff");
    }
}
