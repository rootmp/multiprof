package l2s.gameserver.network.l2.s2c;
import l2s.commons.network.PacketWriter;

import l2s.gameserver.model.Player;

/**
 * @author Bonux
 **/
public class ExFishingEndPacket implements IClientOutgoingPacket
{
	public static final int FAIL = 0;
	public static final int WIN = 1;
	public static final int CANCELED = 2;

	private final int _charId;
	private final int _type;

	public ExFishingEndPacket(Player character, int type)
	{
		_charId = character.getObjectId();
		_type = type;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(_charId);
		packetWriter.writeC(_type);
	}
}