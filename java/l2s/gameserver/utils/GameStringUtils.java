package l2s.gameserver.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GameStringUtils
{
	private static final String TITLE_ICON_REGEX = "\\{i\\d+}";

	public static String checkTitle(String title, int maxLength, boolean removeIcons)
	{
		if(removeIcons)
		{
			title = title.replaceAll(TITLE_ICON_REGEX, "");
			if(title.length() > maxLength)
			{
				title = title.substring(0, maxLength);
			}
		}
		else
		{
			List<String> icons = new ArrayList<>();
			String tempNickName = title;
			Pattern pattern = Pattern.compile(TITLE_ICON_REGEX);
			Matcher matcher = pattern.matcher(tempNickName);
			while(matcher.find())
			{
				String icon = matcher.group(0);
				icons.add(icon);
				tempNickName = tempNickName.replaceFirst(Pattern.quote(matcher.group(0)), "");
			}

			int titleLength = (icons.size() * 2) + tempNickName.length();
			if(titleLength > maxLength)
			{
				if(icons.isEmpty())
				{
					title = title.substring(0, maxLength);
				}
				else
				{
					while(titleLength > maxLength)
					{
						String lastIcon = icons.get(icons.size() - 1);
						if(title.endsWith(lastIcon))
						{
							icons.remove(icons.size() - 1);
							title = title.substring(0, title.length() - lastIcon.length());
							titleLength -= 2;
						}
						else
						{
							title = title.substring(0, title.length() - 1);
							titleLength--;
						}
					}
				}
			}
		}
		return title;
	}
}
