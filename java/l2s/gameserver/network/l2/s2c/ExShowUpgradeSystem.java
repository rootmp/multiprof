package l2s.gameserver.network.l2.s2c;

import l2s.commons.network.PacketWriter;

/**
 * @author VISTALL
 */
public class ExShowUpgradeSystem implements IClientOutgoingPacket
{
	private final int unk;

	public ExShowUpgradeSystem(int unk)
	{
		this.unk = unk;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeH(unk); // unk, maybe type
		packetWriter.writeH(100); // price percent
		packetWriter.writeD(0x00); // unk
		return true;
	}
}