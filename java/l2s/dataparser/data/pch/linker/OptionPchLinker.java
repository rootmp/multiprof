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
 * @author : Mangol
 */
public class OptionPchLinker
{
	private static final Logger LOGGER = LoggerFactory.getLogger(OptionPchLinker.class);

	private static final Pattern pattern = Pattern.compile("\\[(.*)]\\s*?=\\s*(\\d+)", Pattern.DOTALL);
	private static final String OPTION_PCH_FILE_NAME = "data/pts_scripts/option_pch.txt";
	private static OptionPchLinker ourInstance = new OptionPchLinker();

	public static OptionPchLinker getInstance()
	{
		return ourInstance;
	}

	private OptionPchLinker()
	{}

	@SuppressWarnings("resource")
	public void load()
	{
		try
		{
			BufferedReader br = Files.newBufferedReader(Config.DATAPACK_ROOT.toPath().resolve(OPTION_PCH_FILE_NAME), Charset.forName("UTF-16"));
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
					LinkerFactory.addOptionPchLink(link, matcher.group(2));
				}
			}
		}
		catch(Exception e)
		{
			LOGGER.warn(e.getLocalizedMessage(), e);
		}
	}
}
