package net.karoll.cigarettemod.save;

import net.minecraft.nbt.NBTTagCompound;

// code taken from https://github.com/GTNewHorizons/Minecraft-Backpack-Mod because they did it better than me

public abstract class AbstractSave {

    protected String UID;
    protected NBTTagCompound nbtTagCompound;
    protected boolean manualSaving = false;

    public AbstractSave(NBTTagCompound data) {
        nbtTagCompound = data;

        if (nbtTagCompound == null) {
            nbtTagCompound = new NBTTagCompound();
        }
    }

    public AbstractSave(String UUID) {
        load(UUID);
    }

    public void setManualSaving() {
        manualSaving = true;
    }

    public abstract void save();

    protected abstract void load(String UUID);
}
