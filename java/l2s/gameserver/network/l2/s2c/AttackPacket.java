package l2s.gameserver.network.l2.s2c;

import l2s.commons.network.PacketWriter;
import l2s.gameserver.model.Creature;
import l2s.gameserver.model.GameObject;

public class AttackPacket implements IClientOutgoingPacket
{
	public static final int HITFLAG_MISS = 0x01;
	public static final int HITFLAG_SHLD = 0x02;
	public static final int HITFLAG_CRIT = 0x04;
	public static final int HITFLAG_USESS = 0x08;

	public final boolean _soulshot;
	public final int _attackerId;
	private final int _grade;
	private final int _x;
	private final int _y;
	private final int _z;
	private final int _tx;
	private final int _ty;
	private final int _tz;
	private final int _addShotEffect;
	private Hit[] hits;

	private class Hit
	{
		int _targetId;
		int _damage;
		int _flags;

		Hit(GameObject target, int damage, boolean miss, boolean crit, boolean shld)
		{
			_targetId = target.getObjectId();
			_damage = damage;

			if(miss)
			{
				_flags = HITFLAG_MISS;
				return;
			}

			if(_soulshot)
			{
				_flags = HITFLAG_USESS;
			}
			if(crit)
			{
				_flags |= HITFLAG_CRIT;
			}
			if(shld)
			{
				_flags |= HITFLAG_SHLD;
			}
		}
	}

	public AttackPacket(Creature attacker, Creature target, boolean ss, int grade)
	{
		_attackerId = attacker.getObjectId();
		_soulshot = ss;
		_grade = grade;
		_addShotEffect = attacker.getAdditionalVisualSSEffect();
		_x = attacker.getX();
		_y = attacker.getY();
		_z = attacker.getZ();
		_tx = target.getX();
		_ty = target.getY();
		_tz = target.getZ();
		hits = new Hit[0];
	}

	/**
	 * Add this hit (target, damage, miss, critical, shield) to the Server-Client
	 * packet Attack.<BR>
	 * <BR>
	 */
	public void addHit(GameObject target, int damage, boolean miss, boolean crit, boolean shld)
	{
		// Get the last position in the hits table
		int pos = hits.length;

		// Create a new Hit object
		Hit[] tmp = new Hit[pos + 1];

		// Add the new Hit object to hits table
		System.arraycopy(hits, 0, tmp, 0, hits.length);
		tmp[pos] = new Hit(target, damage, miss, crit, shld);
		hits = tmp;
	}

	/**
	 * Return True if the Server-Client packet Attack conatins at least 1 hit.<BR>
	 * <BR>
	 */
	public boolean hasHits()
	{
		return hits.length > 0;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(_attackerId);
		packetWriter.writeD(hits[0]._targetId);
		packetWriter.writeD(_soulshot ? _addShotEffect : 0x00);
		packetWriter.writeD(hits[0]._damage);
		packetWriter.writeD(hits[0]._flags);
		packetWriter.writeD(_soulshot ? _grade : 0x00);
		packetWriter.writeD(_x);
		packetWriter.writeD(_y);
		packetWriter.writeD(_z);
		packetWriter.writeH(hits.length - 1);
		for(int i = 1; i < hits.length; i++)
		{
			packetWriter.writeD(hits[i]._targetId);
			packetWriter.writeD(hits[i]._damage);
			packetWriter.writeD(hits[i]._flags);
			packetWriter.writeD(_soulshot ? _grade : 0x00);
		}
		packetWriter.writeD(_tx);
		packetWriter.writeD(_ty);
		packetWriter.writeD(_tz);
		return true;
	}
}