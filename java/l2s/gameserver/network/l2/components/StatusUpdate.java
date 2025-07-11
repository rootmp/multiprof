package l2s.gameserver.network.l2.components;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import l2s.gameserver.model.Creature;
import l2s.gameserver.model.Playable;
import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.s2c.IClientOutgoingPacket;
import l2s.gameserver.network.l2.s2c.StatusUpdatePacket;
import l2s.gameserver.network.l2.s2c.StatusUpdatePacket.UpdateType;

/**
 * @author Bonux
 **/
public class StatusUpdate implements IBroadcastPacket
{
	private static final Logger _log = LoggerFactory.getLogger(StatusUpdate.class);

	private final Creature _creature;
	private final Creature _caster;
	private final UpdateType _updateType;
	private final int[] _fields;

	public StatusUpdate(Creature creature, Creature caster, UpdateType updateType, int... fields)
	{
		_creature = creature;
		_caster = caster;
		_updateType = updateType;
		_fields = fields;
	}

	public StatusUpdate(Creature creature, UpdateType updateType, int... fields)
	{
		this(creature, null, updateType, fields);
	}

	@Override
	public IClientOutgoingPacket packet(Player player)
	{
		StatusUpdatePacket su = new StatusUpdatePacket(_updateType, _creature, _caster);
		for (int field : _fields)
		{
			if (!player.canReceiveStatusUpdate(_creature, _updateType, field))
				continue;

			switch (field)
			{
				case StatusUpdatePacket.CUR_HP:
					su.addAttribute(field, (int) _creature.getCurrentHp());
					break;
				case StatusUpdatePacket.MAX_HP:
					su.addAttribute(field, _creature.getMaxHp());
					break;
				case StatusUpdatePacket.CUR_MP:
					su.addAttribute(field, (int) _creature.getCurrentMp());
					break;
				case StatusUpdatePacket.MAX_MP:
					su.addAttribute(field, _creature.getMaxMp());
					break;
			}

			if (_creature.isPlayable())
			{
				Playable playable = (Playable) _creature;
				switch (field)
				{
					case StatusUpdatePacket.KARMA:
						su.addAttribute(field, playable.getKarma());
						break;
					case StatusUpdatePacket.PVP_FLAG:
						su.addAttribute(field, playable.getPvpFlag());
						break;
				}

				if (_creature.isPlayer())
				{
					switch (field)
					{
						case StatusUpdatePacket.CUR_CP:
							su.addAttribute(field, (int) playable.getCurrentCp());
							break;
						case StatusUpdatePacket.MAX_CP:
							su.addAttribute(field, playable.getMaxCp());
							break;
						case StatusUpdatePacket.CUR_LOAD:
							su.addAttribute(field, playable.getCurrentLoad());
							break;
						case StatusUpdatePacket.MAX_LOAD:
							su.addAttribute(field, playable.getMaxLoad());
							break;
						case StatusUpdatePacket.CUR_DP:
							su.addAttribute(field, playable.getCurrentDp());
							break;
						case StatusUpdatePacket.MAX_DP:
							su.addAttribute(field, playable.getMaxDp());
							break;
						case StatusUpdatePacket.CUR_BP:
							su.addAttribute(field, playable.getCurrentBp());
							break;
						case StatusUpdatePacket.MAX_BP:
							su.addAttribute(field, playable.getMaxBp());
							break;
					}
				}
			}
		}

		if (!su.hasAttributes())
			return null;

		return su;
	}
}
