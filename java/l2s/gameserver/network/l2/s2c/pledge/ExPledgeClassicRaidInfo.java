package l2s.gameserver.network.l2.s2c.pledge;

import l2s.commons.network.PacketWriter;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.pledge.Clan;
import l2s.gameserver.network.l2.s2c.IClientOutgoingPacket;

public class ExPledgeClassicRaidInfo implements IClientOutgoingPacket
{
	private final int _lastRaidPhase;
	private static final int[][] SKILLS = {{1867, 1},{1867, 2},{1867, 3},{1867, 4},{1867, 5},{1867, 6},{1867, 7}};

	public ExPledgeClassicRaidInfo(Player player)
	{
		Clan clan = player.getClan();
		_lastRaidPhase = clan == null ? 0 : clan.getArenaStage();
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(_lastRaidPhase);
		packetWriter.writeD(SKILLS.length);
		for (int[] skill : SKILLS)
		{
			packetWriter.writeD(skill[0]); // Skill ID
			packetWriter.writeD(skill[1]); // Skill Level
		}
		return true;
	}
}