package net.karoll.cigarettemod.Blocks.Plants;

import java.util.Random;

import net.karoll.cigarettemod.ItemRegistration.ModItems;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCrops;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.world.World;

public class TobaccoPlant extends BlockCrops {

    private static final int GROWTH_TICKS = 5 * 200; // 5 seconds
    private int age;

    public TobaccoPlant() {
        super();
        this.setTickRandomly(true);
    }

    @Override
    protected Item func_149866_i() {
        return ModItems.tobaccoSeeds; // The seed item for the crop
    }

    @Override
    protected Item func_149865_P() {
        return ModItems.tobacco; // The item dropped when the crop is harvested
    }

    @Override
    public void updateTick(World worldIn, int x, int y, int z, Random rand) {
        super.updateTick(worldIn, x, y, z, rand);

        /*
         * if (worldIn.isRemote) {
         * return; // Don't do anything on client side
         * }
         * if (worldIn.getBlockLightValue(x, y + 1, z) >= 9)
         * {
         * int plantAge = worldIn.getBlockMetadata(x, y, z);
         * if (plantAge < 7)
         * {
         * float growthSpeed = this.getGrowthSpeed(worldIn, x, y, z);
         * if (rand.nextInt((int) (25.0F / growthSpeed) + 1) == 0)
         * {
         * ++plantAge;
         * worldIn.setBlockMetadataWithNotify(x, y, z, plantAge, 2);
         * }
         * }
         * }
         */
    }

    private float getGrowthSpeed(World world, int x, int y, int z) {
        float growthSpeed = 1.0F;
        Block soil = world.getBlock(x, y - 1, z);
        if (soil == Blocks.farmland) {
            growthSpeed *= 10.0F; // Increase growth speed by 10x
        }
        return growthSpeed;
    }

}
