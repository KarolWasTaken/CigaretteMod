package net.karoll.cigarettemod;

import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.world.World;

import cpw.mods.fml.common.network.IGuiHandler;

public class CigaretteModGuiHandler implements IGuiHandler {

    public IInventory cigaretteCaseInventory = new InventoryBasic("Cigarette Case", false, 18);

    public static String UUIDOfInventoryToOpen;

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        if (ID == References.CIGARETTECASE_GUI_ID) {
            // return new ContainerCigaretteCase(player.inventory, cigaretteCaseInventory);
            return new ContainerChest(player.inventory, cigaretteCaseInventory);

        }
        return null;
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        if (ID == References.CIGARETTECASE_GUI_ID) {
            // return new GuiCigaretteCase(player.inventory, cigaretteCaseInventory);
            return new GuiChest(player.inventory, cigaretteCaseInventory);
        }
        return null;
    }
}
