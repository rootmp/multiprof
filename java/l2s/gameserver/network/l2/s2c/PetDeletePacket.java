package l2s.gameserver.network.l2.s2c;
import l2s.commons.network.PacketWriter;

public class PetDeletePacket implements IClientOutgoingPacket
{
	private int _petId;
	private int _petnum;

	public PetDeletePacket(int petId, int petnum)
	{
		_petId = petId;
		_petnum = petnum;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(_petnum);
		packetWriter.writeD(_petId);
	}
}