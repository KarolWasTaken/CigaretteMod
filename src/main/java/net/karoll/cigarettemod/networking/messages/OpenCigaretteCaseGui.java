package net.karoll.cigarettemod.networking.messages;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.karoll.cigarettemod.Constants;
import net.karoll.cigarettemod.Items.CigaretteCase.CigaretteCaseGui;
import net.karoll.cigarettemod.Items.CigaretteCase.CigaretteCaseInven;
import net.karoll.cigarettemod.handlers.GuiHandler;
import net.karoll.cigarettemod.save.BackpackSave;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.PacketBuffer;

import java.io.IOException;

/**
 * Network message to let the server know to save backpack changes
 */
public class OpenCigaretteCaseGui implements IMessage {

    protected int windowId = 0;
    // Empty constructor is required for packet handling
    public OpenCigaretteCaseGui() {}
    public OpenCigaretteCaseGui(int windowID) {
        windowId = windowID;
    }
    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(windowId);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        windowId = buf.readInt();
    }

    public static class Handler implements IMessageHandler<OpenCigaretteCaseGui, IMessage> {
        @Override
        public IMessage onMessage(OpenCigaretteCaseGui message, MessageContext ctx) {
            EntityPlayer entityPlayer = Minecraft.getMinecraft().thePlayer;
            Minecraft.getMinecraft().displayGuiScreen(new CigaretteCaseGui(entityPlayer.inventory, new CigaretteCaseInven(GuiHandler.heldItem)));
            entityPlayer.openContainer.windowId = message.windowId;

            return null;
        }
    }
}
