package l2s.gameserver.network.l2.s2c;
import l2s.commons.network.PacketWriter;

import l2s.gameserver.model.Player;

/**
 * @author nexvill
 */
public class ExVitalExInfo implements IClientOutgoingPacket
{
	private final int _vitalityBonus;
	private final int _additionalBonus;
	private final Player _player;

	public ExVitalExInfo(Player player, int baseVitalityBonus, int additionalBonus)
	{
		_vitalityBonus = baseVitalityBonus;
		_additionalBonus = additionalBonus;
		_player = player;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
	    packetWriter.writeD((int) (_player.getLimitedSayhaGraceEndTime() / 1000)); // currentmilis / 1000, when limited sayha ends
	    packetWriter.writeD((int) (_player.getSayhaGraceSupportEndTime() / 1000)); // currentmilis / 1000, when sayha grace suport ends
	    packetWriter.writeD(_vitalityBonus); // Limited sayha bonus
	    packetWriter.writeD(_additionalBonus); // Limited sayha bonus adena (shown as 130%, actually 30%)
	}
}
