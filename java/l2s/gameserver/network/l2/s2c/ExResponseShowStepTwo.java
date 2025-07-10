package l2s.gameserver.network.l2.s2c;
import l2s.commons.network.PacketWriter;

import java.util.Collection;

import l2s.gameserver.model.Player;
import l2s.gameserver.model.petition.PetitionMainGroup;
import l2s.gameserver.model.petition.PetitionSubGroup;
import l2s.gameserver.utils.Language;

/**
 * @author VISTALL
 */
public class ExResponseShowStepTwo implements IClientOutgoingPacket
{
	private Language _language;
	private PetitionMainGroup _petitionMainGroup;

	public ExResponseShowStepTwo(Player player, PetitionMainGroup gr)
	{
		_language = player.getLanguage();
		_petitionMainGroup = gr;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		Collection<PetitionSubGroup> subGroups = _petitionMainGroup.getSubGroups();
		packetWriter.writeD(subGroups.size());
		packetWriter.writeS(_petitionMainGroup.getDescription(_language));
		for (PetitionSubGroup g : subGroups)
		{
			packetWriter.writeC(g.getId());
			packetWriter.writeS(g.getName(_language));
		}
	}
}