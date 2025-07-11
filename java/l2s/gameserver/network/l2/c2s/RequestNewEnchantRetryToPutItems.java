package l2s.gameserver.network.l2.c2s;

import l2s.commons.network.PacketReader;
import l2s.dataparser.data.holder.SynthesisHolder;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.items.ItemInstance;
import l2s.gameserver.network.l2.GameClient;
import l2s.gameserver.network.l2.s2c.ExEnchantRetryToPutItemFail;
import l2s.gameserver.network.l2.s2c.ExEnchantRetryToPutItemOk;
import l2s.gameserver.network.l2.s2c.SystemMessage;
import l2s.gameserver.templates.item.support.SynthesisData;

public class RequestNewEnchantRetryToPutItems implements IClientIncomingPacket
{
	private int _firstItemObjectId;
	private int _secondItemObjectId;

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		_firstItemObjectId = packet.readD();
		_secondItemObjectId = packet.readD();
		return true;
	}

	@Override
	public void run(GameClient client)
	{
		Player player = client.getActiveChar();
		if(player == null)
		{ return; }

		if(player.isInStoreMode())
		{
			player.setSynthesisItem1(null);
			player.setSynthesisItem2(null);
			client.sendPacket(new SystemMessage(SystemMessage.YOU_CANNOT_DO_THAT_WHILE_IN_A_PRIVATE_STORE_OR_PRIVATE_WORKSHOP));
			client.sendPacket(ExEnchantRetryToPutItemFail.STATIC_PACKET);
			return;
		}

		final ItemInstance item1 = player.getInventory().getItemByObjectId(_firstItemObjectId);
		if(item1 == null)
		{
			player.setSynthesisItem1(null);
			player.setSynthesisItem2(null);
			client.sendPacket(ExEnchantRetryToPutItemFail.STATIC_PACKET);
			return;
		}

		final ItemInstance item2 = player.getInventory().getItemByObjectId(_secondItemObjectId);
		if(item2 == null)
		{
			player.setSynthesisItem1(null);
			player.setSynthesisItem2(null);
			client.sendPacket(ExEnchantRetryToPutItemFail.STATIC_PACKET);
			return;
		}

		player.setSynthesisItem1(item1);
		player.setSynthesisItem2(item2);

		SynthesisData data = null;
		for(SynthesisData d : SynthesisHolder.getInstance().getDatas())
		{
			if(item1.getItemId() == d.getItem1Id() && item2.getItemId() == d.getItem2Id())
			{
				data = d;
				break;
			}

			if(item1.getItemId() == d.getItem2Id() && item2.getItemId() == d.getItem1Id())
			{
				data = d;
				break;
			}
		}

		if(data == null)
		{
			player.setSynthesisItem1(null);
			player.setSynthesisItem2(null);
			client.sendPacket(ExEnchantRetryToPutItemFail.STATIC_PACKET);
			return;
		}

		client.sendPacket(ExEnchantRetryToPutItemOk.STATIC_PACKET);
		player.getGuarantedSynthesis().sendInfo(item1.getItemId(),item2.getItemId(), data.getSuccessItemData().getChance());
	}
}
