package l2s.gameserver.network.l2.c2s;
import l2s.commons.network.PacketReader;
import l2s.gameserver.model.Party;
import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.GameClient;

public class RequestPartyLootModification implements IClientIncomingPacket
{
	private byte _mode;

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		_mode = (byte) packet.readD();
		return true;
	}

	@Override
	public void run(GameClient client)
	{
		Player activeChar = client.getActiveChar();
		if (activeChar == null)
			return;

		if (_mode < 0 || _mode > Party.ITEM_ORDER_SPOIL)
			return;

		Party party = activeChar.getParty();
		if (party == null || _mode == party.getLootDistribution() || party.getPartyLeader() != activeChar)
			return;

		party.requestLootChange(_mode);
	}
}
