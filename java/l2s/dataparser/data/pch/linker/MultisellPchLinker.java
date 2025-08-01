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
 * Created with IntelliJ IDEA. User: camelion Date: 12/01/2013 Time: 11:27 PM To
 * change this template use File | Settings | File Templates.
 */
public class MultisellPchLinker
{
	private static final Logger LOGGER = LoggerFactory.getLogger(MultisellPchLinker.class);

	private static final Pattern pattern = Pattern.compile("MultiSell_begin\\s+\\[(\\S+)\\]\\s+(\\d+).*?MultiSell_end", Pattern.DOTALL);
	private static final String MULTISELL_FILE_NAME = "data/pts_scripts/multisell.txt";
	private static MultisellPchLinker ourInstance = new MultisellPchLinker();

	public static MultisellPchLinker getInstance()
	{
		return ourInstance;
	}

	private MultisellPchLinker()
	{}

	@SuppressWarnings("resource")
	public void load()
	{
		try
		{
			BufferedReader br = Files.newBufferedReader(Config.DATAPACK_ROOT.toPath().resolve(MULTISELL_FILE_NAME), Charset.forName("UTF-16"));
			StringBuilder buffer = new StringBuilder();
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
				buffer.append(line).append("\n");
			}
			Matcher matcher = pattern.matcher(buffer);
			while(matcher.find())
			{
				String link = matcher.group(1);
				String value = matcher.group(2);
				link = "@" + link;
				LinkerFactory.addLink(link, value);
			}
		}
		catch(Exception e)
		{
			LOGGER.warn(e.getLocalizedMessage(), e);
		}
	}
}