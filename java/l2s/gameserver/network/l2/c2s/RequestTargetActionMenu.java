package l2s.gameserver.network.l2.c2s;
import l2s.commons.network.PacketReader;
import l2s.gameserver.model.GameObject;
import l2s.gameserver.model.GameObjectsStorage;
import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.GameClient;

/**
 * @author Bonux
 **/
public class RequestTargetActionMenu implements IClientIncomingPacket
{
	private int _targetObjectId;

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		_targetObjectId = packet.readD();
		return true;
	}

	@Override
	public void run(GameClient client)
	{
		Player activeChar = client.getActiveChar();
		if (activeChar == null)
			return;

		GameObject target = GameObjectsStorage.findObject(_targetObjectId);
		if (target == null)
		{
			activeChar.sendActionFailed();
			return;
		}

		if (!target.isTargetable(activeChar))
		{
			activeChar.sendActionFailed();
			return;
		}

		if (activeChar.getAggressionTarget() != null && activeChar.getAggressionTarget() != target)
		{
			activeChar.sendActionFailed();
			return;
		}

		activeChar.setTarget(target);
	}
}