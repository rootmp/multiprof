package l2s.dataparser.data.annotations.value;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author : Camelion
 * @date : 22.08.12 19:03
 *       <p/>
 *       Если поле класса имеет тип List<E extends Enum> то этот список будет
 *       заполняться элементами типа E
 */
@Target(ElementType.FIELD)
// Target field
@Retention(RetentionPolicy.RUNTIME)
public @interface EnumValue {
	/**
	 * Имя, по которому будет произведен поиск переменной в буфере, если
	 * withoutName() == false Если не указано - подставляется имя
	 * аннотированного поля
	 *
	 * @return - имя переменной в буфере
	 */
	String name() default "";

	/**
	 * Если true - поиск будет произведен по шаблону ([\w\d_]+) Если false -
	 * поиск будет произведен по шаблону name + "\s*=\s*([\w\d_]+)"
	 *
	 * @return true - поиск по шаблону, false - поиск по имени
	 */
	boolean withoutName() default false;

	/**
	 * <p/>
	 * Если true - в поиск строкового значения не будут включены символы [] Если
	 * false - поиск будет произведен по стандартному шаблону
	 *
	 * @return true - поиск без [], false - стандартный поиск
	 */
	boolean withoutBounds() default false;

	char[] replaceChars() default {};

	String customRegex() default "";

	String customRegexNoCache() default "";

	boolean log() default false;
}
