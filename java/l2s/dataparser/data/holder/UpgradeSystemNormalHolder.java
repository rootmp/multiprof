package l2s.dataparser.data.holder;

import java.util.Map;

import l2s.commons.data.xml.AbstractHolder;
import l2s.dataparser.data.annotations.Element;
import l2s.dataparser.data.holder.upgrade_system.NormalUpgradeData;

public class UpgradeSystemNormalHolder extends AbstractHolder
{
	@Element(start = "upgradesystem_begin", end = "upgradesystem_end", keyField = "upgrade_id")
	private Map<Integer, NormalUpgradeData> upgrade_system_normal_data;

	private static UpgradeSystemNormalHolder ourInstance = new UpgradeSystemNormalHolder();

	public static UpgradeSystemNormalHolder getInstance()
	{
		return ourInstance;
	}

	@Override
	public int size()
	{
		return upgrade_system_normal_data.size();
	}

	@Override
	public void clear()
	{

	}

	public NormalUpgradeData getNormalUpgradeData(int upgradeId)
	{
		return upgrade_system_normal_data.get(upgradeId);
	}
}