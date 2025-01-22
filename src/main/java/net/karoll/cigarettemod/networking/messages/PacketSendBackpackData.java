package net.karoll.cigarettemod.networking.messages;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.karoll.cigarettemod.handlers.ClientDataHandler;

public class PacketSendBackpackData implements IMessage {
    private int slots;
    private String UID;
    public PacketSendBackpackData() {}
    public PacketSendBackpackData(int slots, String UID) {
        this.slots = slots;
        this.UID = UID;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, UID);
        buf.writeInt(slots);
    }
    @Override
    public void fromBytes(ByteBuf buf) {
        this.UID = ByteBufUtils.readUTF8String(buf);
        this.slots = buf.readInt();
    }

    public static class Handler implements IMessageHandler<PacketSendBackpackData, IMessage> {
        @Override
        public IMessage onMessage(final PacketSendBackpackData message, MessageContext ctx) {
            // Handle the response on the client side
            ClientDataHandler.setSlotsUsed(message.slots);
            ClientDataHandler.setUIDProcessing(message.UID);

            return null; // No response packet needed
        }
    }
}
