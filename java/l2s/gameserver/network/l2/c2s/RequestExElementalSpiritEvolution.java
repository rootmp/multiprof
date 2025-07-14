package l2s.gameserver.network.l2.c2s;

import l2s.commons.network.PacketReader;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.actor.instances.player.Elemental;
import l2s.gameserver.network.l2.GameClient;
import l2s.gameserver.network.l2.components.SystemMsg;
import l2s.gameserver.network.l2.s2c.ExElementalSpiritEvolution;
import l2s.gameserver.network.l2.s2c.ExElementalSpiritEvolutionInfo;
import l2s.gameserver.templates.item.data.ItemData;
import l2s.gameserver.utils.ItemFunctions;

/**
 * @author Bonux
 **/
public class RequestExElementalSpiritEvolution implements IClientIncomingPacket
{
	private int _elementId;

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		_elementId = packet.readC();
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
			activeChar.sendPacket(new ExElementalSpiritEvolution(activeChar, false, _elementId));
			activeChar.sendPacket(new ExElementalSpiritEvolutionInfo(activeChar, _elementId));
			activeChar.sendActionFailed();
			return;
		}

		if(activeChar.isInCombat())
		{
			activeChar.sendPacket(new ExElementalSpiritEvolution(activeChar, false, _elementId));
			activeChar.sendPacket(new ExElementalSpiritEvolutionInfo(activeChar, _elementId));
			activeChar.sendPacket(SystemMsg.UNABLE_TO_EVOLVE_DURING_BATTLE);
			return;
		}

		if(elemental.getLevel() < 10 || elemental.getExp() < elemental.getMaxExp())
		{
			activeChar.sendPacket(new ExElementalSpiritEvolution(activeChar, false, _elementId));
			activeChar.sendPacket(new ExElementalSpiritEvolutionInfo(activeChar, _elementId));
			activeChar.sendPacket(SystemMsg.THIS_SPIRIT_CANNOT_EVOLVE);
			return;
		}

		int nextEvolutionLevel = elemental.getEvolutionLevel() + 1;
		if(elemental.getTemplate().getEvolution(nextEvolutionLevel) == null)
		{
			activeChar.sendPacket(new ExElementalSpiritEvolution(activeChar, false, _elementId));
			activeChar.sendPacket(new ExElementalSpiritEvolutionInfo(activeChar, _elementId));
			activeChar.sendPacket(SystemMsg.THIS_SPIRIT_CANNOT_EVOLVE);
			return;
		}

		activeChar.getInventory().writeLock();
		try
		{
			for(ItemData item : elemental.getEvolution().getRiseLevelCost())
			{
				if(!ItemFunctions.haveItem(activeChar, item.getId(), item.getCount()))
				{
					activeChar.sendPacket(new ExElementalSpiritEvolution(activeChar, false, _elementId));
					activeChar.sendPacket(new ExElementalSpiritEvolutionInfo(activeChar, _elementId));
					activeChar.sendPacket(SystemMsg.THIS_SPIRIT_CANNOT_EVOLVE);
					return;
				}
			}

			if(!elemental.setEvolutionLevel(nextEvolutionLevel))
			{
				activeChar.sendPacket(new ExElementalSpiritEvolution(activeChar, false, _elementId));
				activeChar.sendPacket(new ExElementalSpiritEvolutionInfo(activeChar, _elementId));
				activeChar.sendPacket(SystemMsg.THIS_SPIRIT_CANNOT_EVOLVE);
				return;
			}

			for(ItemData item : elemental.getEvolution().getRiseLevelCost())
				ItemFunctions.deleteItem(activeChar, item.getId(), item.getCount(), true);
		}
		finally
		{
			activeChar.getInventory().writeUnlock();
		}

		activeChar.sendPacket(new ExElementalSpiritEvolution(activeChar, true, _elementId));
		activeChar.sendPacket(new ExElementalSpiritEvolutionInfo(activeChar, _elementId));
	}
}