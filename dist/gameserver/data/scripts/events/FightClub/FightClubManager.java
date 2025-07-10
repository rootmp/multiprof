/*
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
package events.FightClub;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import l2s.gameserver.Announcements;
import l2s.gameserver.Config;
import l2s.gameserver.geometry.Location;
import l2s.gameserver.instancemanager.ReflectionManager;
import l2s.gameserver.listener.actor.player.OnAnswerListener;
import l2s.gameserver.listener.actor.player.OnPlayerExitListener;
import l2s.gameserver.listener.actor.player.OnTeleportListener;
import l2s.gameserver.model.GameObjectsStorage;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.actor.listener.CharListenerList;
import l2s.gameserver.model.base.TeamType;
import l2s.gameserver.model.entity.Hero;
import l2s.gameserver.model.entity.Reflection;
import l2s.gameserver.model.entity.olympiad.Olympiad;
import l2s.gameserver.network.l2.components.ChatType;
import l2s.gameserver.network.l2.components.CustomMessage;
import l2s.gameserver.network.l2.components.SystemMsg;
import l2s.gameserver.network.l2.s2c.ConfirmDlgPacket;
import l2s.gameserver.network.l2.s2c.ExShowScreenMessage;
import l2s.gameserver.network.l2.s2c.ExShowScreenMessage.ScreenMessageAlign;
import l2s.gameserver.network.l2.s2c.RevivePacket;
import l2s.gameserver.scripts.ScriptFile;
import l2s.gameserver.skills.SkillEntry;
import l2s.gameserver.skills.enums.AbnormalEffect;
import l2s.gameserver.utils.Functions;
import l2s.gameserver.utils.ItemFunctions;
import npc.model.events.FightClubManagerInstance.Rate;

/**
 * @author Hl4p3x for L2Scripts Essence
 */
public class FightClubManager extends Functions implements ScriptFile, OnPlayerExitListener, OnTeleportListener
{
	private static Logger _log = LoggerFactory.getLogger(FightClubManager.class);

	private static Map<Integer, Rate> _ratesMap;
	private static List<FightClubArena> _fights;
	private static ReflectionManager _reflectionManager;
	protected static List<Integer> _inBattle;
	private static Map<Integer, Location> _restoreCoord;
	private static List<Integer> _inList;
	private static StringBuilder _itemsList;
	private static Map<String, Integer> _allowedItems;
	private static Location _player1loc;
	private static Location _player2loc;

	@Override
	public void onLoad()
	{
		if (Config.FIGHT_CLUB_ENABLED)
		{
			return;
		}

		CharListenerList.addGlobal(this);

		_ratesMap = new HashMap<Integer, Rate>();
		_fights = new ArrayList<FightClubArena>();
		_restoreCoord = new HashMap<Integer, Location>();
		_inBattle = new ArrayList<Integer>();
		_inList = new ArrayList<Integer>();
		_reflectionManager = ReflectionManager.getInstance();
		_itemsList = new StringBuilder();
		_allowedItems = new HashMap<String, Integer>();
		_player1loc = new Location(-85768, -52456, -11524);
		_player2loc = new Location(-82648, -51368, -11529);

		for (int i = 0; i < Config.ALLOWED_RATE_ITEMS.length; i++)
		{
			String itemName = ItemFunctions.createItem(Integer.parseInt(Config.ALLOWED_RATE_ITEMS[i])).getTemplate().getName();
			_itemsList.append(itemName).append(";");
			_allowedItems.put(itemName, Integer.parseInt(Config.ALLOWED_RATE_ITEMS[i]));
		}

		_log.info("Loaded Event: Fight Club");

	}

	@Override
	public void onTeleport(Player player, int x, int y, int z, Reflection reflection)
	{
		if (player.getTeam() != TeamType.NONE && _inBattle.contains(player.getObjectId()))
		{
			removePlayer(player);
		}
	}

	/**
	 * Removes all the information about the player @ Param player - a reference to
	 * the deleted player
	 */
	private static void removePlayer(Player player)
	{
		if (player != null)
		{
			player.setTeam(TeamType.NONE);
			if (_inBattle.contains(player.getObjectId()))
			{
				_inBattle.remove(player.getObjectId());
			}
			if (_inList.contains(player.getObjectId()))
			{
				_ratesMap.remove(player.getObjectId());
				_inList.remove(player.getObjectId());
			}
			if (_restoreCoord.containsKey(player.getObjectId()))
			{
				_restoreCoord.remove(player.getObjectId());
			}
		}
	}

	public static Location getRestoreLocation(Player player)
	{
		return _restoreCoord.get(player.getObjectId());
	}

	public static Player getPlayer(int playerStoredI)
	{
		return GameObjectsStorage.getPlayer(playerStoredI);
	}

	@Override
	public void onPlayerExit(Player player)
	{
		removePlayer(player);
	}

	@Override
	public void onReload()
	{
		_fights.clear();
		_ratesMap.clear();
		_inBattle.clear();
		_inList.clear();
		onLoad();
	}

	@Override
	public void onShutdown()
	{
		if (Config.FIGHT_CLUB_ENABLED)
		{
			return;
		}

		_fights.clear();
		_ratesMap.clear();
		_inBattle.clear();
		_inList.clear();
	}

	public static String addApplication(Player player, String item, int count)
	{
		if (!checkPlayer(player, true))
		{
			return null;
		}
		if (isRegistered(player))
		{
			return "reg";
		}
		if (ItemFunctions.getItemCount(player, _allowedItems.get(item)) < count)
		{
			show(new CustomMessage("scripts.events.fightclub.CancelledItems"), player);
			return "NoItems";
		}

		final Rate rate = new Rate(player, _allowedItems.get(item), count);
		_ratesMap.put(player.getObjectId(), rate);
		_inList.add(0, player.getObjectId());
		if (Config.FIGHT_CLUB_ANNOUNCE_RATE)
		{
			final String[] args =
			{
				player.getName(),
				String.valueOf(player.getLevel()),
				String.valueOf(rate.getItemCount()),
				item
			};

			Announcements.getInstance().announceByCustomMessage("scripts.events.fightclub.Announce", args, ChatType.MPCC_ROOM);
		}
		return "OK";
	}

	/**
	 * Sends ConfirmDlgPacket @ Param requested - a player, put a request. He <b> </
	 * b> queried @ Param requester - players selected from the list of opponents.
	 * <b> from him </ b> queried
	 */
	public static boolean requestConfirmation(Player requested, Player requester)
	{
		if (!checkPlayer(requester, true))
		{
			return false;
		}

		if (requested.getLevel() - requester.getLevel() > Config.MAXIMUM_LEVEL_DIFFERENCE || requester.getLevel() - requested.getLevel() > Config.MAXIMUM_LEVEL_DIFFERENCE)
		{
			show((new CustomMessage("scripts.events.fightclub.CancelledLevel")).addNumber(Config.MINIMUM_LEVEL_TO_PARRICIPATION).addNumber(Config.MAXIMUM_LEVEL_TO_PARRICIPATION).addNumber(Config.MAXIMUM_LEVEL_DIFFERENCE), requester);
			return false;
		}

		String msg = (new CustomMessage("scripts.events.fightclub.AskPlayer")).addString(requester.getName()).addNumber(requester.getLevel()).toString(requested);
		requested.ask(new ConfirmDlgPacket(SystemMsg.S1, 30000).addString(msg), new OnAnswerListener()
		{
			public void sayYes()
			{
				FightClubManager.doStart(requested, requester);
			}

			public void sayNo()
			{
			}
		});
		return true;
	}

	/**
	 * Test players for create an arena for them @ Param requested - a player, put a
	 * request. He <b> </ b> queried @ Param requester - players selected from the
	 * list of opponents. <b> from him </ b> queried
	 */
	public static void doStart(Player requested, Player requester)
	{
		final int itemId = _ratesMap.get(requested.getObjectId()).getItemId();
		final int itemCount = _ratesMap.get(requested.getObjectId()).getItemCount();
		if (!checkPrepare(requested, requester, itemId, itemCount))
		{
			return;
		}
		if (!checkPlayer(requested, false))
		{
			return;
		}
		if (!checkPlayer(requester, true))
		{
			return;
		}

		_inList.remove(requested.getObjectId());
		_ratesMap.remove(requested.getObjectId());
		_restoreCoord.put(requested.getObjectId(), new Location(requested.getX(), requested.getY(), requested.getZ()));
		_restoreCoord.put(requester.getObjectId(), new Location(requester.getX(), requester.getY(), requester.getZ()));
		ItemFunctions.deleteItem(requested, itemId, itemCount);
		ItemFunctions.deleteItem(requester, itemId, itemCount);
		createBattle(requested, requester, itemId, itemCount);
	}

	private static boolean checkPrepare(Player requested, Player requester, int itemId, int itemCount)
	{

		if (ItemFunctions.getItemCount(requested, itemId) < itemCount)
		{
			show(new CustomMessage("scripts.events.fightclub.CancelledItems"), requested);
			show(new CustomMessage("scripts.events.fightclub.CancelledOpponent"), requester);
			return false;
		}

		if (ItemFunctions.getItemCount(requester, itemId) < itemCount)
		{
			show(new CustomMessage("scripts.events.fightclub.CancelledItems"), requester);
			return false;
		}

		if (_inBattle.contains(requested.getObjectId()))
		{
			show(new CustomMessage("scripts.events.fightclub.CancelledOpponent"), requested);
			return false;
		}

		return true;
	}

	private static void createBattle(Player player1, Player player2, int itemId, int itemCount)
	{
		_inBattle.add(player1.getObjectId());
		_inBattle.add(player2.getObjectId());

		final Reflection _reflection = new Reflection();
		_reflectionManager.add(_reflection);

		final FightClubArena _arena = new FightClubArena(player1, player2, itemId, itemCount, _reflection);
		_fights.add(_arena);
	}

	public static void deleteArena(FightClubArena arena)
	{
		removePlayer(arena.getPlayer1());
		removePlayer(arena.getPlayer2());
		arena.getReflection().collapse();
		_fights.remove(arena);
	}

	/**
	 * @param player the player
	 * @param first
	 * @return
	 */
	public static boolean checkPlayer(Player player, boolean first)
	{
		if (first && player.isDead())
		{
			show(new CustomMessage("scripts.events.fightclub.CancelledDead"), player);
			return false;
		}

		if (first && player.getTeam() != TeamType.NONE)
		{
			show(new CustomMessage("scripts.events.fightclub.CancelledOtherEvent"), player);
			return false;
		}

		if (player.getLevel() < Config.MINIMUM_LEVEL_TO_PARRICIPATION || player.getLevel() > Config.MAXIMUM_LEVEL_TO_PARRICIPATION)
		{
			show(new CustomMessage("scripts.events.fightclub.CancelledLevel"), player);
			return false;
		}

		if (player.isMounted())
		{
			show(new CustomMessage("scripts.events.fightclub.Cancelled"), player);
			return false;
		}

		if (player.isInDuel())
		{
			show(new CustomMessage("scripts.events.fightclub.CancelledDuel"), player);
			return false;
		}

		if (player.getOlympiadGame() != null || first && Olympiad.isRegistered(player))
		{
			show(new CustomMessage("scripts.events.fightclub.CancelledOlympiad"), player);
			return false;
		}

		if (player.isInParty() && player.getParty().isInReflection())
		{
			show(new CustomMessage("scripts.events.fightclub.CancelledOtherEvent"), player);
			return false;
		}

		if (player.isInObserverMode())
		{
			show(new CustomMessage("scripts.event.fightclub.CancelledObserver"), player);
			return false;
		}

		if (player.isTeleporting())
		{
			show(new CustomMessage("scripts.events.fightclub.CancelledTeleport"), player);
			return false;
		}
		return true;
	}

	/**
	 * Private method. That come back true, if a player has registered bid @ Param
	 * player - a reference to the audited Player @ Return - true, if registered
	 */
	private static boolean isRegistered(Player player)
	{
		if (_inList.contains(player.getObjectId()))
		{
			return true;
		}
		return false;
	}

	/**
	 * Gets the class {@ link Rate}, containing your "applications" <b> Method for
	 * use in FightClubInstanceManager! </ b> @ Param index @ Return an object that
	 * contains application
	 */
	public static Rate getRateByIndex(int index)
	{
		return _ratesMap.get(_inList.get(index));
	}

	/**
	 * Gets the class {@ link Rate}, containing your "applications" <b> Method for
	 * use in FightClubInstanceManager! </ b> @ Param index @ Return an object that
	 * contains application
	 */
	public static Rate getRateByStoredId(long storedId)
	{
		return _ratesMap.get(storedId);
	}

	/**
	 * Возвращает через ; имена предметов, разрешенных в качестве ставки. <b> Метод
	 * для использования в FightClubInstanceManager! </b>
	 * 
	 * @return список предметов через ";"
	 */
	public static String getItemsList()
	{
		return _itemsList.toString();
	}

	/**
	 * <b> Метод для использования в FightClubInstanceManager! </b>
	 * 
	 * @param playerObject - ссылка на игрока
	 * @return true, если игрок зарегистрировал ставку
	 */
	public static boolean isRegistered(Object playerObject)
	{
		if (_ratesMap.containsKey(((Player) playerObject).getObjectId()))
		{
			return true;
		}
		return false;
	}

	/**
	 * Removes the registration of a player in the list via the method {@ link #
	 * removePlayer (Player)} <b> Method for use in FightClubInstanceManager! </
	 * b> @ Param playerObject link to player
	 */
	public static void deleteRegistration(Player player)
	{
		removePlayer(player);
	}

	/**
	 * Возвращает количеств игроков, сделавших свои ставки <b> Метод для
	 * использования в FightClubInstanceManager! </b>
	 * 
	 * @return - количество игроков, сделавших ставки
	 */
	public static int getRatesCount()
	{
		return _inList.size();
	}

	/**
	 * Ставит в root игрока
	 * 
	 * @param player
	 */
	private static void rootPlayer(Player player)
	{
		player.startRooted();
		player.startAbnormalEffect(AbnormalEffect.ROOT);
		if (player.getAnyServitor() != null)
		{
			player.getAnyServitor().startRooted();
			player.getAnyServitor().startAbnormalEffect(AbnormalEffect.ROOT);
		}
	}

	/**
	 * Снимает root с игрока
	 * 
	 * @param player
	 */
	private static void unrootPlayers(Player player)
	{
		if (player.isImmobilized())
		{
			player.stopRooted();
			player.stopAbnormalEffect(AbnormalEffect.ROOT);
			if (player.getAnyServitor() != null)
			{
				player.getAnyServitor().stopRooted();
				player.getAnyServitor().stopAbnormalEffect(AbnormalEffect.ROOT);
			}
		}
	}

	/**
	 * Телепортирует игроков на сохраненные координаты
	 * 
	 * @param player1
	 * @param player2
	 */
	@SuppressWarnings("static-access")
	public static void teleportPlayersBack(Player player1, Player player2, Object args)
	{
		if (Config.REMOVE_CLAN_SKILLS && player1.getClan() != null)
		{
			for (final SkillEntry skill : player1.getClan().getSkills())
			{
				player1.addSkill(skill);
			}
		}

		if (Config.REMOVE_CLAN_SKILLS && player2.getClan() != null)
		{
			for (final SkillEntry skill : player1.getClan().getSkills())
			{
				player1.addSkill(skill);
			}
		}

		if (Config.REMOVE_HERO_SKILLS && player1.isHero())
		{
			player1.handleHeroesForFightClub(true);
		}

		if (Config.REMOVE_HERO_SKILLS && player2.isHero())
		{
			player2.handleHeroesForFightClub(true);
		}

		player1.block();
		player1.teleToLocation(_restoreCoord.get(player1.getObjectId()), ReflectionManager.MAIN);
		player1.unblock();

		player2.block();
		player2.teleToLocation(_restoreCoord.get(player2.getObjectId()), ReflectionManager.MAIN);
		player2.unblock();
	}

	/**
	 * Выводит текст по центру экрана. Выводит нескольким игрокам. Положение -
	 * TOP_CENTER
	 * 
	 * @param address - адрес текста
	 * @param arg     - параметр замены (один)
	 * @param bigFont - большой шрифт
	 * @param players - список игроков
	 */
	protected static void sayToPlayers(String address, Object arg, boolean bigFont, Player... players)
	{
		for (Player player : players)
		{
			final CustomMessage sm = new CustomMessage(address);
			player.sendPacket(new ExShowScreenMessage(sm.toString(), 3000, ScreenMessageAlign.TOP_CENTER, bigFont));
		}
	}

	/**
	 * Выводит текст по центру экрана. Выводит нескольким игрокам. Положение -
	 * TOP_CENTER
	 * 
	 * @param address - адрес текста
	 * @param bigFont - большой шрифт
	 * @param players - список игроков
	 */
	protected static void sayToPlayers(String address, boolean bigFont, Player... players)
	{
		for (Player player : players)
		{
			final CustomMessage sm = new CustomMessage(address);
			player.sendPacket(new ExShowScreenMessage(sm.toString(), 3000, ScreenMessageAlign.TOP_CENTER, bigFont));
		}
	}

	/**
	 * Выводит текст по центру экрана. Положение - TOP_CENTER
	 * 
	 * @param player  - целевой игрок
	 * @param address - адрес текста
	 * @param bigFont - большой шрифт
	 * @param args    - параметры замены текста
	 */
	protected static void sayToPlayer(Player player, String address, boolean bigFont, Object... args)
	{
		player.sendPacket(new ExShowScreenMessage(new CustomMessage(address).toString(), 3000, ScreenMessageAlign.TOP_CENTER, bigFont));
	}

	/**
	 * Возрождает мёртвых игроков
	 */
	public static void resurrectPlayers(Player player1, Player player2, Object obj)
	{
		if (player1.isDead())
		{
			player1.restoreExp();
			player1.setCurrentCp(player1.getMaxCp());
			player1.setCurrentHp(player1.getMaxHp(), true);
			player1.setCurrentMp(player1.getMaxMp());
			player1.broadcastPacket(new RevivePacket(player1));
		}
		if (player2.isDead())
		{
			player2.restoreExp();
			player2.setCurrentCp(player2.getMaxCp());
			player2.setCurrentHp(player2.getMaxHp(), true);
			player2.setCurrentMp(player2.getMaxMp());
			player2.broadcastPacket(new RevivePacket(player2));
		}
	}

	/**
	 * Recovers HP / MP / CP members
	 */
	public static void healPlayers(Player player1, Player player2, Object obj)
	{
		player1.setCurrentCp(player1.getMaxCp());
		player1.setCurrentHpMp(player1.getMaxHp(), player1.getMaxMp());
		player2.setCurrentCp(player2.getMaxCp());
		player2.setCurrentHpMp(player2.getMaxHp(), player2.getMaxMp());
	}

	/**
	 * Запускает битву между игроками.
	 * 
	 * @param player1
	 * @param player2
	 */
	protected static void startBattle(Player player1, Player player2)
	{
		unrootPlayers(player1);
		player1.setTeam(TeamType.BLUE);
		unrootPlayers(player2);
		player2.setTeam(TeamType.RED);
		sayToPlayers("scripts.events.fightclub.Start", true, player1, player2);
	}

	/**
	 * Телепортирует игроков в коллизей в заданное отражение
	 * 
	 * @param player1    - первый игрок
	 * @param player2    - втрой игрок
	 * @param reflection - отражение
	 */
	@SuppressWarnings("static-access")
	public static void teleportPlayersToColliseum(Player player1, Player player2, Reflection reflection)
	{
		player1.block();
		unRide(player1);

		if (Config.UNSUMMON_PETS)
		{
			unSummonPet(player1, true);
		}
		if (Config.UNSUMMON_SUMMONS)
		{
			unSummonPet(player1, false);
		}

		player1.stopInvisible(FightClubManager.class, true);
		if (Config.REMOVE_CLAN_SKILLS && player1.getClan() != null)
		{
			for (final SkillEntry skill : player1.getClan().getSkills())
			{
				player1.removeSkill(skill);
			}
		}

		if (Config.REMOVE_HERO_SKILLS && player1.isHero())
		{
			Hero.getInstance();
			player1.handleHeroesForFightClub(false);
		}
		if (Config.CANCEL_BUFF_BEFORE_FIGHT)
		{
			player1.getAbnormalList().stopAll();
			if (player1.getAnyServitor() != null)
			{
				player1.getAnyServitor().getAbnormalList().stopAll();
			}
		}

		player1.teleToLocation(_player1loc, reflection);
		player1.unblock();
		rootPlayer(player1);

		player2.block();
		unRide(player2);

		if (Config.UNSUMMON_PETS)
		{
			unSummonPet(player2, true);
		}
		if (Config.UNSUMMON_SUMMONS)
		{
			unSummonPet(player2, false);
		}

		player2.stopInvisible(FightClubManager.class, true);
		if (Config.REMOVE_CLAN_SKILLS && player2.getClan() != null)
		{
			for (final SkillEntry skill : player2.getClan().getSkills())
			{
				player2.removeSkill(skill);
			}
		}
		if (Config.REMOVE_HERO_SKILLS && player2.isHero())
		{
			Hero.getInstance();
			player2.handleHeroesForFightClub(false);
		}

		if (Config.CANCEL_BUFF_BEFORE_FIGHT)
		{
			player2.getAbnormalList().stopAll();
			if (player2.getAnyServitor() != null)
			{
				player2.getAnyServitor().getAbnormalList().stopAll();
			}
		}

		player2.teleToLocation(_player2loc, reflection);
		player2.unblock();
		rootPlayer(player2);
	}

	protected static class TeleportTask implements Runnable
	{
		private Player _player;
		private Location _location;

		public TeleportTask(Player player, Location location)
		{
			_player = player;
			_location = location;
			player.block();
		}

		@Override
		public void run()
		{
			_player.teleToLocation(_location);
			_player.unblock();
		}
	}
}