package net.karoll.cigarettemod.StatusEffects;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.karoll.cigarettemod.CigaretteMod;
import net.minecraft.client.Minecraft;
import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;

public class PotionCool extends Potion {

    private static final ResourceLocation potionIcons = new ResourceLocation(CigaretteMod.MODID, "textures/gui/cool_potion_icon.png");

    public PotionCool(int id, boolean isBadEffect, int liquidColor) {
        super(id, isBadEffect, liquidColor);
        this.setPotionName("Cool");
    }

    @Override
    public int getStatusIconIndex() {
        Minecraft.getMinecraft().getTextureManager().bindTexture(potionIcons);
        return super.getStatusIconIndex();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean hasStatusIcon() {
        return true; // This tells Minecraft that this potion has a custom icon
    }
}
