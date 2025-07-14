package l2s.gameserver.instancemanager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.napile.primitive.maps.IntObjectMap;
import org.napile.primitive.maps.impl.CHashIntObjectMap;
import org.napile.primitive.sets.IntSet;
import org.napile.primitive.sets.impl.HashIntSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import l2s.commons.dbutils.DbUtils;
import l2s.gameserver.Announcements;
import l2s.gameserver.Config;
import l2s.gameserver.data.xml.holder.NpcHolder;
import l2s.gameserver.database.DatabaseFactory;
import l2s.gameserver.model.Spawner;
import l2s.gameserver.model.enums.RaidBossStatus;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.tables.GmListTable;
import l2s.gameserver.templates.StatsSet;
import l2s.gameserver.templates.npc.NpcTemplate;

public class RaidBossSpawnManager
{
	private static final Logger _log = LoggerFactory.getLogger(RaidBossSpawnManager.class);

	private static RaidBossSpawnManager _instance;
	protected final static IntObjectMap<Spawner> _spawntable = new CHashIntObjectMap<Spawner>();
	protected static Map<Integer, StatsSet> _storedInfo = new ConcurrentHashMap<>();
	private final IntSet _aliveRaidBosses = new HashIntSet();

	private RaidBossSpawnManager()
	{
		_instance = this;

		if(!Config.DONTLOADSPAWN)
		{
			reloadBosses();
		}
	}

	public void reloadBosses()
	{
		loadStatus();
	}

	public void cleanUp()
	{
		updateAllStatusDb();

		_storedInfo.clear();
		_spawntable.clear();
	}

	public static RaidBossSpawnManager getInstance()
	{
		if(_instance == null)
			new RaidBossSpawnManager();
		return _instance;
	}

	private void loadStatus()
	{
		_storedInfo = new ConcurrentHashMap<>();

		Connection con = null;
		Statement statement = null;
		ResultSet rset = null;

		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			rset = con.createStatement().executeQuery("SELECT * FROM `raidboss_status`");
			while(rset.next())
			{
				int id = rset.getInt("id");
				StatsSet info = new StatsSet();
				info.set("current_hp", rset.getDouble("current_hp"));
				info.set("current_mp", rset.getDouble("current_mp"));
				info.set("death_time", rset.getInt("death_time"));
				info.set("respawn_delay", rset.getInt("respawn_delay"));
				_storedInfo.put(id, info);
			}
		}
		catch(Exception e)
		{
			_log.warn("RaidBossSpawnManager: Couldnt load raidboss statuses");
		}
		finally
		{
			DbUtils.closeQuietly(con, statement, rset);
		}

		_log.info("RaidBossSpawnManager: Loaded " + _storedInfo.size() + " Statuses");
	}

	public void updateAllStatusDb()
	{
		for(Spawner spawner : _spawntable.valueCollection())
		{
			NpcInstance raidboss = spawner.getFirstSpawned();
			if(raidboss == null || !raidboss.isVisible() || raidboss.isDead())
				continue;

			updateStatusDb(raidboss);
		}
	}

	private void updateStatusDb(NpcInstance npc)
	{
		if(npc.isReflectionBoss())
			return;

		StatsSet info = _storedInfo.get(npc.getNpcId());
		if(info == null)
			_storedInfo.put(npc.getNpcId(), info = new StatsSet());

		long deathTime = npc.getDeathTime();
		if(deathTime > 0)
		{
			info.set("current_hp", 0);
			info.set("current_mp", 0);
			info.set("death_time", (int) (deathTime / 1000));
		}
		else
		{
			info.set("current_hp", npc.getCurrentHp());
			info.set("current_mp", npc.getCurrentMp());
			info.set("death_time", 0);
		}

		Connection con = null;
		PreparedStatement statement = null;

		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement("REPLACE INTO `raidboss_status` (id, current_hp, current_mp, death_time, respawn_delay) VALUES (?,?,?,?,?)");
			statement.setInt(1, npc.getNpcId());
			statement.setDouble(2, info.getDouble("current_hp"));
			statement.setDouble(3, info.getDouble("current_mp"));
			statement.setInt(4, info.getInteger("death_time", 0));
			statement.setInt(5, 0);
			statement.execute();
		}
		catch(SQLException e)
		{
			_log.warn("RaidBossSpawnManager: Couldnt update raidboss_status table");
		}
		finally
		{
			DbUtils.closeQuietly(con, statement);
		}
	}

	public void addNewSpawn(int npcId, Spawner spawnDat)
	{
		if(_spawntable.containsKey(npcId))
			return;

		_spawntable.put(npcId, spawnDat);

		StatsSet info = _storedInfo.get(npcId);
		if(info != null)
		{
			int respawnDelay = info.getInteger("respawn_delay", 0);
			if(respawnDelay == 0)
			{
				int deathTime = info.getInteger("death_time", 0);
				if(deathTime > 0)
				{
					NpcTemplate template = NpcHolder.getInstance().getTemplate(npcId);
					if(template != null)
						respawnDelay = spawnDat.calcRespawnTime(deathTime * 1000L, template.isRaid);
				}
			}
			spawnDat.setRespawnTime(respawnDelay);
		}
	}

	public void deleteSpawn(int npcId)
	{
		_spawntable.remove(npcId);
	}

	public void onBossSpawned(NpcInstance npc)
	{
		if(npc.isReflectionBoss())
			return;

		int bossId = npc.getNpcId();
		Spawner spawner = _spawntable.get(bossId);
		if(spawner == null || npc.getSpawn() != spawner)
			return;

		StatsSet info = _storedInfo.get(bossId);
		if(info != null && info.getDouble("current_hp") > 1)
		{
			npc.setCurrentHp(info.getDouble("current_hp"), false);
			npc.setCurrentMp(info.getDouble("current_mp"));
		}

		if(npc.isRaid())
		{
			_aliveRaidBosses.add(npc.getNpcId());

			GmListTable.broadcastMessageToGMs("Spawning RaidBoss " + npc.getName());

			if(Config.ALT_ANNONCE_RAID_BOSSES_REVIVAL)
				Announcements.announceToAllFromStringHolder("l2s.gameserver.instancemanager.RaidBossSpawnManager."
						+ (npc.isBoss() ? "onBossSpawned" : "onRaidBossSpawned"), npc.getName(), npc.getTitle());
		}
		updateStatusDb(npc);
	}

	public void onBossDeath(NpcInstance npc)
	{
		if(npc.isReflectionBoss())
			return;

		Spawner spawner = _spawntable.get(npc.getNpcId());
		if(spawner == null || npc.getSpawn() != spawner)
			return;

		_aliveRaidBosses.remove(npc.getNpcId());
		updateStatusDb(npc);
	}

	public RaidBossStatus getRaidBossStatusId(int bossId)
	{
		Spawner spawner = _spawntable.get(bossId);
		if(spawner == null)
			return RaidBossStatus.UNDEFINED;

		NpcInstance npc = spawner.getFirstSpawned();
		return npc == null || !npc.isVisible() || npc.isDead() ? RaidBossStatus.DEAD : RaidBossStatus.ALIVE;
	}

	public boolean isDefined(int bossId)
	{
		return _spawntable.containsKey(bossId);
	}

	public IntObjectMap<Spawner> getSpawnTable()
	{
		return _spawntable;
	}

	public IntSet getAliveRaidBosses()
	{
		return _aliveRaidBosses;
	}
}