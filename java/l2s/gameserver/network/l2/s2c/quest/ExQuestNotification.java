package l2s.gameserver.network.l2.s2c.quest;

import l2s.commons.network.PacketWriter;
import l2s.gameserver.network.l2.s2c.IClientOutgoingPacket;

public class ExQuestNotification implements IClientOutgoingPacket
{
	private int _id;
	private int _count;
	private QuestNotifType _notifType;
	
	public enum QuestNotifType
	{
		START,
		PROGRESS,
		COMPLETED
	}
	
	public ExQuestNotification(int id, int count, QuestNotifType notifType)
	{
		_id = id;
		_count = count;
		_notifType = notifType;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
	  packetWriter.writeD(_id);//nID;
	  packetWriter.writeD(_count);//nCount;
	  packetWriter.writeC(_notifType.ordinal());//cNotifType;
	  return true;
	}
}