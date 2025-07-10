package l2s.gameserver.data.xml.parser;

import java.io.File;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.math3.util.Pair;
import org.dom4j.Element;

import l2s.commons.data.xml.AbstractParser;
import l2s.gameserver.Config;
import l2s.gameserver.data.xml.holder.HennaHolder;
import l2s.gameserver.model.base.ClassId;
import l2s.gameserver.model.base.ClassLevel;
import l2s.gameserver.templates.henna.DyeCombintation;
import l2s.gameserver.templates.henna.HennaTemplate;
import l2s.gameserver.templates.henna.PotentialEffect;
import l2s.gameserver.templates.henna.PotentialFee;

import gnu.trove.map.TIntIntMap;
import gnu.trove.map.hash.TIntIntHashMap;
import gnu.trove.set.TIntSet;
import gnu.trove.set.hash.TIntHashSet;

public final class HennaParser extends AbstractParser<HennaHolder>
{
	private static final HennaParser INSTANCE = new HennaParser();

	public static HennaParser getInstance()
	{
		return INSTANCE;
	}

	private HennaParser()
	{
		super(HennaHolder.getInstance());
	}

	@Override
	public File getXMLPath()
	{
		return new File(Config.DATAPACK_ROOT, "data/parser/hennas/hennas.xml");
	}

	@Override
	public String getDTDFileName()
	{
		return "hennas.dtd";
	}

	@Override
	protected void readData(Element rootElement) throws Exception
	{
		for (Element element : rootElement.elements())
		{
			String elementName = element.getName();
			if ("potential".equalsIgnoreCase(elementName))
			{
				for (Element element1 : element.elements())
				{
					String elementName1 = element1.getName();
					if ("potential_effect".equalsIgnoreCase(elementName1))
					{
						int id = parseInt(element1, "id");
						int slotId = parseInt(element1, "slot_id");
						int maxSkillLevel = parseInt(element1, "max_skill_level");
						int skillId = parseInt(element1, "skill_id");
						getHolder().addPotentialEffect(new PotentialEffect(id, slotId, maxSkillLevel, skillId));
					}
					else if ("potential_exp".equalsIgnoreCase(elementName1))
					{
						int level = parseInt(element1, "level");
						int exp = parseInt(element1, "exp");
						getHolder().addPotentialExp(level, exp);
					}
					else if ("potential_fee".equalsIgnoreCase(elementName1))
					{
						int step = parseInt(element1, "step");
						int dailyCount = parseInt(element1, "daily_count");
						int itemId = parseInt(element1, "item_id");
						int itemCount = parseInt(element1, "item_count");
						PotentialFee fee = new PotentialFee(step, dailyCount, itemId, itemCount);
						for (Element element2 : element1.elements("exp_count"))
						{
							int value = parseInt(element2, "value");
							double chance = parseDouble(element2, "chance");
							fee.getExpCounts().add(new Pair<>(value, chance));
						}
						getHolder().addPotentialFee(fee);
					}
				}
			}
			else if ("combinations".equalsIgnoreCase(elementName))
			{
				for (Element element1 : element.elements("combination"))
				{
					int slotOne = parseInt(element1, "slot_one");
					int slotTwo = parseInt(element1, "slot_two");
					long adena = parseLong(element1, "adena");
					double chance = parseDouble(element1, "chance");
					int resultDyeId = parseInt(element1, "result_dye_id");
					getHolder().addCombination(new DyeCombintation(slotOne, slotTwo, adena, chance, resultDyeId));
				}
			}
			else if ("henna".equalsIgnoreCase(elementName))
			{
				int symbolId = parseInt(element, "dye_id");
				int dyeId = parseInt(element, "dye_item_id");
				int dyeLvl = parseInt(element, "dye_level");
				long drawPrice = parseInt(element, "wear_fee");
				long drawCount = parseInt(element, "need_count");
				long removePrice = parseInt(element, "cancel_fee");
				long removeCount = parseInt(element, "cancel_count");
				// STATS
				int wit = parseInt(element, "wit", 0);
				int str = parseInt(element, "str", 0);
				int _int = parseInt(element, "int", 0);
				int con = parseInt(element, "con", 0);
				int dex = parseInt(element, "dex", 0);
				int men = parseInt(element, "men", 0);

				TIntSet list = new TIntHashSet();
				for (Element classElement : element.elements("class"))
				{
					int classId = parseInt(classElement, "id", -1);
					if (classId >= 0)
					{
						list.add(classId);
					}
					String classLevelStr = parseString(classElement, "level", null);
					if (!StringUtils.isEmpty(classLevelStr))
					{
						ClassLevel classLevel = ClassLevel.valueOf(classLevelStr.toUpperCase());
						for (ClassId c : ClassId.VALUES)
						{
							if (c.isOfLevel(classLevel))
							{
								list.add(c.getId());
							}
						}
					}
				}

				TIntIntMap skills = new TIntIntHashMap();
				for (Element skillsElement : element.elements("skills"))
				{
					for (Element skillElement : skillsElement.elements("skill"))
					{
						int skillId = Integer.parseInt(skillElement.attributeValue("id"));
						int skillLvl = Integer.parseInt(skillElement.attributeValue("level"));
						skills.put(skillId, skillLvl);
					}
				}
				getHolder().addHenna(new HennaTemplate(symbolId, dyeId, dyeLvl, drawPrice, drawCount, removePrice, removeCount, wit, _int, con, str, dex, men, list, skills));
			}
		}
	}
}
