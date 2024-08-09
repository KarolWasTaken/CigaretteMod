package net.karoll.cigarettemod.ItemRegistration;

import net.karoll.cigarettemod.Blocks.Plants.TobaccoPlant;
import net.minecraft.block.Block;

import cpw.mods.fml.common.registry.GameRegistry;

public class ModBlocks {

    public static Block tobaccoPlant;

    public static void init() {
        tobaccoPlant = new TobaccoPlant().setBlockName("tobaccoPlant")
            .setBlockTextureName("cigarettemod:wheat");

        GameRegistry.registerBlock(tobaccoPlant, "tobaccoPlant");
    }

}
