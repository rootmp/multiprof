package l2s.gameserver.network.l2.s2c;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import l2s.commons.network.PacketWriter;
import l2s.gameserver.Config;
import l2s.gameserver.data.xml.holder.ResidenceHolder;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.entity.residence.Castle;
import l2s.gameserver.model.items.ItemInstance;
import l2s.gameserver.model.items.TradeItem;
import l2s.gameserver.network.l2.ServerPacketOpcodes;
import l2s.gameserver.network.l2.ServerPacketOpcodes507;
import l2s.gameserver.templates.npc.BuyListTemplate;

public abstract class ExBuySellListPacket implements IClientOutgoingPacket
{
	private static final int[] CASTLES = {
			3, // Giran
			7, // Goddart
	};

	@Override
	public ByteBuf getOpcodes()
	{
		try
		{
			ServerPacketOpcodes spo = ServerPacketOpcodes507.ExBuySellListPacket;
			ByteBuf opcodes = Unpooled.buffer();
			opcodes.writeByte(spo.getId());
			int exOpcode = spo.getExId();
			if(exOpcode >= 0)
				opcodes.writeShortLE(exOpcode);
			return opcodes.retain();
		}
		catch (IllegalArgumentException e) 
		{}
		catch(Exception e)
		{
			LOGGER.error("Cannot find serverpacket opcode: " + getClass().getSimpleName() + "!");
		}
		return Unpooled.EMPTY_BUFFER;
	}
	
	public static class BuyList extends ExBuySellListPacket
	{
		private final int _listId;
		private final List<TradeItem> _buyList;
		private final long _adena;
		private final double _taxRate;
		private final int _inventoryUsedSlots;

		public BuyList(BuyListTemplate buyList, Player activeChar, double taxRate)
		{
			_adena = activeChar.getAdena();
			_taxRate = taxRate;
			_inventoryUsedSlots = activeChar.getInventory().getSize();

			if(buyList != null)
			{
				_listId = buyList.getListId();
				_buyList = buyList.getItems();
				activeChar.setBuyListId(_listId);
			}
			else
			{
				_listId = 0;
				_buyList = Collections.emptyList();
				activeChar.setBuyListId(0);
			}
		}

		@Override
		public boolean write(PacketWriter packetWriter)
		{
			packetWriter.writeD(0x00); // BUY LIST TYPE
			packetWriter.writeQ(_adena); // current money
			packetWriter.writeD(_listId);
			packetWriter.writeD(_inventoryUsedSlots); //TODO [Bonux] Awakening
			packetWriter.writeH(_buyList.size());
			for(TradeItem item : _buyList)
			{
				writeItemInfo(packetWriter, item, item.getCurrentValue());
				packetWriter.writeQ((long) (item.getOwnersPrice() * (1. + _taxRate)));

			}
			return true;
		}
	}

	public static class SellRefundList extends ExBuySellListPacket
	{
		private final List<TradeItem> _sellList;
		private final List<TradeItem> _refundList;
		private int _done;
		private final double _taxRate;
		private final int _inventoryUsedSlots;

		public SellRefundList(Player activeChar, boolean done, double taxRate)
		{
			_done = done ? 1 : 0;
			_taxRate = taxRate;
			_inventoryUsedSlots = activeChar.getInventory().getSize();
			if(done)
			{
				_refundList = Collections.emptyList();
				_sellList = Collections.emptyList();
			}
			else
			{
				ItemInstance[] items = activeChar.getRefund().getItems();
				if(Config.ALLOW_ITEMS_REFUND)
				{
					_refundList = new ArrayList<TradeItem>(items.length);
					for(ItemInstance item : items)
						_refundList.add(new TradeItem(item));
				}
				else
					_refundList = new ArrayList<TradeItem>(0);

				items = activeChar.getInventory().getItems();
				_sellList = new ArrayList<TradeItem>(items.length);
				for(ItemInstance item : items)
					if(item.canBeSold(activeChar))
						_sellList.add(new TradeItem(item, item.getTemplate().isBlocked(activeChar, item)));
			}
		}

		@Override
		public boolean write(PacketWriter packetWriter)
		{
			packetWriter.writeD(0x01); // SELL/REFUND LIST TYPE
			packetWriter.writeD(_inventoryUsedSlots); //TODO [Bonux] Awakening
			packetWriter.writeH(_sellList.size());
			for(TradeItem item : _sellList)
			{
				writeItemInfo(packetWriter, item);
				if(Config.ALT_SELL_ITEM_ONE_ADENA)
					packetWriter.writeQ(0);
				else
					packetWriter.writeQ((long) ((item.getReferencePrice() / 2) * (1. - _taxRate)));
			}
			packetWriter.writeH(_refundList.size());
			for(TradeItem item : _refundList)
			{
				writeItemInfo(packetWriter, item);
				packetWriter.writeD(item.getObjectId());
				if(Config.ALT_SELL_ITEM_ONE_ADENA)
					packetWriter.writeQ(item.getCount());
				else
					packetWriter.writeQ((long) ((item.getCount() * item.getReferencePrice() / 2) * (1. - _taxRate)));
			}
			packetWriter.writeC(_done);
			return true;
		}
	}

	public static class CurrentTax extends ExBuySellListPacket
	{
		@Override
		public boolean write(PacketWriter packetWriter)
		{
			packetWriter.writeD(3); // BUY LIST TYPE
			packetWriter.writeD(CASTLES.length);
			for(int id : CASTLES)
			{
				packetWriter.writeD(id); // residence id
				try
				{
					packetWriter.writeD(((Castle) ResidenceHolder.getInstance().getResidence(id)).getSellTaxPercent());// residence tax
				}
				catch(NullPointerException ignored)
				{
					packetWriter.writeD(0);
				}
			}
			return true;
		}
	}
}