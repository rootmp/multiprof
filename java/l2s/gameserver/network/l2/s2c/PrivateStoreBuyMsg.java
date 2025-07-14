package l2s.gameserver.network.l2.s2c;

import org.apache.commons.lang3.StringUtils;

import l2s.commons.network.PacketWriter;
import l2s.gameserver.model.Player;

public class PrivateStoreBuyMsg implements IClientOutgoingPacket
{
	private int _objId;
	private String _name;

	/**
	 * Название личного магазина покупки
	 * 
	 * @param player
	 */
	public PrivateStoreBuyMsg(Player player, boolean showName)
	{
		_objId = player.getObjectId();
		_name = showName ? StringUtils.defaultString(player.getBuyStoreName()) : StringUtils.EMPTY;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(_objId);
		packetWriter.writeS(_name);
		return true;
	}
}