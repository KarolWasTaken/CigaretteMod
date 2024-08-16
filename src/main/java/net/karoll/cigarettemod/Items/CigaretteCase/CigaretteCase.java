package net.karoll.cigarettemod.Items.CigaretteCase;

import net.karoll.cigarettemod.CigaretteMod;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class CigaretteCase extends Item {

     public CigaretteCase(){
        this.setMaxStackSize(1);
     }

    @Override
    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
        if (!world.isRemote) {
            player.openGui(CigaretteMod.instance, 420, world, (int) player.posX, (int) player.posY, (int) player.posZ);
        }
        return itemStack;
    }

}
