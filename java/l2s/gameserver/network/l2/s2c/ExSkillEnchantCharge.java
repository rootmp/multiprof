package l2s.gameserver.network.l2.s2c;

import l2s.commons.network.PacketWriter;

public class ExSkillEnchantCharge implements IClientOutgoingPacket
{
	private int _nSkillID;

	public ExSkillEnchantCharge(int nSkillID)
	{
		_nSkillID = nSkillID;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(_nSkillID);//nSkillID
		packetWriter.writeC(0);//cReault
		return true;
	}
}