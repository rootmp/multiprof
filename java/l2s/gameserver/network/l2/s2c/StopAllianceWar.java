package l2s.gameserver.network.l2.s2c;
import l2s.commons.network.PacketWriter;

public class StopAllianceWar implements IClientOutgoingPacket
{
	private String _allianceName;
	private String _char;

	public StopAllianceWar(String alliance, String charName)
	{
		_allianceName = alliance;
		_char = charName;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeS(_allianceName);
		packetWriter.writeS(_char);
	}
}