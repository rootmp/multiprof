package l2s.gameserver.network.l2.s2c;

import l2s.commons.network.PacketWriter;

/**
 * @author Bonux
 **/
public class ExMultiSellResult implements IClientOutgoingPacket
{
	public static final L2GameServerPacket SUCCESS = new ExMultiSellResult();

	private final boolean _success;
	private final int _unk1;
	private final int _unk2;

	private ExMultiSellResult()
	{
		_success = true;
		_unk1 = 0;
		_unk2 = 0;
	}

	public ExMultiSellResult(int unk1, int unk2)
	{
		_success = false;
		_unk1 = unk1;
		_unk2 = unk2;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeC(_success); // Success
		packetWriter.writeD(_unk1); // Unk
		packetWriter.writeD(_unk2); // Unk
		return true;
	}
}