package l2s.gameserver.network.l2.c2s.steadybox;

import l2s.commons.network.PacketReader;
import l2s.commons.util.Rnd;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.actor.variables.PlayerVariables;
import l2s.gameserver.network.l2.GameClient;
import l2s.gameserver.network.l2.c2s.IClientIncomingPacket;
import l2s.gameserver.network.l2.s2c.steadybox.ExSteadyBoxReward;
import l2s.gameserver.network.l2.s2c.steadybox.ExSteadyOneBoxUpdate;

/**
 * @author nexvill
 */
public class RequestExSteadyGetReward implements IClientIncomingPacket
{
	private int _slotId;

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		_slotId = packet.readD();
		return true;
	}

	@Override
	public void run(GameClient client)
	{
		Player activeChar = client.getActiveChar();
		if(activeChar == null)
			return;

		int boxType = activeChar.getVarInt(PlayerVariables.SB_BOX_TYPE + "_" + _slotId, 1);
		takeReward(activeChar, boxType);

		activeChar.sendPacket(new ExSteadyOneBoxUpdate(activeChar, _slotId, true, false));
	}

	// TODO: move it in datapack
	private void takeReward(Player player, int boxType)
	{
		int chance = Rnd.get(10000);
		int itemId = 0;
		int itemCount = 1;

		if(chance > 7000)
		{
			itemId = 3031;
			if(boxType == 1)
			{
				itemCount = 50;
				player.getInventory().addItem(itemId, itemCount); // Spirit Ore
			}
			else if(boxType == 2)
			{
				itemCount = 100;
				player.getInventory().addItem(itemId, itemCount);
			}
			else
			{
				itemCount = 200;
				player.getInventory().addItem(itemId, itemCount);
			}
		}
		else if(chance > 4000)
		{
			itemId = 90907;
			if(boxType == 1)
			{
				itemCount = 250;
				player.getInventory().addItem(itemId, itemCount); // Soulshot Ticket
			}
			else if(boxType == 2)
			{
				itemCount = 500;
				player.getInventory().addItem(itemId, itemCount);
			}
			else
			{
				itemCount = 1000;
				player.getInventory().addItem(itemId, itemCount);
			}
		}
		else if(chance > 100)
		{
			itemId = 93274;
			if(boxType == 1)
			{
				itemCount = 5;
				player.getInventory().addItem(itemId, itemCount); // Sayha's Cookie
			}
			else if(boxType == 2)
			{
				itemCount = 10;
				player.getInventory().addItem(itemId, itemCount);
			}
			else
			{
				itemCount = 20;
				player.getInventory().addItem(itemId, itemCount);
			}
		}
		else if(chance > 95)
		{
			itemId = 72084;
			player.getInventory().addItem(itemId, 1); // Devoted Hero's Circlet(1-day)
		}
		else if(chance > 90)
		{
			itemId = 72085;
			player.getInventory().addItem(itemId, 1); // Devoted Hero's Helmet(1-day)
		}
		else if(chance > 85)
		{
			itemId = 72086;
			player.getInventory().addItem(itemId, 1); // Devoted Hero's Gauntlets(1-day)
		}
		else if(chance > 80)
		{
			itemId = 72087;
			player.getInventory().addItem(itemId, 1); // Devoted Hero's Gloves(1-day)
		}
		else if(chance > 75)
		{
			itemId = 72088;
			player.getInventory().addItem(itemId, 1); // Devoted Hero's Boots(1-day)
		}
		else if(chance > 70)
		{
			itemId = 72089;
			player.getInventory().addItem(itemId, 1); // Devoted Hero's Pendant(1-day)
		}
		else if(chance > 65)
		{
			itemId = 72090;
			player.getInventory().addItem(itemId, 1); // Devoted Hero's Ring - Queen Ant(1-day)
		}
		else if(chance > 70)
		{
			itemId = 72091;
			player.getInventory().addItem(itemId, 1); // Devoted Hero's Necklace - Frintezza(1-day)
		}
		else if(chance > 65)
		{
			itemId = 72092;
			player.getInventory().addItem(itemId, 1); // Devoted Hero's Earring - Zaken(1-day)
		}
		else if(chance > 60)
		{
			itemId = 72093;
			player.getInventory().addItem(itemId, 1); // Devoted Hero's Ring - Baium(1-day)
		}
		else if(chance > 55)
		{
			itemId = 72094;
			player.getInventory().addItem(itemId, 1); // Devoted Hero's Earring - Orfen(1-day)
		}
		else if(chance > 50)
		{
			itemId = 72095;
			player.getInventory().addItem(itemId, 1); // Devoted Hero's Ring - Core(1-day)
		}
		else if(chance > 45)
		{
			itemId = 72096;
			player.getInventory().addItem(itemId, 1); // Devoted Hero's Belt(1-day)
		}
		else if(chance > 40)
		{
			itemId = 72097;
			player.getInventory().addItem(itemId, 1); // Devoted Hero's Shield(1-day)
		}
		else if(chance > 35)
		{
			itemId = 72098;
			player.getInventory().addItem(itemId, 1); // Devoted Hero's Sigil(1-day)
		}
		else if(chance > 30)
		{
			itemId = 72099;
			player.getInventory().addItem(itemId, 1); // Devoted Hero's Doll - Queen Ant(1-day)
		}
		else if(chance > 25)
		{
			itemId = 72100;
			player.getInventory().addItem(itemId, 1); // Devoted Hero's Doll - Baium(1-day)
		}
		else if(chance > 20)
		{
			itemId = 72101;
			player.getInventory().addItem(itemId, 1); // Devoted Hero's Doll - Orfen(1-day)
		}
		else
		{
			itemId = 72102;
			player.getInventory().addItem(itemId, 1); // Devoted Hero's Doll - Frintezza(1-day)
		}

		player.sendPacket(new ExSteadyBoxReward(_slotId, itemId, itemCount));
	}
}
