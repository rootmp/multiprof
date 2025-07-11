package l2s.gameserver.network.l2.c2s;
import org.apache.commons.lang3.StringUtils;

import l2s.commons.ban.BanBindType;
import l2s.commons.network.PacketReader;
import l2s.gameserver.instancemanager.GameBanManager;
import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.ConnectionState;
import l2s.gameserver.network.l2.GameClient;
import l2s.gameserver.network.l2.s2c.ActionFailPacket;
import l2s.gameserver.network.l2.s2c.CharacterSelectedPacket;
import l2s.gameserver.network.l2.s2c.ExNeedToChangeName;
import l2s.gameserver.utils.AutoBan;

public class CharacterSelected implements IClientIncomingPacket
{
	private int _charSlot;

	/**
	 * Format: cdhddd
	 */
	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		_charSlot = packet.readD();
		return true;
	}

	@Override
	public void run(GameClient client)
	{
		if (client.getActiveChar() != null)
			return;

		if (!client.secondaryAuthed())
		{
			client.sendPacket(ActionFailPacket.STATIC);
			return;
		}

		int objId = client.getObjectIdForSlot(_charSlot);

		if (GameBanManager.getInstance().isBanned(BanBindType.PLAYER, objId) || AutoBan.isBanned(objId))
		{
			client.sendPacket(ActionFailPacket.STATIC);
			return;
		}

		Player activeChar = client.loadCharFromDisk(_charSlot);
		if (activeChar == null)
		{
			client.sendPacket(ActionFailPacket.STATIC);
			return;
		}

		if (activeChar.getAccessLevel() < 0)
			activeChar.setAccessLevel(0);

		client.setConnectionState(ConnectionState.JOINING_GAME);
		activeChar.setOnlineStatus(true); // Заглушка для МА, TODO: Перевести на другой МА или МА перевести на xml-rpc.
		if(client.getHwidHolder() != null)
			activeChar.storeLastIpAndHWID(client.getIpAddr(), client.getHwidString());

		String changedOldName = activeChar.getVar(Player.CHANGED_OLD_NAME);
		if (changedOldName != null && !StringUtils.isEmpty(changedOldName))
		{
			client.sendPacket(new ExNeedToChangeName(ExNeedToChangeName.TYPE_PLAYER, ExNeedToChangeName.NONE_REASON, changedOldName));
			return;
		}
		client.sendPacket(new CharacterSelectedPacket(activeChar, client.getSessionKey().playOkID1));
	}
}