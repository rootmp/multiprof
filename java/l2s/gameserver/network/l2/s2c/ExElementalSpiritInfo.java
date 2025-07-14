package l2s.gameserver.network.l2.s2c;

import java.util.Collection;

import l2s.commons.network.PacketWriter;
import l2s.gameserver.Config;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.actor.instances.player.Elemental;
import l2s.gameserver.templates.elemental.ElementalLevelData;

/**
 * @author Bonux
 **/
public class ExElementalSpiritInfo implements IClientOutgoingPacket
{
	private final int _unk, _activeElementId;
	private final Collection<Elemental> _elementals;

	public ExElementalSpiritInfo(Player player, int unk, int activeElementId)
	{
		_unk = unk;
		_activeElementId = activeElementId;
		_elementals = player.getElementalList().values();
	}

	public ExElementalSpiritInfo(Player player, int unk)
	{
		this(player, unk, player.getActiveElement().getId());
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeC(_unk); // Value received from client (RequestExElementalSpiritInfo) Maybe canOpen?
		packetWriter.writeC(_activeElementId); // Active Element ID
		packetWriter.writeC(_elementals.size()); // Elementals Count
		for(Elemental elemental : _elementals)
		{
			packetWriter.writeC(elemental.getElementId()); // Elemental ID

			int evolutionLevel = elemental.getEvolutionLevel();
			packetWriter.writeC(evolutionLevel > 0);
			if(evolutionLevel > 0)
			{
				packetWriter.writeC(evolutionLevel); // Evolution Level (1-3)
				packetWriter.writeD(elemental.getEvolution().getId()); // Evolution ID from client
				packetWriter.writeQ(elemental.getExp()); // Current Exp
				packetWriter.writeQ(elemental.getMaxExp()); // Max Exp for this level
				packetWriter.writeQ(elemental.getEvolution().getMaxExp()); // Max Exp for this evolution
				packetWriter.writeD(elemental.getLevel()); // Level 1-10
				packetWriter.writeD(elemental.getEvolution().getMaxLevel()); // Max Level
				packetWriter.writeD(elemental.getAvailablePoints()); // Available Points
				packetWriter.writeD(elemental.getAttackPoints()); // Current Attack Points
				packetWriter.writeD(elemental.getDefencePoints()); // Current Defence Points
				packetWriter.writeD(elemental.getCritRatePoints()); // Current Crit Rate
				packetWriter.writeD(elemental.getCritAttackPoints()); // Current Crit Damage
				packetWriter.writeD(elemental.getEvolution().getMaxAttackPoints()); // Max Attack Points
				packetWriter.writeD(elemental.getEvolution().getMaxDefencePoints()); // Max Defence Points
				packetWriter.writeD(elemental.getEvolution().getMaxCritRatePoints()); // Max Crit Rate
				packetWriter.writeD(elemental.getEvolution().getMaxCritAttackPoints()); // Max Crit Damage

				ElementalLevelData[] datas = elemental.getEvolution().getLevelDatas();
				packetWriter.writeC(datas.length);
				for(ElementalLevelData data : datas)
				{
					packetWriter.writeH(data.getLevel()); // Level
					packetWriter.writeQ(data.getExp()); // EXP
				}
			}
		}
		packetWriter.writeD(1); // UNK
		packetWriter.writeD(Config.ELEMENTAL_RESET_POINTS_ITEM_ID); // Stats reset cost Item ID
		packetWriter.writeQ(Config.ELEMENTAL_RESET_POINTS_ITEM_COUNT); // Stats reset cost
		return true;
	}
}