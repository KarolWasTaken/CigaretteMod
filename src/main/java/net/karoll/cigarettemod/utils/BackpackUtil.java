package net.karoll.cigarettemod.utils;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import net.karoll.cigarettemod.Constants;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import java.util.UUID;

// code taken from https://github.com/GTNewHorizons/Minecraft-Backpack-Mod because they did it better than me

public class BackpackUtil {

    public static void playOpenSound(Entity ent) {

        /*if (ConfigurationBackpack.PLAY_OPEN_SOUND) {
            String soundName = Constants.MOD_ID + ":OpenBackpack";
            float volume = 0.7f;
            float maxSpeed = 0.2f;
            float minSpeed = 0.9f;
            ent.worldObj.playSoundAtEntity(ent, soundName, volume, (float) ((Math.random() * maxSpeed) + minSpeed));
        }*/
        //PLAY sounf when cig case opens
    }


    public static boolean isServerSide() {
        return FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER;
    }

    public static boolean isServerSide(World world) {
        return !world.isRemote;
    }

    public static String getUUID(NBTTagCompound nbtTagCompound) {
        if (NBTUtil.hasTag(nbtTagCompound, Constants.NBT.UID)) {
            return NBTUtil.getString(nbtTagCompound, Constants.NBT.UID);
        }
        return null;
    }

    /**
     * Compares the UUID's of two ItemStacks.
     *
     * @param suspicious The ItemStack to check.
     * @param original   The original ItemStack to compare to
     * @return Returns true if both have the same UUID, false if one or both ItemStacks are null, one or both ItemStacks
     *         doesn't have the tag "backpack-UID" or if the UUID's are not equal.
     */
    public static boolean UUIDEquals(ItemStack suspicious, ItemStack original) {
        if (suspicious != null && original != null) {
            if (NBTItemStackUtil.hasTag(suspicious, Constants.NBT.UID)
                    && NBTItemStackUtil.hasTag(original, Constants.NBT.UID)) {
                String UIDsuspicious = NBTItemStackUtil.getString(suspicious,Constants.NBT.UID);
                String UIDoriginal = NBTItemStackUtil.getString(original, Constants.NBT.UID);
                return UUIDEquals(UIDsuspicious, UIDoriginal);
            }
        }
        return false;
    }

    /**
     * Compares the UUID's of two ItemStacks.
     *
     * @param suspicious The ItemStack to check.
     * @param original   The original UUID to compare to
     * @return Returns true if both have the same UUID, false if one or both ItemStacks are null, one or both ItemStacks
     *         doesn't have the tag "backpack-UID" or if the UUID's are not equal.
     */
    public static boolean UUIDEquals(ItemStack suspicious, String original) {
        if (suspicious != null && original != null) {
            if (NBTItemStackUtil.hasTag(suspicious, Constants.NBT.UID)) {
                String UIDsuspicious = NBTItemStackUtil.getString(suspicious, Constants.NBT.UID);
                return UUIDEquals(UIDsuspicious, original);
            }
        }
        return false;
    }

    /**
     * Compares the UUID's of two ItemStacks.
     *
     * @param suspicious The UUID to check.
     * @param original   The original UUID to compare to
     * @return Returns true if both have the same UUID, false if one or both ItemStacks are null, one or both ItemStacks
     *         doesn't have the tag "backpack-UID" or if the UUID's are not equal.
     */
    public static boolean UUIDEquals(String suspicious, String original) {
        if (suspicious != null && original != null) {
            try {
                UUID UIDsuspicious = UUID.fromString(suspicious);
                UUID UIDoriginal = UUID.fromString(original);
                return UIDsuspicious.equals(UIDoriginal);
            } catch (IllegalArgumentException e) {
                return false;
            }
        }
        return false;
    }
}
