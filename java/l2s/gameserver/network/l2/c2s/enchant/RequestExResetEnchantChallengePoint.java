package l2s.gameserver.network.l2.c2s.enchant;

import l2s.commons.network.PacketReader;
import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.GameClient;
import l2s.gameserver.network.l2.c2s.IClientIncomingPacket;
import l2s.gameserver.network.l2.s2c.enchant.ExChangedEnchantTargetItemProbabilityList;
import l2s.gameserver.network.l2.s2c.enchant.ExResetEnchantChallengePoint;
import l2s.gameserver.utils.ItemFunctions;

public class RequestExResetEnchantChallengePoint implements IClientIncomingPacket
{

	private int _cDummy;

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		_cDummy = packet.readC();
		return true;
	}

	@Override
	public void run(GameClient client)
	{
		final Player player = client.getActiveChar();
		if(player == null)
			return;
		if(_cDummy == 0)
		{
			player.setEnchantChallengePoint(null);
			player.sendPacket(ExResetEnchantChallengePoint.SUCCESS);
			player.sendPacket(new ExChangedEnchantTargetItemProbabilityList(ItemFunctions.getEnchantProbInfo(player, false, false)));
		}
	}
}