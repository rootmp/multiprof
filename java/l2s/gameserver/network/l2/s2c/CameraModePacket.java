package l2s.gameserver.network.l2.s2c;

import l2s.commons.network.PacketWriter;

public class CameraModePacket implements IClientOutgoingPacket
{
	public static final IClientOutgoingPacket EXIT = new CameraModePacket(0);
	public static final IClientOutgoingPacket ENTER = new CameraModePacket(1);

	private final int mode;

	/**
	 * Forces client camera mode change
	 * 
	 * @param mode 0 - third person cam 1 - first person cam
	 */
	private CameraModePacket(int mode)
	{
		this.mode = mode;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(mode);
		return true;
	}
}