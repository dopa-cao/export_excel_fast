package cn.com.clm.util.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * describe: custom row index
 *
 * @author liming.cao
 */
@Target(value = {ElementType.FIELD})
@Retention(value = RetentionPolicy.RUNTIME)
public @interface RowIndex {

    int value() default 0;

    String header() default "";

    Class<?> type() default String.class;

    String nullValue() default "";

}
