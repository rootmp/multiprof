package l2s.gameserver.network.l2.c2s;
import l2s.commons.network.PacketReader;
import l2s.gameserver.network.l2.GameClient;


/**
 * format: ddS
 */
public class PetitionVote implements IClientIncomingPacket
{
	private int _type, _unk1;
	private String _petitionText;

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		_type = packet.readD();
		_unk1 = packet.readD(); // possible always zero
		_petitionText = readS(4096);
		// not done
		return true;
	}

	@Override
	public void run(GameClient client)
	{
		//
	}
}