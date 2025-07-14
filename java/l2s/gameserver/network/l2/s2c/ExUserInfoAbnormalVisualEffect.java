package l2s.gameserver.network.l2.s2c;

import java.util.Set;

import l2s.commons.network.PacketWriter;
import l2s.gameserver.model.Player;
import l2s.gameserver.skills.enums.AbnormalEffect;

/**
 * @reworked by Bonux
 **/
public class ExUserInfoAbnormalVisualEffect implements IClientOutgoingPacket
{
	private final int _objectId;
	private final int _transformId;
	private final Set<AbnormalEffect> _abnormalEffects;

	public ExUserInfoAbnormalVisualEffect(Player player)
	{
		_objectId = player.getObjectId();
		_transformId = player.getVisualTransformId();
		_abnormalEffects = player.getAbnormalEffects();
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(_objectId);
		packetWriter.writeD(_transformId);
		packetWriter.writeD(_abnormalEffects.size());
		for(AbnormalEffect abnormal : _abnormalEffects)
		{
			packetWriter.writeH(abnormal.getId());
		}

		return true;
	}
}