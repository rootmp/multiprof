package l2s.gameserver.network.l2.c2s.newhenna;

import java.util.Map.Entry;

import l2s.commons.network.PacketReader;
import l2s.commons.util.Rnd;
import l2s.gameserver.dao.CharacterHennaDAO;
import l2s.gameserver.data.xml.holder.HennaPatternPotentialDataHolder;
import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.GameClient;
import l2s.gameserver.network.l2.c2s.IClientIncomingPacket;
import l2s.gameserver.network.l2.s2c.ExAdenaInvenCount;
import l2s.gameserver.network.l2.s2c.newhenna.NewHennaPotenEnchant;
import l2s.gameserver.templates.item.data.ItemData;
import l2s.gameserver.templates.item.henna.DyePotentialFee;
import l2s.gameserver.templates.item.henna.HennaPoten;
import l2s.gameserver.utils.ItemFunctions;

public class RequestExNewHennaPotenEnchant implements IClientIncomingPacket
{
	private int _slotId;
	private int _costItemID;

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		_slotId = packet.readC();
		_costItemID = packet.readD(); //TODO 388
		return true;
	}

	@Override
	public void run(GameClient client) throws Exception
	{
		final Player player = client.getActiveChar();
		if(player == null)
			return;

		if((_slotId < 1) || (_slotId > 4))
			return;

		int dailyStep = player.getDyePotentialDailyStep();
		final DyePotentialFee currentFee = HennaPatternPotentialDataHolder.getInstance().getFee(dailyStep);
		int dailyCount = player.getDyePotentialDailyCount();

		if((currentFee == null) || (dailyCount <= 0))
			return;
		final HennaPoten poten = player.getHennaPoten(_slotId);

		ItemData _costItem = currentFee.getItem(_costItemID);

		if(_costItem == null || !ItemFunctions.deleteItem(player, _costItem.getId(), _costItem.getCount()))
			return;

		dailyCount -= 1;
		if((dailyCount <= 0) && (dailyStep != HennaPatternPotentialDataHolder.getInstance().getMaxPotenEnchantStep()))
		{
			player.setDyePotentialDailyCount(dailyCount);
		}
		else
		{
			player.setDyePotentialDailyCount(dailyCount);
		}
		double totalChance = 0;
		double random = Rnd.nextDouble() * 100;
		for(Entry<Integer, Double> entry : currentFee.getEnchantExp().entrySet())
		{
			totalChance += entry.getValue();
			if(random <= totalChance)
			{

				final int increase = entry.getKey();
				int newEnchantExp = poten.getEnchantExp() + increase;
				final int tatooExpNeeded = HennaPatternPotentialDataHolder.getInstance().getExpForLevel(poten.getEnchantLevel());
				if(newEnchantExp >= tatooExpNeeded)
				{
					newEnchantExp -= tatooExpNeeded;
					if(poten.getEnchantLevel() < 30 && poten.getEnchantLevel() < HennaPatternPotentialDataHolder.getInstance().getMaxPotenLevel())
					{
						poten.setEnchantLevel(poten.getEnchantLevel() + 1);
					}
				}
				if(poten.getEnchantLevel() == 30)
				{
					if(poten.getEnchantExp() + increase >= 2500)
						poten.setEnchantExp(2500);
					else
						poten.setEnchantExp(poten.getEnchantExp() + increase);
				}
				else
					poten.setEnchantExp(newEnchantExp);

				player.getListeners().onPlayerNewHennaPotenEnchant(_slotId, poten.getEnchantLevel() - 1, poten.getEnchantLevel());

				CharacterHennaDAO.getInstance().storeDyePoten(player, poten, _slotId);
				player.sendPacket(new NewHennaPotenEnchant(_slotId, poten.getEnchantLevel(), poten.getEnchantExp(), dailyStep, dailyCount, poten.getActiveStep(), true));

				player.applyDyePotenSkills();
				//player.sendPacket(new NewHennaList(player,0));
				player.sendPacket(new ExAdenaInvenCount(player));
				return;
			}
		}
	}

}
