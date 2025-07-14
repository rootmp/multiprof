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
 * Created with IntelliJ IDEA. User: camelion Date: 12/19/12 Time: 6:04 PM To
 * change this template use File | Settings | File Templates.
 */
public class SkillPchLinker
{
	private static final Logger LOGGER = LoggerFactory.getLogger(SkillPchLinker.class);

	private static final Pattern pattern = Pattern.compile("\\[(.*)]\\s*?=\\s*(\\d+)", Pattern.DOTALL);
	private static final String SKILL_PCH_FILE_NAME = "data/pts_scripts/skill_pch.txt";
	private static SkillPchLinker ourInstance = new SkillPchLinker();

	public static SkillPchLinker getInstance()
	{
		return ourInstance;
	}

	private SkillPchLinker()
	{}

	@SuppressWarnings("resource")
	public void load()
	{
		try
		{
			BufferedReader br = Files.newBufferedReader(Config.DATAPACK_ROOT.toPath().resolve(SKILL_PCH_FILE_NAME), Charset.forName("UTF-16"));
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
					final int[] array = new int[2];
					//TODO на след версиях
					final int skill_id = (int) (Long.parseLong(matcher.group(2)) / 65536 / 65536);
					final int skill_level = (int) (Long.parseLong(matcher.group(2)) - 65536 * 65536 * skill_id);
					//final int skill_id =  (int) (Long.parseLong(matcher.group(2)) /65536);
					//final int skill_level =(int) (Long.parseLong(matcher.group(2)) - 65536 * skill_id);
					array[0] = skill_id;
					array[1] = skill_level;
					LinkerFactory.addLink(link, matcher.group(2));
					LinkerFactory.addLinkSkillPch(link, array);
				}
			}
		}
		catch(Exception e)
		{
			LOGGER.warn(e.getLocalizedMessage(), e);
		}
	}
}