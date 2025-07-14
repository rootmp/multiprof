package l2s.gameserver.network.l2.s2c;

import l2s.commons.network.PacketWriter;
import l2s.gameserver.model.pledge.Clan;

public class ExGetPledgeCrestPreset implements IClientOutgoingPacket
{
	private Clan _clan;

	public ExGetPledgeCrestPreset(Clan clan)
	{
		_clan = clan;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(1);//nResult;
		packetWriter.writeD(_clan.getClanId());//nPledgeSId;
		packetWriter.writeD(_clan.getCrestId());//nPresetCrestDBID;
		return true;
	}
}
