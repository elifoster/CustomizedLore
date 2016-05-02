package santa.lore.network;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import io.netty.buffer.ByteBuf;

public class LoreChangePacket implements IMessage {
    public String lore;

    public LoreChangePacket() {}

    public LoreChangePacket(String lore) {
        this.lore = lore;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.lore = ByteBufUtils.readUTF8String(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, this.lore);
    }
}
