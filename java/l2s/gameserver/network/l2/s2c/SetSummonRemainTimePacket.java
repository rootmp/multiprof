package l2s.gameserver.network.l2.s2c;
import l2s.commons.network.PacketWriter;

import l2s.gameserver.model.Servitor;

public class SetSummonRemainTimePacket implements IClientOutgoingPacket
{
	private final int _maxFed;
	private final int _curFed;

	public SetSummonRemainTimePacket(Servitor summon)
	{
		_curFed = summon.getCurrentFed();
		_maxFed = summon.getMaxFed();
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(_maxFed);
		packetWriter.writeD(_curFed);
		return true;
	}
}