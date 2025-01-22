package net.karoll.cigarettemod.networking.messages;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.karoll.cigarettemod.CigaretteMod;
import net.karoll.cigarettemod.Constants;
import net.karoll.cigarettemod.handlers.ClientDataHandler;
import net.karoll.cigarettemod.save.BackpackSave;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.PacketBuffer;

public class PacketRequestBackpackData implements IMessage {
    private ItemStack itemStack;

    // Empty constructor is required for packet handling
    public PacketRequestBackpackData() {}

    public PacketRequestBackpackData(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        PacketBuffer packetBuffer = new PacketBuffer(buf);
        try {
            packetBuffer.writeItemStackToBuffer(itemStack);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        PacketBuffer packetBuffer = new PacketBuffer(buf);
        try {
            this.itemStack = packetBuffer.readItemStackFromBuffer();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static class Handler implements IMessageHandler<PacketRequestBackpackData, IMessage> {
        @Override
        public IMessage onMessage(final PacketRequestBackpackData message, MessageContext ctx) {
            // Ensure this runs on the server side

            // Handle the request on the server side
            EntityPlayerMP player = ctx.getServerHandler().playerEntity;
            ItemStack itemStack = message.itemStack;

            // Perform operations to get the backpack data
            BackpackSave backpackSave = new BackpackSave(itemStack);
            NBTTagList itemList = backpackSave.getInventory(Constants.NBT.INVENTORY_CIGARETTECASE);
            //ClientDataHandler.setSlotsUsed(itemList.tagCount());
            //ClientDataHandler.setUIDProcessing(backpackSave.getUUID());
            // Send the data back to the client
            CigaretteMod.packetHandler.INSTANCE.sendTo(new PacketSendBackpackData(itemList.tagCount(), backpackSave.getUUID()), player);

            return null; // No response packet needed
        }
    }
}
