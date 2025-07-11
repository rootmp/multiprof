package l2s.gameserver.network.l2.s2c.adenlab;

import l2s.commons.network.PacketWriter;
import l2s.gameserver.network.l2.s2c.IClientOutgoingPacket;

public class ExAdenlabTranscendProb implements IClientOutgoingPacket
{
	private int nBossID;
	private int[] probs;


	public ExAdenlabTranscendProb(int nBossID, int[] probs)
	{
		this.nBossID = nBossID;
		this.probs = probs;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(nBossID);
		packetWriter.writeD(probs.length);
		for(int prob : probs)
			packetWriter.writeD(prob);
		return true;
	}
}
