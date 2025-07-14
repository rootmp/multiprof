package l2s.gameserver.network.l2.s2c;

import l2s.commons.network.PacketWriter;
import l2s.gameserver.model.pledge.SubUnit;

public class PledgeReceiveSubPledgeCreated implements IClientOutgoingPacket
{
	private int type;
	private String _name, leader_name;

	public PledgeReceiveSubPledgeCreated(SubUnit subPledge)
	{
		type = subPledge.getType();
		_name = subPledge.getName();
		leader_name = subPledge.getLeaderName();
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(0x01);
		packetWriter.writeD(type);
		packetWriter.writeS(_name);
		packetWriter.writeS(leader_name);
		return true;
	}
}