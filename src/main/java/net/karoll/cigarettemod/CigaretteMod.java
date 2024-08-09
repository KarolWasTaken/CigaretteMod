package net.karoll.cigarettemod;

import cpw.mods.fml.common.registry.GameRegistry;
import net.karoll.cigarettemod.ItemRegistration.ItemCrafting;
import net.karoll.cigarettemod.ItemRegistration.ModBlocks;
import net.karoll.cigarettemod.ItemRegistration.ModItems;

import net.minecraft.item.ItemStack;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import net.karoll.cigarettemod.ItemRegistration.ModPotions;

import static gregtech.api.enums.Mods.SpiceOfLife;
import static gregtech.api.util.GT_ModHandler.getModItem;


@Mod(modid = CigaretteMod.MODID, version = Tags.VERSION, name = "CigaretteMod", acceptedMinecraftVersions = "[1.7.10]")
public class CigaretteMod {

    public static final String MODID = "cigarettemod";
    public static final Logger LOG = LogManager.getLogger(MODID);

    @SidedProxy(clientSide = "net.karoll.cigarettemod.ClientProxy", serverSide = "net.karoll.cigarettemod.CommonProxy")
    public static CommonProxy proxy;

    @Mod.EventHandler
    // preInit "Run before anything else. Read your config, create blocks, items, etc, and register them with the
    // GameRegistry." (Remove if not needed)
    public void preInit(FMLPreInitializationEvent event) {
        proxy.preInit(event);

        ModPotions.init();
        ModBlocks.init();
        ModItems.init();
    }

    @Mod.EventHandler
    // load "Do your mod setup. Build whatever data structures you care about. Register recipes." (Remove if not needed)
    public void init(FMLInitializationEvent event) {
        proxy.init(event);
        ItemCrafting.init();
    }

    @Mod.EventHandler
    // postInit "Handle interaction with other mods, complete your setup based on this." (Remove if not needed)
    public void postInit(FMLPostInitializationEvent event) {
        proxy.postInit(event);
    }

    @Mod.EventHandler
    // register server commands in this event handler (Remove if not needed)
    public void serverStarting(FMLServerStartingEvent event) {
        proxy.serverStarting(event);
    }

    
}
