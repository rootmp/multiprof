package l2s.gameserver.network.l2.c2s;
import l2s.commons.network.PacketReader;
import l2s.gameserver.network.l2.GameClient;


import l2s.gameserver.model.Player;
import l2s.gameserver.model.items.ManufactureItem;
import l2s.gameserver.network.l2.s2c.RecipeShopItemInfoPacket;

public class RequestRecipeShopMakeInfo implements IClientIncomingPacket
{
	private int _manufacturerId;
	private int _recipeId;

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		_manufacturerId = packet.readD();
		_recipeId = packet.readD();
		return true;
	}

	@Override
	public void run(GameClient client)
	{
		Player activeChar = client.getActiveChar();
		if (activeChar == null)
			return;

		if (activeChar.isActionsDisabled())
		{
			activeChar.sendActionFailed();
			return;
		}

		Player manufacturer = (Player) activeChar.getVisibleObject(_manufacturerId);
		if (manufacturer == null || manufacturer.getPrivateStoreType() != Player.STORE_PRIVATE_MANUFACTURE || !manufacturer.checkInteractionDistance(activeChar))
		{
			activeChar.sendActionFailed();
			return;
		}

		long price = -1;
		for (ManufactureItem i : manufacturer.getCreateList().values())
			if (i.getRecipeId() == _recipeId)
			{
				price = i.getCost();
				break;
			}

		if (price == -1)
		{
			activeChar.sendActionFailed();
			return;
		}

		activeChar.sendPacket(new RecipeShopItemInfoPacket(activeChar, manufacturer, _recipeId, price, 0xFFFFFFFF));
	}
}