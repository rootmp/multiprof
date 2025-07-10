package l2s.gameserver.network.l2.s2c;
import l2s.commons.network.PacketWriter;

public class PledgeReceiveUpdatePower implements IClientOutgoingPacket
{
	private int _privs;

	public PledgeReceiveUpdatePower(int privs)
	{
		_privs = privs;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(_privs); // Filler??????
	}
}