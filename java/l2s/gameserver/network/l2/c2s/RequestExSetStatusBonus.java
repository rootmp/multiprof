package l2s.gameserver.network.l2.c2s;
import l2s.commons.network.PacketReader;
import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.GameClient;

/**
 * @author nexvill
 */
public class RequestExSetStatusBonus implements IClientIncomingPacket
{
	private int _str, _dex, _con, _int, _wit, _men;

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		packet.readD(); // total bytes
		_str = packet.readH();
		_dex = packet.readH();
		_con = packet.readH();
		_int = packet.readH();
		_wit = packet.readH();
		_men = packet.readH();
		return true;
	}

	@Override
	public void run(GameClient client)
	{
		Player activeChar = client.getActiveChar();
		if (activeChar == null)
			return;

		if (activeChar.usedStatPoints() >= activeChar.getTotalStatPoints())
			return;

		activeChar.setStatBonus(0, _str + activeChar.getStatBonus(0));
		activeChar.setStatBonus(1, _dex + activeChar.getStatBonus(1));
		activeChar.setStatBonus(2, _con + activeChar.getStatBonus(2));
		activeChar.setStatBonus(3, _int + activeChar.getStatBonus(3));
		activeChar.setStatBonus(4, _wit + activeChar.getStatBonus(4));
		activeChar.setStatBonus(5, _men + activeChar.getStatBonus(5));
		activeChar.broadcastUserInfo(true);
	}
}