package l2s.gameserver.network.l2.s2c;

import l2s.commons.network.PacketWriter;

public class ExLockedResult implements IClientOutgoingPacket
{
	private int _Lock;
	private int _Result;

	public ExLockedResult(int lock, int result)
	{
		_Lock = lock;
		_Result = result;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeC(_Lock);
		packetWriter.writeC(_Result);
		return true;
	}
}