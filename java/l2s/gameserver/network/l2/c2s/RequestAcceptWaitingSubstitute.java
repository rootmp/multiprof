package l2s.gameserver.network.l2.c2s;
import l2s.commons.network.PacketReader;
import l2s.gameserver.network.l2.GameClient;


/**
 * Created by IntelliJ IDEA. User: Cain Date: 23.05.12 Time: 23:09 ответ от чара
 * выбранного на замену
 */
public class RequestAcceptWaitingSubstitute implements IClientIncomingPacket
{
	private int _flag;
	private int _unk1;
	private int _unk2;

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		_flag = packet.readD();
		_unk1 = packet.readD();
		_unk2 = packet.readD();
		return true;
	}

	@Override
	public void run(GameClient client)
	{
		//
	}
}
