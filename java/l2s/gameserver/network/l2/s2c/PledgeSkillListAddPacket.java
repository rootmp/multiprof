package l2s.gameserver.network.l2.s2c;
import l2s.commons.network.PacketWriter;

public class PledgeSkillListAddPacket implements IClientOutgoingPacket
{
	private int _skillId;
	private int _skillLevel;

	public PledgeSkillListAddPacket(int skillId, int skillLevel)
	{
		_skillId = skillId;
		_skillLevel = skillLevel;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(_skillId);
		packetWriter.writeD(_skillLevel);
	}
}