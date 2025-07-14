package l2s.gameserver.data.xml.parser;

import java.io.File;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.dom4j.Element;

import l2s.commons.collections.MultiValueSet;
import l2s.commons.data.xml.AbstractParser;
import l2s.gameserver.Config;
import l2s.gameserver.data.xml.holder.EventHolder;
import l2s.gameserver.geometry.Location;
import l2s.gameserver.model.entity.events.Event;
import l2s.gameserver.model.entity.events.EventAction;
import l2s.gameserver.model.entity.events.actions.ActiveDeactiveAction;
import l2s.gameserver.model.entity.events.actions.AnnounceAction;
import l2s.gameserver.model.entity.events.actions.GiveItemAction;
import l2s.gameserver.model.entity.events.actions.GlobalRemoveAbnormalsAction;
import l2s.gameserver.model.entity.events.actions.GlobalRemoveItemsAction;
import l2s.gameserver.model.entity.events.actions.GlobalRemoveVariablesAction;
import l2s.gameserver.model.entity.events.actions.GlobalRewardListAction;
import l2s.gameserver.model.entity.events.actions.IfElseAction;
import l2s.gameserver.model.entity.events.actions.InitAction;
import l2s.gameserver.model.entity.events.actions.NpcSayAction;
import l2s.gameserver.model.entity.events.actions.OpenCloseAction;
import l2s.gameserver.model.entity.events.actions.PlaySoundAction;
import l2s.gameserver.model.entity.events.actions.RefreshAction;
import l2s.gameserver.model.entity.events.actions.RespawnAction;
import l2s.gameserver.model.entity.events.actions.SayAction;
import l2s.gameserver.model.entity.events.actions.ScheduleCancelAction;
import l2s.gameserver.model.entity.events.actions.SpawnDespawnAction;
import l2s.gameserver.model.entity.events.actions.StartStopAction;
import l2s.gameserver.model.entity.events.actions.TeleportPlayersAction;
import l2s.gameserver.model.entity.events.objects.BoatPoint;
import l2s.gameserver.model.entity.events.objects.CTBTeamObject;
import l2s.gameserver.model.entity.events.objects.CastleDamageZoneObject;
import l2s.gameserver.model.entity.events.objects.DoorObject;
import l2s.gameserver.model.entity.events.objects.FenceObject;
import l2s.gameserver.model.entity.events.objects.FortressCombatFlagObject;
import l2s.gameserver.model.entity.events.objects.ItemObject;
import l2s.gameserver.model.entity.events.objects.RewardObject;
import l2s.gameserver.model.entity.events.objects.SiegeToggleNpcObject;
import l2s.gameserver.model.entity.events.objects.SpawnExObject;
import l2s.gameserver.model.entity.events.objects.SpawnObject;
import l2s.gameserver.model.entity.events.objects.SpawnSimpleObject;
import l2s.gameserver.model.entity.events.objects.StaticObjectObject;
import l2s.gameserver.model.entity.events.objects.TaskObject;
import l2s.gameserver.model.entity.events.objects.ZoneObject;
import l2s.gameserver.model.reward.RewardList;
import l2s.gameserver.model.reward.RewardType;
import l2s.gameserver.network.l2.components.ChatType;
import l2s.gameserver.network.l2.components.NpcString;
import l2s.gameserver.network.l2.components.SysString;
import l2s.gameserver.network.l2.components.SystemMsg;
import l2s.gameserver.network.l2.s2c.PlaySoundPacket;
import l2s.gameserver.scripts.Scripts;
import l2s.gameserver.skills.enums.AbnormalEffect;
import l2s.gameserver.utils.Util;

/**
 * @author VISTALL
 * @date 12:56/10.12.2010
 */
public final class EventParser extends AbstractParser<EventHolder>
{
	private static final EventParser _instance = new EventParser();

	public static EventParser getInstance()
	{
		return _instance;
	}

	protected EventParser()
	{
		super(EventHolder.getInstance());
	}

	@Override
	public File getXMLPath()
	{
		return new File(Config.DATAPACK_ROOT, "data/parser/events/");
	}

	@Override
	public File getCustomXMLPath()
	{
		return new File(Config.DATAPACK_ROOT, "data/parser/events/custom/");
	}

	@Override
	public String getDTDFileName()
	{
		return "events.dtd";
	}

	@Override
	@SuppressWarnings("unchecked")
	protected void readData(Element rootElement) throws Exception
	{
		for(Iterator<Element> iterator = rootElement.elementIterator("event"); iterator.hasNext();)
		{
			Element eventElement = iterator.next();
			int id = Integer.parseInt(eventElement.attributeValue("id"));
			String name = eventElement.attributeValue("name");
			String impl = eventElement.attributeValue("impl");
			Class<Event> eventClass = null;
			try
			{
				eventClass = (Class<Event>) Class.forName("l2s.gameserver.model.entity.events.impl." + impl + "Event");
			}
			catch(ClassNotFoundException e)
			{
				eventClass = (Class<Event>) Scripts.getInstance().getClasses().get("events." + impl + "Event");
			}

			if(eventClass == null)
			{
				info("Not found impl class: " + impl + "; File: " + getCurrentFileName());
				continue;
			}

			Constructor<Event> constructor = eventClass.getConstructor(MultiValueSet.class);

			MultiValueSet<String> set = new MultiValueSet<String>();
			set.set("id", id);
			set.set("name", name);

			for(Iterator<Element> parameterIterator = eventElement.elementIterator("parameter"); parameterIterator.hasNext();)
			{
				Element parameterElement = parameterIterator.next();
				set.set(parameterElement.attributeValue("name"), parameterElement.attributeValue("value"));
			}

			Event event = constructor.newInstance(set);

			event.addOnStartActions(parseActions(eventElement.element("on_start"), null));
			event.addOnStopActions(parseActions(eventElement.element("on_stop"), null));
			event.addOnInitActions(parseActions(eventElement.element("on_init"), null));

			Element onTime = eventElement.element("on_time");
			if(onTime != null)
			{
				for(Iterator<Element> onTimeIterator = onTime.elementIterator("on"); onTimeIterator.hasNext();)
				{
					Element on = onTimeIterator.next();
					int time = 0;
					if(Util.isNumber(on.attributeValue("time")))
						time = Integer.parseInt(on.attributeValue("time"));
					else
						time = set.getInteger(on.attributeValue("time"));

					if(on.attributeValue("time_unit") != null)
					{
						TimeUnit timeUnit = TimeUnit.valueOf(on.attributeValue("time_unit").toUpperCase());
						time = (int) TimeUnit.SECONDS.convert(time, timeUnit);
					}

					event.addOnTimeActions(time, parseActions(on, String.valueOf(time)));
				}
			}

			Element onAct = eventElement.element("on_act");
			if(onAct != null)
			{
				for(Iterator<Element> onActIterator = onAct.elementIterator("on"); onActIterator.hasNext();)
				{
					Element on = onActIterator.next();
					String act = on.attributeValue("act");
					event.addOnActActions(act, parseActions(on, act));
				}
			}

			for(Iterator<Element> objectIterator = eventElement.elementIterator("objects"); objectIterator.hasNext();)
			{
				Element objectElement = objectIterator.next();
				String objectsName = objectElement.attributeValue("name");
				List<Object> objects = parseObjects(objectElement, id + ":" + name);

				event.addObjects(objectsName, objects);
			}

			getHolder().addEvent(event);
		}
	}

	private List<Object> parseObjects(Element element, String str)
	{
		if(element == null)
			return Collections.emptyList();

		List<Object> objects = new ArrayList<Object>(2);
		for(Iterator<Element> objectIterator = element.elementIterator(); objectIterator.hasNext();)
		{
			Element objectElement = objectIterator.next();
			final String nodeName = objectElement.getName();
			if(nodeName.equalsIgnoreCase("boat_point"))
				objects.add(BoatPoint.parse(objectElement));
			else if(nodeName.equalsIgnoreCase("point"))
				objects.add(Location.parse(objectElement));
			else if(nodeName.equalsIgnoreCase("spawn"))
				objects.add(new SpawnObject(objectElement.attributeValue("name")));
			else if(nodeName.equalsIgnoreCase("spawn_ex"))
				objects.add(new SpawnExObject(objectElement.attributeValue("name")));
			else if(nodeName.equalsIgnoreCase("door"))
				objects.add(new DoorObject(Integer.parseInt(objectElement.attributeValue("id"))));
			else if(nodeName.equalsIgnoreCase("static_object"))
				objects.add(new StaticObjectObject(Integer.parseInt(objectElement.attributeValue("id"))));
			else if(nodeName.equalsIgnoreCase("spawn_npc"))
			{
				int id = Integer.parseInt(objectElement.attributeValue("id"));
				int x = Integer.parseInt(objectElement.attributeValue("x"));
				int y = Integer.parseInt(objectElement.attributeValue("y"));
				int z = Integer.parseInt(objectElement.attributeValue("z"));
				objects.add(new SpawnSimpleObject(id, new Location(x, y, z)));
			}
			else if(nodeName.equalsIgnoreCase("combat_flag"))
			{
				int x = Integer.parseInt(objectElement.attributeValue("x"));
				int y = Integer.parseInt(objectElement.attributeValue("y"));
				int z = Integer.parseInt(objectElement.attributeValue("z"));
				objects.add(new FortressCombatFlagObject(new Location(x, y, z)));
			}
			else if(nodeName.equalsIgnoreCase("siege_toggle_npc"))
			{
				int id = Integer.parseInt(objectElement.attributeValue("id"));
				int fakeId = Integer.parseInt(objectElement.attributeValue("fake_id"));
				int x = Integer.parseInt(objectElement.attributeValue("x"));
				int y = Integer.parseInt(objectElement.attributeValue("y"));
				int z = Integer.parseInt(objectElement.attributeValue("z"));
				int hp = Integer.parseInt(objectElement.attributeValue("hp"));
				Set<String> set = Collections.emptySet();
				for(Iterator<Element> oIterator = objectElement.elementIterator(); oIterator.hasNext();)
				{
					Element sub = oIterator.next();
					if(set.isEmpty())
						set = new HashSet<String>();
					set.add(sub.attributeValue("name"));
				}
				objects.add(new SiegeToggleNpcObject(id, fakeId, new Location(x, y, z), hp, set));
			}
			else if(nodeName.equalsIgnoreCase("reward"))
			{
				int item_id = Integer.parseInt(objectElement.attributeValue("item_id"));
				long min = Long.parseLong(objectElement.attributeValue("min"));
				long max = objectElement.attributeValue("max") == null ? min : Long.parseLong(objectElement.attributeValue("max"));
				double chance = objectElement.attributeValue("chance") == null ? 100. : Double.parseDouble(objectElement.attributeValue("chance"));
				objects.add(new RewardObject(item_id, min, max, chance));
			}
			else if(nodeName.equalsIgnoreCase("item"))
			{
				int item_id = Integer.parseInt(objectElement.attributeValue("id"));
				long item_count = objectElement.attributeValue("count") == null ? -1 : Long.parseLong(objectElement.attributeValue("count"));
				objects.add(new ItemObject(item_id, item_count));
			}
			else if(nodeName.equalsIgnoreCase("castle_zone"))
			{
				long price = Long.parseLong(objectElement.attributeValue("price"));
				objects.add(new CastleDamageZoneObject(objectElement.attributeValue("name"), price));
			}
			else if(nodeName.equalsIgnoreCase("zone"))
			{
				objects.add(new ZoneObject(objectElement.attributeValue("name")));
			}
			else if(nodeName.equalsIgnoreCase("ctb_team"))
			{
				int mobId = Integer.parseInt(objectElement.attributeValue("mob_id"));
				int flagId = Integer.parseInt(objectElement.attributeValue("id"));
				Location loc = Location.parse(objectElement);

				objects.add(new CTBTeamObject(mobId, flagId, loc));
			}
			else if(nodeName.equalsIgnoreCase("rewardlist"))
				objects.add(RewardList.parseRewardList(getLogger(), objectElement, RewardType.valueOf(objectElement.attributeValue("type")), str));
			else if(nodeName.equalsIgnoreCase("abnormal"))
				objects.add(AbnormalEffect.valueOf(objectElement.attributeValue("name")));
			else if(nodeName.equalsIgnoreCase("task"))
			{
				boolean fixed_rate = objectElement.attributeValue("fixed_rate")
						== null ? false : Boolean.parseBoolean(objectElement.attributeValue("fixed_rate"));
				int delay = Integer.parseInt(objectElement.attributeValue("delay"));
				int initial_delay = objectElement.attributeValue("initial_delay")
						== null ? delay : Integer.parseInt(objectElement.attributeValue("initial_delay"));
				TaskObject task = new TaskObject(fixed_rate, initial_delay, delay);
				task.setActions(parseActions(objectElement, null));
				objects.add(task);
			}
			else if(nodeName.equalsIgnoreCase("fence"))
			{
				int x = Integer.parseInt(objectElement.attributeValue("x"));
				int y = Integer.parseInt(objectElement.attributeValue("y"));
				int z = Integer.parseInt(objectElement.attributeValue("z"));
				int width = Integer.parseInt(objectElement.attributeValue("width"));
				int length = Integer.parseInt(objectElement.attributeValue("length"));
				int height = objectElement.attributeValue("height") == null ? 1 : Integer.parseInt(objectElement.attributeValue("height"));
				objects.add(new FenceObject(new Location(x, y, z), width, length, height));
			}
		}

		return objects;
	}

	private List<EventAction> parseActions(Element element, String param)
	{
		if(element == null)
			return Collections.emptyList();

		IfElseAction lastIf = null;
		List<EventAction> actions = new ArrayList<EventAction>(0);
		for(Iterator<Element> iterator = element.elementIterator(); iterator.hasNext();)
		{
			Element actionElement = iterator.next();
			if(actionElement.getName().equalsIgnoreCase("start"))
			{
				String name = actionElement.attributeValue("name");
				StartStopAction startStopAction = new StartStopAction(name, true);
				actions.add(startStopAction);
			}
			else if(actionElement.getName().equalsIgnoreCase("stop"))
			{
				String name = actionElement.attributeValue("name");
				StartStopAction startStopAction = new StartStopAction(name, false);
				actions.add(startStopAction);
			}
			else if(actionElement.getName().equalsIgnoreCase("spawn"))
			{
				String name = actionElement.attributeValue("name");
				SpawnDespawnAction spawnDespawnAction = new SpawnDespawnAction(name, true);
				actions.add(spawnDespawnAction);
			}
			else if(actionElement.getName().equalsIgnoreCase("despawn"))
			{
				String name = actionElement.attributeValue("name");
				SpawnDespawnAction spawnDespawnAction = new SpawnDespawnAction(name, false);
				actions.add(spawnDespawnAction);
			}
			else if(actionElement.getName().equalsIgnoreCase("respawn"))
			{
				String name = actionElement.attributeValue("name");
				RespawnAction respawnAction = new RespawnAction(name);
				actions.add(respawnAction);
			}
			else if(actionElement.getName().equalsIgnoreCase("open"))
			{
				String name = actionElement.attributeValue("name");
				OpenCloseAction a = new OpenCloseAction(true, name);
				actions.add(a);
			}
			else if(actionElement.getName().equalsIgnoreCase("close"))
			{
				String name = actionElement.attributeValue("name");
				OpenCloseAction a = new OpenCloseAction(false, name);
				actions.add(a);
			}
			else if(actionElement.getName().equalsIgnoreCase("active"))
			{
				String name = actionElement.attributeValue("name");
				ActiveDeactiveAction a = new ActiveDeactiveAction(true, name);
				actions.add(a);
			}
			else if(actionElement.getName().equalsIgnoreCase("deactive"))
			{
				String name = actionElement.attributeValue("name");
				ActiveDeactiveAction a = new ActiveDeactiveAction(false, name);
				actions.add(a);
			}
			else if(actionElement.getName().equalsIgnoreCase("refresh"))
			{
				String name = actionElement.attributeValue("name");
				RefreshAction a = new RefreshAction(name);
				actions.add(a);
			}
			else if(actionElement.getName().equalsIgnoreCase("init"))
			{
				String name = actionElement.attributeValue("name");
				InitAction a = new InitAction(name);
				actions.add(a);
			}
			else if(actionElement.getName().equalsIgnoreCase("global_add_reward"))
			{
				String name = actionElement.attributeValue("name");
				int minLevel = actionElement.attributeValue("min_level") == null ? 1 : Integer.parseInt(actionElement.attributeValue("min_level"));
				int maxLevel = actionElement.attributeValue("max_level")
						== null ? Integer.MAX_VALUE : Integer.parseInt(actionElement.attributeValue("max_level"));
				actions.add(new GlobalRewardListAction(true, name, minLevel, maxLevel));
			}
			else if(actionElement.getName().equalsIgnoreCase("global_remove_reward"))
			{
				String name = actionElement.attributeValue("name");
				int minLevel = actionElement.attributeValue("min_level") == null ? 1 : Integer.parseInt(actionElement.attributeValue("min_level"));
				int maxLevel = actionElement.attributeValue("max_level")
						== null ? Integer.MAX_VALUE : Integer.parseInt(actionElement.attributeValue("max_level"));
				actions.add(new GlobalRewardListAction(false, name, minLevel, maxLevel));
			}
			else if(actionElement.getName().equalsIgnoreCase("global_remove_items"))
			{
				String name = actionElement.attributeValue("name");
				actions.add(new GlobalRemoveItemsAction(name));
			}
			else if(actionElement.getName().equalsIgnoreCase("global_remove_abnormals"))
			{
				int skillId = Integer.parseInt(actionElement.attributeValue("skill_id"));
				actions.add(new GlobalRemoveAbnormalsAction(skillId));
			}
			else if(actionElement.getName().equalsIgnoreCase("global_remove_variables"))
			{
				String name = actionElement.attributeValue("name");
				GlobalRemoveVariablesAction.VariableType type = GlobalRemoveVariablesAction.VariableType.valueOf(actionElement.attributeValue("type").toUpperCase());
				actions.add(new GlobalRemoveVariablesAction(name, type));
			}
			else if(actionElement.getName().equalsIgnoreCase("npc_say"))
			{
				int npc = Integer.parseInt(actionElement.attributeValue("npc"));
				ChatType chat = ChatType.valueOf(actionElement.attributeValue("chat"));
				int range = Integer.parseInt(actionElement.attributeValue("range"));
				NpcString string = NpcString.valueOf(actionElement.attributeValue("text"));
				NpcSayAction action = new NpcSayAction(npc, range, chat, string);
				actions.add(action);
			}
			else if(actionElement.getName().equalsIgnoreCase("play_sound"))
			{
				int range = Integer.parseInt(actionElement.attributeValue("range"));
				String sound = actionElement.attributeValue("sound");
				PlaySoundPacket.Type type = PlaySoundPacket.Type.valueOf(actionElement.attributeValue("type"));

				PlaySoundAction action = new PlaySoundAction(range, sound, type);
				actions.add(action);
			}
			else if(actionElement.getName().equalsIgnoreCase("give_item"))
			{
				int itemId = Integer.parseInt(actionElement.attributeValue("id"));
				long count = Integer.parseInt(actionElement.attributeValue("count"));

				GiveItemAction action = new GiveItemAction(itemId, count);
				actions.add(action);
			}
			else if(actionElement.getName().equalsIgnoreCase("announce"))
			{
				int id = Integer.parseInt(actionElement.attributeValue("id"));
				String value = actionElement.attributeValue("value");
				int time = Integer.MAX_VALUE;
				if(param != null)
				{
					if(value == null)
						value = param;
					if(Util.isNumber(param))
						time = Integer.parseInt(param);
				}
				actions.add(new AnnounceAction(id, value, time));
			}
			else if(actionElement.getName().equalsIgnoreCase("if"))
			{
				String name = actionElement.attributeValue("name");
				IfElseAction action = new IfElseAction(name, false);

				action.setIfList(parseActions(actionElement, param));
				actions.add(action);

				lastIf = action;
			}
			else if(actionElement.getName().equalsIgnoreCase("ifnot"))
			{
				String name = actionElement.attributeValue("name");
				IfElseAction action = new IfElseAction(name, true);

				action.setIfList(parseActions(actionElement, param));
				actions.add(action);

				lastIf = action;
			}
			else if(actionElement.getName().equalsIgnoreCase("else"))
			{
				if(lastIf == null)
					info("Not find <if> for <else> tag");
				else
					lastIf.setElseList(parseActions(actionElement, param));
			}
			else if(actionElement.getName().equalsIgnoreCase("say"))
			{
				ChatType chat = ChatType.valueOf(actionElement.attributeValue("chat"));
				int range = Integer.parseInt(actionElement.attributeValue("range"));

				String how = actionElement.attributeValue("how");
				String text = actionElement.attributeValue("text");

				SysString sysString = SysString.valueOf2(how);

				SayAction sayAction = null;
				if(sysString != null)
					sayAction = new SayAction(range, chat, sysString, SystemMsg.valueOf(text));
				else
					sayAction = new SayAction(range, chat, how, NpcString.valueOf(text));

				actions.add(sayAction);
			}
			else if(actionElement.getName().equalsIgnoreCase("teleport_players"))
			{
				String name = actionElement.attributeValue("id");
				TeleportPlayersAction a = new TeleportPlayersAction(name);
				actions.add(a);
			}
			else if(actionElement.getName().equalsIgnoreCase("schedule_task"))
			{
				String name = actionElement.attributeValue("name");
				ScheduleCancelAction scheduleCancelAction = new ScheduleCancelAction(name, true);
				actions.add(scheduleCancelAction);
			}
			else if(actionElement.getName().equalsIgnoreCase("cancel_task"))
			{
				String name = actionElement.attributeValue("name");
				ScheduleCancelAction scheduleCancelAction = new ScheduleCancelAction(name, false);
				actions.add(scheduleCancelAction);
			}
		}

		return actions.isEmpty() ? Collections.<EventAction> emptyList() : actions;
	}
}
