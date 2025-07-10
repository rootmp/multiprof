package l2s.gameserver.network.l2.s2c;
import l2s.commons.network.PacketWriter;

public class ExChangeNPCState implements IClientOutgoingPacket
{
	private int _objId;
	private int _state;

	public ExChangeNPCState(int objId, int state)
	{
		_objId = objId;
		_state = state;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(_objId);
		packetWriter.writeD(_state);
	}
}
