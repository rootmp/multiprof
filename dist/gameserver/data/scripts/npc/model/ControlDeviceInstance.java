package npc.model;

import java.util.concurrent.ScheduledFuture;

import l2s.commons.collections.MultiValueSet;
import l2s.gameserver.ThreadPoolManager;
import l2s.gameserver.model.Creature;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.network.l2.components.HtmlMessage;
import l2s.gameserver.skills.SkillEntry;
import l2s.gameserver.skills.enums.SkillEntryType;
import l2s.gameserver.templates.npc.NpcTemplate;

/**
 * @author nexvill
 */
public class ControlDeviceInstance extends NpcInstance
{
	// Skills
	private static final SkillEntry CLAN_STRONGHOLD_EFFECT = SkillEntry.makeSkillEntry(SkillEntryType.NONE, 48078, 1);
	// Npcs
	private static final int CRUMA_TOWER_CLAN_STRONGHOLD_DEVICE = 34156;
	// Tasks
	private ScheduledFuture<?> _crumaTowerTask = null;

	public ControlDeviceInstance(int objectId, NpcTemplate template, MultiValueSet<String> set)
	{
		super(objectId, template, set);
	}

	@Override
	public void showMainChatWindow(Player player, boolean firstTalk, Object... replace)
	{
		if (getNpcId() == CRUMA_TOWER_CLAN_STRONGHOLD_DEVICE)
		{
			if (getClan() == null)
			{
				showChatWindow(player, "default/clan_stronghold_device001.htm", firstTalk);
			}
			else
			{
				final HtmlMessage html = new HtmlMessage(this);
				html.setFile("default/clan_stronghold_device002.htm");
				html.replace("%owner%", String.valueOf(getClan().getName()));
				player.sendPacket(html);
			}
		}
	}

	@Override
	public void onMenuSelect(Player player, int ask, long reply, int state)
	{
		if (getNpcId() == 34156)
		{
			if (ask == -34156)
			{
				if (reply == 404)
				{
					setClan(player.getClan());
					setTitle(getClan().getName());
					broadcastCharInfo();
					ThreadPoolManager.getInstance().schedule(() ->
					{
						setClan(null);
						setTitle("");
						stopCrumaTowerTask();
						broadcastCharInfo();
					}, 14_400_000L);
					_crumaTowerTask = ThreadPoolManager.getInstance().scheduleAtFixedRate(() ->
					{
						for (final Creature creature : getAroundCharacters(900, 100))
						{
							if (creature.isPlayer())
							{
								final Player plr = creature.getPlayer();
								CLAN_STRONGHOLD_EFFECT.getEffects(plr, plr);
							}
						}
					}, 1000L, 10000L);
				}
			}
		}
	}

	@Override
	protected void onSpawn()
	{
		super.onSpawn();
	}

	private void stopCrumaTowerTask()
	{
		if (_crumaTowerTask != null)
		{
			_crumaTowerTask.cancel(true);
			_crumaTowerTask = null;
		}
	}
}