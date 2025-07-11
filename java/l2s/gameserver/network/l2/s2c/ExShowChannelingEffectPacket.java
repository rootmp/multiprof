package l2s.gameserver.network.l2.s2c;

import l2s.commons.network.PacketWriter;
import l2s.gameserver.model.Creature;

/**
 * @author Bonux
 **/
public class ExShowChannelingEffectPacket implements IClientOutgoingPacket
{
	private final int _casterObjectId;
	private final int _targetObjectId;
	private final int _state;

	public ExShowChannelingEffectPacket(Creature caster, Creature target, int state)
	{
		_casterObjectId = caster.getObjectId();
		_targetObjectId = target.getObjectId();
		_state = state;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(_casterObjectId);
		packetWriter.writeD(_targetObjectId);
		packetWriter.writeD(_state);
		return true;
	}
}