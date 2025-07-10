package l2s.gameserver.handler.chat;

import l2s.gameserver.model.Player;

public interface IBayleeInterfaceHandler
{
	boolean useBayleeInterfaceCommand(String command, Player player, String params);

	String[] getCommandList();
}
