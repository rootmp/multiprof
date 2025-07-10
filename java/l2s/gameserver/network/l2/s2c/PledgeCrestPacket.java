package l2s.gameserver.network.l2.s2c;
import l2s.commons.network.PacketWriter;

/**
 * @reworked by nexvill
 */
public class PledgeCrestPacket implements IClientOutgoingPacket
{
	private int _clanId;
	private int _crestId;
	private int _crestSize;
	private byte[] _data;

	public PledgeCrestPacket(int clanId, int crestId, byte[] data)
	{
		_clanId = clanId;
		_crestId = crestId;
		_data = data;
		_crestSize = _data.length;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(_clanId);
		packetWriter.writeD(_crestId);
		if (_crestSize != 0)
		{
			packetWriter.writeD(_crestSize);
			packetWriter.writeD(_crestSize);
			writeB(_data);
		}
		else
		{
			packetWriter.writeD(0);
		}
	}
}