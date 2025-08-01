package l2s.gameserver.handler.admincommands;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import l2s.commons.data.xml.AbstractHolder;
import l2s.gameserver.handler.admincommands.impl.AdminAdmin;
import l2s.gameserver.handler.admincommands.impl.AdminAnnouncements;
import l2s.gameserver.handler.admincommands.impl.AdminAttributes;
import l2s.gameserver.handler.admincommands.impl.AdminCamera;
import l2s.gameserver.handler.admincommands.impl.AdminCancel;
import l2s.gameserver.handler.admincommands.impl.AdminChangeAccessLevel;
import l2s.gameserver.handler.admincommands.impl.AdminClanHall;
import l2s.gameserver.handler.admincommands.impl.AdminCreateItem;
import l2s.gameserver.handler.admincommands.impl.AdminDebug;
import l2s.gameserver.handler.admincommands.impl.AdminDelete;
import l2s.gameserver.handler.admincommands.impl.AdminDisconnect;
import l2s.gameserver.handler.admincommands.impl.AdminDoorControl;
import l2s.gameserver.handler.admincommands.impl.AdminEditChar;
import l2s.gameserver.handler.admincommands.impl.AdminEffects;
import l2s.gameserver.handler.admincommands.impl.AdminElemental;
import l2s.gameserver.handler.admincommands.impl.AdminEnchant;
import l2s.gameserver.handler.admincommands.impl.AdminGeodata;
import l2s.gameserver.handler.admincommands.impl.AdminGiveAll;
import l2s.gameserver.handler.admincommands.impl.AdminGm;
import l2s.gameserver.handler.admincommands.impl.AdminGmChat;
import l2s.gameserver.handler.admincommands.impl.AdminHeal;
import l2s.gameserver.handler.admincommands.impl.AdminHelpPage;
import l2s.gameserver.handler.admincommands.impl.AdminIP;
import l2s.gameserver.handler.admincommands.impl.AdminInstance;
import l2s.gameserver.handler.admincommands.impl.AdminKill;
import l2s.gameserver.handler.admincommands.impl.AdminLevel;
import l2s.gameserver.handler.admincommands.impl.AdminMammon;
import l2s.gameserver.handler.admincommands.impl.AdminMenu;
import l2s.gameserver.handler.admincommands.impl.AdminMonsterRace;
import l2s.gameserver.handler.admincommands.impl.AdminNochannel;
import l2s.gameserver.handler.admincommands.impl.AdminOldBan;
import l2s.gameserver.handler.admincommands.impl.AdminOlympiad;
import l2s.gameserver.handler.admincommands.impl.AdminPForge;
import l2s.gameserver.handler.admincommands.impl.AdminPetition;
import l2s.gameserver.handler.admincommands.impl.AdminPledge;
import l2s.gameserver.handler.admincommands.impl.AdminPolymorph;
import l2s.gameserver.handler.admincommands.impl.AdminQuests;
import l2s.gameserver.handler.admincommands.impl.AdminReload;
import l2s.gameserver.handler.admincommands.impl.AdminRepairChar;
import l2s.gameserver.handler.admincommands.impl.AdminRes;
import l2s.gameserver.handler.admincommands.impl.AdminRide;
import l2s.gameserver.handler.admincommands.impl.AdminScripts;
import l2s.gameserver.handler.admincommands.impl.AdminServer;
import l2s.gameserver.handler.admincommands.impl.AdminShop;
import l2s.gameserver.handler.admincommands.impl.AdminShutdown;
import l2s.gameserver.handler.admincommands.impl.AdminSkill;
import l2s.gameserver.handler.admincommands.impl.AdminSpawn;
import l2s.gameserver.handler.admincommands.impl.AdminSpawnEditor;
import l2s.gameserver.handler.admincommands.impl.AdminTarget;
import l2s.gameserver.handler.admincommands.impl.AdminTeleport;
import l2s.gameserver.handler.admincommands.impl.AdminZone;
import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.components.CustomMessage;
import l2s.gameserver.utils.Log;

public class AdminCommandHandler extends AbstractHolder
{
	private static final AdminCommandHandler _instance = new AdminCommandHandler();

	public static AdminCommandHandler getInstance()
	{
		return _instance;
	}

	private Map<String, IAdminCommandHandler> _datatable = new HashMap<String, IAdminCommandHandler>();

	private AdminCommandHandler()
	{
		registerAdminCommandHandler(new AdminAdmin());
		registerAdminCommandHandler(new AdminAnnouncements());
		registerAdminCommandHandler(new AdminAttributes());
		registerAdminCommandHandler(new AdminOldBan());
		registerAdminCommandHandler(new AdminCamera());
		registerAdminCommandHandler(new AdminCancel());
		registerAdminCommandHandler(new AdminChangeAccessLevel());
		registerAdminCommandHandler(new AdminClanHall());
		registerAdminCommandHandler(new AdminCreateItem());
		registerAdminCommandHandler(new AdminDebug());
		registerAdminCommandHandler(new AdminDelete());
		registerAdminCommandHandler(new AdminDisconnect());
		registerAdminCommandHandler(new AdminDoorControl());
		registerAdminCommandHandler(new AdminEditChar());
		registerAdminCommandHandler(new AdminEffects());
		registerAdminCommandHandler(new AdminElemental());
		registerAdminCommandHandler(new AdminEnchant());
		registerAdminCommandHandler(new AdminGeodata());
		registerAdminCommandHandler(new AdminGiveAll());
		registerAdminCommandHandler(new AdminGm());
		registerAdminCommandHandler(new AdminGmChat());
		registerAdminCommandHandler(new AdminHeal());
		registerAdminCommandHandler(new AdminHelpPage());
		registerAdminCommandHandler(new AdminInstance());
		registerAdminCommandHandler(new AdminIP());
		registerAdminCommandHandler(new AdminLevel());
		registerAdminCommandHandler(new AdminMammon());
		registerAdminCommandHandler(new AdminMenu());
		registerAdminCommandHandler(new AdminMonsterRace());
		registerAdminCommandHandler(new AdminNochannel());
		registerAdminCommandHandler(new AdminOlympiad());
		registerAdminCommandHandler(new AdminPetition());
		registerAdminCommandHandler(new AdminPForge());
		registerAdminCommandHandler(new AdminPledge());
		registerAdminCommandHandler(new AdminPolymorph());
		registerAdminCommandHandler(new AdminQuests());
		registerAdminCommandHandler(new AdminReload());
		registerAdminCommandHandler(new AdminRepairChar());
		registerAdminCommandHandler(new AdminRes());
		registerAdminCommandHandler(new AdminRide());
		registerAdminCommandHandler(new AdminServer());
		registerAdminCommandHandler(new AdminShop());
		registerAdminCommandHandler(new AdminShutdown());
		registerAdminCommandHandler(new AdminSkill());
		registerAdminCommandHandler(new AdminScripts());
		registerAdminCommandHandler(new AdminSpawn());
		registerAdminCommandHandler(new AdminTarget());
		registerAdminCommandHandler(new AdminTeleport());
		registerAdminCommandHandler(new AdminZone());
		registerAdminCommandHandler(new AdminKill());
		registerAdminCommandHandler(new AdminSpawnEditor());
	}

	public void registerAdminCommandHandler(IAdminCommandHandler handler)
	{
		for(Enum<?> e : handler.getAdminCommandEnum())
			_datatable.put(e.toString().toLowerCase(), handler);
	}

	public IAdminCommandHandler getAdminCommandHandler(String adminCommand)
	{
		String command = adminCommand;
		if(adminCommand.indexOf(" ") != -1)
			command = adminCommand.substring(0, adminCommand.indexOf(" "));
		return _datatable.get(command);
	}

	public void useAdminCommandHandler(Player activeChar, String adminCommand)
	{
		if(!(activeChar.isGM() || activeChar.getPlayerAccess().CanUseGMCommand))
		{
			activeChar.sendMessage(new CustomMessage("l2s.gameserver.network.l2.c2s.SendBypassBuildCmd.NoCommandOrAccess").addString(adminCommand));
			return;
		}

		String[] wordList = adminCommand.split(" ");
		IAdminCommandHandler handler = _datatable.get(wordList[0]);
		if(handler != null)
		{
			try
			{
				for(Enum<?> e : handler.getAdminCommandEnum())
				{
					if(e.toString().equalsIgnoreCase(wordList[0]))
					{
						boolean success = handler.useAdminCommand(e, wordList, adminCommand, activeChar);
						Log.LogCommand(activeChar, activeChar.getTarget(), adminCommand, success);
						return;
					}
				}
			}
			catch(Exception e)
			{
				error("", e);
			}

			activeChar.sendMessage("Cannot find handler for command: " + adminCommand);
		}
	}

	@Override
	public void process()
	{

	}

	@Override
	public int size()
	{
		return _datatable.size();
	}

	@Override
	public void clear()
	{
		_datatable.clear();
	}

	/**
	 * Получение списка зарегистрированных админ команд
	 * 
	 * @return список команд
	 */
	public Set<String> getAllCommands()
	{
		return _datatable.keySet();
	}
}