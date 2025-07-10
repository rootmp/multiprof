package l2s.gameserver.data.xml.parser;

import java.io.File;
import java.util.Iterator;

import org.dom4j.Element;

import l2s.commons.data.xml.AbstractParser;
import l2s.gameserver.Config;
import l2s.gameserver.data.xml.holder.PetDataHolder;
import l2s.gameserver.model.base.MountType;
import l2s.gameserver.model.base.PetType;
import l2s.gameserver.templates.StatsSet;
import l2s.gameserver.templates.item.data.RewardItemData;
import l2s.gameserver.templates.pet.PetData;
import l2s.gameserver.templates.pet.PetLevelData;
import l2s.gameserver.templates.pet.PetSkillData;

/**
 * @author Bonux
 */
public final class PetDataParser extends AbstractParser<PetDataHolder>
{
	private static final PetDataParser _instance = new PetDataParser();

	public static PetDataParser getInstance()
	{
		return _instance;
	}

	private PetDataParser()
	{
		super(PetDataHolder.getInstance());
	}

	@Override
	public File getXMLPath()
	{
		return new File(Config.DATAPACK_ROOT, "data/parser/pets/");
	}

	@Override
	public String getDTDFileName()
	{
		return "pet_data.dtd";
	}

	@Override
	protected void readData(Element rootElement) throws Exception
	{
		for (Iterator<Element> iterator = rootElement.elementIterator(); iterator.hasNext();)
		{
			Element element = iterator.next();

			final int index = -1; // TODO Just to avoid errors while implement new pet system
			final int npcId = Integer.parseInt(element.attributeValue("npc_id"));
			final int controlItemId = element.attributeValue("control_item") == null ? 0 : Integer.parseInt(element.attributeValue("control_item"));
			String[] sync_levels = element.attributeValue("sync_level") == null ? new String[0] : element.attributeValue("sync_level").split(";");
			final int[] syncLvls = new int[sync_levels.length];
			for (int i = 0; i < sync_levels.length; i++)
			{
				syncLvls[i] = Integer.parseInt(sync_levels[i]);
			}
			// TODO: [Bonux] final int[] evolve = "evolve";
			final PetType type = element.attributeValue("type") == null ? PetType.NORMAL : PetType.valueOf(element.attributeValue("type").toUpperCase());
			final MountType mountType = element.attributeValue("mount_type") == null ? MountType.NONE : MountType.valueOf(element.attributeValue("mount_type").toUpperCase());

			final int petType = element.attributeValue("pet_type") == null ? 0 : Integer.parseInt(element.attributeValue("pet_type"));
			final PetData template = new PetData(index, npcId, controlItemId, syncLvls, type, mountType, petType);
			for (Iterator<Element> secondIterator = element.elementIterator(); secondIterator.hasNext();)
			{
				Element secondElement = secondIterator.next();
				if ("skills".equalsIgnoreCase(secondElement.getName()))
				{
					for (Iterator<Element> thirdIterator = secondElement.elementIterator("skill"); thirdIterator.hasNext();)
					{
						Element thirdElement = thirdIterator.next();

						final int skillId = Integer.parseInt(thirdElement.attributeValue("id"));
						final int skillLevel = Integer.parseInt(thirdElement.attributeValue("level"));
						final int petMinLevel = Integer.parseInt(thirdElement.attributeValue("min_level"));

						template.addSkill(new PetSkillData(skillId, skillLevel, petMinLevel));
					}
				}
				else if ("expiration_reward_items".equalsIgnoreCase(secondElement.getName()))
				{
					for (Iterator<Element> thirdIterator = secondElement.elementIterator("item"); thirdIterator.hasNext();)
					{
						Element thirdElement = thirdIterator.next();

						final int itemId = Integer.parseInt(thirdElement.attributeValue("id"));
						final long minCount = Integer.parseInt(thirdElement.attributeValue("min_count"));
						final long maxCount = Integer.parseInt(thirdElement.attributeValue("max_count"));
						final double chance = thirdElement.attributeValue("chance") == null ? 100 : Integer.parseInt(thirdElement.attributeValue("chance"));

						template.addExpirationRewardItem(new RewardItemData(itemId, minCount, maxCount, chance));
					}
				}
				else if ("level_data".equalsIgnoreCase(secondElement.getName()))
				{
					for (Iterator<Element> thirdIterator = secondElement.elementIterator("stats"); thirdIterator.hasNext();)
					{
						Element thirdElement = thirdIterator.next();

						final int level = Integer.parseInt(thirdElement.attributeValue("level"));
						final StatsSet stats_set = new StatsSet();
						for (Iterator<Element> fourthIterator = thirdElement.elementIterator("set"); fourthIterator.hasNext();)
						{
							Element fourthElement = fourthIterator.next();
							stats_set.set(fourthElement.attributeValue("name"), fourthElement.attributeValue("value"));
						}

						template.addLvlData(level, new PetLevelData(stats_set));
					}
				}
			}
			getHolder().addTemplate(template);
		}
	}
}