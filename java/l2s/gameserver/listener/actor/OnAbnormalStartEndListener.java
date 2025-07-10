package l2s.gameserver.listener.actor;

import l2s.gameserver.listener.CharListener;
import l2s.gameserver.model.Creature;
import l2s.gameserver.model.actor.instances.creature.Abnormal;

/**
 * @author nexvill
 **/
public interface OnAbnormalStartEndListener extends CharListener
{
	public void onAbnormalStart(Creature actor, Abnormal a);

	public void onAbnormalEnd(Creature actor, Abnormal a);
}