package l2s.gameserver.security;

import java.util.Collection;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import l2s.gameserver.GameServer;
import l2s.gameserver.model.HaveHwid;
import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.GameClient;
import l2s.gameserver.network.l2.components.hwid.DefaultHwidHolder;
import l2s.gameserver.network.l2.components.hwid.EmptyHwidHolder;
import l2s.gameserver.network.l2.components.hwid.HwidHolder;

public final class HwidUtils
{
	protected final Logger _log = LoggerFactory.getLogger(HwidUtils.class);

	private static HwidUtils _instance;

	public static HwidUtils getInstance()
	{
		if(_instance == null)
			_instance = new HwidUtils();
		return _instance;
	}

	public final HwidHolder createHwidHolder(final String hwid)
	{
		return GameServer.DEVELOP ? new EmptyHwidHolder(hwid) : new DefaultHwidHolder(hwid);
	}

	public final boolean isSameHWID(final Player player1, final Player player2)
	{
		return player1 == player2 ? true : isSameHWID(player1.getHwidHolder(), player2.getHwidHolder());
	}

	public final boolean isSameHWID(final GameClient client, final String hwid2)
	{
		return isSameHWID(client.getHwidString(), hwid2);
	}

	public final boolean isSameHWID(final GameClient client1, final GameClient client2)
	{
		return isSameHWID(client1.getHwidHolder(), client2.getHwidHolder());
	}

	public final boolean isSameHWID(final String hwid1, final String hwid2)
	{
		if(GameServer.DEVELOP)
			return false;

		if(hwid1 == null || hwid2 == null)
			return true;

		final DefaultHwidHolder hwidHolder11 = new DefaultHwidHolder(hwid1);
		final DefaultHwidHolder hwidHolder12 = new DefaultHwidHolder(hwid2);
		return hwidHolder11 == hwidHolder12;
	}

	public final boolean isSameHWID(final HaveHwid haveHwid1, final HaveHwid haveHwid2)
	{
		return this.isSameHWID(haveHwid1.getHwidHolder(), haveHwid2.getHwidHolder());
	}

	public final boolean isSameHWID(final HwidHolder hwidHolder1, final HwidHolder hwidHolder2)
	{
		return !GameServer.DEVELOP && (hwidHolder1 == null || hwidHolder2 == null || hwidHolder1 == hwidHolder2);
	}

	public final Collection<Player> filterSameHwids(final Collection<Player> players)
	{
		if(GameServer.DEVELOP)
			return players;
		players.forEach(player -> {
			if(player.getHwidHolder() == null)
				_log.error(player + " don't have hwid for some reason.");

		});
		return players.stream().filter(player -> player.getHwidHolder()
				!= null).filter(distinctByKey(Player::getHwidHolder)).collect(Collectors.toList());
	}

	public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor)
	{
		Set<Object> seen = ConcurrentHashMap.newKeySet();
		return t -> seen.add(keyExtractor.apply(t));
	}

	public final boolean haveSameHwids(Collection<Player> players)
	{
		if(GameServer.DEVELOP)
			return false;
		players.forEach(player -> {
			if(player.getHwidHolder() == null)
				_log.error(player + " don't have hwid for some reason.");

		});

		long distinctCount = players.stream().filter(player -> player.getHwidHolder() != null).filter(distinctByKey(Player::getHwidHolder)).count();

		return distinctCount < players.size();
	}

}
