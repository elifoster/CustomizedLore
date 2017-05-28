package santa.lore;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class LoreEventHandler {
    @SubscribeEvent
    public void addCustomLore(ItemTooltipEvent event) {
        ItemStack stack = event.getItemStack();
        EntityPlayer player = event.getEntityPlayer();
        //noinspection ConstantConditions
        if (player == null || !player.world.isRemote || stack == null || !stack.hasTagCompound() ||
          !stack.getTagCompound().hasKey("CustomLore")) {
            return;
        }
        String lore = stack.getTagCompound().getString("CustomLore");
        if (lore.isEmpty()) {
            return;
        }
        if (lore.contains("§")) {
            event.getToolTip().add(lore);
        } else {
            event.getToolTip().add(TextFormatting.GRAY + lore);
        }
    }
}
