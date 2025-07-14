package l2s.gameserver.network.l2.s2c.timerestrictfield;

import java.util.HashMap;
import java.util.Map;

import l2s.commons.network.PacketWriter;
import l2s.gameserver.data.xml.holder.TimeRestrictFieldHolder;
import l2s.gameserver.instancemanager.ServerVariables;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.actor.variables.PlayerVariables;
import l2s.gameserver.network.l2.s2c.IClientOutgoingPacket;
import l2s.gameserver.templates.TimeRestrictFieldInfo;

/**
 * @author nexvill
 */
public class ExTimeRestrictFieldList implements IClientOutgoingPacket
{
	private final Player _player;
	private Map<Integer, TimeRestrictFieldInfo> _fields = new HashMap<>();

	public ExTimeRestrictFieldList(Player player)
	{
		_player = player;
		_fields = TimeRestrictFieldHolder.getInstance().getFields();
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(_fields.size());

		if(_fields.size() > 0)
		{
			for(int id : _fields.keySet())
			{
				final TimeRestrictFieldInfo field = _fields.get(id);

				packetWriter.writeD(1);
				packetWriter.writeD(field.getItemId());
				packetWriter.writeQ(field.getItemCount());
				packetWriter.writeD(field.getResetCycle());
				packetWriter.writeD(id);
				packetWriter.writeD(field.getMinLevel());
				packetWriter.writeD(field.getMaxLevel());

				int reflectionId = 0;
				switch(id)
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

				int remainTime = 0;
				if((_player.getReflection().getId() <= -1000) && (_player.getReflection().getId() == reflectionId))
				{
					remainTime = _player.getTimeRestrictFieldRemainTime();
				}
				else
				{
					remainTime = _player.getVarInt(PlayerVariables.RESTRICT_FIELD_TIMELEFT + "_" + reflectionId, field.getRemainTimeBase());
				}
				int remainTimeRefill = _player.getVarInt(PlayerVariables.RESTRICT_FIELD_TIMELEFT + "_" + reflectionId + "_refill", field.getRemainTimeMax()
						- field.getRemainTimeBase());

				boolean used = _player.getVarBoolean(PlayerVariables.RESTRICT_FIELD_USED, false);

				packetWriter.writeD(field.getRemainTimeBase());
				packetWriter.writeD(remainTime);
				packetWriter.writeD(field.getRemainTimeMax());
				packetWriter.writeD(remainTimeRefill);
				packetWriter.writeD(field.getRemainTimeMax() - field.getRemainTimeBase());
				if(((id == 18) && !ServerVariables.getBool("frost_lord_castle_open", false))
						|| ((id == 12) && !ServerVariables.getBool("antharas_lair_open", false)))
				{
					packetWriter.writeC(0);
				}
				else
				{
					packetWriter.writeC(1); // is field active
				}
				if(id > 100)
				{
					packetWriter.writeC(used ? 1 : 0);
				}
				else
				{
					packetWriter.writeC(0);
				}
				packetWriter.writeC(0); // can re-enter
				packetWriter.writeC(0);
				packetWriter.writeC(0);
				packetWriter.writeC(field.isWorld()); // is cross-server field
			}
		}
		return true;
	}
}