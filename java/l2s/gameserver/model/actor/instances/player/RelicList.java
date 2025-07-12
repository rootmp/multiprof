package l2s.gameserver.model.actor.instances.player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.CopyOnWriteArrayList;
import l2s.gameserver.dao.AccountRelicsDAO;
import l2s.commons.util.Rnd;
import l2s.dataparser.data.holder.ItemAnnounceDataHolder;
import l2s.gameserver.dao.AccountRelicsCollectionDAO;
import l2s.gameserver.model.GameObjectsStorage;
import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.components.SystemMsg;
import l2s.gameserver.network.l2.s2c.relics.ExRelicsActiveInfo;
import l2s.gameserver.network.l2.s2c.relics.ExRelicsAnnounce;
import l2s.gameserver.network.l2.s2c.relics.ExRelicsCollectionCompleteAnnounce;
import l2s.gameserver.network.l2.s2c.relics.ExRelicsCollectionUpdate;
import l2s.gameserver.network.l2.s2c.relics.ExRelicsCombination;
import l2s.gameserver.network.l2.s2c.relics.ExRelicsSummonResult;
import l2s.gameserver.network.l2.s2c.relics.ExRelicsUpdateList;
import l2s.gameserver.network.l2.s2c.relics.ExRelicsUpgrade;
import l2s.gameserver.skills.SkillEntry;
import l2s.gameserver.templates.relics.CollectionRelicsInfo;
import l2s.gameserver.templates.relics.RelicsCollectionTemplate;
import l2s.gameserver.templates.relics.RelicsCoupon;
import l2s.gameserver.templates.relics.RelicsInfo;
import l2s.gameserver.templates.relics.RelicsProb;
import l2s.gameserver.templates.relics.RelicsTemplate;
import l2s.gameserver.utils.ItemFunctions;
import l2s.gameserver.data.clientDat.RelicsData;
import l2s.gameserver.data.xml.holder.SkillHolder;
import l2s.gameserver.data.xml.holder.OptionDataHolder;
import l2s.gameserver.data.xml.holder.RelicHolder;
import l2s.gameserver.data.xml.holder.RelicsCouponHolder;
import l2s.gameserver.data.xml.holder.RelicsSynthesisHolder;
import l2s.gameserver.templates.OptionDataTemplate;
import l2s.gameserver.templates.item.data.ItemData;
import l2s.gameserver.templates.relics.RelicsCollection;

public class RelicList
{
	private final Player owner;
	private Map<Integer, RelicsInfo> _relics = new TreeMap<>();
	private final Map<Integer, Integer> synthesisCosts = Map.of(1, 100000, 2, 500000, 3, 1000000, 4, 3000000, 5, 10000000, 6, 100000000, 7, 200000000);

	private Map<Integer, List<CollectionRelicsInfo>> _relicsCollection = new TreeMap<>();

	private List<SkillEntry> _relicsSkills = new CopyOnWriteArrayList<>();

	private List<Integer> _collectionCompleteAnnounce = new ArrayList<>();
	private boolean _collectionUpdated = false;

	public RelicList(Player owner)
	{
		this.owner = owner;
	}

	public void restore()
	{
		_relics = AccountRelicsDAO.getInstance().restore(owner);
		_relicsCollection = AccountRelicsCollectionDAO.getInstance().restore(owner);
		applyStats();
	}

	public List<SkillEntry> getRelicsSkills()
	{
		return _relicsSkills;
	}

	public List<RelicsCollection> getCurrentCollections()
	{
		List<RelicsCollection> collections = new ArrayList<>();
		for(Map.Entry<Integer, List<CollectionRelicsInfo>> entry : _relicsCollection.entrySet())
		{
			int collectionId = entry.getKey();
			List<CollectionRelicsInfo> collectionRelics = entry.getValue();
			byte isComplete = (byte) (isCollectionComplete(RelicsData.getInstance().getRelicCollection(collectionId)) ? 1 : 0);
			RelicsCollection collection = new RelicsCollection(collectionId, isComplete, collectionRelics);
			collections.add(collection);
		}
		return collections;
	}

	public List<RelicsInfo> getAllRelicsInfo()
	{
		return new ArrayList<>(_relics.values());
	}

	public boolean addRelicToPlayer(int relicId)
	{
		RelicsInfo relic = _relics.get(relicId);

		if(relic == null)
		{
			relic = new RelicsInfo(relicId, 0, 0);
			_relics.put(relicId, relic);
		}
		else
			relic.nCount += 1;

		if(ItemAnnounceDataHolder.getInstance().getRelics().contains(Integer.valueOf(relicId)))
		{
			for(Player st : GameObjectsStorage.getPlayers(true, false))
			{
				if(!st.getVarBoolean("DisableSysMsgDoll", false))
					st.sendPacket(new ExRelicsAnnounce(owner, relicId));
			}
		}

		updateCollections(relic);

		if(!AccountRelicsDAO.getInstance().saveOrUpdate(owner, relic))
			return false;

		applyStats();
		return true;
	}

	private void updateCollections(RelicsInfo relic)
	{
		for(RelicsCollectionTemplate collectionTemplate : RelicsData.getInstance().getAllCollectionTemplates().values())
		{
			for(int[] needRelic : collectionTemplate.getNeedRelics())
			{
				if(needRelic[0] == relic.nRelicsID)
				{
					List<CollectionRelicsInfo> collectionRelicsList = _relicsCollection.computeIfAbsent(collectionTemplate.getRelicsCollectionId(), k -> new ArrayList<>());
					boolean exists = collectionRelicsList.stream().anyMatch(r -> r.nRelicsID == relic.nRelicsID);
					if(!exists)
					{
						collectionRelicsList.add(new CollectionRelicsInfo(relic.nRelicsID, relic.nLevel));
						saveOrUpdateCollection(collectionTemplate.getRelicsCollectionId(), relic.nRelicsID, relic.nLevel);
						if(isCollectionComplete(collectionTemplate))
							_collectionCompleteAnnounce.add(collectionTemplate.getRelicsCollectionId());
						_collectionUpdated = true;
					}
				}
			}
		}
	}

	private void saveOrUpdateCollection(int collectionId, int relicId, int level)
	{
		CollectionRelicsInfo collectionRelicsInfo = new CollectionRelicsInfo(relicId, level);
		AccountRelicsCollectionDAO.getInstance().saveOrUpdate(owner, collectionId, collectionRelicsInfo);
	}

	private void applyStats()
	{
		owner.getStatsRecorder().block();
		try
		{
			_relicsSkills.clear();
			for(RelicsInfo relicInfo : _relics.values())
			{
				RelicsTemplate relicTemplate = RelicHolder.getInstance().getRelic(relicInfo.nRelicsID);
				if(relicTemplate != null)
				{
					SkillEntry skillEntry = relicTemplate.getSkillByIndex(relicInfo.nLevel);
					if(skillEntry != null)
					{
						SkillEntry currentSkill = owner.getKnownSkill(skillEntry.getId());
						if(currentSkill == null || currentSkill.getLevel() < skillEntry.getLevel())
						{
							SkillEntry skill = SkillHolder.getInstance().getSkillEntry(skillEntry.getId(), skillEntry.getLevel());
							owner.addSkill(skill);
							_relicsSkills.add(skill);
						}
					}

					owner.sendPacket(new ExRelicsActiveInfo(relicInfo.nRelicsID, relicInfo.nLevel));
				}
			}

			for(RelicsCollectionTemplate collectionTemplate : RelicsData.getInstance().getAllCollectionTemplates().values())
			{
				if(isCollectionComplete(collectionTemplate))
				{
					OptionDataTemplate option = OptionDataHolder.getInstance().getTemplate(collectionTemplate.getOptionId());
					owner.addOptionData(option);
				}
			}
		}
		finally
		{
			owner.getStatsRecorder().unblock();
			owner.sendUserInfo(true);
			owner.broadcastUserInfo(true);
		}
	}

	private boolean isCollectionComplete(RelicsCollectionTemplate collectionTemplate)
	{
		List<int[]> neededRelics = collectionTemplate.getNeedRelics();
		for(int[] neededRelic : neededRelics)
		{
			if(!_relics.containsKey(neededRelic[0]))
				return false; // Если хотя бы одна реликвия из списка отсутствует, коллекция не полная
		}
		return true;
	}

	public void summonRelics(int count, List<RelicsProb> relicChances)
	{
		if(relicChances.isEmpty())
			return;

		List<Integer> addedRelics = new ArrayList<>();
		Map<Integer, RelicsInfo> updatedRelics = new HashMap<>();

		for(int i = 0; i < count; i++)
		{
			RelicsProb selectedRelic = selectRandomRelic(relicChances);
			if(selectedRelic != null)
			{
				addRelicToPlayer(selectedRelic.nRelicsID);
				addedRelics.add(selectedRelic.nRelicsID);
				RelicsInfo relicInfo = _relics.get(selectedRelic.nRelicsID);
				updatedRelics.put(selectedRelic.nRelicsID, relicInfo);
			}
		}
		owner.sendPacket(new ExRelicsSummonResult(1, 0, addedRelics));
		owner.sendPacket(new ExRelicsUpdateList(new ArrayList<>(updatedRelics.values())));
	}
	
	public boolean giveRelics(int item_id)
	{
		RelicsCoupon coupon = RelicsCouponHolder.getInstance().getCoupon(item_id);
		if(coupon == null)
			return false;

		if(!ItemFunctions.deleteItem(owner, item_id, 1, true))
		{
			owner.sendPacket(SystemMsg.INCORRECT_ITEM_COUNT);
			return false;
		}

		int count = coupon.getCount(); // Количество выдаваемых реликвий
		List<RelicsProb> relicChances = coupon.getRelicsProb();

		List<Integer> addedRelics = new ArrayList<>();
		Map<Integer, RelicsInfo> updatedRelics = new HashMap<>();

		for(int i = 0; i < count; i++)
		{
			RelicsProb selectedRelic = selectRandomRelic(relicChances);
			if(selectedRelic != null)
			{
				addRelicToPlayer(selectedRelic.nRelicsID);
				addedRelics.add(selectedRelic.nRelicsID);
				RelicsInfo relicInfo = _relics.get(selectedRelic.nRelicsID);
				updatedRelics.put(selectedRelic.nRelicsID, relicInfo);
			}
		}
		owner.sendPacket(new ExRelicsSummonResult(1, item_id, addedRelics));
		owner.sendPacket(new ExRelicsUpdateList(new ArrayList<>(updatedRelics.values())));
		return true;
	}

	public void synthesizeRelics(int nGrade, List<Integer> stuffList)
	{
		if(stuffList.size() % 4 != 0)
		{
			owner.sendPacket(SystemMsg.INCORRECT_ITEM_COUNT);
			return;
		}

		int synthesisCost = synthesisCosts.getOrDefault(nGrade, 0);
		int requiredAdena = (stuffList.size() / 4) * synthesisCost;

		if(owner.getAdena() < requiredAdena)
		{
			owner.sendPacket(SystemMsg.YOU_DO_NOT_HAVE_ENOUGH_ADENA);
			return;
		}

		Map<Integer, Integer> relicCounts = new HashMap<>();
		for(int relicId : stuffList)
			relicCounts.put(relicId, relicCounts.getOrDefault(relicId, 0) + 1);

		for(Map.Entry<Integer, Integer> entry : relicCounts.entrySet())
		{
			RelicsInfo relic = _relics.get(entry.getKey());
			if(relic == null || relic.nCount < entry.getValue())
			{
				owner.sendPacket(SystemMsg.INCORRECT_ITEM_COUNT);
				return;
			}

			RelicsTemplate relicTemplate = RelicHolder.getInstance().getRelic(entry.getKey());
			if(relicTemplate == null || relicTemplate.getGrade() != nGrade)
			{
				owner.sendPacket(SystemMsg.INCORRECT_ITEM_COUNT);
				return;
			}
		}

		if(!ItemFunctions.deleteItem(owner, 57, requiredAdena, true))
		{
			owner.sendPacket(SystemMsg.YOU_DO_NOT_HAVE_ENOUGH_ADENA);
			return;
		}

		List<Integer> synthesizedRelics = new ArrayList<>();
		List<ItemData> failItemList = new ArrayList<>();
		List<Integer> currentRelicBatch = new ArrayList<>();
		Map<Integer, RelicsInfo> updatedRelics = new HashMap<>();

		for(int i = 0; i < stuffList.size(); i++)
		{
			currentRelicBatch.add(stuffList.get(i));

			if(currentRelicBatch.size() == 4)
			{
				for(int relicId : currentRelicBatch)
				{
					RelicsInfo relic = _relics.get(relicId);
					relic.nCount -= 1;
					updatedRelics.put(relicId, relic);
					AccountRelicsDAO.getInstance().saveOrUpdate(owner, relic);
				}
				RelicsProb selectedRelic = selectRandomRelic(RelicsSynthesisHolder.getInstance().getRelicsProb(nGrade));
				addRelicToPlayer(selectedRelic.nRelicsID);

				RelicsInfo newRelic = _relics.get(selectedRelic.nRelicsID);
				updatedRelics.put(selectedRelic.nRelicsID, newRelic);

				synthesizedRelics.add(selectedRelic.nRelicsID);
				currentRelicBatch.clear();
			}
		}
		owner.sendPacket(new ExRelicsCombination(1, synthesizedRelics, failItemList));
		owner.sendPacket(new ExRelicsUpdateList(new ArrayList<>(updatedRelics.values())));
	}

	private RelicsProb selectRandomRelic(List<RelicsProb> probList)
	{
		List<RelicsProb> relicChances = new ArrayList<>(probList);
		Collections.shuffle(relicChances);
		
		long totalChance = relicChances.stream().mapToLong(r -> r.nProb).sum();
		long randomValue = Rnd.get(totalChance);

		for (RelicsProb prob : relicChances)
		{
			randomValue -= prob.nProb;
			if (randomValue <= 0)
				return prob;
		}

		return Rnd.get(relicChances);
	}

	public void upgradeRelic(int relicId, int targetLevel, List<Integer> stuffList)
	{
		if(stuffList.isEmpty() || stuffList.size() > 4)
		{
			owner.sendPacket(SystemMsg.INCORRECT_ITEM_COUNT);
			return;
		}

		int upgradeCost = 200000000;
		if(owner.getAdena() < upgradeCost)
		{
			owner.sendPacket(SystemMsg.YOU_DO_NOT_HAVE_ENOUGH_ADENA);
			return;
		}

		RelicsInfo mainRelic = _relics.get(relicId);
		if(mainRelic == null || mainRelic.nLevel > 3)
		{
			owner.sendPacket(SystemMsg.INCORRECT_ITEM_COUNT);
			return;
		}

		RelicsTemplate mainRelicTemplate = RelicHolder.getInstance().getRelic(relicId);
		if(mainRelicTemplate == null)
		{
			owner.sendPacket(SystemMsg.INCORRECT_ITEM_COUNT);
			return;
		}

		int grade = mainRelicTemplate.getGrade();
		Map<Integer, Integer> requiredRelics = new HashMap<>();
		for(int stuffRelicId : stuffList)
		{
			requiredRelics.put(stuffRelicId, requiredRelics.getOrDefault(stuffRelicId, 0) + 1);
		}

		for(Map.Entry<Integer, Integer> entry : requiredRelics.entrySet())
		{
			int stuffRelicId = entry.getKey();
			int requiredCount = entry.getValue();

			RelicsInfo stuffRelic = _relics.get(stuffRelicId);
			if(stuffRelic == null || stuffRelic.nCount < requiredCount)
			{
				owner.sendPacket(SystemMsg.INCORRECT_ITEM_COUNT);
				return;
			}

			RelicsTemplate stuffRelicTemplate = RelicHolder.getInstance().getRelic(stuffRelicId);
			if(stuffRelicTemplate == null || stuffRelicTemplate.getGrade() != grade)
			{
				owner.sendPacket(SystemMsg.INCORRECT_ITEM_COUNT);
				return;
			}
		}

		if(!ItemFunctions.deleteItem(owner, 57, upgradeCost, true))
		{
			owner.sendPacket(SystemMsg.YOU_DO_NOT_HAVE_ENOUGH_ADENA);
			return;
		}

		for(int stuffRelicId : stuffList)
		{
			RelicsInfo stuffRelic = _relics.get(stuffRelicId);
			stuffRelic.nCount -= 1;
			AccountRelicsDAO.getInstance().saveOrUpdate(owner, stuffRelic);
		}

		int upgradeChance;
		switch(stuffList.size())
		{
			case 1:
				upgradeChance = 10;
				break;
			case 2:
				upgradeChance = 20;
				break;
			case 3:
				upgradeChance = 30;
				break;
			case 4:
				upgradeChance = 50;
				break;
			default:
				upgradeChance = 0;
				break;
		}

		boolean success = Rnd.chance(upgradeChance);
		if(success)
		{
			mainRelic.nLevel = mainRelic.nLevel + 1;
			AccountRelicsDAO.getInstance().saveOrUpdate(owner, mainRelic);
			owner.sendPacket(new ExRelicsUpgrade(1, relicId, mainRelic.nLevel));
		}
		else
			owner.sendPacket(new ExRelicsUpgrade(0, relicId, mainRelic.nLevel));

		applyStats();
		owner.sendPacket(new ExRelicsUpdateList(getAllRelicsInfo()));
	}

	public void checkUpdate()
	{
		for(Integer coll : _collectionCompleteAnnounce)
		{
			owner.sendPacket(new ExRelicsCollectionCompleteAnnounce(coll));
		}
		_collectionCompleteAnnounce.clear();
		if(_collectionUpdated)
		{
			owner.sendPacket(new ExRelicsCollectionUpdate(getCurrentCollections()));
			_collectionUpdated = false;
		}
	}
}
