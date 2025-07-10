package l2s.gameserver.data.xml.parser;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.dom4j.Element;

import l2s.gameserver.Config;
import l2s.gameserver.data.xml.holder.SkillAcquireHolder;
import l2s.gameserver.data.xml.holder.SkillHolder;
import l2s.gameserver.model.Skill;
import l2s.gameserver.model.SkillLearn;
import l2s.gameserver.model.base.ClassId;
import l2s.gameserver.model.base.ClassLevel;
import l2s.gameserver.model.base.PledgeRank;
import l2s.gameserver.model.base.Race;
import l2s.gameserver.skills.enums.SkillType;
import l2s.gameserver.stats.conditions.Condition;

/**
 * @author: VISTALL
 * @date: 20:55/30.11.2010
 */
public final class SkillAcquireParser extends StatParser<SkillAcquireHolder>
{
	private static final SkillAcquireParser _instance = new SkillAcquireParser();

	protected SkillAcquireParser()
	{
		super(SkillAcquireHolder.getInstance());
	}

	public static SkillAcquireParser getInstance()
	{
		return _instance;
	}

	@Override
	public File getXMLPath()
	{
		return new File(Config.DATAPACK_ROOT, "data/parser/player_parameters/skill_tree_data/");
	}

	@Override
	public String getDTDFileName()
	{
		return "tree.dtd";
	}

	@Override
	protected void readData(Element rootElement) throws Exception
	{
		for (Element element : rootElement.elements())
		{
			String elementName = element.getName();
			if ("certification_skill_tree".equalsIgnoreCase(elementName))
			{
				getHolder().addAllCertificationLearns(parseSkillLearn(element));
			}
			else if ("sub_unit_skill_tree".equalsIgnoreCase(elementName))
			{
				getHolder().addAllSubUnitLearns(parseSkillLearn(element));
			}
			else if ("pledge_skill_tree".equalsIgnoreCase(elementName))
			{
				getHolder().addAllPledgeLearns(parseSkillLearn(element));
			}
			else if ("fishing_skill_tree".equalsIgnoreCase(elementName))
			{
				getHolder().addAllFishingLearns(parseSkillLearn(element));
			}
			else if ("hero_skill_tree".equalsIgnoreCase(elementName))
			{
				getHolder().addAllHeroLearns(parseSkillLearn(element));
			}
			else if ("gm_skill_tree".equalsIgnoreCase(elementName))
			{
				getHolder().addAllGMLearns(parseSkillLearn(element));
			}
			else if ("custom_skill_tree".equalsIgnoreCase(elementName))
			{
				getHolder().addAllCustomLearns(parseSkillLearn(element));
			}
			else
			{
				boolean general = "general_skill_tree".equalsIgnoreCase(elementName);
				if (general || "normal_skill_tree".equalsIgnoreCase(elementName))
				{
					Race race = element.attributeValue("race") == null ? null : Race.valueOf(element.attributeValue("race").toUpperCase());
					int classId = parseInt(element, "class_id", -1);
					if (classId >= 0)
					{
						Set<SkillLearn> learns = parseSkillLearn(element, ClassId.valueOf(classId).getClassLevel(), race);
						if (general)
						{
							getHolder().addAllGeneralSkillLearns(classId, learns);
						}
						else
						{
							getHolder().addAllNormalSkillLearns(classId, learns);
						}
					}
					else
					{
						String classLevelStr = parseString(element, "class_level", null);
						if (!StringUtils.isEmpty(classLevelStr))
						{
							ClassLevel classLevel = ClassLevel.valueOf(classLevelStr.toUpperCase());
							Set<SkillLearn> learns = parseSkillLearn(element, classLevel, race);
							for (ClassId c : ClassId.VALUES)
							{
								if (c.isOfLevel(classLevel))
								{
									if (general)
									{
										getHolder().addAllGeneralSkillLearns(c.getId(), learns);
									}
									else
									{
										getHolder().addAllNormalSkillLearns(c.getId(), learns);
									}
								}
							}
						}
						else
						{
							Set<SkillLearn> learns = parseSkillLearn(element, ClassLevel.NONE, race);
							if (general)
							{
								getHolder().addAllGeneralSkillLearns(-1, learns);
							}
							else
							{
								getHolder().addAllNormalSkillLearns(-1, learns);
							}
						}
					}
				}
			}
		}
	}

	@Override
	protected void onParsed()
	{
		getHolder().init();
	}

	private Set<SkillLearn> parseSkillLearn(Element tree, ClassLevel classLevel, Race treeRace)
	{
		Set<SkillLearn> skillLearns = new HashSet<>();
		for (Element element : tree.elements("skill"))
		{
			int id = parseInt(element, "id");
			int level = parseInt(element, "level", 1);
			int cost = parseInt(element, "cost", 0);
			int minLevel = parseInt(element, "min_level", 0);
			boolean autoGet = parseBoolean(element, "auto_get", true);
			Race race = treeRace != null ? treeRace : element.attributeValue("race") == null ? null : Race.valueOf(element.attributeValue("race").toUpperCase());
			PledgeRank pledgeRank = PledgeRank.valueOfXml(parseString(element, "social_class", PledgeRank.VAGABOND.name()));

			Skill skill = SkillHolder.getInstance().getSkill(id, level);
			if (skill != null)
			{
				if ((skill.getSkillType() == SkillType.NOTDONE || skill.getSkillType() == SkillType.NOTUSED) && Config.ALT_LOG_NOTDONE_SKILLS)
					warn("Skill ID[" + id + "] LEVEL[" + level + "] not done!");
			}
			else
			{
				warn("Skill ID[" + id + "] LEVEL[" + level + "] doesn't exist!");
				continue;
			}

			SkillLearn skillLearn = new SkillLearn(id, level, minLevel, cost, autoGet, race, classLevel, pledgeRank);

			for (Element element1 : element.elements("required_items"))
			{
				for (Element element2 : element1.elements("item"))
				{
					int[] itemId1 = parseIntArr(element2, "id", ";");
					long itemCount1 = parseLong(element2, "count", 1L);
					skillLearn.addRequiredItem(itemId1, itemCount1);
				}
			}

			for (Element element1 : element.elements("blocked_skills"))
			{
				for (Element element2 : element1.elements("skill"))
				{
					int skillId1 = parseInt(element2, "id");
					int skillLevel1 = parseInt(element2, "level", 1);
					Skill skill1 = SkillHolder.getInstance().getSkill(skillId1, skillLevel1);
					if (skill1 != null)
					{
						skillLearn.getBlockedSkills().add(skill1);
					}
				}
			}

			Condition condition = parseFirstCond(element);
			if (condition != null)
			{
				skillLearn.addCondition(condition);
			}

			skillLearns.add(skillLearn);
		}
		return skillLearns;
	}

	private Set<SkillLearn> parseSkillLearn(Element tree)
	{
		return parseSkillLearn(tree, ClassLevel.NONE, null);
	}

	@Override
	protected Object getTableValue(String name, int... arg)
	{
		return null;
	}
}