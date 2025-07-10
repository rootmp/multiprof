package l2s.gameserver.network.l2.s2c;
import l2s.commons.network.PacketWriter;

/**
 * @author VISTALL
 */
public class ExPVPMatchUserDie implements IClientOutgoingPacket
{
	private int _blueKills, _redKills;

	public ExPVPMatchUserDie(int blueKills, int redKills)
	{
		_blueKills = blueKills;
		_redKills = redKills;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(_blueKills);
		packetWriter.writeD(_redKills);
	}
}