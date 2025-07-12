package l2s.gameserver.model.actor.variables;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import l2s.gameserver.dao.AccountVariablesDAO;
import l2s.gameserver.templates.VariableContainer;

public class AccountVariables
{
	private final Map<String, VariableContainer> _accountVariables = new ConcurrentHashMap<String, VariableContainer>();
	private String _accountName;

	public void restoreVariables(String accountName)
	{
		_accountName = accountName;
		
		List<VariableContainer> variables = AccountVariablesDAO.getInstance().restore(_accountName);
		for(VariableContainer var : variables)
			_accountVariables.put(var.getName(), var);
	}

	public Collection<VariableContainer> getVariables()
	{
		return _accountVariables.values();
	}

	public boolean setVar(String name, Object value)
	{
		return setVar(name, value, -1);
	}

	public void setVar(String name, List<Integer> list)
	{
		if ((list == null) || list.isEmpty())
			return;

		final StringBuilder sb = new StringBuilder();
		for (int element : list)
		{
			sb.append(element);
			sb.append(",");
		}
		sb.deleteCharAt(sb.length() - 1);
		
		setVar(name, sb.toString());
	}
	
	public boolean setVar(String name, Object value, long expirationTime)
	{
		VariableContainer var = new VariableContainer(name, String.valueOf(value), expirationTime);
		if(AccountVariablesDAO.getInstance().insert(_accountName, var))
		{
			_accountVariables.put(name, var);
			return true;
		}
		return false;
	}

	public boolean unsetVar2(String name)
	{
		if(name == null || name.isEmpty())
			return false;
		for(String key : _accountVariables.keySet())
		{
			if(key.indexOf(name) != -1 && AccountVariablesDAO.getInstance().delete2(_accountName, name+"%"))
				return _accountVariables.remove(key) != null;
		}
		return false;
	}
	
	public boolean unsetVar(String name)
	{
		if(name == null || name.isEmpty())
			return false;

		if(_accountVariables.containsKey(name) && AccountVariablesDAO.getInstance().delete(_accountName, name))
			return _accountVariables.remove(name) != null;

		return false;
	}

	public List<Integer> getVarIntegerList(String name)
	{
		final String val = getVar(name);
		final List<Integer> result;
		if (val != null)
		{
			final String[] splitVal = val.split(",");
			result = new ArrayList<>(splitVal.length + 1);
			for (String split : splitVal)
				result.add(Integer.parseInt(split));
		}
		else
			result = new ArrayList<>(1);
		return result;
	}
	
	public String getVar(String name)
	{
		return getVar(name, null);
	}


	public VariableContainer getVarObject(String name)
	{
		VariableContainer var = _accountVariables.get(name);
		if(var != null && !var.isExpired())
			return var;

		return null;
	}
	
	public String getVar(String name, String defaultValue)
	{
		VariableContainer var = _accountVariables.get(name);
		if(var != null && !var.isExpired())
			return var.getValue();

		return defaultValue;
	}

	public long getVarExpireTime(String name)
	{
		VariableContainer var = _accountVariables.get(name);
		if(var != null)
			return var.getExpireTime();
		return 0;
	}

	public int getVarInt(String name)
	{
		return getVarInt(name, 0);
	}

	public int getVarInt(String name, int defaultValue)
	{
		String var = getVar(name);
		if(var != null)
			return Integer.parseInt(var);

		return defaultValue;
	}

	public long getVarLong(String name)
	{
		return getVarLong(name, 0L);
	}

	public long getVarLong(String name, long defaultValue)
	{
		String var = getVar(name);
		if(var != null)
			return Long.parseLong(var);

		return defaultValue;
	}

	public double getVarDouble(String name)
	{
		return getVarDouble(name, 0.);
	}

	public double getVarDouble(String name, double defaultValue)
	{
		String var = getVar(name);
		if(var != null)
			return Double.parseDouble(var);

		return defaultValue;
	}

	public boolean getVarBoolean(String name)
	{
		return getVarBoolean(name, false);
	}

	public boolean getVarBoolean(String name, boolean defaultValue)
	{
		String var = getVar(name);
		if(var != null)
			return !(var.equals("0") || var.equalsIgnoreCase("false"));

		return defaultValue;
	}
}