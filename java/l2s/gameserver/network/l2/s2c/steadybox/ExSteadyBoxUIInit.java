package l2s.gameserver.network.l2.s2c.steadybox;

import l2s.gameserver.Config;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.actor.variables.PlayerVariables;
import l2s.gameserver.network.l2.s2c.L2GameServerPacket;
import l2s.gameserver.templates.item.ItemTemplate;

/**
 * @author nexvill
 */
public class ExSteadyBoxUIInit extends L2GameServerPacket
{
	private Player _player;

	public ExSteadyBoxUIInit(Player player)
	{
		_player = player;
	}

	@Override
	protected final void writeImpl()
	{
		writeD(Config.STEADY_BOX_KILL_MOBS); // mobs to kill
		writeD(Config.STEADY_BOX_KILL_PLAYERS); // players to kill
		writeD(2); // event id
		writeD(1600218000); // event start time (players kill)
		writeD(1924174800); // event end time (players kill)

		writeD(3);

		writeD(1);
		writeD(ItemTemplate.ITEM_ID_MONEY_L); // item id
		writeD(500); // count
		writeD(0); // ?

		writeD(2);
		writeD(ItemTemplate.ITEM_ID_MONEY_L);
		writeD(1500);
		writeD(0);

		writeD(3);
		writeD(ItemTemplate.ITEM_ID_MONEY_L);
		writeD(2000);
		writeD(0);

		writeD(5);

		writeD(0); // minutes to open
		writeD(ItemTemplate.ITEM_ID_MONEY_L); // item id used to reset cd
		writeD(100); // count
		writeD(0); // ?

		writeD(60);
		writeD(ItemTemplate.ITEM_ID_MONEY_L);
		writeD(500);
		writeD(0);

		writeD(180);
		writeD(ItemTemplate.ITEM_ID_MONEY_L);
		writeD(1000);
		writeD(0);

		writeD(360);
		writeD(ItemTemplate.ITEM_ID_MONEY_L);
		writeD(1500);
		writeD(0);

		writeD(540);
		writeD(ItemTemplate.ITEM_ID_MONEY_L);
		writeD(2000);
		writeD(0);

		int timeToReward = (int) ((_player.getVarLong(PlayerVariables.SB_REWARD_TIME + "_" + 1, -2) - System.currentTimeMillis()) / 1000);
		if (timeToReward < 0)
		{
			timeToReward = 0;
		}
		writeD(timeToReward); // timer in seconds to expire and box can open
	}
}