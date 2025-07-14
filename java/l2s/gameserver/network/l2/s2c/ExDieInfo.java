package l2s.gameserver.network.l2.s2c;

import java.util.Map;

import l2s.commons.network.PacketWriter;
import l2s.gameserver.data.xml.holder.DamageHolder;
import l2s.gameserver.data.xml.holder.DroppedItemsHolder;
import l2s.gameserver.model.Player;

/**
 * @author nexvill
 */
public class ExDieInfo implements IClientOutgoingPacket
{
	private final Map<Integer, DroppedItemsHolder> _droppedItemsInfo;
	private final Map<Integer, DamageHolder> _damageInfo;
	private final Player _player;

	public ExDieInfo(Player player)
	{
		player.getObjectId();
		_droppedItemsInfo = player.getPlayer().getDroppedItemsInfo();
		_damageInfo = player.getDamageInfo();
		_player = player;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeH(_droppedItemsInfo.size());
		if(_droppedItemsInfo.size() > 0)
		{
			for(DroppedItemsHolder drop : _droppedItemsInfo.values())
			{
				packetWriter.writeD(drop.getItemId());
				packetWriter.writeD(drop.getEnchantLevel());
				packetWriter.writeD(drop.getCount());
			}
		}
		// -------------------------------------------------------------
		packetWriter.writeH(_damageInfo.size());

		for(DamageHolder dmg : _damageInfo.values())
		{
			if(dmg.getCreatureId() != 0)
			{

				packetWriter.writeH(1);
				packetWriter.writeD(dmg.getCreatureId());
			}
			else
			{
				packetWriter.writeH(0);
				packetWriter.writeS(dmg.getName());
			}
			packetWriter.writeS("");

			if(dmg.getSkill() != null)
			{
				packetWriter.writeD(dmg.getSkill().getId());
			}
			else
			{
				packetWriter.writeD(0);
			}
			packetWriter.writeF(dmg.getDamage());
			packetWriter.writeH(dmg.getType());
		}
		_player.resetDamageInfo();
		return true;
	}
}