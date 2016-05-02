package santa.lore;

import cpw.mods.fml.common.network.IGuiHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import santa.lore.block.ContainerLoreCustomizer;
import santa.lore.block.GuiLoreCustomizer;

public class GuiHandler implements IGuiHandler {
    public static int CUSTOMIZER_GUI_ID = 0;
    @Override
    public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
        if (id == CUSTOMIZER_GUI_ID) {
            return new ContainerLoreCustomizer(player.inventory, world, x, y, z, player);
        }
        return null;
    }

    @Override
    public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
        if (id == CUSTOMIZER_GUI_ID) {
            return new GuiLoreCustomizer(player.inventory, world, x, y, z);
        }
        return null;
    }
}
