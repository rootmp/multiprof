package l2s.gameserver.network.l2.s2c;

import l2s.commons.network.PacketWriter;

/**
 * @author monithly
 */
public class ExMagicAttackInfo implements IClientOutgoingPacket
{
	public final static int CRITICAL = 1;
	public final static int CRITICAL_HEAL = 2;
	public final static int OVERHIT = 3;
	public final static int EVADED = 4;
	public final static int BLOCKED = 5;
	public final static int RESISTED = 6;
	public final static int IMMUNE = 7;

	private final int _attackerId;
	private final int _targetId;
	private final int _info;

	public ExMagicAttackInfo(int attackerId, int targetId, int info)
	{
		_attackerId = attackerId;
		_targetId = targetId;
		_info = info;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(_attackerId);
		packetWriter.writeD(_targetId);
		packetWriter.writeD(_info);
		return true;
	}
}
