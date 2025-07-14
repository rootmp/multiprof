package l2s.gameserver.model.actor.instances.player;

import java.util.Collection;

import org.apache.commons.lang3.StringUtils;

import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.hash.TIntObjectHashMap;
import l2s.gameserver.dao.CharacterSpectatingListDAO;
import l2s.gameserver.model.GameObjectsStorage;
import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.components.SystemMsg;
import l2s.gameserver.network.l2.s2c.RelationChangedPacket;
import l2s.gameserver.network.l2.s2c.SystemMessagePacket;
import l2s.gameserver.network.l2.s2c.spectating.ExUserWatcherTargetList;
import l2s.gameserver.network.l2.s2c.spectating.ExUserWatcherTargetStatus;

/**
 * @author nexvill
 */
public class SpectatingList
{
	public static final int MAX_SPECTATING_LIST_SIZE = 10;

	private TIntObjectMap<Spectating> _spectatingList = new TIntObjectHashMap<Spectating>(0);
	private final Player _owner;

	public SpectatingList(Player owner)
	{
		_owner = owner;
	}

	public void restore()
	{
		_spectatingList = CharacterSpectatingListDAO.getInstance().select(_owner);
	}

	public Spectating get(int objectId)
	{
		return _spectatingList.get(objectId);
	}

	public Spectating get(String name)
	{
		if(StringUtils.isEmpty(name))
			return null;

		for(Spectating b : values())
		{
			if(name.equalsIgnoreCase(b.getName()))
				return b;
		}
		return null;
	}

	public boolean contains(int objectId)
	{
		return _spectatingList.containsKey(objectId);
	}

	public boolean contains(Player player)
	{
		if(player == null)
			return false;
		return contains(player.getObjectId());
	}

	public boolean contains(String name)
	{
		return get(name) != null;
	}

	public int size()
	{
		return _spectatingList.size();
	}

	public Spectating[] values()
	{
		return _spectatingList.values(new Spectating[_spectatingList.size()]);
	}

	public Collection<Spectating> valueCollection()
	{
		return _spectatingList.valueCollection();
	}

	public boolean isEmpty()
	{
		return _spectatingList.isEmpty();
	}

	public void add(int objId, TIntObjectMap<Spectating> player)
	{
		String name = player.get(objId).getName();
		if(StringUtils.isEmpty(name) || name.equalsIgnoreCase(_owner.getName()) || contains(name))
		{
			if(name.equalsIgnoreCase(_owner.getName()))
				_owner.sendPacket(SystemMsg.YOU_CANNOT_ADD_YOURSELF_TO_YOUR_SURVEILLANCE_LIST);
			else if(contains(name))
				_owner.sendPacket(SystemMsg.THE_CHARACTER_HAS_ALREADY_BEEN_ADDED_TO_THE_SURVEILLANCE_LIST);
			return;
		}

		_owner.sendPacket(new SystemMessagePacket(SystemMsg.YOU_ADDED_C1_TO_YOUR_SURVEILLANCE_LIST).addString(name));
		_spectatingList.put(objId, player.get(objId));

		CharacterSpectatingListDAO.getInstance().insert(_owner, objId, name);

		_owner.sendPacket(new ExUserWatcherTargetList(_owner));

		Player plr = GameObjectsStorage.getPlayer(objId);
		if((plr != null) && plr.isOnline())
		{
			RelationChangedPacket packet = new RelationChangedPacket(plr, _owner);
			_owner.sendPacket(packet);
		}
	}

	public void remove(String name)
	{
		if(StringUtils.isEmpty(name))
			return;

		int spectatingObjId = 0;
		for(Spectating s : values())
		{
			if(name.equalsIgnoreCase(s.getName()))
			{
				spectatingObjId = s.getObjectId();
				break;
			}
		}

		if(spectatingObjId == 0)
		{
			_owner.sendPacket(SystemMsg.YOU_HAVE_FAILED_TO_DELETE_THE_CHARACTER_);
			return;
		}

		_owner.sendPacket(new SystemMessagePacket(SystemMsg.YOU_DELETED_C1_FROM_YOUR_SURVEILLANCE_LIST).addString(name));

		_spectatingList.remove(spectatingObjId);

		CharacterSpectatingListDAO.getInstance().delete(_owner, spectatingObjId);

		Player plr = GameObjectsStorage.getPlayer(spectatingObjId);
		if((plr != null) && plr.isOnline())
		{
			RelationChangedPacket packet = new RelationChangedPacket(plr, _owner);
			_owner.sendPacket(packet);
		}

		_owner.sendPacket(new ExUserWatcherTargetList(_owner));
	}

	public void notifyChangeName(int spectatingObjectId)
	{
		if(_spectatingList.containsKey(spectatingObjectId))
		{
			_owner.sendPacket(new ExUserWatcherTargetList(_owner));
		}
	}

	public void notifySpectatings(boolean login)
	{
		for(Player spectating : GameObjectsStorage.getPlayers(false, false))
		{

			Spectating thisSpectating = spectating.getSpectatingList().get(_owner.getObjectId());
			if(thisSpectating == null)
				continue;

			thisSpectating.update(_owner, login);

			if(login)
				spectating.sendPacket(new SystemMessagePacket(SystemMsg.C1_FROM_YOUR_SURVEILLANCE_LIST_IS_ONLINE).addString(_owner.getName()));
			else
			{
				spectating.sendPacket(new SystemMessagePacket(SystemMsg.C1_FROM_YOUR_SURVEILLANCE_LIST_IS_OFFLINE).addString(_owner.getName()));
			}
			spectating.sendPacket(new ExUserWatcherTargetStatus(thisSpectating, login));
		}
	}

	@Override
	public String toString()
	{
		return "SpectatingList[owner=" + _owner.getName() + "]";
	}
}
