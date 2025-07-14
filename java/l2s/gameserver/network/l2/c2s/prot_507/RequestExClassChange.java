package l2s.gameserver.network.l2.c2s.prot_507;

import l2s.commons.network.PacketReader;
import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.GameClient;
import l2s.gameserver.network.l2.c2s.IClientIncomingPacket;
import l2s.gameserver.network.l2.s2c.prot_507.ExClassChangeFail;
import l2s.gameserver.utils.ItemFunctions;

public class RequestExClassChange implements IClientIncomingPacket
{
	private int nClass;
	private int nRace;
	private int nSex;
	@SuppressWarnings("unused")
	private int nJobGroup;
	private boolean bExtractSkill;
	private int nCommissionId;

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		nClass = packet.readD();
		nRace = packet.readD();
		nSex = packet.readD();
		nJobGroup = packet.readD();
		bExtractSkill = packet.readC() == 1;
		nCommissionId = packet.readD();
		return true;
	}

	@Override
	public void run(GameClient client) throws Exception
	{
		final Player player = client.getActiveChar();
		if(player == null)
			return;

		if(player.isInCombat() || player.isDead())
		{
			player.sendPacket(new ExClassChangeFail());
			return;
		}

		if(player.isInOlympiadMode() || player.getOlympiadGame() != null)
		{
			player.sendPacket(new ExClassChangeFail());
			return;
		}

		/*	if (NpcAI.GetInventoryInfo(player, NpcAI.IPT_MAX_SLOT_COUNT) - NpcAI.GetInventoryInfo(player, NpcAI.IPT_CURRENT_SLOT_COUNT) < 30
			    || NpcAI.GetInventoryInfo(player, NpcAI.IPT_CURRENT_WEIGHT) >= (NpcAI.GetInventoryInfo(player, NpcAI.IPT_MAX_CARRY_WEIGHT) * 0.5)
			    || NpcAI.GetInventoryInfo(player, NpcAI.IPT_CURRENT_EQUIP_SLOT) != 0)
			{
				player.sendPacket(new ExClassChangeFail());
				return;
			}*/

		if(!ItemFunctions.haveItem(player, 103209, 1))
		{
			player.sendPacket(new ExClassChangeFail());
			return;
		}

		if(nCommissionId > 0 && nCommissionId != 103060)
		{
			player.sendPacket(new ExClassChangeFail());
			return;
		}

		if(nCommissionId > 0 && !ItemFunctions.haveItem(player, nCommissionId, 3))
		{
			player.sendPacket(new ExClassChangeFail());
			return;
		}

		/*if (!ItemFunctions.deleteItem(player, 103209, 3, "ClassChange"))
		{
			player.sendPacket(new ExClassChangeFail());
			return;
		}
		
		if (nCommissionId > 0)
			ItemFunctions.deleteItem(player, nCommissionId, 1, "ClassChange");
		
		AiUtils.ApplyClassChange(player, nRace, nSex, nClass, bExtractSkill);*/
	}
}
