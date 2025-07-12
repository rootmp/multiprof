package l2s.gameserver.network.l2.c2s;

import l2s.commons.network.PacketReader;
import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.GameClient;
import l2s.gameserver.utils.ItemFunctions;

public class RequestExChangeNickNameColorIcon implements IClientIncomingPacket
{
	private int COLORS[] = {
			0xFFFF7,
			0xFFFF7,
			0x9090FD, //Розовый
			0x7A48FD, //Пурпурный
			0x96F9FD, //Желтый
			0xFB98EE, //Сиреневый
			0xFD5A90, //Фиолетовый
			0x02EE96, //Светло-зеленый
			0x9AA002, //Темно-зеленый
			0x6F8DA2, //Желтая охра
			0x455F92, //Шоколадный
			0x919192, //Серебряный
			0x00C9FD, //Истинный Золотой
			0x1E9DFD, //Истинный Апельсин
			0xFD65FD, //Истинный Розовый
			0x0202EE, //Истинный Красный
			0xFDD900,//Небесный голубой
	};

	private int _nItemClassID;
	private int _nColorIndex;
	private String _sNickName;

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		_nItemClassID = packet.readD();
		_nColorIndex = packet.readD();
		_sNickName = packet.readSizedString();
		return true;
	}

	@Override
	public void run(GameClient client)
	{
		Player activeChar = client.getActiveChar();
		if(activeChar == null)
			return;
		//TODO добавить проверки на доступность цвета по ид итема
		if(ItemFunctions.deleteItem(activeChar, _nItemClassID, 1))
		{
			if(!_sNickName.isBlank())
				activeChar.setTitle(_sNickName);

			activeChar.setTitleColor(COLORS[_nColorIndex]);
			activeChar.broadcastCharInfo();
		}
	}
}