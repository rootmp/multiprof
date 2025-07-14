package l2s.gameserver.network.l2.c2s;

import l2s.commons.network.PacketReader;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.actor.instances.player.Elemental;
import l2s.gameserver.network.l2.GameClient;
import l2s.gameserver.network.l2.s2c.ExElementalSpiritSetTalent;

/**
 * @author Bonux
 **/
public class RequestExElementalSpiritSetTalent implements IClientIncomingPacket
{
	private int _elementId;
	private int[][] _talents;

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		_elementId = packet.readC();
		_talents = new int[packet.readC()][];
		for(int i = 0; i < _talents.length; i++)
		{
			_talents[i] = new int[] {
					packet.readC(),
					packet.readC()
			};
		}
		return true;
	}

	@Override
	public void run(GameClient client)
	{
		Player activeChar = client.getActiveChar();
		if(activeChar == null)
			return;

		Elemental elemental = activeChar.getElementalList().get(_elementId);
		if(elemental == null)
		{
			activeChar.sendPacket(new ExElementalSpiritSetTalent(activeChar, false, _elementId));
			activeChar.sendActionFailed();
			return;
		}

		boolean update = false;
		for(int[] talent : _talents)
		{
			int currentPoints;
			int newPoints;
			switch(talent[0])
			{
				case 1:
					currentPoints = elemental.getAttackPoints();
					newPoints = Math.min(currentPoints + Math.min(talent[1], elemental.getAvailablePoints()), elemental.getEvolution().getMaxAttackPoints());
					if(currentPoints < newPoints)
					{
						elemental.setAttackPoints(newPoints);
						update = true;
					}
					break;
				case 2:
					currentPoints = elemental.getDefencePoints();
					newPoints = Math.min(currentPoints + Math.min(talent[1], elemental.getAvailablePoints()), elemental.getEvolution().getMaxDefencePoints());
					if(currentPoints < newPoints)
					{
						elemental.setDefencePoints(newPoints);
						update = true;
					}
					break;
				case 3:
					currentPoints = elemental.getCritRatePoints();
					newPoints = Math.min(currentPoints + Math.min(talent[1], elemental.getAvailablePoints()), elemental.getEvolution().getMaxCritRatePoints());
					if(currentPoints < newPoints)
					{
						elemental.setCritRatePoints(newPoints);
						update = true;
					}
					break;
				case 4:
					currentPoints = elemental.getCritAttackPoints();
					newPoints = Math.min(currentPoints
							+ Math.min(talent[1], elemental.getAvailablePoints()), elemental.getEvolution().getMaxCritAttackPoints());
					if(currentPoints < newPoints)
					{
						elemental.setCritAttackPoints(newPoints);
						update = true;
					}
					break;
			}
		}

		if(!update)
		{
			activeChar.sendPacket(new ExElementalSpiritSetTalent(activeChar, false, _elementId));
			activeChar.sendActionFailed();
			return;
		}

		activeChar.sendPacket(new ExElementalSpiritSetTalent(activeChar, true, _elementId));
	}
}