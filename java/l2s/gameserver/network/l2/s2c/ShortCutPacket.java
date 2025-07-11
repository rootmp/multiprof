package l2s.gameserver.network.l2.s2c;

import l2s.commons.network.PacketWriter;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.actor.instances.player.ShortCut;
import l2s.gameserver.model.items.ItemInstance;
import l2s.gameserver.skills.TimeStamp;

/**
 * @author VISTALL
 * @date 7:48/29.03.2011
 */
public abstract class ShortCutPacket implements IClientOutgoingPacket
{
	public static ShortcutInfo convert(Player player, ShortCut shortCut)
	{
		ShortcutInfo shortcutInfo = null;
		int page = shortCut.getSlot() + shortCut.getPage() * 12;
		boolean autoUse = shortCut.getAutoUse();
		switch(shortCut.getType())
		{
			case DELETED_ITEM:
			case ITEM:
				int reuseGroup = -1, currentReuse = 0, reuse = 0, variation1Id = 0, variation2Id = 0, variation3Id = 0;
				ItemInstance item = player.getInventory().getItemByObjectId(shortCut.getId());
				if(item != null)
				{
					variation1Id = item.getVariation1Id();
					variation2Id = item.getVariation2Id();
					variation3Id = item.getVariation3Id();
					reuseGroup = item.getTemplate().getDisplayReuseGroup();
					if(item.getTemplate().getReuseDelay() > 0)
					{
						TimeStamp timeStamp = player.getSharedGroupReuse(item.getTemplate().getReuseGroup());
						if(timeStamp != null)
						{
							currentReuse = (int) (timeStamp.getReuseCurrent() / 1000L);
							reuse = (int) (timeStamp.getReuseBasic() / 1000L);
						}
					}
				}
				shortcutInfo = new ItemShortcutInfo(shortCut.getType(), page, autoUse, shortCut.getId(), reuseGroup, currentReuse, reuse, variation1Id, variation2Id, variation3Id, shortCut.getCharacterType());
				break;
			case SKILL:
				shortcutInfo = new SkillShortcutInfo(shortCut.getType(), page, autoUse, shortCut.getId(), shortCut.getLevel(), shortCut.getSubLevel(), shortCut.getCharacterType());
				break;
			default:
				shortcutInfo = new ShortcutInfo(shortCut.getType(), page, autoUse, shortCut.getId(), shortCut.getCharacterType());
				break;
		}
		return shortcutInfo;
	}

	protected static class ItemShortcutInfo extends ShortcutInfo
	{
		private int _reuseGroup;
		private int _currentReuse;
		private int _basicReuse;
		private int _variation1Id;
		private int _variation2Id;
		private int _variation3Id;
		
		public ItemShortcutInfo(ShortCut.ShortCutType type, int page, boolean autoUse, int id, int reuseGroup, int currentReuse, int basicReuse, int variation1Id, int variation2Id, int variation3Id, int characterType)
		{
			super(type, page, autoUse, id, characterType);
			_reuseGroup = reuseGroup;
			_currentReuse = currentReuse;
			_basicReuse = basicReuse;
			_variation1Id = variation1Id;
			_variation2Id = variation2Id;
			_variation3Id = variation3Id;
		}

		@Override
		protected void write0(PacketWriter packet, ShortCutPacket p)
		{
			packet.writeD(_id);
			packet.writeD(_characterType);
			packet.writeD(_reuseGroup);
			packet.writeD(_currentReuse);
			packet.writeD(_basicReuse);
			packet.writeD(_variation1Id);
			packet.writeD(_variation2Id);
			packet.writeD(_variation3Id);
			packet.writeD(0x00); // TODO: [Bonux] ??HARMONY??
		}
	}

	protected static class SkillShortcutInfo extends ShortcutInfo
	{
		private final int _level;
		private int _sublevel;

		public SkillShortcutInfo(ShortCut.ShortCutType type, int page, boolean autoUse, int id, int level, int sublevel, int characterType)
		{
			super(type, page, autoUse, id, characterType);
			_level = level;
			_sublevel = sublevel;
		}

		public int getLevel()
		{
			return _level;
		}

		@Override
		protected void write0(PacketWriter packet, ShortCutPacket p)
		{
			packet.writeD(_id);
			packet.writeH(_level);
			packet.writeH(_sublevel);
			packet.writeD(_id); // TODO [VISTALL] skill reuse group
			packet.writeC(0x00);
			packet.writeD(_characterType);
			//p.packetWriter.writeD(0); // if 1 - cant use
			//p.packetWriter.writeD(0);// reuse delay ?
		}
	}

	protected static class ShortcutInfo
	{
		protected final ShortCut.ShortCutType _type;
		protected final int _page;
		protected final boolean _autoUse;
		protected final int _id;
		protected final int _characterType;

		public ShortcutInfo(ShortCut.ShortCutType type, int page, boolean autoUse, int id, int characterType)
		{
			_type = type;
			_page = page;
			_autoUse = autoUse;
			_id = id;
			_characterType = characterType;
		}

		protected void write(PacketWriter packet, ShortCutPacket p)
		{
			packet.writeD(_type.ordinal());
			packet.writeD(_page);
			packet.writeC(_autoUse); // is autouse
			write0(packet, p);
		}

		protected void write0(PacketWriter packet, ShortCutPacket p)
		{
			packet.writeD(_id);
			packet.writeD(_characterType);
		}
	}
}
