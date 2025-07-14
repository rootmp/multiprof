package l2s.gameserver.listener.actor.player;

import l2s.gameserver.listener.PlayerListener;
import l2s.gameserver.model.Player;

public interface OnPlayerElementalSetEvolutionLevel extends PlayerListener
{
	public void setEvolutionLevel(Player player, int oldEvolutionLevel, int nextEvolutionLevel);
}
