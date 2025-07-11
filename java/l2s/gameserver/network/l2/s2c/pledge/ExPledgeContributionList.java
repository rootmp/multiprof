package l2s.gameserver.network.l2.s2c.pledge;

import l2s.commons.network.PacketWriter;
import l2s.gameserver.dao.CharacterVariablesDAO;
import l2s.gameserver.model.actor.variables.PlayerVariables;
import l2s.gameserver.model.pledge.Clan;
import l2s.gameserver.model.pledge.UnitMember;
import l2s.gameserver.network.l2.s2c.IClientOutgoingPacket;

/**
 * @author nexvill
 */
public class ExPledgeContributionList implements IClientOutgoingPacket
{
	private Clan _clan;

	public ExPledgeContributionList(Clan clan)
	{
		_clan = clan;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(_clan.getAllMembers().size());
		for (UnitMember member : _clan.getAllMembers())
		{
			packetWriter.writeSizedString(member.getName());
			if (CharacterVariablesDAO.getInstance().getVarFromPlayer(member.getObjectId(), PlayerVariables.WEEKLY_CONTRIBUTION) != null
					&& CharacterVariablesDAO.getInstance().getVarFromPlayer(member.getObjectId(), PlayerVariables.TOTAL_CONTRIBUTION) != null)
			{
				packetWriter.writeD(Integer.parseInt(CharacterVariablesDAO.getInstance().getVarFromPlayer(member.getObjectId(), PlayerVariables.WEEKLY_CONTRIBUTION)));
				packetWriter.writeD(Integer.parseInt(CharacterVariablesDAO.getInstance().getVarFromPlayer(member.getObjectId(), PlayerVariables.TOTAL_CONTRIBUTION)));
			}
			else
			{
				packetWriter.writeD(0);
				packetWriter.writeD(0);
			}
		}
		return true;
	}
}