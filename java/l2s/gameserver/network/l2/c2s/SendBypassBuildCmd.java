package l2s.gameserver.network.l2.c2s;
import l2s.commons.network.PacketReader;
import l2s.gameserver.network.l2.GameClient;


import l2s.gameserver.handler.admincommands.AdminCommandHandler;
import l2s.gameserver.model.Player;

public class SendBypassBuildCmd implements IClientIncomingPacket
{
	private String _command;

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		_command = packet.readS();

		if (_command != null)
			_command = _command.trim();
		return true;
	}

	@Override
	public void run(GameClient client)
	{
		Player activeChar = client.getActiveChar();

		if (activeChar == null)
			return;

		String cmd = _command;

		if (!cmd.contains("admin_"))
			cmd = "admin_" + cmd;

		AdminCommandHandler.getInstance().useAdminCommandHandler(activeChar, cmd);
	}
}