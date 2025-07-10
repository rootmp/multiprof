package l2s.gameserver.handler.admincommands.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import l2s.gameserver.data.xml.holder.SpawnHolder;
import l2s.gameserver.handler.admincommands.IAdminCommandHandler;
import l2s.gameserver.model.Creature;
import l2s.gameserver.model.GameObject;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.World;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.network.l2.s2c.ShowBoardPacket;
import l2s.gameserver.templates.spawn.SpawnTemplate;

/**
 * Written by Berezkin Nikolay, on 17.02.2021
 */
public class AdminSpawnEditor implements IAdminCommandHandler
{

	@Override
	public Enum<?>[] getAdminCommandEnum()
	{
		return commands.values();
	}

	private enum commands
	{
		admin_spawnEditor,
		admin_spawnEditor_groupInfo,
		admin_spawnEditor_spawnInfo,
		admin_spawnEditor_markAsEdited
	}

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private final File outDirectory = new File("./spawn_out/");
	private final int elementsPerPageAroundChar = 15;
	private final Map<Player, List<SpawnTemplate>> editedTemplates = new HashMap<>();
	private final String line = "<table border=0 cellspacing=0 cellpadding=0>\n" + "    <tr>\n" + "        <td width=610><img src=\"l2ui.squaregray\" width=\"600\" height=\"1\"></td>\n" + "    </tr>\n" + "</table><br>";

	private String formattedSpawnTemplateList(List<SpawnTemplate> templates, int p, Player player)
	{
		StringBuilder sb = new StringBuilder();
		sb.append("<center>");
		sb.append("<table height=\"100\" cellpadding=0>");
		sb.append("<tr><td>Note: blablablablablabla</td></tr>");
		sb.append("</table>");
		sb.append(line);
		sb.append("<table width=\"730\" border=1 cellpadding=0>");
		int indexStart = Math.max(0, p - 1) * elementsPerPageAroundChar + 1;
		int indexEnd = Math.max(0, p) * elementsPerPageAroundChar;
		int index = 1;
		if (templates.isEmpty())
			sb.append("<tr height=\"22\" align=\"center\">").append("<td>").append("empty").append("</td>").append("</tr>");
		else
		{
			for (SpawnTemplate temp : templates)
			{
				if (index >= indexStart && index <= indexEnd)
					sb.append("<tr height=\"22\" align=\"center\">").append(templateToTd(temp, player, 0, p)).append("</tr>");
				else
					index++;
			}
		}
		sb.append("</table>");
		sb.append(line);
		sb.append("<table cellpadding=0>");
		sb.append("<tr>");
		for (int page = 1; page <= Math.ceil(templates.size() / elementsPerPageAroundChar); page++)
		{
			sb.append("<td><a action=\"bypass -h admin_spawneditor_groupInfo " + page + "\">").append(page).append("</a></td>");
		}
		sb.append("</tr>");
		sb.append("</table>");
		sb.append("</center>");
		return sb.toString();
	}

	private String spawnAroundPlayer(Player player, int page)
	{
		return formattedCharactersList(player.getAroundCharacters(2000, 300), page);
	}

	private String formattedCharactersList(List<Creature> chars, int p)
	{
		StringBuilder sb = new StringBuilder();
		sb.append("<center>");
		sb.append("<table height=\"100\" cellpadding=0>");
		sb.append("<tr><td>Note: blablablablablabla</td></tr>");
		sb.append("</table>");
		sb.append(line);
		sb.append("<table width=\"730\" bgcolor=\"000000\" cellpadding=0>");
		int indexStart = Math.max(0, p - 1) * elementsPerPageAroundChar + 1;
		int indexEnd = Math.max(0, p) * elementsPerPageAroundChar;
		int index = 1;
		if (chars.isEmpty())
		{
			sb.append("<tr height=\"22\" align=\"center\">").append("<td>").append("empty").append("</td>").append("</tr>");
		}
		else
		{
			for (Creature cha : chars)
			{
				if (index >= indexStart && index <= indexEnd)
					sb.append("<tr height=\"22\" align=\"center\">").append(creatureToTd(index++, cha)).append("</tr>");
				else
					index++;
			}
		}
		sb.append("</table>");
		sb.append(line);
		sb.append("<table cellpadding=0>");
		sb.append("<tr>");
		for (int page = 1; page <= Math.ceil(chars.size() / elementsPerPageAroundChar); page++)
		{
			sb.append("<td><a action=\"bypass -h admin_spawneditor " + page + "\">").append(page).append("</a></td>");
		}
		sb.append("</tr>");
		sb.append("</table>");
		sb.append("</center>");
		return sb.toString();
	}

	private String formattedSpawnInfo(Creature obj, int p, Player player)
	{
		StringBuilder sb = new StringBuilder();
		sb.append("<center>");
		final NpcInstance asNpc = (NpcInstance) obj;
		final List<SpawnTemplate> templates = SpawnHolder.getInstance().getAllTemplateBySimple(asNpc.getSpawn().getTemplate());
		final String fileName = SpawnHolder.getInstance().getFileByTemplate(asNpc.getSpawn().getTemplate()) == null ? "none" : SpawnHolder.getInstance().getFileByTemplate(asNpc.getSpawn().getTemplate());
		sb.append("<table height=\"50\" cellpadding=0>");
		sb.append("<tr><td>Note: editing file with name " + fileName + "</td></tr>");
		sb.append("</table>");
		sb.append(line);
		sb.append("<table width=\"730\" bgcolor=\"000000\" cellpadding=0>");
		int indexStart = Math.max(0, p - 1) * elementsPerPageAroundChar + 1;
		int indexEnd = Math.max(0, p) * elementsPerPageAroundChar;
		int index = 1;
		if (!templates.isEmpty())
		{
			for (SpawnTemplate template : SpawnHolder.getInstance().getAllTemplateBySimple(asNpc.getSpawn().getTemplate()))
			{
				if (index >= indexStart && index <= indexEnd)
				{
					sb.append("<tr height=\"22\" align=\"center\">").append(templateToTd(template, player, obj.getObjectId(), p)).append("</tr>");
				}
				index++;
			}
			sb.append("</table>");
			sb.append(line);
			sb.append("<table cellpadding=0>");
			sb.append("<tr><td>Note: editing file with name " + fileName + "</td></tr>");
			sb.append("</table>");
			sb.append(line);
			sb.append("<table cellpadding=0>");
			sb.append("<tr>");
			for (int page = 1; page <= Math.ceil(templates.size() / elementsPerPageAroundChar); page++)
			{
				sb.append("<td><a action=\"bypass -h admin_spawneditor_spawninfo " + page + " " + obj.getObjectId() + "\">").append(page).append("</a></td>");
			}
			sb.append("</tr>");
		}
		sb.append("</table>");
		sb.append("</center>");
		return sb.toString();
	}

	private String creatureToTd(int index, Creature creature)
	{
		return "<td width=\"50\">" + index + "</td>" + // 560
				"<td width=\"110\">" + "<a action=\"bypass -h admin_spawneditor_spawninfo 1 " + creature.getObjectId() + "\">" + creature.getName() + "</a></td>" + // 450
				"<td width=\"150\">" + (creature.isNpc() ? ((NpcInstance) creature).getSpawnedLoc().toString() : creature.getLoc().toString()) + "</td>" + // 300
				"<td width=\"260\">" + (creature.isNpc() ? ((NpcInstance) creature).getSpawn().getTemplate() != null ? ((NpcInstance) creature).getSpawn().getTemplate().getName() : "none" : "none") + "</td>" + // 150
				"<td width=\"75\">" + "<button value=\"Remove\" action=\"bypass" + ""/* TODO */
				+ "\" width=73 height=22 back=\"L2UI_CH3.Btn1_normalOn\" fore=\"L2UI_CH3.Btn1_normal\">" + "</td>" + "<td width=\"75\">" + "<button value=\"Info\" action=\"bypass" + ""/*
																																														 * TODO
																																														 */
				+ "\" width=73 height=22 back=\"L2UI_CH3.Btn1_normalOn\" fore=\"L2UI_CH3.Btn1_normal\">" + "</td>";
	}

	private String templateToTd(SpawnTemplate temp, Player player, int objectId, int page)
	{
		String html = "<td width=\"75\" align=left>" + (temp.getName().startsWith("point:") ? "<a action=\"bypass -h admin_spawneditor_point 1 " + temp.getUuid() + "\">point</a>" : temp.getName()) + "</td>" + "<td width=\"75\" align=left>" + temp.getNpcList().stream().map(it -> it.getTemplate().getId() + "-" + it.getMax()).collect(Collectors.joining(",")) + "</td>" + "<td width=\"70\" align=left>" + "<a action=\"bypass -h admin_spawneditor_territory 1 " + temp.getUuid() + "\">territory</a></td>" + "<td width=\"15\" align=left>" + "<button action=\"bypass -h admin_spawneditor_markasedited " + page + " " + temp.getUuid() + " " + objectId + "\" width=15 height=15 tooltip=\"click to select template as edited\" back=\"L2UI.CheckBox" + isTemplateEdited(player, temp.getUuid()) + "\" fore=\"L2UI.CheckBox" + isTemplateEdited(player, temp.getUuid()) + "\"></button></td>";
		logger.info(html);
		return html;
	}

	@Override
	public boolean useAdminCommand(Enum<?> comm, String[] wordList, String fullString, Player player)
	{
		final String html = "<html><title>spawn editor</title><body><?content?></body></html>";
		commands command = (commands) comm;
		if (command == commands.admin_spawnEditor)
		{
			if (wordList.length < 2)
			{
				player.sendMessage("no such arguments");
				return false;
			}
			ShowBoardPacket.separateAndSend(html.replace("<?content?>", spawnAroundPlayer(player, Integer.parseInt(wordList[1]))), player);
			return true;

		}
		else if (command == commands.admin_spawnEditor_groupInfo)
		{
			if (wordList.length < 3)
			{
				player.sendMessage("no such arguments");
				return false;
			}
			ShowBoardPacket.separateAndSend(html.replace("<?content?>", formattedSpawnTemplateList(SpawnHolder.getInstance().getSpawn(wordList[1]), Integer.parseInt(wordList[2]), player)), player);
			return true;
		}
		else if (command == commands.admin_spawnEditor_spawnInfo)
		{
			if (wordList.length < 3)
			{
				player.sendMessage("no such arguments");
				return false;
			}
			GameObject target = World.getAroundObjectById(player, Integer.parseInt(wordList[2]));
			if (!(target instanceof NpcInstance))
			{
				player.sendMessage("incorrect target");
				return false;
			}
			ShowBoardPacket.separateAndSend(html.replace("<?content?>", formattedSpawnInfo((Creature) target, Integer.parseInt(wordList[1]), player)), player);
			return true;
		}
		else if (command == commands.admin_spawnEditor_markAsEdited)
		{
			if (wordList.length < 3)
			{
				player.sendMessage("no such arguments");
				return false;
			}
			List<SpawnTemplate> playerScratch = editedTemplates.get(player);
			if (playerScratch == null)
			{
				Optional<SpawnTemplate> editedTemplate = SpawnHolder.getInstance().getAllTemplates().stream().filter(it -> it.getUuid().equals(wordList[2])).findFirst();
				if (editedTemplate.isPresent())
				{
					List<SpawnTemplate> templates = new ArrayList<>();
					templates.add(editedTemplate.get());
					editedTemplates.put(player, templates);
				}
			}
			else if (!playerScratch.isEmpty())
			{
				if (playerScratch.removeIf(temp -> temp.getUuid().equals(wordList[2])))
				{
					editedTemplates.put(player, playerScratch);
				}
				else
				{
					Optional<SpawnTemplate> editedTemplate = SpawnHolder.getInstance().getAllTemplates().stream().filter(it -> it.getUuid().equals(wordList[2])).findFirst();
					if (editedTemplate.isPresent())
					{
						playerScratch.add(editedTemplate.get());
						editedTemplates.put(player, playerScratch);
					}
				}
			}
			GameObject target = World.getAroundObjectById(player, Integer.parseInt(wordList[3]));
			if (!(target instanceof NpcInstance))
			{
				player.sendMessage("incorrect target");
				return false;
			}
			ShowBoardPacket.separateAndSend(html.replace("<?content?>", formattedSpawnInfo((Creature) target, Integer.parseInt(wordList[1]), player)), player);
		}
		return false;
	}

	private String isTemplateEdited(Player player, String uuid)
	{
		return editedTemplates.get(player) != null && editedTemplates.get(player).stream().anyMatch(it -> it.getUuid().equals(uuid)) ? "_checked" : "";
	}
}
