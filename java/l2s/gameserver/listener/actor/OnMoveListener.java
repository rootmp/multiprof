package l2s.gameserver.listener.actor;

import l2s.gameserver.geometry.Location;
import l2s.gameserver.listener.CharListener;
import l2s.gameserver.model.Creature;

public interface OnMoveListener extends CharListener
{
	public void onMove(Creature actor, Location loc);
}
