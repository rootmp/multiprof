package l2s.gameserver.model.actor.instances.player;

import l2s.commons.dao.JdbcEntity;
import l2s.commons.dao.JdbcEntityState;
import l2s.gameserver.dao.CharacterHennaDAO;
import l2s.gameserver.data.xml.holder.HennaHolder;
import l2s.gameserver.model.Player;
import l2s.gameserver.templates.henna.HennaTemplate;

/**
 * @author Bonux
 */
public class Henna implements JdbcEntity
{
	private final Player owner;
	private final int slot;

	private HennaTemplate template = null;
	private int enchantStep = 1;
	private int enchantExp = 0;
	private int potentialId = 0;

	private JdbcEntityState jdbcEntityState = JdbcEntityState.CREATED;

	public Henna(Player owner, int slot)
	{
		this.owner = owner;
		this.slot = slot;
	}

	public Player getOwner()
	{
		return owner;
	}

	public int getSlot()
	{
		return slot;
	}

	public HennaTemplate getTemplate()
	{
		return template;
	}

	public void setTemplate(HennaTemplate template)
	{
		this.template = template;
	}

	public int getId()
	{
		return template == null ? 0 : template.getSymbolId();
	}

	public boolean isActive()
	{
		return getSlot() < 4 || owner.getKnownSkill(45269) != null;
	}

	public int getEnchantStep()
	{
		return enchantStep;
	}

	public int getEnchantExp()
	{
		return enchantExp;
	}

	public void setEnchantExp(int enchantExp)
	{
		if (this.enchantExp == enchantExp)
			return;

		this.enchantExp = enchantExp;
		this.enchantStep = HennaHolder.getInstance().getEnchantStep(getEnchantExp());
	}

	public int getCurrentEnchantExp()
	{
		return HennaHolder.getInstance().getEnchantExp(getEnchantStep(), getEnchantExp());
	}

	public int getActiveStep()
	{
		if (template == null)
			return 0;

		int dyeLevel = template.getDyeLvl();
		if (dyeLevel <= 0)
			return 0;

		return Math.min(dyeLevel, getEnchantStep() - 1);
	}

	public int getPotentialId()
	{
		return potentialId;
	}

	public void setPotentialId(int potentialId)
	{
		this.potentialId = potentialId;
	}

	@Override
	public void setJdbcState(JdbcEntityState state)
	{
		jdbcEntityState = state;
	}

	@Override
	public JdbcEntityState getJdbcState()
	{
		return jdbcEntityState;
	}

	@Override
	public void save()
	{
		CharacterHennaDAO.getInstance().saveOrUpdate(this);
	}

	@Override
	public void update()
	{
		CharacterHennaDAO.getInstance().saveOrUpdate(this);
	}

	@Override
	public void delete()
	{
		CharacterHennaDAO.getInstance().delete(this);
	}

	@Override
	public String toString()
	{
		return "Henna[slot=" + slot + ", id=" + getId() + ", active=" + isActive() + ", enchantStep=" + getEnchantStep() + ", enchantExp=" + getEnchantExp() + ", activeStep=" + getActiveStep() + "]";
	}
}