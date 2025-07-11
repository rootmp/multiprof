package l2s.gameserver.network.l2.s2c;

import l2s.commons.network.PacketWriter;

public class AskJoinPledgePacket implements IClientOutgoingPacket
{
	private int _requestorId;
	private String _pledgeName;

	public AskJoinPledgePacket(int requestorId, String pledgeName)
	{
		_requestorId = requestorId;
		_pledgeName = pledgeName;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(_requestorId);
		packetWriter.writeS(""); // Invitor name
		packetWriter.writeS(_pledgeName);
		packetWriter.writeD(0x00); // Pledge type
		packetWriter.writeS(""); // Pledge Unit name
		return true;
	}
}