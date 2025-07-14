package l2s.dataparser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import l2s.dataparser.data.Parser;
import l2s.dataparser.data.parser.SynthesisParser;
import l2s.dataparser.data.pch.LinkerFactory;

/**
 * @author : Camelion
 * @date : 25.08.12 12:54
 *       <p/>
 *       Главный загрузчик, который управляет последовательностью загрузки
 *       датапака
 */
public class PTSDataLoader
{
	private static final Logger LOGGER = LoggerFactory.getLogger(PTSDataLoader.class);

	public static void load()
	{
		try
		{
			LOGGER.info("/======== Loading PTS scripts data ========/");
			//LinkerFactory.getInstance();

			SynthesisParser.getInstance().load();

			Parser.clear();
			LOGGER.info("/======== End loading PTS scripts data ========/");
		}
		catch(Exception e)
		{
			LOGGER.warn(e.getLocalizedMessage(), e);
		}
	}
}