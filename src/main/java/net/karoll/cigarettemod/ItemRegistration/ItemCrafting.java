package net.karoll.cigarettemod.ItemRegistration;

import static gregtech.api.recipe.RecipeMaps.*;
import static gregtech.api.util.GT_RecipeBuilder.SECONDS;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.enums.ToolDictNames;
import gregtech.api.util.GT_OreDictUnificator;
import tconstruct.library.crafting.DryingRackRecipes;

public class ItemCrafting {

    public static void init() {
        // making dried tobacco
        DryingRackRecipes.addDryingRecipe(ModItems.tobacco, 1200, ModItems.driedTobacco);

        ItemStack driedTobaccoStack = new ItemStack(ModItems.driedTobacco);
        Object craftingToolKnife = ToolDictNames.craftingToolKnife.name();
        /*
         * GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.cutDriedTobacco, 5),
         * "ST ",
         * "   ",
         * "   ",
         * 'S', craftingToolSaw,
         * 'T', driedTobaccoStack
         * ));
         */
        GameRegistry.addRecipe(
            new ShapelessOreRecipe(new ItemStack(ModItems.cutDriedTobacco, 5), craftingToolKnife, driedTobaccoStack));

        // macerator recipe
        /*
         * GT_Values.RA.stdBuilder()
         * .itemInputs(GT_ModHandler.getModItem(TinkerConstruct.ID, "woodPattern", 1L, GT_Values.W))
         * .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Wood, 1L)).outputChances(10000)
         * .duration(10 * SECONDS).eut(2).addTo(maceratorRecipes);
         */

        // making cellulose acetate fibers
        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Wood, 5L),
                ItemList.Shape_Extruder_Pipe_Tiny.get(0L))
            .itemOutputs(new ItemStack(ModItems.celluloseAcetate, 1))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(extruderRecipes);

        // making tow
        GT_Values.RA.stdBuilder()
            .itemInputs(new ItemStack(ModItems.celluloseAcetate, 5))
            .itemOutputs(new ItemStack(ModItems.tow, 1))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(compressorRecipes);

        // making filter rod
        GT_Values.RA.stdBuilder()
            .itemInputs(new ItemStack(ModItems.tow, 1))
            .itemOutputs(new ItemStack(ModItems.filterRod, 1))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(wiremillRecipes);

        // making filters
        ItemStack filterRod = new ItemStack(ModItems.filterRod, 1);
        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(ModItems.filter, 3), craftingToolKnife, filterRod));

        // cig box
        /*
         * GameRegistry.addRecipe(new ItemStack(ModItems.cigaretteCase, 1), new Object[] {
         * "P P",
         * "PCP",
         * "SRS",
         * 'P', OrePrefixes.plate.get(Materials.AnyIron),
         * 'C', Blocks.chest,
         * 'S', OrePrefixes.screw.get(Materials.AnyIron),
         * 'R', OrePrefixes.stick.get(Materials.AnyIron)
         * });
         */
        /*
         * GameRegistry.addRecipe(new ItemStack(ModItems.filterRod, 1),
         * "P P",
         * "PCP",
         * "SRS",
         * 'P', OrePrefixes.plate.get(Materials.AnyIron),
         * 'C', Blocks.chest,
         * 'S', OrePrefixes.screw.get(Materials.AnyIron),
         * 'R', OrePrefixes.stick.get(Materials.AnyIron));
         */
    }
}
