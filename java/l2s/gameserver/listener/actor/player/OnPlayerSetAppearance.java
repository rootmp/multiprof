package l2s.gameserver.listener.actor.player;

import l2s.gameserver.listener.PlayerListener;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.items.ItemInstance;
import l2s.gameserver.templates.item.support.AppearanceStone;

public interface OnPlayerSetAppearance extends PlayerListener
{
	public void setStone(Player actor, ItemInstance targetItem, AppearanceStone appearanceStone);
}
