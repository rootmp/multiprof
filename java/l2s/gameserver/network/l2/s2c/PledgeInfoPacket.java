package l2s.gameserver.network.l2.s2c;
import l2s.commons.network.PacketWriter;

import l2s.gameserver.Config;
import l2s.gameserver.model.pledge.Clan;

public class PledgeInfoPacket implements IClientOutgoingPacket
{
	private int clan_id;
	private String clan_name, ally_name;

	public PledgeInfoPacket(Clan clan)
	{
		clan_id = clan.getClanId();
		clan_name = clan.getName();
		ally_name = clan.getAlliance() == null ? "" : clan.getAlliance().getAllyName();
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(Config.REQUEST_ID);
		packetWriter.writeD(clan_id);
		packetWriter.writeS(clan_name);
		packetWriter.writeS(ally_name);
	}
}