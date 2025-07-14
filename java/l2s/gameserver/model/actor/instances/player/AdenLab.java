package l2s.gameserver.model.actor.instances.player;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import l2s.commons.util.Rnd;
import l2s.gameserver.Config;
import l2s.gameserver.dao.CharacterAdenLabDAO;
import l2s.gameserver.data.xml.holder.AdenLabDataHolder;
import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.s2c.adenlab.ExAdenlabBossInfo;
import l2s.gameserver.network.l2.s2c.adenlab.ExAdenlabBossList;
import l2s.gameserver.network.l2.s2c.adenlab.ExAdenlabNormalPlay;
import l2s.gameserver.network.l2.s2c.adenlab.ExAdenlabNormalSlot;
import l2s.gameserver.network.l2.s2c.adenlab.ExAdenlabSpecialFix;
import l2s.gameserver.network.l2.s2c.adenlab.ExAdenlabSpecialPlay;
import l2s.gameserver.network.l2.s2c.adenlab.ExAdenlabSpecialProb;
import l2s.gameserver.network.l2.s2c.adenlab.ExAdenlabSpecialSlot;
import l2s.gameserver.network.l2.s2c.adenlab.ExAdenlabTranscendEnchant;
import l2s.gameserver.network.l2.s2c.adenlab.ExAdenlabTranscendProb;
import l2s.gameserver.network.l2.s2c.adenlab.ExAdenlabUnlockBoss;
import l2s.gameserver.templates.adenLab.AdenLabData;
import l2s.gameserver.templates.adenLab.AdenLabStageTemplate;
import l2s.gameserver.templates.adenLab.CardselectOptionData;
import l2s.gameserver.utils.ItemFunctions;

public class AdenLab
{
	private Player player;
	private Map<Integer, AdenLabData> _adenLab;
	private int[] temp_special_options = new int[2];

	public AdenLab(Player player)
	{
		this.player = player;
		_adenLab = CharacterAdenLabDAO.getInstance().load(player.getObjectId());
		updateStats();
	}

	public void getBossInfo(int nBossID)
	{
		AdenLabData data = _adenLab.get(nBossID);
		if(data == null)
			player.sendPacket(new ExAdenlabBossInfo(nBossID, 1, 0, 0, 0, Collections.emptyMap()));
		else
			player.sendPacket(new ExAdenlabBossInfo(nBossID, data.getCurrentSlot(), 0, data.getNormalGameSaleDailyCount(), data.getNormalGameDailyCount(), data.getSpecialSlots()));
	}

	public void getNormalSlot(int nBossID, int nSlotID)
	{
		AdenLabData data = _adenLab.computeIfAbsent(nBossID, AdenLabData::new);

		int currentSlot = data.getCurrentSlot();

		if(nSlotID != currentSlot)
			return;

		AdenLabStageTemplate slot = AdenLabDataHolder.getInstance().getSlot(nSlotID);
		if(slot == null)
			return;

		int totalCards = slot.getCardCount();
		int openedCards = data.getOpenCards();

		int remainingCards = totalCards - openedCards;

		player.sendPacket(new ExAdenlabNormalSlot(nBossID, nSlotID, remainingCards));
	}

	public void normalPlay(int nBossID, int nSlotID, int nFeeIndex)
	{
		AdenLabData data = _adenLab.computeIfAbsent(nBossID, AdenLabData::new);

		if(nSlotID != data.getCurrentSlot())
			return;
		AdenLabStageTemplate slot = AdenLabDataHolder.getInstance().getSlot(nSlotID);
		if(slot == null)
			return;

		int need_item = Config.ADENLAB_RESEARCH_DIARY[nFeeIndex];
		if(!ItemFunctions.haveItem(player, need_item, 1))
			return;
		if(!ItemFunctions.haveItem(player, 57, Config.ADENLAB_ADENA_PLAY))
			return;

		if(!ItemFunctions.deleteItem(player, need_item, 1))
			return;
		if(!ItemFunctions.deleteItem(player, 57, Config.ADENLAB_ADENA_PLAY))
			return;

		int openedCards = data.getOpenCards();
		int totalCards = slot.getCardCount();

		if(openedCards >= totalCards)
			return;

		boolean success = (Rnd.get(totalCards - openedCards) == 0);

		data.setOpenCards(openedCards + 1);

		if(success)
		{
			data.setCurrentSlot(nSlotID + 1);
			data.setOpenCards(0);
			updateStats();
		}
		CharacterAdenLabDAO.getInstance().save(player.getObjectId(), data);
		player.sendPacket(new ExAdenlabNormalPlay(nBossID, nSlotID, success));

	}

	public void specialSlot(int nBossID, int nSlotID)
	{
		AdenLabData data = _adenLab.computeIfAbsent(nBossID, AdenLabData::new);

		if(nSlotID > data.getCurrentSlot())
			return;

		player.sendPacket(new ExAdenlabSpecialSlot(nBossID, nSlotID, new int[] {}, data.getSpecialSlot(nSlotID)));
	}

	public void sendBossList()
	{
		player.sendPacket(new ExAdenlabBossList(new int[] {
				0
		}));
	}

	public void unlockBoss(int nBossID)
	{
		player.sendPacket(new ExAdenlabUnlockBoss(nBossID, true));
	}

	public void specialProb(int nBossID, int nSlotID)
	{
		AdenLabStageTemplate slot = AdenLabDataHolder.getInstance().getSlot(nSlotID);
		if(slot == null)
			return;
		player.sendPacket(new ExAdenlabSpecialProb(nBossID, nSlotID, slot.getSpecialGradeProbs()));
	}

	public void specialPlay(int nBossID, int nSlotID, int nFeeIndex)
	{
		AdenLabData data = _adenLab.computeIfAbsent(nBossID, AdenLabData::new);
		if(nSlotID > data.getCurrentSlot())
			return;

		AdenLabStageTemplate slot = AdenLabDataHolder.getInstance().getSlot(nSlotID);
		if(slot == null)
			return;

		List<CardselectOptionData> options = slot.getOptionsByChance();
		if(options == null || options.isEmpty())
			return;

		if(!ItemFunctions.haveItem(player, 57, Config.ADENLAB_ADENA_PLAY))
			return;

		if(!ItemFunctions.deleteItem(player, 57, Config.ADENLAB_ADENA_PLAY))
			return;

		temp_special_options[0] = options.get(0).getLevel();

		if(options.size() > 1)
			temp_special_options[1] = options.get(1).getLevel();
		else
			temp_special_options[1] = 0;

		player.sendPacket(new ExAdenlabSpecialPlay(nBossID, nSlotID, true, temp_special_options));
	}

	public void specialFix(int nBossID, int nSlotID, int nFeeIndex)
	{
		AdenLabData data = _adenLab.computeIfAbsent(nBossID, AdenLabData::new);

		if(data.getSpecialSlots().containsKey(nSlotID))
		{
			if(!ItemFunctions.haveItem(player, 57, Config.ADENLAB_ADENA_FIX))
				return;

			if(!ItemFunctions.deleteItem(player, 57, Config.ADENLAB_ADENA_FIX))
				return;
		}

		if(temp_special_options[0] != 0 || temp_special_options[1] != 0)
		{
			if(!data.getSpecialSlots().containsKey(nSlotID))
			{
				data.setCurrentSlot(nSlotID + 1);
				data.setOpenCards(0);
			}
			data.setSpecialSlot(nSlotID, temp_special_options);

			CharacterAdenLabDAO.getInstance().save(player.getObjectId(), data);
			temp_special_options = new int[2];

			player.sendPacket(new ExAdenlabSpecialFix(nBossID, nSlotID, true));
			updateStats();
		}
	}

	public void transcendEnchant(int nBossID, int nFeeIndex)
	{
		player.sendPacket(new ExAdenlabTranscendEnchant(nBossID, true));
	}

	private void updateStats()
	{
		player.getStatsRecorder().block();
		player.cleanExOptionData();
		for(Map.Entry<Integer, AdenLabData> entry : _adenLab.entrySet())
		{
			AdenLabData adenLabData = entry.getValue();

			int currentSlot = adenLabData.getCurrentSlot();
			int lastCompletedSlot = currentSlot - 1;

			if(lastCompletedSlot < 0)
				continue;

			for(int slot = lastCompletedSlot; slot > 0; slot--)
			{
				AdenLabStageTemplate stageTemplate = AdenLabDataHolder.getInstance().getSlot(slot);

				if(stageTemplate == null)
					continue;

				if(stageTemplate.getNormalEffect() != 0)
					player.addExOptionData(stageTemplate.getNormalEffect(), 1);
				else
				{
					int[] option_id = stageTemplate.getSpecialOptionsId();
					int[] option_level = adenLabData.getSpecialSlot(slot);

					for(int index = 0; index < option_level.length; index++)
						player.addExOptionData(option_id[index], option_level[index]);
				}
			}
		}
		player.getStatsRecorder().unblock();
		player.updateUserBonus();
		player.sendUserInfo(true);
		player.broadcastUserInfo(true);
	}

	public void sendTranscendProb(int nBossID)
	{
		player.sendPacket(new ExAdenlabTranscendProb(nBossID, new int[] {}));
	}
}
