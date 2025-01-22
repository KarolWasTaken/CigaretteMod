package net.karoll.cigarettemod.handlers;

import net.karoll.cigarettemod.Items.CigaretteCase.CigaretteCaseInven;
import net.karoll.cigarettemod.Items.CigaretteCase.CigaretteCaseContainer;
import net.karoll.cigarettemod.Items.CigaretteCase.CigaretteCaseGui;
import net.karoll.cigarettemod.References;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import cpw.mods.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler {
    public static ItemStack heldItem;
    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        if (ID == References.CIGARETTECASE_GUI_ID) {
            if (player instanceof EntityPlayerMP) {
                EntityPlayerMP playerMP = (EntityPlayerMP) player;

                // prepareplayer
                if (playerMP.openContainer != playerMP.inventoryContainer) {
                    playerMP.closeScreen();
                }
                playerMP.getNextWindowId();

                playerMP.openContainer = new CigaretteCaseContainer(player.inventory, new CigaretteCaseInven(heldItem));
                playerMP.openContainer.windowId = playerMP.currentWindowId;
                playerMP.openContainer.addCraftingToCrafters(playerMP);
                // Perform operations using playerMP
            }
            //return new CigaretteCaseContainer(player.inventory, new CigaretteCaseInven(heldItem));
        }
        return null;
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        if (ID == References.CIGARETTECASE_GUI_ID) {

            Minecraft.getMinecraft().displayGuiScreen(new CigaretteCaseGui(player.inventory, new CigaretteCaseInven(heldItem)));
            //return new CigaretteCaseGui(player.inventory, new CigaretteCaseInven(heldItem));
        }
        return null;
    }
}
