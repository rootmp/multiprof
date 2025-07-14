package l2s.gameserver.network.l2.s2c.events;

import l2s.commons.network.PacketWriter;
import l2s.gameserver.Config;
import l2s.gameserver.network.l2.s2c.IClientOutgoingPacket;

public class ExFestivalBMGame implements IClientOutgoingPacket
{
	private int cRewardItemGrade;
	private int nRewardItemClassId;
	private int nRewardItemCount;

	public ExFestivalBMGame(int cRewardItemGrade, int nRewardItemClassId, int nRewardItemCount)
	{
		this.cRewardItemGrade = cRewardItemGrade;
		this.nRewardItemClassId = nRewardItemClassId;
		this.nRewardItemCount = nRewardItemCount;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeC(1); // result (0 - close window)
		packetWriter.writeD(Config.BM_FESTIVAL_ITEM_TO_PLAY); //nTicketItemClassId item id to play
		/*if (Config.BM_FESTIVAL_PLAY_LIMIT != -1)
			packetWriter.writeQ(_player.getVarInt("FESTIVAL_BM_EXIST_GAMES", Config.BM_FESTIVAL_PLAY_LIMIT)); //nTicketItemAmount tickets amount.. not used?
		else*/
		packetWriter.writeQ(0);

		packetWriter.writeD(Config.BM_FESTIVAL_ITEM_TO_PLAY_COUNT); //nTicketItemAmountPerGame tickets used per game
		packetWriter.writeC(cRewardItemGrade);
		packetWriter.writeD(nRewardItemClassId);
		packetWriter.writeD(nRewardItemCount);
		return true;
	}
}