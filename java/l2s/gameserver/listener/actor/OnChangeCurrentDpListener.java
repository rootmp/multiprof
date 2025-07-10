package l2s.gameserver.listener.actor;

import l2s.gameserver.listener.CharListener;
import l2s.gameserver.model.Creature;

/**
 * @author nexvill
 **/
public interface OnChangeCurrentDpListener extends CharListener
{
	public void onChangeCurrentDp(Creature actor, int oldDp, int newDp);
}