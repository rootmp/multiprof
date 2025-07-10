package l2s.gameserver.listener.actions;

import l2s.gameserver.geometry.ILocation;
import l2s.gameserver.model.Creature;

@FunctionalInterface
public interface OnArrivedAction
{
	void onArrived(Creature actor, ILocation loc, boolean toTarget);
}
