package l2s.gameserver.network.l2.s2c;

import l2s.commons.network.PacketWriter;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.actor.instances.player.AutoFarm;

public class ExAutoplaySetting implements IClientOutgoingPacket
{
	private final AutoFarm _autoFarm;

	public ExAutoplaySetting(Player player)
	{
		_autoFarm = player.getAutoFarm();
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeH(16);
		packetWriter.writeC(_autoFarm.isFarmActivate());
		packetWriter.writeC(_autoFarm.isAutoPickUpItems());
		packetWriter.writeH(_autoFarm.getTargetType().ordinal());
		packetWriter.writeC(_autoFarm.isMeleeAttackMode());
		packetWriter.writeD(_autoFarm.getHealPercent()); // Auto Heal Percent
		packetWriter.writeD(_autoFarm.getPetHealPercent()); // new 272
		packetWriter.writeC(_autoFarm.isPoliteFarm());
		return true;
	}
}
