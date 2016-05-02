package santa.lore.network;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import net.minecraft.entity.player.EntityPlayerMP;
import santa.lore.block.ContainerLoreCustomizer;

public class LoreChangePacketHandler implements IMessageHandler<LoreChangePacket, IMessage> {
    @Override
    public IMessage onMessage(LoreChangePacket message, MessageContext ctx) {
        EntityPlayerMP player = ctx.getServerHandler().playerEntity;
        ((ContainerLoreCustomizer) player.openContainer).updateLore(message.lore);
        return null;
    }
}
