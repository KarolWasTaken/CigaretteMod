package net.karoll.cigarettemod.networking.messages;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.karoll.cigarettemod.Constants;
import net.karoll.cigarettemod.save.BackpackSave;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.PacketBuffer;

import java.io.IOException;

/**
 * Network message to let the server know to save backpack changes
 */
public class PacketSendInventory implements IMessage {
    private NBTTagList inventoryTagList;
    private String UID;

    // Empty constructor is required for packet handling
    public PacketSendInventory() {}

    public PacketSendInventory(NBTTagList tagList, String UID) {
        this.inventoryTagList = tagList;
        this.UID = UID;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        PacketBuffer packetBuffer = new PacketBuffer(buf);
        NBTTagCompound compound = new NBTTagCompound();
        compound.setTag("Inventory", this.inventoryTagList);
        try {
            packetBuffer.writeNBTTagCompoundToBuffer(compound);
        } catch (IOException e) {
            e.printStackTrace();
        }

        ByteBufUtils.writeUTF8String(buf, UID);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        PacketBuffer packetBuffer = new PacketBuffer(buf);
        NBTTagCompound compound;
        try {
            compound = packetBuffer.readNBTTagCompoundFromBuffer();
            this.inventoryTagList = compound.getTagList("Inventory", 10);
        } catch (Exception e) {
            e.printStackTrace();
        }

        this.UID = ByteBufUtils.readUTF8String(buf);
    }

    public static class Handler implements IMessageHandler<PacketSendInventory, IMessage> {
        @Override
        public IMessage onMessage(PacketSendInventory message, MessageContext ctx) {
            /*// Run on server thread
            ctx.getServerHandler().playerEntity.getServerForPlayer().addScheduledTask(() -> {
                // The code that handles the inventory save
                // For example:
                BackpackSave backpackSave = new BackpackSave(message.UID);
                backpackSave.setInventory(Constants.NBT.INVENTORY_CIGARETTECASE, message.inventoryTagList);
            });
            return null; // No response packet needed*/

//            final EntityPlayerMP player = ctx.getServerHandler().playerEntity;
//            player.getServerForPlayer().scheduleTask(new Runnable() {
//                @Override
//                public void run() {
//                    // The code that handles the inventory save
//                    // For example:
//                    BackpackSave backpackSave = new BackpackSave(message.UID);
//                    backpackSave.setInventory(Constants.NBT.INVENTORY_CIGARETTECASE, message.inventoryTagList);
//                }
//            });
//            return null; // No response packet needed
            BackpackSave backpackSave = new BackpackSave(message.UID);
            backpackSave.setInventory(Constants.NBT.INVENTORY_CIGARETTECASE, message.inventoryTagList);
            return null;
        }
    }
}
