package handler.dailymissions;

import l2s.gameserver.model.Player;
import l2s.gameserver.model.actor.instances.player.DailyMission;
import l2s.gameserver.model.actor.instances.player.Elemental;
import l2s.gameserver.model.base.ElementalElement;
import l2s.gameserver.templates.dailymissions.DailyMissionStatus;

public class WindElementalFiveEvolution extends ScriptDailyMissionHandler
{
	@Override
	public DailyMissionStatus getStatus(Player player, DailyMission mission)
	{
		if (mission.isCompleted())
			return DailyMissionStatus.COMPLETED;
		Elemental elemental = player.getElementalList().get(ElementalElement.WIND);
		if (elemental != null && elemental.getEvolutionLevel() >= 5)
			return DailyMissionStatus.AVAILABLE;
		return DailyMissionStatus.NOT_AVAILABLE;
	}

	@Override
	public boolean isReusable()
	{
		return false;
	}
}
