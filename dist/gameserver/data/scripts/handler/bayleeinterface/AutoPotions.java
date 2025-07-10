package handler.bayleeinterface;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import l2s.gameserver.model.Player;
import l2s.gameserver.model.actor.variables.PlayerVariables;

/**
 * @author Belly
 **/
public class AutoPotions extends BayleeInterfaceLoader
{
	private static final Logger _log = LoggerFactory.getLogger(AutoPotions.class);
	private static final String[] _commandList = new String[]
	{
		"AUTOPOTION"
	};

	@Override
	public boolean useBayleeInterfaceCommand(String command, Player player, String params)
	{
		if (player == null)
		{
			return false;
		}

		if (command.equalsIgnoreCase(_commandList[0]))
		{
			try
			{
				final String[] pot = params.trim().split(";");
				if (pot.length != 3)
				{
					_log.warn(player.getName() + " sent Auto HP less than 3");
					return false;
				}
				final int mp = Integer.parseInt(pot[0]);
				final int cp = Integer.parseInt(pot[1]);
				final int hp = Integer.parseInt(pot[2]);
				if (player.getVarInt(PlayerVariables.AUTO_MP_VAR) != mp && mp > 0)
				{
					player.setVar(PlayerVariables.AUTO_MP_VAR, mp);
					player.sendMessage("Automatic use of MP potions set at " + mp + "%.");
				}
				if (player.getVarInt(PlayerVariables.AUTO_CP_VAR) != cp && cp > 0)
				{
					player.setVar(PlayerVariables.AUTO_CP_VAR, cp);
					player.sendMessage("Automatic use of CP potions set at " + cp + "%.");
				}
				if (player.getVarInt(PlayerVariables.AUTO_HP_VAR) != hp && hp > 0)
				{
					player.setVar(PlayerVariables.AUTO_HP_VAR, hp);
					player.sendMessage("Automatic use of HP potions set at " + hp + "%.");
				}
			}
			catch (final Exception e)
			{
				return false;
			}
		}
		return true;
	}

	@Override
	public String[] getCommandList()
	{
		return _commandList;
	}
}