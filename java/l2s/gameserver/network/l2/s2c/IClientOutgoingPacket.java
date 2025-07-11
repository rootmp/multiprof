package l2s.gameserver.network.l2.s2c;
import l2s.commons.network.PacketWriter;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import l2s.commons.network.IOutgoingPacket;
import l2s.commons.network.PacketWriter;
import l2s.gameserver.data.xml.holder.ItemHolder;
import l2s.gameserver.enums.ItemListType;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.base.Element;
import l2s.gameserver.model.base.MultiSellIngredient;
import l2s.gameserver.model.items.ItemInfo;
import l2s.gameserver.model.items.ItemInstance;
import l2s.gameserver.network.l2.ServerPacketOpcodes;
import l2s.gameserver.network.l2.ServerPacketOpcodes507;
import l2s.gameserver.network.l2.components.IBroadcastPacket;
import l2s.gameserver.network.l2.s2c.updatetype.IUpdateTypeComponent;
import l2s.gameserver.skills.TimeStamp;
import l2s.gameserver.templates.item.ItemTemplate;
import l2s.gameserver.templates.item.support.Ensoul;

/**
 * @author KenM
 */
public interface IClientOutgoingPacket extends IOutgoingPacket, IBroadcastPacket
{
	Logger LOGGER = LoggerFactory.getLogger(IClientOutgoingPacket.class);

	/**
	 * @param masks
	 * @param type
	 * @return {@code true} if the mask contains the current update component type
	 */
	default boolean containsMask(int masks, IUpdateTypeComponent type)
	{
		return (masks & type.getMask()) == type.getMask();
	}

	@Override
	default boolean canBeWritten()
	{
		return true;
	}

	@Override
	default ByteBuf getOpcodes()
	{
		try
		{
			ServerPacketOpcodes spo = ServerPacketOpcodes507.valueOf(getClass().getSimpleName());
			if(spo != null)
			{
				ByteBuf opcodes = Unpooled.buffer();
				opcodes.writeByte(spo.getId());
				int exOpcode = spo.getExId();
				if(exOpcode >= 0)
					opcodes.writeShortLE(exOpcode);
				return opcodes.retain();
			}

		}
		catch (IllegalArgumentException e) 
		{}
		catch(Exception e)
		{
			LOGGER.error("Cannot find serverpacket opcode: " + getClass().getSimpleName() + "!");
		}
		return Unpooled.EMPTY_BUFFER;
	}
	
	@Override
	default String getType()
	{
		return "[S] " + getClass().getSimpleName();
	}

	default IClientOutgoingPacket packet(Player player)
	{
		return this;
	}

	/**
	 * Sends this packet to the target player, useful for lambda operations like <br>
	 * {@code L2World.getInstance().getPlayers().forEach(packet::sendTo)}
	 * @param player
	 */
	/*default void sendTo(WorldObject player)
	{
		player.sendPacket(this);
	}*/

	default void runImpl(Player player)
	{

	}

	default void writeOptionalD(PacketWriter packetWriter, int value)
	{
		if(value >= Short.MAX_VALUE)
		{
			packetWriter.writeH(Short.MAX_VALUE);
			packetWriter.writeD(value);
		}
		else
		{
			packetWriter.writeH(value);
		}
	}

	default void writeItemInfo(PacketWriter packetWriter, ItemInfo item)
	{
		writeItemInfo(packetWriter, item, item.getCount());
	}
	
	default void writeItemInfo(PacketWriter packetWriter, ItemInfo item, long count)
	{
		int flags = calculateMask(packetWriter, item);
		packetWriter.writeH(flags);
		//Dd  
		packetWriter.writeD(item.getObjectId());
		packetWriter.writeD(item.getItemId());
		//cqcc
		packetWriter.writeC(item.isEquipped() ? -1 : item.getEquipSlot());
		packetWriter.writeQ(count);
		packetWriter.writeC(item.getItem().getType2());
		packetWriter.writeC(item.getCustomType1());
		//hq 
		packetWriter.writeH(item.isEquipped() ? 1 : 0);
		packetWriter.writeQ(item.getItem().getBodyPart());
		//hc
		packetWriter.writeH(item.getEnchantLevel());
		packetWriter.writeC(0);
		//ddch
		packetWriter.writeD(item.getShadowLifeTime());
		packetWriter.writeD(item.getTemporalLifeTime());
		packetWriter.writeC(!item.isBlocked());
		packetWriter.writeH(item.isLocked()); // 140 PROTOCOL

		if(containsMask(flags, ItemListType.IS_AUGMENTED))
		{
			packetWriter.writeD(item.getVariation1Id());
			packetWriter.writeD(item.getVariation2Id());
			packetWriter.writeD(item.getVariation3Id());
		}

		if(containsMask(flags, ItemListType.IS_ELEMENTED))
		{
			packetWriter.writeH(item.getAttackElement());
			packetWriter.writeH(item.getAttackElementValue());
			packetWriter.writeH(item.getDefenceFire());
			packetWriter.writeH(item.getDefenceWater());
			packetWriter.writeH(item.getDefenceWind());
			packetWriter.writeH(item.getDefenceEarth());
			packetWriter.writeH(item.getDefenceHoly());
			packetWriter.writeH(item.getDefenceUnholy());
		}

		/*	if (containsMask(flags, ItemListType.HAVE_ENCHANT_OPTIONS))
		{
			packetWriter.writeD(item.getEnchantOptions()[0]);
			packetWriter.writeD(item.getEnchantOptions()[1]);
			packetWriter.writeD(item.getEnchantOptions()[2]);
		}*/
		if(containsMask(flags, ItemListType.VISUAL_CHANGED))
			packetWriter.writeD(item.getVisualId());

		if(containsMask(flags, ItemListType.HAVE_ENSOUL))
		{
			packetWriter.writeC(item.getNormalEnsouls().size());
			for(Ensoul ensoul : item.getNormalEnsouls())
				packetWriter.writeD(ensoul.getId());

			packetWriter.writeC(item.getSpecialEnsouls().size());
			for(Ensoul ensoul : item.getSpecialEnsouls())
				packetWriter.writeD(ensoul.getId());
		}

		if(containsMask(flags, ItemListType.USED_COUNT))
			packetWriter.writeH(item.getUsedCount());

		if(containsMask(flags, ItemListType.REUSE_DELAY))
			packetWriter.writeD(item.getReuseTime());

		if(containsMask(flags, ItemListType.EVOLVE))
		{
			packetWriter.writeD(item.getPetParam().getEvolveLevel()); // petEvolve step
			packetWriter.writeD(item.getPetParam().getRandomName());
			packetWriter.writeD(item.getPetParam().getPassiveSkill());
			packetWriter.writeD(item.getPetParam().getPassiveSkillLevel());
			packetWriter.writeD(item.getPetParam().getPetIndex()); // pet id
			packetWriter.writeQ(item.getPetParam().getExp());// pet exp*/
		}
		if(containsMask(flags, ItemListType.IS_BLESSED))
			packetWriter.writeC(1);
		
		if(containsMask(flags, ItemListType.IS_ILLUSORY))
			packetWriter.writeC(1);
	}

	default int calculateMask(PacketWriter packetWriter, ItemInfo item)
	{
		int flags = 0;
		if(item.getVariation1Id() > 0 || item.getVariation2Id() > 0)
			flags |= ItemListType.IS_AUGMENTED.getMask();

		int attackElementValue = item.getAttackElementValue();
		int defenceFire = item.getDefenceFire();
		int defenceWater = item.getDefenceWater();
		int defenceWind = item.getDefenceWind();
		int defenceEarth = item.getDefenceEarth();
		int defenceHoly = item.getDefenceHoly();
		int defenceUnholy = item.getDefenceUnholy();
		if(attackElementValue > 0 || defenceFire > 0 || defenceWater > 0 || defenceWind > 0 || defenceEarth > 0 || defenceHoly > 0 || defenceUnholy > 0)
			flags |= ItemListType.IS_ELEMENTED.getMask();

		/*	for(int enchantOption : item.getEnchantOptions())
		{
			if(enchantOption > 0)
			{
				flags |= ItemListType.HAVE_ENCHANT_OPTIONS.getMask();
				break;
			}
		}*/
		if(item.getVisualId() > 0)
			flags |= ItemListType.VISUAL_CHANGED.getMask();

		Collection<Ensoul> normalEnsouls = item.getNormalEnsouls();
		Collection<Ensoul> specialEnsouls = item.getSpecialEnsouls();
		if(!normalEnsouls.isEmpty() || !specialEnsouls.isEmpty())
			flags |= ItemListType.HAVE_ENSOUL.getMask();

		//if(UseCountItemHolder.getInstance().containsItem(item.getItemId()))
			//flags |= ItemListType.USED_COUNT.getBlockLength();
		
		if(item.getReuseTime() > 0)
			flags |= ItemListType.REUSE_DELAY.getMask();
		
		//if(PetDataHolder.getInstance().isPetControlItem(item.getItemId()))
			//flags |= ItemListType.EVOLVE.getMask();

		if(item.isBlessed())
			flags |= ItemListType.IS_BLESSED.getMask();

		return flags;
	}

	default void writeItemInfo(PacketWriter packetWriter, ItemInstance item)
	{
		writeItemInfo(packetWriter, null, item, item.getCount());
	}

	default void writeItemInfo(PacketWriter packetWriter, Player player, ItemInstance item)
	{
		writeItemInfo(packetWriter, player, item, item.getCount());
	}

	default void writeItemInfo(PacketWriter packetWriter, ItemInstance item, long count)
	{
		writeItemInfo(packetWriter, null, item, count);
	}

	default void writeItemInfo(PacketWriter packetWriter, ItemInfo item, boolean writeSize, int initSize)
	{
		writeItemInfo(packetWriter, item, item.getCount(), writeSize, initSize);
	}
	
	default int calculateMaskLength(PacketWriter packetWriter, ItemInfo item) 
  {
  	int _initSize = 45;
  	int flags = calculateMask(packetWriter, item);

		if (containsMask(flags, ItemListType.IS_AUGMENTED))
			_initSize+=ItemListType.IS_AUGMENTED.getBlockLength();
		
		if (containsMask(flags, ItemListType.IS_ELEMENTED))
			_initSize+=ItemListType.IS_ELEMENTED.getBlockLength();

	/*	if (containsMask(flags, ItemListType.HAVE_ENCHANT_OPTIONS))
			_initSize+=ItemListType.HAVE_ENCHANT_OPTIONS.getBlockLength();*/

		if (containsMask(flags, ItemListType.VISUAL_CHANGED))
			_initSize+=ItemListType.VISUAL_CHANGED.getBlockLength();

		if (containsMask(flags, ItemListType.HAVE_ENSOUL))
			_initSize+=ItemListType.HAVE_ENSOUL.getBlockLength();

		if (containsMask(flags, ItemListType.USED_COUNT))
			_initSize+=ItemListType.USED_COUNT.getBlockLength();
		
		if (containsMask(flags, ItemListType.REUSE_DELAY))
			_initSize+=ItemListType.REUSE_DELAY.getBlockLength();
		
		if (containsMask(flags, ItemListType.EVOLVE))
			_initSize+=ItemListType.EVOLVE.getBlockLength();
		
		if (containsMask(flags, ItemListType.IS_BLESSED))
			_initSize+=ItemListType.IS_BLESSED.getBlockLength();
		
		if (containsMask(flags, ItemListType.IS_ILLUSORY))
			_initSize+=ItemListType.IS_ILLUSORY.getBlockLength();
		
  	return _initSize;
  }
  
	default void writeItemInfo(PacketWriter packetWriter, ItemInfo item, long count, boolean writeSize, int initSize)
	{
		int size = initSize + calculateMaskLength(packetWriter, item);
		int flags = calculateMask(packetWriter, item);

		if (writeSize)
			packetWriter.writeD(size);

		packetWriter.writeH(flags);
		packetWriter.writeD(item.getObjectId());
		packetWriter.writeD(item.getItemId());
		packetWriter.writeC(item.isEquipped() ? -1 : item.getEquipSlot());
		packetWriter.writeQ(count);
		packetWriter.writeC(item.getItem().getType2());
		packetWriter.writeC(item.getCustomType1());
		packetWriter.writeH(item.isEquipped() ? 1 : 0);
		packetWriter.writeQ(item.getItem().getBodyPart());
		packetWriter.writeH(item.getEnchantLevel());
		packetWriter.writeC(item.getCustomType2());
		packetWriter.writeD(item.getShadowLifeTime());
		packetWriter.writeD(item.getTemporalLifeTime());
		packetWriter.writeC(!item.isBlocked());
		packetWriter.writeH(item.isLocked()); // 140 PROTOCOL

		if (containsMask(flags, ItemListType.IS_AUGMENTED))
		{
			packetWriter.writeD(item.getVariation1Id());
			packetWriter.writeD(item.getVariation2Id());
			packetWriter.writeD(item.getVariation3Id());
		}

		if (containsMask(flags, ItemListType.IS_ELEMENTED))
		{
			packetWriter.writeH(item.getAttackElement());
			packetWriter.writeH(item.getAttackElementValue());
			packetWriter.writeH(item.getDefenceFire());
			packetWriter.writeH(item.getDefenceWater());
			packetWriter.writeH(item.getDefenceWind());
			packetWriter.writeH(item.getDefenceEarth());
			packetWriter.writeH(item.getDefenceHoly());
			packetWriter.writeH(item.getDefenceUnholy());
		}

		if (containsMask(flags, ItemListType.VISUAL_CHANGED))
			packetWriter.writeD(item.getVisualId());

		if (containsMask(flags, ItemListType.HAVE_ENSOUL))
		{
			packetWriter.writeC(item.getNormalEnsouls().size());
			for(Ensoul ensoul : item.getNormalEnsouls())
				packetWriter.writeD(ensoul.getId());

			packetWriter.writeC(item.getSpecialEnsouls().size());
			for(Ensoul ensoul : item.getSpecialEnsouls())
				packetWriter.writeD(ensoul.getId());
		}
		
		if (containsMask(flags, ItemListType. USED_COUNT))
			packetWriter.writeH(item.getUsedCount());
		
		if (containsMask(flags, ItemListType.REUSE_DELAY))
			packetWriter.writeD(item.getReuseTime());

		if (containsMask(flags, ItemListType.EVOLVE))
		{
			packetWriter.writeD(item.getPetParam().getEvolveLevel()); // petEvolve step
			packetWriter.writeD(item.getPetParam().getRandomName());
			packetWriter.writeD(item.getPetParam().getPassiveSkill());
			packetWriter.writeD(item.getPetParam().getPassiveSkillLevel());
			packetWriter.writeD(item.getPetParam().getPetIndex()); // pet id
			packetWriter.writeQ(item.getPetParam().getExp());// pet exp*/
		}
		
		if (containsMask(flags, ItemListType.IS_BLESSED))
			packetWriter.writeC(1);
		
		if(containsMask(flags, ItemListType.IS_ILLUSORY))
			packetWriter.writeC(1);
	}
	
	default void writeItemInfo(PacketWriter packetWriter, Player player, ItemInstance item, long count)
	{
		int flags = calculateMask(packetWriter, player, item);
		packetWriter.writeH(flags);
		//Dd  
		packetWriter.writeD(item.getObjectId());
		packetWriter.writeD(item.getItemId());
		//cqcc
		packetWriter.writeC(item.isEquipped() ? -1 : item.getEquipSlot());
		packetWriter.writeQ(count);
		packetWriter.writeC(item.getTemplate().getType2());
		packetWriter.writeC(item.getCustomType1());
		//hq 
		packetWriter.writeH(item.isEquipped() ? 1 : 0);
		packetWriter.writeQ(item.getTemplate().getBodyPart());
		//hc
		packetWriter.writeH(item.getFixedEnchantLevel(player));
		packetWriter.writeC(0);
		//ddch
		packetWriter.writeD(item.getShadowLifeTime());
		packetWriter.writeD(item.getTemporalLifeTime());

		if(player != null)
			packetWriter.writeC(!item.getTemplate().isBlocked(player, item));
		else
			packetWriter.writeC(0x01);

		packetWriter.writeH(item.isLocked()); // 140 PROTOCOL

		if(containsMask(flags, ItemListType.IS_AUGMENTED))
		{
			packetWriter.writeD(item.getVariation1Id());
			packetWriter.writeD(item.getVariation2Id());
			packetWriter.writeD(item.getVariation3Id());
		}

		if(containsMask(flags, ItemListType.IS_ELEMENTED))
		{
			packetWriter.writeH(item.getAttackElement().getId());
			packetWriter.writeH(item.getAttributeElementValue());
			packetWriter.writeH(item.getDefenceFire());
			packetWriter.writeH(item.getDefenceWater());
			packetWriter.writeH(item.getDefenceWind());
			packetWriter.writeH(item.getDefenceEarth());
			packetWriter.writeH(item.getDefenceHoly());
			packetWriter.writeH(item.getDefenceUnholy());
		}

		/*	if (containsMask(flags, ItemListType.HAVE_ENCHANT_OPTIONS))
		{
			packetWriter.writeD(item.getEnchantOptions()[0]);
			packetWriter.writeD(item.getEnchantOptions()[1]);
			packetWriter.writeD(item.getEnchantOptions()[2]);
		}*/

		if(containsMask(flags, ItemListType.VISUAL_CHANGED))
			packetWriter.writeD(item.getVisualId());

		if(containsMask(flags, ItemListType.HAVE_ENSOUL))
		{
			packetWriter.writeC(item.getNormalEnsouls().size());
			for(Ensoul ensoul : item.getNormalEnsouls())
				packetWriter.writeD(ensoul.getId());

			packetWriter.writeC(item.getSpecialEnsouls().size());
			for(Ensoul ensoul : item.getSpecialEnsouls())
				packetWriter.writeD(ensoul.getId());
		}

		if(containsMask(flags, ItemListType.USED_COUNT))
			packetWriter.writeH(item.getUseCount());

		if(containsMask(flags, ItemListType.REUSE_DELAY))
			packetWriter.writeD(player==null? 0 : (int) player.getSharedGroupReuse(item.getTemplate().getReuseGroup()).getReuseCurrent());

		if(containsMask(flags, ItemListType.EVOLVE))
		{
			packetWriter.writeD(item.getPetParam().getEvolveLevel()); // petEvolve step
			packetWriter.writeD(item.getPetParam().getRandomName());
			packetWriter.writeD(item.getPetParam().getPassiveSkill());
			packetWriter.writeD(item.getPetParam().getPassiveSkillLevel());
			packetWriter.writeD(item.getPetParam().getPetIndex()); // pet id
			packetWriter.writeQ(item.getPetParam().getExp());// pet exp*/
		}

		if(containsMask(flags, ItemListType.IS_BLESSED))
			packetWriter.writeC(1);
		
		if(containsMask(flags, ItemListType.IS_ILLUSORY))
			packetWriter.writeC(1);
	}

	default int calculateMask(PacketWriter packetWriter, Player player, ItemInstance item)
	{
		int flags = 0;
		if(item.isAugmented())
			flags |= ItemListType.IS_AUGMENTED.getMask();

		int attackElementValue = item.getAttackElementValue();
		int defenceFire = item.getDefenceFire();
		int defenceWater = item.getDefenceWater();
		int defenceWind = item.getDefenceWind();
		int defenceEarth = item.getDefenceEarth();
		int defenceHoly = item.getDefenceHoly();
		int defenceUnholy = item.getDefenceUnholy();
		if(attackElementValue > 0 || defenceFire > 0 || defenceWater > 0 || defenceWind > 0 || defenceEarth > 0 || defenceHoly > 0 || defenceUnholy > 0)
			flags |= ItemListType.IS_ELEMENTED.getMask();

		/*	for(int enchantOption : item.getEnchantOptions())
		{
			if(enchantOption > 0)
			{
				flags |= ItemListType.HAVE_ENCHANT_OPTIONS.getMask();
				break;
			}
		}*/

		if(item.getVisualId() > 0)
			flags |= ItemListType.VISUAL_CHANGED.getMask();

		Collection<Ensoul> normalEnsouls = item.getNormalEnsouls();
		Collection<Ensoul> specialEnsouls = item.getSpecialEnsouls();
		if(!normalEnsouls.isEmpty() || !specialEnsouls.isEmpty())
			flags |= ItemListType.HAVE_ENSOUL.getMask();

		//if(UseCountItemHolder.getInstance().containsItem(item.getItemId()))
			//flags |= ItemListType.USED_COUNT.getMask();

		if(player != null)
		{
			TimeStamp sts = player.getSharedGroupReuse(item.getTemplate().getReuseGroup());
			if(sts != null && sts.hasNotPassed())
			{
				int reuseTimeleft = (int) sts.getReuseCurrent();
				if(reuseTimeleft > 0)
					flags |= ItemListType.REUSE_DELAY.getMask();
			}
		}

		//if(PetDataHolder.getInstance().isPetControlItem(item.getItemId()))
			//flags |= ItemListType.EVOLVE.getMask();

		if(item.isBlessed())
			flags |= ItemListType.IS_BLESSED.getMask();

		return flags;
	}
	
	default void writeItemElements(PacketWriter packetWriter)
	{
		packetWriter.writeH(-1); // attack element (-1 - none)
		packetWriter.writeH(0x00); // attack element value
		packetWriter.writeH(0x00); // водная стихия (fire pdef)
		packetWriter.writeH(0x00); // огненная стихия (water pdef)
		packetWriter.writeH(0x00); // земляная стихия (wind pdef)
		packetWriter.writeH(0x00); // воздушная стихия (earth pdef)
		packetWriter.writeH(0x00); // темная стихия (holy pdef)
		packetWriter.writeH(0x00); // светлая стихия (dark pdef)
	}

	default void writeItemElements(PacketWriter packetWriter, MultiSellIngredient item)
	{
		if(item.getItemId() <= 0)
		{
			writeItemElements(packetWriter);
			return;
		}
		ItemTemplate i = ItemHolder.getInstance().getTemplate(item.getItemId());
		if(item.getItemAttributes().getValue() > 0)
		{
			if(i.isWeapon())
			{
				Element e = item.getItemAttributes().getElement();
				packetWriter.writeH(e.getId()); // attack element (-1 - none)
				packetWriter.writeH(item.getItemAttributes().getValue(e) + i.getBaseAttributeValue(e)); // attack element value
				packetWriter.writeH(0); // водная стихия (fire pdef)
				packetWriter.writeH(0); // огненная стихия (water pdef)
				packetWriter.writeH(0); // земляная стихия (wind pdef)
				packetWriter.writeH(0); // воздушная стихия (earth pdef)
				packetWriter.writeH(0); // темная стихия (holy pdef)
				packetWriter.writeH(0); // светлая стихия (dark pdef)
			}
			else if(i.isArmor())
			{
				packetWriter.writeH(-1); // attack element (-1 - none)
				packetWriter.writeH(0); // attack element value
				for(Element e : Element.VALUES)
					packetWriter.writeH(item.getItemAttributes().getValue(e) + i.getBaseAttributeValue(e));
			}
			else
				writeItemElements(packetWriter);
		}
		else
			writeItemElements(packetWriter);
	}

	
}
