package l2s.gameserver.model.actor.instances.player;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;

import l2s.gameserver.model.Player;
import l2s.gameserver.model.actor.variables.PlayerVariables;
import l2s.gameserver.model.base.RestartType;

public class RestartPointSetting
{
	private Player player;
	private Map<RestartType, Integer> _setting = new HashMap<>();

	public RestartPointSetting(Player player)
	{
		this.player = player;
	}

	public void load()
	{
		String[] var = player.getVar(PlayerVariables.RESTART_POINT_SETTING_VAR, "").split(";");
		if(var != null && var.length > 0)
		{
			for(String type : var)
			{
				if(type.isEmpty())
					continue;
				String[] param = type.split("-");
				_setting.put(RestartType.valueOf(param[0]), Integer.parseInt(param[1]));
			}
		}
	}

	public void update()
	{
		StringBuilder sb = new StringBuilder();
		for(Entry<RestartType, Integer> entry : _setting.entrySet())
			sb.append(entry.getKey().name() + "-" + entry.getValue() + ";");

		player.setVar(PlayerVariables.RESTART_POINT_SETTING_VAR, StringUtils.removeEnd(sb.toString(), ";"));
	}

	public void changeButtonStatus(int nRestartPoint, int _bLocked)
	{
		_setting.put(RestartType.VALUES[nRestartPoint], _bLocked);
		update();
	}

	public Map<RestartType, Integer> getSetting()
	{
		return _setting;
	}
}
