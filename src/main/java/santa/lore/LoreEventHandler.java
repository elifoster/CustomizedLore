package santa.lore;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;

public class LoreEventHandler {
    @SubscribeEvent
    public void addCustomLore(ItemTooltipEvent event) {
        ItemStack stack = event.itemStack;
        if (event.entityPlayer == null || !event.entityPlayer.worldObj.isRemote || stack == null ||
          !stack.hasTagCompound() || !stack.getTagCompound().hasKey("CustomLore")) {
            return;
        }
        String lore = stack.getTagCompound().getString("CustomLore");
        if (lore.isEmpty()) {
            return;
        }
        if (lore.contains("§")) {
            event.toolTip.add(lore);
        } else {
            event.toolTip.add(EnumChatFormatting.GRAY + lore);
        }
    }
}
