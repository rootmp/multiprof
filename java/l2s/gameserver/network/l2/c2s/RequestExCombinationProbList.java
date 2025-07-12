package l2s.gameserver.network.l2.c2s;

import l2s.commons.network.PacketReader;
import l2s.dataparser.data.holder.SynthesisHolder;
import l2s.dataparser.data.holder.synthesis.SynthesisData;
import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.GameClient;
import l2s.gameserver.network.l2.s2c.ExCombinationProbList;

public class RequestExCombinationProbList implements IClientIncomingPacket
{
	private int nOneSlotServerID;
	private int nTwoSlotServerID;

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		nOneSlotServerID = packet.readD();
		nTwoSlotServerID = packet.readD();
		return true;
	}

	@Override
	public void run(GameClient client) throws Exception
	{
		Player player = client.getActiveChar();
		if(player == null)
			return;
		
		SynthesisData data = SynthesisHolder.getInstance().getSynthesisData(player, nOneSlotServerID,nTwoSlotServerID);
		if(data!=null)
		{
			double chance = data.getSuccessItemData().getChance()*10000;
			player.sendPacket(new ExCombinationProbList(nOneSlotServerID, nTwoSlotServerID, (int) chance));
		}
	}

}
