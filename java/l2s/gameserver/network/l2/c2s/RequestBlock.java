package l2s.gameserver.network.l2.c2s;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import l2s.commons.network.PacketReader;
import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.GameClient;
import l2s.gameserver.network.l2.components.SystemMsg;
import l2s.gameserver.network.l2.s2c.BlockListPacket;

public class RequestBlock implements IClientIncomingPacket
{
	// format: cd(S)
	private static final Logger _log = LoggerFactory.getLogger(RequestBlock.class);

	private final static int BLOCK = 0;
	private final static int UNBLOCK = 1;
	private final static int BLOCKLIST = 2;
	private final static int ALLBLOCK = 3;
	private final static int ALLUNBLOCK = 4;

	private Integer _type;
	private String targetName = null;

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		_type = packet.readD(); // 0x00 - block, 0x01 - unblock, 0x03 - allblock, 0x04 - allunblock

		if (_type == BLOCK || _type == UNBLOCK)
			targetName = packet.readS(16);
		return true;
	}

	@Override
	public void run(GameClient client)
	{
		Player activeChar = client.getActiveChar();
		if (activeChar == null)
			return;

		switch (_type)
		{
			case BLOCK:
				activeChar.getBlockList().add(targetName);
				break;
			case UNBLOCK:
				activeChar.getBlockList().remove(targetName);
				break;
			case BLOCKLIST:
				activeChar.sendPacket(new BlockListPacket(activeChar));
				break;
			case ALLBLOCK:
				activeChar.setBlockAll(true);
				activeChar.sendPacket(SystemMsg.YOU_ARE_NOW_BLOCKING_EVERYTHING);
				activeChar.sendEtcStatusUpdate();
				break;
			case ALLUNBLOCK:
				activeChar.setBlockAll(false);
				activeChar.sendPacket(SystemMsg.YOU_ARE_NO_LONGER_BLOCKING_EVERYTHING);
				activeChar.sendEtcStatusUpdate();
				break;
			default:
				_log.info("Unknown 0x0a block type: " + _type);
		}
	}
}