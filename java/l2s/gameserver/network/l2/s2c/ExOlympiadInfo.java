package l2s.gameserver.network.l2.s2c;

import java.util.concurrent.TimeUnit;

import l2s.commons.network.PacketWriter;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.entity.olympiad.CompType;
import l2s.gameserver.model.entity.olympiad.Olympiad;

public class ExOlympiadInfo implements IClientOutgoingPacket
{
	private final boolean open;
	private final int timeLeft;
	private final CompType type;

	private ExOlympiadInfo(boolean open, int timeLeft, CompType type)
	{
		this.open = open;
		this.timeLeft = timeLeft;
		this.type = type;
	}

	public ExOlympiadInfo(Player player)
	{
		this(Olympiad.canShowHud(player), (int) TimeUnit.MILLISECONDS.toSeconds(Olympiad.getMillisToCompEnd()), Olympiad.getCompType());
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeC(open);
		packetWriter.writeD(timeLeft);
		packetWriter.writeC(type == null ? 0 : Math.min(type.ordinal(), 1)); // 0 - 3x3 Battles, 1 - Olympiad
		return true;
	}
}
