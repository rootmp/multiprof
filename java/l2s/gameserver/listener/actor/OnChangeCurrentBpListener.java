package l2s.gameserver.listener.actor;

import l2s.gameserver.listener.CharListener;
import l2s.gameserver.model.Creature;

/**
 * @author nexvill
 **/
public interface OnChangeCurrentBpListener extends CharListener
{
	public void onChangeCurrentBp(Creature actor, double oldBp, double newBp);
}