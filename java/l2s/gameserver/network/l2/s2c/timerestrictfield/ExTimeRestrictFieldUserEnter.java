package l2s.gameserver.network.l2.s2c.timerestrictfield;

import java.util.HashMap;
import java.util.Map;

import l2s.gameserver.data.xml.holder.TimeRestrictFieldHolder;
import l2s.gameserver.instancemanager.ReflectionManager;
import l2s.gameserver.instancemanager.TranscendentInstanceZone40;
import l2s.gameserver.instancemanager.TranscendentInstanceZone50;
import l2s.gameserver.instancemanager.TranscendentInstanceZone60;
import l2s.gameserver.instancemanager.TranscendentInstanceZone70;
import l2s.gameserver.instancemanager.TranscendentInstanceZone80;
import l2s.gameserver.instancemanager.TranscendentInstanceZone85;
import l2s.gameserver.instancemanager.TranscendentInstanceZone90;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.actor.variables.PlayerVariables;
import l2s.gameserver.network.l2.components.SystemMsg;
import l2s.gameserver.network.l2.s2c.IClientOutgoingPacket;
import l2s.commons.network.PacketWriter;
import l2s.gameserver.templates.TimeRestrictFieldInfo;
import l2s.gameserver.utils.ReflectionUtils;

/**
 * @author nexvill
 */
public class ExTimeRestrictFieldUserEnter implements IClientOutgoingPacket
{
	private final Player _player;
	private final int _fieldId;
	private Map<Integer, TimeRestrictFieldInfo> _fields = new HashMap<>();

	public ExTimeRestrictFieldUserEnter(Player player, int fieldId)
	{
		_player = player;
		_fieldId = fieldId;
		_fields = TimeRestrictFieldHolder.getInstance().getFields();
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		TimeRestrictFieldInfo field = _fields.get(_fieldId);

		if (_fieldId > 100)
		{
			boolean used = _player.getVarBoolean(PlayerVariables.RESTRICT_FIELD_USED, false);
			if (!used && _player.consumeItem(field.getItemId(), field.getItemCount(), true))
			{
				int izId = 0;
				if (field.getFieldId() < 105)
				{
					izId = field.getFieldId() + 107;
				}
				else
				{
					izId = field.getFieldId() + 106;
				}
				switch (izId)
				{
					case 208:
					{
						ReflectionUtils.enterReflection(_player, new TranscendentInstanceZone40(), izId);
						break;
					}
					case 209:
					{
						ReflectionUtils.enterReflection(_player, new TranscendentInstanceZone50(), izId);
						break;
					}
					case 210:
					{
						ReflectionUtils.enterReflection(_player, new TranscendentInstanceZone60(), izId);
						break;
					}
					case 211:
					{
						ReflectionUtils.enterReflection(_player, new TranscendentInstanceZone70(), izId);
						break;
					}
					case 212:
					{
						ReflectionUtils.enterReflection(_player, new TranscendentInstanceZone80(), izId);
						break;
					}
					case 213:
					{
						ReflectionUtils.enterReflection(_player, new TranscendentInstanceZone85(), izId);
						break;
					}
					case 215:
					{
						ReflectionUtils.enterReflection(_player, new TranscendentInstanceZone90(), izId);
					}
				}
				packetWriter.writeC(1);
				packetWriter.writeD(_fieldId);
				packetWriter.writeD(0);
				packetWriter.writeD(0);
			}
			return true;
		}

		int reflectionId = 0;
		switch (field.getFieldId())
		{
			case 1:
			{
				reflectionId = -1000;
				break;
			}
			case 4:
			{
				reflectionId = -1002;
				break;
			}
			case 11:
			{
				reflectionId = -1001;
				break;
			}
			case 12:
			{
				reflectionId = -1004;
				break;
			}
			case 18:
			{
				reflectionId = -1003;
				break;
			}
		}

		int remainTime = _player.getVarInt(PlayerVariables.RESTRICT_FIELD_TIMELEFT + "_" + reflectionId, field.getRemainTimeBase());

		if (_player.consumeItem(field.getItemId(), field.getItemCount(), true))
		{
			_player.setVar(PlayerVariables.RESTRICT_FIELD_TIMESTART + "_" + reflectionId, System.currentTimeMillis());
			_player.setVar(PlayerVariables.RESTRICT_FIELD_TIMELEFT + "_" + reflectionId, remainTime);

			switch (field.getFieldId())
			{
				case 1:
				{
					_player.teleToLocation(field.getEnterLoc(), ReflectionManager.PRIMEVAL_ISLE);
					_player.setReflection(ReflectionManager.PRIMEVAL_ISLE);
					_player.setActiveReflection(ReflectionManager.PRIMEVAL_ISLE);
					break;
				}
				case 4:
				{
					_player.teleToLocation(field.getEnterLoc(), ReflectionManager.FORGOTTEN_PRIMEVAL_GARDEN);
					_player.setReflection(ReflectionManager.FORGOTTEN_PRIMEVAL_GARDEN);
					_player.setActiveReflection(ReflectionManager.FORGOTTEN_PRIMEVAL_GARDEN);
					break;
				}
				case 11:
				{
					_player.teleToLocation(field.getEnterLoc(), ReflectionManager.ALLIGATOR_ISLAND);
					_player.setReflection(ReflectionManager.ALLIGATOR_ISLAND);
					_player.setActiveReflection(ReflectionManager.ALLIGATOR_ISLAND);
					break;
				}
				case 12:
				{
					_player.teleToLocation(field.getEnterLoc(), ReflectionManager.ANTHARAS_LAIR);
					_player.setReflection(ReflectionManager.ANTHARAS_LAIR);
					_player.setActiveReflection(ReflectionManager.ANTHARAS_LAIR);
					break;
				}
				case 18:
				{
					_player.teleToLocation(field.getEnterLoc(), ReflectionManager.FROST_LORD_CASTLE);
					_player.setReflection(ReflectionManager.FROST_LORD_CASTLE);
					_player.setActiveReflection(ReflectionManager.FROST_LORD_CASTLE);
					break;
				}
			}

			_player.startTimeRestrictField();

			packetWriter.writeC(1);
			packetWriter.writeD(_fieldId);
			packetWriter.writeD((int) (System.currentTimeMillis() / 1000));
			packetWriter.writeD(remainTime);
		}
		else
		{
			_player.sendPacket(SystemMsg.YOU_DO_NOT_HAVE_ENOUGH_REQUIRED_ITEMS);
		}
		return true;
	}
}