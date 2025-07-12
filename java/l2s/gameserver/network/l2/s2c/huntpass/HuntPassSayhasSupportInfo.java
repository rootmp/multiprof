package l2s.gameserver.network.l2.s2c.huntpass;

import l2s.commons.network.PacketWriter;
import l2s.gameserver.model.HuntPass;
import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.s2c.IClientOutgoingPacket;

public class HuntPassSayhasSupportInfo implements IClientOutgoingPacket
{
	private final HuntPass _huntPass;
	private final int _timeUsed;
	private final boolean _sayhaToggle;

	public HuntPassSayhasSupportInfo(Player player)
	{
		_huntPass = player.getHuntPass();
		_sayhaToggle = _huntPass.toggleSayha();
		_timeUsed = _huntPass.getUsedSayhaTime() + (int) (_huntPass.getToggleStartTime() > 0 ? (System.currentTimeMillis() / 1000) - _huntPass.getToggleStartTime() : 0);
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeC(_sayhaToggle ? 1 : 0);
		packetWriter.writeD(_huntPass.getAvailableSayhaTime());
		packetWriter.writeD(_timeUsed);
		return true;
	}
}
