package net.karoll.cigarettemod.Items.CigaretteCase;

import net.karoll.cigarettemod.Items.cigarettes.CigaretteBase;
import net.minecraft.client.Minecraft;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;

public class CigaretteCaseSlot extends Slot {
    public CigaretteCaseSlot(IInventory inventory, int slotIndex, int xPos, int yPos) {
        super(inventory, slotIndex, xPos, yPos);
    }

    @Override
    public boolean isItemValid(ItemStack is) {
        //Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("§eisItemValid called with: " + is.toString()));
        return is != null && is.getItem() != null && is.getItem() instanceof CigaretteBase;
        //return true;
    }
}
