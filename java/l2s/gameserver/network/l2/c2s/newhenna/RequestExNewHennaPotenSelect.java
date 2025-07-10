package l2s.gameserver.network.l2.c2s.newhenna;

import l2s.gameserver.data.xml.holder.HennaHolder;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.actor.instances.player.Henna;
import l2s.gameserver.network.l2.c2s.L2GameClientPacket;
import l2s.gameserver.network.l2.s2c.newhenna.ExNewHennaPotenSelect;
import l2s.gameserver.templates.henna.PotentialEffect;

public class RequestExNewHennaPotenSelect implements IClientIncomingPacket
{
	private int cSlotID;
	private int nPotenID;

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		cSlotID = packet.readC();
		nPotenID = packet.readD();
		return true;
	}

	@Override
	public void run(GameClient client)
	{
		Player activeChar = client.getActiveChar();
		if (activeChar == null)
			return;

		Henna henna = activeChar.getHennaList().get(cSlotID);
		if (henna == null)
			return;

		PotentialEffect potentialEffect = HennaHolder.getInstance().getPotentialEffect(nPotenID);
		if (potentialEffect == null || potentialEffect.getSlotId() != henna.getSlot())
		{
			activeChar.sendPacket(new ExNewHennaPotenSelect(henna, false));
			return;
		}

		henna.setPotentialId(nPotenID);
		henna.updated(false);

		activeChar.sendPacket(new ExNewHennaPotenSelect(henna, true));
		activeChar.getHennaList().refreshStats(true);
	}
}