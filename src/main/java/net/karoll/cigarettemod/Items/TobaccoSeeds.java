package net.karoll.cigarettemod.Items;

import net.karoll.cigarettemod.ItemRegistration.ModBlocks;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemSeeds;

public class TobaccoSeeds extends ItemSeeds {

    public TobaccoSeeds() {
        super(ModBlocks.tobaccoPlant, Blocks.farmland);
        this.setUnlocalizedName("tobaccoSeeds");
        this.setTextureName("cigarettemod:tobaccoplant_seeds");
    }
}
