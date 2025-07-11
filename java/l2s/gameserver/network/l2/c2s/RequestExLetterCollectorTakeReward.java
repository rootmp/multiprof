package l2s.gameserver.network.l2.c2s;
import l2s.commons.network.PacketReader;
import l2s.gameserver.listener.actor.OnActorAct;
import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.GameClient;

public class RequestExLetterCollectorTakeReward implements IClientIncomingPacket
{
	private int letterSetId;

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		letterSetId = packet.readD();
		return true;
	}

	@Override
	public void run(GameClient client)
	{
		Player player = client.getActiveChar();
		if (player == null)
			return;

		player.getListeners().onAct(OnActorAct.EX_LETTER_COLLECTOR_TAKE_REWARD, letterSetId);
	}
}
