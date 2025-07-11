package l2s.gameserver.network.l2.s2c;

import l2s.commons.network.PacketWriter;

public class ExStartScenePlayer implements IClientOutgoingPacket
{
	private final int _sceneId;

	public ExStartScenePlayer(int sceneId)
	{
		_sceneId = sceneId;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(_sceneId);
		packetWriter.writeD(-1); // TODO[UNDERGROUND]: UNK
		return true;
	}
}