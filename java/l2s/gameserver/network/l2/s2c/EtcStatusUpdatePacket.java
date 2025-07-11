package l2s.gameserver.network.l2.s2c;

import l2s.commons.network.PacketWriter;
import l2s.gameserver.model.Player;

public class EtcStatusUpdatePacket implements IClientOutgoingPacket
{
	private static final int NO_CHAT_FLAG = 1 << 0;
	private static final int DANGER_AREA_FLAG = 1 << 1;
	private static final int CHARM_OF_COURAGE_FLAG = 1 << 2;

	private int _increasedForce;
	private int _weightPenalty;
	private int _flags;
	private int _lightSouls, _darkSouls;

	public EtcStatusUpdatePacket(Player player)
	{
		_increasedForce = player.getIncreasedForce();
		_weightPenalty = player.getWeightPenalty();
		_lightSouls = player.getConsumedSouls(0);
		_darkSouls = player.getConsumedSouls(1);

		if (player.getMessageRefusal() || (player.getNoChannel() != 0) || player.isBlockAll())
		{
			_flags |= NO_CHAT_FLAG;
		}
		if (player.isInDangerArea())
		{
			_flags |= DANGER_AREA_FLAG;
		}
		if (player.isCharmOfCourage())
		{
			_flags |= CHARM_OF_COURAGE_FLAG;
		}
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeC(_increasedForce); // 1-7 increase force, level
		packetWriter.writeD(_weightPenalty); // 1-4 weight penalty, level (1=50%, 2=66.6%, 3=80%, 4=100%)
		packetWriter.writeC(0x00); // Weapon Grade Penalty [1-4]
		packetWriter.writeC(0x00); // Armor Grade Penalty [1-4]
		packetWriter.writeC(0x00); // Death Penalty [1-15, 0 = disabled)], not used anymore in Ertheia
		packetWriter.writeC(0x00); // Old count for charged souls.
		packetWriter.writeC(_flags);
		packetWriter.writeC(_darkSouls); // Shadow souls
		packetWriter.writeC(_lightSouls); // Light souls
		return true;
	}
}