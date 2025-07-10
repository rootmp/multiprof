package l2s.gameserver.network.l2.s2c;
import l2s.commons.network.PacketWriter;

public class ExLetterCollectorUiLauncher implements IClientOutgoingPacket
{
	private final boolean activate;
	private final int minLevel;

	public ExLetterCollectorUiLauncher(boolean activate, int minLevel)
	{
		this.activate = activate;
		this.minLevel = minLevel;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeC(activate);
		packetWriter.writeD(minLevel);
	}
}
