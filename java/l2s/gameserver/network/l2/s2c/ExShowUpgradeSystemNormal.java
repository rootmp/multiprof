package l2s.gameserver.network.l2.s2c;

import l2s.commons.network.PacketWriter;

/**
 * @author VISTALL
 */
public class ExShowUpgradeSystemNormal implements IClientOutgoingPacket
{
	private final int type;

	public ExShowUpgradeSystemNormal(int type)
	{
		this.type = type;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeH(0x01); // unk, maybe type
		packetWriter.writeH(type); // unk, maybe type
		packetWriter.writeH(100); // unk, maybe chance
		packetWriter.writeD(0x00); // unk
		packetWriter.writeD(0x00); // unk
		return true;
	}
}