package l2s.gameserver.data.clientDat;

import java.io.BufferedReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import l2s.gameserver.Config;

public final class LetterCollectData 
{
	protected final Logger _log = LoggerFactory.getLogger(LetterCollectData.class);

	private static LetterCollectData  _instance;

	private Map<Integer,int[]> _letterCollectData = new HashMap<Integer,int[]>();

	public Map<Integer,int[]> getLetterCollectData()
	{
		return _letterCollectData;
	}

	public  int[] getLetterCollectDataById(int id)
	{
		return _letterCollectData.get(id);
	}

	private static final Pattern pattern = Pattern.compile("letter_collect_data_begin\\s+id=(\\d+).*?letter_item_ids=(\\S+).*?letter_collect_data_end", Pattern.DOTALL);
	private static final Pattern commentReplacePattern = Pattern.compile("(/\\*[^\\*]*[^/]*/|//[^\\n]*)", Pattern.DOTALL | Pattern.MULTILINE);
	private static final String LETTER_COLLECT_DATA_FILE_NAME = "LetterCollectData_ClassicAden.txt";

	public static LetterCollectData  getInstance()
	{
		if(_instance == null)
			_instance = new LetterCollectData();
		return _instance;
	}

	@SuppressWarnings("resource")
	public void load()
	{
		try
		{
			BufferedReader br = Files.newBufferedReader(Config.DATAPACK_ROOT.toPath().resolve("data/client_dat/event/").resolve(LETTER_COLLECT_DATA_FILE_NAME), Charset.forName("UTF-8"));
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
			// replace comments
			Matcher matcher = commentReplacePattern.matcher(buffer);
			while(matcher.find())
			{
				buffer = new StringBuilder(matcher.replaceAll("").trim());
			}
			matcher = pattern.matcher(buffer);
			while(matcher.find())
			{
				String[] letter_item_ids_s = matcher.group(2).replace("{","").replace("}","").split(";");
				int[] letter_item_ids = Stream.of(letter_item_ids_s).mapToInt(Integer::parseInt).toArray();
				_letterCollectData.put(Integer.parseInt(matcher.group(1)), letter_item_ids);
			}
		}
		catch(Exception e)
		{

			_log.warn(e.getLocalizedMessage(), e);
		}
		_log.info("LetterCollectData: " + _letterCollectData.size());
	}


}