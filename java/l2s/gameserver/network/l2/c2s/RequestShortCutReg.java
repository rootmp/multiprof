package l2s.gameserver.network.l2.c2s;
import l2s.commons.network.PacketReader;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.actor.instances.player.ShortCut;
import l2s.gameserver.network.l2.GameClient;
import l2s.gameserver.network.l2.components.IBroadcastPacket;
import l2s.gameserver.network.l2.s2c.ShortCutRegisterPacket;

public class RequestShortCutReg implements IClientIncomingPacket
{
	private ShortCut.ShortCutType _type;
	private int _id, _slot, _page, _lvl, _characterType;

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		try
		{
			_type = ShortCut.ShortCutType.VALUES[packet.readD()];
		}
		catch (Exception e)
		{
			return false;
		}
		int slot = packet.readD();
		int unk0 = packet.readC();
		_id = packet.readD();
		_lvl = packet.readD();
		_characterType = packet.readD();

		int unk1 = packet.readD(); // UNK
		int unk2 = packet.readD(); // UNK

		_slot = slot % 12;
		_page = slot / 12;
		return true;
	}

	@Override
	public void run(GameClient client)
	{
		Player activeChar = client.getActiveChar();
		if (activeChar == null)
			return;

		if (_page < 0 || _page > ShortCut.PAGE_MAX)
		{
			activeChar.sendActionFailed();
			return;
		}

		IBroadcastPacket msg = activeChar.getAutoShortCuts().canRegShortCut(_slot, _page, _type, _id);
		if (msg != null)
		{
			activeChar.sendPacket(msg);
			return;
		}

		ShortCut shortCut = new ShortCut(_slot, _page, false, _type, _id, _lvl, _characterType);
		activeChar.sendPacket(new ShortCutRegisterPacket(activeChar, shortCut));
		activeChar.registerShortCut(shortCut);

		if ((_slot == 1) && (_page == 23))
			activeChar.getAutoShortCuts().activate(277, true);
	}
}