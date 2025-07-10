package l2s.gameserver.network.l2.s2c;
import l2s.commons.network.PacketWriter;

//пропадает почти весь интерфейс и пооявляется кнопка отказ
//связан с пакетом RequestLeaveCuriousHouse
public class ExCuriousHouseLeave implements IClientOutgoingPacket
{
	public static final L2GameServerPacket STATIC = new ExCuriousHouseLeave();

	private ExCuriousHouseLeave()
	{
		// TRIGGER
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		//
	}
}
