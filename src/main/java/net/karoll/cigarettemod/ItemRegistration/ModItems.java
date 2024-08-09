package net.karoll.cigarettemod.ItemRegistration;

import net.karoll.cigarettemod.CigaretteMod;
import net.karoll.cigarettemod.Items.ItemTiers;
import net.karoll.cigarettemod.Items.TobaccoSeeds;
import net.karoll.cigarettemod.Items.cigarettes.CigaretteBase;
import net.karoll.cigarettemod.Items.cigarettes.CigaretteButtBase;
import net.karoll.cigarettemod.Items.filters.Filter;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

import cpw.mods.fml.common.registry.GameRegistry;

public class ModItems {

    // Declare static item variables
    public static Item tobaccoSeeds;
    public static Item tobacco;
    public static Item driedTobacco;
    public static Item cutDriedTobacco;

    public static Item celluloseAcetate;
    public static Item tow;
    public static Item filterRod;
    public static Item filter;
    public static Item cigaretteCase;

    public static CigaretteBase crudeCigarette;
    public static CigaretteBase basicCigarette;
    public static CigaretteBase mediocreCigarette;
    public static CigaretteBase finestCigarette;
    public static CigaretteButtBase crudeCigaretteButt;
    public static CigaretteButtBase basicCigaretteButt;
    public static CigaretteButtBase mediocreCigaretteButt;
    public static CigaretteButtBase finestCigaretteButt;

    // Initialize and register items
    public static void init() {
        // Create and configure tobacco seeds item
        tobaccoSeeds = new TobaccoSeeds().setUnlocalizedName("tobaccoSeeds")
            .setTextureName(CigaretteMod.MODID + ":tobaccoplant_seeds");
        GameRegistry.registerItem(tobaccoSeeds, "tobaccoSeeds");

        // Create and configure tobacco item
        tobacco = new Item().setUnlocalizedName("tobacco")
            .setTextureName(CigaretteMod.MODID + ":tobacco")
            .setCreativeTab(CreativeTabs.tabMisc);
        GameRegistry.registerItem(tobacco, "tobacco");

        driedTobacco = new Item().setUnlocalizedName("driedTobacco")
            .setTextureName(CigaretteMod.MODID + ":dried_tobacco")
            .setCreativeTab(CreativeTabs.tabMisc);
        GameRegistry.registerItem(driedTobacco, "driedTobacco");

        cutDriedTobacco = new Item().setUnlocalizedName("cutDriedTobacco")
            .setTextureName(CigaretteMod.MODID + ":cut_dried_tobacco")
            .setCreativeTab(CreativeTabs.tabMisc);
        GameRegistry.registerItem(cutDriedTobacco, "cutDriedTobacco");

        celluloseAcetate = new Item().setUnlocalizedName("celluloseAcetate")
            .setTextureName(CigaretteMod.MODID + ":cellulose_acetate")
            .setCreativeTab(CreativeTabs.tabMisc);
        GameRegistry.registerItem(celluloseAcetate, "celluloseAcetate");

        tow = new Item().setUnlocalizedName("tow")
            .setTextureName(CigaretteMod.MODID + ":tow")
            .setCreativeTab(CreativeTabs.tabMisc);
        GameRegistry.registerItem(tow, "tow");

        filterRod = new Item().setUnlocalizedName("filterRod")
            .setTextureName(CigaretteMod.MODID + ":filter_rod")
            .setCreativeTab(CreativeTabs.tabMisc);
        GameRegistry.registerItem(filterRod, "filterRod");

        Filter basic_filter = new Filter(ItemTiers.Tier.basic);
        filter = basic_filter.setUnlocalizedName("filter")
            .setTextureName(basic_filter.GetTextureName())
            .setCreativeTab(CreativeTabs.tabMisc);
        GameRegistry.registerItem(filter, "filter");

        // cogarette butts
        crudeCigaretteButt = new CigaretteButtBase(ItemTiers.Tier.crude);
        basicCigaretteButt = new CigaretteButtBase(ItemTiers.Tier.basic);
        mediocreCigaretteButt = new CigaretteButtBase(ItemTiers.Tier.mediocre);
        finestCigaretteButt = new CigaretteButtBase(ItemTiers.Tier.finest);
        // cigarettes
        crudeCigarette = new CigaretteBase(ItemTiers.Tier.crude);
        basicCigarette = new CigaretteBase(ItemTiers.Tier.basic);
        mediocreCigarette = new CigaretteBase(ItemTiers.Tier.mediocre);
        finestCigarette = new CigaretteBase(ItemTiers.Tier.finest);

        crudeCigarette.setUnlocalizedName("crudeCigarette")
            .setTextureName(CigaretteMod.MODID + ":cigarette_crude")
            .setCreativeTab(CreativeTabs.tabMisc);
        GameRegistry.registerItem(crudeCigarette, "crudeCigarette");

        basicCigarette.setUnlocalizedName("basicCigarette")
            .setTextureName(CigaretteMod.MODID + ":cigarette_basic")
            .setCreativeTab(CreativeTabs.tabMisc);
        GameRegistry.registerItem(basicCigarette, "basicCigarette");

        mediocreCigarette.setUnlocalizedName("mediocreCigarette")
            .setTextureName(CigaretteMod.MODID + ":cigarette_mediocre")
            .setCreativeTab(CreativeTabs.tabMisc);
        GameRegistry.registerItem(mediocreCigarette, "mediocreCigarette");

        finestCigarette.setUnlocalizedName("finestCigarette")
            .setTextureName(CigaretteMod.MODID + ":cigarette_finest")
            .setCreativeTab(CreativeTabs.tabMisc);
        GameRegistry.registerItem(finestCigarette, "finestCigarette");

        // cigarette butts
        crudeCigaretteButt.setUnlocalizedName("crudeCigaretteButt")
            .setTextureName(crudeCigaretteButt.GetTextureLocation())
            .setCreativeTab(CreativeTabs.tabMisc);
        GameRegistry.registerItem(crudeCigaretteButt, "crudeCigaretteButt");

        basicCigaretteButt.setUnlocalizedName("basicCigaretteButt")
            .setTextureName(basicCigaretteButt.GetTextureLocation())
            .setCreativeTab(CreativeTabs.tabMisc);
        GameRegistry.registerItem(basicCigaretteButt, "basicCigaretteButt");

        mediocreCigaretteButt.setUnlocalizedName("mediocreCigaretteButt")
            .setTextureName(mediocreCigaretteButt.GetTextureLocation())
            .setCreativeTab(CreativeTabs.tabMisc);
        GameRegistry.registerItem(mediocreCigaretteButt, "mediocreCigaretteButt");

        finestCigaretteButt.setUnlocalizedName("finestCigaretteButt")
            .setTextureName(finestCigaretteButt.GetTextureLocation())
            .setCreativeTab(CreativeTabs.tabMisc);
        GameRegistry.registerItem(finestCigaretteButt, "finestCigaretteButt");

    }

}
