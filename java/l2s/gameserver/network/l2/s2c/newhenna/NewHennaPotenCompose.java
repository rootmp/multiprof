package l2s.gameserver.network.l2.s2c.newhenna;

import l2s.commons.network.PacketWriter;
import l2s.gameserver.network.l2.s2c.IClientOutgoingPacket;

public class NewHennaPotenCompose implements IClientOutgoingPacket
{
	private final int _resultHennaId;
	private final int _resultItemId;
	private final boolean _success;
	
	public NewHennaPotenCompose(int resultHennaId, int resultItemId, boolean success)
	{
		_resultHennaId = resultHennaId;
		_resultItemId = resultItemId;
		_success = success;
	}
	
	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(_resultHennaId);
		packetWriter.writeD(_resultItemId);
		packetWriter.writeC(_success ? 1 : 0);
		return true;
	}
}
