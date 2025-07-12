package l2s.gameserver.network.l2.c2s.worldexchange;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import l2s.commons.network.PacketReader;
import l2s.gameserver.Config;
import l2s.gameserver.enums.WorldExchangeItemSubType;
import l2s.gameserver.enums.WorldExchangeSortType;
import l2s.gameserver.instancemanager.WorldExchangeManager;
import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.GameClient;
import l2s.gameserver.network.l2.c2s.IClientIncomingPacket;
import l2s.gameserver.network.l2.s2c.worldexchange.ExWorldExchangeItemList;
import l2s.gameserver.templates.WorldExchangeHolder;
import l2s.gameserver.utils.Language;


public class RequestExWorldExchangeItemList implements IClientIncomingPacket
{
	protected final Logger _log = LoggerFactory.getLogger(RequestExWorldExchangeItemList.class);
	private int _category;
	private int _sortType;
	private final List<Integer> _itemIdList = new ArrayList<>();
	private int cListingType;
	private int cCurrencyType;
	private int nPage;
	
	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		_category = packet.readH();
		_sortType = packet.readC();
		cListingType = packet.readC();
		cCurrencyType = packet.readC();
		nPage = packet.readD(); // page
		int size = packet.readD();
		for (int i = 0; i < size; i++)
		{
			_itemIdList.add(packet.readD());
		}
		return true;
	}
	
	@Override
	public void run(GameClient client)
	{
		if (!Config.ENABLE_WORLD_EXCHANGE || _category > 25)
			return;

		final Player player = client.getActiveChar();
		if (player == null)
			return;

		final Language lang = Config.CAN_SELECT_LANGUAGE ? player.getLanguage() : Config.WORLD_EXCHANGE_DEFAULT_LANG;
		if (_itemIdList.isEmpty())
		{
			WorldExchangeSortType sortType = WorldExchangeSortType.getWorldExchangeSortType(_sortType);
			if(sortType == null)
				_log.error("WorldExchangeSortType == null sortType:" + _sortType);
			
			final List<WorldExchangeHolder> holders = WorldExchangeManager.getInstance().getItemBids(player.getObjectId(), nPage , WorldExchangeItemSubType.getWorldExchangeItemSubType(_category), sortType, lang, cListingType, cCurrencyType);
			player.sendPacket(new ExWorldExchangeItemList(player, holders, WorldExchangeItemSubType.getWorldExchangeItemSubType(_category)));
		}
		else
		{
			WorldExchangeManager.getInstance().addCategoryType(_itemIdList, _category);
			final List<WorldExchangeHolder> holders = WorldExchangeManager.getInstance().getItemBids(_itemIdList, nPage, WorldExchangeSortType.getWorldExchangeSortType(_sortType), lang, cListingType, cCurrencyType);
			player.sendPacket(new ExWorldExchangeItemList(player, holders, WorldExchangeItemSubType.getWorldExchangeItemSubType(_category)));
		}
	}
}
