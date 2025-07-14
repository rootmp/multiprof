package l2s.gameserver.network.l2.s2c;

import l2s.commons.network.PacketWriter;

public class SurrenderPledgeWar implements IClientOutgoingPacket
{
	private String _pledgeName;
	private String _char;

	public SurrenderPledgeWar(String pledge, String charName)
	{
		_pledgeName = pledge;
		_char = charName;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeS(_pledgeName);
		packetWriter.writeS(_char);
		return true;
	}
}