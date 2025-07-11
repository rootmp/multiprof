package l2s.gameserver.network.l2.s2c;

import l2s.commons.network.PacketWriter;

/**
 * Format: ch S
 */
public class ExAskJoinPartyRoom implements IClientOutgoingPacket
{
	private String _charName;
	private String _roomName;

	public ExAskJoinPartyRoom(String charName, String roomName)
	{
		_charName = charName;
		_roomName = roomName;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeS(_charName);
		packetWriter.writeS(_roomName);
		return true;
	}
}