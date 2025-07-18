package l2s.dataparser.data.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import l2s.dataparser.data.annotations.factory.DefaultFactory;
import l2s.dataparser.data.annotations.factory.IObjectFactory;

/**
 * @author : Camelion
 * @date : 22.08.12 2:09
 *       <p/>
 *       Показывает парсеру, что поле класса должно считываться как элемент.
 *       <p/>
 *       Классы переменных, помеченных аттрибутом @Element: 1) не должны быть
 *       вложенными, либо должны иметь модификаторы public static 2) должны
 *       иметь конструктор без аргументов
 *       <p/>
 *       Если поле класса имеет тип List<E>, то этот список будет заполняться
 *       элементами типа E
 */
@Target(ElementType.FIELD)
// Target field
@Retention(RetentionPolicy.RUNTIME)
public @interface Element {
	/**
	 * @return - строка - индикатор начала элемента
	 */
	String start();

	/**
	 * @return - строка - индикатор окончания элемента
	 */
	String end();

	/**
	 * Возвращает класс-фабрику, которая управляет созданием Element-объектов.
	 * Позволяет указать в качестве типа поля - какой-нибудь супер-класс, а
	 * управление объектами повесить на данную фабрику.
	 * <p/>
	 * Необходима при создании сложных структур
	 *
	 * @return класс-фабрика, создающая объекты нужного типа.
	 */
	@SuppressWarnings("rawtypes")
	Class<? extends IObjectFactory> objectFactory() default DefaultFactory.class;

	/**
	 * Указывает имя поля объекта, которое будет использоваться в качестве ключа
	 * для Map. Если не указано, используется List.
	 */
	String keyField() default "";
}
