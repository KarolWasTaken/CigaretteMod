package net.karoll.cigarettemod.ItemRegistration;

import java.util.HashMap;

import net.karoll.cigarettemod.StatusEffects.PotionCool;
import net.minecraft.potion.Potion;

public class ModPotions {

    public static HashMap<String, Integer> PotionIDConverter = new HashMap<>();
    public static Potion coolPotion = new PotionCool(30, false, 0x08ff00);

    public static void init() {
        PotionIDConverter.put("coolPotion", 30);
        Potion.potionTypes[PotionIDConverter.get("coolPotion")] = coolPotion;
    }
}
