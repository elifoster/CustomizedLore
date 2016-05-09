package santa.lore.network;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import santa.lore.block.ContainerLoreCustomizer;

public class LoreChangePacketHandler implements IMessageHandler<LoreChangePacket, IMessage> {
    @Override
    public IMessage onMessage(LoreChangePacket message, MessageContext ctx) {
        EntityPlayerMP player = ctx.getServerHandler().playerEntity;
        ((ContainerLoreCustomizer) player.openContainer).updateLore(message.lore);
        return null;
    }
}
