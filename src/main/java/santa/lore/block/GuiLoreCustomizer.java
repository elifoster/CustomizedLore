package santa.lore.block;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import santa.lore.CustomizedLore;
import santa.lore.network.LoreChangePacket;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;

public class GuiLoreCustomizer extends GuiContainer implements IContainerListener {
    private GuiTextField textField;
    private ContainerLoreCustomizer container;
    private static final ResourceLocation texture = new ResourceLocation(CustomizedLore.MOD_ID + ":textures/gui/customizer.png");

    public GuiLoreCustomizer(InventoryPlayer invPlayer, World world, BlockPos pos) {
        super(new ContainerLoreCustomizer(invPlayer, world, pos, Minecraft.getMinecraft().player));
        this.container = (ContainerLoreCustomizer) this.inventorySlots;
    }

    @Override
    public void initGui() {
        super.initGui();
        Keyboard.enableRepeatEvents(true);
        int i = (width - xSize) / 2;
        int j = (height - ySize) / 2;
        textField = new GuiTextField(0, fontRenderer, i + 62, j + 24, 103, 12);
        textField.setTextColor(-1);
        textField.setDisabledTextColour(-1);
        textField.setEnableBackgroundDrawing(false);
        textField.setMaxStringLength(300);
        inventorySlots.removeListener(this);
        inventorySlots.addListener(this);
    }

    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
        Keyboard.enableRepeatEvents(false);
        inventorySlots.removeListener(this);
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
    public void updateCraftingInventory(@Nonnull Container container, @Nonnull NonNullList<ItemStack> contents) {
        ItemStack inSlotZero = container.getSlot(0).getStack();
        if (!inSlotZero.isEmpty()) {
            sendSlotContents(container, 0, inSlotZero);
        }
    }

    @Override
    public void sendSlotContents(@Nonnull Container container, int slot, @Nullable ItemStack itemstack) {
        if (slot == 0) {
            //noinspection ConstantConditions
            boolean hasLore = !itemstack.isEmpty() && itemstack.hasTagCompound() &&
              itemstack.getTagCompound().hasKey("CustomLore");
            String s = hasLore ? itemstack.getTagCompound().getString("CustomLore") : "";
            textField.setText(s);
            textField.setEnabled(!itemstack.isEmpty());

            if (!itemstack.isEmpty()) {
                updateText();
            }
        }
    }

    @Override
    public void sendProgressBarUpdate(@Nonnull Container container, int varToUpdate, int newValue) {}

    @Override
    public void sendAllWindowProperties(@Nonnull Container container, @Nonnull IInventory inventory) {}

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        textField.drawTextBox();
    }
}
