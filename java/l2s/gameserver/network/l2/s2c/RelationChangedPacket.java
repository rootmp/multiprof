package l2s.gameserver.network.l2.s2c;

import java.util.ArrayList;
import java.util.List;

import l2s.commons.network.PacketWriter;
import l2s.gameserver.model.Playable;
import l2s.gameserver.model.Player;

public class RelationChangedPacket implements IClientOutgoingPacket
{
	private class RelationChangedData
	{
		public int objectId;
		public boolean isAutoAttackable;
		public long relation;
		public int karma, pvpFlag;
	}

	public enum RelationChangedType
	{
		INSIDE_BATTLEFIELD,
		IN_PVP,
		CHAOTIC,
		IN_PARTY(32),
		PARTY_LEADER,
		SAME_PARTY,
		IN_PLEDGE,
		UNK,
		PLEDGE_LEADER(16),
		SAME_PLEDGE,
		SIEGE_PARTICIPANT,
		SIEGE_ATTACKER,
		UNK2,
		SIEGE_ALLY,
		SIEGE_ENEMY,
		SIEGE_DEFENDER,
		CLAN_WAR_ATTACKED(8192),
		CLAN_WAR_ATTACKER(24576), // mutual war
		IN_ALLIANCE,
		ALLIANCE_LEADER,
		SAME_ALLIANCE,
		UNK3, // blue sword
		SURVEILLANCE,
		KILLED_BY_DEATH_KNIGHT(0x20000000);

		private int _id;

		RelationChangedType(int id)
		{
			_id = id;
		}

		RelationChangedType()
		{

		}

		public long getRelationState()
		{
			return getId() > 0 ? getId() :(long) Math.pow(2, ordinal()) * (SURVEILLANCE == this ? 10000_0000L : 1L);
		}

		public int getId()
		{
			return _id;
		}
	}

	// Masks
	private static final byte SEND_DEFAULT = (byte) 0x01;
	private static final byte SEND_ONE = (byte) 0x02;
	private static final byte SEND_MULTI = (byte) 0x04;

	private byte _mask = (byte) 0x00;

	private final List<RelationChangedData> _datas = new ArrayList<>();

	public RelationChangedPacket()
	{
		//
	}

	public RelationChangedPacket(Playable about, Player target)
	{
		add(about, target);
	}

	public void add(Playable about, Player target)
	{
		RelationChangedData data = new RelationChangedData();
		data.objectId = about.getObjectId();
		data.karma = about.getKarma();
		data.pvpFlag = about.getPvpFlag();
		data.isAutoAttackable = about.isAutoAttackable(target);
		data.relation = about.getRelation(target);

		_datas.add(data);

		if(_datas.size() > 1)
			_mask |= SEND_MULTI;
		else if(_datas.size() == 1)
			_mask |= SEND_ONE;
	}

	public void setTestRelation(int objectId, RelationChangedType relationType, boolean isAutoAttackable, int karma, int pvpFlag)
	{
		RelationChangedData data = new RelationChangedData();
		data.objectId = objectId;
		data.karma = karma;
		data.pvpFlag = pvpFlag;
		data.isAutoAttackable = isAutoAttackable;
		data.relation |=  relationType.getRelationState();

		_datas.add(data);

		if (_datas.size() > 1)
			_mask |= SEND_MULTI;
		else if (_datas.size() == 1)
			_mask |= SEND_ONE;
	}
	
	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeC(_mask);
		if((_mask & SEND_MULTI) == SEND_MULTI)
		{
			packetWriter.writeH(_datas.size());
			for(RelationChangedData data : _datas)
				writeRelation(packetWriter, data);
		}
		else if((_mask & SEND_ONE) == SEND_ONE)
			writeRelation(packetWriter, _datas.get(0));
		else if((_mask & SEND_DEFAULT) == SEND_DEFAULT)
			packetWriter.writeD(_datas.get(0).objectId);
		return true;
	}

	private void writeRelation(PacketWriter packetWriter, RelationChangedData data)
	{
		packetWriter.writeD(data.objectId);
		packetWriter.writeQ(data.relation);
		packetWriter.writeC(data.isAutoAttackable);
		packetWriter.writeD(data.karma);
		packetWriter.writeC(data.pvpFlag);
	}
}