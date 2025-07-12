package l2s.dataparser.data.annotations.factory;

import java.lang.reflect.Constructor;

/**
 * @author : Camelion
 * @date : 24.08.12  22:57
 */
/**
 * Реализация фабрики по умолчанию (всегда используется тип аннотированного
 * поля)
 */
public class DefaultFactory implements IObjectFactory<Object>
{
	private Class<?> clazz;

	/**
	 * Устанавливает класс, из которого будут создаваться объекты. Данный метод
	 * гарантировано вызывается перед createObjectFor, если используется
	 * NoneFactory
	 *
	 * @param clazz
	 */
	@Override
	public void setFieldClass(Class<?> clazz)
	{
		this.clazz = clazz;
	}

  @Override
  public Object createObjectFor(StringBuilder data) throws IllegalAccessException, InstantiationException
  {
      try
      {
          Constructor<?> constructor = clazz.getDeclaredConstructor();
          return constructor.newInstance();
      }
      catch (Exception e)
      {
          throw new IllegalAccessException("Error creating object for class " + clazz.getName() + ": " + e.getMessage());
      }
  }
}