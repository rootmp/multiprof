package l2s.dataparser.data.pch.linker;

import java.io.BufferedReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import l2s.dataparser.data.pch.LinkerFactory;
import l2s.gameserver.Config;

/**
 * Created with IntelliJ IDEA. User: camelion Date: 13/01/13 Time: 2:15 AM To
 * change this template use File | Settings | File Templates.
 */
public class NpcPchLinker
{
	private static final Logger LOGGER = LoggerFactory.getLogger(NpcPchLinker.class);

	private static final Pattern pattern = Pattern.compile("\\[(.*)]\\s*?=\\s*(\\d+)", Pattern.DOTALL);
	private static final String NPC_PCH_FILE_NAME = "data/pts_scripts/npc_pch.txt";
	private static NpcPchLinker ourInstance = new NpcPchLinker();

	public static NpcPchLinker getInstance()
	{
		return ourInstance;
	}

	private NpcPchLinker()
	{}

	@SuppressWarnings("resource")
	public void load()
	{
		try
		{
			BufferedReader br = Files.newBufferedReader(Config.DATAPACK_ROOT.toPath().resolve(NPC_PCH_FILE_NAME), Charset.forName("UTF-16"));
			String line;
			// Считываем файл до конца
			while((line = br.readLine()) != null)
			{
				line = line.trim();
				if(line.startsWith("//") || line.isEmpty())
					continue;
				if(line.contains("//"))
				{// обрезаем комментарии
					int index = line.indexOf("//");
					String replacement = line.substring(index);
					line = line.replace(replacement, "").trim();
				}
				if(line.isEmpty())
					continue;
				Matcher matcher = pattern.matcher(line);
				if(matcher.find())
				{
					String link = "@" + matcher.group(1);
					LinkerFactory.addLink(link, matcher.group(2));
					LinkerFactory.addLinkNpc(link, matcher.group(2));
				}
			}
		}
		catch(Exception e)
		{
			LOGGER.warn(e.getLocalizedMessage(), e);
		}
	}
}