package com.gz.mozixing.utils;

import com.gz.mozixing.Constant;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * map按字母排序
 *
 * @author Alex
 * @since 19/1/30
 */
public class MapSortUtil {
    private static MapSortUtil mapSortUtil;

    public static MapSortUtil getInstance() {
        if (mapSortUtil == null) {
            mapSortUtil = new MapSortUtil();
        }
        return mapSortUtil;
    }

    /**
     * @Title: sortMap
     * @Description: 对集合内的数据按key的字母顺序做排序
     */
    public List<Map.Entry<String, String>> sortMap(final Map<String, String> map) {
        final List<Map.Entry<String, String>> infos = new ArrayList<Map.Entry<String, String>>(map.entrySet());

        // 重写集合的排序方法：按字母顺序
        Collections.sort(infos, new Comparator<Map.Entry<String, String>>() {
            @Override
            public int compare(final Map.Entry<String, String> o1, final Map.Entry<String, String> o2) {

                return (o1.getKey().toString().compareTo(o2.getKey()));
            }
        });


        return infos;
    }

    //
    public String getNetWorkMd5String(final Map<String, String> map, final String time) {
        List<Map.Entry<String, String>> list = new MapSortUtil().sortMap(map);
        StringBuilder stringBuilder = new StringBuilder();
        for (final Map.Entry<String, String> m : list) {
            stringBuilder.append(m.getValue());
        }
        stringBuilder.append(time);
        stringBuilder.append(Constant.md5Key);

        return Constant.MD5(stringBuilder.toString());
    }

    public String getNetWorkMd5String(final String time) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(time);
        stringBuilder.append(Constant.md5Key);

        return Constant.MD5(stringBuilder.toString());
    }
}
