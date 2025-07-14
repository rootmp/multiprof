package l2s.gameserver.network.l2.components;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import l2s.gameserver.model.Creature;
import l2s.gameserver.model.Playable;
import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.s2c.IClientOutgoingPacket;
import l2s.gameserver.network.l2.s2c.StatusUpdatePacket;
import l2s.gameserver.network.l2.s2c.StatusUpdatePacket.StatusType;
import l2s.gameserver.network.l2.s2c.StatusUpdatePacket.UpdateType;

public class StatusUpdate implements IBroadcastPacket
{
	private final Creature _creature;
	private final Creature _caster;
	private final StatusType _statusType;
	private final List<UpdateType> _fields;

	public StatusUpdate(Creature creature, Creature caster, StatusType statusType, List<UpdateType> fields)
	{
		_creature = creature;
		_caster = caster;
		_statusType = statusType;
		_fields = fields;
	}

	public StatusUpdate(Creature creature, StatusType statusType, List<UpdateType> fields)
	{
		this(creature, null, statusType, fields);
	}

	public StatusUpdate(Creature creature, Creature caster, StatusType statusType, UpdateType... fields)
	{
		_creature = creature;
		_caster = caster;
		_statusType = statusType;
		_fields = (fields != null) ? Arrays.asList(fields) : new ArrayList<UpdateType>();
	}

	public StatusUpdate(Creature creature, StatusType updateType, UpdateType... fields)
	{
		this(creature, null, updateType, fields);
	}

	@Override
	public IClientOutgoingPacket packet(Player player)
	{
		StatusUpdatePacket su = new StatusUpdatePacket(_statusType, _creature, _caster);
		for(UpdateType field : _fields)
		{
			if(!player.canReceiveStatusUpdate(_creature, _statusType, field))
				continue;

			switch(field)
			{
				case VCP_HP:
					su.addAttribute(field.getClientId(), (int) _creature.getCurrentHp());
					break;
				case VCP_MAXHP:
					su.addAttribute(field.getClientId(), _creature.getMaxHp());
					break;
				case VCP_MP:
					su.addAttribute(field.getClientId(), (int) _creature.getCurrentMp());
					break;
				case VCP_MAXMP:
					su.addAttribute(field.getClientId(), _creature.getMaxMp());
					break;
				default:
					break;
			}

			if(_creature.isPlayable())
			{
				Playable playable = (Playable) _creature;
				switch(field)
				{
					case VCP_CRIMINAL_RATE:
						su.addAttribute(field.getClientId(), playable.getKarma());
						break;
					case VCP_ISGUILTY:
						su.addAttribute(field.getClientId(), playable.getPvpFlag());
						break;
					default:
						break;
				}

				if(_creature.isPlayer())
				{
					switch(field)
					{
						case VCP_CP:
							su.addAttribute(field.getClientId(), (int) playable.getCurrentCp());
							break;
						case VCP_MAXCP:
							su.addAttribute(field.getClientId(), playable.getMaxCp());
							break;
						case VCP_CARRINGWEIGHT:
							su.addAttribute(field.getClientId(), playable.getCurrentLoad());
							break;
						case VCP_CARRYWEIGHT:
							su.addAttribute(field.getClientId(), playable.getMaxLoad());
							break;
						case VCP_DP:
							su.addAttribute(field.getClientId(), playable.getCurrentDp());
							break;
						case VCP_MAXDP:
							su.addAttribute(field.getClientId(), playable.getMaxDp());
							break;
						case VCP_BP:
							su.addAttribute(field.getClientId(), playable.getCurrentBp());
							break;
						case VCP_MAXBP:
							su.addAttribute(field.getClientId(), playable.getMaxBp());
							break;
						case VCP_AP:
							su.addAttribute(field.getClientId(), playable.getCurrentAp());
							break;
						case VCP_MAXAP:
							su.addAttribute(field.getClientId(), playable.getMaxAp());
							break;
						case VCP_LP:
							su.addAttribute(field.getClientId(), playable.getCurrentLp());
							break;
						case VCP_MAXLP:
							su.addAttribute(field.getClientId(), playable.getMaxLp());
							break;

						case VCP_WP:
							su.addAttribute(field.getClientId(), playable.getCurrentWp());
							break;
						case VCP_MAXWP:
							su.addAttribute(field.getClientId(), playable.getMaxWp());
							break;
						default:
							break;
					}
				}
			}
		}

		if(!su.hasAttributes())
			return null;

		return su;
	}
}
