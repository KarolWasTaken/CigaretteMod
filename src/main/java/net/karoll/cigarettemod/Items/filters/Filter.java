package net.karoll.cigarettemod.Items.filters;

import net.karoll.cigarettemod.CigaretteMod;
import net.karoll.cigarettemod.Items.ItemTiers;
import net.minecraft.item.Item;

public class Filter extends Item {

    public static ItemTiers.Tier Tier;

    public Filter(ItemTiers.Tier tier) {
        Tier = tier;
    }

    public String GetTextureName() {
        if (Tier == ItemTiers.Tier.crude) {
            return CigaretteMod.MODID + ":filter_crude";
        } else {
            return CigaretteMod.MODID + ":filter_basic";
        }
    }

}
