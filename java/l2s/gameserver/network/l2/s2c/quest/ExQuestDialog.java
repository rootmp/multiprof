package l2s.gameserver.network.l2.s2c.quest;

import l2s.commons.network.PacketWriter;
import l2s.gameserver.network.l2.s2c.IClientOutgoingPacket;

public class ExQuestDialog implements IClientOutgoingPacket
{
	private int _id;
	private int _DialogType;

	public ExQuestDialog(int id, int DialogType)
	{
		_id = id;
		_DialogType = DialogType;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(_id);//id
		packetWriter.writeC(_DialogType);//cDialogType
		return true;
	}
}