package net.karoll.cigarettemod.Items.CigaretteCase;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ibxm.Player;
import net.karoll.cigarettemod.CigaretteMod;
import net.karoll.cigarettemod.Constants;
import net.karoll.cigarettemod.handlers.ClientDataHandler;
import net.karoll.cigarettemod.handlers.GuiHandler;
import net.karoll.cigarettemod.misc.Localisations;
import net.karoll.cigarettemod.networking.messages.OpenCigaretteCaseGui;
import net.karoll.cigarettemod.networking.messages.PacketRequestBackpackData;
import net.karoll.cigarettemod.networking.messages.PacketSendInventory;
import net.karoll.cigarettemod.save.BackpackSave;
import net.karoll.cigarettemod.utils.NBTItemStackUtil;
import net.karoll.cigarettemod.utils.NBTUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import org.lwjgl.input.Keyboard;

import java.util.*;

public class CigaretteCase extends Item {

    private IIcon _iconCigaretteCaseClosed;
    private IIcon _iconCigaretteCaseOpenEmpty;
    private IIcon _iconCigaretteCaseOpenFull;
    private BackpackSave backpackSave;
    public CigaretteCase() {
        this.setMaxStackSize(1);
    }

    private BackpackSave getBackPackSave(ItemStack itemStack){
        if (!NBTItemStackUtil.hasTag(itemStack, Constants.NBT.UID)) {
            // give it one :)
            String UID = UUID.randomUUID().toString();
            NBTItemStackUtil.setString(itemStack, Constants.NBT.UID, UID);
            if(NBTItemStackUtil.hasTag(itemStack, Constants.NBT.IS_OPEN)) {
                NBTItemStackUtil.setBoolean(itemStack, Constants.NBT.IS_OPEN, NBTItemStackUtil.getBoolean(itemStack, Constants.NBT.IS_OPEN));
            }
            else {
                NBTItemStackUtil.setBoolean(itemStack, Constants.NBT.IS_OPEN, false);
            }
            backpackSave = new BackpackSave(itemStack, UID);
        }
        else {
            backpackSave = new BackpackSave(itemStack);
        }
        return backpackSave;
    }
    private boolean doesBackPackHaveUUID(ItemStack itemStack)
    {
        if (!NBTItemStackUtil.hasTag(itemStack, Constants.NBT.UID)) {
            return false;
        }
        else {
            return true;
        }
    }

    @Override
    public void registerIcons(IIconRegister register) {
        super.registerIcons(register);
        this._iconCigaretteCaseClosed = register.registerIcon(CigaretteMod.MODID + ":case_closed");
        this._iconCigaretteCaseOpenEmpty = register.registerIcon(CigaretteMod.MODID + ":case_open_empty");
        this._iconCigaretteCaseOpenFull = register.registerIcon(CigaretteMod.MODID + ":case_open_full");
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
    public IIcon getIcon(ItemStack itemStack, int pass) {
        boolean isCaseOpen = NBTItemStackUtil.getBoolean(itemStack, Constants.NBT.IS_OPEN);
        if(isCaseOpen) {
            int used = 0;
            String UID = NBTItemStackUtil.getString(itemStack, Constants.NBT.UID);
            if(UID == "") {
                return this._iconCigaretteCaseOpenEmpty;
            }
            NBTTagCompound InventoryNBT = CigaretteMod.saveFileHandler.loadBackpack(UID);
            NBTTagList InventoryTagList = NBTUtil.getTagList(InventoryNBT, Constants.NBT.INVENTORY_CIGARETTECASE, net.minecraftforge.common.util.Constants.NBT.TAG_COMPOUND);
            ItemStack[] Inventory = LoadInventoryFromNBT(InventoryTagList);
            // Count non-null elements
            for (ItemStack element : Inventory) {
                if (element != null) {
                    used++;
                }
            }
            if(used >= 1) {
                return this._iconCigaretteCaseOpenFull;
            }
            else {
                return this._iconCigaretteCaseOpenEmpty;
            }
        }
        return this._iconCigaretteCaseClosed;
    }

    public ItemStack[] LoadInventoryFromNBT(NBTTagList InventoryTag) {
        int i;
        int size = 18;
        ItemStack[] Inventory = new ItemStack[size];

        for (i = 0; i < size; ++i) {
            Inventory[i] = (ItemStack) null;
        }

        for (i = 0; i < InventoryTag.tagCount(); ++i) {
            NBTTagCompound nbttagcompound = InventoryTag.getCompoundTagAt(i);
            int j = nbttagcompound.getByte("Slot") & 255;
            if (j >= 0 && j < size) {
                Inventory[j] = ItemStack.loadItemStackFromNBT(nbttagcompound);
                // DEBUG HERE
                //ItemStack item = ItemStack.loadItemStackFromNBT(nbttagcompound);
            }
        }
        return Inventory;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
        try {
            Boolean isOpen = NBTItemStackUtil.getBoolean(itemStack, Constants.NBT.IS_OPEN);
            if (!world.isRemote) {
                // give it a UUID on rightclick
                BackpackSave cigCaseSave = getBackPackSave(itemStack);
                GuiHandler.heldItem = itemStack;
                EntityPlayerMP playerMP = (EntityPlayerMP) player;

                if(playerMP.isSneaking()) {
                    // toggle is open state
                    isOpen = !isOpen;
                    NBTItemStackUtil.setBoolean(itemStack, Constants.NBT.IS_OPEN, isOpen);
                }
                else {
                    if(isOpen)
                    {
                        // prepare player for opening the gui for cig case
                        if (playerMP.openContainer != playerMP.inventoryContainer) {
                            playerMP.closeScreen();
                        }
                        playerMP.getNextWindowId();

                        //open GUI
                        OpenCigaretteCaseGui message = new OpenCigaretteCaseGui(playerMP.currentWindowId);
                        CigaretteMod.packetHandler.INSTANCE.sendTo(message, playerMP);

                        playerMP.openContainer = new CigaretteCaseContainer(player.inventory, new CigaretteCaseInven(GuiHandler.heldItem));
                        playerMP.openContainer.windowId = playerMP.currentWindowId;
                        playerMP.openContainer.addCraftingToCrafters(playerMP);
                    }
                    else {
                        String UID = NBTItemStackUtil.getString(itemStack, Constants.NBT.UID);
                        if(UID == "") {
                            return itemStack;
                        }
                        // grab inventory data
                        NBTTagCompound InventoryNBT = CigaretteMod.saveFileHandler.loadBackpack(UID);
                        NBTTagList InventoryTagList = NBTUtil.getTagList(InventoryNBT, Constants.NBT.INVENTORY_CIGARETTECASE, net.minecraftforge.common.util.Constants.NBT.TAG_COMPOUND);
                        ItemStack[] Inventory = LoadInventoryFromNBT(InventoryTagList);
                        // get all non-null elements
                        Map<Integer, ItemStack> NonNullInventory = new Hashtable<>();
                        for (int i = 0; i < Inventory.length - 1; i++) {
                            if(Inventory[i] != null)
                            {
                                NonNullInventory.put(i, Inventory[i]);
                            }
                        }
                        // grab latest cig
                        Integer maxKey = NonNullInventory.keySet().stream().max(Integer::compareTo).orElse(null);
                        if(maxKey == null){
                            return itemStack;
                        }
                        ItemStack maxValue = NonNullInventory.get(maxKey);

                        // adds cig to player inven
                        boolean success = player.inventory.addItemStackToInventory(maxValue);
                        player.inventoryContainer.detectAndSendChanges();

                        // removes cig from inven and saves
                        Inventory[maxKey] = null;
                        NBTTagList InventoryNBTTagList = SaveInventoryToNBT(Inventory);
                        CigaretteMod.packetHandler.INSTANCE.sendToServer(new PacketSendInventory(InventoryNBTTagList, UID));
                    }
                }
            }
            /*else{
                Minecraft.getMinecraft().displayGuiScreen(new CigaretteCaseGui(player.inventory, new CigaretteCaseInven(itemStack)));
            }*/
            //player.openGui(CigaretteMod.instance, 420, world, (int) player.posX, (int) player.posY, (int) player.posZ);
        }
        catch (ConcurrentModificationException e)
        {
            Minecraft.getMinecraft().thePlayer.sendChatMessage(e.getMessage());
        }
        return itemStack;
    }

    public NBTTagList SaveInventoryToNBT(ItemStack[] inven) {
        //Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("§9========== saveInventoryFromNBT() =========="));
        //Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("§9saving inventory into NBT"));
        NBTTagList nbttaglist = new NBTTagList();

        for (int i = 0; i < 18; ++i) {
            ItemStack itemstack = inven[i];

            if (itemstack != null) {
                NBTTagCompound nbttagcompound = new NBTTagCompound();
                nbttagcompound.setByte("Slot", (byte) i);
                itemstack.writeToNBT(nbttagcompound);
                nbttaglist.appendTag(nbttagcompound);
            }
        }
        return nbttaglist;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack itemStack, EntityPlayer player, List<String> information, boolean advancedTooltip) {
        // mouse over description
        // if we have L or R shift pressed
        if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
            // and backpack is not ender backpack

            //getBackPackSave(itemStack);
            // call server to populate variable "Backpack backpackSave" and return it here
            /*NBTTagList itemList = backpackSave.getInventory(Constants.NBT.INVENTORY_CIGARETTECASE);
            int used = itemList.tagCount();
            int size = 18;
            information.add(used + "/" + size + ' ' + StatCollector.translateToLocal(Localisations.SLOTS_USED));*/
            if (!NBTItemStackUtil.hasTag(itemStack, Constants.NBT.UID)) {
                information.add("0" + "/" + "18" + ' ' + StatCollector.translateToLocal(Localisations.SLOTS_USED));
                return;
            }

            if (ClientDataHandler.getSlotsUsed() == -1 || !Objects.equals(ClientDataHandler.getUIDProcessing(), NBTItemStackUtil.getString(itemStack, Constants.NBT.UID))) {
                // Request server data if not already received
                CigaretteMod.packetHandler.INSTANCE.sendToServer(new PacketRequestBackpackData(itemStack));
                information.add(StatCollector.translateToLocal("Fetching data..."));
            } else {
                // Use the cached data
                int used = ClientDataHandler.getSlotsUsed();
                int size = 18;
                information.add("§e" + used + "/" + size + ' ' + StatCollector.translateToLocal(Localisations.SLOTS_USED));
            }
        }
    }
}
