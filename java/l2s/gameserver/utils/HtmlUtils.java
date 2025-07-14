package l2s.gameserver.utils;

import static l2s.gameserver.utils.Util.STRING_EMPTY;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.lang3.RegExUtils;
import org.apache.commons.lang3.StringUtils;

import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.components.HtmlMessage;
import l2s.gameserver.network.l2.components.NpcString;
import l2s.gameserver.network.l2.components.SysString;

/**
 * @author VISTALL
 * @date 17:17/21.04.2011
 */
public class HtmlUtils
{
	private static final Map<String, String> HTML_GLOBAL_VARIABLES = new HashMap<>();
	private static final Pattern variablePattern = Pattern.compile("\\$\\{.+?}", Pattern.CASE_INSENSITIVE);

	public static final String PREV_BUTTON = "<button value=\"&$1037;\" action=\"bypass %prev_bypass%\" width=\"60\" height=\"25\" back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";
	public static final String NEXT_BUTTON = "<button value=\"&$1038;\" action=\"bypass %next_bypass%\" width=\"60\" height=\"25\" back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";

	public static String htmlResidenceName(int id)
	{
		return "&%" + id + ";";
	}

	public static String htmlNpcName(int npcId)
	{
		return "&@" + npcId + ";";
	}

	public static String htmlSysString(SysString sysString)
	{
		return htmlSysString(sysString.getId());
	}

	public static String htmlSysString(int id)
	{
		return "&$" + id + ";";
	}

	public static String htmlItemName(int itemId)
	{
		return "&#" + itemId + ";";
	}

	public static String htmlClassName(int classId)
	{
		return "<ClassId>" + classId + "</ClassId>";
	}

	public static String htmlNpcString(NpcString id, Object... params)
	{
		return htmlNpcString(id.getId(), params);
	}

	public static String htmlNpcString(int id, Object... params)
	{
		String replace = "<fstring";
		if(params.length > 0)
		{
			for(int i = 0; i < params.length; i++)
			{
				replace += " p" + (i + 1) + "=\"" + String.valueOf(params[i]) + "\"";
			}
		}
		replace += ">" + id + "</fstring>";
		return replace;
	}

	public static String htmlButton(String value, String action, int width)
	{
		return htmlButton(value, action, width, 22);
	}

	public static String htmlButton(String value, String action, int width, int height)
	{
		return String.format("<button value=\"%s\" action=\"%s\" back=\"L2UI_CT1.Button_DF_Small_Down\" width=\"%d\" height=\"%d\" fore=\"L2UI_CT1.Button_DF_Small\">", value, action, width, height);
	}

	public static String iconImg(String icon)
	{
		return "<img src=icon." + icon + " width=32 height=32>";
	}

	public static String bbParse(String s)
	{
		if(s == null)
		{ return null; }

		s = StringUtils.replace(s, "\r", "");
		s = StringUtils.replace(s, "\n", "");
		s = RegExUtils.replaceAll(s, "<!--((?!TEMPLATE).*?)-->", "");
		s = RegExUtils.replaceFirst(s, ".*?(101|102|103)?<\\s*html\\s*>", "$1<html>");
		return s;
	}

	public static void sendHtm(Player player, String htm)
	{
		HtmlMessage htmlMessage = new HtmlMessage(5);
		if(htm.endsWith(".htm"))
		{
			htmlMessage.setFile(htm);
		}
		else
		{
			htmlMessage.setHtml(htm);
		}

		player.sendPacket(htmlMessage);
	}

	public static void registerGlobalHtmlVariable(String variable, Object value)
	{
		HTML_GLOBAL_VARIABLES.put(String.format("${%s}", variable), String.valueOf(value));
	}

	public static String evaluate(String text, Map<String, String> variables)
	{
		if(variables != null)
		{
			variables.putAll(HTML_GLOBAL_VARIABLES);
		}
		else
		{
			variables = HTML_GLOBAL_VARIABLES;
		}
		return parse(text, variables);
	}

	private static String parse(String text, Map<String, String> variables)
	{
		StringBuilder builder = new StringBuilder();
		var matcher = variablePattern.matcher(text);
		while(matcher.find())
		{
			String key = text.substring(matcher.start(), matcher.end());
			matcher.appendReplacement(builder, variables.getOrDefault(key, STRING_EMPTY));
		}
		matcher.appendTail(builder);
		return builder.toString();
	}
}