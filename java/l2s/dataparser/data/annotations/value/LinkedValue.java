package l2s.dataparser.data.annotations.value;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import l2s.dataparser.data.common.LinkedType;

/**
 * @author : 4ipolino
 */
@Target(ElementType.FIELD)
// Target field
@Retention(RetentionPolicy.RUNTIME)
public @interface LinkedValue {
	/**
	 * Имя, по которому будет произведен поиск переменной в буфере, если
	 * withoutName() == false Если не указано - подставляется имя
	 * аннотированного поля
	 *
	 * @return - имя переменной в буфере
	 */
	String name() default "";

	/**
	 * Если true - поиск будет произведен по шаблону \[(\S*?)] Если false -
	 * поиск будет произведен по шаблону name + "\s*=\s*\[(\S*?)]"
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

	LinkedType LinkedType() default LinkedType.linked_all;
}
