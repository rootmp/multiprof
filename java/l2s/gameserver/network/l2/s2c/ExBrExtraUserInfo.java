package l2s.gameserver.network.l2.s2c;

import l2s.commons.network.PacketWriter;
import l2s.gameserver.model.Player;

//@Deprecated
public class ExBrExtraUserInfo implements IClientOutgoingPacket
{
	private int _objectId;
	private int _effect3;
	private int _lectureMark;

	public ExBrExtraUserInfo(Player cha)
	{
		_objectId = cha.getObjectId();
		_effect3 = 0/* cha.getAbnormalEffect3() */;
		_lectureMark = cha.getLectureMark();
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(_objectId); // object id of player
		packetWriter.writeD(_effect3); // event effect id
		packetWriter.writeC(_lectureMark);
		return true;
	}
}