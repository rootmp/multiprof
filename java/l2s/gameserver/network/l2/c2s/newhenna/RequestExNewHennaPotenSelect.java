package l2s.gameserver.network.l2.c2s.newhenna;

import l2s.commons.network.PacketReader;
import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.GameClient;
import l2s.gameserver.network.l2.c2s.IClientIncomingPacket;
import l2s.gameserver.network.l2.s2c.newhenna.NewHennaList;

public class RequestExNewHennaPotenSelect implements IClientIncomingPacket
{
	private int _slotId;
	private int _potenId;

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		_slotId = packet.readC();
		_potenId = packet.readD();
		return true;
	}

	@Override
	public void run(GameClient client) throws Exception
	{
		final Player player = client.getActiveChar();
		if(player == null)
			return;

		if((_slotId < 1) || (_slotId > player.getHennaPotenList().length))
			return;

		player.potenSelect(_slotId, _potenId);
		player.applyDyePotenSkills();
		player.sendPacket(new NewHennaList(player, 0));
	}

}
