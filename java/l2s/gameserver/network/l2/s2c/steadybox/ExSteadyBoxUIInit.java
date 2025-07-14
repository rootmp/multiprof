package l2s.gameserver.network.l2.s2c.steadybox;

import l2s.commons.network.PacketWriter;
import l2s.gameserver.Config;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.actor.variables.PlayerVariables;
import l2s.gameserver.network.l2.s2c.IClientOutgoingPacket;
import l2s.gameserver.templates.item.ItemTemplate;

/**
 * @author nexvill
 */
public class ExSteadyBoxUIInit implements IClientOutgoingPacket
{
	private Player _player;

	public ExSteadyBoxUIInit(Player player)
	{
		_player = player;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(Config.STEADY_BOX_KILL_MOBS); // mobs to kill
		packetWriter.writeD(Config.STEADY_BOX_KILL_PLAYERS); // players to kill
		packetWriter.writeD(2); // event id
		packetWriter.writeD(1600218000); // event start time (players kill)
		packetWriter.writeD(1924174800); // event end time (players kill)

		packetWriter.writeD(3);

		packetWriter.writeD(1);
		packetWriter.writeD(ItemTemplate.ITEM_ID_MONEY_L); // item id
		packetWriter.writeD(500); // count
		packetWriter.writeD(0); // ?

		packetWriter.writeD(2);
		packetWriter.writeD(ItemTemplate.ITEM_ID_MONEY_L);
		packetWriter.writeD(1500);
		packetWriter.writeD(0);

		packetWriter.writeD(3);
		packetWriter.writeD(ItemTemplate.ITEM_ID_MONEY_L);
		packetWriter.writeD(2000);
		packetWriter.writeD(0);

		packetWriter.writeD(5);

		packetWriter.writeD(0); // minutes to open
		packetWriter.writeD(ItemTemplate.ITEM_ID_MONEY_L); // item id used to reset cd
		packetWriter.writeD(100); // count
		packetWriter.writeD(0); // ?

		packetWriter.writeD(60);
		packetWriter.writeD(ItemTemplate.ITEM_ID_MONEY_L);
		packetWriter.writeD(500);
		packetWriter.writeD(0);

		packetWriter.writeD(180);
		packetWriter.writeD(ItemTemplate.ITEM_ID_MONEY_L);
		packetWriter.writeD(1000);
		packetWriter.writeD(0);

		packetWriter.writeD(360);
		packetWriter.writeD(ItemTemplate.ITEM_ID_MONEY_L);
		packetWriter.writeD(1500);
		packetWriter.writeD(0);

		packetWriter.writeD(540);
		packetWriter.writeD(ItemTemplate.ITEM_ID_MONEY_L);
		packetWriter.writeD(2000);
		packetWriter.writeD(0);

		int timeToReward = (int) ((_player.getVarLong(PlayerVariables.SB_REWARD_TIME + "_" + 1, -2) - System.currentTimeMillis()) / 1000);
		if(timeToReward < 0)
		{
			timeToReward = 0;
		}
		packetWriter.writeD(timeToReward); // timer in seconds to expire and box can open
		return true;
	}
}