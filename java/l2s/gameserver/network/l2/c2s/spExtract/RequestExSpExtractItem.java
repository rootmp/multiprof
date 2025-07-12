package l2s.gameserver.network.l2.c2s.spExtract;

import l2s.commons.network.PacketReader;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.actor.variables.PlayerVariables;
import l2s.gameserver.network.l2.GameClient;
import l2s.gameserver.network.l2.c2s.IClientIncomingPacket;
import l2s.gameserver.network.l2.s2c.spExtract.ExSpExtractItem;
import l2s.gameserver.utils.ItemFunctions;
import l2s.gameserver.utils.TimeUtils;

public class RequestExSpExtractItem implements IClientIncomingPacket
{
	private int _nItemID;

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		_nItemID = packet.readD();
		return true;
	}

	@Override
	public void run(GameClient client)
	{
		Player player = client.getActiveChar();
		if(player ==null)
			return;
		if(player.getSp() >= 5000000000L && player.getVarInt(PlayerVariables.SP_EXTRACT_ITEM_VAR, 0) < 5 && ItemFunctions.haveItem(player, 57, 3000000))
		{
			player.setVar(PlayerVariables.SP_EXTRACT_ITEM_VAR, player.getVarInt(PlayerVariables.SP_EXTRACT_ITEM_VAR, 0) + 1,TimeUtils.DAILY_DATE_PATTERN.next(System.currentTimeMillis()));
			player.setSp(player.getSp() - 5000000000L);
			ItemFunctions.deleteItem(player, 57, 3000000);
			ItemFunctions.addItem(player, 98232, 1);
			player.sendPacket(new ExSpExtractItem(0, 0, _nItemID));
			player.sendUserInfo();
		}
	}
}