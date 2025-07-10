package l2s.gameserver.network.l2.s2c;
import l2s.commons.network.PacketWriter;

import org.napile.primitive.maps.IntObjectMap;
import org.napile.primitive.pair.IntObjectPair;

import l2s.gameserver.Config;
import l2s.gameserver.instancemanager.RankManager;
import l2s.gameserver.model.base.ClassId;
import l2s.gameserver.model.entity.Hero;
import l2s.gameserver.model.pledge.Clan;
import l2s.gameserver.tables.ClanTable;
import l2s.gameserver.templates.StatsSet;

/**
 * @author nexvill
 */
public class ExOlympiadHeroAndLegendInfo implements IClientOutgoingPacket
{
	private final IntObjectMap<StatsSet> heroes;

	public ExOlympiadHeroAndLegendInfo()
	{
		heroes = Hero.getInstance().getHeroes();
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeC(78); // unk, 78 on JP
		packetWriter.writeC(0); // unk 0
		writeString(""); // legend name
		writeString(""); // clan name
		packetWriter.writeD(Config.REQUEST_ID); // server id
		packetWriter.writeD(0); // race
		packetWriter.writeD(0); // sex
		packetWriter.writeD(0); // class id
		packetWriter.writeD(0); // level
		packetWriter.writeD(0); // legend times
		packetWriter.writeD(0); // wins
		packetWriter.writeD(0); // loses
		packetWriter.writeD(0); // points
		packetWriter.writeD(0); // clan level

		packetWriter.writeD(Hero.getInstance().getHeroes().size());
		for (IntObjectPair<StatsSet> pair : heroes.entrySet())
		{
			StatsSet hero = pair.getValue();

			int sex = 0;
			int level = 0;
			int wins = 0;
			int loses = 0;
			int points = 0;
			for (int id : RankManager.getInstance().getPreviousOlyList().keySet())
			{
				StatsSet player = new StatsSet();
				player = RankManager.getInstance().getPreviousOlyList().get(id);
				if (pair.getKey() == player.getInteger("objId"))
				{
					sex = player.getInteger("sex");
					level = player.getInteger("level");
					wins = player.getInteger("competitions_win");
					loses = player.getInteger("competitions_lost");
					points = player.getInteger("olympiad_points");
					break;
				}
			}

			writeString(hero.getString(Hero.CHAR_NAME));
			writeString(hero.getString(Hero.CLAN_NAME));
			packetWriter.writeD(Config.REQUEST_ID);
			ClassId classId = ClassId.valueOf(hero.getInteger(Hero.CLASS_ID));
			packetWriter.writeD(classId.getRace().ordinal());
			packetWriter.writeD(sex); // sex
			packetWriter.writeD(classId.getId());
			packetWriter.writeD(level); // level
			packetWriter.writeD(hero.getInteger(Hero.COUNT));
			packetWriter.writeD(wins);
			packetWriter.writeD(loses);
			packetWriter.writeD(points);

			Clan clan = ClanTable.getInstance().getClanByCharId(pair.getKey());
			if (clan != null)
			{
				packetWriter.writeD(clan.getLevel());
			}
			else
			{
				packetWriter.writeD(0);
			}
		}
	}
}
