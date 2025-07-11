package l2s.gameserver.network.l2.s2c;

import l2s.commons.network.PacketWriter;

public class ExVariationCancelResult implements IClientOutgoingPacket
{
	private int _closeWindow;
	private int _unk1;

	public ExVariationCancelResult(int result)
	{
		_closeWindow = 1;
		_unk1 = result;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(_unk1);
		packetWriter.writeD(_closeWindow);
		return true;
	}
}