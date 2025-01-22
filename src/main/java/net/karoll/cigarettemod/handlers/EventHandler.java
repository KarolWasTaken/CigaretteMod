package net.karoll.cigarettemod.handlers;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.karoll.cigarettemod.CigaretteMod;
import net.minecraftforge.event.world.WorldEvent;

public class EventHandler {

    @SubscribeEvent
    public void worldLoad(WorldEvent.Load event) {
        CigaretteMod.saveFileHandler.init();
    }

}
