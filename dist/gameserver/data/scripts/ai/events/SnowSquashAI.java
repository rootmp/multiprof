package ai.events;

import l2s.commons.util.Rnd;
import l2s.gameserver.ai.DefaultAI;
import l2s.gameserver.geometry.Location;
import l2s.gameserver.model.Creature;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.Skill;
import l2s.gameserver.model.entity.Reflection;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.network.l2.components.NpcString;
import l2s.gameserver.network.l2.s2c.PlaySoundPacket;
import l2s.gameserver.skills.SkillEntry;
import l2s.gameserver.skills.enums.SkillEntryType;
import l2s.gameserver.utils.ChatUtils;
import l2s.gameserver.utils.NpcUtils;

/**
 * @author Bonux
 **/
public class SnowSquashAI extends DefaultAI
{
	private static final int small_baby_gourd = 13399; // Молодая Тыква
	private static final int g_small_adult_gourd = 13400; // Спелая Тыква
	private static final int b_small_adult_gourd = 13401; // Завядшая Тыква
	private static final int big_baby_gourd = 13403; // Большая Молодая Тыква
	private static final int g_big_adult_gourd = 13404; // Большая Спелая Тыква
	private static final int b_big_adult_gourd = 13405; // Большая Завядшая Тыква
	private static final int kg_small_adult_gourd = 13402; // Королевская Тыква
	private static final int kg_big_adult_gourd = 13406; // Большая Королевская Тыква

	private static final int s_gourd_nectar = 17110; // Нектар Снежной Тыквы
	private static final int s_gourd_nectar_good = 4513; // Тыква - Повышение Уровня
	private static final int s_gourd_nectar_bad = 4514; // Тыква - Отравление

	private static final NpcString[] ON_SPAWN_TEXTS_1 = new NpcString[]
	{
		NpcString.WHATS_THIS_WHY_AM_I_BEING_DISTURBED,
		NpcString.TADA_HERE_I_AM,
		NpcString.WHAT_ARE_YOU_LOOKING_AT,
		NpcString.IF_YOU_GIVE_ME_NECTAR_THIS_LITTLE_WINTERMELON_WILL_GROW_UP_QUICKLY,
		NpcString.ARE_YOU_MY_MOMMY,
		NpcString.FANCY_MEETING_YOU_HERE,
		NpcString.ARE_YOU_AFRAID_OF_THE_BIGBAD_WINTERMLON,
		NpcString.IMPRESSIVE_ARENT_I,
		NpcString.OBEY_ME,
		NpcString.RAISE_ME_WELL_AND_YOULL_BE_REWARDED_NEGLECT_ME_AND_SUFFER_THE_CONSEQUENCES
	};

	private static final NpcString[] ON_SPAWN_TEXTS_2 = new NpcString[]
	{
		NpcString.BRING_ME_NECTAR,
		NpcString.I_MUST_HAVE_NECTAR_TO_GROW,
		NpcString.GIVE_ME_SOME_NECTAR_QUICKLY_OR_YOULL_GET_NOTHING,
		NpcString.PLEASE_GIVE_ME_SOME_NECTAR_IM_HUNGRY,
		NpcString.NECTAR_PLEASE,
		NpcString.NECTAR_WILL_MAKE_ME_GROW_QUICKLY,
		NpcString.DONT_YOU_WANT_A_BIGGER_WINTERMELON_GIVE_ME_SOME_NECTAR_AND_ILL_GROW_MUCH_LARGER,
		NpcString.IF_YOU_RAISE_ME_WELL_YOULL_GET_PRIZES_OR_NOT,
		NpcString.YOU_ARE_HERE_FOR_THE_STUFF_EH_WELL_ITS_MINE_ALL_MINE,
		NpcString.TRUST_ME_GIVE_ME_SOME_NECTAR_AND_ILL_BECOME_A_GIANT_WINTERMELON
	};

	private static final NpcString[] WAIT_TEXTS = new NpcString[]
	{
		NpcString.SO_LONG_LOSERS,
		NpcString.IM_OUT_OF_HERE,
		NpcString.I_MUST_BE_GOING_HAVE_FUN_EVERYBODY,
		NpcString.TIME_IS_UP_PUT_YOUR_WEAPONS_DOWN,
		NpcString.GOOD_FOR_ME_BAD_FOR_YOU
	};

	private static final NpcString[] ON_ATTACK_TEXTS_1 = new NpcString[]
	{
		NpcString.KEEP_IT_COMING,
		NpcString.THATS_WHAT_IM_TALKING_ABOUT,
		NpcString.MAY_I_HAVE_SOME_MORE,
		NpcString.THAT_HIT_THE_SPOT,
		NpcString.I_FEEL_SPECIAL,
		NpcString.I_THINK_ITS_WORKING,
		NpcString.YOU_DO_UNDERSTAND,
		NpcString.YUCK_WHAT_IS_THIS_HA_HA_JUST_KIDDING,
		NpcString.A_TOTAL_OF_FIVE_AND_ILL_BE_TWICE_AS_ALIVE,
		NpcString.NECTAR_IS_SUBLIME
	};

	private static final NpcString[] ON_ATTACK_TEXTS_2 = new NpcString[]
	{
		NpcString.TRANSFORM,
		NpcString.I_FEEL_DIFFERENT,
		NpcString.IM_BIGGER_NOW_BRING_IT_ON,
		NpcString.IM_NOT_A_KID_ANYMORE,
		NpcString.BIG_TIME,
		NpcString.GOOD_LUCK,
		NpcString.IM_ALL_GROWN_UP_NOW,
		NpcString.IF_YOU_LET_ME_GO_ILL_BE_YOUR_BEST_FRIEND,
		NpcString.IM_CHUCK_FULL_OF_GOODNESS,
		NpcString.GOOD_JOB_NOW_WHAT_ARE_YOU_GOING_TO_DO
	};

	private static final NpcString[] ON_ATTACK_TEXTS_3 = new NpcString[]
	{
		NpcString.YOU_CALL_THAT_A_HIT,
		NpcString.WHY_ARE_YOU_HITTING_ME_OUCH_STOP_IT_GIVE_ME_NECTAR,
		NpcString.STOP_OR_ILL_WILT,
		NpcString.IM_NOT_FULLY_GROWN_YET_OH_WELL_DO_WHAT_YOU_WILL_ILL_FADE_AWAY_WITHOUT_NECTAR_ANYWAY,
		NpcString.GO_AHEAD_AND_HIT_ME_AGAIN_IT_WONT_DO_YOU_ANY_GOOD,
		NpcString.WOE_IS_ME_IM_WILTING,
		NpcString.IM_NOT_FULLY_GROWN_YET_HOW_ABOUT_SOME_NECTAR_TO_EASE_MY_PAIN,
		NpcString.THE_END_IS_NEAR,
		NpcString.PRETTY_PLEASE_WITH_SUGAR_ON_TOP_GIVE_ME_SOME_NECTAR,
		NpcString.IF_I_DIE_WITHOUT_NECTAR_YOULL_GET_NOTHING
	};

	private static final NpcString[] ON_ATTACK_TEXTS_4 = new NpcString[]
	{
		NpcString.BETTER_LUCK_NEXT_TIME,
		NpcString.NICE_SHOT,
		NpcString.IM_NOT_AFRAID_OF_YOU,
		NpcString.IF_I_KNEW_THIS_WAS_GOING_TO_HAPPEN_I_WOULD_HAVE_STAYED_HOME,
		NpcString.TRY_HARDER_OR_IM_OUT_OF_HERE,
		NpcString.IM_TOUGHER_THAN_I_LOOK,
		NpcString.GOOD_STRIKE,
		NpcString.OH_MY_GOURD,
		NpcString.THATS_ALL_YOUVE_GOT,
		NpcString.WHY_ME
	};

	private static final NpcString[] ON_ATTACK_TEXTS_5 = new NpcString[]
	{
		NpcString.SOUNDTASTIC,
		NpcString.I_CAN_SING_ALONG_IF_YOU_LIKE,
		NpcString.I_THINK_YOU_NEED_SOME_BACKUP,
		NpcString.KEEP_UP_THAT_RHYTHM_AND_YOULL_BE_A_STAR,
		NpcString.MY_HEART_YEARNS_FOR_MORE_MUSIC,
		NpcString.YOURE_OUT_OF_TUNE_AGAIN,
		NpcString.THIS_IS_AWFUL,
		NpcString.I_THINK_I_BROKE_SOMETHING,
		NpcString.WHAT_A_LOVELY_MELODY_PLAY_IT_AGAIN,
		NpcString.MUSIC_TO_MY_UH_EARS
	};

	private static final NpcString[] ON_ATTACK_TEXTS_6 = new NpcString[]
	{
		NpcString.YOU_NEED_MUSIC_LESSONS,
		NpcString.I_CANT_HEAR_YOU,
		NpcString.YOU_CANT_HURT_ME_LIKE_THAT,
		NpcString.IM_STRONGER_THAN_YOU_ARE,
		NpcString.NO_MUSIC_IM_OUT_OF_HERE,
		NpcString.THAT_RACKET_IS_GETTING_ON_MY_NERVES_TONE_IT_DOWN_A_BIT,
		NpcString.YOU_CAN_ONLY_HURT_ME_THROUGH_MUSIC,
		NpcString.ONLY_MUSICAL_INSTRUMENTS_CAN_HURT_ME_NOTHING_ELSE,
		NpcString.YOUR_SKILLS_ARE_IMPRESSIVE_BUT_SADLY_USELESS,
		NpcString.CATCH_A_CHRONO_FOR_ME_PLEASE
	};

	private static final NpcString[] ON_DEAD_TEXTS_1 = new NpcString[]
	{
		NpcString.IM_FEELING_BETTER_ANOTHER_THIRTY_SECONDS_AND_ILL_BE_OUT_OF_HERE,
		NpcString.TWENTY_SECONDS_AND_ITS_CIAO_BABY,
		NpcString.WOOHOO_JUST_TEN_SECONDS_LEFT_NINE_EIGHT_SEVEN,
		NpcString.GIVE_ME_NECTAR_OR_ILL_BE_GONE_IN_TWO_MINUTES,
		NpcString.GIVE_ME_NECTAR_OR_ILL_BE_GONE_IN_ONE_MINUTE
	};

	private static final NpcString[] ON_DEAD_TEXTS_2 = new NpcString[]
	{
		NpcString.YOU_GOT_ME,
		NpcString.NOW_LOOK_AT_WHAT_YOUVE_DONE,
		NpcString.YOU_WIN,
		NpcString.FINGERS_CROSSED,
		NpcString.DONT_TELL_ANYONE,
		NpcString.GROSS_MY_GUTS_ARE_COMING_OUT,
		NpcString.TAKE_IT_AND_GO,
		NpcString.I_SHOULDVE_LEFT_WHEN_I_COULD,
		NpcString.NOW_LOOK_WHAT_YOU_HAVE_DONE,
		NpcString.I_FEEL_DIRTY
	};

	private boolean _attacked = false;

	private int _tryCount = 0;
	private int _successCount = 0;
	private int _failCount = 0;

	public SnowSquashAI(NpcInstance actor)
	{
		super(actor);
	}

	@Override
	protected void onEvtSpawn()
	{
		super.onEvtSpawn();

		NpcInstance actor = getActor();
		if (actor == null)
			return;

		int npcId = actor.getNpcId();
		if (npcId == small_baby_gourd)
		{
			ChatUtils.say(actor, Rnd.get(ON_SPAWN_TEXTS_1));
			addTimer(99702, 1000 * 2);
			addTimer(99723, 1000 * 60);
		}
		else if (npcId == g_small_adult_gourd)
			addTimer(99711, 1000 * 60);
		else if (npcId == b_small_adult_gourd)
			addTimer(99714, 1000 * 60);
		else if (npcId == big_baby_gourd)
		{
			ChatUtils.say(actor, Rnd.get(ON_SPAWN_TEXTS_1));
			addTimer(99704, 1000 * 3);
			addTimer(99725, 1000 * 60);
		}
		else if (npcId == g_big_adult_gourd)
			addTimer(99717, 1000 * 60);
		else if (npcId == b_big_adult_gourd)
			addTimer(99720, 1000 * 60);
		else if (npcId == kg_small_adult_gourd)
			addTimer(99711, 1000 * 60);
		else if (npcId == kg_big_adult_gourd)
			addTimer(99717, 1000 * 60);
	}

	@Override
	protected void onEvtTimer(int timerId, Object arg1, Object arg2)
	{
		super.onEvtTimer(timerId, arg1, arg2);

		NpcInstance actor = getActor();
		if (actor == null)
			return;

		int npcId = actor.getNpcId();
		if (npcId == small_baby_gourd)
		{
			if (timerId == 99702)
				ChatUtils.say(actor, Rnd.get(ON_SPAWN_TEXTS_2));
			else if (timerId == 99723)
			{
				ChatUtils.say(actor, NpcString.GIVE_ME_NECTAR_OR_ILL_BE_GONE_IN_TWO_MINUTES);
				addTimer(99724, 1000 * 60);
			}
			else if (timerId == 99724)
			{
				ChatUtils.say(actor, NpcString.GIVE_ME_NECTAR_OR_ILL_BE_GONE_IN_ONE_MINUTE);
				addTimer(99709, 1000 * 60);
			}
			else if (timerId == 99709)
				actor.deleteMe();
		}
		else if (npcId == g_small_adult_gourd)
		{
			if (timerId == 99701)
			{
				ChatUtils.say(actor, Rnd.get(WAIT_TEXTS));
				actor.deleteMe();
			}
			else if (timerId == 99711)
			{
				ChatUtils.say(actor, NpcString.IM_FEELING_BETTER_ANOTHER_THIRTY_SECONDS_AND_ILL_BE_OUT_OF_HERE);
				addTimer(99712, 1000 * 10);
			}
			else if (timerId == 99712)
			{
				ChatUtils.say(actor, NpcString.TWENTY_SECONDS_AND_ITS_CIAO_BABY);
				addTimer(99713, 1000 * 10);
			}
			else if (timerId == 99713)
			{
				ChatUtils.say(actor, NpcString.WOOHOO_JUST_TEN_SECONDS_LEFT_NINE_EIGHT_SEVEN);
				addTimer(99701, 1000 * 10);
			}
		}
		else if (npcId == b_small_adult_gourd)
		{
			if (timerId == 99703)
			{
				ChatUtils.say(actor, Rnd.get(WAIT_TEXTS));
				actor.deleteMe();
			}
			else if (timerId == 99714)
			{
				ChatUtils.say(actor, NpcString.IM_FEELING_BETTER_ANOTHER_THIRTY_SECONDS_AND_ILL_BE_OUT_OF_HERE);
				addTimer(99715, 1000 * 10);
			}
			else if (timerId == 99715)
			{
				ChatUtils.say(actor, NpcString.TWENTY_SECONDS_AND_ITS_CIAO_BABY);
				addTimer(99716, 1000 * 10);
			}
			else if (timerId == 99716)
			{
				ChatUtils.say(actor, NpcString.WOOHOO_JUST_TEN_SECONDS_LEFT_NINE_EIGHT_SEVEN);
				addTimer(99703, 1000 * 10);
			}
		}
		else if (npcId == big_baby_gourd)
		{
			if (timerId == 99704)
				ChatUtils.say(actor, Rnd.get(ON_SPAWN_TEXTS_2));
			else if (timerId == 99725)
			{
				ChatUtils.say(actor, NpcString.GIVE_ME_NECTAR_OR_ILL_BE_GONE_IN_TWO_MINUTES);
				addTimer(99726, 1000 * 60);
			}
			else if (timerId == 99726)
			{
				ChatUtils.say(actor, NpcString.GIVE_ME_NECTAR_OR_ILL_BE_GONE_IN_ONE_MINUTE);
				addTimer(99710, 1000 * 60);
			}
			else if (timerId == 99710)
				actor.deleteMe();
		}
		else if (npcId == g_big_adult_gourd)
		{
			if (timerId == 99705)
			{
				ChatUtils.say(actor, Rnd.get(WAIT_TEXTS));
				actor.deleteMe();
			}
			else if (timerId == 99717)
			{
				ChatUtils.say(actor, NpcString.IM_FEELING_BETTER_ANOTHER_THIRTY_SECONDS_AND_ILL_BE_OUT_OF_HERE);
				addTimer(99718, 1000 * 10);
			}
			else if (timerId == 99718)
			{
				ChatUtils.say(actor, NpcString.TWENTY_SECONDS_AND_ITS_CIAO_BABY);
				addTimer(99719, 1000 * 10);
			}
			else if (timerId == 99719)
			{
				ChatUtils.say(actor, NpcString.WOOHOO_JUST_TEN_SECONDS_LEFT_NINE_EIGHT_SEVEN);
				addTimer(99705, 1000 * 10);
			}
		}
		else if (npcId == b_big_adult_gourd)
		{
			if (timerId == 99706)
			{
				ChatUtils.say(actor, Rnd.get(WAIT_TEXTS));
				actor.deleteMe();
			}
			else if (timerId == 99720)
			{
				ChatUtils.say(actor, NpcString.IM_FEELING_BETTER_ANOTHER_THIRTY_SECONDS_AND_ILL_BE_OUT_OF_HERE);
				addTimer(99721, 1000 * 10);
			}
			else if (timerId == 99721)
			{
				ChatUtils.say(actor, NpcString.TWENTY_SECONDS_AND_ITS_CIAO_BABY);
				addTimer(99722, 1000 * 10);
			}
			else if (timerId == 99722)
			{
				ChatUtils.say(actor, NpcString.WOOHOO_JUST_TEN_SECONDS_LEFT_NINE_EIGHT_SEVEN);
				addTimer(99706, 1000 * 10);
			}
		}
		else if (npcId == kg_small_adult_gourd)
		{
			if (timerId == 99701)
			{
				ChatUtils.say(actor, Rnd.get(WAIT_TEXTS));
				actor.deleteMe();
			}
			else if (timerId == 99711)
			{
				ChatUtils.say(actor, NpcString.IM_FEELING_BETTER_ANOTHER_THIRTY_SECONDS_AND_ILL_BE_OUT_OF_HERE);
				addTimer(99712, 1000 * 10);
			}
			else if (timerId == 99712)
			{
				ChatUtils.say(actor, NpcString.TWENTY_SECONDS_AND_ITS_CIAO_BABY);
				addTimer(99713, 1000 * 10);
			}
			else if (timerId == 99713)
			{
				ChatUtils.say(actor, NpcString.WOOHOO_JUST_TEN_SECONDS_LEFT_NINE_EIGHT_SEVEN);
				addTimer(99701, 1000 * 10);
			}
		}
		else if (npcId == kg_big_adult_gourd)
		{
			if (timerId == 99705)
			{
				ChatUtils.say(actor, Rnd.get(WAIT_TEXTS));
				actor.deleteMe();
			}
			else if (timerId == 99717)
			{
				ChatUtils.say(actor, NpcString.IM_FEELING_BETTER_ANOTHER_THIRTY_SECONDS_AND_ILL_BE_OUT_OF_HERE);
				addTimer(99718, 1000 * 10);
			}
			else if (timerId == 99718)
			{
				ChatUtils.say(actor, NpcString.TWENTY_SECONDS_AND_ITS_CIAO_BABY);
				addTimer(99719, 1000 * 10);
			}
			else if (timerId == 99719)
			{
				ChatUtils.say(actor, NpcString.WOOHOO_JUST_TEN_SECONDS_LEFT_NINE_EIGHT_SEVEN);
				addTimer(99705, 1000 * 10);
			}
		}
	}

	@Override
	protected void onEvtSeeSpell(Skill skill, Creature caster, Creature target)
	{
		NpcInstance actor = getActor();
		if (actor == null || actor != target)
			return;

		onEvtAttacked(caster, skill, 0);
	}

	@Override
	protected void onEvtAttacked(Creature attacker, Skill skill, int damage)
	{
		NpcInstance actor = getActor();
		if (actor == null || actor.isDead())
			return;

		int npcId = actor.getNpcId();
		int skillId = skill == null ? 0 : skill.getId();
		if (npcId == small_baby_gourd)
		{
			if (!_attacked)
			{
				_tryCount = 0;
				_successCount = 0;
				_failCount = 0;

				if (skillId == s_gourd_nectar)
				{
					_tryCount++;

					if (Rnd.get(1000) < 631)
					{
						_successCount++;

						ChatUtils.say(actor, Rnd.get(ON_ATTACK_TEXTS_1));
						actor.doCast(SkillEntry.makeSkillEntry(SkillEntryType.NONE, s_gourd_nectar_good, 1), actor, false);
					}
					else
					{
						_failCount++;

						ChatUtils.say(actor, Rnd.get(ON_ATTACK_TEXTS_1));
						actor.doCast(SkillEntry.makeSkillEntry(SkillEntryType.NONE, s_gourd_nectar_bad, 1), actor, false);
					}
				}
				_attacked = true;
			}
			else
			{
				if (skillId == s_gourd_nectar && _tryCount >= 4)
				{
					_tryCount++;

					ChatUtils.say(actor, Rnd.get(ON_ATTACK_TEXTS_2));

					Player owner = actor.getPlayer();
					Location loc = actor.getLoc();
					Reflection reflection = actor.getReflection();

					actor.deleteMe();

					if (Rnd.get(1000) < 631)
						_successCount++;
					else
						_failCount++;

					if (_successCount == 5)
					{
						spawnNextNpc(kg_small_adult_gourd, loc, reflection, owner);

						_successCount = 0;
						_failCount = 0;
					}
					else if (_successCount == 4)
					{
						spawnNextNpc(g_small_adult_gourd, loc, reflection, owner);

						_successCount = 0;
						_failCount = 0;
					}
					else
					{
						spawnNextNpc(b_small_adult_gourd, loc, reflection, owner);

						_successCount = 0;
						_failCount = 0;
					}
					attacker.sendPacket(new PlaySoundPacket("ItemSound3.sys_sow_success"));
				}
				else if (skillId == s_gourd_nectar)
				{
					_tryCount++;

					if (Rnd.get(1000) < 631)
					{
						_successCount++;

						ChatUtils.say(actor, Rnd.get(ON_ATTACK_TEXTS_1));
						actor.doCast(SkillEntry.makeSkillEntry(SkillEntryType.NONE, s_gourd_nectar_good, 1), actor, false);
					}
					else
					{
						_failCount++;

						ChatUtils.say(actor, Rnd.get(ON_ATTACK_TEXTS_1));
						actor.doCast(SkillEntry.makeSkillEntry(SkillEntryType.NONE, s_gourd_nectar_bad, 1), actor, false);
					}
				}
				else if (Rnd.get(2) == 0)
					ChatUtils.say(actor, Rnd.get(ON_ATTACK_TEXTS_3));
			}
		}
		else if (npcId == g_small_adult_gourd)
		{
			if (!_attacked)
			{
				ChatUtils.say(actor, Rnd.get(ON_ATTACK_TEXTS_4));
				_attacked = true;
			}
			else
			{
				if (Rnd.get(2) == 0)
					ChatUtils.say(actor, Rnd.get(ON_ATTACK_TEXTS_4));
			}
		}
		else if (npcId == b_small_adult_gourd)
		{
			if (!_attacked)
			{
				ChatUtils.say(actor, Rnd.get(ON_ATTACK_TEXTS_4));
				_attacked = true;
			}
			else
			{
				if (Rnd.get(2) == 0)
					ChatUtils.say(actor, Rnd.get(ON_ATTACK_TEXTS_4));
			}
		}
		else if (npcId == big_baby_gourd)
		{
			if (!_attacked)
			{
				_tryCount = 0;
				_successCount = 0;
				_failCount = 0;

				if (skillId == s_gourd_nectar)
				{
					_tryCount++;

					if (Rnd.get(1000) < 631)
					{
						_successCount++;

						ChatUtils.say(actor, Rnd.get(ON_ATTACK_TEXTS_1));
						actor.doCast(SkillEntry.makeSkillEntry(SkillEntryType.NONE, s_gourd_nectar_good, 1), actor, false);
					}
					else
					{
						_failCount++;

						ChatUtils.say(actor, Rnd.get(ON_ATTACK_TEXTS_1));
						actor.doCast(SkillEntry.makeSkillEntry(SkillEntryType.NONE, s_gourd_nectar_bad, 1), actor, false);
					}
				}
				_attacked = true;
			}
			else
			{
				if (skillId == s_gourd_nectar && _tryCount >= 4)
				{
					_tryCount++;

					ChatUtils.say(actor, Rnd.get(ON_ATTACK_TEXTS_2));

					Player owner = actor.getPlayer();
					Location loc = actor.getLoc();
					Reflection reflection = actor.getReflection();

					actor.deleteMe();

					if (Rnd.get(1000) < 631)
						_successCount++;
					else
						_failCount++;

					if (_successCount == 5)
					{
						spawnNextNpc(kg_big_adult_gourd, loc, reflection, owner);

						_successCount = 0;
						_failCount = 0;
					}
					else if (_successCount == 4)
					{
						spawnNextNpc(g_big_adult_gourd, loc, reflection, owner);

						_successCount = 0;
						_failCount = 0;
					}
					else
					{
						spawnNextNpc(b_big_adult_gourd, loc, reflection, owner);

						_successCount = 0;
						_failCount = 0;
					}
					attacker.sendPacket(new PlaySoundPacket("ItemSound3.sys_sow_success"));
				}
				else if (skillId == s_gourd_nectar)
				{
					_tryCount++;

					if (Rnd.get(1000) < 631)
					{
						_successCount++;

						ChatUtils.say(actor, Rnd.get(ON_ATTACK_TEXTS_1));
						actor.doCast(SkillEntry.makeSkillEntry(SkillEntryType.NONE, s_gourd_nectar_good, 1), actor, false);
					}
					else
					{
						_failCount++;

						ChatUtils.say(actor, Rnd.get(ON_ATTACK_TEXTS_1));
						actor.doCast(SkillEntry.makeSkillEntry(SkillEntryType.NONE, s_gourd_nectar_bad, 1), actor, false);
					}
				}
				else if (Rnd.get(5) == 0)
					ChatUtils.say(actor, Rnd.get(ON_ATTACK_TEXTS_3));
			}
		}
		else if (npcId == g_big_adult_gourd)
		{
			if (!_attacked)
			{
				int weaponId = attacker.getActiveWeaponInstance() == null ? 0 : attacker.getActiveWeaponInstance().getItemId();
				if (weaponId == 5817 || weaponId == 4202 || weaponId == 5133 || weaponId == 7058 || weaponId == 8350)
					ChatUtils.say(actor, Rnd.get(ON_ATTACK_TEXTS_5));

				_attacked = true;
			}
			else
			{
				int weaponId = attacker.getActiveWeaponInstance() == null ? 0 : attacker.getActiveWeaponInstance().getItemId();
				if (weaponId == 5817 || weaponId == 4202 || weaponId == 5133 || weaponId == 7058 || weaponId == 8350)
				{
					if (Rnd.get(5) == 0)
						ChatUtils.say(actor, Rnd.get(ON_ATTACK_TEXTS_5));
				}
				else if (Rnd.get(3) == 0)
					ChatUtils.say(actor, Rnd.get(ON_ATTACK_TEXTS_6));
			}
		}
		else if (npcId == b_big_adult_gourd)
		{
			if (!_attacked)
			{
				int weaponId = attacker.getActiveWeaponInstance() == null ? 0 : attacker.getActiveWeaponInstance().getItemId();
				if (weaponId == 5817 || weaponId == 4202 || weaponId == 5133 || weaponId == 7058 || weaponId == 8350)
					ChatUtils.say(actor, Rnd.get(ON_ATTACK_TEXTS_5));

				_attacked = true;
			}
			else
			{
				int weaponId = attacker.getActiveWeaponInstance() == null ? 0 : attacker.getActiveWeaponInstance().getItemId();
				if (weaponId == 5817 || weaponId == 4202 || weaponId == 5133 || weaponId == 7058 || weaponId == 8350)
				{
					if (Rnd.get(5) == 0)
						ChatUtils.say(actor, Rnd.get(ON_ATTACK_TEXTS_5));
				}
				else if (Rnd.get(3) == 0)
					ChatUtils.say(actor, Rnd.get(ON_ATTACK_TEXTS_6));
			}
		}
		else if (npcId == kg_small_adult_gourd)
		{
			if (!_attacked)
			{
				ChatUtils.say(actor, Rnd.get(ON_ATTACK_TEXTS_4));
				_attacked = true;
			}
			else
			{
				if (Rnd.get(2) == 0)
					ChatUtils.say(actor, Rnd.get(ON_ATTACK_TEXTS_4));
			}
		}
		else if (npcId == kg_big_adult_gourd)
		{
			if (!_attacked)
			{
				int weaponId = attacker.getActiveWeaponInstance() == null ? 0 : attacker.getActiveWeaponInstance().getItemId();
				if (weaponId == 5817 || weaponId == 4202 || weaponId == 5133 || weaponId == 7058 || weaponId == 8350)
					ChatUtils.say(actor, Rnd.get(ON_ATTACK_TEXTS_5));

				_attacked = true;
			}
			else
			{
				int weaponId = attacker.getActiveWeaponInstance() == null ? 0 : attacker.getActiveWeaponInstance().getItemId();
				if (weaponId == 5817 || weaponId == 4202 || weaponId == 5133 || weaponId == 7058 || weaponId == 8350)
				{
					if (Rnd.get(5) == 0)
						ChatUtils.say(actor, Rnd.get(ON_ATTACK_TEXTS_5));
				}
				else if (Rnd.get(3) == 0)
					ChatUtils.say(actor, Rnd.get(ON_ATTACK_TEXTS_6));
			}
		}
	}

	@Override
	protected void onEvtDead(Creature killer)
	{
		super.onEvtDead(killer);

		NpcInstance actor = getActor();
		if (actor == null)
			return;

		if (killer != null && actor.getDistance(killer) <= 1500)
		{
			int npcId = actor.getNpcId();
			if (npcId == small_baby_gourd)
				ChatUtils.say(actor, Rnd.get(ON_DEAD_TEXTS_1));
			else if (npcId == g_small_adult_gourd) // Спелая Тыква
			{
				ChatUtils.say(actor, Rnd.get(ON_DEAD_TEXTS_2));

				int dropedCount = 0;
				if (Rnd.chance(5))
				{
					dropedCount++;
					if (Rnd.chance(10))
					{
						if (Rnd.chance(20))
							actor.dropItem(actor.getPlayer(), 49667, 1); // Сундук с Оружием - Ранг C
						else if (Rnd.chance(30))
							actor.dropItem(actor.getPlayer(), 49668, 1); // Сундук с Доспехами - Ранг C
						else
							actor.dropItem(actor.getPlayer(), 49669, 1); // Сундук с Аксессуаром - Ранг C
					}
					else
					{
						if (Rnd.chance(5))
							actor.dropItem(actor.getPlayer(), 70000, 1); // Запечатанная Руна - Ур. 1
						else if (Rnd.chance(10))
							actor.dropItem(actor.getPlayer(), 49541, 1); // Сундук с Книгой Заклинаний
						else if (Rnd.chance(20))
							actor.dropItem(actor.getPlayer(), 29008, 1); // Свиток Опыта/SP
						else if (Rnd.chance(30))
							actor.dropItem(actor.getPlayer(), 3959, 1); // Свиток Благ. Воскрешения
						else
							actor.dropItem(actor.getPlayer(), 3958, 1); // Свиток Благ. Телепорта
					}
				}

				if (Rnd.chance(10))
				{
					dropedCount++;
					if (Rnd.chance(5))
						actor.dropItem(actor.getPlayer(), 70354, 1); // Свиток Трансформации в Темного Ассасина
					else if (Rnd.chance(10))
						actor.dropItem(actor.getPlayer(), 49081, 1); // Рисовый Пирожок Пылающего Боевого Духа
					else if (Rnd.chance(20))
						actor.dropItem(actor.getPlayer(), 49080, 1); // Рисовый Пирожок Боевого Духа
					else if (Rnd.chance(30))
						actor.dropItem(actor.getPlayer(), 1540, 1); // Быстродействующее Зелье Исцеления
					else
						actor.dropItem(actor.getPlayer(), 29697, 1); // Свиток Эффектов 1-й Профессии
				}

				if (Rnd.chance(20))
				{
					dropedCount++;
					int i = Rnd.get(9);
					switch (i)
					{
						case 0:
							actor.dropItem(actor.getPlayer(), 4039, 1); // Клей
							break;
						case 1:
							actor.dropItem(actor.getPlayer(), 4040, 1); // Смазка
							break;
						case 2:
							actor.dropItem(actor.getPlayer(), 1894, 1); // Выделанная Кожа
							break;
						case 3:
							actor.dropItem(actor.getPlayer(), 1883, 1); // Стальная Заготовка
							break;
						case 4:
							actor.dropItem(actor.getPlayer(), 4043, 1); // Азоф
							break;
						case 5:
							actor.dropItem(actor.getPlayer(), 1874, 1); // Орихаруконовая Руда
							break;
						case 6:
							actor.dropItem(actor.getPlayer(), 5591, 1); // Зелье CP
							break;
						case 7:
							actor.dropItem(actor.getPlayer(), 1876, 1); // Мифриловая Руда
							break;
						case 8:
							actor.dropItem(actor.getPlayer(), 1882, 1); // Кожа
							break;
					}
				}

				if (dropedCount < 1)
					actor.dropItem(actor.getPlayer(), 37543, 5); // Нектар Снежной Тыквы

			}
			else if (npcId == b_small_adult_gourd) // Завядшая Тыква
			{
				ChatUtils.say(actor, Rnd.get(ON_DEAD_TEXTS_2));

				int dropedCount = 0;
				if (Rnd.chance(5))
				{
					dropedCount++;
					if (Rnd.chance(10))
					{
						if (Rnd.chance(20))
							actor.dropItem(actor.getPlayer(), 49491, 1); // Сундук с Оружием Ранга D
						else if (Rnd.chance(30))
							actor.dropItem(actor.getPlayer(), 49668, 1); // Сундук с Доспехами - Ранг C
						else
							actor.dropItem(actor.getPlayer(), 49669, 1); // Сундук с Аксессуаром - Ранг C
					}
					else if (Rnd.chance(20))
						actor.dropItem(actor.getPlayer(), 9138, 1); // Шапка Деда Мороза
					else
					{
						if (Rnd.chance(5))
							actor.dropItem(actor.getPlayer(), 70000, 1); // Запечатанная Руна - Ур. 1
						else if (Rnd.chance(10))
							actor.dropItem(actor.getPlayer(), 49541, 1); // Сундук с Книгой Заклинаний
						else if (Rnd.chance(20))
							actor.dropItem(actor.getPlayer(), 29008, 1); // Свиток Опыта/SP
						else if (Rnd.chance(30))
							actor.dropItem(actor.getPlayer(), 3959, 1); // Свиток Благ. Воскрешения
						else
							actor.dropItem(actor.getPlayer(), 3958, 1); // Свиток Благ. Телепорта
					}
				}

				if (Rnd.chance(10))
				{
					dropedCount++;
					if (Rnd.chance(30))
						actor.dropItem(actor.getPlayer(), 70353, 1); // Свиток Трансформации в Пирата
					else
						actor.dropItem(actor.getPlayer(), 49080, 1); // Рисовый Пирожок Боевого Духа
				}

				if (Rnd.chance(20))
				{
					dropedCount++;
					int i = Rnd.get(13);
					switch (i)
					{
						case 0:
							actor.dropItem(actor.getPlayer(), 4039, 1); // Клей
							break;
						case 1:
							actor.dropItem(actor.getPlayer(), 4040, 1); // Смазка
							break;
						case 2:
							actor.dropItem(actor.getPlayer(), 1894, 1); // Выделанная Кожа
							break;
						case 3:
							actor.dropItem(actor.getPlayer(), 1883, 1); // Стальная Заготовка
							break;
						case 4:
							actor.dropItem(actor.getPlayer(), 4043, 1); // Азоф
							break;
						case 5:
							actor.dropItem(actor.getPlayer(), 1874, 1); // Орихаруконовая Руда
							break;
						case 6:
							actor.dropItem(actor.getPlayer(), 5591, 1); // Зелье CP
							break;
						case 7:
							actor.dropItem(actor.getPlayer(), 1876, 1); // Мифриловая Руда
							break;
						case 8:
							actor.dropItem(actor.getPlayer(), 1882, 1); // Кожа
							break;
						case 9:
							actor.dropItem(actor.getPlayer(), 1884, 2); // Веревка
							break;
						case 10:
							actor.dropItem(actor.getPlayer(), 1870, 2); // Уголь
							break;
						case 11:
							actor.dropItem(actor.getPlayer(), 1872, 2); // Кость Животного
							break;
						case 12:
							actor.dropItem(actor.getPlayer(), 1865, 2); // Лак
							break;
					}
				}

				if (Rnd.chance(40))
				{
					dropedCount++;
					int i = Rnd.get(7);
					switch (i)
					{
						case 0:
							actor.dropItem(actor.getPlayer(), 49435, 1); // Свиток Духа Берсерка
							break;
						case 1:
							actor.dropItem(actor.getPlayer(), 29690, 1); // Свиток Благословения Тела
							break;
						case 2:
							actor.dropItem(actor.getPlayer(), 29691, 1); // Свиток Благословения Духа
							break;
						case 3:
							actor.dropItem(actor.getPlayer(), 3927, 1); // Свиток Шепота Смерти
							break;
						case 4:
							actor.dropItem(actor.getPlayer(), 3930, 1); // Свиток Ускорения
							break;
						case 5:
							actor.dropItem(actor.getPlayer(), 3928, 1); // Свиток Фокусировки
							break;
						case 6:
							actor.dropItem(actor.getPlayer(), 3932, 1); // Свиток Воодушевления
							break;
					}
				}

				if (dropedCount < 1)
					actor.dropItem(actor.getPlayer(), 37543, 3); // Нектар Снежной Тыквы
			}
			else if (npcId == big_baby_gourd)
				ChatUtils.say(actor, Rnd.get(ON_DEAD_TEXTS_1));
			else if (npcId == g_big_adult_gourd) // Большая Спелая Тыква
			{
				ChatUtils.say(actor, Rnd.get(ON_DEAD_TEXTS_2));

				int dropedCount = 0;
				if (Rnd.chance(5))
				{
					dropedCount++;
					if (Rnd.chance(1))
						actor.dropItem(actor.getPlayer(), 6660, 1); // Кольцо Королевы Муравьев
					else if (Rnd.chance(10))
					{
						if (Rnd.chance(20))
							actor.dropItem(actor.getPlayer(), 49664, 1); // Сундук с Оружием - Ранг B
						else if (Rnd.chance(30))
							actor.dropItem(actor.getPlayer(), 49667, 1); // Сундук с Оружием - Ранг C
						else
							actor.dropItem(actor.getPlayer(), 49665, 1); // Сундук с Доспехами - Ранг B
					}
					else if (Rnd.chance(20))
						actor.dropItem(actor.getPlayer(), 37534, 1); // Костюм Деда Мороза
					else
					{
						if (Rnd.chance(5))
							actor.dropItem(actor.getPlayer(), 6572, 1); // Благословенный Свиток: Модифицировать Доспех
																		// (B)
						else if (Rnd.chance(10))
							actor.dropItem(actor.getPlayer(), 49756, 1); // Лоскут Ткани
						else if (Rnd.chance(20))
							actor.dropItem(actor.getPlayer(), 90020, 1); // Сундук с Лучшей Краской 3-я Профессия
						else if (Rnd.chance(30))
							actor.dropItem(actor.getPlayer(), 29010, 1); // Свиток Опыта/SP - Высокое Качество
						else
							actor.dropItem(actor.getPlayer(), 29008, 1); // Свиток Опыта/SP
					}
				}

				if (Rnd.chance(15))
				{
					dropedCount++;
					int i = Rnd.get(11);
					switch (i)
					{
						case 0:
							actor.dropItem(actor.getPlayer(), 49518, 1); // Особый Плод Пирата
							break;
						case 1:
							actor.dropItem(actor.getPlayer(), 70159, 1); // Зелье Восстановления Маны
							break;
						case 2:
							actor.dropItem(actor.getPlayer(), 29696, 1); // Большое Быстродействующее Зелье Исцеления
							break;
						case 3:
							actor.dropItem(actor.getPlayer(), 29698, 1); // Свиток Эффектов 2-й Профессии
							break;
						case 4:
							actor.dropItem(actor.getPlayer(), 70233, 1); // Клубничный Сок
							break;
						case 5:
							actor.dropItem(actor.getPlayer(), 70234, 1); // Сок Манго
							break;
						case 6:
							actor.dropItem(actor.getPlayer(), 70235, 1); // Вишневый Сок
							break;
						case 7:
							actor.dropItem(actor.getPlayer(), 70309, 400); // Чудесный Благ. Заряд Души: Ранг B
							break;
						case 8:
							actor.dropItem(actor.getPlayer(), 3950, 100); // Благословенный Заряд Духа: Ранг B
							break;
						case 9:
							actor.dropItem(actor.getPlayer(), 3949, 100); // Благословенный Заряд Духа: Ранг С
							break;
						case 10:
							actor.dropItem(actor.getPlayer(), 70308, 400); // Чудесный Благ. Заряд Души: Ранг С
							break;
					}
				}

				if (Rnd.chance(20))
				{
					dropedCount++;
					int i = Rnd.get(6);
					switch (i)
					{
						case 0:
							actor.dropItem(actor.getPlayer(), 1540, 1); // Быстродействующее Зелье Исцеления
							break;
						case 1:
							actor.dropItem(actor.getPlayer(), 1886, 1); // Серебрянная Заготовка
							break;
						case 2:
							actor.dropItem(actor.getPlayer(), 1875, 1); // Камень Чистоты
							break;
						case 3:
							actor.dropItem(actor.getPlayer(), 1888, 1); // Синтетический Кокс
							break;
						case 4:
							actor.dropItem(actor.getPlayer(), 5592, 1); // Большое Зелье CP
							break;
						case 5:
							actor.dropItem(actor.getPlayer(), 4041, 1); // Отвердитель Формы
							break;
					}
				}

				if (dropedCount < 1)
				{
					int i = Rnd.get(3);
					switch (i)
					{
						case 0:
							actor.dropItem(actor.getPlayer(), 70233, 1); // Клубничный Сок
							break;
						case 1:
							actor.dropItem(actor.getPlayer(), 70234, 1); // Сок Манго
							break;
						case 2:
							actor.dropItem(actor.getPlayer(), 70235, 1); // Вишневый Сок
							break;
					}
				}
			}
			else if (npcId == b_big_adult_gourd) // Большая Завядшая Тыква
			{
				ChatUtils.say(actor, Rnd.get(ON_DEAD_TEXTS_2));

				int dropedCount = 0;
				if (Rnd.chance(5))
				{
					dropedCount++;
					if (Rnd.chance(1))
						actor.dropItem(actor.getPlayer(), 6662, 1); // Кольцо Ядра
					else if (Rnd.chance(10))
					{
						if (Rnd.chance(20))
							actor.dropItem(actor.getPlayer(), 49667, 1); // Сундук с Оружием - Ранг C
						else if (Rnd.chance(30))
							actor.dropItem(actor.getPlayer(), 49665, 1); // Сундук с Доспехами - Ранг B
						else
							actor.dropItem(actor.getPlayer(), 49666, 1); // Сундук с Аксессуаром - Ранг B
					}
					else if (Rnd.chance(20))
						actor.dropItem(actor.getPlayer(), 37534, 1); // Костюм Деда Мороза
					else
					{
						if (Rnd.chance(5))
							actor.dropItem(actor.getPlayer(), 6572, 1); // Благословенный Свиток: Модифицировать Доспех
																		// (B)
						else
							actor.dropItem(actor.getPlayer(), 29008, 1); // Свиток Опыта/SP
					}
				}

				if (Rnd.chance(15))
				{
					dropedCount++;
					int i = Rnd.get(6);
					switch (i)
					{
						case 0:
							actor.dropItem(actor.getPlayer(), 49518, 1); // Особый Плод Пирата
							break;
						case 1:
							actor.dropItem(actor.getPlayer(), 70159, 1); // Зелье Восстановления Маны
							break;
						case 2:
							actor.dropItem(actor.getPlayer(), 29698, 1); // Свиток Эффектов 2-й Профессии
							break;
						case 3:
							actor.dropItem(actor.getPlayer(), 1540, 1); // Быстродействующее Зелье Исцеления
							break;
						case 4:
							actor.dropItem(actor.getPlayer(), 3949, 100); // Благословенный Заряд Духа: Ранг C
							break;
						case 5:
							actor.dropItem(actor.getPlayer(), 70308, 400); // Чудесный Благ. Заряд Души: Ранг C
							break;
					}
				}

				if (Rnd.chance(20))
				{
					dropedCount++;
					int i = Rnd.get(10);
					switch (i)
					{
						case 0:
							actor.dropItem(actor.getPlayer(), 1875, 1); // Камень Чистоты
							break;
						case 1:
							actor.dropItem(actor.getPlayer(), 4042, 1); // Энрия
							break;
						case 2:
							actor.dropItem(actor.getPlayer(), 1888, 1); // Синтетический Кокс
							break;
						case 3:
							actor.dropItem(actor.getPlayer(), 5592, 1); // Большое Зелье CP
							break;
						case 4:
							actor.dropItem(actor.getPlayer(), 4039, 1); // Клей
							break;
						case 5:
							actor.dropItem(actor.getPlayer(), 4040, 1); // Смазка
							break;
						case 6:
							actor.dropItem(actor.getPlayer(), 1894, 1); // Выделанная Кожа
							break;
						case 7:
							actor.dropItem(actor.getPlayer(), 1883, 1); // Стальная Заготовка
							break;
						case 8:
							actor.dropItem(actor.getPlayer(), 4043, 1); // Азоф
							break;
						case 9:
							actor.dropItem(actor.getPlayer(), 1874, 1); // Орихаруконовая Руда
							break;
					}
				}

				if (dropedCount < 1)
				{
					int i = Rnd.get(4);
					switch (i)
					{
						case 0:
							actor.dropItem(actor.getPlayer(), 49518, 1); // Особый Плод Пирата
							break;
						case 1:
							actor.dropItem(actor.getPlayer(), 70159, 1); // Зелье Восстановления Маны
							break;
						case 2:
							actor.dropItem(actor.getPlayer(), 29698, 1); // Свиток Эффектов 2-й Профессии
							break;
						case 3:
							actor.dropItem(actor.getPlayer(), 1540, 1); // Быстродействующее Зелье Исцеления
							break;
					}
				}
			}
			else if (npcId == kg_small_adult_gourd) // Королевская Спелая Тыква
			{
				ChatUtils.say(actor, Rnd.get(ON_DEAD_TEXTS_2));

				int dropedCount = 0;
				if (Rnd.chance(5))
				{
					dropedCount++;
					if (Rnd.chance(10))
					{
						if (Rnd.chance(20))
							actor.dropItem(actor.getPlayer(), 49667, 1); // Сундук с Оружием - Ранг C
						else if (Rnd.chance(30))
							actor.dropItem(actor.getPlayer(), 49666, 1); // Сундук с Аксессуаром - Ранг B
						else
							actor.dropItem(actor.getPlayer(), 49668, 1); // Сундук с Доспехами - Ранг C
					}
					else if (Rnd.chance(20))
						actor.dropItem(actor.getPlayer(), 37534, 1); // Костюм Деда Мороза
					else
					{
						if (Rnd.chance(1))
							actor.dropItem(actor.getPlayer(), 70005, 1); // Запечатанная Руна - Ур. 6
						else if (Rnd.chance(5))
							actor.dropItem(actor.getPlayer(), 70000, 1); // Запечатанная Руна - Ур. 1
						else if (Rnd.chance(10))
							actor.dropItem(actor.getPlayer(), 49541, 1); // Сундук с Книгой Заклинаний
						else if (Rnd.chance(20))
							actor.dropItem(actor.getPlayer(), 29008, 1); // Свиток Опыта/SP
						else if (Rnd.chance(30))
							actor.dropItem(actor.getPlayer(), 3959, 1); // Свиток Благ. Воскрешения
						else
							actor.dropItem(actor.getPlayer(), 3958, 1); // Свиток Благ. Телепорта
					}
				}

				if (Rnd.chance(10))
				{
					dropedCount++;
					if (Rnd.chance(20))
					{
						int i = Rnd.get(3);
						switch (i)
						{
							case 0:
								actor.dropItem(actor.getPlayer(), 70354, 1); // Свиток Трансформации в Темного Ассасина
								break;
							case 1:
								actor.dropItem(actor.getPlayer(), 49081, 1); // Рисовый Пирожок Пылающего Боевого Духа
								break;
							case 2:
								actor.dropItem(actor.getPlayer(), 29698, 1); // Свиток Эффектов 2-й Профессии
								break;
						}
					}
					else if (Rnd.chance(40))
					{
						int i = Rnd.get(2);
						switch (i)
						{
							case 0:
								actor.dropItem(actor.getPlayer(), 29697, 1); // Свиток Эффектов 1-й Профессии
								break;
							case 1:
								actor.dropItem(actor.getPlayer(), 49080, 1); // Рисовый Пирожок Боевого Духа
								break;
						}
					}
					else
						actor.dropItem(actor.getPlayer(), 1540, 1); // Быстродействующее Зелье Исцеления
				}

				if (Rnd.chance(20))
				{
					dropedCount++;
					int i = Rnd.get(6);
					switch (i)
					{
						case 0:
							actor.dropItem(actor.getPlayer(), 5591, 4); // Зелье CP
							break;
						case 1:
							actor.dropItem(actor.getPlayer(), 4039, 1); // Клей
							break;
						case 2:
							actor.dropItem(actor.getPlayer(), 4040, 1); // Смазка
							break;
						case 3:
							actor.dropItem(actor.getPlayer(), 1894, 1); // Выделанная Кожа
							break;
						case 4:
							actor.dropItem(actor.getPlayer(), 1883, 1); // Стальная Заготовка
							break;
						case 5:
							actor.dropItem(actor.getPlayer(), 4043, 1); // Азоф
							break;
					}
				}

				if (dropedCount < 1)
					actor.dropItem(actor.getPlayer(), 37543, 10); // Нектар Снежной Тыквы
			}
			else if (npcId == kg_big_adult_gourd) // Королевская Большая Спелая Тыква
			{
				ChatUtils.say(actor, Rnd.get(ON_DEAD_TEXTS_2));

				int dropedCount = 0;
				if (Rnd.chance(5))
				{
					dropedCount++;
					if (Rnd.chance(1))
						actor.dropItem(actor.getPlayer(), 6661, 1); // Серьга Орфен
					else if (Rnd.chance(5))
						actor.dropItem(actor.getPlayer(), 49706, 1); // Сундук с Талисманом Тьмы
					else if (Rnd.chance(10))
					{
						if (Rnd.chance(5))
							actor.dropItem(actor.getPlayer(), 49785, 1); // Сундук с Оружием Ранга А
						else if (Rnd.chance(25))
							actor.dropItem(actor.getPlayer(), 49664, 1); // Сундук с Оружием - Ранг B
						else
							actor.dropItem(actor.getPlayer(), 49667, 1); // Сундук с Оружием - Ранг C
					}
					else
					{
						if (Rnd.chance(5))
							actor.dropItem(actor.getPlayer(), 6569, 1); // Благословенный Свиток: Модифицировать Оружие
																		// (A)
						else if (Rnd.chance(10))
							actor.dropItem(actor.getPlayer(), 49756, 1); // Лоскут Ткани
						else if (Rnd.chance(20))
							actor.dropItem(actor.getPlayer(), 90020, 1); // Сундук с Лучшей Краской 3-я профессия
						else if (Rnd.chance(30))
							actor.dropItem(actor.getPlayer(), 29010, 1); // Свиток Опыта/SP - Высокое Качество
						else
							actor.dropItem(actor.getPlayer(), 29008, 1); // Свиток Опыта/SP
					}
				}

				if (Rnd.chance(15))
				{
					dropedCount++;
					int i = Rnd.get(11);
					switch (i)
					{
						case 0:
							actor.dropItem(actor.getPlayer(), 70310, 200); // Чудесный Благ. Заряд Души: Ранг A
							break;
						case 1:
							actor.dropItem(actor.getPlayer(), 49518, 1); // Особый Плод Пирата
							break;
						case 2:
							actor.dropItem(actor.getPlayer(), 70159, 1); // Зелье Восстановления Маны
							break;
						case 3:
							actor.dropItem(actor.getPlayer(), 3951, 50); // Благословенный Заряд Духа: Ранг A
							break;
						case 4:
							actor.dropItem(actor.getPlayer(), 29698, 1); // Свиток Эффектов 2-й Профессии
							break;
						case 5:
							actor.dropItem(actor.getPlayer(), 70233, 1); // Клубничный Сок
							break;
						case 6:
							actor.dropItem(actor.getPlayer(), 70234, 1); // Сок Манго
							break;
						case 7:
							actor.dropItem(actor.getPlayer(), 70235, 1); // Вишневый Сок
							break;
						case 8:
							actor.dropItem(actor.getPlayer(), 29696, 1); // Большое Быстродействующее Зелье Исцеления
							break;
						case 9:
							actor.dropItem(actor.getPlayer(), 70309, 400); // Чудесный Благ. Заряд Души: Ранг B
							break;
						case 10:
							actor.dropItem(actor.getPlayer(), 3950, 100); // Благословенный Заряд Духа: Ранг B
							break;
					}
				}

				if (Rnd.chance(20))
				{
					dropedCount++;
					int i = Rnd.get(8);
					switch (i)
					{
						case 0:
							actor.dropItem(actor.getPlayer(), 1891, 1); // Заготовка Ремесленника
							break;
						case 1:
							actor.dropItem(actor.getPlayer(), 1892, 1); // Заготовка Кузнеца
							break;
						case 2:
							actor.dropItem(actor.getPlayer(), 4041, 1); // Отвердитель Формы
							break;
						case 3:
							actor.dropItem(actor.getPlayer(), 1893, 1); // Орихарукон
							break;
						case 4:
							actor.dropItem(actor.getPlayer(), 1890, 1); // Мифриловый Сплав
							break;
						case 5:
							actor.dropItem(actor.getPlayer(), 1887, 1); // Очищенный Лак
							break;
						case 6:
							actor.dropItem(actor.getPlayer(), 1886, 1); // Серебряная Заготовка
							break;
						case 7:
							actor.dropItem(actor.getPlayer(), 5592, 2); // Большое Зелье CP
							break;
					}
				}

				if (dropedCount < 1)
				{
					if (Rnd.chance(70))
					{
						int i = Rnd.get(3);
						switch (i)
						{
							case 0:
								actor.dropItem(actor.getPlayer(), 70233, 1); // Клубничный Сок
								break;
							case 1:
								actor.dropItem(actor.getPlayer(), 70234, 1); // Сок Манго
								break;
							case 2:
								actor.dropItem(actor.getPlayer(), 70235, 1); // Вишневый Сок
								break;
						}
					}
					else
					{
						int i = Rnd.get(2);
						switch (i)
						{
							case 0:
								actor.dropItem(actor.getPlayer(), 49518, 1); // Особый Плод Пирата
								break;
							case 1:
								actor.dropItem(actor.getPlayer(), 70159, 1); // Зелье Восстановления Маны
								break;
						}
					}
				}
			}
			killer.sendPacket(new PlaySoundPacket("ItemSound.quest_middle"));
		}
	}

	@Override
	protected boolean randomWalk()
	{
		return false;
	}

	private static void spawnNextNpc(int npcId, Location loc, Reflection reflection, Player owner)
	{
		if (owner == null)
			return;

		NpcInstance npc = NpcUtils.spawnSingle(npcId, loc, reflection);
		npc.setOwner(owner);
		npc.onAction(owner, false);
	}
}