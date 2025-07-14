package l2s.gameserver.network.l2.c2s.newhenna;

import l2s.commons.network.PacketReader;
import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.GameClient;
import l2s.gameserver.network.l2.c2s.IClientIncomingPacket;
import l2s.gameserver.network.l2.s2c.HennaUnequipInfoPacket;
import l2s.gameserver.templates.item.henna.Henna;

public class RequestExHennaUnEquipInfo implements IClientIncomingPacket
{
	private int _id;

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		_id = packet.readD();
		return true;
	}

	@Override
	public void run(GameClient client) throws Exception
	{
		final Player player = client.getActiveChar();
		if(player == null)
			return;
		Henna henna = null;

		for(int slot = 1; slot <= 4; ++slot)
		{
			if(player.getHenna(slot) != null)
			{
				if(player.getHenna(slot).getDyeId() == this._id)
				{
					henna = player.getHenna(slot);
					break;
				}
			}
		}
		if(henna == null)
			return;

		player.sendPacket(new HennaUnequipInfoPacket(henna, player));
	}

}
