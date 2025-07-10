package l2s.gameserver.network.l2.s2c;
import l2s.commons.network.PacketWriter;

public class ShowTutorialMarkPacket implements IClientOutgoingPacket
{
	/**
	 * После клика по знаку вопроса клиент попросит html-ку с этим номером.
	 */
	private boolean _quest;
	private int _tutorialId;

	public ShowTutorialMarkPacket(boolean quest, int tutorialId)
	{
		_quest = quest;
		_tutorialId = tutorialId;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeC(_quest);
		packetWriter.writeD(_tutorialId);
	}
}