package l2s.gameserver.network.l2.s2c;

import l2s.commons.network.PacketWriter;
import l2s.gameserver.model.Player;

/**
 * @author Bonux
 **/
public class ExShowBeautyMenuPacket implements IClientOutgoingPacket
{
	public static final int CHANGE_STYLE = 0x00;
	public static final int RESTORE_STYLE = 0x01;

	private final int _type;
	private final int _hairStyle;
	private final int _hairColor;
	private final int _face;

	public ExShowBeautyMenuPacket(Player player, int type)
	{
		_type = type;
		_hairStyle = player.getBeautyHairStyle() > 0 ? player.getBeautyHairStyle() : player.getHairStyle();
		_hairColor = player.getBeautyHairColor() > 0 ? player.getBeautyHairColor() : player.getHairColor();
		_face = player.getBeautyFace() > 0 ? player.getBeautyFace() : player.getFace();
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(_type); // 0x00 - изменение стиля, 0x01 отмена стиля
		packetWriter.writeD(_hairStyle);
		packetWriter.writeD(_hairColor);
		packetWriter.writeD(_face);
		return true;
	}
}
