package l2s.gameserver.network.l2.s2c;

import l2s.commons.network.PacketWriter;

public class ExUpgradeSystemResult implements IClientOutgoingPacket
{
	private final int result;
	private final int objectId;

	public ExUpgradeSystemResult(int result, int objectId)
	{
		this.result = result;
		this.objectId = objectId;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeH(result);
		packetWriter.writeD(objectId);
		return true;
	}
}