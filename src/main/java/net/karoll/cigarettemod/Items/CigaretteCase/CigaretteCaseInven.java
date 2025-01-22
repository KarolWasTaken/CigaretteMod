package net.karoll.cigarettemod.Items.CigaretteCase;

import net.karoll.cigarettemod.CigaretteMod;
import net.karoll.cigarettemod.Constants;
import net.karoll.cigarettemod.Items.cigarettes.CigaretteBase;
import net.karoll.cigarettemod.networking.messages.PacketSendInventory;
import net.karoll.cigarettemod.save.BackpackSave;
import net.karoll.cigarettemod.utils.NBTItemStackUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ChatComponentText;

public class CigaretteCaseInven extends InventoryBasic {

    //private ItemStack cigaretteCase;
    private String UID;
    private BackpackSave backpackSave;
    public CigaretteCaseInven(ItemStack itemHeld) {
        super("Cigarette Case", false, 18);

        //cigaretteCase = itemHeld;
        if(!NBTItemStackUtil.hasTag(itemHeld, Constants.NBT.UID)) {
            // item does not have uid
            throw new RuntimeException("Cigarette Case does not have UID");
        }
        UID = NBTItemStackUtil.getString(itemHeld, Constants.NBT.UID);
        backpackSave = new BackpackSave(UID);
        loadInventoryFromNBT(backpackSave.getInventory(Constants.NBT.INVENTORY_CIGARETTECASE));
    }


    public void loadInventoryFromNBT(NBTTagList p_70486_1_) {
        // DEBUG HERE
        //Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("§2=========== loadInventoryFromNBT() ========="));
        //Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("§2Loading inventory from NBT:"));
        int i;

        for (i = 0; i < this.getSizeInventory(); ++i) {
            this.setInventorySlotContents(i, (ItemStack) null);
        }

        for (i = 0; i < p_70486_1_.tagCount(); ++i) {
            NBTTagCompound nbttagcompound = p_70486_1_.getCompoundTagAt(i);
            int j = nbttagcompound.getByte("Slot") & 255;
            if (j >= 0 && j < this.getSizeInventory()) {
                this.setInventorySlotContents(j, ItemStack.loadItemStackFromNBT(nbttagcompound));

                // DEBUG HERE
                ItemStack item = ItemStack.loadItemStackFromNBT(nbttagcompound);
                /*if(item != null) {
                    Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("§a- "
                        +ItemStack.loadItemStackFromNBT(nbttagcompound).getDisplayName()
                        +"x"
                        +ItemStack.loadItemStackFromNBT(nbttagcompound).stackSize));
                }
                else {
                    Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("§4- NULL ITEM"));
                }*/
            }
        }
        //Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("§2=========================================="));
    }
    public NBTTagList saveInventoryToNBT() {
        //Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("§9========== saveInventoryFromNBT() =========="));
        //Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("§9saving inventory into NBT"));
        NBTTagList nbttaglist = new NBTTagList();

        for (int i = 0; i < this.getSizeInventory(); ++i) {
            ItemStack itemstack = this.getStackInSlot(i);

            if (itemstack != null) {

                NBTTagCompound nbttagcompound = new NBTTagCompound();
                nbttagcompound.setByte("Slot", (byte) i);
                itemstack.writeToNBT(nbttagcompound);
                nbttaglist.appendTag(nbttagcompound);
                /*Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("§1- "
                    +itemstack.getDisplayName()
                    +"x"
                    +itemstack.stackSize));*/
            }
        }

        //Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("§9=========================================="));
        return nbttaglist;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer player)
    {
        return true;
    }

    @Override
    public void openInventory()
    {
        loadInventoryFromNBT(backpackSave.getInventory(Constants.NBT.INVENTORY_CIGARETTECASE));
        super.openInventory();
    }

    @Override
    public void closeInventory()
    {
        NBTTagList taglist = saveInventoryToNBT();
        // tells server to save changes
        CigaretteMod.packetHandler.INSTANCE.sendToServer(new PacketSendInventory(taglist, UID));
        super.closeInventory();
    }

    /*@Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        if(stack == null || stack.getItem() instanceof CigaretteBase) {
            super.setInventorySlotContents(index, stack);
        }
    }*/

    /*@Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        return stack == null || stack.getItem() instanceof CigaretteBase;
    }*/

}
