package l2s.gameserver.network.l2.c2s;
import l2s.commons.network.PacketReader;
import l2s.gameserver.Config;
import l2s.gameserver.data.xml.holder.PetitionGroupHolder;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.petition.PetitionMainGroup;
import l2s.gameserver.network.l2.GameClient;
import l2s.gameserver.network.l2.s2c.ExResponseShowStepTwo;

/**
 * @author VISTALL
 */
public class RequestExShowStepTwo implements IClientIncomingPacket
{
	private int _petitionGroupId;

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		_petitionGroupId = packet.readC();
		return true;
	}

	@Override
	public void run(GameClient client)
	{
		Player player = client.getActiveChar();
		if (player == null || !Config.EX_NEW_PETITION_SYSTEM)
			return;

		PetitionMainGroup group = PetitionGroupHolder.getInstance().getPetitionGroup(_petitionGroupId);
		if (group == null)
			return;

		player.setPetitionGroup(group);
		player.sendPacket(new ExResponseShowStepTwo(player, group));
	}
}