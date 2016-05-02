package santa.lore.block;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.*;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class ContainerLoreCustomizer extends Container {
    private IInventory output = new InventoryCraftResult();
    private IInventory input = new InventoryBasic("Customize", true, 1) {
        @Override
        public void markDirty() {
            super.markDirty();
            ContainerLoreCustomizer.this.onCraftMatrixChanged(this);
        }
    };

    private World world;
    public String itemLore = "";

    public ContainerLoreCustomizer(InventoryPlayer playerInv, final World world, final int x, final int y, final int z, EntityPlayer player) {
        this.world = world;
        this.addSlotToContainer(new Slot(input, 0, 50, 47) {
            @Override
            public int getSlotStackLimit() {
                return 1;
            }
        });

        this.addSlotToContainer(new Slot(output, 1, 106, 47) {
            @Override
            public boolean isItemValid(ItemStack stack) {
                return false;
            }

            @Override
            public boolean canTakeStack(EntityPlayer player) {
                return this.getHasStack();
            }

            @Override
            public void onPickupFromSlot(EntityPlayer player, ItemStack stack) {
                ContainerLoreCustomizer.this.input.setInventorySlotContents(0, null);

            }
        });

        int i;
        for (i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlotToContainer(new Slot(playerInv, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for (i = 0; i < 9; ++i) {
            this.addSlotToContainer(new Slot(playerInv, i, 8 + i * 18, 142));
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return true;
    }

    @Override
    public void onCraftMatrixChanged(IInventory inv) {
        super.onCraftMatrixChanged(inv);

        if (inv == this.input) {
            this.updateLore(this.itemLore);
        }
    }

    public static void setNBT(ItemStack stack, String lore) {
        if (lore.isEmpty() && stack.hasTagCompound() && stack.getTagCompound().hasKey("CustomLore")) {
            stack.getTagCompound().removeTag("CustomLore");
        } else {
            if (!stack.hasTagCompound()) {
                stack.setTagCompound(new NBTTagCompound());
            }
            stack.getTagCompound().setString("CustomLore", lore);
        }
    }

    private void updateOutput() {
        ItemStack itemstack = this.input.getStackInSlot(0);

        if (itemstack == null) {
            this.output.setInventorySlotContents(0, null);
        } else {
            ItemStack copy = itemstack.copy();
            setNBT(copy, this.itemLore);
            this.output.setInventorySlotContents(0, copy);
            this.detectAndSendChanges();
        }
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int slotIndex) {
        return null;
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void updateLore(String newLore) {
        newLore = newLore.replace("&&", "&");
        char[] chars = newLore.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            if (i == chars.length - 1) {
                continue;
            }
            char character = chars[i];
            char nextChar = chars[i + 1];
            if (character == '&' && nextChar != '&') {
                chars[i] = '§';
            }
        }
        this.itemLore = new String(chars);
        this.updateOutput();
    }

    @Override
    public void onContainerClosed(EntityPlayer player) {
        super.onContainerClosed(player);

        if (!this.world.isRemote) {
            ItemStack itemstack = this.input.getStackInSlot(0);
            if (itemstack != null) {
                player.dropPlayerItemWithRandomChoice(itemstack, false);
            }
        }
    }
}
