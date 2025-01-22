package net.karoll.cigarettemod;

import net.karoll.cigarettemod.ItemRegistration.ItemCrafting;
import net.karoll.cigarettemod.ItemRegistration.ModBlocks;
import net.karoll.cigarettemod.ItemRegistration.ModItems;
import net.karoll.cigarettemod.ItemRegistration.ModPotions;

import net.karoll.cigarettemod.handlers.GuiHandler;
import net.karoll.cigarettemod.handlers.SaveFileHandler;
import net.karoll.cigarettemod.networking.PacketHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.network.NetworkRegistry;

@Mod(modid = CigaretteMod.MODID, version = Tags.VERSION, name = "CigaretteMod", acceptedMinecraftVersions = "[1.7.10]")
public class CigaretteMod {

    public static final String MODID = "cigarettemod";
    public static final Logger LOG = LogManager.getLogger(MODID);

    @Mod.Instance(CigaretteMod.MODID)
    public static CigaretteMod instance;

    @SidedProxy(clientSide = "net.karoll.cigarettemod.ClientProxy", serverSide = "net.karoll.cigarettemod.CommonProxy")
    public static CommonProxy proxy;

    // save file stuff
    public static SaveFileHandler saveFileHandler = new SaveFileHandler();
    public static PacketHandler packetHandler = new PacketHandler();

    @Mod.EventHandler
    // preInit "Run before anything else. Read your config, create blocks, items, etc, and register them with the
    // GameRegistry." (Remove if not needed)
    public void preInit(FMLPreInitializationEvent event) {
        proxy.preInit(event);

        //GameRegistry.registerTileEntity(TileEntityCigaretteCase.class, "cigaretteCase");
        ModPotions.init();
        ModBlocks.init();
        ModItems.init();
    }

    @Mod.EventHandler
    // load "Do your mod setup. Build whatever data structures you care about. Register recipes." (Remove if not needed)
    public void init(FMLInitializationEvent event) {
        proxy.init(event);
        ItemCrafting.init();

        // Register the GUI handle
        NetworkRegistry.INSTANCE.registerGuiHandler(instance, new GuiHandler());
        packetHandler.init();
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

        // comment out when not debugging
        event.registerServerCommand(new DebugCommands());
    }
}
