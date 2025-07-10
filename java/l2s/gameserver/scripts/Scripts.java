package l2s.gameserver.scripts;

import java.io.File;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.lang3.ClassUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import l2s.commons.compiler.Compiler;
import l2s.commons.compiler.MemoryClassLoader;
import l2s.commons.listener.Listener;
import l2s.commons.listener.ListenerList;
import l2s.gameserver.Config;
import l2s.gameserver.handler.bypass.Bypass;
import l2s.gameserver.handler.bypass.BypassHolder;
import l2s.gameserver.listener.script.OnInitScriptListener;
import l2s.gameserver.listener.script.OnLoadScriptListener;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.scripts.annotation.OnScriptInit;
import l2s.gameserver.scripts.annotation.OnScriptLoad;

/**
 * Min rework by Hl4p3x
 * Script for loading datapack scripts.
 */
public class Scripts
{
	private static final Logger LOGGER = LoggerFactory.getLogger(Scripts.class);

	private final Map<String, Class<?>> _classes = new TreeMap<String, Class<?>>();
	private final Map<Class<?>, Object> _instances = new ConcurrentHashMap<Class<?>, Object>();
	private final ScriptListenerImpl _listeners = new ScriptListenerImpl();

	public Scripts()
	{
		load();
	}

	protected void load()
	{
		LOGGER.info("Scripts: Loading...");

		final List<Class<?>> classes = load(new File(Config.DATAPACK_ROOT, "data/scripts"));
		if (classes.isEmpty())
		{
			throw new Error("Failed loading scripts!");
		}

		for (Class<?> clazz : classes)
		{
			_classes.put(clazz.getName(), clazz);
		}

		LOGGER.info("Scripts: Loaded {} classes.", _classes.size());
		for (Class<?> clazz : _classes.values())
		{
			try
			{
				Object o = getClassInstance(clazz);
				if (ClassUtils.isAssignable(clazz, OnLoadScriptListener.class))
				{
					if (o == null)
						o = clazz.getDeclaredConstructor().newInstance();

					_listeners.add((OnLoadScriptListener) o);
				}

				for (Method method : clazz.getMethods())
				{
					if (method.isAnnotationPresent(OnScriptLoad.class))
					{
						Class<?>[] par = method.getParameterTypes();
						if (par.length != 0)
						{
							LOGGER.error("Wrong parameters for load method: {}, class: {}", method.getName(), clazz.getSimpleName());
							continue;
						}

						try
						{
							if (Modifier.isStatic(method.getModifiers()))
							{
								method.invoke(clazz);
							}
							else
							{
								if (o == null)
								{
									o = clazz.getDeclaredConstructor().newInstance();
								}
								method.invoke(o);
							}
						}
						catch (Exception e)
						{
							LOGGER.error("Exception: {}", e);
						}
					}
				}
			}
			catch (Exception e)
			{
				LOGGER.error("", e);
			}
		}

		_listeners.load();
	}

	public void init()
	{
		for (Class<?> clazz : _classes.values())
		{
			init(clazz);
		}
		_listeners.init();
	}

	public List<Class<?>> load(File target)
	{
		Collection<File> scriptFiles = Collections.emptyList();
		final List<Class<?>> classes = new ArrayList<Class<?>>();
		final Compiler compiler = new Compiler();

		if (target.isFile())
		{
			scriptFiles = new ArrayList<File>(1);
			scriptFiles.add(target);
		}
		else if (target.isDirectory())
		{
			scriptFiles = FileUtils.listFiles(target, FileFilterUtils.suffixFileFilter(".java"), FileFilterUtils.directoryFileFilter());
		}

		if (scriptFiles.isEmpty())
		{
			return Collections.emptyList();
		}

		if (compiler.compile(scriptFiles))
		{
			MemoryClassLoader classLoader = compiler.getClassLoader();
			for (String name : classLoader.getLoadedClasses())
			{
				if (name.contains(ClassUtils.INNER_CLASS_SEPARATOR))
				{
					continue;
				}

				try
				{
					Class<?> clazz = classLoader.loadClass(name);
					if (Modifier.isAbstract(clazz.getModifiers()))
					{
						continue;
					}
					classes.add(clazz);
				}
				catch (ClassNotFoundException e)
				{
					LOGGER.error("Scripts: Can't load script class: {}", name, e);
					classes.clear();
					break;
				}
			}
		}

		return classes;
	}

	private Object init(Class<?> clazz)
	{
		Object o = getClassInstance(clazz);
		try
		{
			if (ClassUtils.isAssignable(clazz, OnInitScriptListener.class))
			{
				if (o == null)
				{
					o = clazz.getDeclaredConstructor().newInstance();
				}
				_listeners.add((OnInitScriptListener) o);
			}

			for (Method method : clazz.getMethods())
			{
				if (method.isAnnotationPresent(Bypass.class))
				{
					Class<?>[] par = method.getParameterTypes();
					if (par.length == 0 || par[0] != Player.class || par[1] != NpcInstance.class || par[2] != String[].class)
					{
						LOGGER.error("Wrong parameters for bypass method: {}, class: {}", method.getName(), clazz.getSimpleName());
						continue;
					}

					Bypass an = method.getAnnotation(Bypass.class);
					if (Modifier.isStatic(method.getModifiers()))
					{
						BypassHolder.getInstance().registerBypass(an.value(), clazz, method);
					}
					else
					{
						if (o == null)
						{
							o = clazz.getDeclaredConstructor().newInstance();
						}
					}
					BypassHolder.getInstance().registerBypass(an.value(), o, method);
				}
				else if (method.isAnnotationPresent(OnScriptInit.class))
				{
					Class<?>[] par = method.getParameterTypes();
					if (par.length != 0)
					{
						LOGGER.error("Wrong parameters for init method: {}, class: {}", method.getName(), clazz.getSimpleName());
						continue;
					}

					try
					{
						if (Modifier.isStatic(method.getModifiers()))
						{
							method.invoke(clazz);
						}
						else
						{
							if (o == null)
							{
								o = clazz.getDeclaredConstructor().newInstance();
							}
							method.invoke(o);
						}
					}
					catch (Exception e)
					{
						LOGGER.error("Exception: {}", e, e);
					}
				}
			}
		}
		catch (Exception e)
		{
			LOGGER.error("", e);
		}
		return o;
	}

	public Map<String, Class<?>> getClasses()
	{
		return _classes;
	}

	public Object getClassInstance(Class<?> clazz)
	{
		return _instances.get(clazz);
	}

	public Object getClassInstance(String className)
	{
		Class<?> classes = _classes.get(className);
		if (classes != null)
		{
			return getClassInstance(classes);
		}
		return null;
	}

	public class ScriptListenerImpl extends ListenerList<Scripts>
	{
		public void load()
		{
			for (Listener<Scripts> listener : getListeners())
			{
				if (OnLoadScriptListener.class.isInstance(listener))
				{
					((OnLoadScriptListener) listener).onLoad();
				}
			}
		}

		public void init()
		{
			for (Listener<Scripts> listener : getListeners())
			{
				if (OnInitScriptListener.class.isInstance(listener))
				{
					((OnInitScriptListener) listener).onInit();
				}
			}
		}
	}

	/**
	 * @return the only instance of this class.
	 */
	public static Scripts getInstance()
	{
		return SingletonHolder.INSTANCE;
	}

	/**
	 * Singleton holder for the class.
	 */
	private static class SingletonHolder
	{
		protected static final Scripts INSTANCE = new Scripts();
	}
}