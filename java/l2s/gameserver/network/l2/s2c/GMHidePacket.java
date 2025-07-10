package l2s.gameserver.network.l2.s2c;
import l2s.commons.network.PacketWriter;

public class GMHidePacket implements IClientOutgoingPacket
{
	private final int obj_id;

	public GMHidePacket(int id)
	{
		obj_id = id; // TODO хз чей id должен посылатся, нужно эксперементировать
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(obj_id);
	}
}