package l2s.gameserver.network.l2.s2c;
import l2s.commons.network.PacketWriter;

/**
 * @author Bonux
 **/
public class ReceiveVipBotCaptchaImage implements IClientOutgoingPacket
{
	public ReceiveVipBotCaptchaImage()
	{
		//
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeQ(0x00); // TransactionID
		packetWriter.writeC(0x00); // TryCount
		packetWriter.writeD(0x00); // RemainTime
		return true;
	}
}