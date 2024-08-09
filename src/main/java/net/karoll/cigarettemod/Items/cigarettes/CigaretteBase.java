package net.karoll.cigarettemod.Items.cigarettes;

import net.karoll.cigarettemod.CigaretteMod;
import net.karoll.cigarettemod.ItemRegistration.ModItems;
import net.karoll.cigarettemod.ItemRegistration.ModPotions;
import net.karoll.cigarettemod.Items.ItemTiers;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class CigaretteBase extends Item {

    private static final int TICK = 20;
    public ItemTiers.Tier Tier;
    private int _coolStatusEffectDuration;
    private IIcon _iconInHand;
    private IIcon _iconInInventory;
    private Boolean _isLowDurability = false;
    private CigaretteButtBase _cigaretteButt;

    public CigaretteBase(ItemTiers.Tier tier) {
        Tier = tier;

        SetUpCigarette(tier);
        this.setMaxStackSize(1);
    }

    private void SetUpCigarette(ItemTiers.Tier tier) {
        switch (tier) {
            case crude:
                this.setMaxDamage(1);
                _coolStatusEffectDuration = 60 * TICK;
                _cigaretteButt = ModItems.crudeCigaretteButt;
                break;
            case basic:
                this.setMaxDamage(3);
                _coolStatusEffectDuration = 180 * TICK;
                _cigaretteButt = ModItems.basicCigaretteButt;
                break;
            case mediocre:
                this.setMaxDamage(5);
                _coolStatusEffectDuration = 300 * TICK;
                _cigaretteButt = ModItems.mediocreCigaretteButt;
                break;
            case finest:
                this.setMaxDamage(7);
                _coolStatusEffectDuration = 420 * TICK;
                _cigaretteButt = ModItems.finestCigaretteButt;
                break;
        }
    }

    @Override
    public void registerIcons(IIconRegister register) {
        super.registerIcons(register);
        this._iconInInventory = register.registerIcon(CigaretteMod.MODID + ":cigarette_" + Tier.name());
        this._iconInHand = register.registerIcon(CigaretteMod.MODID + ":cigarette_" + Tier.name() + "_inhand");
    }

    // code below allows the texture to change when cig in hand.
    @Override
    public boolean requiresMultipleRenderPasses() {
        return true;
    }

    @Override
    public int getRenderPasses(int metadata) {
        return 2; // Number of passes needed; typically 1 if you're not actually using multiple passes.
    }

    @Override
    public IIcon getIcon(ItemStack stack, int pass) {
        EntityPlayer player = Minecraft.getMinecraft().thePlayer;
        boolean isInHand = player.getCurrentEquippedItem() != null && player.getCurrentEquippedItem()
            .equals(stack);

        // Return the appropriate icon based on whether it's in hand or in inventory
        return isInHand ? this._iconInHand : this._iconInInventory;
    }

    @Override
    public boolean isDamageable() {
        return true; // This tells the game that the item can take damage
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer player) {
        if (!worldIn.isRemote && !_isLowDurability) {
            // add cool status effect for
            player.removePotionEffect(ModPotions.PotionIDConverter.get("coolPotion"));
            player.addPotionEffect(
                new PotionEffect(ModPotions.PotionIDConverter.get("coolPotion"), _coolStatusEffectDuration, 0));
            itemStackIn.damageItem(1, player);
            if (itemStackIn.getItemDamage() == itemStackIn.getMaxDamage()) {
                _isLowDurability = true;

                ItemStack newStack = new ItemStack(_cigaretteButt, 1);
                player.inventory.setInventorySlotContents(player.inventory.currentItem, newStack);
                player.inventory.markDirty(); // marks inven for an update
                // player.setCurrentItemOrArmor(0, newStack); // Replace the current item with the updated one
                return newStack;
            }
        }
        return super.onItemRightClick(itemStackIn, worldIn, player);
    }
}
