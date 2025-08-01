package l2s.dataparser.data;

import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import l2s.dataparser.data.annotations.Element;
import l2s.dataparser.data.annotations.ElementArray;
import l2s.dataparser.data.annotations.array.DoubleArray;
import l2s.dataparser.data.annotations.array.EnumArray;
import l2s.dataparser.data.annotations.array.IntArray;
import l2s.dataparser.data.annotations.array.LinkedArray;
import l2s.dataparser.data.annotations.array.ObjectArray;
import l2s.dataparser.data.annotations.array.StringArray;
import l2s.dataparser.data.annotations.class_annotations.ParseSuper;
import l2s.dataparser.data.annotations.factory.IObjectFactory;
import l2s.dataparser.data.annotations.value.DateValue;
import l2s.dataparser.data.annotations.value.DoubleValue;
import l2s.dataparser.data.annotations.value.EnumValue;
import l2s.dataparser.data.annotations.value.IntValue;
import l2s.dataparser.data.annotations.value.LinkedValue;
import l2s.dataparser.data.annotations.value.LongValue;
import l2s.dataparser.data.annotations.value.ObjectValue;
import l2s.dataparser.data.annotations.value.StringValue;
import l2s.dataparser.data.annotations.value.TimeValue;
import l2s.dataparser.data.common.LinkedType;
import l2s.dataparser.data.common.ParserUtil;
import l2s.dataparser.data.pch.LinkerFactory;
import l2s.gameserver.skills.enums.BasicProperty;
import l2s.gameserver.templates.item.ItemGrade;

/**
 * @author : Camelion
 * @date : 22.08.12 2:24
 */
public class Parser
{
	private static final Logger LOGGER = LoggerFactory.getLogger(Parser.class);

	private static long doElementAnnotationWorkTime = 0;
	private static long doElementArrayAnnotationWorkTime = 0;
	private static long doIntValueWorkTime = 0;
	private static long doLongValueWorkTime = 0;
	private static long doDoubleValueWorkTime = 0;
	private static long doStringValueWorkTime = 0;
	private static long doEnumValueWorkTime = 0;
	private static long doObjectValueWorkTime = 0;
	private static long doTimeValueWorkTime = 0;
	private static long doDateValueWorkTime = 0;
	private static long doIntArrayWorkTime = 0;
	private static long doDoubleArrayWorkTime = 0;
	private static long doStringArrayWorkTime = 0;
	private static long doEnumArrayWorkTime = 0;
	private static long doObjectArrayWorkTime = 0;
	private static long doLinkedArrayWorkTime = 0;

	public static StringBuilder parseClass(StringBuilder buffer, Class<?> clazz, Object object) throws Exception
	{
		// Parse class annotations
		ParseSuper parseSuper = clazz.getAnnotation(ParseSuper.class);
		if(parseSuper != null)
			buffer = parseClass(buffer, clazz.getSuperclass(), object);
		// Parse field annotations
		for(Field field : clazz.getDeclaredFields())
		{
			if(field.getAnnotations().length == 0)
				continue;
			boolean fieldAccessible = field.isAccessible();
			field.setAccessible(true);
			for(Annotation annotation : field.getDeclaredAnnotations())
			{
				if(annotation instanceof Element)
				{
					Element elementAnnotation = (Element) annotation;
					long startTime = System.nanoTime();
					buffer = doElementAnnotation(elementAnnotation, buffer, field, object);
					doElementAnnotationWorkTime += System.nanoTime() - startTime;
				}
				else if(annotation instanceof ElementArray)
				{
					ElementArray elementArrayAnnotation = (ElementArray) annotation;
					long startTime = System.nanoTime();
					buffer = doElementArrayAnnotation(elementArrayAnnotation, buffer, field, object);
					doElementArrayAnnotationWorkTime += System.nanoTime() - startTime;
				}
				else if(annotation instanceof IntValue)
				{
					IntValue intValueAnnotation = (IntValue) annotation;
					long startTime = System.nanoTime();
					buffer = doIntValue(intValueAnnotation, buffer, field, object);
					doIntValueWorkTime += System.nanoTime() - startTime;
				}
				else if(annotation instanceof LongValue)
				{
					LongValue longValueAnnotation = (LongValue) annotation;
					long startTime = System.nanoTime();
					buffer = doLongValue(longValueAnnotation, buffer, field, object);
					doLongValueWorkTime += System.nanoTime() - startTime;
				}
				else if(annotation instanceof DoubleValue)
				{
					DoubleValue doubleValueAnnotation = (DoubleValue) annotation;
					long startTime = System.nanoTime();
					buffer = doDoubleValue(doubleValueAnnotation, buffer, field, object);
					doDoubleValueWorkTime += System.nanoTime() - startTime;
				}
				else if(annotation instanceof StringValue)
				{
					StringValue stringValueAnnotation = (StringValue) annotation;
					long startTime = System.nanoTime();
					buffer = doStringValue(stringValueAnnotation, buffer, field, object);
					doStringValueWorkTime += System.nanoTime() - startTime;
				}
				else if(annotation instanceof LinkedValue)
				{
					LinkedValue stringValueAnnotation = (LinkedValue) annotation;
					long startTime = System.nanoTime();
					buffer = doLinkedValue(stringValueAnnotation, buffer, field, object);
					doStringValueWorkTime += System.nanoTime() - startTime;
				}
				else if(annotation instanceof EnumValue)
				{
					EnumValue enumValueAnnotation = (EnumValue) annotation;
					long startTime = System.nanoTime();
					buffer = doEnumValue(enumValueAnnotation, buffer, field, object);
					doEnumValueWorkTime += System.nanoTime() - startTime;
				}
				else if(annotation instanceof ObjectValue)
				{
					ObjectValue objectValueAnnotation = (ObjectValue) annotation;
					long startTime = System.nanoTime();
					buffer = doObjectValue(objectValueAnnotation, buffer, field, object);
					doObjectValueWorkTime += System.nanoTime() - startTime;
				}
				else if(annotation instanceof TimeValue)
				{
					TimeValue timeValueAnnotation = (TimeValue) annotation;
					long startTime = System.nanoTime();
					buffer = doTimeValue(timeValueAnnotation, buffer, field, object);
					doTimeValueWorkTime += System.nanoTime() - startTime;
				}
				else if(annotation instanceof DateValue)
				{
					DateValue dateValueAnnotation = (DateValue) annotation;
					long startTime = System.nanoTime();
					buffer = doDateValue(dateValueAnnotation, buffer, field, object);
					doDateValueWorkTime += System.nanoTime() - startTime;
				}
				else if(annotation instanceof IntArray)
				{
					IntArray intArrayAnnotation = (IntArray) annotation;
					long startTime = System.nanoTime();
					buffer = doIntArray(intArrayAnnotation, buffer, field, object);
					doIntArrayWorkTime += System.nanoTime() - startTime;
				}
				else if(annotation instanceof DoubleArray)
				{
					DoubleArray doubleArrayAnnotation = (DoubleArray) annotation;
					long startTime = System.nanoTime();
					buffer = doDoubleArray(doubleArrayAnnotation, buffer, field, object);
					doDoubleArrayWorkTime += System.nanoTime() - startTime;
				}
				else if(annotation instanceof StringArray)
				{
					StringArray stringArrayAnnotation = (StringArray) annotation;
					long startTime = System.nanoTime();
					buffer = doStringArray(stringArrayAnnotation, buffer, field, object);
					doStringArrayWorkTime += System.nanoTime() - startTime;
				}
				else if(annotation instanceof EnumArray)
				{
					EnumArray enumArrayAnnotation = (EnumArray) annotation;
					long startTime = System.nanoTime();
					buffer = doEnumArray(enumArrayAnnotation, buffer, field, object);
					doEnumArrayWorkTime += System.nanoTime() - startTime;
				}
				else if(annotation instanceof ObjectArray)
				{
					ObjectArray objectArrayAnnotation = (ObjectArray) annotation;
					long startTime = System.nanoTime();
					buffer = doObjectArray(objectArrayAnnotation, buffer, field, object);
					doObjectArrayWorkTime += System.nanoTime() - startTime;
				}
				else if(annotation instanceof LinkedArray)
				{
					LinkedArray linkedArrayAnnotation = (LinkedArray) annotation;
					long startTime = System.nanoTime();
					buffer = doLinkedArray(linkedArrayAnnotation, buffer, field, object);
					doLinkedArrayWorkTime += System.nanoTime() - startTime;
				}
			}
			field.setAccessible(fieldAccessible);
		}
		return buffer;
	}

	private static final Pattern doubleArrayWithoutNamePattern = Pattern.compile("\\{([\\d+-\\.; ]*?)}", Pattern.DOTALL);
	private static Map<String, Pattern> doubleArrayPatternCache = new HashMap<>();

	private static StringBuilder doDoubleArray(DoubleArray doubleArrayAnnotation, StringBuilder buffer, Field field, Object object) throws IllegalAccessException
	{
		boolean isList = List.class.isAssignableFrom(field.getType());
		String name = doubleArrayAnnotation.name().isEmpty() ? field.getName() : doubleArrayAnnotation.name();
		boolean withoutName = doubleArrayAnnotation.withoutName();
		String splitter = doubleArrayAnnotation.splitter();
		boolean canBeNull = doubleArrayAnnotation.canBeNull();
		Pattern pattern;
		if(withoutName)
		{
			pattern = doubleArrayWithoutNamePattern;
		}
		else
		{
			if(!doubleArrayPatternCache.containsKey(name))
			{
				StringBuilder s = new StringBuilder();
				s.append("\\b").append(name).append("\\s*?=\\s*?\\{([\\d+-\\.; ]*?)}");
				pattern = Pattern.compile(s.toString(), Pattern.DOTALL);
				doubleArrayPatternCache.put(name, pattern);
			}
			else
			{
				pattern = doubleArrayPatternCache.get(name);
			}
		}
		Matcher matcher = pattern.matcher(buffer);
		while(matcher.find())
		{
			String elementBuffer = matcher.group(1).trim();
			// Удаляем текущий элемент из буфера
			buffer = buffer.delete(matcher.start(), matcher.end());
			matcher = pattern.matcher(buffer);
			// Проверяем, может ли объект принять значение null
			if(canBeNull && ParserUtil.mayBeNullObject(elementBuffer))
			{
				if(!isList)
					break;
				else
					continue;
			}
			String[] parts = elementBuffer.split(splitter);
			double[] array = new double[parts.length];
			for(int i = 0; i < parts.length; i++)
				array[i] = Double.valueOf(parts[i].trim());
			ParserUtil.appendValueToField(field, object, array);
			if(!isList)
				break;
		}
		return buffer;
	}

	private static final Pattern longValueWithoutNamePattern = Pattern.compile("([\\d-]+)", Pattern.DOTALL);
	private static Map<String, Pattern> longValuePatternCache = new HashMap<>();

	private static StringBuilder doLongValue(LongValue longValueAnnotation, StringBuilder buffer, Field field, Object object) throws IllegalAccessException
	{
		String name = longValueAnnotation.name().isEmpty() ? field.getName() : longValueAnnotation.name();
		boolean withoutName = longValueAnnotation.withoutName();
		Pattern pattern;
		if(withoutName)
		{
			pattern = longValueWithoutNamePattern;
		}
		else
		{
			if(longValuePatternCache.containsKey(name))
			{
				pattern = longValuePatternCache.get(name);
			}
			else
			{
				StringBuilder s = new StringBuilder();
				s.append("\\b").append(name).append("\\s*?=\\s*?([\\d-]+)");
				pattern = Pattern.compile(s.toString(), Pattern.DOTALL);
				longValuePatternCache.put(name, pattern);
			}
		}
		Matcher matcher = pattern.matcher(buffer);
		if(matcher.find())
		{
			String valueBuffer = matcher.group(1);
			long intValue = Long.valueOf(valueBuffer);
			ParserUtil.appendValueToField(field, object, intValue);
			// Удаляем текущий элемент из буфера
			buffer = buffer.delete(matcher.start(), matcher.end());
		}
		return buffer;
	}

	private static StringBuilder doDateValue(DateValue dateValueAnnotation, StringBuilder buffer, Field field, Object object) throws ParseException, IllegalAccessException
	{
		String name = dateValueAnnotation.name().isEmpty() ? field.getName() : dateValueAnnotation.name();
		String format = dateValueAnnotation.format();
		Pattern pattern;
		StringBuilder s = new StringBuilder();
		s.append("\\b").append(name).append("\\s*?=\\s*?(.*?)\n");
		pattern = Pattern.compile(s.toString(), Pattern.DOTALL);
		Matcher matcher = pattern.matcher(buffer);
		if(matcher.find())
		{
			String stringValue = matcher.group(1).trim();
			buffer = buffer.delete(matcher.start(), matcher.end());
			SimpleDateFormat dateFormat = new SimpleDateFormat(format);
			Date date = dateFormat.parse(stringValue);
			ParserUtil.appendValueToField(field, object, date);
		}
		return buffer;
	}

	private static final Pattern linkPatternWithoutName = Pattern.compile("\\{([@\\-\n\t\\d\\w_;]+)}", Pattern.DOTALL);

	private static StringBuilder doLinkedArray(LinkedArray linkedArrayAnnotation, StringBuilder buffer, Field field, Object object) throws Exception
	{
		boolean isList = List.class.isAssignableFrom(field.getType());
		String name = linkedArrayAnnotation.name().isEmpty() ? field.getName() : linkedArrayAnnotation.name();
		boolean withoutName = linkedArrayAnnotation.withoutName();
		boolean canBeNull = linkedArrayAnnotation.canBeNull();
		String splitter = linkedArrayAnnotation.splitter();
		LinkedType type = linkedArrayAnnotation.LinkedType();
		String regexp = linkedArrayAnnotation.regexp();

		Pattern pattern;
		if(withoutName)
		{
			pattern = linkPatternWithoutName;
		}
		else
		{
			pattern = Pattern.compile(String.format(regexp.isEmpty() ? "\\b%s\\s*?=\\s*?\\{([@\\-\n\t\\d\\w_;]+)}" : regexp, name), Pattern.DOTALL);
		}
		Matcher matcher = pattern.matcher(buffer);
		while(matcher.find())
		{
			String elementBuffer = matcher.group(1);
			// Удаляем текущий элемент из буфера
			buffer = buffer.delete(matcher.start(), matcher.end());
			matcher = pattern.matcher(buffer);
			elementBuffer = elementBuffer.replaceAll("\\s+", "");
			// Проверяем, может ли объект принять значение null
			if(canBeNull && ParserUtil.mayBeNullObject(elementBuffer))
			{
				if(!isList)
					break;
				else
					continue;
			}
			String[] parts = elementBuffer.split(splitter);
			long[] array = new long[parts.length];
			for(int i = 0; i < parts.length; i++)
			{
				String link = parts[i];
				switch(type)
				{
					case linked_all:
						if(link.startsWith("@"))
						{
							String p = LinkerFactory.getInstance().findValueFor(link);
							if(p == null)
								throw new NoSuchFieldException(link);
							array[i] = Long.parseLong(p);
						}
						else if(!NumberUtils.isParsable(link))
							array[i] = LinkerFactory.getInstance().npcPchfindClearValue(link) - 1000000;
						else
							array[i] = Long.parseLong(link);
						break;
					case linked_option:
						if(!NumberUtils.isParsable(link))
							array[i] = LinkerFactory.getInstance().optionPchfindClearValue(link);
						else
							array[i] = Long.parseLong(link);
						break;
					case linked_item:
						if(!NumberUtils.isParsable(link))
							array[i] = LinkerFactory.getInstance().itemPchfindClearValue(link);
						else
							array[i] = Long.parseLong(link);
						break;
					case linked_npc:
						if(!NumberUtils.isParsable(link))
							array[i] = LinkerFactory.getInstance().npcPchfindClearValue(link) - 1000000;
						else
							array[i] = Long.parseLong(link);
						break;
					case linked_skill:
						if(link.startsWith("@"))
						{
							String p = LinkerFactory.getInstance().findValueFor(link);
							if(p == null)
								throw new NoSuchFieldException(link);
							array[i] = Long.parseLong(p);
						}
						else
						{
							String p = LinkerFactory.getInstance().findValueFor("@" + link);
							if(p == null)
								throw new NoSuchFieldException(link);
							array[i] = Long.parseLong(p);
						}
						break;
					default:
						break;

				}
			}
			ParserUtil.appendValueToField(field, object, array);
			if(!isList)
				break;
		}
		return buffer;
	}

	private static final Pattern timeValueWithoutNamePattern = Pattern.compile("(\\d+)hour|min|sec");

	// Не применимо к List списку
	private static StringBuilder doTimeValue(TimeValue timeValueAnnotation, StringBuilder buffer, Field field, Object object) throws Exception
	{
		String name = timeValueAnnotation.name().isEmpty() ? field.getName() : timeValueAnnotation.name();
		boolean withoutName = timeValueAnnotation.withoutName();
		Pattern pattern;
		if(withoutName)
		{
			pattern = timeValueWithoutNamePattern;
		}
		else
		{
			StringBuilder s = new StringBuilder();
			s.append("\\b").append(name).append("\\s*?=\\s*?(\\d+)(hour|min|sec)");
			pattern = Pattern.compile(s.toString(), Pattern.DOTALL);
		}
		Matcher matcher = pattern.matcher(buffer);
		if(matcher.find())
		{
			String stringValue = matcher.group(1);
			String type = matcher.group(2);
			buffer = buffer.delete(matcher.start(), matcher.end());
			long longValue = Long.valueOf(stringValue);
			// Convert to seconds
			if(type.equals("min"))
				longValue *= 60;
			else if(type.equals("hour"))
				longValue *= 3600;
			ParserUtil.appendValueToField(field, object, longValue);
		}
		return buffer;
	}

	private static final Pattern doubleValueWithoutNamePattern = Pattern.compile("([\\d\\.\\-]+)", Pattern.DOTALL);

	private static StringBuilder doDoubleValue(DoubleValue doubleValueAnnotation, StringBuilder buffer, Field field, Object object) throws IllegalAccessException
	{
		boolean isList = List.class.isAssignableFrom(field.getType());
		String name = doubleValueAnnotation.name().isEmpty() ? field.getName() : doubleValueAnnotation.name();
		boolean withoutName = doubleValueAnnotation.withoutName();
		Pattern pattern;
		if(withoutName)
		{
			pattern = doubleValueWithoutNamePattern;
		}
		else
		{
			pattern = Pattern.compile(String.format("\\b%s\\s*?=\\s*?([\\d\\.\\-]+)", name), Pattern.DOTALL);
		}
		Matcher matcher = pattern.matcher(buffer);
		while(matcher.find())
		{
			String valueBuffer = matcher.group(1);
			double doubleValue = Double.valueOf(valueBuffer);
			ParserUtil.appendValueToField(field, object, doubleValue);
			// Удаляем текущий элемент из буфера
			buffer = buffer.delete(matcher.start(), matcher.end());
			if(!isList)
				break;
			else
				matcher = pattern.matcher(buffer);
		}
		return buffer;
	}

	private static final Pattern stringArrayWithoutNamePattern = Pattern.compile("\\{([\\S; \n\t]*?)}", Pattern.DOTALL);
	private static Map<String, Pattern> stringArrayPatternCache = new HashMap<>();

	private static StringBuilder doStringArray(StringArray stringArrayAnnotation, StringBuilder buffer, Field field, Object object) throws IllegalAccessException
	{
		boolean isList = List.class.isAssignableFrom(field.getType());
		String name = stringArrayAnnotation.name().isEmpty() ? field.getName() : stringArrayAnnotation.name();
		boolean withoutName = stringArrayAnnotation.withoutName();
		String splitter = stringArrayAnnotation.splitter();
		String[] bounds = stringArrayAnnotation.bounds();
		boolean canBeNull = stringArrayAnnotation.canBeNull();
		Pattern pattern;
		if(withoutName)
		{
			if(bounds[0].equals("\\{") && bounds[1].equals("}"))
				pattern = stringArrayWithoutNamePattern;
			else
			{
				StringBuilder s = new StringBuilder();
				s.append("\\b").append(bounds[0]).append("([\\S;]+)").append(bounds[1]);
				pattern = Pattern.compile(s.toString(), Pattern.DOTALL);
			}
		}
		else
		{
			if(!stringArrayPatternCache.containsKey(name))
			{
				StringBuilder s = new StringBuilder();
				s.append("\\b").append(name).append("\\s*?=\\s*?\\{([\\S; \n\t]*?)}");
				pattern = Pattern.compile(s.toString(), Pattern.DOTALL);
			}
			else
			{
				pattern = stringArrayPatternCache.get(name);
			}
		}
		Matcher matcher = pattern.matcher(buffer);
		while(matcher.find())
		{
			String elementBuffer = matcher.group(1);
			// Удаляем текущий элемент из буфера
			buffer = buffer.delete(matcher.start(), matcher.end());
			// Проверяем, может ли объект принять значение null
			if(canBeNull && ParserUtil.mayBeNullObject(elementBuffer))
			{
				if(!isList)
					break;
				else
					continue;
			}
			elementBuffer = elementBuffer.replaceAll("[\\[\\]\\n\\t ]+", "");
			String[] parts = elementBuffer.split(splitter);
			ParserUtil.appendValueToField(field, object, parts);
			if(!isList)
				break;
			else
				matcher = pattern.matcher(buffer);
		}
		return buffer;
	}

	private static final Pattern enumValueWithoutNamePattern = Pattern.compile("([\\w\\d_]+)", Pattern.DOTALL);
	private static Map<String, Pattern> enumValuePatternCache = new HashMap<>();

	@SuppressWarnings({
			"rawtypes", "unchecked"
	})
	private static StringBuilder doEnumValue(EnumValue enumValueAnnotation, StringBuilder buffer, Field field, Object object) throws IllegalAccessException
	{
		String name = enumValueAnnotation.name().isEmpty() ? field.getName() : enumValueAnnotation.name();
		boolean withoutName = enumValueAnnotation.withoutName();
		Class<? extends Enum> clazz = (Class<? extends Enum>) field.getType();
		Pattern pattern;
		if(withoutName)
		{
			pattern = enumValueWithoutNamePattern;
		}
		else if(!enumValueAnnotation.customRegexNoCache().isEmpty())
		{
			StringBuilder s = new StringBuilder();
			s.append("\\b").append(name).append(enumValueAnnotation.customRegexNoCache());
			pattern = Pattern.compile(s.toString(), Pattern.DOTALL);
		}
		else
		{
			if(!enumValuePatternCache.containsKey(name))
			{
				StringBuilder s = new StringBuilder();
				s.append("\\b").append(name).append(enumValueAnnotation.customRegex().isEmpty() ? "\\s*?=\\s*?([\\w\\d_]+)" : enumValueAnnotation.customRegex());
				pattern = Pattern.compile(s.toString(), Pattern.DOTALL);
				enumValuePatternCache.put(name, pattern);
			}
			else
			{
				pattern = enumValuePatternCache.get(name);
			}
		}
		if(enumValueAnnotation.log())
		{
			System.out.println("EnumValue: " + field.getName() + " = " + buffer.toString() + " " + pattern.pattern());
		}

		Matcher matcher = pattern.matcher(buffer);
		if(matcher.find())
		{
			String valueBuffer = matcher.group(1);

			if(enumValueAnnotation.log())
			{
				System.out.println("valueBuffer: " + valueBuffer);
			}
			if(enumValueAnnotation.replaceChars().length > 0)
			{
				String regex = "[" + Pattern.quote(new String(enumValueAnnotation.replaceChars())) + "]";
				valueBuffer = valueBuffer.replaceAll(regex, "");
			}
			Enum enumValue;
			/*	if(clazz.getName() == NpcRace.class.getName())
					enumValue = Enum.valueOf(clazz, valueBuffer.replaceAll("race_", ""));
				else */if(clazz.getName() == ItemGrade.class.getName())
				enumValue = Enum.valueOf(clazz, valueBuffer.toUpperCase());//костыль, выпилить когда нибудь
			else if(clazz.getName() == BasicProperty.class.getName())
				enumValue = Enum.valueOf(clazz, valueBuffer.toUpperCase());// поднимем базовые параметры в верхний регистр (нельзя создать int в нижнем регистре) 
			else
				enumValue = Enum.valueOf(clazz, valueBuffer);

			ParserUtil.appendValueToField(field, object, enumValue);
			// Удаляем текущий элемент из буфера
			buffer = buffer.delete(matcher.start(), matcher.end());
		}
		return buffer;
	}

	private static Map<String, Pattern> objectValuePatternCache = new HashMap<>();

	@SuppressWarnings("unused")
	private static StringBuilder doObjectValue(ObjectValue objectValueAnnotation, StringBuilder buffer, Field field, Object object) throws Exception
	{
		boolean isList = List.class.isAssignableFrom(field.getType());
		Class<?> clazz;
		if(isList)
		{
			ParameterizedType parameterizedType = (ParameterizedType) field.getGenericType();
			clazz = (Class<?>) parameterizedType.getActualTypeArguments()[0];
		}
		else
		{
			clazz = field.getType();
		}
		String name = objectValueAnnotation.name().isEmpty() ? field.getName() : objectValueAnnotation.name();
		boolean withoutName = objectValueAnnotation.withoutName();
		boolean canBeNull = objectValueAnnotation.canBeNull();
		boolean dotAll = objectValueAnnotation.dotAll();
		IObjectFactory<?> objectFactory = objectValueAnnotation.objectFactory().getDeclaredConstructor().newInstance();
		objectFactory.setFieldClass(clazz);
		StringBuilder stringArray;
		Pattern pattern;
		if(withoutName)
		{
			stringArray = ParserUtil.getArray(0, buffer);// Main array
			int to = buffer.indexOf(stringArray.toString()) + stringArray.length();
			buffer = buffer.delete(0, to);
			stringArray = ParserUtil.removeBracetsAndCopy(stringArray);
			// Проверяем, может ли объект принять значение null
			if(canBeNull && ParserUtil.mayBeNullObject(stringArray))
				return buffer;
			Object objectValueObject = objectFactory.createObjectFor(stringArray);
			ParserUtil.appendValueToField(field, object, objectValueObject);
			StringBuilder lostBuffer = parseClass(stringArray, clazz, objectValueObject);
		}
		else
		{
			if(!objectValuePatternCache.containsKey(dotAll ? "dotAll:" + name : name))
			{
				StringBuilder s = new StringBuilder();
				if(dotAll)
					s.append("(?s)");
				s.append(name).append("\\s*?=\\s*?");
				pattern = Pattern.compile(s.toString());
				objectValuePatternCache.put(dotAll ? "dotAll:" + name : name, pattern);
			}
			else
			{
				pattern = objectValuePatternCache.get(dotAll ? "dotAll:" + name : name);
			}
			Matcher matcher = pattern.matcher(buffer);
			while(matcher.find())
			{
				int from = matcher.end();
				stringArray = ParserUtil.getArray(from, buffer);// Main array
				int to = buffer.indexOf(stringArray.toString()) + stringArray.length();
				buffer = buffer.delete(matcher.start(), to);
				matcher = pattern.matcher(buffer);
				stringArray = ParserUtil.removeBracetsAndCopy(stringArray);
				// Проверяем, может ли объект принять значение null
				if(canBeNull && ParserUtil.mayBeNullObject(stringArray))
				{
					if(!isList)
						break;
					else
						continue;
				}
				Object objectValueObject = objectFactory.createObjectFor(stringArray);
				ParserUtil.appendValueToField(field, object, objectValueObject);
				StringBuilder lostBuffer = parseClass(stringArray, clazz, objectValueObject);
				if(!isList)
					break;
			}
		}
		return buffer;
	}

	private static StringBuilder doLinkedValue(LinkedValue linkedValueAnnotation, StringBuilder buffer, Field field, Object object) throws IllegalAccessException, NoSuchFieldException
	{
		String name = linkedValueAnnotation.name().isEmpty() ? field.getName() : linkedValueAnnotation.name();
		boolean withoutName = linkedValueAnnotation.withoutName();
		boolean withoutBounds = linkedValueAnnotation.withoutBounds();
		LinkedType type = linkedValueAnnotation.LinkedType();
		Pattern pattern;
		if(withoutName)
		{
			if(withoutBounds)
			{
				pattern = stringValueWithoutNameWithoutBoundsPattern;
			}
			else
			{
				pattern = stringValueWithoutNamePattern;
			}
		}
		else
		{
			if(withoutBounds)
			{
				if(!stringValuePatternCache.containsKey(name))
				{
					StringBuilder s = new StringBuilder();
					s.append("\\b").append(name).append("\\s*?=\\s*?([\\S*]+)");
					pattern = Pattern.compile(s.toString(), Pattern.DOTALL);
					stringValuePatternCache.put(name, pattern);
				}
				else
				{
					pattern = stringValuePatternCache.get(name);
				}
			}
			else
			{
				if(!stringValueWithBoundsPatternCache.containsKey(name))
				{
					StringBuilder s = new StringBuilder();
					s.append("\\b").append(name).append("\\s*?=\\s*?\\[([\\S ]*?)]");
					pattern = Pattern.compile(s.toString(), Pattern.DOTALL);
					stringValueWithBoundsPatternCache.put(name, pattern);
				}
				else
				{
					pattern = stringValueWithBoundsPatternCache.get(name);
				}
			}
		}
		Matcher matcher = pattern.matcher(buffer);
		if(matcher.find())
		{
			String link = matcher.group(1).trim();
			Object param = 0;

			switch(type)
			{
				case linked_all:
					if(link.startsWith("@"))
					{
						String p = LinkerFactory.getInstance().findValueFor(link);
						if(p == null)
							throw new NoSuchFieldException(link);
						param = Long.parseLong(p);
					}
					else if(!NumberUtils.isParsable(link))
						param = LinkerFactory.getInstance().npcPchfindClearValue(link) - 1000000;
					else
						param = Long.parseLong(link);
					break;
				case linked_item:
					if(!NumberUtils.isParsable(link))
						param = LinkerFactory.getInstance().itemPchfindClearValue(link);
					else
						param = Integer.parseInt(link);
					break;
				case linked_option:
					if(!NumberUtils.isParsable(link))
						param = LinkerFactory.getInstance().optionPchfindClearValue(link);
					else
						param = Long.parseLong(link);
					break;
				case linked_npc:
					if(!NumberUtils.isParsable(link))
						param = LinkerFactory.getInstance().npcPchfindClearValue(link) - 1000000;
					else
						param = Long.parseLong(link);
					break;
				case linked_skill:
					if(link.startsWith("@"))
					{
						String p = LinkerFactory.getInstance().findValueFor(link);
						if(p == null)
							throw new NoSuchFieldException(link);
						param = Long.parseLong(p);
					}
					else
					{
						String p = LinkerFactory.getInstance().findValueFor("@" + link);
						if(p == null)
							throw new NoSuchFieldException(link);
						param = Long.parseLong(p);
					}
					break;
				default:
					break;
			}

			ParserUtil.appendValueToField(field, object, param);
			// Удаляем текущий элемент из буфера
			buffer = buffer.delete(matcher.start(), matcher.end());
		}
		return buffer;
	}

	private static final Pattern stringValueWithoutNamePattern = Pattern.compile("\\[([\\S ]*?)]", Pattern.DOTALL);
	private static final Pattern stringValueWithoutNameWithoutBoundsPattern = Pattern.compile("([^,;:\\s\\{}\\[\\]]+)", Pattern.DOTALL);
	private static Map<String, Pattern> stringValueWithBoundsPatternCache = new HashMap<>();
	private static Map<String, Pattern> stringValuePatternCache = new HashMap<>();

	private static StringBuilder doStringValue(StringValue stringValueAnnotation, StringBuilder buffer, Field field, Object object) throws IllegalAccessException
	{
		String name = stringValueAnnotation.name().isEmpty() ? field.getName() : stringValueAnnotation.name();
		boolean withoutName = stringValueAnnotation.withoutName();
		boolean withoutBounds = stringValueAnnotation.withoutBounds();
		Pattern pattern;
		if(withoutName)
		{
			if(withoutBounds)
			{
				pattern = stringValueWithoutNameWithoutBoundsPattern;
			}
			else
			{
				pattern = stringValueWithoutNamePattern;
			}
		}
		else
		{
			if(withoutBounds)
			{
				if(!stringValuePatternCache.containsKey(name))
				{
					StringBuilder s = new StringBuilder();
					s.append("\\b").append(name).append("\\s*?=\\s*?([\\S*]+)");
					pattern = Pattern.compile(s.toString(), Pattern.DOTALL);
					stringValuePatternCache.put(name, pattern);
				}
				else
				{
					pattern = stringValuePatternCache.get(name);
				}
			}
			else
			{
				if(!stringValueWithBoundsPatternCache.containsKey(name))
				{
					StringBuilder s = new StringBuilder();
					s.append("\\b").append(name).append("\\s*?=\\s*?\\[([\\S ]*?)]");
					pattern = Pattern.compile(s.toString(), Pattern.DOTALL);
					stringValueWithBoundsPatternCache.put(name, pattern);
				}
				else
				{
					pattern = stringValueWithBoundsPatternCache.get(name);
				}
			}
		}
		Matcher matcher = pattern.matcher(buffer);
		if(matcher.find())
		{
			String valueBuffer = matcher.group(1).trim();
			ParserUtil.appendValueToField(field, object, valueBuffer);
			// Удаляем текущий элемент из буфера
			buffer = buffer.delete(matcher.start(), matcher.end());
		}
		return buffer;
	}

	private static final Pattern intValueWithoutNamePattern = Pattern.compile("([\\d-]+)", Pattern.DOTALL);
	private static Map<String, Pattern> intValuePatternCache = new HashMap<>();

	private static StringBuilder doIntValue(IntValue intValueAnnotation, StringBuilder buffer, Field field, Object object) throws IllegalAccessException
	{
		String name = intValueAnnotation.name().isEmpty() ? field.getName() : intValueAnnotation.name();
		boolean withoutName = intValueAnnotation.withoutName();
		boolean inBrackets = intValueAnnotation.inBrackets();
		String[] replacements = intValueAnnotation.replacements();
		StringBuilder patternAdd = new StringBuilder();
		if(replacements.length > 0)
		{
			for(int i = 0; i < replacements.length; i += 2)
			{
				patternAdd.append("|").append(replacements[i]);
			}
		}
		Pattern pattern;
		if(withoutName)
		{
			pattern = intValueWithoutNamePattern;
		}
		else
		{
			if(patternAdd.length() == 0)
			{
				if(intValuePatternCache.containsKey(name))
				{
					pattern = intValuePatternCache.get(name);
				}
				else
				{
					StringBuilder s = new StringBuilder();
					if(inBrackets)
						s.append("\\b").append(name).append("\\s*?=\\s*?\\{([\\d-]+)\\}");
					else
						s.append("\\b").append(name).append("\\s*?=\\s*?([\\d-]+)");
					pattern = Pattern.compile(s.toString(), Pattern.DOTALL);
					intValuePatternCache.put(name, pattern);
				}
			}
			else
			{
				StringBuilder s = new StringBuilder();
				s.append("\\b").append(name).append("\\s*?=\\s*?([\\d-]+").append(patternAdd).append(")");
				pattern = Pattern.compile(s.toString(), Pattern.DOTALL);
			}
		}
		Matcher matcher = pattern.matcher(buffer);
		if(matcher.find())
		{
			String valueBuffer = matcher.group(1);
			if(replacements.length > 0)
			{
				for(int i = 0; i < replacements.length; i += 2)
				{
					valueBuffer = valueBuffer.replace(replacements[i], replacements[i + 1]);
				}
			}
			int intValue = Integer.valueOf(valueBuffer);
			ParserUtil.appendValueToField(field, object, intValue);
			// Удаляем текущий элемент из буфера
			buffer = buffer.delete(matcher.start(), matcher.end());
		}
		return buffer;
	}

	@SuppressWarnings("unused")
	private static StringBuilder doObjectArray(ObjectArray objectArrayAnnotation, StringBuilder buffer, Field field, Object object) throws Exception
	{
		boolean isList = List.class.isAssignableFrom(field.getType());
		Class<?> initialClass;
		Class<?> objectClass;
		if(isList)
		{
			ParameterizedType parameterizedType = (ParameterizedType) field.getGenericType();
			Class<?> listElementType = (Class<?>) parameterizedType.getActualTypeArguments()[0];
			if(listElementType.isArray())
				initialClass = listElementType.getComponentType();
			else
				initialClass = listElementType;
		}
		else
		{
			initialClass = field.getType().getComponentType();
		}
		objectClass = initialClass;
		if(objectClass == null)
			throw new Exception("field:" + field.getName() + " " + buffer.toString() + "Array requried, but not found.");
		while(objectClass.isArray())
		{
			objectClass = objectClass.getComponentType();
		}
		String name = objectArrayAnnotation.name().isEmpty() ? field.getName() : objectArrayAnnotation.name();
		boolean withoutName = objectArrayAnnotation.withoutName();
		boolean canBeNull = objectArrayAnnotation.canBeNull();
		String splitter = objectArrayAnnotation.splitter();

		IObjectFactory<?> objectFactory = objectArrayAnnotation.objectFactory().getDeclaredConstructor().newInstance();
		objectFactory.setFieldClass(objectClass);
		StringBuilder stringArray;
		Pattern pattern;
		if(withoutName)
		{
			stringArray = ParserUtil.getArray(0, buffer);// Main array
			int from = buffer.indexOf(stringArray.toString());
			int to = buffer.indexOf(stringArray.toString()) + stringArray.length();
			buffer = buffer.delete(from, to);
			if(canBeNull && ParserUtil.mayBeNullObject(stringArray))
				return buffer;
			int elementCounts = ParserUtil.getArrayCounts(stringArray, 2);
			Object[] array = (Object[]) Array.newInstance(initialClass, elementCounts);
			StringBuilder lostBuffer = fillObjectArray(array, stringArray, initialClass, objectFactory, canBeNull);
			ParserUtil.appendValueToField(field, object, array);
		}
		else
		{
			StringBuilder s = new StringBuilder();
			s.append("\\b").append(name).append("\\s*?=\\s*?");
			pattern = Pattern.compile(s.toString(), Pattern.DOTALL);
			Matcher matcher = pattern.matcher(buffer);
			while(matcher.find())
			{
				int searchArrayFrom = matcher.end();
				stringArray = ParserUtil.getArray(searchArrayFrom, buffer);// Main
				// array
				int replaceTo = buffer.indexOf(stringArray.toString(), searchArrayFrom) + stringArray.length();
				buffer = buffer.delete(matcher.start(), replaceTo);
				if(canBeNull && ParserUtil.mayBeNullObject(stringArray))
				{
					if(!isList)
						break;
					else
						continue;
				}
				int elementCounts = ParserUtil.getArrayCounts(stringArray, 2);
				Object[] array = (Object[]) Array.newInstance(initialClass, elementCounts);
				StringBuilder lostBuffer = fillObjectArray(array, stringArray, initialClass, objectFactory, canBeNull);
				ParserUtil.appendValueToField(field, object, array);
				if(!isList)
					break;
				else
					matcher = pattern.matcher(buffer);
			}
		}
		return buffer;
	}

	@SuppressWarnings({
			"unused", "rawtypes"
	})
	public static StringBuilder fillObjectArray(Object[] array, StringBuilder buffer, Class<?> initialClass, IObjectFactory<?> objectFactory, boolean canBeNull) throws Exception
	{
		int arrayLevel = 1;
		Class<?> tempClass = initialClass;
		while(tempClass != null && tempClass.isArray())
		{
			tempClass = tempClass.getComponentType();
			arrayLevel++;
		}
		buffer = ParserUtil.removeBracetsAndCopy(buffer);
		int elementCounts = ParserUtil.getArrayCounts(buffer, 1);
		for(int pos = 0; pos < elementCounts; pos++)
		{
			StringBuilder stringArray = ParserUtil.getArray(0, buffer);
			int from = buffer.indexOf(stringArray.toString());
			int to = from + stringArray.length();
			buffer = buffer.delete(from, to);
			if(arrayLevel == 1)
			{// Массив первого уровня
				StringBuilder tempBuffer = ParserUtil.removeBracetsAndCopy(stringArray);
				if(canBeNull && ParserUtil.mayBeNullObject(tempBuffer))
					continue;

				Object object = objectFactory.createObjectFor(tempBuffer);
				try
				{
					StringBuilder lostBuffer = parseClass(tempBuffer, object.getClass(), object);
				}
				catch(Exception e)
				{
					System.out.println(object.getClass() + "\n" + tempBuffer + " \n " + e);
				}
				array[pos] = object;
			}
			else
			{ // Дальнейший поиск вглубь
				if(canBeNull && ParserUtil.mayBeNullObject(stringArray))
					continue;
				int tempCount = ParserUtil.getArrayCounts(stringArray, 2);
				Class ii = initialClass.getComponentType();
				Object[] newArray = (Object[]) Array.newInstance(ii, tempCount);
				array[pos] = newArray;
				StringBuilder lostBuffer = fillObjectArray(newArray, stringArray, ii, objectFactory, canBeNull);
			}
		}
		return buffer;
	}

	private static final Pattern enumArrayWithoutNamePattern = Pattern.compile("\\{([\\w0-9_; ]*?)}", Pattern.DOTALL);

	@SuppressWarnings({
			"rawtypes", "unchecked"
	})
	private static StringBuilder doEnumArray(EnumArray enumArrayAnnotation, StringBuilder buffer, Field field, Object object) throws IllegalAccessException
	{
		boolean isList = List.class.isAssignableFrom(field.getType());
		String name = enumArrayAnnotation.name().isEmpty() ? field.getName() : enumArrayAnnotation.name();
		boolean withoutName = enumArrayAnnotation.withoutName();
		String splitter = enumArrayAnnotation.splitter();
		Class<? extends Enum> clazz;
		if(isList)
		{
			ParameterizedType parameterizedType = (ParameterizedType) field.getGenericType();
			clazz = (Class<? extends Enum>) ((Class<?>) parameterizedType.getActualTypeArguments()[0]).getComponentType();
		}
		else
		{
			clazz = (Class<? extends Enum>) field.getType().getComponentType();
		}
		Pattern pattern;
		if(withoutName)
		{
			pattern = enumArrayWithoutNamePattern;
		}
		else
		{
			pattern = Pattern.compile(String.format(enumArrayAnnotation.customRegex().isEmpty() ? "\\b%s\\s*?=\\s*?\\{([\\w0-9_; ]+?)}" : enumArrayAnnotation.customRegex(), name), Pattern.DOTALL);
		}
		Matcher matcher = pattern.matcher(buffer);
		while(matcher.find())
		{
			String elementBuffer = matcher.group(1).trim();
			if(enumArrayAnnotation.replaceChars().length > 0)
			{
				String regex = "[" + Pattern.quote(new String(enumArrayAnnotation.replaceChars())) + "]";
				elementBuffer = elementBuffer.replaceAll(regex, "");
			}

			String[] parts = elementBuffer.split(splitter);
			Enum[] array = (Enum[]) Array.newInstance(clazz, parts.length);
			for(int i = 0; i < parts.length; i++)
				array[i] = Enum.valueOf(clazz, parts[i].trim());
			ParserUtil.appendValueToField(field, object, array);
			// Удаляем текущий элемент из буфера
			buffer = buffer.delete(matcher.start(), matcher.end());
			if(!isList)
				break;
			else
				matcher = pattern.matcher(buffer);
		}
		return buffer;
	}

	private static final Pattern intArrayWithoutNamePattern = Pattern.compile("\\{([\\d+-; ]*?)}", Pattern.DOTALL);
	private static Map<String, Pattern> intArrayPatternCache = new HashMap<>();

	private static StringBuilder doIntArray(IntArray intArrayAnnotation, StringBuilder buffer, Field field, Object object) throws IllegalAccessException
	{
		boolean isList = List.class.isAssignableFrom(field.getType());
		boolean flatMode = intArrayAnnotation.flat() && isList;

		String name = intArrayAnnotation.name().isEmpty() ? field.getName() : intArrayAnnotation.name();
		boolean withoutName = intArrayAnnotation.withoutName();
		String splitter = intArrayAnnotation.splitter();
		String[] bounds = intArrayAnnotation.bounds();
		boolean canBeNull = intArrayAnnotation.canBeNull();
		Pattern pattern;
		if(withoutName)
		{
			if(bounds[0].equals("{") && bounds[1].equals("}"))
			{
				pattern = intArrayWithoutNamePattern;
			}
			else
			{
				pattern = Pattern.compile(String.format("%s([\\d+-; ]*?)%s", bounds[0], bounds[1]), Pattern.DOTALL);
			}
		}
		else
		{
			if(!intArrayPatternCache.containsKey(name))
			{
				StringBuilder s = new StringBuilder();
				s.append("\\b").append(name).append("\\s*?=\\s*?").append(bounds[0]).append("([\\d+-; ]*?)").append(bounds[1]);
				pattern = Pattern.compile(s.toString(), Pattern.DOTALL);
				intArrayPatternCache.put(name, pattern);
			}
			else
			{
				pattern = intArrayPatternCache.get(name);
			}
		}
		Matcher matcher = pattern.matcher(buffer);
		while(matcher.find())
		{
			String elementBuffer = matcher.group(1).trim();
			// Удаляем текущий элемент из буфера
			buffer = buffer.delete(matcher.start(), matcher.end());
			matcher = pattern.matcher(buffer);
			// Проверяем, может ли объект принять значение null
			if(canBeNull && ParserUtil.mayBeNullObject(elementBuffer))
			{
				if(!isList)
					break;
				else
					continue;
			}
			String[] parts = elementBuffer.split(splitter);

			if(flatMode)
			{
				for(String p : parts)
				{
					int val = Integer.parseInt(p.trim());
					ParserUtil.appendValueToField(field, object, val);
				}
				break;
			}
			else
			{
				int[] array = new int[parts.length];
				for(int i = 0; i < parts.length; i++)
					array[i] = Integer.parseInt(parts[i].trim());
				ParserUtil.appendValueToField(field, object, array);
				if(!isList)
					break;
			}
		}
		return buffer;
	}

	@SuppressWarnings("unused")
	private static StringBuilder doElementAnnotation(Element elementAnnotation, StringBuilder buffer, Field field, Object object) throws Exception
	{
		boolean isList = List.class.isAssignableFrom(field.getType());
		boolean isMap = Map.class.isAssignableFrom(field.getType());
		Class<?> clazz;
		if(isList || isMap)
		{
			ParameterizedType parameterizedType = (ParameterizedType) field.getGenericType();
			clazz = (Class<?>) parameterizedType.getActualTypeArguments()[isMap ? 1 : 0];
		}
		else
		{
			clazz = field.getType();
		}

		String startPoint = elementAnnotation.start();
		String endPoint = elementAnnotation.end();
		String keyField = elementAnnotation.keyField();

		IObjectFactory<?> objectFactory = elementAnnotation.objectFactory().getDeclaredConstructor().newInstance();
		objectFactory.setFieldClass(clazz);

		Pattern pattern = Pattern.compile(String.format("\\b%s(.*?)%s", startPoint, endPoint), Pattern.DOTALL);
		Matcher matcher = pattern.matcher(buffer);
		List<int[]> replacements = new ArrayList<>();

		while(matcher.find())
		{
			StringBuilder elementBuffer = new StringBuilder(matcher.group(1).trim());
			Object elementObject = objectFactory.createObjectFor(elementBuffer);

			StringBuilder lostBuffer = Parser.parseClass(elementBuffer, elementObject.getClass(), elementObject);

			if(isList)
			{
				ParserUtil.appendValueToField(field, object, elementObject);
			}
			else if(isMap)
			{
				Field keyFieldObj = clazz.getDeclaredField(keyField);
				keyFieldObj.setAccessible(true);
				Object key = keyFieldObj.get(elementObject);
				if(key == null)
					throw new IllegalArgumentException("Key field '" + keyField + "' is null for object: " + elementObject);

				@SuppressWarnings("unchecked")
				Map<Object, Object> map = (Map<Object, Object>) field.get(object);
				if(map == null)
				{
					map = new HashMap<>();
					field.set(object, map);
				}
				map.put(key, elementObject);
			}
			else
				ParserUtil.appendValueToField(field, object, elementObject);

			replacements.add(new int[] {
					matcher.start(), matcher.end()
			});
			if(!isList && !isMap)
				break;
		}

		buffer = ParserUtil.deleteFromBuffer(buffer, replacements);
		return buffer;
	}

	private static StringBuilder doElementArrayAnnotation(ElementArray elementArrayAnnotation, StringBuilder buffer, Field field, Object object) throws Exception
	{
		boolean isList = List.class.isAssignableFrom(field.getType());
		Class<?> clazz;
		if(isList)
		{
			ParameterizedType parameterizedType = (ParameterizedType) field.getGenericType();
			clazz = ((Class<?>) parameterizedType.getActualTypeArguments()[0]).getComponentType();
		}
		else
		{
			clazz = field.getType().getComponentType();
		}
		String startPoint = elementArrayAnnotation.start();
		String endPoint = elementArrayAnnotation.end();
		Pattern pattern = Pattern.compile(String.format("\\b%s(.*?)%s", startPoint, endPoint), Pattern.DOTALL);
		Matcher matcher = pattern.matcher(buffer);
		while(matcher.find())
		{
			StringBuilder elementBuffer = new StringBuilder(matcher.group(1).trim());
			String[] parts = elementBuffer.toString().split("\\n");
			Object[] array = (Object[]) Array.newInstance(clazz, parts.length);
			StringBuilder lostBuffer = new StringBuilder();
			for(int i = 0; i < array.length; i++)
			{
				Object elementObject = clazz.getDeclaredConstructor().newInstance();
				lostBuffer.append(Parser.parseClass(elementBuffer, clazz, elementObject));
				array[i] = elementObject;
			}
			ParserUtil.appendValueToField(field, object, array);
			// Удаляем текущий элемент из буфера
			buffer = buffer.delete(matcher.start(), matcher.end());
			if(!isList)
				break;
			else
				matcher = pattern.matcher(buffer);
		}
		return buffer;
	}

	public static void printStatistic()
	{
		StringBuilder s = new StringBuilder();
		s.append("\n");
		s.append("doElementAnnotationWorkTime(): ").append(TimeUnit.NANOSECONDS.toMillis(doElementAnnotationWorkTime) / 1000D).append(" sec.\n");
		s.append("doElementArrayAnnotationWorkTime(): ").append(TimeUnit.NANOSECONDS.toMillis(doElementArrayAnnotationWorkTime)
				/ 1000D).append(" sec.\n");
		s.append("doIntValueWorkTime(): ").append(TimeUnit.NANOSECONDS.toMillis(doIntValueWorkTime) / 1000D).append(" sec.\n");
		s.append("doLongValueWorkTime(): ").append(TimeUnit.NANOSECONDS.toMillis(doLongValueWorkTime) / 1000D).append(" sec.\n");
		s.append("doDoubleValueWorkTime(): ").append(TimeUnit.NANOSECONDS.toMillis(doDoubleValueWorkTime) / 1000D).append(" sec.\n");
		s.append("doStringValueWorkTime(): ").append(TimeUnit.NANOSECONDS.toMillis(doStringValueWorkTime) / 1000D).append(" sec.\n");
		s.append("doEnumValueWorkTime(): ").append(TimeUnit.NANOSECONDS.toMillis(doEnumValueWorkTime) / 1000D).append(" sec.\n");
		s.append("doObjectValueWorkTime(): ").append(TimeUnit.NANOSECONDS.toMillis(doObjectValueWorkTime) / 1000D).append(" sec.\n");
		s.append("doTimeValueWorkTime(): ").append(TimeUnit.NANOSECONDS.toMillis(doTimeValueWorkTime) / 1000D).append(" sec.\n");
		s.append("doDateValueWorkTime(): ").append(TimeUnit.NANOSECONDS.toMillis(doDateValueWorkTime) / 1000D).append(" sec.\n");
		s.append("doIntArrayWorkTime(): ").append(TimeUnit.NANOSECONDS.toMillis(doIntArrayWorkTime) / 1000D).append(" sec.\n");
		s.append("doDoubleArrayWorkTime(): ").append(TimeUnit.NANOSECONDS.toMillis(doDoubleArrayWorkTime) / 1000D).append(" sec.\n");
		s.append("doStringArrayWorkTime(): ").append(TimeUnit.NANOSECONDS.toMillis(doStringArrayWorkTime) / 1000D).append(" sec.\n");
		s.append("doEnumArrayWorkTime(): ").append(TimeUnit.NANOSECONDS.toMillis(doEnumArrayWorkTime) / 1000D).append(" sec.\n");
		s.append("doObjectArrayWorkTime(): ").append(TimeUnit.NANOSECONDS.toMillis(doObjectArrayWorkTime) / 1000D).append(" sec.\n");
		s.append("doLinkedArrayWorkTime(): ").append(TimeUnit.NANOSECONDS.toMillis(doLinkedArrayWorkTime) / 1000D).append(" sec.\n");
		LOGGER.info("Parser statistic\n{}", s);
		doElementAnnotationWorkTime = 0;
		doElementArrayAnnotationWorkTime = 0;
		doIntValueWorkTime = 0;
		doLongValueWorkTime = 0;
		doDoubleValueWorkTime = 0;
		doStringValueWorkTime = 0;
		doEnumValueWorkTime = 0;
		doObjectValueWorkTime = 0;
		doTimeValueWorkTime = 0;
		doDateValueWorkTime = 0;
		doIntArrayWorkTime = 0;
		doDoubleArrayWorkTime = 0;
		doStringArrayWorkTime = 0;
		doEnumArrayWorkTime = 0;
		doObjectArrayWorkTime = 0;
		doLinkedArrayWorkTime = 0;
	}

	public static void clear()
	{
		stringArrayPatternCache.clear();
		enumValuePatternCache.clear();
		objectValuePatternCache.clear();
		stringValuePatternCache.clear();
		stringValueWithBoundsPatternCache.clear();
		intValuePatternCache.clear();
		longValuePatternCache.clear();
		intArrayPatternCache.clear();
		doubleArrayPatternCache.clear();
	}
}