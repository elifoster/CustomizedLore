package santa.lore;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import santa.lore.block.BlockLoreCustomizer;
import santa.lore.network.LoreChangePacket;
import santa.lore.network.LoreChangePacketHandler;

@Mod(name = "CustomizedLore", modid = CustomizedLore.MOD_ID, version = "1.0.0")
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

        // These are the 2 tags that I know are used for the workbench.
        OreDictionary.registerOre("craftingTableWood", Blocks.crafting_table);
        OreDictionary.registerOre("crafterWood", Blocks.crafting_table);
        GameRegistry.addRecipe(new ShapelessOreRecipe(LORE_CUSTOMIZER, "craftingTableWood", Items.book));
        GameRegistry.addRecipe(new ShapelessOreRecipe(LORE_CUSTOMIZER, "crafterWood", Items.book));
    }
}
