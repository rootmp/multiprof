package handler.bayleeinterface;

import l2s.gameserver.handler.chat.BayleeInterfaceHandler;
import l2s.gameserver.handler.chat.IBayleeInterfaceHandler;
import l2s.gameserver.listener.script.OnInitScriptListener;

/**
 * @author Belly
 **/
public abstract class BayleeInterfaceLoader implements IBayleeInterfaceHandler, OnInitScriptListener
{
	@Override
	public void onInit()
	{
		BayleeInterfaceHandler.getInstance().registerCommand(this);
	}
}