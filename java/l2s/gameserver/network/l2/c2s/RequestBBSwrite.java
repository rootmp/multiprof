package l2s.gameserver.network.l2.c2s;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import l2s.commons.network.PacketReader;
import l2s.gameserver.Config;
import l2s.gameserver.handler.bbs.BbsHandlerHolder;
import l2s.gameserver.handler.bbs.IBbsHandler;
import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.GameClient;
import l2s.gameserver.network.l2.components.SystemMsg;
import l2s.gameserver.utils.BypassStorage.ValidBypass;

/**
 * Format SSSSSS
 */
public class RequestBBSwrite implements IClientIncomingPacket
{
	private static final Logger _log = LoggerFactory.getLogger(RequestBBSwrite.class);

	private String _url;
	private String _arg1;
	private String _arg2;
	private String _arg3;
	private String _arg4;
	private String _arg5;

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		_url = packet.readS();
		_arg1 = packet.readS();
		_arg2 = packet.readS();
		_arg3 = packet.readS();
		_arg4 = packet.readS();
		_arg5 = packet.readS();
		return true;
	}

	@Override
	public void run(GameClient client)
	{
		Player activeChar = client.getActiveChar();
		if(activeChar == null)
			return;

		ValidBypass bp = activeChar.getBypassStorage().validate(_url);
		if (bp == null)
		{
			_log.warn("RequestBBSwrite: Unexpected bypass : " + _url + " client : " + client + "!");
			return;
		}

		if (!Config.BBS_ENABLED)
		{
			activeChar.sendPacket(SystemMsg.THE_COMMUNITY_SERVER_IS_CURRENTLY_OFFLINE);
			return;
		}

		IBbsHandler handler = BbsHandlerHolder.getInstance().getCommunityHandler(bp.bypass);
		if (handler != null)
			handler.onWriteCommand(activeChar, bp.bypass, _arg1, _arg2, _arg3, _arg4, _arg5);
	}
}