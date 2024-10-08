package net.karoll.cigarettemod.Items.CigaretteCase;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

public class CigaretteCaseGui extends GuiContainer {

    private static final ResourceLocation field_147017_u = new ResourceLocation(
        "textures/gui/container/generic_54.png");
    private IInventory PlayerInventory;
    private IInventory CigaretteCaseInventory;
    private int inventoryRows;

    public CigaretteCaseGui(IInventory playerInven, IInventory CigCase) {
        super(new ContainerChest(playerInven, CigCase));
        this.PlayerInventory = playerInven;
        this.CigaretteCaseInventory = CigCase;
        this.allowUserInput = false;
        short short1 = 222;
        int i = short1 - 108;
        this.inventoryRows = CigCase.getSizeInventory() / 9;
        this.ySize = i + this.inventoryRows * 18;
    }

    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        this.fontRendererObj.drawString("Cigarette Case", 8, 6, 4210752);
        this.fontRendererObj.drawString("Inventory", 8, this.ySize - 96 + 2, 4210752);
    }

    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager()
            .bindTexture(field_147017_u);
        int k = (this.width - this.xSize) / 2;
        int l = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.inventoryRows * 18 + 17);
        this.drawTexturedModalRect(k, l + this.inventoryRows * 18 + 17, 0, 126, this.xSize, 96);
    }
}
