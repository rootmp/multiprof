package l2s.gameserver.handler.chat;

import java.util.HashMap;
import java.util.Map;

import l2s.commons.data.xml.AbstractHolder;

public class BayleeInterfaceHandler extends AbstractHolder
{
	private static final BayleeInterfaceHandler _instance = new BayleeInterfaceHandler();
	private final Map<String, IBayleeInterfaceHandler> _datatable = new HashMap<String, IBayleeInterfaceHandler>();

	private BayleeInterfaceHandler()
	{
		//
	}

	public static BayleeInterfaceHandler getInstance()
	{
		return _instance;
	}

	public void registerCommand(IBayleeInterfaceHandler handler)
	{
		final String[] ids = handler.getCommandList();
		if(ids == null)
			return;
		for(final String element : ids)
		{
			_datatable.put(element, handler);
		}
	}

	public IBayleeInterfaceHandler getBayleeInterfaceHandler(String cmd)
	{
		String command = cmd;
		if(cmd.indexOf(" ") != -1)
		{
			command = cmd.substring(0, cmd.indexOf(" "));
		}
		return _datatable.get(command);
	}

	@Override
	public int size()
	{
		return _datatable.size();
	}

	@Override
	public void clear()
	{
		_datatable.clear();
	}
}
