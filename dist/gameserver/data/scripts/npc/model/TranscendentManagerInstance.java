package npc.model;

import l2s.commons.collections.MultiValueSet;
import l2s.gameserver.ThreadPoolManager;
import l2s.gameserver.geometry.Location;
import l2s.gameserver.instancemanager.ReflectionManager;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.actor.instances.creature.Abnormal;
import l2s.gameserver.model.base.Race;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.network.l2.components.NpcString;
import l2s.gameserver.network.l2.s2c.ExShowScreenMessage;
import l2s.gameserver.skills.SkillEntry;
import l2s.gameserver.skills.enums.SkillEntryType;
import l2s.gameserver.templates.npc.NpcTemplate;

/**
 * @author nexvill
 */
public class TranscendentManagerInstance extends NpcInstance
{
	private int stage = 0;

	public TranscendentManagerInstance(int objectId, NpcTemplate template, MultiValueSet<String> set)
	{
		super(objectId, template, set);
	}

	@Override
	public void showMainChatWindow(Player player, boolean firstTalk, Object... replace)
	{
		for (Abnormal e : player.getAbnormalList())
		{
			if ((stage == 1) && ((e.getSkill().getId() == 45197) || (e.getSkill().getId() == 45198) || (e.getSkill().getId() == 59829)))
			{
				return;
			}
		}

		if (stage == 2)
		{
			showChatWindow(player, "transcendent/return.htm", firstTalk);
			return;
		}

		switch (player.getClassId())
		{
			case GLADIATOR:
			{
				showChatWindow(player, "transcendent/gladiator.htm", firstTalk);
				player.sendPacket(new ExShowScreenMessage(NpcString.GLADIATOR_TRANSCENDENT_SKILLS_DOUBLE_TRIPLE_SONIC_SLASH, 5000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, false));
				break;
			}
			case WARLORD:
			{
				player.sendPacket(new ExShowScreenMessage(NpcString.WARLORD_TRANSCENDENT_SKILL_FATAL_STRIKE, 5000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, false));
				showChatWindow(player, "transcendent/warlord.htm", firstTalk);
				break;
			}
			case PALADIN:
			{
				player.sendPacket(new ExShowScreenMessage(NpcString.PALADIN_TRANSCENDENT_SKILL_SHIELD_STRIKE, 5000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, false));
				showChatWindow(player, "transcendent/paladin.htm", firstTalk);
				break;
			}
			case DARK_AVENGER:
			{
				player.sendPacket(new ExShowScreenMessage(NpcString.DARK_AVENGER_TRANSCENDENT_SKILL_SHIELD_STRIKE, 5000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, false));
				showChatWindow(player, "transcendent/dark_avenger.htm", firstTalk);
				break;
			}
			case TREASURE_HUNTER:
			{
				player.sendPacket(new ExShowScreenMessage(NpcString.TREASURE_HUNTER_TRANSCENDENT_SKILL_DEADLY_BLOW, 5000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, false));
				showChatWindow(player, "transcendent/treasure_hunter.htm", firstTalk);
				break;
			}
			case HAWKEYE:
			{
				player.sendPacket(new ExShowScreenMessage(NpcString.HAWKEYE_TRANSCENDENT_SKILL_DOUBLE_SHOT, 5000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, false));
				showChatWindow(player, "transcendent/hawkeye.htm", firstTalk);
				break;
			}
			case SORCERER:
			{
				player.sendPacket(new ExShowScreenMessage(NpcString.SORCERER_TRANSCENDENT_SKILL_PROMINENCE, 5000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, false));
				showChatWindow(player, "transcendent/sorcerer.htm", firstTalk);
				break;
			}
			case NECROMANCER:
			{
				player.sendPacket(new ExShowScreenMessage(NpcString.NECROMANCER_TRANSCENDENT_SKILL_DEATH_SPIKE, 5000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, false));
				showChatWindow(player, "transcendent/necromancer.htm", firstTalk);
				break;
			}
			case WARLOCK:
			{
				player.sendPacket(new ExShowScreenMessage(NpcString.WARLOCK_TRANSCENDENT_SKILL_BLAZE, 5000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, false));
				showChatWindow(player, "transcendent/warlock.htm", firstTalk);
				break;
			}
			case BISHOP:
			{
				player.sendPacket(new ExShowScreenMessage(NpcString.BISHOP_TRANSCENDENT_SKILL_MIGHT_OF_HEAVEN, 5000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, false));
				showChatWindow(player, "transcendent/bishop.htm", firstTalk);
				break;
			}
			case PROPHET:
			{
				player.sendPacket(new ExShowScreenMessage(NpcString.PROPHET_TRANSCENDENT_SKILL_MIGHT_OF_HEAVEN, 5000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, false));
				showChatWindow(player, "transcendent/prophet.htm", firstTalk);
				break;
			}
			case TEMPLE_KNIGHT:
			{
				player.sendPacket(new ExShowScreenMessage(NpcString.TEMPLE_KNIGHT_TRANSCENDENT_SKILL_TRIBUNAL, 5000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, false));
				showChatWindow(player, "transcendent/temple_knight.htm", firstTalk);
				break;
			}
			case SWORDSINGER:
			{
				player.sendPacket(new ExShowScreenMessage(NpcString.SWORDSINGER_TRANSCENDENT_SKILL_GUARD_CRUSH, 5000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, false));
				showChatWindow(player, "transcendent/swordsinger.htm", firstTalk);
				break;
			}
			case PLAIN_WALKER:
			{
				player.sendPacket(new ExShowScreenMessage(NpcString.PLAINS_WALKER_TRANSCENDENT_SKILL_DEADLY_BLOW, 5000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, false));
				showChatWindow(player, "transcendent/plains_walker.htm", firstTalk);
				break;
			}
			case SILVER_RANGER:
			{
				player.sendPacket(new ExShowScreenMessage(NpcString.SILVER_RANGER_TRANSCENDENT_SKILL_DOUBLE_SHOT, 5000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, false));
				showChatWindow(player, "transcendent/silver_ranger.htm", firstTalk);
				break;
			}
			case SPELLSINGER:
			{
				player.sendPacket(new ExShowScreenMessage(NpcString.SPELLSINGER_TRANSCENDENT_SKILL_HYDRO_BLAST, 5000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, false));
				showChatWindow(player, "transcendent/spellsinger.htm", firstTalk);
				break;
			}
			case ELEMENTAL_SUMMONER:
			{
				player.sendPacket(new ExShowScreenMessage(NpcString.ELEMENTAL_SUMMONER_TRANSCENDENT_SKILL_AQUA_SWIRL, 5000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, false));
				showChatWindow(player, "transcendent/elemental_summoner.htm", firstTalk);
				break;
			}
			case ELDER:
			{
				player.sendPacket(new ExShowScreenMessage(NpcString.ELVEN_ELDER_TRANSCENDENT_SKILL_MIGHT_OF_HEAVEN, 5000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, false));
				showChatWindow(player, "transcendent/elven_elder.htm", firstTalk);
				break;
			}
			case SHILLEN_KNIGHT:
			{
				player.sendPacket(new ExShowScreenMessage(NpcString.SHILLIEN_KNIGHT_TRANSCENDENT_SKILL_JUDGMENT, 5000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, false));
				showChatWindow(player, "transcendent/shillien_knight.htm", firstTalk);
				break;
			}
			case BLADEDANCER:
			{
				player.sendPacket(new ExShowScreenMessage(NpcString.BLADEDANCER_TRANSCENDENT_SKILL_GUARD_CRUSH, 5000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, false));
				showChatWindow(player, "transcendent/bladedancer.htm", firstTalk);
				break;
			}
			case ABYSS_WALKER:
			{
				player.sendPacket(new ExShowScreenMessage(NpcString.ABYSS_WALKER_TRANSCENDENT_SKILL_DEADLY_BLOW, 5000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, false));
				showChatWindow(player, "transcendent/abyss_walker.htm", firstTalk);
				break;
			}
			case PHANTOM_RANGER:
			{
				player.sendPacket(new ExShowScreenMessage(NpcString.PHANTOM_RANGER_TRANSCENDENT_SKILL_DOUBLE_SHOT, 5000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, false));
				showChatWindow(player, "transcendent/phantom_ranger.htm", firstTalk);
				break;
			}
			case SPELLHOWLER:
			{
				player.sendPacket(new ExShowScreenMessage(NpcString.SPELLHOWLER_TRANSCENDENT_SKILL_HURRICANE, 5000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, false));
				showChatWindow(player, "transcendent/spellhowler.htm", firstTalk);
				break;
			}
			case PHANTOM_SUMMONER:
			{
				player.sendPacket(new ExShowScreenMessage(NpcString.PHANTOM_SUMMONER_TRANSCENDENT_SKILL_TWISTER, 5000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, false));
				showChatWindow(player, "transcendent/phantom_summoner.htm", firstTalk);
				break;
			}
			case SHILLEN_ELDER:
			{
				player.sendPacket(new ExShowScreenMessage(NpcString.SHILLIEN_ELDER_TRANSCENDENT_SKILL_MIGHT_OF_HEAVEN, 5000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, false));
				showChatWindow(player, "transcendent/shillien_elder.htm", firstTalk);
				break;
			}
			case DESTROYER:
			{
				player.sendPacket(new ExShowScreenMessage(NpcString.DESTROYER_TRANSCENDENT_SKILL_FATAL_STRIKE, 5000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, false));
				showChatWindow(player, "transcendent/destroyer.htm", firstTalk);
				break;
			}
			case TYRANT:
			{
				player.sendPacket(new ExShowScreenMessage(NpcString.TYRANT_TRANSCENDENT_SKILL_HURRICANE_ASSAULT, 5000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, false));
				showChatWindow(player, "transcendent/tyrant.htm", firstTalk);
				break;
			}
			case OVERLORD:
			{
				player.sendPacket(new ExShowScreenMessage(NpcString.OVERLORD_TRANSCENDENT_SKILL_STEAL_ESSENCE, 5000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, false));
				showChatWindow(player, "transcendent/overlord.htm", firstTalk);
				break;
			}
			case WARCRYER:
			{
				player.sendPacket(new ExShowScreenMessage(NpcString.WARCRYER_TRANSCENDENT_SKILL_STEAL_ESSENCE, 5000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, false));
				showChatWindow(player, "transcendent/warcryer.htm", firstTalk);
				break;
			}
			case BOUNTY_HUNTER:
			{
				player.sendPacket(new ExShowScreenMessage(NpcString.BOUNTY_HUNTER_TRANSCENDENT_SKILL_FATAL_STRIKE, 5000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, false));
				showChatWindow(player, "transcendent/bounty_hunter.htm", firstTalk);
				break;
			}
			case WARSMITH:
			{
				player.sendPacket(new ExShowScreenMessage(NpcString.WARSMITH_TRANSCENDENT_SKILL_FATAL_STRIKE, 5000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, false));
				showChatWindow(player, "transcendent/warsmith.htm", firstTalk);
				break;
			}
			case DUELIST:
			{
				player.sendPacket(new ExShowScreenMessage(NpcString.DUELIST_TRANSCENDENT_SKILLS_DOUBLE_TRIPLE_SONIC_SLASH, 5000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, false));
				showChatWindow(player, "transcendent/duelist.htm", firstTalk);
				break;
			}
			case DREADNOUGHT:
			{
				player.sendPacket(new ExShowScreenMessage(NpcString.DREADNOUGHT_TRANSCENDENT_SKILL_SPINNING_SLASHER, 5000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, false));
				showChatWindow(player, "transcendent/dreadnought.htm", firstTalk);
				break;
			}
			case PHOENIX_KNIGHT:
			{
				player.sendPacket(new ExShowScreenMessage(NpcString.PHOENIX_KNIGHT_TRANSCENDENT_SKILL_SHIELD_STRIKE, 5000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, false));
				showChatWindow(player, "transcendent/phoenix_knight.htm", firstTalk);
				break;
			}
			case HELL_KNIGHT:
			{
				player.sendPacket(new ExShowScreenMessage(NpcString.HELL_KNIGHT_TRANSCENDENT_SKILL_SHIELD_STRIKE, 5000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, false));
				showChatWindow(player, "transcendent/hell_knight.htm", firstTalk);
				break;
			}
			case SAGITTARIUS:
			{
				player.sendPacket(new ExShowScreenMessage(NpcString.SAGITTARIUS_TRANSCENDENT_SKILL_LETHAL_SHOT, 5000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, false));
				showChatWindow(player, "transcendent/sagittarius.htm", firstTalk);
				break;
			}
			case ADVENTURER:
			{
				player.sendPacket(new ExShowScreenMessage(NpcString.ADVENTURER_TRANSCENDENT_SKILL_LETHAL_BLOW, 5000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, false));
				showChatWindow(player, "transcendent/adventurer.htm", firstTalk);
				break;
			}
			case ARCHMAGE:
			{
				player.sendPacket(new ExShowScreenMessage(NpcString.ARCHMAGE_TRANSCENDENT_SKILL_PROMINENCE, 5000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, false));
				showChatWindow(player, "transcendent/archmage.htm", firstTalk);
				break;
			}
			case SOULTAKER:
			{
				player.sendPacket(new ExShowScreenMessage(NpcString.SOULTAKER_TRANSCENDENT_SKILL_DEATH_SPIKE, 5000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, false));
				showChatWindow(player, "transcendent/soultaker.htm", firstTalk);
				break;
			}
			case ARCANA_LORD:
			{
				player.sendPacket(new ExShowScreenMessage(NpcString.ARCANA_LORD_TRANSCENDENT_SKILL_BLAZE, 5000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, false));
				showChatWindow(player, "transcendent/arcana_lord.htm", firstTalk);
				break;
			}
			case CARDINAL:
			{
				player.sendPacket(new ExShowScreenMessage(NpcString.CARDINAL_TRANSCENDENT_SKILL_DIVINE_BEAM, 5000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, false));
				showChatWindow(player, "transcendent/cardinal.htm", firstTalk);
				break;
			}
			case HIEROPHANT:
			{
				player.sendPacket(new ExShowScreenMessage(NpcString.HIEROPHANT_TRANSCENDENT_SKILL_DIVINE_BEAM, 5000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, false));
				showChatWindow(player, "transcendent/hierophant.htm", firstTalk);
				break;
			}
			case EVAS_TEMPLAR:
			{
				player.sendPacket(new ExShowScreenMessage(NpcString.EVAS_TEMPLAR_TRANSCENDENT_SKILL_TRIBUNAL, 5000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, false));
				showChatWindow(player, "transcendent/evas_templar.htm", firstTalk);
				break;
			}
			case SWORD_MUSE:
			{
				player.sendPacket(new ExShowScreenMessage(NpcString.SWORD_MUSE_TRANSCENDENT_SKILL_DEADLY_STRIKE, 5000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, false));
				showChatWindow(player, "transcendent/sword_muse.htm", firstTalk);
				break;
			}
			case WIND_RIDER:
			{
				player.sendPacket(new ExShowScreenMessage(NpcString.WIND_RIDER_TRANSCENDENT_SKILL_LETHAL_BLOW, 5000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, false));
				showChatWindow(player, "transcendent/wind_rider.htm", firstTalk);
				break;
			}
			case MOONLIGHT_SENTINEL:
			{
				player.sendPacket(new ExShowScreenMessage(NpcString.MOONLIGHT_SENTINEL_TRANSCENDENT_SKILL_LETHAL_SHOT, 5000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, false));
				showChatWindow(player, "transcendent/moonlight_sentinel.htm", firstTalk);
				break;
			}
			case MYSTIC_MUSE:
			{
				player.sendPacket(new ExShowScreenMessage(NpcString.MYSTIC_MUSE_TRANSCENDENT_SKILL_HYDRO_BLAST, 5000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, false));
				showChatWindow(player, "transcendent/mystic_muse.htm", firstTalk);
				break;
			}
			case ELEMENTAL_MASTER:
			{
				player.sendPacket(new ExShowScreenMessage(NpcString.ELEMENTAL_MASTER_TRANSCENDENT_SKILL_AQUA_SWIRL, 5000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, false));
				showChatWindow(player, "transcendent/elemental_master.htm", firstTalk);
				break;
			}
			case EVAS_SAINT:
			{
				player.sendPacket(new ExShowScreenMessage(NpcString.EVAS_SAINT_TRANSCENDENT_SKILL_DIVINE_BEAM, 5000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, false));
				showChatWindow(player, "transcendent/evas_saint.htm", firstTalk);
				break;
			}
			case SHILLIEN_TEMPLAR:
			{
				player.sendPacket(new ExShowScreenMessage(NpcString.SHILLIEN_TEMPLAR_TRANSCENDENT_SKILL_JUDGMENT, 5000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, false));
				showChatWindow(player, "transcendent/shillien_templar.htm", firstTalk);
				break;
			}
			case SPECTRAL_DANCER:
			{
				player.sendPacket(new ExShowScreenMessage(NpcString.SPECTRAL_DANCER_TRANSCENDENT_SKILL_DEADLY_STRIKE, 5000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, false));
				showChatWindow(player, "transcendent/spectral_dancer.htm", firstTalk);
				break;
			}
			case GHOST_HUNTER:
			{
				player.sendPacket(new ExShowScreenMessage(NpcString.GHOST_HUNTER_TRANSCENDENT_SKILL_LETHAL_BLOW, 5000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, false));
				showChatWindow(player, "transcendent/ghost_hunter.htm", firstTalk);
				break;
			}
			case GHOST_SENTINEL:
			{
				player.sendPacket(new ExShowScreenMessage(NpcString.GHOST_SENTINEL_TRANSCENDENT_SKILL_LETHAL_SHOT, 5000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, false));
				showChatWindow(player, "transcendent/ghost_sentinel.htm", firstTalk);
				break;
			}
			case STORM_SCREAMER:
			{
				player.sendPacket(new ExShowScreenMessage(NpcString.STORM_SCREAMER_TRANSCENDENT_SKILL_HURRICANE, 5000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, false));
				showChatWindow(player, "transcendent/storm_screamer.htm", firstTalk);
				break;
			}
			case SPECTRAL_MASTER:
			{
				player.sendPacket(new ExShowScreenMessage(NpcString.SPECTRAL_MASTER_TRANSCENDENT_SKILL_TWISTER, 5000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, false));
				showChatWindow(player, "transcendent/spectral_master.htm", firstTalk);
				break;
			}
			case SHILLIEN_SAINT:
			{
				player.sendPacket(new ExShowScreenMessage(NpcString.SHILLIENS_SAINT_TRANSCENDENT_SKILL_DIVINE_BEAM, 5000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, false));
				showChatWindow(player, "transcendent/shillien_saint.htm", firstTalk);
				break;
			}
			case TITAN:
			{
				player.sendPacket(new ExShowScreenMessage(NpcString.TITAN_TRANSCENDENT_SKILL_DEMOLITION_IMPACT, 5000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, false));
				showChatWindow(player, "transcendent/titan.htm", firstTalk);
				break;
			}
			case GRAND_KHAVATARI:
			{
				player.sendPacket(new ExShowScreenMessage(NpcString.GRAND_KHAVATARI_TRANSCENDENT_SKILL_HURRICANE_ASSAULT, 5000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, false));
				showChatWindow(player, "transcendent/grand_khavatari.htm", firstTalk);
				break;
			}
			case DOMINATOR:
			{
				player.sendPacket(new ExShowScreenMessage(NpcString.DOMINATOR_TRANSCENDENT_SKILL_STEAL_ESSENCE, 5000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, false));
				showChatWindow(player, "transcendent/dominator.htm", firstTalk);
				break;
			}
			case DOOMCRYER:
			{
				player.sendPacket(new ExShowScreenMessage(NpcString.DOOMCRYER_TRANSCENDENT_SKILL_STEAL_ESSENCE, 5000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, false));
				showChatWindow(player, "transcendent/doomcryer.htm", firstTalk);
				break;
			}
			case FORTUNE_SEEKER:
			{
				player.sendPacket(new ExShowScreenMessage(NpcString.FORTUNE_SEEKER_TRANSCENDENT_SKILL_SPOIL_CRUSH, 5000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, false));
				showChatWindow(player, "transcendent/fortune_seeker.htm", firstTalk);
				break;
			}
			case MAESTRO:
			{
				player.sendPacket(new ExShowScreenMessage(NpcString.MAESTRO_TRANSCENDENT_SKILL_EARTH_TREMOR, 5000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, false));
				showChatWindow(player, "transcendent/maestro.htm", firstTalk);
				break;
			}
			case BERSERKER:
			{
				player.sendPacket(new ExShowScreenMessage(NpcString.BERSERKER_TRANSCENDENT_SKILL_SOUL_IMPULSE, 5000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, false));
				showChatWindow(player, "transcendent/berserker.htm", firstTalk);
				break;
			}
			case SOUL_BREAKER:
			{
				player.sendPacket(new ExShowScreenMessage(NpcString.SOUL_BREAKER_TRANSCENDENT_SKILL_SOUL_PIERCING, 5000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, false));
				showChatWindow(player, "transcendent/soul_breaker.htm", firstTalk);
				break;
			}
			case SOUL_RANGER:
			{
				player.sendPacket(new ExShowScreenMessage(NpcString.SOUL_RANGER_TRANSCENDENT_SKILL_TWIN_SHOT, 5000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, false));
				showChatWindow(player, "transcendent/soul_ranger.htm", firstTalk);
				break;
			}
			case DOOMBRINGER:
			{
				player.sendPacket(new ExShowScreenMessage(NpcString.DOOMBRINGER_TRANSCENDENT_SKILL_SOUL_IMPULSE, 5000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, false));
				showChatWindow(player, "transcendent/doombringer.htm", firstTalk);
				break;
			}
			case SOUL_HOUND:
			{
				player.sendPacket(new ExShowScreenMessage(NpcString.SOUL_HOUND_TRANSCENDENT_SKILL_SOUL_PIERCING, 5000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, false));
				showChatWindow(player, "transcendent/soul_hound.htm", firstTalk);
				break;
			}
			case TRICKSTER:
			{
				player.sendPacket(new ExShowScreenMessage(NpcString.TRICKSTER_TRANSCENDENT_SKILL_TWIN_SHOT, 5000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, false));
				showChatWindow(player, "transcendent/trickster.htm", firstTalk);
				break;
			}
			case H_DEATH_MESSENGER:
			case E_DEATH_MESSENGER:
			case DE_DEATH_MESSENGER:
			{
				player.sendPacket(new ExShowScreenMessage(NpcString.DEATH_MESSENGER_TRANSCENDENT_SKILL_WIPEOUT, 5000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, false));
				showChatWindow(player, "transcendent/death_messenger.htm", firstTalk);
				break;
			}
			case H_DEATH_KNIGHT:
			case E_DEATH_KNIGHT:
			case DE_DEATH_KNIGHT:
			{
				player.sendPacket(new ExShowScreenMessage(NpcString.DEATH_KNIGHT_TRANSCENDENT_SKILL_WIPEOUT, 5000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, false));
				showChatWindow(player, "transcendent/death_knight.htm", firstTalk);
				break;
			}
			case WIND_SNIPER:
			{
				player.sendPacket(new ExShowScreenMessage(NpcString.WIND_SNIPER_TRANSCENDENT_SKILL_FREEZING_WOUND, 5000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, false));
				showChatWindow(player, "transcendent/wind_sniper.htm", firstTalk);
				break;
			}
			case STORM_BLASTER:
			{
				player.sendPacket(new ExShowScreenMessage(NpcString.STORM_BLASTER_TRANSCENDENT_SKILL_FREEZING_WOUND, 5000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, false));
				showChatWindow(player, "transcendent/storm_blaster.htm", firstTalk);
				break;
			}
			case DRAGOON:
			{
				player.sendPacket(new ExShowScreenMessage(NpcString.DRAGOON_TRANSCENDENT_SKILLS_PIERCING_WILD_SCRATCH, 5000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, false));
				showChatWindow(player, "transcendent/dragoon.htm", firstTalk);
				break;
			}
			case VANGUARD_RIDER:
			{
				player.sendPacket(new ExShowScreenMessage(NpcString.VANGUARD_RIDER_TRANSCENDENT_SKILLS_AMAZING_PIERCING_SHADOW_SCRATCH, 5000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, false));
				showChatWindow(player, "transcendent/vanguard_rider.htm", firstTalk);
				break;
			}
			default:
				break;
		}

		stage = 1;
		ThreadPoolManager.getInstance().schedule(() ->
		{
			stage = 2;
		}, 600000L);

		SkillEntry skillEntryBase = SkillEntry.makeSkillEntry(SkillEntryType.NONE, 45197, 1);
		SkillEntry skillEntryKama = SkillEntry.makeSkillEntry(SkillEntryType.NONE, 45198, 1);

		if ((getReflectionId() > 101) && (getReflectionId() < 105))
		{
			SkillEntry prophecy = SkillEntry.makeSkillEntry(SkillEntryType.NONE, 59829, 1);
			altUseSkill(prophecy, player);
		}

		if (player.getRace() != Race.KAMAEL)
		{
			altUseSkill(skillEntryBase, player);
		}
		else
		{
			altUseSkill(skillEntryKama, player);
		}
	}

	@Override
	public void onMenuSelect(Player player, int ask, long reply, int state)
	{
		if (ask == -20210813)
		{
			if (reply == 1)
			{
				Location STABLE_LOCATION = new Location(-119664, 246306, 1232);
				String back = player.getVar("backCoords");
				if (back != null)
				{
					Location loc = Location.parseLoc(back);
					loc.setZ(loc.getZ() + 100);
					player.teleToLocation(loc, ReflectionManager.MAIN);
					player.unsetVar("backCoords");
				}
				else
					player.setLoc(STABLE_LOCATION);
			}
		}
	}
}
