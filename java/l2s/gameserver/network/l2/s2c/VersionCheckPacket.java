package l2s.gameserver.network.l2.s2c;

import l2s.commons.network.PacketWriter;
import l2s.gameserver.Config;

public class VersionCheckPacket implements IClientOutgoingPacket
{
	private byte[] _key;

	public VersionCheckPacket(byte[] key)
	{
		_key = key;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		if(_key == null || _key.length == 0)
		{
			packetWriter.writeC(0x00);
			return false;
		}
		packetWriter.writeC(0x01);
		for(int i = 0; i < 8; i++)
			packetWriter.writeC(_key[i]);
		packetWriter.writeD(0x01);
		packetWriter.writeD(Config.REQUEST_ID); // Server ID
		packetWriter.writeC(0x00); // Merged Server
		packetWriter.writeD(0x00); // Seed (obfuscation key)
		packetWriter.writeC(0x04); // 0x00 - Main, 0x01 - Classic, 0x04 - Essence
		packetWriter.writeC(0x00); // Arena
		packetWriter.writeC(0x00); // Unk
		return true;
	}
}