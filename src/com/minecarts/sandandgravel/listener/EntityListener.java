package com.minecarts.sandandgravel.listener;

import com.minecarts.sandandgravel.SandAndGravel;

public class EntityListener extends org.bukkit.event.entity.EntityListener{
    private SandAndGravel plugin;
    public EntityListener(SandAndGravel plugin){
        this.plugin = plugin;
    }
}
