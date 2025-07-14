package l2s.gameserver.network.l2.s2c;

import l2s.commons.network.PacketWriter;

/**
 * @reworked by nexvill
 */
public class AllianceCrestPacket implements IClientOutgoingPacket
{
	private int _crestId;
	private int _clanId;
	private byte[] _data;

	public AllianceCrestPacket(int crestId, int clanId, byte[] data)
	{
		_crestId = crestId;
		_clanId = clanId;
		_data = data;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(_clanId);
		packetWriter.writeD(_crestId);
		if (_data.length > 0)
		{
			packetWriter.writeD(_data.length);
			packetWriter.writeD(_data.length);
			packetWriter.writeB(_data);
		}
		else
		{
			packetWriter.writeD(0);
		}
		return true;
	}
}