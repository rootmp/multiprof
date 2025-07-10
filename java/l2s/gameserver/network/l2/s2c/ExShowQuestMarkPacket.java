package l2s.gameserver.network.l2.s2c;
import l2s.commons.network.PacketWriter;

public class ExShowQuestMarkPacket implements IClientOutgoingPacket
{
	private final int _questId, _cond;

	public ExShowQuestMarkPacket(int questId, int cond)
	{
		_questId = questId;
		_cond = cond;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(_questId);
		packetWriter.writeD(_cond);
	}
}