package l2s.gameserver.model.actor.instances.player;

import l2s.commons.lang.reference.HardReference;
import l2s.commons.lang.reference.HardReferences;
import l2s.gameserver.model.GameObjectsStorage;
import l2s.gameserver.model.Player;

/**
 * @author nexvill
 */
public class Spectating
{
	private final int _objectId;
	private String _name;
	private int _classId;
	private int _level;

	private HardReference<Player> _playerRef = HardReferences.emptyRef();

	public Spectating(int objectId, String name, int classId, int level)
	{
		_objectId = objectId;
		_name = name;
		_classId = classId;
		_level = level;
		Player player = GameObjectsStorage.getPlayer(objectId);
		_playerRef = player != null ? player.getRef() : HardReferences.<Player> emptyRef();
	}

	public void update(Player player, boolean set)
	{
		_level = player.getLevel();
		_name = player.getName();
		_classId = player.getActiveClassId();
		_playerRef = set ? player.getRef() : HardReferences.<Player> emptyRef();
	}

	public int getObjectId()
	{
		return _objectId;
	}

	public String getName()
	{
		return _name;
	}

	public int getClassId()
	{
		Player player = getPlayer();
		return player == null ? _classId : player.getActiveClassId();
	}

	public int getLevel()
	{
		Player player = getPlayer();
		return player == null ? _level : player.getLevel();
	}

	public boolean isOnline()
	{
		Player player = _playerRef.get();
		return player != null && !player.isInOfflineMode();
	}

	public Player getPlayer()
	{
		Player player = _playerRef.get();
		return player != null && !player.isInOfflineMode() ? player : null;
	}
}