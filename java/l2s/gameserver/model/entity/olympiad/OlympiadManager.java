package l2s.gameserver.model.entity.olympiad;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import l2s.commons.util.Rnd;
import l2s.gameserver.Config;
import l2s.gameserver.model.base.ClassId;
import l2s.gameserver.utils.MultiValueIntegerMap;

import gnu.trove.iterator.TIntObjectIterator;
import gnu.trove.list.TIntList;

public class OlympiadManager implements Runnable
{
	private final Map<Integer, OlympiadGame> games = new ConcurrentHashMap<>();
	private final AtomicInteger lastGameId = new AtomicInteger(0);

	public void sleep(long time)
	{
		try
		{
			Thread.sleep(time);
		}
		catch (InterruptedException e)
		{
		}
	}

	@Override
	public void run()
	{
		if (Olympiad.isOlympiadEnd())
			return;

		while (Olympiad.inCompPeriod())
		{
			if (Olympiad.getParticipantsMap().isEmpty())
			{
				sleep(60000);
				continue;
			}

			switch (Olympiad.getCompType())
			{
				case TEAM:
				{ // Подготовка и запуск коммандных боев
					if (Olympiad.registeredParticipants.size() >= Config.NONCLASS_GAME_MIN)
						prepareBattles(CompType.TEAM, Olympiad.registeredParticipants);
					break;
				}
				case NON_CLASSED:
				{ // Подготовка и запуск внеклассовых боев
					if (Olympiad.registeredParticipants.size() >= Config.NONCLASS_GAME_MIN)
						prepareBattles(CompType.NON_CLASSED, Olympiad.registeredParticipants);
					break;
				}
				case CLASSED:
				{ // Подготовка и запуск классовых боев
					MultiValueIntegerMap participantsByClasses = new MultiValueIntegerMap();
					for (int playerObjectId : Olympiad.registeredParticipants.toArray())
					{
						participantsByClasses.put(ClassId.valueOf(Olympiad.getParticipantClass(playerObjectId)).getType2().ordinal(), playerObjectId);
					}
					for (TIntObjectIterator<TIntList> iterator = participantsByClasses.iterator(); iterator.hasNext();)
					{
						iterator.advance();
						if (iterator.value().size() >= Config.CLASS_GAME_MIN)
							prepareBattles(CompType.CLASSED, iterator.value());
					}
					break;
				}
			}
			sleep(30000);
		}

		Olympiad.registeredParticipants.clear();
		Olympiad._playersHWID.clear();

		// when comp time finish wait for all games terminated before execute the
		// cleanup code
		boolean allGamesTerminated = false;

		// wait for all games terminated
		while (!allGamesTerminated)
		{
			sleep(30000);

			if (games.isEmpty())
				break;

			allGamesTerminated = true;
			for (OlympiadGame game : games.values())
				if (game.getTask() != null && !game.getTask().isTerminated())
					allGamesTerminated = false;
		}

		games.clear();
	}

	private void prepareBattles(CompType matchType, TIntList registered)
	{
		int stadiumsCount = Config.OLYMPIAD_STADIAS_COUNT;
		while (games.size() < stadiumsCount)
		{
			OlympiadParticipiantData[][] nextOpponents = nextOpponents(matchType, registered);
			if (nextOpponents == null)
				break;

			OlympiadGame game = OlympiadGame.makeGame(lastGameId.incrementAndGet(), matchType, nextOpponents[0], nextOpponents[1]);
			if (game != null)
			{
				game.sheduleTask(new OlympiadGameTask(game, BattleStatus.Begining, 0, 1));
				games.put(game.getId(), game);
			}
		}
	}

	public boolean removeGame(int index)
	{
		return games.remove(index) != null;
	}

	public OlympiadGame getGame(int index)
	{
		return games.get(index);
	}

	public Collection<OlympiadGame> getGames()
	{
		return games.values();
	}

	private OlympiadParticipiantData[][] nextOpponents(CompType matchType, TIntList registered)
	{
		int count = matchType == CompType.TEAM ? 3 : 1;
		if ((count * 2) > registered.size())
			return null;
		OlympiadParticipiantData[][] opponents = new OlympiadParticipiantData[2][];
		for (int j = 0; j < 2; j++)
		{
			opponents[j] = new OlympiadParticipiantData[count];
			for (int i = 0; i < count; i++)
			{
				int[] regArr = registered.toArray(new int[registered.size()]);
				int objectId = regArr.length == 0 ? 0 : Rnd.get(regArr);
				if (objectId == 0)
					return null;

				OlympiadParticipiantData participiantData = Olympiad.getParticipantInfo(objectId);
				if (participiantData == null)
					return null;

				opponents[j][i] = participiantData;
				removeOpponent(objectId);
			}
		}
		return opponents;
	}

	private void removeOpponent(Integer noble)
	{
		Olympiad.registeredParticipants.remove(noble);
		Olympiad._playersHWID.remove(noble); // obj id? remove by key
	}
}