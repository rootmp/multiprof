package l2s.gameserver.network.l2.c2s;
import l2s.commons.network.PacketReader;
import l2s.gameserver.network.l2.GameClient;


import l2s.gameserver.model.Creature;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.actor.instances.creature.Abnormal;

public class RequestDispel implements IClientIncomingPacket
{
	private int _objectId, _id, _level;

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		_objectId = packet.readD();
		_id = packet.readD();
		_level = packet.readD();
		return true;
	}

	@Override
	public void run(GameClient client) throws Exception
	{
		final Player activeChar = client.getActiveChar();
		if ((activeChar == null) || (activeChar.getObjectId() != _objectId && !activeChar.isMyServitor(_objectId)))
			return;

		Creature target = activeChar;
		if (activeChar.getObjectId() != _objectId)
		{
			target = activeChar.getServitor(_objectId);
		}

		for (final Abnormal e : target.getAbnormalList())
		{
			if (e.getDisplayId() == _id && e.getDisplayLevel() == _level)
			{
				if (e.getSkill().getId() == 11541)
				{
					e.exit();
				}
				else
					return;
			}
		}
	}
}