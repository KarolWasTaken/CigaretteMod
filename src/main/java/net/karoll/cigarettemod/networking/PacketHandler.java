package net.karoll.cigarettemod.networking;

import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;
import net.karoll.cigarettemod.CigaretteMod;
import net.karoll.cigarettemod.networking.messages.*;

public class PacketHandler {
    public final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(CigaretteMod.MODID);

    public void init() {
        // Server
        INSTANCE.registerMessage(PacketSendInventory.Handler.class, PacketSendInventory.class, 0, Side.SERVER);
        INSTANCE.registerMessage(PacketRequestBackpackData.Handler.class, PacketRequestBackpackData.class, 1, Side.SERVER);

        // Client
        INSTANCE.registerMessage(PacketSendBackpackData.Handler.class, PacketSendBackpackData.class, 2, Side.CLIENT);
        INSTANCE.registerMessage(OpenCigaretteCaseGui.Handler.class, OpenCigaretteCaseGui.class, 3, Side.CLIENT);
    }
}
