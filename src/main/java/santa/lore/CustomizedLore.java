package santa.lore;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import santa.lore.block.BlockLoreCustomizer;
import santa.lore.network.LoreChangePacket;
import santa.lore.network.LoreChangePacketHandler;

@Mod(name = "CustomizedLore", modid = CustomizedLore.MOD_ID, version = "3.0.0", acceptedMinecraftVersions = "[1.9,1.10.2]")
public class CustomizedLore {
    public static final String MOD_ID = "customizedlore";

    public static Block LORE_CUSTOMIZER;
    public static String LORE_CUSTOMIZER_NAME = "lore_customizer";

    @Mod.Instance(MOD_ID)
    public static CustomizedLore mod;

    public static SimpleNetworkWrapper channel;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        LORE_CUSTOMIZER = new BlockLoreCustomizer();
        channel = NetworkRegistry.INSTANCE.newSimpleChannel("CustomizedLoreChannel");
        channel.registerMessage(LoreChangePacketHandler.class, LoreChangePacket.class, 0, Side.SERVER);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        NetworkRegistry.INSTANCE.registerGuiHandler(mod, new GuiHandler());
        MinecraftForge.EVENT_BUS.register(new LoreEventHandler());

        if (event.getSide().isClient()) {
            ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(LORE_CUSTOMIZER), 0,
              new ModelResourceLocation(MOD_ID + ":" + LORE_CUSTOMIZER_NAME, "inventory"));
            Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(Item.getItemFromBlock(LORE_CUSTOMIZER),
              0, new ModelResourceLocation(MOD_ID + ":" + LORE_CUSTOMIZER_NAME, "inventory"));
        }

        // These are the 2 tags that I know are used for the workbench.
        OreDictionary.registerOre("craftingTableWood", Blocks.CRAFTING_TABLE);
        OreDictionary.registerOre("crafterWood", Blocks.CRAFTING_TABLE);
        GameRegistry.addRecipe(new ShapelessOreRecipe(LORE_CUSTOMIZER, "craftingTableWood", Items.BOOK));
        GameRegistry.addRecipe(new ShapelessOreRecipe(LORE_CUSTOMIZER, "crafterWood", Items.BOOK));
    }
}
