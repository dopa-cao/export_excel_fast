package cn.com.clm.util.reflection;

import cn.com.clm.util.annotation.RowIndex;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.TreeMap;

/**
 * describe: row parse
 *
 * @author liming.cao
 */
public class RowParseUtil {

    private RowParseUtil() {
    }

    public static Map<Integer, Field> parser(Class cls) {
        Map<Integer, Field> map = new TreeMap<>();
        if (null == cls) {
            return map;
        }
        Field[] fields = cls.getDeclaredFields();
        for (Field field : fields) {
            RowIndex rowIndex = field.getAnnotation(RowIndex.class);
            if (null != rowIndex) {
                field.setAccessible(Boolean.TRUE);
                map.put(rowIndex.value(), field);
            }
        }
        return map;
    }


}
