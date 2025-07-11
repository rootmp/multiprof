package l2s.gameserver.network.l2.s2c.pledge;

import l2s.gameserver.network.l2.s2c.L2GameServerPacket;
import l2s.gameserver.utils.PledgeBonusUtils;

/**
 * @author Bonux
 **/
public class ExPledgeBonusList implements IClientOutgoingPacket
{
	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeC(2); // Reward Type (0 - Skill, 1 - Item)
		packetWriter.writeD(PledgeBonusUtils.ATTENDANCE_REWARDS.get(1)); // Login bonus (.25)
		packetWriter.writeD(PledgeBonusUtils.ATTENDANCE_REWARDS.get(2)); // Login bonus (.5)
		packetWriter.writeD(PledgeBonusUtils.ATTENDANCE_REWARDS.get(3)); // Login bonus (.75)
		packetWriter.writeD(PledgeBonusUtils.ATTENDANCE_REWARDS.get(4)); // Login bonus (max)
		packetWriter.writeC(2); // Reward Type (0 - Skill, 1 - Item)
		packetWriter.writeD(PledgeBonusUtils.HUNTING_REWARDS.get(1)); // Hunting bonus (.25)
		packetWriter.writeD(PledgeBonusUtils.HUNTING_REWARDS.get(2)); // Hunting bonus (.5)
		packetWriter.writeD(PledgeBonusUtils.HUNTING_REWARDS.get(3)); // Hunting bonus (.75)
		packetWriter.writeD(PledgeBonusUtils.HUNTING_REWARDS.get(4)); // Hunting bonus (max)
		return true;
	}
}