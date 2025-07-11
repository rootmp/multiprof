package l2s.gameserver.network.l2.s2c.newhenna;

import l2s.commons.network.PacketWriter;
import l2s.gameserver.network.l2.s2c.IClientOutgoingPacket;

public class ExNewHennaPotenOpenslot implements IClientOutgoingPacket
{

	private int nResult;
	private int nSlotID;
	private int nOpenedSlotStep;
	private int nActiveStep;

	public ExNewHennaPotenOpenslot()
	{
		System.out.println("NOTDONE " + this.getClass().getSimpleName()); 
	}
	
	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(nResult);
		packetWriter.writeD(nSlotID);
		packetWriter.writeD(nOpenedSlotStep);
		packetWriter.writeD(nActiveStep);

		return true;
	}
}
