package l2s.gameserver.data.clientDat;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import l2s.gameserver.Config;
import l2s.gameserver.skills.enums.SkillAutoUseType;
import l2s.gameserver.templates.CollectionTemplate;
import l2s.gameserver.templates.StatsSet;
import l2s.gameserver.templates.item.data.CollectionItemData;
import l2s.gameserver.templates.item.data.ItemData;
import l2s.gameserver.templates.relics.RelicsCollectionTemplate;
import l2s.gameserver.templates.skill.enchant.SkillEnchantCharge;
import l2s.gameserver.utils.SkillUtils;

public final class DatParser
{
	protected final Logger _log = LoggerFactory.getLogger(DatParser.class);

	private Map<Integer, StatsSet> _collectionData = new HashMap<Integer, StatsSet>();

	private Map<Integer, StatsSet> _itemBaseinfoData = new HashMap<Integer, StatsSet>();

	private Map<Integer, StatsSet> _itemNameData = new HashMap<Integer, StatsSet>();
	private Map<Integer, StatsSet> _itemNameDataEu = new HashMap<Integer, StatsSet>();

	private Map<Integer, StatsSet> _itemNameDataAutoUse = new HashMap<Integer, StatsSet>();

	private Map<Integer, StatsSet> _itemStatData = new HashMap<Integer, StatsSet>();

	private Map<Integer, StatsSet> _weapongrpData = new HashMap<Integer, StatsSet>();
	private Map<Integer, StatsSet> _armorgrpData = new HashMap<Integer, StatsSet>();
	private Map<Integer, StatsSet> _etcItemgrpData = new HashMap<Integer, StatsSet>();
	private Map<Integer, String> _itemsIcon = new HashMap<Integer, String>();

	private Map<Long, StatsSet> _skillgrpData = new HashMap<Long, StatsSet>();
	private Map<Long, StatsSet> _mSConditionData = new HashMap<Long, StatsSet>();

	private Map<Integer, SkillAutoUseType> _autoUse = new HashMap<Integer, SkillAutoUseType>();

	private Map<Integer, SkillEnchantCharge> _enchantChargeData_ClassicAden = new HashMap<Integer, SkillEnchantCharge>();

	private Map<Integer, StatsSet> _ensoul_stone_client_ClassicAden;

	private Map<Integer, StatsSet> _ensoul_option_client_Classic;

	private Map<Long, StatsSet> _skillNameRuData;

	private Map<Integer, StatsSet> _NpcgrpData;

	private List<StatsSet> _UpgradeSystem_Normal_ClassicAden;

	private static DatParser _instance;

	public static DatParser getInstance()
	{
		if(_instance == null)
			_instance = new DatParser();
		return _instance;
	}

	public String getIcon(String string)
	{
		return string.split(";")[0];
	}

	public String getId2(int option_id)
	{
		int start = ((int) (option_id / 100)) * 100;
		int end = ((int) (option_id / 100)) * 100;

		if(start < 0)
			start = 0;
		if(end < 100)
			end = 1;

		end = end + 99;
		if(end == 100)
			end = 99;
		return start + "-" + end;
	}

	public void load()
	{
		_log.info("");
		_log.info("===================[read client files (*.dat)]=======================");

		//_ensoul_stone_client_ClassicAden = loadtxtToMap("ensoul_stone_client_ClassicAden.txt", "id", "ensoul_stone_begin", "ensoul_stone_end");
		//_ensoul_option_client_Classic = loadtxtToMap("ensoul_option_client_ClassicAden-ru.txt", "id", "ensoul_option_begin", "ensoul_option_end");

		List<StatsSet> enchantCharge = loadtxtToList("SkillEnchantCharge_ClassicAden.txt", "skill_enchant_charge_begin", "skill_enchant_charge_end");
		for(StatsSet statsSet : enchantCharge)
			_enchantChargeData_ClassicAden.put(statsSet.getInteger("group_item"), new SkillEnchantCharge(statsSet));

		//_skillAcquireClassic = loadtxtToList("SkillAcquire_ClassicAden.txt", "skillacquire_begin", "skillacquire_end").stream().collect(Collectors.groupingBy(r -> r.getInteger("class_id")));
		List<StatsSet> auto_use = loadtxtToList("Skillgrp_ClassicAden.txt", "skill_autouse_begin", "skill_autouse_end");
		for(StatsSet skillParam : auto_use)
			_autoUse.put(skillParam.getInteger("skill_id"), SkillAutoUseType.values()[skillParam.getInteger("auto_use_type")]);

		//_UpgradeSystem_Normal_ClassicAden = loadtxtToList("UpgradeSystem_Normal_ClassicAden.txt", "upgradesystem_begin", "upgradesystem_end", new String[] { "[", "]" });

		_collectionData = loadtxtToMap("collection_ClassicAden-ru.txt", "collection_ID", "collection_begin", "collection_end", new String[] {
				"[", "]"
		});
		for(StatsSet collection : _collectionData.values())
		{
			int _collection_id = collection.getInteger("collection_ID");
			int _main_category = collection.getInteger("main_category");
			int _option_id = collection.getInteger("option_id");

			CollectionTemplate colltemplate = new CollectionTemplate(_collection_id, _main_category, _option_id);

			String _items_str = collection.getString("slot_item");
			Pattern stringArray = Pattern.compile("\\{([\\S; \n\t]*?)}", Pattern.DOTALL);

			Matcher matcher = stringArray.matcher(_items_str.substring(1, _items_str.length() - 1));
			while(matcher.find())
			{
				int[] param = Stream.of(matcher.group(1).split(";")).mapToInt(Integer::parseInt).toArray();
				colltemplate.addItem(new CollectionItemData(param[0], param[2], param[3], param[4], param[5], param[7]));
			}
			colltemplate.setMaxSlot(colltemplate.getItems().stream().collect(Collectors.groupingBy(r -> r.getSlotId())).size());

			String _reward_items_str = collection.getString("complete_item_type");

			matcher = stringArray.matcher(_reward_items_str.substring(1, _reward_items_str.length() - 1));
			while(matcher.find())
			{
				int[] param = Stream.of(matcher.group(1).split(";")).mapToInt(Integer::parseInt).toArray();
				colltemplate.addReward(new ItemData(param[0], param[1]));
			}

			CollectionsData.getInstance().addCollection(colltemplate);
		}

		Map<Integer, StatsSet> _relicsCollectionData = loadtxtToMap("relics_collection_ClassicAden-ru.txt", "relics_collection_id", "relics_collection_begin", "relics_collection_end", new String[] {
				"[", "]"
		});
		for(StatsSet relic : _relicsCollectionData.values())
		{
			int relics_collection_id = relic.getInteger("relics_collection_id");
			RelicsData.getInstance().addRelicCollection(relics_collection_id, new RelicsCollectionTemplate(relic));
		}
		//_skillNameRuData = loadtxtToMapSkillLong("SkillName_ClassicAden-ru.txt", "skill_id", "skill_begin", "skill_end", new String[] { "{", "}", "[", "]" });

		//_NpcNameData = loadtxtToMap("NpcName_ClassicAden-eu.txt", "id", "npc_begin", "npc_end", new String[] { "[", "]" });
		//_NpcgrpData = loadtxtToMap("Npcgrp_ClassicAden.txt", "npc_id", "npc_begin", "npc_end", new String[] { "[", "]" });

		_itemNameData = loadtxtToMap("ItemName_ClassicAden-ru.txt", "id", "item_name_begin", "item_name_end", new String[] {
				"[", "]"
		});

		_itemNameDataAutoUse = loadtxtToMap("ItemName_ClassicAden-ru.txt", "Item_id", "automatic_use_begin", "automatic_use_end", new String[] {
				"[", "]"
		});

		_itemNameDataEu = loadtxtToMap("ItemName_ClassicAden-eu.txt", "id", "item_name_begin", "item_name_end", new String[] {
				"[", "]"
		});

		//_itemStatData = loadtxtToMap("ItemStatData_ClassicAden.txt", "object_id", "item_begin", "item_end", new String[] { "{", "}", "[", "]" });

		//_itemBaseinfoData = loadtxtToMap("item_baseinfo_ClassicAden.txt", "id", "item_baseinfo_begin", "item_baseinfo_end", new String[] {"{","}","[","]" });

		_weapongrpData = loadtxtToMap("Weapongrp_ClassicAden.txt", "object_id", "item_begin", "item_end", new String[] {
				"{", "}", "[", "]"
		});
		_armorgrpData = loadtxtToMap("Armorgrp_ClassicAden.txt", "object_id", "item_begin", "item_end", new String[] {
				"{", "}", "[", "]"
		});
		_etcItemgrpData = loadtxtToMap("EtcItemgrp_ClassicAden.txt", "object_id", "item_begin", "item_end", new String[] {
				"{", "}", "[", "]"
		});

		_itemsIcon = loadItemsIcon();
		_skillgrpData = loadtxtToMapSkillLong("Skillgrp_ClassicAden.txt", "skill_id", "skill_begin", "skill_end", new String[] {
				"{", "}", "[", "]"
		});

		_mSConditionData = loadtxtToMapSkillLong("MSConditionData_ClassicAden.txt", "skill_id", "skill_begin", "skill_end", new String[] {
				"{", "}", "[", "]"
		});

		_log.info("=======================================================================");
		_log.info("");
	}

	private Map<Integer, String> loadItemsIcon()
	{
		Map<Integer, String> result = new HashMap<Integer, String>();
		_weapongrpData.entrySet().forEach(s -> result.put(s.getKey(), getIcon(s.getValue().getString("icon"))));
		_armorgrpData.entrySet().forEach(s -> result.put(s.getKey(), getIcon(s.getValue().getString("icon"))));
		_etcItemgrpData.entrySet().forEach(s -> result.put(s.getKey(), getIcon(s.getValue().getString("icon"))));
		return result;
	}

	public String getItemIcon(int item_id)
	{
		return _itemsIcon.getOrDefault(item_id, "icon.etc_question_mark_i00");
	}

	@SuppressWarnings("resource")
	public Map<Integer, StatsSet> loadtxtToMap(String file_name, String name_id, String start_line, String end_line, String... replace)
	{
		Map<Integer, StatsSet> _result = new HashMap<Integer, StatsSet>();
		try
		{
			BufferedReader br = Files.newBufferedReader(Config.DATAPACK_ROOT.toPath().resolve("data/client_dat/").resolve(file_name), Charset.forName("UTF-8"));
			String line;
			// Считываем файл до конца
			while((line = br.readLine()) != null)
			{
				line = line.trim();

				if(line.isEmpty())
					continue;

				if(line.isEmpty())
					continue;
				if(!line.startsWith(start_line))
					continue;

				String[] l = line.split("	");
				StatsSet baseDat = new StatsSet();

				for(String s : l)
				{
					if(s.startsWith(start_line) || s.startsWith(end_line))
						continue;
					String[] tmp = s.split("=");
					if(tmp.length < 2)
						continue;
					String value = tmp[1];
					if(replace.length > 0)
						for(String rep : replace)
							value = value.replace(rep, "");

					baseDat.set(tmp[0], value);
				}
				if(file_name.equalsIgnoreCase("Skillgrp_ClassicAden.txt") || file_name.equalsIgnoreCase("SkillName_ClassicAden-ru.txt")
						|| file_name.equalsIgnoreCase("SkillName_ClassicAden-eu.txt") || file_name.equalsIgnoreCase("MSConditionData_Classic.txt"))
					_result.put((baseDat.getInteger(name_id) * 65536) + baseDat.getInteger("skill_level"), baseDat);
				else
					_result.put(baseDat.getInteger(name_id), baseDat);
			}
		}
		catch(Exception e)
		{

			_log.warn(e.getLocalizedMessage(), e);
		}
		_log.info(file_name + ": " + _result.size() + " objects");
		return _result;
	}

	@SuppressWarnings("resource")
	public Map<Long, StatsSet> loadtxtToMapSkillLong(String file_name, String name_id, String start_line, String end_line, String... replace)
	{
		Map<Long, StatsSet> _result = new HashMap<Long, StatsSet>();
		try
		{
			BufferedReader br = Files.newBufferedReader(Config.DATAPACK_ROOT.toPath().resolve("data/client_dat/").resolve(file_name), Charset.forName("UTF-8"));
			String line;
			// Считываем файл до конца
			while((line = br.readLine()) != null)
			{
				line = line.trim();

				if(line.isEmpty())
					continue;

				if(line.isEmpty())
					continue;
				if(!line.startsWith(start_line))
					continue;

				String[] l = line.split("	");
				StatsSet baseDat = new StatsSet();

				for(String s : l)
				{
					if(s.startsWith(start_line) || s.startsWith(end_line))
						continue;
					String[] tmp = s.split("=");
					if(tmp.length < 2)
						continue;
					String value = tmp[1];
					if(replace.length > 0)
						for(String rep : replace)
							value = value.replace(rep, "");

					//if(tmp[0].equalsIgnoreCase("skill_sublevel") && Integer.parseInt(value) !=0)
					//	continue loop;

					baseDat.set(tmp[0], value);
				}
				if(file_name.equalsIgnoreCase("Skillgrp_ClassicAden.txt") || file_name.equalsIgnoreCase("SkillName_ClassicAden-ru.txt")
						|| file_name.equalsIgnoreCase("SkillName_ClassicAden-eu.txt") || file_name.equalsIgnoreCase("MSConditionData_ClassicAden.txt"))
					_result.put(SkillUtils.getSkillPTSLongHash(baseDat.getInteger(name_id), SkillUtils.getSkillLevelMask(baseDat.getInteger("skill_level"), baseDat.getInteger("skill_sublevel"))), baseDat);
				else
					_result.put(baseDat.getLong(name_id), baseDat);
			}
		}
		catch(Exception e)
		{

			_log.warn(e.getLocalizedMessage(), e);
		}
		_log.info(file_name + ": " + _result.size() + " objects");
		return _result;
	}

	@SuppressWarnings("resource")
	public List<StatsSet> loadtxtToList(String file_name, String start_line, String end_line, String... replace)
	{
		List<StatsSet> _result = new ArrayList<StatsSet>();
		try
		{
			BufferedReader br = Files.newBufferedReader(Config.DATAPACK_ROOT.toPath().resolve("data/client_dat/").resolve(file_name), Charset.forName("UTF-8"));
			String line;

			// Считываем файл до конца
			loop:
			while((line = br.readLine()) != null)
			{
				line = line.trim();

				if(line.isEmpty())
					continue;

				if(line.isEmpty())
					continue;
				if(!line.startsWith(start_line))
					continue;

				String[] l = line.split("	");
				StatsSet baseDat = new StatsSet();

				for(String s : l)
				{
					if(s.startsWith(start_line) || s.startsWith(end_line))
						continue;
					String[] tmp = s.split("=");
					if(tmp.length < 2)
						continue;
					String value = tmp[1];
					if(replace.length > 0)
						for(String rep : replace)
							value = value.replace(rep, "");
					if(tmp[0].equalsIgnoreCase("skill_sublevel") && Integer.parseInt(value) != 0)
						continue loop;
					baseDat.set(tmp[0], value);
				}
				_result.add(baseDat);
			}
		}
		catch(Exception e)
		{

			_log.warn(e.getLocalizedMessage(), e);
		}
		_log.info(file_name + ": " + _result.size() + " objects");
		return _result;
	}

	public StatsSet getItemBaseInfoById(int id)
	{
		StatsSet data = _itemBaseinfoData.get(id);
		if(data == null)
			return new StatsSet();
		return _itemBaseinfoData.get(id);
	}

	public Map<Integer, StatsSet> getCollectionData()
	{
		return _collectionData;
	}

	public StatsSet getMSConditionDataById(long id)
	{
		if(id == 0)
			return null;

		return _mSConditionData.get(id);
	}

	public Collection<StatsSet> getSkillgrpData()
	{
		return _skillgrpData.values();
	}

	public StatsSet getSkillgrpDataById(long id)
	{
		if(id == 0)
			return null;

		return _skillgrpData.get(id);
	}

	public StatsSet getEtcItemgrpDataById(int id)
	{
		if(id == 0)
			return null;
		return _etcItemgrpData.get(id);
	}

	public StatsSet getArmorgrpDataById(int id)
	{
		if(id == 0)
			return null;

		return _armorgrpData.get(id);
	}

	public StatsSet getWeapongrpDataById(int id)
	{
		if(id == 0)
			return null;

		return _weapongrpData.get(id);
	}

	public Map<Integer, StatsSet> getItemNameData()
	{
		return _itemNameData;
	}

	public StatsSet getItemNameDataByIdEu(int id)
	{
		if(id == 0)
			return null;

		if(_itemNameDataEu.get(id) == null)
			return new StatsSet();

		return _itemNameDataEu.get(id);
	}

	public StatsSet getItemNameDataById(int id)
	{
		if(id == 0)
			return null;

		return _itemNameData.get(id);
	}

	public StatsSet getItemStatDataById(int id)
	{
		if(id == 0)
			return null;
		StatsSet data = _itemStatData.get(id);
		if(data == null)
			return new StatsSet();
		return _itemStatData.get(id);
	}

	public Map<Integer, StatsSet> getItemNameDataEu()
	{
		return _itemNameDataEu;
	}

	public Map<Integer, StatsSet> getItemAutoUse()
	{
		return _itemNameDataAutoUse;
	}

	public Map<Integer, SkillAutoUseType> getAutoUse()
	{
		return _autoUse;
	}

	public Map<Integer, StatsSet> getEnsoulStoneClassicAden()
	{
		return _ensoul_stone_client_ClassicAden;
	}

	public Map<Integer, StatsSet> getEnsoulOptionClient()
	{
		return _ensoul_option_client_Classic;
	}

	public Map<Integer, SkillEnchantCharge> getEnchantChargeData()
	{
		return _enchantChargeData_ClassicAden;
	}

	public Map<Long, StatsSet> getSkillNameRuData()
	{
		return _skillNameRuData;
	}

	public StatsSet getNpcgrpDataById(int key)
	{
		return _NpcgrpData.get(key);
	}

	public void generateRuTxtFile(List<StatsSet> _skillNameRuData)
	{
		try (BufferedWriter writer = new BufferedWriter(new FileWriter("ru.txt")))
		{
			_skillNameRuData.stream().sorted(Comparator.comparingLong(stats -> stats.getLong("skill_id"))).forEach(stats -> {
				try
				{
					// Извлекаем данные
					long skillId = stats.getLong("skill_id");
					int skillLevel = stats.getInteger("skill_level");
					String name = stats.getString("name").replace("[", "").replace("]", "");
					if(!name.isEmpty())
					{
						int skill_sublevel = stats.getInteger("skill_sublevel");
						if(skill_sublevel == 0)
						{
							// Формируем строку
							String line = skillId + "\t" + skillLevel + "\t" + name;

							// Записываем строку в файл
							writer.write(line);
							writer.newLine();
						}
					}
				}
				catch(IOException e)
				{
					e.printStackTrace();
				}
			});
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}

	public StatsSet getSkillNameRuData(long skillPTSLongHash)
	{
		return _skillNameRuData.get(skillPTSLongHash);
	}

	public List<StatsSet> get_UpgradeSystem_Normal_ClassicAden()
	{
		return _UpgradeSystem_Normal_ClassicAden;
	}
}