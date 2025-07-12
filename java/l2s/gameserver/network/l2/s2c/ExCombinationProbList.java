package l2s.gameserver.network.l2.s2c;

import l2s.commons.network.PacketWriter;

public class ExCombinationProbList implements IClientOutgoingPacket
{
	private int nOneSlotServerID;
	private int nTwoSlotServerID;
	private int nProb;

	public ExCombinationProbList(int nOneSlotServerID, int nTwoSlotServerID, int nProb)
	{
		this.nOneSlotServerID = nOneSlotServerID;
		this.nTwoSlotServerID = nTwoSlotServerID;
		this.nProb = nProb;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(nOneSlotServerID);
		packetWriter.writeD(nTwoSlotServerID);
		packetWriter.writeD(nProb);
		return true;
	}
}
