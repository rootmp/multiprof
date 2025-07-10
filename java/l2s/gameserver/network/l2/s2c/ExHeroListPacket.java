package l2s.gameserver.network.l2.s2c;

import java.util.Collection;

import org.apache.commons.lang3.StringUtils;

import l2s.gameserver.Config;
import l2s.gameserver.model.entity.Hero;
import l2s.gameserver.templates.StatsSet;

/**
 * Format: (ch) d [SdSdSdd] d: size [ S: hero name d: hero class ID S: hero clan
 * name d: hero clan crest id S: hero ally name d: hero Ally id d: count ]
 */
public class ExHeroListPacket extends L2GameServerPacket
{
	private Collection<StatsSet> _heroList;

	public ExHeroListPacket()
	{
		_heroList = Hero.getInstance().getHeroes().valueCollection();
	}

	@Override
	protected final void writeImpl()
	{
		writeD(_heroList.size());
		for (StatsSet hero : _heroList)
		{
			writeS(hero.getString(Hero.CHAR_NAME));
			writeD(hero.getInteger(Hero.CLASS_ID));
			writeS(hero.getString(Hero.CLAN_NAME, StringUtils.EMPTY));
			writeD(0x00/* На оффе не используется иконки: hero.getInteger(Hero.CLAN_CREST, 0) */);
			writeS(hero.getString(Hero.ALLY_NAME, StringUtils.EMPTY));
			writeD(0x00/* На оффе не используется иконки: hero.getInteger(Hero.ALLY_CREST, 0) */);
			writeD(hero.getInteger(Hero.COUNT));
			writeD(Config.REQUEST_ID); // server id
			writeC(0); // unk
		}
	}
}