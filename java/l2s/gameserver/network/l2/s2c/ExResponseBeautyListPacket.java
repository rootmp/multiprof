package l2s.gameserver.network.l2.s2c;

import l2s.commons.network.PacketWriter;
import l2s.gameserver.Config;
import l2s.gameserver.data.xml.holder.BeautyShopHolder;
import l2s.gameserver.model.Player;
import l2s.gameserver.templates.beatyshop.BeautySetTemplate;
import l2s.gameserver.templates.beatyshop.BeautyStyleTemplate;
import l2s.gameserver.utils.ItemFunctions;

public class ExResponseBeautyListPacket implements IClientOutgoingPacket
{
	public static int HAIR_LIST = 0;
	public static int FACE_LIST = 1;

	private final int _type;
	private final long _adena;
	private final long _coins;
	private final int[][] _data;

	public ExResponseBeautyListPacket(Player player, int type)
	{
		_type = type;
		_adena = player.getAdena();
		_coins = ItemFunctions.getItemCount(player, Config.BEAUTY_SHOP_COIN_ITEM_ID);

		BeautySetTemplate set = BeautyShopHolder.getInstance().getTemplate(player);
		if(set != null)
		{
			BeautyStyleTemplate[] styles = new BeautyStyleTemplate[0];
			if(type == HAIR_LIST)
				styles = set.getHairs();
			else if(type == FACE_LIST)
				styles = set.getFaces();

			_data = new int[styles.length][2];

			for(int i = 0; i < styles.length; i++)
			{
				_data[i][0] = styles[i].getId();
				_data[i][1] = 0; // Limit
			}
		}
		else
			_data = new int[0][2];
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeQ(_adena);
		packetWriter.writeQ(_coins);
		packetWriter.writeD(_type);
		packetWriter.writeD(_data.length);
		for(int[] element : _data)
		{
			packetWriter.writeD(element[0]);
			packetWriter.writeD(element[1]);
		}
		packetWriter.writeD(0);
		return true;
	}
}