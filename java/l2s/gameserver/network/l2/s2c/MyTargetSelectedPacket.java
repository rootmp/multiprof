package l2s.gameserver.network.l2.s2c;

import l2s.commons.network.PacketWriter;
import l2s.gameserver.model.Creature;
import l2s.gameserver.model.GameObject;
import l2s.gameserver.model.Player;

public class MyTargetSelectedPacket implements IClientOutgoingPacket
{
	private final boolean _success;
	private int _objectId;
	private int _color;
	private final boolean _actionMenu;

	/**
	 * @param int objectId of the target
	 * @param int level difference to the target. name color is calculated from that
	 */
	public MyTargetSelectedPacket(int objectId, int color, boolean actionMenu)
	{
		_success = true;
		_objectId = objectId;
		_color = color;
		_actionMenu = actionMenu;
	}

	public MyTargetSelectedPacket(int objectId, int color)
	{
		this(objectId, color, false);
	}

	public MyTargetSelectedPacket(Player player, GameObject target, boolean actionMenu)
	{
		_success = true;
		_objectId = target.getObjectId();
		if(target.isCreature())
			_color = player.getLevel() - ((Creature) target).getLevel();
		else
			_color = 0;
		_actionMenu = actionMenu;
	}

	public MyTargetSelectedPacket(Player player, GameObject target)
	{
		this(player, target, false);
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(_success ? 0x01 : 0x00);
		if(_success)
		{
			packetWriter.writeD(_objectId);
			packetWriter.writeH(_color);
			packetWriter.writeD(_actionMenu ? 0x03 : 0x00); // TargetSelectionMode (0 - Standart, 3 - Context menu (With ALT))
		}
		return true;
	}
}