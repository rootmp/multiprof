package l2s.gameserver.network.l2.s2c.enchant;

import l2s.gameserver.network.l2.s2c.IClientOutgoingPacket;
import l2s.commons.network.PacketWriter;

/**
 * @author nexvill
 */
public class ExEnchantChallengePointInfo implements IClientOutgoingPacket
{
	public ExEnchantChallengePointInfo()
	{
		//
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		int count = 1;
		packetWriter.writeD(count);
		for (int i = 0; i < count; i++)
		{
			packetWriter.writeD(0); // points group id
			packetWriter.writeD(0); // challenge point
			packetWriter.writeD(0); // ticket point opt1
			packetWriter.writeD(0); // ticket point opt2
			packetWriter.writeD(0); // ticket point opt3
			packetWriter.writeD(0); // ticket point opt4
			packetWriter.writeD(0); // ticket point opt5
			packetWriter.writeD(0); // ticket point opt6
		}
		return true;
	}
}