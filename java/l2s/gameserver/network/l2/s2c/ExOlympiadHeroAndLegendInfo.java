package l2s.gameserver.network.l2.s2c;

import java.util.List;

import l2s.commons.network.PacketWriter;
import l2s.gameserver.model.entity.Hero;
import l2s.gameserver.templates.ranking.OlympiadHeroInfo;

public class ExOlympiadHeroAndLegendInfo implements IClientOutgoingPacket
{
	private final List<OlympiadHeroInfo> heroes;

	public ExOlympiadHeroAndLegendInfo()
	{
		heroes = Hero.getInstance().getOlympiadHeroesInfo();
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeH(50);

		writeOlympiadHeroInfo(packetWriter, new OlympiadHeroInfo());

		packetWriter.writeD(heroes.size());

		for(OlympiadHeroInfo h : heroes)
			writeOlympiadHeroInfo(packetWriter, h);

		return true;
	}

	private void writeOlympiadHeroInfo(PacketWriter packetWriter, OlympiadHeroInfo heroInfo)
	{
		packetWriter.writeSizedString(heroInfo.sCharName);
		packetWriter.writeSizedString(heroInfo.sPledgeName);
		packetWriter.writeD(heroInfo.nWorldID);
		packetWriter.writeD(heroInfo.nRace);
		packetWriter.writeD(heroInfo.nSex);
		packetWriter.writeD(heroInfo.nClassID);
		packetWriter.writeD(heroInfo.nLevel);
		packetWriter.writeD(heroInfo.nCount);
		packetWriter.writeD(heroInfo.nWinCount);
		packetWriter.writeD(heroInfo.nLoseCount);
		packetWriter.writeD(heroInfo.nDrawCount);
		packetWriter.writeD(heroInfo.nOlympiadPoint);
		packetWriter.writeD(heroInfo.nPledgeLevel);
	}
}
