package santa.lore.block;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import santa.lore.CustomizedLore;
import santa.lore.network.LoreChangePacket;

import java.util.List;

public class GuiLoreCustomizer extends GuiContainer implements ICrafting {
    private GuiTextField textField;
    private ContainerLoreCustomizer container;
    private static final ResourceLocation texture = new ResourceLocation(CustomizedLore.MOD_ID + ":textures/gui/customizer.png");

    public GuiLoreCustomizer(InventoryPlayer invPlayer, World world, int x, int y, int z) {
        super(new ContainerLoreCustomizer(invPlayer, world, x, y, z, Minecraft.getMinecraft().thePlayer));
        this.container = (ContainerLoreCustomizer) this.inventorySlots;
    }

    @Override
    public void initGui() {
        super.initGui();
        Keyboard.enableRepeatEvents(true);
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;
        this.textField = new GuiTextField(this.fontRendererObj, i + 62, j + 24, 103, 12);
        this.textField.setTextColor(-1);
        this.textField.setDisabledTextColour(-1);
        this.textField.setEnableBackgroundDrawing(false);
        this.textField.setMaxStringLength(300);
        this.inventorySlots.removeCraftingFromCrafters(this);
        this.inventorySlots.addCraftingToCrafters(this);
    }

    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
        Keyboard.enableRepeatEvents(false);
        this.inventorySlots.removeCraftingFromCrafters(this);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(texture);
        int k = (this.width - this.xSize) / 2;
        int l = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);
        this.drawTexturedModalRect(k + 59, l + 20, 0, this.ySize + (this.container.getSlot(0).getHasStack() ? 0 : 16), 110, 16);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2) {}

    private void updateText() {
        String s = this.textField.getText();
        if (s.equals(this.container.itemLore)) {
            return;
        }
        LoreChangePacket packet = new LoreChangePacket(s);
        CustomizedLore.channel.sendToServer(packet);
    }

    @Override
    protected void keyTyped(char charTyped, int par2) {
        if (this.textField.textboxKeyTyped(charTyped, par2)) {
            this.updateText();
        } else {
            super.keyTyped(charTyped, par2);
        }
    }

    @Override
    protected void mouseClicked(int x, int y, int z) {
        super.mouseClicked(x, y, z);
        this.textField.mouseClicked(x, y, z);
    }

    @Override
    public void sendContainerAndContentsToPlayer(Container container, List contents) {
        this.sendSlotContents(container, 0, container.getSlot(0).getStack());
    }

    @Override
    public void sendSlotContents(Container container, int slot, ItemStack itemstack) {
        if (slot == 0) {
            boolean hasLore = itemstack != null && itemstack.hasTagCompound() &&
              itemstack.getTagCompound().hasKey("CustomLore");
            String s = hasLore ? itemstack.getTagCompound().getString("CustomLore") : "";
            this.textField.setText(s);
            this.textField.setEnabled(itemstack != null);

            if (itemstack != null) {
                this.updateText();
            }
        }
    }

    @Override
    public void sendProgressBarUpdate(Container container, int p_71112_2_, int p_71112_3_) {}

    @Override
    public void drawScreen(int par1, int par2, float par3) {
        super.drawScreen(par1, par2, par3);
        this.textField.drawTextBox();
    }
}
