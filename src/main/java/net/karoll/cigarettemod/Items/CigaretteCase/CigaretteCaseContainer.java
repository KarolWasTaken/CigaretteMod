package net.karoll.cigarettemod.Items.CigaretteCase;

import net.karoll.cigarettemod.ItemRegistration.ModItems;
import net.karoll.cigarettemod.Items.cigarettes.CigaretteBase;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.karoll.cigarettemod.Items.CigaretteCase.CigaretteCaseSlot;
import net.minecraft.util.MathHelper;

import java.util.Set;

public class CigaretteCaseContainer extends Container {

    private IInventory CigaretteCaseInventory;
    private int numRows;

    public CigaretteCaseContainer(IInventory playerInven, IInventory cigCase) {
        this.CigaretteCaseInventory = cigCase;
        this.numRows = cigCase.getSizeInventory() / 9;
        int i = (this.numRows - 4) * 18;
        int j;
        int k;
        int slotCount = -1;
        // cig case
        for (j = 0; j < this.numRows; ++j) {
            for (k = 0; k < 9; ++k) {
                slotCount++;
                this.addSlotToContainer(new net.karoll.cigarettemod.Items.CigaretteCase.CigaretteCaseSlot(cigCase, k + j * 9, 8 + k * 18, 18 + j * 18)); //k + j * 9
                //Minecraft.getMinecraft().thePlayer.sendChatMessage("Cigarette slot index: " + k + j * 9);
            }
        }

        // inven
        for (j = 0; j < 3; ++j) {
            for (k = 0; k < 9; ++k) {
                this.addSlotToContainer(new Slot(playerInven, k + j * 9 + 9, 8 + k * 18, 103 + j * 18 + i));
            }
        }

        // hotbar
        for (j = 0; j < 9; ++j) {
            this.addSlotToContainer(new Slot(playerInven, j, 8 + j * 18, 161 + i));
        }
    }

    public boolean canInteractWith(EntityPlayer player) {
        return this.CigaretteCaseInventory.isUseableByPlayer(player);
    }

    /**
     * Called when a player shift-clicks on a slot. You must override this or you will crash when someone does that.
     */
    public ItemStack transferStackInSlot(EntityPlayer player, int index) {
        ItemStack itemstack = null;
        Slot slot = (Slot) this.inventorySlots.get(index);

        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            // Check if the slot is in the CigaretteCase inventory
            if (index < this.numRows * 9) {
                if (!this.mergeItemStack(itemstack1, this.numRows * 9, this.inventorySlots.size(), true)) {
                    return null;
                }
                slot.onSlotChange(itemstack1, itemstack);
            } else if(itemstack1.getItem() instanceof CigaretteBase) {
                if (!this.mergeItemStack(itemstack1, 0, this.numRows * 9, false)) {
                    return null;
                }
            }

            if (itemstack1.stackSize == 0) {
                slot.putStack(null);
            } else {
                slot.onSlotChanged();
            }

            if (itemstack1.stackSize == itemstack.stackSize)
            {
                return null;
            }
            slot.onPickupFromSlot(player, itemstack1);
        }

        return itemstack;
    }

    public void onContainerClosed(EntityPlayer p_75134_1_) {
        super.onContainerClosed(p_75134_1_);
        this.CigaretteCaseInventory.closeInventory();
    }

    @Override
    public void putStacksInSlots(ItemStack[] itemStacks) {
        super.putStacksInSlots(itemStacks);
    }

    @Override
    public void putStackInSlot(int slotID, ItemStack itemStack) {
        super.putStackInSlot(slotID, itemStack);
    }

    /**
     * Return this chest container's lower chest inventory.
     */
    /*public IInventory getLowerChestInventory()
    {
        return this.CigaretteCaseInventory;
    }*/
    /*@Override
    public ItemStack slotClick(int slotId, int clickedButton, int mode, EntityPlayer player) {
        //return super.slotClick(slotId, clickedButton, mode, player);
    }*/
}
