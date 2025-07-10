package l2s.gameserver.network.l2.s2c.pledge;

import l2s.gameserver.dao.CharacterVariablesDAO;
import l2s.gameserver.model.actor.variables.PlayerVariables;
import l2s.gameserver.model.pledge.Clan;
import l2s.gameserver.model.pledge.UnitMember;
import l2s.gameserver.network.l2.s2c.L2GameServerPacket;

/**
 * @author nexvill
 */
public class ExPledgeContributionList extends L2GameServerPacket
{
	private Clan _clan;

	public ExPledgeContributionList(Clan clan)
	{
		_clan = clan;
	}

	@Override
	protected final void writeImpl()
	{
		writeD(_clan.getAllMembers().size());
		for (UnitMember member : _clan.getAllMembers())
		{
			writeString(member.getName());
			if (CharacterVariablesDAO.getInstance().getVarFromPlayer(member.getObjectId(), PlayerVariables.WEEKLY_CONTRIBUTION) != null && CharacterVariablesDAO.getInstance().getVarFromPlayer(member.getObjectId(), PlayerVariables.TOTAL_CONTRIBUTION) != null)
			{
				writeD(Integer.parseInt(CharacterVariablesDAO.getInstance().getVarFromPlayer(member.getObjectId(), PlayerVariables.WEEKLY_CONTRIBUTION)));
				writeD(Integer.parseInt(CharacterVariablesDAO.getInstance().getVarFromPlayer(member.getObjectId(), PlayerVariables.TOTAL_CONTRIBUTION)));
			}
			else
			{
				writeD(0);
				writeD(0);
			}
		}
	}
}