package l2s.gameserver.listener.actor.player;

import l2s.gameserver.listener.PlayerListener;
import l2s.gameserver.model.Player;

public interface QuestionMarkListener extends PlayerListener
{
	public void onQuestionMarkClicked(Player player, int questionMarkId);
}
