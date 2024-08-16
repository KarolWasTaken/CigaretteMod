package net.karoll.cigarettemod.Items.cigarettes;

import java.util.HashMap;
import java.util.Map;

import net.karoll.cigarettemod.CigaretteMod;
import net.karoll.cigarettemod.ItemRegistration.ModItems;
import net.karoll.cigarettemod.ItemRegistration.ModPotions;
import net.karoll.cigarettemod.Items.ItemTiers;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import gregtech.api.enums.ItemList;
import gregtech.api.enums.SoundResource;
import gregtech.api.util.GT_Utility;
import mods.railcraft.client.particles.EntitySteamFX;

public class CigaretteBase extends Item {

    private static final int TICK = 20;
    public ItemTiers.Tier Tier;
    private int _coolStatusEffectDuration;
    private IIcon _iconInHand;
    private IIcon _iconInInventory;
    private IIcon _iconInHandNotLit;
    private IIcon _iconInInventoryNotLit;
    private CigaretteButtBase _cigaretteButt;

    private Map<String, ItemStack> lighterStates = new HashMap<>();

    public CigaretteBase(ItemTiers.Tier tier) {
        Tier = tier;
        // Cigarette NBT:
        // isLit
        // lastUseTime

        this._coolStatusEffectDuration = getCoolStatusEffectDuration(tier);
        this._cigaretteButt = getCigaretteButt(tier);
        setMaxDamage(tier);
        this.setMaxStackSize(1);
    }

    private void setMaxDamage(ItemTiers.Tier tier) {
        switch (tier) {
            case crude:
                this.setMaxDamage(1);
                break;
            case basic:
                this.setMaxDamage(3);
                break;
            case mediocre:
                this.setMaxDamage(5);
                break;
            case finest:
                this.setMaxDamage(7);
                break;
            default:
                this.setMaxDamage(0);
                break;
        }
    }

    private int getCoolStatusEffectDuration(ItemTiers.Tier tier) {
        switch (tier) {
            case crude:
                return 60 * TICK;
            case basic:
                return 180 * TICK;
            case mediocre:
                return 300 * TICK;
            case finest:
                return 420 * TICK;
            default:
                return 0;
        }
    }

    private CigaretteButtBase getCigaretteButt(ItemTiers.Tier tier) {
        switch (tier) {
            case crude:
                return ModItems.crudeCigaretteButt;
            case basic:
                return ModItems.basicCigaretteButt;
            case mediocre:
                return ModItems.mediocreCigaretteButt;
            case finest:
                return ModItems.finestCigaretteButt;
            default:
                return null;
        }
    }

    @Override
    public void registerIcons(IIconRegister register) {
        super.registerIcons(register);
        this._iconInInventory = register.registerIcon(CigaretteMod.MODID + ":cigarette_" + Tier.name());
        this._iconInHand = register.registerIcon(CigaretteMod.MODID + ":cigarette_" + Tier.name() + "_inhand");
        this._iconInInventoryNotLit = register
            .registerIcon(CigaretteMod.MODID + ":cigarette_" + Tier.name() + "_unlit");
        this._iconInHandNotLit = register
            .registerIcon(CigaretteMod.MODID + ":cigarette_" + Tier.name() + "_unlit_inhand");
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

        // see if cig is lit or not
        NBTTagCompound tagCompound = stack.getTagCompound();
        boolean isCigaretteLit = tagCompound != null && tagCompound.getBoolean("lit");

        if (isCigaretteLit) {
            long lastUseTime = tagCompound.getLong("lastUseTime");
            long currentTime = Minecraft.getMinecraft().theWorld.getTotalWorldTime();

            // Check if 10 seconds (200 ticks) have passed since last use
            if (currentTime - lastUseTime > 200) {
                isCigaretteLit = false;
                tagCompound.setBoolean("lit", false);
                stack.setTagCompound(tagCompound);
            }
        }

        // Return the appropriate icon based on whether it's in hand or in inventory
        if (isCigaretteLit) {
            return isInHand ? this._iconInHand : this._iconInInventory;
        } else {
            return isInHand ? this._iconInHandNotLit : this._iconInInventoryNotLit;
        }
    }

    @Override
    public boolean isDamageable() {
        return true; // This tells the game that the item can take damage
    }

    // please simplify this at some point man
    @Override
    public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer player) {
        if (worldIn.isRemote) return super.onItemRightClick(itemStackIn, worldIn, player);

        // find lighter
        int lighterIndex = findLighter(player);
        if (lighterIndex == -1) return super.onItemRightClick(itemStackIn, worldIn, player);

        // see if it's platinum
        boolean isPlatinum = isLighterPlatinum(player.inventory.mainInventory[lighterIndex]);
        updateLighterStates(isPlatinum);

        // decrease lighter fuel
        ItemStack lighterItemStack = player.inventory.mainInventory[lighterIndex];
        handleLighterFuel(lighterItemStack, isPlatinum);

        // set NBT as lit
        setCigaretteLit(itemStackIn, worldIn);
        spawnSmokeParticles(player, worldIn);
        applyCoolStatusEffect(player);

        return handleCigaretteDamage(itemStackIn, worldIn, player);
    }

    private int findLighter(EntityPlayer player) {
        ItemStack[] inventory = player.inventory.mainInventory;
        for (int i = 0; i < inventory.length - 1; i++) {
            if (inventory[i] != null && inventory[i].getUnlocalizedName()
                .contains("gt.metaitem.01")) {
                String name = inventory[i].getDisplayName();
                if (name.contains("Lighter")) {
                    return i;
                }
            }
        }
        return -1;
    }

    private boolean isLighterPlatinum(ItemStack lighter) {
        return lighter.getDisplayName()
            .contains("Platinum");
    }

    private void updateLighterStates(boolean isPlatinum) {
        lighterStates.clear();
        if (isPlatinum) {
            lighterStates.put("empty", ItemList.Tool_Lighter_Platinum_Empty.get(1L));
            lighterStates.put("used", ItemList.Tool_Lighter_Platinum_Used.get(1L));
            lighterStates.put("full", ItemList.Tool_Lighter_Platinum_Full.get(1L));
        } else {
            lighterStates.put("empty", ItemList.Tool_Lighter_Invar_Empty.get(1L));
            lighterStates.put("used", ItemList.Tool_Lighter_Invar_Used.get(1L));
            lighterStates.put("full", ItemList.Tool_Lighter_Invar_Full.get(1L));
        }
    }

    private void handleLighterFuel(ItemStack lighterItemStack, boolean isPlatinum) {
        long newFuelAmount = isFullLighter(lighterItemStack) ? (isPlatinum ? 999L : 99L)
            : getLighterFuel(lighterItemStack) - 1;
        GT_Utility.sendSoundToPlayers(
            Minecraft.getMinecraft().theWorld,
            SoundResource.FIRE_IGNITE,
            1.0F,
            1.0F,
            (int) Minecraft.getMinecraft().thePlayer.posX,
            (int) Minecraft.getMinecraft().thePlayer.posY,
            (int) Minecraft.getMinecraft().thePlayer.posZ);
        GT_Utility.ItemNBT.setLighterFuel(lighterItemStack, newFuelAmount);
        updateLighterTexture(lighterItemStack, newFuelAmount);
    }

    private boolean isFullLighter(ItemStack lighterItemStack) {
        return lighterItemStack.getDisplayName()
            .contains("Lighter (Full)");
    }

    private long getLighterFuel(ItemStack lighterItemStack) {
        return GT_Utility.ItemNBT.getLighterFuel(lighterItemStack);
    }

    private void updateLighterTexture(ItemStack lighterItemStack, long newFuelAmount) {
        if (newFuelAmount <= 0) {
            useUp(lighterItemStack);
        } else {
            prepare(lighterItemStack, newFuelAmount);
        }
    }

    private void setCigaretteLit(ItemStack itemStackIn, World worldIn) {
        NBTTagCompound tagCompound = itemStackIn.getTagCompound();
        if (tagCompound == null) tagCompound = new NBTTagCompound();
        if (!tagCompound.getBoolean("lit")) {
            tagCompound.setBoolean("lit", true);
            itemStackIn.setTagCompound(tagCompound);
        }
        tagCompound.setLong("lastUseTime", worldIn.getTotalWorldTime());
        itemStackIn.setTagCompound(tagCompound);
    }

    private void spawnSmokeParticles(EntityPlayer player, World worldIn) {
        Map<String, Double> smokePos = getPositionInfrontofPlayerFace(player);
        Map<String, Double> smokeOffsets = getOffsetInfrontofPlayerFace(player);
        for (int i = 0; i < 5; i++) {
            Minecraft.getMinecraft().effectRenderer.addEffect(
                new EntitySteamFX(
                    worldIn,
                    smokePos.get("x"),
                    smokePos.get("y"),
                    smokePos.get("z"),
                    smokeOffsets.get("x") / 10,
                    smokeOffsets.get("y") / 10,
                    smokeOffsets.get("z") / 10));
        }
    }

    private void applyCoolStatusEffect(EntityPlayer player) {
        player.removePotionEffect(ModPotions.PotionIDConverter.get("coolPotion"));
        player.addPotionEffect(
            new PotionEffect(ModPotions.PotionIDConverter.get("coolPotion"), _coolStatusEffectDuration, 0));
    }

    private ItemStack handleCigaretteDamage(ItemStack itemStackIn, World worldIn, EntityPlayer player) {
        itemStackIn.damageItem(1, player);
        if (itemStackIn.getItemDamage() == itemStackIn.getMaxDamage()) {
            ItemStack newStack = new ItemStack(_cigaretteButt, 1);
            player.inventory.setInventorySlotContents(player.inventory.currentItem, newStack);
            player.inventory.markDirty();
            return newStack;
        }
        return super.onItemRightClick(itemStackIn, worldIn, player);
    }

    private Map<String, Double> getOffsetInfrontofPlayerFace(EntityPlayer player) {
        // Calculate the position in front of the player's face
        double playerPosX = player.posX;
        double playerPosY = player.posY + player.getEyeHeight(); // Eye height to spawn near the face
        double playerPosZ = player.posZ;

        // find direction hes facing
        float pitch = player.rotationPitch;
        float yaw = player.rotationYaw;

        // using direction find offset for x, z, or y
        double offsetX = -Math.sin(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch));
        double offsetZ = Math.cos(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch));
        double offsetY = -Math.sin(Math.toRadians(pitch));

        Map<String, Double> data = new HashMap<>();
        data.put("x", offsetX);
        data.put("y", offsetY);
        data.put("z", offsetZ);
        return data;
    }

    private Map<String, Double> getPositionInfrontofPlayerFace(EntityPlayer player) {
        // Calculate the position in front of the player's face
        double playerPosX = player.posX;
        double playerPosY = player.posY + player.getEyeHeight(); // Eye height to spawn near the face
        double playerPosZ = player.posZ;

        Map<String, Double> offsets = getOffsetInfrontofPlayerFace(player);

        // using direction find offset for x, z, or y
        double offsetX = offsets.get("x");
        double offsetZ = offsets.get("z");
        double offsetY = offsets.get("y");

        double smokePosX = playerPosX + offsetX * 0.25; // 0.5 is the distance from the face
        double smokePosY = playerPosY + offsetY * 0.25;
        double smokePosZ = playerPosZ + offsetZ * 0.25;

        Map<String, Double> data = new HashMap<>();
        data.put("x", smokePosX);
        data.put("y", smokePosY);
        data.put("z", smokePosZ);
        return data;
    }

    /**
     * <h5>Turns a full lighter into a used one.</h5>
     * Modified method from gregtech.common.items.behaviors.Behaviour_Lighter
     *
     * @param aStack
     * @param fuel
     */
    private void prepare(ItemStack aStack, long fuel) {
        if (GT_Utility.areStacksEqual(aStack, lighterStates.get("full"), true)) {
            aStack.func_150996_a(
                lighterStates.get("used")
                    .getItem());
            Items.feather.setDamage(aStack, Items.feather.getDamage(lighterStates.get("used")));
            GT_Utility.ItemNBT.setLighterFuel(aStack, fuel);
        }
    }

    /**
     * <h5>Turns a used lighter into an empty one.</h5>
     * Modified method from gregtech.common.items.behaviors.Behaviour_Lighter
     *
     * @param aStack
     */
    private void useUp(ItemStack aStack) {
        if (lighterStates.get(0) == null) {
            aStack.stackSize -= 1;
        } else {
            aStack.func_150996_a(
                lighterStates.get(0)
                    .getItem());
            Items.feather.setDamage(aStack, Items.feather.getDamage(lighterStates.get(0)));
        }
    }
}
