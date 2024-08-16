package net.karoll.cigarettemod.Items.CigaretteCase;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

public class TileEntityCigaretteCase extends TileEntity implements IInventory {

    private ItemStack[] inventory = new ItemStack[18]; // Example size


    // do later
    // private String uniqueID;
    // public TileEntityCigaretteCase() {
    // this.uniqueID = java.util.UUID.randomUUID().toString();
    // }
    // public String getUniqueID() {
    // return uniqueID;
    // }

    // @Override
    // public void writeToNBT(NBTTagCompound compound) {
    // super.writeToNBT(compound);
    // compound.setString("UniqueID", uniqueID);
    // // Save inventory
    // for (int i = 0; i < inventory.length; i++) {
    // if (inventory[i] != null) {
    // NBTTagCompound itemTag = new NBTTagCompound();
    // inventory[i].writeToNBT(itemTag);
    // compound.setTag("Item" + i, itemTag);
    // }
    // }
    // }
    //
    //
    // @Override
    // public void readFromNBT(NBTTagCompound compound) {
    // super.readFromNBT(compound);
    // uniqueID = compound.getString("UniqueID");
    // // Load inventory
    // for (int i = 0; i < inventory.length; i++) {
    // if (compound.hasKey("Item" + i)) {
    // NBTTagCompound itemTag = compound.getCompoundTag("Item" + i);
    // inventory[i] = ItemStack.loadItemStackFromNBT(itemTag);
    // }
    // }
    // }

    public ItemStack[] getInventory() {
        return inventory;
    }

    @Override
    public int getSizeInventory() {
        return inventory.length;
    }

    @Override
    public ItemStack getStackInSlot(int slotIn) {
        return inventory[slotIn];
    }

    @Override
    public ItemStack decrStackSize(int index, int count) {
        if (inventory[index] != null) {
            ItemStack itemstack;
            if (inventory[index].stackSize <= count) {
                itemstack = inventory[index];
                inventory[index] = null;
                return itemstack;
            } else {
                itemstack = inventory[index].splitStack(count);
                if (inventory[index].stackSize == 0) {
                    inventory[index] = null;
                }
                this.markDirty();
                return itemstack;
            }
        } else {
            return null;
        }
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int index) {
        if (inventory[index] != null) {
            ItemStack itemstack = inventory[index];
            inventory[index] = null;
            return itemstack;
        }
        return null;
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        inventory[index] = stack;
        if (stack != null && stack.stackSize > getInventoryStackLimit()) {
            stack.stackSize = getInventoryStackLimit();
        }
        this.markDirty();
    }

    @Override
    public String getInventoryName() {
        return "Cigarette Case";
    }

    @Override
    public boolean hasCustomInventoryName() {
        return false;
    }

    @Override
    public void markDirty() {
        super.markDirty();
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer player) {
        // return worldObj.getTileEntity(xCoord, yCoord, zCoord) == this &&
        // player.getDistanceSq(xCoord + 0.5D, yCoord + 0.5D, zCoord + 0.5D) <= 64.0D;
        return true;
    }

    @Override
    public void openInventory() {

    }

    @Override
    public void closeInventory() {

    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        return false;
    }
}
