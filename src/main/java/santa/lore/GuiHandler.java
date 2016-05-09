package santa.lore;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import santa.lore.block.ContainerLoreCustomizer;
import santa.lore.block.GuiLoreCustomizer;

public class GuiHandler implements IGuiHandler {
    public static int CUSTOMIZER_GUI_ID = 0;
    @Override
    public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
        if (id == CUSTOMIZER_GUI_ID) {
            return new ContainerLoreCustomizer(player.inventory, world, new BlockPos(x, y, z), player);
        }
        return null;
    }

    @Override
    public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
        if (id == CUSTOMIZER_GUI_ID) {
            return new GuiLoreCustomizer(player.inventory, world, new BlockPos(x, y, z));
        }
        return null;
    }
}
