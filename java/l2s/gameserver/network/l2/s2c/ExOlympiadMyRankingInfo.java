package l2s.gameserver.network.l2.s2c;

import java.util.Calendar;

import l2s.commons.network.PacketWriter;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.entity.olympiad.Olympiad;

public class ExOlympiadMyRankingInfo implements IClientOutgoingPacket
{
	private final int cycleYear;
	private final int cycleMonth;
	private final int cycle;
	private final int points;

	public ExOlympiadMyRankingInfo(Player player)
	{
		Calendar calendar = Calendar.getInstance();
		cycleYear = calendar.get(Calendar.YEAR);
		cycleMonth = calendar.get(Calendar.MONTH);
		cycle = Olympiad.getCurrentCycle();
		points = Olympiad.getParticipantPoints(player.getObjectId());
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(cycleYear);
		packetWriter.writeD(cycleMonth);
		packetWriter.writeD(cycle);
		packetWriter.writeD(0x00); // Place on current cycle ?
		packetWriter.writeD(0x00); // Wins
		packetWriter.writeD(0x00); // Loses
		packetWriter.writeD(points);
		packetWriter.writeD(0x00); // Place on previous cycle
		packetWriter.writeD(0x00); // win count & lose count previous cycle? lol
		packetWriter.writeD(0x00); // ??
		packetWriter.writeD(0x00); // Points on previous cycle
		packetWriter.writeD(0x00); // Hero counts
		packetWriter.writeD(0x00); // Legend counts
		packetWriter.writeD(0x00); // change to 1 causes shows nothing
		return true;
	}
}
