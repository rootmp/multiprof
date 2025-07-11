package l2s.gameserver.network.l2.s2c;

import l2s.commons.network.PacketWriter;

public class ExDuelAskStart implements IClientOutgoingPacket
{
	String _requestor;
	int _isPartyDuel;

	public ExDuelAskStart(String requestor, int isPartyDuel)
	{
		_requestor = requestor;
		_isPartyDuel = isPartyDuel;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeS(_requestor);
		packetWriter.writeD(_isPartyDuel);
		return true;
	}
}