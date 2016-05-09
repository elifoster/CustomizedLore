package santa.lore.block;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import santa.lore.CustomizedLore;
import santa.lore.network.LoreChangePacket;

import java.io.IOException;
import java.util.List;

public class GuiLoreCustomizer extends GuiContainer implements ICrafting {
    private GuiTextField textField;
    private ContainerLoreCustomizer container;
    private static final ResourceLocation texture = new ResourceLocation(CustomizedLore.MOD_ID + ":textures/gui/customizer.png");

    public GuiLoreCustomizer(InventoryPlayer invPlayer, World world, BlockPos pos) {
        super(new ContainerLoreCustomizer(invPlayer, world, pos, Minecraft.getMinecraft().thePlayer));
        this.container = (ContainerLoreCustomizer) this.inventorySlots;
    }

    @Override
    public void initGui() {
        super.initGui();
        Keyboard.enableRepeatEvents(true);
        int i = (width - xSize) / 2;
        int j = (height - ySize) / 2;
        textField = new GuiTextField(0, fontRendererObj, i + 62, j + 24, 103, 12);
        textField.setTextColor(-1);
        textField.setDisabledTextColour(-1);
        textField.setEnableBackgroundDrawing(false);
        textField.setMaxStringLength(300);
        inventorySlots.removeCraftingFromCrafters(this);
        inventorySlots.onCraftGuiOpened(this);
    }

    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
        Keyboard.enableRepeatEvents(false);
        inventorySlots.removeCraftingFromCrafters(this);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.getTextureManager().bindTexture(texture);
        int k = (width - xSize) / 2;
        int l = (height - ySize) / 2;
        drawTexturedModalRect(k, l, 0, 0, xSize, ySize);
        drawTexturedModalRect(k + 59, l + 20, 0, ySize + (container.getSlot(0).getHasStack() ? 0 : 16), 110, 16);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {}

    private void updateText() {
        String s = textField.getText();
        if (s.equals(container.itemLore)) {
            return;
        }
        LoreChangePacket packet = new LoreChangePacket(s);
        CustomizedLore.channel.sendToServer(packet);
    }

    @Override
    protected void keyTyped(char charTyped, int keyCode) throws IOException {
        if (textField.textboxKeyTyped(charTyped, keyCode)) {
            updateText();
        } else {
            super.keyTyped(charTyped, keyCode);
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        textField.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public void updateCraftingInventory(Container container, List<ItemStack> contents) {
        sendSlotContents(container, 0, container.getSlot(0).getStack());
    }

    @Override
    public void sendSlotContents(Container container, int slot, ItemStack itemstack) {
        if (slot == 0) {
            boolean hasLore = itemstack != null && itemstack.hasTagCompound() &&
              itemstack.getTagCompound().hasKey("CustomLore");
            String s = hasLore ? itemstack.getTagCompound().getString("CustomLore") : "";
            textField.setText(s);
            textField.setEnabled(itemstack != null);

            if (itemstack != null) {
                updateText();
            }
        }
    }

    @Override
    public void sendProgressBarUpdate(Container container, int varToUpdate, int newValue) {}

    @Override
    public void sendAllWindowProperties(Container p_175173_1_, IInventory p_175173_2_) {}

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        textField.drawTextBox();
    }
}
