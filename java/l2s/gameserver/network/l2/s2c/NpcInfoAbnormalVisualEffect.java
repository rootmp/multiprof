package l2s.gameserver.network.l2.s2c;
import l2s.commons.network.PacketWriter;

import java.util.Set;

import l2s.gameserver.model.Creature;
import l2s.gameserver.skills.enums.AbnormalEffect;

/**
 * @reworked by Bonux
 **/
public class NpcInfoAbnormalVisualEffect implements IClientOutgoingPacket
{
	private final int _objectId;
	private final int _transformId;
	private final Set<AbnormalEffect> _abnormalEffects;

	public NpcInfoAbnormalVisualEffect(Creature npc)
	{
		_objectId = npc.getObjectId();
		_transformId = npc.getVisualTransformId();
		_abnormalEffects = npc.getAbnormalEffects();
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(_objectId);
		packetWriter.writeD(_transformId);
		packetWriter.writeH(_abnormalEffects.size());
		for (AbnormalEffect abnormal : _abnormalEffects)
			packetWriter.writeH(abnormal.getId());
	
		return true;
	}
}