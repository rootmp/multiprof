package l2s.gameserver.network.l2.s2c;
import l2s.commons.network.PacketWriter;

public class StartAllianceWar implements IClientOutgoingPacket
{
	private String _allianceName;
	private String _char;

	public StartAllianceWar(String alliance, String charName)
	{
		_allianceName = alliance;
		_char = charName;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeS(_char);
		packetWriter.writeS(_allianceName);
		return true;
	}
}