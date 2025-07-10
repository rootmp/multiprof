package l2s.gameserver.network.l2.c2s;

import l2s.gameserver.model.Player;
import l2s.gameserver.model.items.ItemInstance;
import l2s.gameserver.utils.ChatUtils;
import l2s.gameserver.utils.HtmlUtils;

public class UseItem extends L2GameClientPacket
{
	private int _objectId;
	private boolean _ctrlPressed;

	@Override
	protected boolean readImpl()
	{
		_objectId = readD();
		_ctrlPressed = readD() == 1;
		return true;
	}

	@Override
	protected void runImpl()
	{
		Player activeChar = getClient().getActiveChar();
		if (activeChar == null) {
			return;
		}
		
		activeChar.setActive();

		ItemInstance item = activeChar.getInventory().getItemByObjectId(_objectId);
		if (item == null)
		{
			activeChar.sendActionFailed();
			return;
		}

		if (_ctrlPressed)
		{
			if (activeChar.isGM())
			{
				ChatUtils.sys(activeChar, "Subject ID:" + item.getItemId() + "| Name" + item.getName());
			}
			if (item.isWeapon() || item.isArmor() || item.isAccessory())
			{
				boolean hasRestrictions = false;

				StringBuilder sb = new StringBuilder();
				sb.append("<font color=LEVEL>Limits:</font>").append("<br1>");
				if ((item.getCustomFlags() & ItemInstance.FLAG_NO_DROP) == ItemInstance.FLAG_NO_DROP)
				{
					sb.append("You can't throw away").append("<br1>");
					hasRestrictions = true;
				}
				if ((item.getCustomFlags() & ItemInstance.FLAG_NO_TRADE) == ItemInstance.FLAG_NO_TRADE)
				{
					sb.append("Cannot be sold/exchanged").append("<br1>");
					hasRestrictions = true;
				}
				if ((item.getCustomFlags() & ItemInstance.FLAG_NO_TRANSFER) == ItemInstance.FLAG_NO_TRANSFER)
				{
					sb.append("Cannot be stored").append("<br1>");
					hasRestrictions = true;
				}
				if ((item.getCustomFlags() & ItemInstance.FLAG_NO_CRYSTALLIZE) == ItemInstance.FLAG_NO_CRYSTALLIZE)
				{
					sb.append("Can't crystallize").append("<br1>");
					hasRestrictions = true;
				}
				if ((item.getCustomFlags() & ItemInstance.FLAG_NO_SHAPE_SHIFTING) == ItemInstance.FLAG_NO_SHAPE_SHIFTING)
				{
					sb.append("Cannot be processed").append("<br1>");
					hasRestrictions = true;
				}

				if (hasRestrictions)
				{
					HtmlUtils.sendHtm(activeChar, sb.toString());
					return;
				}
			}
		}

		activeChar.useItem(item, _ctrlPressed, true);
	}
}