package l2s.gameserver.network.l2.s2c;
import l2s.commons.network.PacketWriter;

import l2s.gameserver.Config;

public class ExPledgeEmblem implements IClientOutgoingPacket
{
	private int _clanId, _crestId, _crestPart, _totalSize;
	private byte[] _data;

	public ExPledgeEmblem(int clanId, int crestId, int crestPart, int totalSize, byte[] data)
	{
		_clanId = clanId;
		_crestId = crestId;
		_crestPart = crestPart;
		_totalSize = totalSize;
		_data = data;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(Config.REQUEST_ID);
		packetWriter.writeD(_clanId);
		packetWriter.writeD(_crestId);
		packetWriter.writeD(_crestPart);
		packetWriter.writeD(_totalSize);
		packetWriter.writeD(_data.length);
		writeB(_data);
	}
}