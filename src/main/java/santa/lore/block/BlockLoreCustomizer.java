package santa.lore.block;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import santa.lore.CustomizedLore;
import santa.lore.GuiHandler;

public class BlockLoreCustomizer extends Block {
    private IIcon topIcon;
    private IIcon sideIcon;

    public BlockLoreCustomizer() {
        super(Material.anvil);
        String name = CustomizedLore.MOD_ID + ":" + CustomizedLore.LORE_CUSTOMIZER_NAME;
        this.setBlockName(name);
        this.setHardness(5F);
        this.setResistance(5F);
        this.setCreativeTab(CreativeTabs.tabDecorations);
        this.setBlockTextureName(name);
        GameRegistry.registerBlock(this, CustomizedLore.LORE_CUSTOMIZER_NAME);
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int meta, float p_149727_7_, float p_149727_8_, float p_149727_9_) {
        if (!world.isRemote) {
            player.openGui(CustomizedLore.mod, GuiHandler.CUSTOMIZER_GUI_ID, world, x, y, z);
        }
        return true;
    }

    @Override
    public IIcon getIcon(int side, int meta) {
        switch (side) {
            case 0: {
                return Blocks.planks.getBlockTextureFromSide(side);
            }
            case 1: {
                return this.topIcon;
            }
            default: {
                return this.sideIcon;
            }
        }
    }

    @Override
    public void registerBlockIcons(IIconRegister ir) {
        this.topIcon = ir.registerIcon(this.getTextureName() + "_top");
        this.sideIcon = ir.registerIcon(this.getTextureName() + "_side");
    }
}
