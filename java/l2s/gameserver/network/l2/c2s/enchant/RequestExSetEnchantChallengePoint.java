package l2s.gameserver.network.l2.c2s.enchant;

import l2s.commons.network.PacketReader;
import l2s.gameserver.enums.ChallengeEffect;
import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.GameClient;
import l2s.gameserver.network.l2.c2s.IClientIncomingPacket;
import l2s.gameserver.network.l2.s2c.enchant.ExChangedEnchantTargetItemProbabilityList;
import l2s.gameserver.network.l2.s2c.enchant.ExResetEnchantItemFailRewardInfo;
import l2s.gameserver.network.l2.s2c.enchant.ExSetEnchantChallengePoint;
import l2s.gameserver.utils.ItemFunctions;


public class RequestExSetEnchantChallengePoint implements IClientIncomingPacket
{
	private int _nUseType;
	@SuppressWarnings("unused")
	private int _bUseTicket;

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
	  _nUseType = packet.readD();
	  _bUseTicket = packet.readC();
		return true;
	}

	@Override
	public void run(GameClient client)
	{
		final Player player = client.getActiveChar();
		if (player == null)
			return;

		ChallengeEffect cEff = ChallengeEffect.getValueFromIndex(_nUseType);
		if(cEff == null)
			return;
		
		if(player.getEnchantItem()!=null && player.getEnchantItem().getEnchantLevel()>= cEff.getMinEnchant() && player.getEnchantItem().getEnchantLevel()<= cEff.getMaxEnchant())
		{
			player.setEnchantChallengePoint(cEff);
			player.sendPacket(ExSetEnchantChallengePoint.SUCCESS);
			player.sendPacket(new ExChangedEnchantTargetItemProbabilityList(ItemFunctions.getEnchantProbInfo(player, false, false)));
			player.sendPacket(new ExResetEnchantItemFailRewardInfo(player, player.getEnchantItem().getObjectId()));
		} else
			player.sendPacket(ExSetEnchantChallengePoint.FAIL);
	}
}