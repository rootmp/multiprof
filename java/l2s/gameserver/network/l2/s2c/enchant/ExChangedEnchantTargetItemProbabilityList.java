package l2s.gameserver.network.l2.s2c.enchant;

import java.util.List;

import l2s.commons.network.PacketWriter;
import l2s.gameserver.network.l2.s2c.IClientOutgoingPacket;
import l2s.gameserver.templates.skill.enchant.EnchantProbInfo;

public class ExChangedEnchantTargetItemProbabilityList implements IClientOutgoingPacket
{
	private List<EnchantProbInfo> _list;

	public ExChangedEnchantTargetItemProbabilityList(List<EnchantProbInfo> list)
	{
		_list = list;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(_list.size());
		for(EnchantProbInfo item : _list)
		{
			packetWriter.writeD(item.nItemServerId);
			packetWriter.writeD(item.nTotalSuccessProbPermyriad);
			packetWriter.writeD(item.nBaseProbPermyriad);
			packetWriter.writeD(item.nSupportProbPermyriad);
			packetWriter.writeD(item.nItemSkillProbPermyriad);
			packetWriter.writeD(item.nVariationProbPermyriad);
		}
		return true;
	}
}