package l2s.gameserver.network.l2.c2s;
import l2s.commons.network.PacketReader;
import l2s.gameserver.network.l2.GameClient;


import l2s.gameserver.Config;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.petition.PetitionMainGroup;
import l2s.gameserver.model.petition.PetitionSubGroup;
import l2s.gameserver.network.l2.s2c.ExResponseShowContents;

/**
 * @author VISTALL
 */
public class RequestExShowStepThree implements IClientIncomingPacket
{
	private int _subId;

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		_subId = packet.readC();
		return true;
	}

	@Override
	public void run(GameClient client)
	{
		Player player = client.getActiveChar();
		if (player == null || !Config.EX_NEW_PETITION_SYSTEM)
			return;

		PetitionMainGroup group = player.getPetitionGroup();
		if (group == null)
			return;

		PetitionSubGroup subGroup = group.getSubGroup(_subId);
		if (subGroup == null)
			return;

		player.sendPacket(new ExResponseShowContents(subGroup.getDescription(player.getLanguage())));
	}
}