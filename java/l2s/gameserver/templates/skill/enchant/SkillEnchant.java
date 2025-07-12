package l2s.gameserver.templates.skill.enchant;

import java.util.ArrayList;
import java.util.List;

import l2s.gameserver.dao.CharacterSkillEnchantDAO;
import l2s.gameserver.model.Player;

public class SkillEnchant
{
	private final Player _player;
	private final List<SkillEnchantInfo> skillEnchantList = new ArrayList<>();

	public SkillEnchant(Player player)
	{
		_player = player;
	}

	public void load()
	{
		skillEnchantList.addAll(CharacterSkillEnchantDAO.getInstance().restore(_player));
	}

	public void update()
	{
		for(SkillEnchantInfo enchant : skillEnchantList)
		{
			CharacterSkillEnchantDAO.getInstance().store(_player, enchant);
		}
	}

	public void update(SkillEnchantInfo enchant)
	{
		CharacterSkillEnchantDAO.getInstance().store(_player, enchant);
	}

	public SkillEnchantInfo addEnchant(int skillID, int subLevel, int exp)
	{
		SkillEnchantInfo enchant = new SkillEnchantInfo(skillID, subLevel, exp);
		skillEnchantList.add(enchant);
		update(enchant);
		return enchant;
	}

	public SkillEnchantInfo findEnchant(int skillID, int subLevel)
	{
		for(SkillEnchantInfo enchant : skillEnchantList)
		{
			if(enchant.getSkillID() == skillID && enchant.getSubLevel() == subLevel)
				return enchant;
		}
		return addEnchant(skillID, subLevel,0); // Enchant not found
	}
}
