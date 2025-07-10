package l2s.gameserver.network.l2.s2c;
import l2s.commons.network.PacketWriter;

import l2s.gameserver.model.Servitor;

public class ExPartyPetWindowDelete implements IClientOutgoingPacket
{
	private int _summonObjectId;
	private int _ownerObjectId;
	private int _type;
	private String _summonName;

	public ExPartyPetWindowDelete(Servitor summon)
	{
		_summonObjectId = summon.getObjectId();
		_summonName = summon.getName();
		_type = summon.getServitorType();
		_ownerObjectId = summon.getPlayer().getObjectId();
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(_summonObjectId);
		packetWriter.writeD(_type);
		packetWriter.writeD(_ownerObjectId);
		packetWriter.writeS(_summonName);
	}
}