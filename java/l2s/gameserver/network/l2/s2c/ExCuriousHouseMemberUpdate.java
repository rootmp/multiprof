package l2s.gameserver.network.l2.s2c;
import l2s.commons.network.PacketWriter;

import l2s.gameserver.model.Player;

public class ExCuriousHouseMemberUpdate implements IClientOutgoingPacket
{
	private Player _player;

	public ExCuriousHouseMemberUpdate(Player player)
	{
		_player = player;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(_player.getObjectId());
		packetWriter.writeD(_player.getMaxHp());
		packetWriter.writeD(_player.getMaxCp());
		packetWriter.writeD((int) _player.getCurrentHp());
		packetWriter.writeD((int) _player.getCurrentCp());
	}
}
