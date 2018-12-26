package cn.com.clm.util;

import cn.com.clm.param.People;
import com.fasterxml.uuid.Generators;

import java.util.*;

/**
 * describe: data util
 *
 * @author liming
 */
public class DataUtil {

    /**
     * mock data
     *
     * @return  data
     */
    public static List<People> buildList() {
        List<People> peoples = new ArrayList<>(20);
        for (int i = 0; i < 20; i++) {
            People people = new People();
            people.setId(i + 1L);
            people.setUuid(uuid());
            people.setAddress("常山市白瓜阵六期");
            people.setBirth(getDate(new Date(),i));
            people.setEmail("13231"+ i + "@qq.com");
            people.setName("张丹丹" + i);
            people.setSex(i % 2);
            peoples.add(people);
        }
        return peoples;
    }

    /**
     * get date
     *
     * @param date  date
     * @param count count
     * @return      result
     */
    private static Date getDate(Date date, int count) {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, - count);
        return calendar.getTime();
    }


    /**
     * create uuid
     *
     * @return  result
     */
    public static String uuid() {
        UUID uuid = Generators.timeBasedGenerator().generate();
        return uuid.toString().replace("-", "");
    }

}
