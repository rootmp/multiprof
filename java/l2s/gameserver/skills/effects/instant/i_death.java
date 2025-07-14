/*
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
package l2s.gameserver.skills.effects.instant;

import l2s.commons.util.Rnd;
import l2s.gameserver.model.Creature;
import l2s.gameserver.templates.skill.EffectTemplate;

/**
 * @author Hl4p3x for L2Scripts Essence
 * Kill character with a certain chance
 */
public class i_death extends i_abstract_effect
{
	int _chance = 100;

	public i_death(EffectTemplate template)
	{
		super(template);
		_chance = getParams().getInteger("chance", 100);
	}

	public void instantUse(Creature effector, Creature effected, boolean reflected)
	{
		if(!effector.isDead() && Rnd.get(100) < _chance)
		{
			effector.doDie(effector);
		}
	}
}
