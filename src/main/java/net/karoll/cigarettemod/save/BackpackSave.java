package net.karoll.cigarettemod.save;

import net.karoll.cigarettemod.CigaretteMod;
import net.karoll.cigarettemod.Constants;
import net.karoll.cigarettemod.Items.CigaretteCase.CigaretteCase;
import net.karoll.cigarettemod.utils.BackpackUtil;
import net.karoll.cigarettemod.utils.NBTItemStackUtil;
import net.karoll.cigarettemod.utils.NBTUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants.NBT;

import java.util.UUID;

// code taken from https://github.com/GTNewHorizons/Minecraft-Backpack-Mod and edited because they did it better than me

public class BackpackSave extends AbstractSave {

    // loads backpack from UUID
    public BackpackSave(String uuid) {
        super(uuid);
    }

    public BackpackSave(NBTTagCompound data) {
        // loads backpack from UUID
        super(data);

        // checks (on backpack open) if backpack has a "backpack-UID" nbt tag with a UID
        if (NBTUtil.hasTag(nbtTagCompound, Constants.NBT.UID)) {
            UID = NBTUtil.getString(nbtTagCompound, Constants.NBT.UID);
        }
    }

    public BackpackSave(ItemStack backpack) {
        // Loads backpack from itemstack (eg looks for UUID)
        this(backpack, false);
    }

    // for generating UID outside of BACKPACK save class
    public BackpackSave(ItemStack backpack, String UID) {
        super(new NBTTagCompound());
        // Loads backpack from itemstack (eg looks for UUID)
        //this(backpack, false);
        initialize(backpack, UID);
    }


    public BackpackSave(ItemStack backpack, boolean force) {
        // line 24
        super(new NBTTagCompound());
        // checks if the item stack has a "backpack-UID" nbt tag with a UID
        if (!NBTItemStackUtil.hasTag(backpack, Constants.NBT.UID)) {
            // if not init backpack
            initialize(backpack);
        } else {
            if (backpack.getItem() instanceof CigaretteCase) {
                // item is backpack and has UID, retrieve UID and load backpack
                load(NBTItemStackUtil.getString(backpack, Constants.NBT.UID));
                // force backpack to init even tho data loaded (perhaps to wipe it?)
                if (force) {
                    initialize(backpack);
                }
            }
        }
    }

    public boolean isUninitialized() {
        return nbtTagCompound.hasNoTags();
    }

    public void initialize(ItemStack backpack, String UID) {
        // check that incomming itemStack is of class type backpack and this code is ran server side only
        if (backpack.getItem() instanceof CigaretteCase && BackpackUtil.isServerSide()) {


            // initialise variables and data for backpack
            int size = 18; // cigarette case standard size

            // set's flag in parent called "bool manualSaving" dunno why prolly for efficiency
            setManualSaving();
            // set's itemstack's nbt tag to have key:("size") value:(<size>)
            setSize(size);

            setOpenState(false);
            // set's itemstack's nbt tag to have key:("backpackInventories") value:(new NBTTagCompound())
            // if they dont have one
            if (!NBTUtil.hasTag(nbtTagCompound, Constants.NBT.INVENTORY_CIGARETTECASE)) {
                //NBTUtil.setCompoundTag(nbtTagCompound, "asda", new NBTTagCompound());
                //NBTUtil.setTagList(Constants.NBT.INVENTORY_CIGARETTECASE, new NBTTagCompound());

                //NBTTagCompound inventories = NBTUtil.getCompoundTag(nbtTagCompound, Constants.NBT.INVENTORIES);
                NBTUtil.setTagList(nbtTagCompound, Constants.NBT.INVENTORY_CIGARETTECASE, new NBTTagList());
            }

            save();
        }
    }
    public void initialize(ItemStack backpack) {
        // check that incomming itemStack is of class type backpack and this code is ran server side only
        if (backpack.getItem() instanceof CigaretteCase && BackpackUtil.isServerSide()) {

            // set's itemstack's nbt tag to have key:("name") value:("item.backpack.name")
            // unlocalised backpack name may change due to like colour and size change idk
            //NBTItemStackUtil
            //       .setString(backpack, Constants.NBT.NAME, backpack.getItem().getUnlocalizedName(backpack) + ".name");
            // if backpack doesnt have a UID nbt tag
            if (!NBTItemStackUtil.hasTag(backpack, Constants.NBT.UID)) {
                // give it one :)
                UID = UUID.randomUUID().toString();
                NBTItemStackUtil.setString(backpack, Constants.NBT.UID, UID);
            }

            // initialise variables and data for backpack
            // i think meta is like what colour and size backpack is
            int size = 18; // cigarette case standard size

            // set's flag in parent called "bool manualSaving" dunno why prolly for efficiency
            setManualSaving();

            // set's itemstack's nbt tag to have key:("size") value:(<size>)
            setSize(size);

            setOpenState(false);

            // set's itemstack's nbt tag to have key:("backpackInventories") value:(new NBTTagCompound())
            // if they dont have one
            if (!NBTUtil.hasTag(nbtTagCompound, Constants.NBT.INVENTORY_CIGARETTECASE)) {
                NBTUtil.setTagList(nbtTagCompound, Constants.NBT.INVENTORY_CIGARETTECASE, new NBTTagList());
            }

            save();
        }
    }

    public String getUUID() {
        return UID;
    }
    public static String getUUID(ItemStack backpack) {
        // lots for UID nbt tag
        if (NBTItemStackUtil.hasTag(backpack, Constants.NBT.UID)) {
            // returns value, if found, to caller
            return NBTItemStackUtil.getString(backpack, Constants.NBT.UID);
        } else {
            // bro what in the recursion
            // if cannot get UID makes a new backpack save file for that backpack with this class' UID and sends it to caller
            return new BackpackSave(backpack).getUUID();
        }
    }
    public int getSize() {
        return NBTUtil.getInteger(nbtTagCompound, Constants.NBT.SIZE);
    }
    public boolean getOpenState() {
        return NBTUtil.getBoolean(nbtTagCompound, Constants.NBT.IS_OPEN);
    }
    public void setSize(int size) {
        NBTUtil.setInteger(nbtTagCompound, Constants.NBT.SIZE, size);

        if (!manualSaving) {
            save();
        }
    }
    public void setOpenState(boolean isOpen) {
        NBTUtil.setBoolean(nbtTagCompound, Constants.NBT.IS_OPEN, isOpen);

        if (!manualSaving) {
            save();
        }
    }

    public NBTTagList getInventory(String inventory) {
        return NBTUtil.getTagList(nbtTagCompound, inventory, NBT.TAG_COMPOUND);
    }

    public void setInventory(String inventoryName, NBTTagList inventory) {
        // finds the internal inventory for backpack (using string) and set's it's nbt data tag list (list of tags used for storing item data)
        NBTUtil.setTagList(nbtTagCompound, inventoryName, inventory);

        // and saves the changes into the save file
        if (!manualSaving) {
            save();
        }
    }

    @Override
    public void save() {
        // check UID is legit and we are running server side
        if (UID != null && BackpackUtil.isServerSide()) {
            // save backpack via its UID (filename)
            CigaretteMod.saveFileHandler.saveBackpack(nbtTagCompound, UID);
        }

        // sets manual saving to false so that backpack can autosave
        manualSaving = false;
    }

    @Override
    protected void load(String UUID) {
        // if uid is not empty and this is running on server side
        if (UUID != null && BackpackUtil.isServerSide()) {
            // this class' UID is equal to the one sent
            UID = UUID;
            // and we load nbt data from save file handler using UID
            nbtTagCompound = CigaretteMod.saveFileHandler.loadBackpack(UID);
        }
    }
}
