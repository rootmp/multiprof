package l2s.gameserver.network.l2.s2c;

import java.util.ArrayList;
import java.util.List;

import l2s.commons.network.PacketWriter;
import l2s.gameserver.model.Creature;

public class StatusUpdatePacket implements IClientOutgoingPacket
{
	public static enum StatusType
	{
		/*0*/Normal,
		/*1*/HPUpdate,
		/*2*/CPUpdate,
		/*3*/MPUpdate,
		/*4*/DamageText,
		/*5*/EXPUpdate,
		/*6*/DotEffect
	}

	public static enum UpdateType
	{
		VCP_CLASS(0x00),
		VCP_LEVEL(0x01),
		VCP_UNK_1(0x02),
		VCP_STR(0x03),
		VCP_DEX(0x04),
		VCP_CON(0x05),
		VCP_INT(0x06),
		VCP_WIT(0x07),
		VCP_MEN(0x08),
		VCP_HP(0x09),
		VCP_MAXHP(0x0A),
		VCP_MP(0x0B),
		VCP_MAXMP(0x0C),
		VCP_SP(0x0D),
		VCP_CARRINGWEIGHT(0xE),
		VCP_CARRYWEIGHT(0xF),
		VCP_ATTACKRANGE(0x10),
		VCP_PHYSICALATTACK(0x11),
		VCP_PHYSICALATTACKSPEED(0x12),
		VCP_PHYSICALDEFENSE(0x13),
		VCP_PHYSICALAVOID(0x14),
		VCP_HITRATE(0x15),
		VCP_CRITICALRATE(0x16),
		VCP_MAGICALATTACK(0x17),
		VCP_MAGICCASTINGSPEED(0x18),
		VCP_ISGUILTY(0x19),
		VCP_CRIMINAL_RATE(0x1A),
		VCP_COLLISION_RADIUS(0x1B),
		VCP_COLLISION_HEIGHT(0x1C),
		VCP_BASE_SPEED(0x1D),
		VCP_SPEED_MODIFIER(0x1E),
		VCP_HP_REGEN(0x1F),
		VCP_CP(0x20),
		VCP_MAXCP(0x21),
		VCP_ULTIMATE_SKILL_POINT(0x22),
		VCP_SMALLWINDOW_UPDATE(0x23),
		VCP_LUC(0x24),
		VCP_CHA(0x25),
		VCP_UNK4(0x26),
		VCP_DP(0x27),
		VCP_MAXDP(0x28),
		VCP_UNK1(0x29),
		VCP_BP(0x2A),
		VCP_MAXBP(0x2B),
		VCP_AP(0x2C),
		VCP_MAXAP(0x2D),
		VCP_LP(0x2E),
		VCP_MAXLP(0x2F), //max WP lp bp
		VCP_LL(0x30),
		VCP_UNK2(0x31),
		VCP_WP(0x32), //WP Lp bp
		VCP_MAXWP(0x33),
		VCP_MAX(0x34);

		private int _client_id;

		UpdateType(int client_id)
		{
			_client_id = client_id;
		}

		public int getClientId()
		{
			return _client_id;
		}
	}

	private final StatusType _updateType;
	private final int _objectId, _receiverId;

	private final List<Attribute> _attributes = new ArrayList<Attribute>();

	class Attribute
	{
		public final int id;
		public final int value;

		Attribute(int id, int value)
		{
			this.id = id;
			this.value = value;
		}
	}

	public StatusUpdatePacket(StatusType updateType, Creature creature)
	{
		_updateType = updateType;
		_objectId = creature.getObjectId();
		_receiverId = 0;
	}

	public StatusUpdatePacket(StatusType updateType, Creature creature, Creature receiver)
	{
		_updateType = updateType;
		_objectId = creature.getObjectId();
		_receiverId = receiver == null ? 0 : receiver.getObjectId();
	}

	public StatusUpdatePacket addAttribute(int id, int level)
	{
		_attributes.add(new Attribute(id, level));
		return this;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(_objectId);
		packetWriter.writeD(_receiverId);
		packetWriter.writeC(_updateType.ordinal());
		packetWriter.writeC(_attributes.size());

		for(Attribute temp : _attributes)
		{
			packetWriter.writeC(temp.id);
			if(temp.id == 9 || temp.id == 10)
				packetWriter.writeQ(temp.value);
			else
				packetWriter.writeD(temp.value);
		}
		return true;
	}

	public boolean hasAttributes()
	{
		return !_attributes.isEmpty();
	}
}