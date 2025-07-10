package l2s.gameserver.network.l2.s2c;
import l2s.commons.network.PacketWriter;

import l2s.gameserver.model.Party;
import l2s.gameserver.model.Player;

/**
 * ch Sddd
 */
public class ExMPCCPartyInfoUpdate implements IClientOutgoingPacket
{
	private Party _party;
	Player _leader;
	private int _mode, _count;

	/**
	 * @param party
	 * @param mode  0 = Remove, 1 = Add
	 */
	public ExMPCCPartyInfoUpdate(Party party, int mode)
	{
		_party = party;
		_mode = mode;
		_count = _party.getMemberCount();
		_leader = _party.getPartyLeader();
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeS(_leader.getName());
		packetWriter.writeD(_leader.getObjectId());
		packetWriter.writeD(_count);
		packetWriter.writeD(_mode); // mode 0 = Remove Party, 1 = AddParty, maybe more...
	}
}