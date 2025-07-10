package l2s.gameserver.network.l2.c2s;
import l2s.commons.network.PacketReader;
import l2s.gameserver.network.l2.GameClient;


import l2s.commons.dao.JdbcEntityState;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.base.Element;
import l2s.gameserver.model.items.ItemInstance;
import l2s.gameserver.network.l2.components.SystemMsg;
import l2s.gameserver.network.l2.s2c.ExChangeAttributeFail;
import l2s.gameserver.network.l2.s2c.ExChangeAttributeOk;
import l2s.gameserver.network.l2.s2c.InventoryUpdatePacket;
import l2s.gameserver.network.l2.s2c.SystemMessagePacket;

/**
 * @author Bonux
 */
public class RequestChangeAttributeItem implements IClientIncomingPacket
{
	public int _consumeItemId;
	public int _itemObjId;
	public int _newElementId;

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		_consumeItemId = packet.readD(); // Change Attribute Crystall ID
		_itemObjId = packet.readD(); // Item for Change ObjId
		_newElementId = packet.readD(); // Element
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

		if (activeChar.getPrivateStoreType() != Player.STORE_PRIVATE_NONE)
		{
			activeChar.sendPacket(SystemMsg.YOU_CAN_NOT_CHANGE_THE_ATTRIBUTE_WHILE_OPERATING_A_PRIVATE_STORE_OR_PRIVATE_WORKSHOP);
			activeChar.sendPacket(ExChangeAttributeFail.STATIC);
			return;
		}

		if (activeChar.isInTrade())
		{
			activeChar.sendActionFailed();
			return;
		}

		ItemInstance item = activeChar.getInventory().getItemByObjectId(_itemObjId);
		if (item == null || !item.isWeapon())
		{
			activeChar.sendPacket(SystemMsg.UNABLE_TO_CHANCE_THE_ATTRIBUTE);
			activeChar.sendPacket(ExChangeAttributeFail.STATIC);
			return;
		}

		if (!activeChar.getInventory().destroyItemByItemId(_consumeItemId, 1L))
		{
			activeChar.sendActionFailed();
			return;
		}

		Element oldElement = item.getAttackElement();
		int elementVal = item.getAttributeElementValue(oldElement, false);
		item.setAttributeElement(oldElement, 0);

		Element newElement = Element.getElementById(_newElementId);
		item.setAttributeElement(newElement, item.getAttributeElementValue(newElement, false) + elementVal);

		item.setJdbcState(JdbcEntityState.UPDATED);
		item.update();

		activeChar.getInventory().refreshEquip(item);

		SystemMessagePacket msg = new SystemMessagePacket(SystemMsg.IN_THE_ITEM_S1_ATTRIBUTE_S2_SUCCESSFULLY_CHANGED_TO_S3);
		msg.addName(item);
		msg.addElementName(oldElement);
		msg.addElementName(newElement);
		activeChar.sendPacket(msg);
		activeChar.sendPacket(new InventoryUpdatePacket().addModifiedItem(activeChar, item));
		activeChar.sendPacket(ExChangeAttributeOk.STATIC);
		activeChar.updateStats();
	}
}
