package l2s.gameserver.network.l2.s2c;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import l2s.commons.network.PacketWriter;
import l2s.dataparser.data.holder.SynthesisHolder;
import l2s.dataparser.data.holder.synthesis.SynthesisData;
import l2s.gameserver.model.Player;
import l2s.gameserver.templates.item.henna.HennaPoten;

public class ExNewHennaComposeProbList implements IClientOutgoingPacket
{
	private class ProbInfo
	{
		public int nItemClassID;
		public int nProb;
	};

	private Map<Integer, List<ProbInfo>> _probList = new HashMap<>();

	public ExNewHennaComposeProbList(Player player)
	{

		for(HennaPoten henna : player.getHennaPotenList())
		{
			if(henna !=null && henna.getHenna()!=null)
			{
				int dyeId = henna.getHenna().getDyeId();
				List<ProbInfo> probInfoList = _probList.getOrDefault(dyeId, new ArrayList<>());

				for(SynthesisData d : SynthesisHolder.getInstance().getDatas())
				{
					if(henna.getHenna().getDyeItemId() == d.getItem1Id())
					{
						ProbInfo probInfo = new ProbInfo();
						probInfo.nItemClassID = d.getItem2Id();
						
						double chance = d.getSuccessItemData().getChance()*100;
						probInfo.nProb = (int) chance;
						probInfoList.add(probInfo);
					}
				}

				if (!probInfoList.isEmpty()) 
					_probList.put(dyeId, probInfoList);
			}

		}
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(_probList.size());
		for(Entry<Integer, List<ProbInfo>> prob : _probList.entrySet())
		{
			packetWriter.writeD(prob.getKey());
			packetWriter.writeD(prob.getValue().size());
			for(ProbInfo val : prob.getValue())
			{
				packetWriter.writeD(val.nItemClassID);
				packetWriter.writeD(val.nProb);
			}
		}
		return true;
	}
}