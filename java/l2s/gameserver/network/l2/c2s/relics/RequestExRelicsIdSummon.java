package l2s.gameserver.network.l2.c2s.relics;

import java.util.Collections;

import l2s.commons.network.PacketReader;
import l2s.gameserver.data.xml.holder.RelicHolder;
import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.GameClient;
import l2s.gameserver.network.l2.c2s.IClientIncomingPacket;
import l2s.gameserver.network.l2.components.SystemMsg;
import l2s.gameserver.network.l2.s2c.relics.ExRelicsSummonResult;
import l2s.gameserver.templates.relics.RelicsSummonInfo;
import l2s.gameserver.utils.ItemFunctions;
import l2s.gameserver.utils.TimeUtils;

public class RequestExRelicsIdSummon implements IClientIncomingPacket
{
	private int nSummonID;
	private int nCommisionID;

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		nSummonID = packet.readD();
		nCommisionID = packet.readD();
		return true;
	}

	@Override
	public void run(GameClient client) throws Exception
	{
		final Player player = client.getActiveChar();
		if(player == null)
			return;
		RelicsSummonInfo info = RelicHolder.getInstance().getSummonInfo(nSummonID);
		if(info != null && info.getItemId() == nCommisionID)
		{
			int limit = player.getVarInt("RelicsSummonLimit_" + info.getSummonId(), info.getDailyLimit());
			if(limit < 1)
			{
				player.sendPacket(new ExRelicsSummonResult(0, 0, Collections.emptyList()));
				return;
			}
			if(!ItemFunctions.deleteItem(player, info.getItemId(), info.getPrice(), true))
			{
				player.sendPacket(SystemMsg.INCORRECT_ITEM_COUNT);
				return;
			}
			player.getRelics().summonRelics(info.getCount(), info.getRelicProbs());
			player.setVar("RelicsSummonLimit_" + info.getSummonId(), limit - 1, TimeUtils.DAILY_DATE_PATTERN.next(System.currentTimeMillis()));
		}
	}
}
