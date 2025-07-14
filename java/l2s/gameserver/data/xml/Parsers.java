package l2s.gameserver.data.xml;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import l2s.gameserver.Config;
import l2s.gameserver.GameServer;
import l2s.gameserver.ThreadPoolManager;
import l2s.gameserver.data.htm.HtmCache;
import l2s.gameserver.data.string.ItemNameHolder;
import l2s.gameserver.data.string.NpcNameHolder;
import l2s.gameserver.data.string.NpcStringHolder;
import l2s.gameserver.data.string.SkillNameHolder;
import l2s.gameserver.data.string.StringsHolder;
import l2s.gameserver.data.xml.parser.AdditionalDropParser;
import l2s.gameserver.data.xml.parser.AgathionParser;
import l2s.gameserver.data.xml.parser.AppearanceStoneParser;
import l2s.gameserver.data.xml.parser.ArmorSetsParser;
import l2s.gameserver.data.xml.parser.AttendanceRewardParser;
import l2s.gameserver.data.xml.parser.BaseStatsBonusParser;
import l2s.gameserver.data.xml.parser.BlackCouponParser;
import l2s.gameserver.data.xml.parser.BotReportPropertiesParser;
import l2s.gameserver.data.xml.parser.BuyListParser;
import l2s.gameserver.data.xml.parser.ClassDataParser;
import l2s.gameserver.data.xml.parser.CubicParser;
import l2s.gameserver.data.xml.parser.DailyMissionsParser;
import l2s.gameserver.data.xml.parser.DomainParser;
import l2s.gameserver.data.xml.parser.DoorParser;
import l2s.gameserver.data.xml.parser.ElementalDataParser;
import l2s.gameserver.data.xml.parser.EnchantItemParser;
import l2s.gameserver.data.xml.parser.EnchantStoneParser;
import l2s.gameserver.data.xml.parser.EnsoulParser;
import l2s.gameserver.data.xml.parser.EventParser;
import l2s.gameserver.data.xml.parser.ExperienceDataParser;
import l2s.gameserver.data.xml.parser.FakeItemParser;
import l2s.gameserver.data.xml.parser.FakePlayersParser;
import l2s.gameserver.data.xml.parser.FestivalBMParser;
import l2s.gameserver.data.xml.parser.FightClubMapParser;
import l2s.gameserver.data.xml.parser.FishDataParser;
import l2s.gameserver.data.xml.parser.HitCondBonusParser;
import l2s.gameserver.data.xml.parser.InitialShortCutsParser;
import l2s.gameserver.data.xml.parser.InstantZoneParser;
import l2s.gameserver.data.xml.parser.ItemParser;
import l2s.gameserver.data.xml.parser.KarmaIncreaseDataParser;
import l2s.gameserver.data.xml.parser.LevelBonusParser;
import l2s.gameserver.data.xml.parser.LevelUpRewardParser;
import l2s.gameserver.data.xml.parser.LimitedShopParser;
import l2s.gameserver.data.xml.parser.LuckyGameParser;
import l2s.gameserver.data.xml.parser.MissionLevelRewardsParser;
import l2s.gameserver.data.xml.parser.MultiSellParser;
import l2s.gameserver.data.xml.parser.NpcParser;
import l2s.gameserver.data.xml.parser.OptionDataParser;
import l2s.gameserver.data.xml.parser.PetDataParser;
import l2s.gameserver.data.xml.parser.PetitionGroupParser;
import l2s.gameserver.data.xml.parser.PlayerTemplateParser;
import l2s.gameserver.data.xml.parser.PremiumAccountParser;
import l2s.gameserver.data.xml.parser.ProductDataParser;
import l2s.gameserver.data.xml.parser.RandomCraftListParser;
import l2s.gameserver.data.xml.parser.RecipeParser;
import l2s.gameserver.data.xml.parser.ResidenceFunctionsParser;
import l2s.gameserver.data.xml.parser.ResidenceParser;
import l2s.gameserver.data.xml.parser.RestartPointParser;
import l2s.gameserver.data.xml.parser.ShuttleTemplateParser;
import l2s.gameserver.data.xml.parser.SkillAcquireParser;
import l2s.gameserver.data.xml.parser.SkillParser;
import l2s.gameserver.data.xml.parser.SpawnParser;
import l2s.gameserver.data.xml.parser.StaticObjectParser;
import l2s.gameserver.data.xml.parser.SubjugationsParser;
import l2s.gameserver.data.xml.parser.TeleportListParser;
import l2s.gameserver.data.xml.parser.TimeRestrictFieldParser;
import l2s.gameserver.data.xml.parser.TransformTemplateParser;
import l2s.gameserver.data.xml.parser.UpgradeSystemParser;
import l2s.gameserver.data.xml.parser.VIPDataParser;
import l2s.gameserver.data.xml.parser.VariationDataParser;
import l2s.gameserver.data.xml.parser.ZoneParser;
import l2s.gameserver.instancemanager.ReflectionManager;
import l2s.gameserver.utils.Strings;

/**
 * @author VISTALL
 * @date 20:55/30.11.2010
 */
public abstract class Parsers
{
	private static final Logger _log = LoggerFactory.getLogger(Parsers.class);

	public static void parseAll()
	{
		GameServer.printSection("HTML Cache and Strings");
		ThreadPoolManager.getInstance().execute(() -> HtmCache.getInstance().reload());
		StringsHolder.getInstance().load();
		ItemNameHolder.getInstance().load();
		NpcNameHolder.getInstance().load();
		NpcStringHolder.getInstance().load();
		SkillNameHolder.getInstance().load();
		Strings.reload();
		GameServer.printSection("Loading Parsers");
		SkillParser.getInstance().load();
		OptionDataParser.getInstance().load();
		VariationDataParser.getInstance().load();
		AgathionParser.getInstance().load();
		ItemParser.getInstance().load();
		EnsoulParser.getInstance().load();
		RecipeParser.getInstance().load();
		//
		ExperienceDataParser.getInstance().load();
		BaseStatsBonusParser.getInstance().load();
		LevelBonusParser.getInstance().load();
		KarmaIncreaseDataParser.getInstance().load();
		HitCondBonusParser.getInstance().load();
		PlayerTemplateParser.getInstance().load();
		InitialShortCutsParser.getInstance().load();
		ClassDataParser.getInstance().load();
		TransformTemplateParser.getInstance().load();
		NpcParser.getInstance().load();
		AdditionalDropParser.getInstance().load();
		PetDataParser.getInstance().load();
		ElementalDataParser.getInstance().load();

		TeleportListParser.getInstance().load();
		DomainParser.getInstance().load();
		RestartPointParser.getInstance().load();

		StaticObjectParser.getInstance().load();
		DoorParser.getInstance().load();
		GameServer.printSection("Zones");
		ZoneParser.getInstance().load();
		GameServer.printSection("Spawn Manager");
		SpawnParser.getInstance().load();
		GameServer.printSection("Instances and Timed Zones");
		InstantZoneParser.getInstance().load();
		TimeRestrictFieldParser.getInstance().load();

		ReflectionManager.getInstance().init();
		//
		SkillAcquireParser.getInstance().load();
		//
		ResidenceFunctionsParser.getInstance().load();
		ResidenceParser.getInstance().load();
		ShuttleTemplateParser.getInstance().load();
		GameServer.printSection("Events");
		EventParser.getInstance().load();
		// support(cubic & agathion)
		CubicParser.getInstance().load();
		//
		BuyListParser.getInstance().load();
		MultiSellParser.getInstance().load();
		UpgradeSystemParser.getInstance().load();
		ProductDataParser.getInstance().load();
		AttendanceRewardParser.getInstance().load();
		GameServer.printSection("Special Craft System");
		LimitedShopParser.getInstance().load();
		BlackCouponParser.getInstance().load();
		FestivalBMParser.getInstance().load();
		// item support
		EnchantItemParser.getInstance().load();
		EnchantStoneParser.getInstance().load();
		AppearanceStoneParser.getInstance().load();
		ArmorSetsParser.getInstance().load();
		FishDataParser.getInstance().load();
		GameServer.printSection("Random Craft System");
		RandomCraftListParser.getInstance().load();
		GameServer.printSection("Subjulgation System");
		SubjugationsParser.getInstance().load();

		LevelUpRewardParser.getInstance().load();
		LuckyGameParser.getInstance().load();

		VIPDataParser.getInstance().load();
		PremiumAccountParser.getInstance().load();

		// etc
		PetitionGroupParser.getInstance().load();
		BotReportPropertiesParser.getInstance().load();

		GameServer.printSection("Missions");
		DailyMissionsParser.getInstance().load();
		MissionLevelRewardsParser.getInstance().load();

		// Fake players
		GameServer.printSection("Fake Players");
		FakeItemParser.getInstance().load();
		FakePlayersParser.getInstance().load();

		GameServer.printSection("Fight Club Events");
		if(Config.FIGHT_CLUB_ENABLED)
		{
			FightClubMapParser.getInstance().load();
		}
		else
		{
			_log.info("Fight Club Event: DISABLED");
		}
	}
}
