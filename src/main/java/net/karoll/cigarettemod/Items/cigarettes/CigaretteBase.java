package net.karoll.cigarettemod.Items.cigarettes;

import java.util.HashMap;
import java.util.Map;

import mods.railcraft.client.particles.EntitySteamFX;
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
        if (!worldIn.isRemote) {
            boolean hasLighterUsed = false;
            boolean hasLighterFull = false;
            boolean isPlatinum = false;
            int lighterIndex = -1;
            ItemStack[] playerInventory = player.inventory.mainInventory;

            // look for lighter and see if its already open
            for (int i = 0; i < playerInventory.length - 1; i++) {
                if (playerInventory[i] != null && playerInventory[i].getUnlocalizedName()
                    .contains("gt.metaitem.01")) {
                    String Name = playerInventory[i].getDisplayName();
                    if (Name.contains("Lighter (Full)")) {
                        hasLighterFull = true;
                        lighterIndex = i;
                        break;
                    } else if (Name.contains("Lighter") && !Name.contains("(Empty)")) {
                        hasLighterUsed = true;
                        lighterIndex = i;
                        break;
                    }
                }
            }
            // if no lighter or lighter is empty
            if (!hasLighterUsed && !hasLighterFull) {
                return super.onItemRightClick(itemStackIn, worldIn, player);
            }
            // see if item is platinum or not
            if (playerInventory[lighterIndex].getDisplayName()
                .contains("Platinum")) {
                isPlatinum = true;
            }
            // depending on plat or not, set lighterStates
            if (!isPlatinum) {
                lighterStates.put("empty", ItemList.Tool_Lighter_Invar_Empty.get(1L));
                lighterStates.put("used", ItemList.Tool_Lighter_Invar_Used.get(1L));
                lighterStates.put("full", ItemList.Tool_Lighter_Invar_Full.get(1L));
            } else {
                lighterStates.put("empty", ItemList.Tool_Lighter_Platinum_Empty.get(1L));
                lighterStates.put("used", ItemList.Tool_Lighter_Platinum_Used.get(1L));
                lighterStates.put("full", ItemList.Tool_Lighter_Platinum_Full.get(1L));
            }

            // grabs lighter item and important info
            long fuelAmount;
            long newFuelAmount;
            ItemStack lighterItemStack = playerInventory[lighterIndex];
            if (hasLighterFull) {
                if (isPlatinum) {
                    newFuelAmount = 999L;
                } else {
                    newFuelAmount = 99L;
                }
            } else {
                fuelAmount = GT_Utility.ItemNBT.getLighterFuel(lighterItemStack);
                newFuelAmount = fuelAmount - 1;
            }
            // plays fire ignite sound at player feet
            GT_Utility.sendSoundToPlayers(
                worldIn,
                SoundResource.FIRE_IGNITE,
                1.0F,
                1.0F,
                (int) player.posX,
                (int) player.posY,
                (int) player.posZ);
            // use fuel on lighter
            GT_Utility.ItemNBT.setLighterFuel(lighterItemStack, newFuelAmount);
            // see if the lighter needs a swap of texture
            if (newFuelAmount <= 0) {
                useUp(lighterItemStack);
            } else {
                prepare(lighterItemStack, newFuelAmount);
            }

            // actual cig stuff here
            // grab the NBT for the cig to make it lit
            NBTTagCompound tagCompound = itemStackIn.getTagCompound();
            if (tagCompound == null) {
                tagCompound = new NBTTagCompound();
            }
            if (!tagCompound.getBoolean("lit")) {
                tagCompound.setBoolean("lit", true);
                itemStackIn.setTagCompound(tagCompound);
            }
            // store when last it was lit (unlit after 10 seconds)
            tagCompound.setLong("lastUseTime", worldIn.getTotalWorldTime());
            itemStackIn.setTagCompound(tagCompound);

            // spawn smoke infront of player's face
            Map<String, Double> smokePos = getPositionInfrontofPlayerFace(player);
            Map<String, Double> smokeOffsets = getOffsetInfrontofPlayerFace(player);
            // Spawn smoke particles
            for (int i = 0; i < 5; i++) { // Number of smoke particles
                // worldIn.spawnParticle("smoke",
                // smokePos.get("x"),
                // smokePos.get("y"),
                // smokePos.get("z"),
                // 0.0D,
                // 0.0D,
                // 0.0D);
                Minecraft.getMinecraft().effectRenderer.addEffect(
                    new EntitySteamFX(worldIn, smokePos.get("x"), smokePos.get("y"), smokePos.get("z"), smokeOffsets.get("x") / 10, smokeOffsets.get("y") / 10, smokeOffsets.get("z") / 10));
            }

            // add cool status effect for
            player.removePotionEffect(ModPotions.PotionIDConverter.get("coolPotion"));
            player.addPotionEffect(
                new PotionEffect(ModPotions.PotionIDConverter.get("coolPotion"), _coolStatusEffectDuration, 0));
            itemStackIn.damageItem(1, player);
            if (itemStackIn.getItemDamage() == itemStackIn.getMaxDamage()) {

                ItemStack newStack = new ItemStack(_cigaretteButt, 1);
                player.inventory.setInventorySlotContents(player.inventory.currentItem, newStack);
                player.inventory.markDirty(); // marks inven for an update
                // player.setCurrentItemOrArmor(0, newStack); // Replace the current item with the updated one
                return newStack;
            }
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
