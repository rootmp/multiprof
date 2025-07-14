package l2s.gameserver.model;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import gnu.trove.iterator.TIntObjectIterator;
import gnu.trove.map.hash.TIntObjectHashMap;
import l2s.commons.collections.LazyArrayList;
import l2s.commons.util.Rnd;
import l2s.gameserver.ThreadPoolManager;
import l2s.gameserver.ai.CtrlEvent;
import l2s.gameserver.model.GameObjectTasks.NotifyAITask;
import l2s.gameserver.model.instances.NpcInstance;

/**
 * Аггролист NPC.
 * 
 * @author G1ta0
 */
public class AggroList
{
	private final NpcInstance _npc;
	private final TIntObjectHashMap<AggroInfo> _hateList = new TIntObjectHashMap<AggroInfo>();
	private final Map<Party, PartyDamage> _partyDamageMap = new HashMap<Party, PartyDamage>();
	private AggroInfo[] _hated = _hateList.values(new AggroInfo[_hateList.size()]);
	private AggroInfo _mostHated = null;

	public AggroList(NpcInstance npc)
	{
		_npc = npc;
	}

	public void addDamageHate(Creature attacker, int damage, int aggro)
	{
		if(attacker == null)
		{ return; }

		damage = Math.max(damage, 0);

		if(((damage == 0) && (aggro == 0)) || attacker.isConfused())
		{ return; }

		AggroInfo ai;

		if((ai = _hateList.get(attacker.getObjectId())) == null)
		{
			_hateList.put(attacker.getObjectId(), ai = new AggroInfo(attacker));
		}

		if(attacker.getPlayer() != null)
		{
			final Party party = attacker.getPlayer().getParty();
			if(party != null)
			{
				PartyDamage pd = _partyDamageMap.get(party);
				if(pd == null)
				{
					pd = new PartyDamage(party);
					_partyDamageMap.put(party, pd);
				}
				pd.damage += damage;
			}
		}

		ai.damage += damage;
		ai.hate += aggro;
		ai.damage = Math.max(ai.damage, 0);
		ai.hate = Math.max(ai.hate, 0);

		if((aggro > 0) && ((_mostHated == null) || ((_mostHated != ai) && (ai.hate > _mostHated.hate))))
		{
			_mostHated = ai;
			ThreadPoolManager.getInstance().execute(new NotifyAITask(_npc, CtrlEvent.EVT_MOST_HATED_CHANGED));
		}
	}

	public void reduceHate(Creature target, int hate)
	{
		final AggroInfo ai = _hateList.get(target.getObjectId());
		if(ai != null)
		{
			ai.hate -= hate;
			ai.hate = Math.max(ai.hate, 0);

			if(ai == _mostHated)
			{
				final Creature mostHated = getMostHated(-1);
				if(mostHated != null)
				{
					final AggroInfo mostHatedInfo = get(mostHated);
					if((mostHatedInfo != null) && (mostHatedInfo != ai) && (mostHatedInfo.hate > ai.hate))
					{
						_mostHated = mostHatedInfo;
						ThreadPoolManager.getInstance().execute(new NotifyAITask(_npc, CtrlEvent.EVT_MOST_HATED_CHANGED));
						return;
					}
				}

				if(ai.hate <= 0)
				{
					ThreadPoolManager.getInstance().execute(new NotifyAITask(_npc, CtrlEvent.EVT_MOST_HATED_CHANGED));
				}
			}
		}
	}

	public int getHate(Creature target)
	{
		int hate = 0;
		final AggroInfo ai = _hateList.get(target.getObjectId());
		if(ai != null)
		{
			hate = ai.hate;
		}

		return hate;
	}

	public AggroInfo get(Creature attacker)
	{
		return _hateList.get(attacker.getObjectId());
	}

	private void remove(int objectId, boolean onlyHate)
	{
		if(!onlyHate)
		{
			_hateList.remove(objectId);
			return;
		}

		final AggroInfo ai = _hateList.get(objectId);
		if(ai != null)
		{
			if(ai.damage == 0)
			{
				_hateList.remove(objectId);
			}
			else
			{
				ai.hate = 0;
			}
		}
	}

	public void remove(Creature attacker, boolean onlyHate)
	{
		remove(attacker.getObjectId(), onlyHate);
	}

	public void clear()
	{
		clear(false);
	}

	public void clear(boolean onlyHate)
	{
		if(_hateList.isEmpty())
		{ return; }

		if(!onlyHate)
		{
			_hateList.clear();
			return;
		}

		AggroInfo ai;
		for(final TIntObjectIterator<AggroInfo> itr = _hateList.iterator(); itr.hasNext();)
		{
			itr.advance();
			ai = itr.value();
			ai.hate = 0;
			if(ai.damage == 0)
			{
				itr.remove();
			}
		}
	}

	public boolean isEmpty()
	{
		return _hateList.isEmpty();
	}

	private Creature getOrRemoveHated(int objectId)
	{
		final GameObject object = GameObjectsStorage.findObject(objectId);
		if((object == null) || !object.isCreature())
		{
			remove(objectId, true);
			return null;
		}

		final Creature cha = (Creature) object;
		if((cha.isPlayable() && ((Playable) cha).isInNonAggroTime()) || (cha.isPlayer() && !((Player) cha).isOnline()))
		{
			remove(objectId, true);
			return null;
		}
		return cha;
	}

	public List<Creature> getHateList(int radius)
	{
		if(_hated.length == 0)
		{ return Collections.emptyList(); }

		try
		{
			Arrays.sort(_hated, HateComparator.getInstance());
		}
		catch(final Exception e)
		{
			// Заглушка против глюка явы: Comparison method violates its general contract!
		}

		if(_hated[0].hate == 0)
		{ return Collections.emptyList(); }

		final List<Creature> tempList = new LazyArrayList<Creature>();
		for(final AggroInfo ai : _hated)
		{
			if(ai.hate == 0)
			{
				continue;
			}

			final Creature cha = getOrRemoveHated(ai.attackerId);
			if(cha == null)
			{
				continue;
			}

			if((radius == -1) || cha.isInRangeZ(_npc.getLoc(), radius))
			{
				tempList.add(cha);
				break;
			}
		}

		return tempList;
	}

	public Creature getMostHated(int radius)
	{
		if(_hated.length == 0)
		{ return null; }

		try
		{
			Arrays.sort(_hated, HateComparator.getInstance());
		}
		catch(final Exception e)
		{
			// Заглушка против глюка явы: Comparison method violates its general contract!
		}

		if(_hated[0].hate == 0)
		{ return null; }

		for(final AggroInfo ai : _hated)
		{
			if(ai.hate == 0)
			{
				continue;
			}

			final Creature cha = getOrRemoveHated(ai.attackerId);
			if(cha == null)
			{
				continue;
			}

			if((radius == -1) || cha.isInRangeZ(_npc.getLoc(), radius))
			{
				if(cha.isDead())
				{
					continue;
				}
				return cha;
			}
		}

		return null;
	}

	public Creature getRandomHated(int radius)
	{
		if(_hated.length == 0)
		{ return null; }

		try
		{
			Arrays.sort(_hated, HateComparator.getInstance());
		}
		catch(final Exception e)
		{
			// Заглушка против глюка явы: Comparison method violates its general contract!
		}

		if(_hated[0].hate == 0)
		{ return null; }

		final LazyArrayList<Creature> randomHated = LazyArrayList.newInstance();

		Creature mostHated;
		for(final AggroInfo ai : _hated)
		{
			if(ai.hate == 0)
			{
				continue;
			}

			final Creature cha = getOrRemoveHated(ai.attackerId);
			if(cha == null)
			{
				continue;
			}

			if((radius == -1) || cha.isInRangeZ(_npc.getLoc(), radius))
			{
				if(cha.isDead())
				{
					continue;
				}
				randomHated.add(cha);
				break;
			}
		}

		if(randomHated.isEmpty())
		{
			mostHated = null;
		}
		else
		{
			mostHated = randomHated.get(Rnd.get(randomHated.size()));
		}

		LazyArrayList.recycle(randomHated);

		return mostHated;
	}

	public Creature getTopDamager(Creature defaultDamager)
	{
		if(_hated.length == 0)
		{ return defaultDamager; }

		try
		{
			Arrays.sort(_hated, DamageComparator.getInstance());
		}
		catch(final Exception e)
		{
			// Заглушка против глюка явы: Comparison method violates its general contract!
		}

		if(_hated[0].damage == 0)
		{ return defaultDamager; }

		Creature topDamager = defaultDamager;
		int topDamage = 0;

		final List<Creature> chars = World.getAroundCharacters(_npc);
		loop:
		for(final AggroInfo ai : _hated)
		{
			for(final Creature cha : chars)
			{
				if(cha.getObjectId() == ai.attackerId)
				{
					topDamager = cha;
					topDamage = ai.damage;
					break loop;
				}
			}
		}

		PartyDamage[] partyDmg;
		if(_partyDamageMap.isEmpty())
		{ return topDamager; }

		partyDmg = _partyDamageMap.values().toArray(new PartyDamage[_partyDamageMap.size()]);

		try
		{
			Arrays.sort(partyDmg, DamageComparator.getInstance());
		}
		catch(final Exception e)
		{
			// Заглушка против глюка явы: Comparison method violates its general contract!
		}

		for(final PartyDamage pd : partyDmg)
		{
			if(pd.damage > topDamage)
			{
				for(AggroInfo ai : _hated)
				{
					for(final Player player : pd.party.getPartyMembers())
					{
						if((player.getObjectId() == ai.attackerId) && chars.contains(player))
						{ return player; }
					}
				}
			}
		}

		return topDamager;
	}

	public Map<Creature, HateInfo> getCharMap()
	{
		if(isEmpty())
		{ return Collections.emptyMap(); }

		final Map<Creature, HateInfo> aggroMap = new HashMap<Creature, HateInfo>();
		final List<Creature> chars = World.getAroundCharacters(_npc);
		AggroInfo ai;
		for(final TIntObjectIterator<AggroInfo> itr = _hateList.iterator(); itr.hasNext();)
		{
			itr.advance();
			ai = itr.value();
			if((ai.damage == 0) && (ai.hate == 0))
			{
				continue;
			}
			for(final Creature attacker : chars)
			{
				if(attacker.getObjectId() == ai.attackerId)
				{
					aggroMap.put(attacker, new HateInfo(attacker, ai));
					break;
				}
			}
		}

		return aggroMap;
	}

	public Map<Playable, HateInfo> getPlayableMap()
	{
		if(isEmpty())
		{ return Collections.emptyMap(); }

		final Map<Playable, HateInfo> aggroMap = new HashMap<Playable, HateInfo>();
		final List<Playable> chars = World.getAroundPlayables(_npc);
		AggroInfo ai;
		for(final TIntObjectIterator<AggroInfo> itr = _hateList.iterator(); itr.hasNext();)
		{
			itr.advance();
			ai = itr.value();
			if((ai.damage == 0) && (ai.hate == 0))
			{
				continue;
			}
			for(final Playable attacker : chars)
			{
				if(attacker.getObjectId() == ai.attackerId)
				{
					aggroMap.put(attacker, new HateInfo(attacker, ai));
					break;
				}
			}
		}

		return aggroMap;
	}

	public Collection<AggroInfo> getAggroInfos()
	{
		Collection<AggroInfo> infos = _hateList.valueCollection();
		return infos;
	}

	public Collection<PartyDamage> getPartyDamages()
	{
		Collection<PartyDamage> damages = _partyDamageMap.values();
		return damages;
	}

	public void copy(AggroList aggroList)
	{
		final Collection<AggroInfo> aggroInfos = aggroList.getAggroInfos();
		for(final AggroInfo aggroInfo : aggroInfos)
		{
			_hateList.put(aggroInfo.attackerId, aggroInfo);
		}

		final Collection<PartyDamage> partyDamages = aggroList.getPartyDamages();
		for(final PartyDamage partyDamage : partyDamages)
		{
			_partyDamageMap.put(partyDamage.party, partyDamage);
		}
	}

	private abstract static class DamageHate
	{
		public int hate;
		public int damage;
	}

	public static class HateInfo extends DamageHate
	{
		public final Creature attacker;

		HateInfo(Creature attacker, AggroInfo ai)
		{
			this.attacker = attacker;
			this.hate = ai.hate;
			this.damage = ai.damage;
		}
	}

	public static class AggroInfo extends DamageHate
	{
		public final int attackerId;

		AggroInfo(Creature attacker)
		{
			this.attackerId = attacker.getObjectId();
		}
	}

	public static class PartyDamage extends DamageHate
	{
		public final Party party;

		PartyDamage(Party party)
		{
			this.party = party;
		}
	}

	public static class DamageComparator implements Comparator<DamageHate>
	{
		private static Comparator<DamageHate> instance = new DamageComparator();

		public static Comparator<DamageHate> getInstance()
		{
			return instance;
		}

		@Override
		public int compare(DamageHate o1, DamageHate o2)
		{
			if((o1 == null) || (o2 == null) || (o1 == o2))
			{ return 0; }
			return Integer.compare(o2.damage, o1.damage);
		}
	}

	public static class HateComparator implements Comparator<DamageHate>
	{
		private static Comparator<DamageHate> instance = new HateComparator();

		public static Comparator<DamageHate> getInstance()
		{
			return instance;
		}

		@Override
		public int compare(DamageHate o1, DamageHate o2)
		{
			if((o1 == null) || (o2 == null) || (o1 == o2))
			{ return 0; }
			if(o1.hate == o2.hate)
			{ return Integer.compare(o2.damage, o1.damage); }
			return Integer.compare(o2.hate, o1.hate);
		}
	}
}
