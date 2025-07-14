package l2s.dataparser.data.common;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import l2s.commons.data.xml.AbstractHolder;
import l2s.commons.data.xml.AbstractParser;
import l2s.dataparser.data.Parser;
import l2s.gameserver.Config;

/**
 * @author : Camelion
 * @date : 22.08.12 1:37
 */
public abstract class AbstractDataParser<H extends AbstractHolder> extends AbstractParser<H>
{
	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractDataParser.class);
	private static final Pattern commentReplacePattern = Pattern.compile("(/\\*[^\\*]*[^/]*/|//[^\\n]*)", Pattern.DOTALL | Pattern.MULTILINE);

	protected AbstractDataParser(H holder)
	{
		super(holder);
	}

	@Override
	protected void parse()
	{
		try
		{
			StringBuilder buffer = new StringBuilder();
			Path path = Config.DATAPACK_ROOT.toPath().resolve(getFileName());

			if(Files.isDirectory(path))
			{
				try (Stream<Path> fileStream = Files.list(path))
				{
					StringBuilder combinedBuffer = new StringBuilder();
					fileStream.filter(file -> file.toString().endsWith(".txt")).sorted().forEach(file -> {
						try (BufferedReader br = Files.newBufferedReader(file, StandardCharsets.UTF_16))
						{
							String line;
							while((line = br.readLine()) != null)
							{
								if(line.startsWith("//"))
									continue;
								if(line.contains("//"))
								{
									// Обрезаем комментарии
									int index = line.indexOf("//");
									line = line.substring(0, index).trim();
								}
								line = line.trim();
								if(!line.isEmpty())
									combinedBuffer.append(line).append("\n");
							}
						}
						catch(IOException e)
						{
							LOGGER.warn("Ошибка при чтении файла: " + file, e);
						}
					});
					buffer.append(combinedBuffer);
				}
			}
			else
			{
				// Если путь - файл, читаем его как обычно
				try (BufferedReader br = Files.newBufferedReader(path, StandardCharsets.UTF_16))
				{
					String line;
					while((line = br.readLine()) != null)
					{
						if(line.startsWith("//"))
							continue;
						if(line.contains("//"))
						{
							// Обрезаем комментарии
							int index = line.indexOf("//");
							line = line.substring(0, index).trim();
						}
						line = line.trim();
						if(!line.isEmpty())
							buffer.append(line).append("\n");
					}
				}
			}

			// Убираем комментарии в конце файла
			Matcher matcher = commentReplacePattern.matcher(buffer);
			while(matcher.find())
			{
				buffer = new StringBuilder(matcher.replaceAll("").trim());
			}

			// Разбираем содержимое
			@SuppressWarnings("unused")
			StringBuilder lost = Parser.parseClass(buffer, _holder.getClass(), _holder);
		}
		catch(Exception e)
		{
			LOGGER.warn(e.getLocalizedMessage(), e);
		}
	}

	@Override
	protected void readData(Element rootElement) throws Exception
	{}

	@Override
	public File getXMLPath()
	{
		return null;
	}

	@Override
	public String getDTDFileName()
	{
		return null;
	}

	protected abstract String getFileName();
}