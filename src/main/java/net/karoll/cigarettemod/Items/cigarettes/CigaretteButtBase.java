package net.karoll.cigarettemod.Items.cigarettes;

import java.util.HashMap;
import java.util.Map;

import net.karoll.cigarettemod.CigaretteMod;
import net.karoll.cigarettemod.Items.ItemTiers;
import net.minecraft.item.Item;

public class CigaretteButtBase extends Item {

    public ItemTiers.Tier Tier;
    private int _cigaretteType;

    public CigaretteButtBase(ItemTiers.Tier tier) {
        Tier = tier;
        Map<ItemTiers.Tier, Integer> _tierToCigaretteType = new HashMap<>();
        _tierToCigaretteType.put(ItemTiers.Tier.crude, 3);
        _tierToCigaretteType.put(ItemTiers.Tier.basic, 1);
        _tierToCigaretteType.put(ItemTiers.Tier.mediocre, 1);
        _tierToCigaretteType.put(ItemTiers.Tier.finest, 2);

        _cigaretteType = _tierToCigaretteType.get(Tier);
        this.setMaxStackSize(16);
    }

    public String GetTextureLocation() {
        return CigaretteMod.MODID + ":cigarette_used_" + _cigaretteType;
    }

}
