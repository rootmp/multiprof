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
 * Created with IntelliJ IDEA. User: camelion Date: 12/21/12 Time: 9:04 PM To
 * change this template use File | Settings | File Templates.
 */
public class ItemPchLinker
{
	private static final Logger LOGGER = LoggerFactory.getLogger(ItemPchLinker.class);

	private static final Pattern pattern = Pattern.compile("\\[(.*)]\\s*?=\\s*(\\d+)", Pattern.DOTALL);
	private static final String ITEM_PCH_FILE_NAME = "data/pts_scripts/item_pch.txt";
	private static ItemPchLinker ourInstance = new ItemPchLinker();

	public static ItemPchLinker getInstance()
	{
		return ourInstance;
	}

	private ItemPchLinker()
	{}

	@SuppressWarnings("resource")
	public void load()
	{
		try
		{
			BufferedReader br = Files.newBufferedReader(Config.DATAPACK_ROOT.toPath().resolve(ITEM_PCH_FILE_NAME), Charset.forName("UTF-16"));
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
					LinkerFactory.addLinkItem(link, matcher.group(2));
				}
			}
		}
		catch(Exception e)
		{
			LOGGER.warn(e.getLocalizedMessage(), e);
		}
	}
}