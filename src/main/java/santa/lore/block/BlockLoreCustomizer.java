package santa.lore.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import santa.lore.CustomizedLore;
import santa.lore.GuiHandler;

public class BlockLoreCustomizer extends Block {
    public BlockLoreCustomizer() {
        super(Material.ANVIL);
        String name = CustomizedLore.MOD_ID + ":" + CustomizedLore.LORE_CUSTOMIZER_NAME;
        setUnlocalizedName(name);
        setHardness(5F);
        setResistance(5F);
        setCreativeTab(CreativeTabs.DECORATIONS);
        setRegistryName(name);
        GameRegistry.register(this);
        GameRegistry.register(new ItemBlock(this).setRegistryName(getRegistryName()));
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (!world.isRemote) {
            player.openGui(CustomizedLore.mod, GuiHandler.CUSTOMIZER_GUI_ID, world, pos.getX(), pos.getY(), pos.getZ());
        }
        return true;
    }
}
