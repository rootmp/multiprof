package l2s.gameserver.network.l2.s2c;
import l2s.commons.network.PacketWriter;

//пир отправке этого пакета на экране появляется иконка получения письма
public class ExCuriousHouseEnter implements IClientOutgoingPacket
{
	public static final L2GameServerPacket STATIC = new ExCuriousHouseEnter();

	public void ExCuriousHouseEnter()
	{
		// TRIGGER
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		//
	}
}
