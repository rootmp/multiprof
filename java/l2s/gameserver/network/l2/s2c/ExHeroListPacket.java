package l2s.gameserver.network.l2.s2c;
import l2s.commons.network.PacketWriter;

import java.util.Collection;

import org.apache.commons.lang3.StringUtils;

import l2s.gameserver.Config;
import l2s.gameserver.model.entity.Hero;
import l2s.gameserver.templates.StatsSet;

/**
 * Format: (ch) d [SdSdSdd] d: size [ S: hero name d: hero class ID S: hero clan
 * name d: hero clan crest id S: hero ally name d: hero Ally id d: count ]
 */
public class ExHeroListPacket implements IClientOutgoingPacket
{
	private Collection<StatsSet> _heroList;

	public ExHeroListPacket()
	{
		_heroList = Hero.getInstance().getHeroes().valueCollection();
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(_heroList.size());
		for (StatsSet hero : _heroList)
		{
			packetWriter.writeS(hero.getString(Hero.CHAR_NAME));
			packetWriter.writeD(hero.getInteger(Hero.CLASS_ID));
			packetWriter.writeS(hero.getString(Hero.CLAN_NAME, StringUtils.EMPTY));
			packetWriter.writeD(0x00/* На оффе не используется иконки: hero.getInteger(Hero.CLAN_CREST, 0) */);
			packetWriter.writeS(hero.getString(Hero.ALLY_NAME, StringUtils.EMPTY));
			packetWriter.writeD(0x00/* На оффе не используется иконки: hero.getInteger(Hero.ALLY_CREST, 0) */);
			packetWriter.writeD(hero.getInteger(Hero.COUNT));
			packetWriter.writeD(Config.REQUEST_ID); // server id
			packetWriter.writeC(0); // unk
		}
	}
}